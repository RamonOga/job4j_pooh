package ru.pooh;


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
        System.out.println(content);
        String[] lines = content.split(System.lineSeparator());
        String method = lines[0].split(" ")[0];
        String mode = lines[0].split(" ")[1];
        String text = lines[lines.length - 1];
        var rsl = new Req(method, mode, text);
        System.out.println(rsl);
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

    @Override
    public String toString() {
        return "Req{" +
                "method='" + method + '\'' +
                ", mode='" + mode + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}