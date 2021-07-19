package ru.job4j.lasttask.services;

import ru.job4j.lasttask.Req;
import ru.job4j.lasttask.Resp;

public interface Service {
    Resp process(Req req);
}
