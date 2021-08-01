package ru.pooh.services;

import ru.pooh.Req;
import ru.pooh.Resp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        return null;
    }
}
