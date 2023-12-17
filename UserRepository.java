/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.facom39701.riakapp1.repository;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.FetchValue.Response;
import com.basho.riak.client.api.commands.kv.ListKeys;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.facom39701.riakapp1.model.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author docker
 */
public class UserRepository {
    
    private final RiakClient client;
    private final Namespace bucket;
    
    public UserRepository(final RiakClient client) {
        this.client = client;
        this.bucket = new Namespace ("Users");
    }
    
    public void save(final User user)
        throws ExecutionException,
        InterruptedException {
        
            final Location loc = new Location (this.bucket, user.getName());
            final StoreValue storeOp = new StoreValue.Builder(user)
                .withLocation(loc)
                .build();
            client.execute (storeOp);
            System.out.println("User saved!");
            }

    public User get(final String name)
        throws UnresolvedConflictException ,
            ExecutionException ,
            InterruptedException {

                final Location loc = new Location(this.bucket, name);
                final FetchValue fetchOp = new FetchValue.Builder(loc)
                .build ();
                final Response response = client.execute(fetchOp);
                
                System.out.println("User retrieved");
                return response.getValue(User.class);
                }
    
    public List<String> list() throws ExecutionException, InterruptedException {
        final ListKeys listOp = new ListKeys.Builder(this.bucket).build();
        final ListKeys.Response response = client.execute(listOp);
        Iterator<Location> iterator = response.iterator();

        List<String> keys = new ArrayList<>();
        while(iterator.hasNext()) 
            keys.add(iterator.next().getKeyAsString());

        return keys;}
}
