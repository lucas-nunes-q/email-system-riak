/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.facom39701.riakapp1.repository;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.FetchValue.Response;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.facom39701.riakapp1.model.Message;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author docker
 */
public class MessageRepository {

    private static String generateKey(final Message msg) {
        return msg.getSender() + "_" + msg.getCreatedAt();
    }
    private final RiakClient client;
    private final Namespace bucket;

    public MessageRepository(final RiakClient client) {
        this.client = client;
        this.bucket = new Namespace(" Messages ");
    }

    public String save(final Message msg)
            throws ExecutionException,
            InterruptedException {
        final String msgKey = generateKey(msg);
        final Location loc = new Location(this.bucket, msgKey);
        final StoreValue storeOp = new StoreValue.Builder(msg)
                .withLocation(loc)
                .build();
        client.execute(storeOp);
        System.out.println(" Message saved !");
        return msgKey;
    }

    public Message get(final String msgKey)
            throws UnresolvedConflictException,
            ExecutionException,
            InterruptedException {
        final Location loc = new Location(this.bucket, msgKey);
        final FetchValue fetchOp = new FetchValue.Builder(loc)
                .build();
        final Response response = client.execute(fetchOp);
        System.out.println(" Message retrieved ");
        return response.getValue(Message.class);
    }
}
