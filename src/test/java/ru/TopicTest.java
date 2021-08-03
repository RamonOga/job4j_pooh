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

    @Test
    public void whenRespMethodTestQueueModeManyResponses() {
        String message1 = "temperature=18";
        String message2 = "Stream API";
        String message3 = "temperature=18";
        String postText1 =  textPost("/queue/weather", message1);
        String postText2 =  textPost("/queue/java", message2);
        String postText3 =  textPost("/queue/java", message3);
        String getText1=  textGet("/queue/weather", "1");
        String getText2=  textGet("/queue/java", "1");
        String getText3=  textGet("/queue/java", "1");

        TopicService ts = new TopicService();

        Req postReq1 = Req.of(postText1);
        Req postReq2 = Req.of(postText2);
        Req postReq3 = Req.of(postText3);
        Req getReq1 = Req.of(getText1);
        Req getReq2 = Req.of(getText2);
        Req getReq3 = Req.of(getText3);
        ts.process(postReq1);
        ts.process(postReq2);
        ts.process(postReq3);
        Resp getResp1 = ts.process(getReq1);
        Resp getResp2 = ts.process(getReq2);
        Resp getResp3 = ts.process(getReq3);
        Assert.assertEquals(200, getResp1.status());
        Assert.assertEquals(message1, getResp1.text());
        Assert.assertEquals(200, getResp2.status());
        Assert.assertEquals(message2, getResp2.text());
        Assert.assertEquals(200, getResp3.status());
        Assert.assertEquals(message3, getResp3.text());
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
