package ru.pooh.services;

import ru.pooh.HttpResponseCodes;
import ru.pooh.Req;
import ru.pooh.Resp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>> users = new ConcurrentHashMap<>();

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
        String topicName = req.text().split("/")[2];
        String value = req.message();
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        clq.offer(value);
        ConcurrentLinkedQueue<String> tmp = topic.putIfAbsent(topicName, clq);
        if (tmp != null) { // Здесь нужно synchronized блок?
            tmp.add(value);
        }
        return new Resp(HttpResponseCodes.OK.toString()
                ,HttpResponseCodes.OK.get());
    }

    private Resp get(Req req) {
        String topicName = req.text().split("/")[2];
        int id = Integer.parseInt(req.text().split("/")[3]);
        ConcurrentLinkedQueue<String> tmp = topic.get(topicName);
        if (tmp == null) {
            return new Resp(HttpResponseCodes.BadRequest.toString()
                    , HttpResponseCodes.BadRequest.get());
        }
        users.putIfAbsent(id, tmp);

        String message = users.get(id).poll();

        if (message == null) {
            return new Resp(HttpResponseCodes.InternalServerError.toString(),
                    HttpResponseCodes.InternalServerError.get());
        }

        return new Resp(message, HttpResponseCodes.OK.get());

    }

}
