package com.bat.flexible.dao.exchange.dataobject;

public class ExchangeShareDistributorDO {
    private Integer id;

    private Integer exchangeShareId;

    private Integer distributorId;

    private Short type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeShareId() {
        return exchangeShareId;
    }

    public void setExchangeShareId(Integer exchangeShareId) {
        this.exchangeShareId = exchangeShareId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }
}