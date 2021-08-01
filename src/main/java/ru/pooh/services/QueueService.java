package ru.pooh.services;

import ru.pooh.Req;
import ru.pooh.Resp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (req.method().equals("POST")) {
            return get(req);
        }
        if (req.method().equals("GET")) {
            return get(req);
        }
        return null; // throw new IllegalRequestException??
    }

    private Resp get(Req req) {
        String queueName = req.text().split("/")[1];
        String value = req.message();
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<String>();
        clq.offer(value);
        queue.putIfAbsent(queueName, clq);
        return new Resp("", -1);
    }

    private Resp post() {
        return null;
    }
}