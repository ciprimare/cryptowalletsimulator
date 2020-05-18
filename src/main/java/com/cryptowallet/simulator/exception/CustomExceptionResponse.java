package com.cryptowallet.simulator.exception;

import java.util.Date;

public class CustomExceptionResponse {
    private final Date timestamp;
    private final String message;
    private final String details;

    public CustomExceptionResponse(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
