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
import com.facom39701.riakapp1.model.Timeline;
import com.facom39701.riakapp1.model.TimelineType;
import com.facom39701.riakapp1.util.DateUtil;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author docker
 */
public class TimelineRepository {

    private static String generateKey(final String ownerName,
            final TimelineType type,
            final String dateString) {
        return ownerName + "_" + type.name() + "_" + dateString;
    }
    private final RiakClient client;
    private final Namespace bucket;
    private final MessageRepository msgRepo;

    public TimelineRepository(final RiakClient client) {
        this.client = client;
        this.bucket = new Namespace(" Timelines ");
        this.msgRepo = new MessageRepository(this.client);
    }

    public void postMessage(final Message msg)
            throws ExecutionException,
            InterruptedException {
        final String msgKey = msgRepo.save(msg);
        addToTimeline(msg, TimelineType.INBOX, msgKey);
        addToTimeline(msg, TimelineType.SENT, msgKey);
        System.out.println(" Message saved to both timelines ");
    }

    public Timeline getTimeline(final String ownerName,
            final TimelineType type,
            final Date date)
            throws UnresolvedConflictException,
            ExecutionException,
            InterruptedException {
        final String timelineKey = generateKey(
                ownerName,
                type,
                DateUtil.getDateStrFromDate(date));
        final Location loc = new Location(this.bucket, timelineKey);
        final FetchValue fetchOp = new FetchValue.Builder(loc)
                .build();
        final Response response = client.execute(fetchOp);
        System.out.println(" Timeline retrieved ");
        return response.getValue(Timeline.class);
    }

    private void addToTimeline(final Message msg,
            final TimelineType type,
            final String msgKey)
            throws UnresolvedConflictException,
            ExecutionException,
            InterruptedException {
        final String owner = (type == TimelineType.INBOX)
                ? msg.getRecipient()
                : msg.getSender();
        final String timelineKey = generateKey(
                owner,
                type,
                DateUtil.getDateStrFromTimestampStr(msg.getCreatedAt()));
        final Location loc = new Location(this.bucket, timelineKey);
        final FetchValue fetchOp = new FetchValue.Builder(loc)
                .build();
        final Response response = client.execute(fetchOp);
        Timeline timeline = response.getValue(Timeline.class);
        if (timeline == null) {
            timeline = new Timeline(owner, type);
        }
        timeline.getMessages().add(msgKey);
        final StoreValue store = new StoreValue.Builder(timeline)
                .withLocation(loc)
                .build();
        client.execute(store);
    }
}
