package com.bat.dubboapi.financial.basesetting.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/18 14:52
 */
public class CurrencyRateRpcDTO implements Serializable {
    private Integer id;

    private BigDecimal exchangeRate;

    private BigDecimal reverseExRate;

    private String cyForid;

    private String cyToid;

    private Date begDate;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getReverseExRate() {
        return reverseExRate;
    }

    public void setReverseExRate(BigDecimal reverseExRate) {
        this.reverseExRate = reverseExRate;
    }

    public String getCyForid() {
        return cyForid;
    }

    public void setCyForid(String cyForid) {
        this.cyForid = cyForid;
    }

    public String getCyToid() {
        return cyToid;
    }

    public void setCyToid(String cyToid) {
        this.cyToid = cyToid;
    }

    public Date getBegDate() {
        return begDate;
    }

    public void setBegDate(Date begDate) {
        this.begDate = begDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
