package ru.pooh.services;

import ru.pooh.HttpResponseCodes;
import ru.pooh.Req;
import ru.pooh.Resp;
import ru.pooh.logger.LogCreator;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.Logger;
public class QueueService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    static private final Logger LOG = LogCreator.getLOG();

    @Override
    public Resp process(Req req) {
        if (req.method().equals("POST")) {
            return post(req);
        }
        if (req.method().equals("GET")) {
            return get(req);
        }
        return new Resp(HttpResponseCodes.BadRequest.toString(),
                HttpResponseCodes.BadRequest.get() );
    }

    private Resp post(Req req) {
        String queueName = req.text().split("/")[2];
        String value = req.message();
        queue.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
        queue.get(queueName).add(value);
        return new Resp(HttpResponseCodes.OK.toString()
                ,HttpResponseCodes.OK.get());
    }

    private Resp get(Req req) {
        String message = null;
        try {
            String queueName = req.text().split("/")[2];
            ConcurrentLinkedQueue<String> tmp = queue.get(queueName);
            message = tmp.poll();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new Resp(HttpResponseCodes.InternalServerError.toString(),
                    HttpResponseCodes.InternalServerError.get());
        }

        return new Resp(message
                , HttpResponseCodes.OK.get());
    }
}