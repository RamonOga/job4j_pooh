package ru.pooh.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogCreator {
    private static final Logger LOG = LogManager.getLogger();

    public static Logger getLOG(){
        return LOG;
    }

}
