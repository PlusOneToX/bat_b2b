package com.bat.flexible.api.exchange.dto;

public class ExchangeNoticeRequest {

    //当前页
    private Integer page=1;

    //页面大小
    private Integer count=10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
