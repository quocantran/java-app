package com.example.test.core.error;

public class ForbiddenException extends Exception {
    private String message;
    private int status = 403;

    public ForbiddenException(String message) {
        super(message);
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

}
