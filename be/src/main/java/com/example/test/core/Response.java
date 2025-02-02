package com.example.test.core;

public class Response<T> {
    private Object message;
    private int status;
    private T data;
    private Object error;

    public Response() {
    }

    public Response(Object message, int status, T data, Object error) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
