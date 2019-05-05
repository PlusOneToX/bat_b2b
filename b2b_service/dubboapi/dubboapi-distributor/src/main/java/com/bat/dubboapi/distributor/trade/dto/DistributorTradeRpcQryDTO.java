package com.bat.dubboapi.distributor.trade.dto;



import java.io.Serializable;


public class DistributorTradeRpcQryDTO implements Serializable {
    private static final long serialVersionUID = -1322382721329107495L;
    private Integer id;

    private String name;

    private String nameEn;

    private Short payWay;

    private Integer settlingTime;

    private String erpSettleAccountNo;

    private Short openFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public Integer getSettlingTime() {
        return settlingTime;
    }

    public void setSettlingTime(Integer settlingTime) {
        this.settlingTime = settlingTime;
    }

    public String getErpSettleAccountNo() {
        return erpSettleAccountNo;
    }

    public void setErpSettleAccountNo(String erpSettleAccountNo) {
        this.erpSettleAccountNo = erpSettleAccountNo;
    }

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
    }
}