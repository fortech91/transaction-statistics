package com.n26.statistics.exception;

import org.springframework.http.HttpStatus;

/**
 * @author FortunatusE
 * @date 12/8/2018
 */
public class TransactionException extends RuntimeException {


    private HttpStatus status;

    public TransactionException(HttpStatus status){
        this.status = status;
    }

    public TransactionException(String message){
        super(message);
    }

    public TransactionException(String message, Throwable cause){
        super(message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
