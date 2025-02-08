package com.example.test.domain.response;

public class ResponsePaginationDTO {
    private ResponseMetaDTO meta;
    private Object result;

    public ResponseMetaDTO getMeta() {
        return meta;
    }

    public void setMeta(ResponseMetaDTO meta) {
        this.meta = meta;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
