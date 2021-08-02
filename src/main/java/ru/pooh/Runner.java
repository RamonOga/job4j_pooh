package ru.pooh;


import ru.pooh.services.QueueService;

public class Runner {
    public static void main(String[] args) {
        QueueService qs = new QueueService();
        String ln = System.lineSeparator();
        String tex = "POST /queue/weather HTTP/1.1" + ln +
                "Host: localhost:9000" + ln +
                "User-Agent: curl/7.77.0" + ln +
                "Accept: */*" + ln +
                "Content-Length: 14" + ln +
                "Content-Type: application/x-www-form-urlencoded" + ln +
                ln +
                "temperature=18";

        System.out.println(tex);
        Req req = Req.of(tex);
        Resp resp = qs.process(req);
        System.out.println(resp.status());
        System.out.println(resp.text());
    }
}
