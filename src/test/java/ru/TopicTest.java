package ru;

import org.junit.Assert;
import org.junit.Test;
import ru.pooh.Req;
import ru.pooh.Resp;
import ru.pooh.services.QueueService;
import ru.pooh.services.TopicService;

public class TopicTest {
    @Test
    public void whenGetMethodTestQueueMode() {
        String text =  textPost("/queue/weather", "temperature=18");
        TopicService ts = new TopicService();
        Req req = Req.of(text);
        Resp resp = ts.process(req);
        Assert.assertEquals(200, resp.status());
        Assert.assertEquals("OK", resp.text());
    }

    @Test
    public void whenRespMethodTestQueueMode() {
        String message = "temperature=18";
        String postText =  textPost("/queue/weather", message);
        String getText =  textGet("/queue/weather", "1");

        TopicService ts = new TopicService();

        Req postReq = Req.of(postText);
        Req getReq = Req.of(getText);
        ts.process(postReq);
        Resp getResp = ts.process(getReq);
        Assert.assertEquals(200, getResp.status());
        Assert.assertEquals(message, getResp.text());
    }

    private String textPost(String query, String message) {
        String ln = System.lineSeparator();
        return  "POST " + query + " HTTP/1.1" + ln +
                "Host: localhost:9000" + ln +
                "User-Agent: curl/7.77.0" + ln +
                "Accept: */*" + ln +
                "Content-Length: 14" + ln +
                "Content-Type: application/x-www-form-urlencoded" + ln +
                ln +
                message;
    }

    private String textGet(String query, String id) {
        String ln = System.lineSeparator();
        return  "GET " + query + "/" + id + " HTTP/1.1" + ln +
                "Host: localhost:9000" + ln +
                "User-Agent: curl/7.77.0" + ln +
                "Accept: */*" + ln +
                "Content-Length: 14" + ln +
                "Content-Type: application/x-www-form-urlencoded" + ln +
                ln;
    }
}
