package ru.pooh.services;

import ru.pooh.Req;
import ru.pooh.Resp;

public interface Service {
    Resp process(Req req);

}
