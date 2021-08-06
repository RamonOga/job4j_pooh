package ru.pooh.services;

import ru.pooh.HttpResponseCodes;
import ru.pooh.Req;
import ru.pooh.Resp;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (req.method().equals("POST")) {
            return post(req);
        }
        if (req.method().equals("GET")) {
            return get(req);
        }
        return new Resp(HttpResponseCodes.BadRequest.toString()
                , HttpResponseCodes.BadRequest.get() );
    }

    private Resp post(Req req) {
        String queueName = req.text().split("/")[2];
        String value = req.message();

        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        clq.offer(value);
        ConcurrentLinkedQueue<String> tmp = queue.putIfAbsent(queueName, clq);
        if (tmp != null) { // Здесь нужно synchronized блок?
            tmp.add(value);
        }
        return new Resp(HttpResponseCodes.OK.toString()
                ,HttpResponseCodes.OK.get());
    }

    private Resp get(Req req) {
        String queueName = req.text().split("/")[2];
        ConcurrentLinkedQueue<String> tmp = queue.get(queueName);
        if (tmp == null) {
            return new Resp(HttpResponseCodes.BadRequest.toString()
                    , HttpResponseCodes.BadRequest.get());
        }

        String message = tmp.poll();

        if (message == null) {
            return new Resp(HttpResponseCodes.InternalServerError.toString(),
                    HttpResponseCodes.InternalServerError.get());
        }

        return new Resp(message
                , HttpResponseCodes.OK.get());
    }
}