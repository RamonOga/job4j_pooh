package ru.job4j.lasttask;

import java.util.Arrays;

public class Req {
    private final String method;
    private final String mode;
    private final String text;

    private Req(String method, String mode, String text) {
        this.method = method; // post or get
        this.mode = mode; // queue or topic
        this.text = text;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        String method = lines[0].split(" ")[0];
        String mode = lines[0].split(" ")[1];
        String text = lines[lines.length - 1];
        return new Req(method, mode, text);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }
}