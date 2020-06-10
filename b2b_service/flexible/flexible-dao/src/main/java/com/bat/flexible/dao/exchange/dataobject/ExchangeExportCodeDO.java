package com.bat.flexible.dao.exchange.dataobject;

public class ExchangeExportCodeDO {
    private Integer id;

    private Integer exchangeExportId;

    private Integer exchangeCodeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeExportId() {
        return exchangeExportId;
    }

    public void setExchangeExportId(Integer exchangeExportId) {
        this.exchangeExportId = exchangeExportId;
    }

    public Integer getExchangeCodeId() {
        return exchangeCodeId;
    }

    public void setExchangeCodeId(Integer exchangeCodeId) {
        this.exchangeCodeId = exchangeCodeId;
    }
}