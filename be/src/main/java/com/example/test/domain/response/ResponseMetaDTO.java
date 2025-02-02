package com.example.test.domain.response;

public class ResponseMetaDTO {
    private int current, pageSize, pages;
    private long total;

    private int currents, currentSize;

    public int getPageSize() {
        return pageSize;
    }

    public void setCurrents(int currents) {
        this.currents = currents;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
