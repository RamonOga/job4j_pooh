package ru.pooh;


public class Req {
    private final String method;
    private final String mode;
    private final String text;
    private final String message;

    private Req(String method, String mode, String text, String message) {
        this.method = method; // post or get
        this.mode = mode; // queue or topic
        this.text = text;
        this.message = message;
    }

    public static Req of(String content) {
        System.out.println(content);
        String[] lines = content.split(System.lineSeparator());
        String method = lines[0].split(" ")[0];
        String mode = lines[0].split(" ")[1].split("/")[1];
        String text = lines[0].split(" ")[1];
        String message = lines[lines.length - 1];
        var rsl = new Req(method, mode, text, message);
        System.out.println(rsl.toString());
        return rsl;
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

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return "Req{" +
                "method='" + method + '\'' +
                ", mode='" + mode + '\'' +
                ", text='" + text + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}