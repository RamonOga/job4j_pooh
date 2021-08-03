package ru.pooh.services;

import ru.pooh.HttpResponseCodes;
import ru.pooh.Req;
import ru.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> users = new ConcurrentHashMap<>();

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
        fillUsersQueues(topicName, value);
        return new Resp(HttpResponseCodes.OK.toString()
                ,HttpResponseCodes.OK.get());
    }

    private Resp get(Req req) {
        String topicName = req.text().split("/")[2];
        int id = Integer.parseInt(req.text().split("/")[3]);

        Map<String, ConcurrentLinkedQueue<String>> userMap = users.get(id);
        if (userMap == null) {
            users.put(id, new ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>());
            userMap = users.get(id);
        }
        ConcurrentLinkedQueue<String> userTopicQueueFromYourMap = userMap.get(topicName);

        if (userTopicQueueFromYourMap == null) {
            ConcurrentLinkedQueue<String> TopicQueueFromSharedMap
                    = topic.get(topicName);
            if (TopicQueueFromSharedMap == null) {
                return new Resp(HttpResponseCodes.BadRequest.toString(),
                        HttpResponseCodes.BadRequest.get());
            }
            userMap.putIfAbsent(topicName, TopicQueueFromSharedMap);
            userTopicQueueFromYourMap = userMap.get(topicName);
        }

        String message = userTopicQueueFromYourMap.poll();

        if (message == null) {
            return new Resp(HttpResponseCodes.InternalServerError.toString(),
                    HttpResponseCodes.InternalServerError.get());
        }
        return new Resp(message, HttpResponseCodes.OK.get());
    }

    private void fillUsersQueues(String topicName, String message) {
        for (Integer id : users.keySet()) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMap = users.get(id);
            ConcurrentLinkedQueue<String> userTopic = userMap.get(topicName);
            if (userTopic == null) {
                return;
            } else {
                userTopic.offer(message);
            }
        }
    }

}
