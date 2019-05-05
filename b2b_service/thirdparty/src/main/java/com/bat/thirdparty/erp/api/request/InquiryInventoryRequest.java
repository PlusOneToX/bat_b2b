package com.bat.thirdparty.erp.api.request;

import java.util.List;

public class InquiryInventoryRequest extends BaseTokenRequest {

    private Integer Datetime;
    private List<String> StokeId;
    private Integer PAGECOUNT = 1000;
    private Integer PAGEINDEX;
    private List<Long> FMATERIALID;

    public Integer getDatetime() {
        return Datetime;
    }

    public void setDatetime(Integer datetime) {
        Datetime = datetime;
    }

    public List<String> getStokeId() {
        return StokeId;
    }

    public void setStokeId(List<String> stokeId) {
        StokeId = stokeId;
    }

    public Integer getPAGECOUNT() {
        return PAGECOUNT;
    }

    public void setPAGECOUNT(Integer PAGECOUNT) {
        this.PAGECOUNT = PAGECOUNT;
    }

    public Integer getPAGEINDEX() {
        return PAGEINDEX;
    }

    public void setPAGEINDEX(Integer PAGEINDEX) {
        this.PAGEINDEX = PAGEINDEX;
    }

    @Override
    public String getPath() {
        return "InquiryInventory";
    }

    public List<Long> getFMATERIALID() {
        return FMATERIALID;
    }

    public void setFMATERIALID(List<Long> FMATERIALID) {
        this.FMATERIALID = FMATERIALID;
    }
}
