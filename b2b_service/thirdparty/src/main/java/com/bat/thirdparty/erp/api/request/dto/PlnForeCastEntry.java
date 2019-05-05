package com.bat.thirdparty.erp.api.request.dto;

/**
 * 预测单据详情
 * "Pln_ForeCastDetail": [{
 * "FSUPPLYORGID": "100",
 * "FMATERIALID": "0.6950290608209.1101-1",
 * "FQTY": "1",
 * "FSTARTDATE": "2019/7/12",
 * "FENDDATE": "2019/7/13",
 * "FAVERATYPE": "N",
 * "FRESERVETYPE": "1"
 * }]
 */
public class PlnForeCastEntry {

    private String FSUPPLYORGID = ""; //供应组织
    private String FMATERIALID; //物料编号
    private Double FQTY; //数量
    private String FSTARTDATE = "";//预测开始时间
    private String FENDDATE = "";//预测结束时间
    private String FAVERATYPE = "N"; //均化类型
    private String FRESERVETYPE = "1"; //预留类型
    private String FCUSTID = "";// 客户编码
    private String F_PAEZ_BASE1 = ""; //申请事业部
    private String F_PAEZ_BASE = ""; //员工

    public String getFSUPPLYORGID() {
        return FSUPPLYORGID;
    }

    public void setFSUPPLYORGID(String fSUPPLYORGID) {
        FSUPPLYORGID = fSUPPLYORGID;
    }

    public String getFMATERIALID() {
        return FMATERIALID;
    }

    public void setFMATERIALID(String fMATERIALID) {
        FMATERIALID = fMATERIALID;
    }

    public Double getFQTY() {
        return FQTY;
    }

    public void setFQTY(Double fQTY) {
        FQTY = fQTY;
    }

    public String getFSTARTDATE() {
        return FSTARTDATE;
    }

    public void setFSTARTDATE(String fSTARTDATE) {
        FSTARTDATE = fSTARTDATE;
    }

    public String getFENDDATE() {
        return FENDDATE;
    }

    public void setFENDDATE(String fENDDATE) {
        FENDDATE = fENDDATE;
    }

    public String getFAVERATYPE() {
        return FAVERATYPE;
    }

    public void setFAVERATYPE(String fAVERATYPE) {
        FAVERATYPE = fAVERATYPE;
    }

    public String getFRESERVETYPE() {
        return FRESERVETYPE;
    }

    public void setFRESERVETYPE(String fRESERVETYPE) {
        FRESERVETYPE = fRESERVETYPE;
    }

    public String getFCUSTID() {
        return FCUSTID;
    }

    public void setFCUSTID(String FCUSTID) {
        this.FCUSTID = FCUSTID;
    }

    public String getF_PAEZ_BASE1() {
        return F_PAEZ_BASE1;
    }

    public void setF_PAEZ_BASE1(String f_PAEZ_BASE1) {
        F_PAEZ_BASE1 = f_PAEZ_BASE1;
    }

    public String getF_PAEZ_BASE() {
        return F_PAEZ_BASE;
    }

    public void setF_PAEZ_BASE(String f_PAEZ_BASE) {
        F_PAEZ_BASE = f_PAEZ_BASE;
    }
}
