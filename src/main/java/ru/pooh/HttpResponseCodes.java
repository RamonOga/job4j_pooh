package ru.pooh;

public enum HttpResponseCodes {
    OK(200),
    NoContent(204),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500),
    NotImplemented(501),
    ServiceUnavailable (503);


    private final int value;

    private HttpResponseCodes(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
