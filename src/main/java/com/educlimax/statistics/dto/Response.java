package com.educlimax.statistics.dto;

import org.springframework.http.HttpStatus;

/**
 * @author FortunatusE
 * @date 12/9/2018
 */
public class Response {

    private HttpStatus status;
    private Object body;

    public Response(HttpStatus status) {
        this.status = status;
    }

    public Response(HttpStatus status, Object body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", body=" + body +
                '}';
    }
}
