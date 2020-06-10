package com.bat.flexible.dao.exchange.co;

import io.swagger.annotations.ApiModelProperty;

public class ExchangeShareDistributorCO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "转发活动配置id")
    private Integer exchangeShareId;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "关系类型 1当前分销商可视 2当前分销商不可视（活动为指定全部分销商生效）")
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
