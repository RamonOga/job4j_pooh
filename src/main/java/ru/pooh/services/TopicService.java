package ru.pooh.services;

import org.apache.logging.log4j.Logger;
import ru.pooh.HttpResponseCodes;
import ru.pooh.Req;
import ru.pooh.Resp;
import ru.pooh.logger.LogCreator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> users = new ConcurrentHashMap<>();

    static private final Logger LOG = LogCreator.getLOG();


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

        try {
            topic.putIfAbsent(topicName, new ConcurrentLinkedQueue<>());
            topic.get(topicName).add(value);

            fillUsersQueues(topicName, value);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new Resp(HttpResponseCodes.InternalServerError.toString(),
                    HttpResponseCodes.InternalServerError.get());
        }

        return new Resp(HttpResponseCodes.OK.toString()
                ,HttpResponseCodes.OK.get());
    }

    private Resp get(Req req) {
        String topicName = req.text().split("/")[2];
        int id = Integer.parseInt(req.text().split("/")[3]);
        String message;
        try {
            users.putIfAbsent(id, new ConcurrentHashMap<>());
            Map<String, ConcurrentLinkedQueue<String>> userMap = users.get(id);
            userMap.putIfAbsent(topicName, topic.get(topicName));
             message = userMap.get(topicName).poll();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new Resp(HttpResponseCodes.InternalServerError.toString(),
                    HttpResponseCodes.InternalServerError.get());
        }


        return new Resp(message, HttpResponseCodes.OK.get());
    }

    private void fillUsersQueues(String topicName, String message) {
        for (Integer id : users.keySet()) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMap = users.get(id);
            ConcurrentLinkedQueue<String> userTopic = userMap.get(topicName);
            userTopic.offer(message);
        }
    }

}
