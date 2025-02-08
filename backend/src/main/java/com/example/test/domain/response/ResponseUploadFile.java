package com.example.test.domain.response;

public class ResponseUploadFile {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResponseUploadFile(String url) {
        this.url = url;
    }

}
