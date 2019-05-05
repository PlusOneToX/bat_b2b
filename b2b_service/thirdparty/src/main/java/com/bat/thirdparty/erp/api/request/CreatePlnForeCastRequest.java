package com.bat.thirdparty.erp.api.request;

import com.bat.thirdparty.erp.api.request.dto.PlnForeCastEntry;

import java.util.List;

/**
 * {
 * "ACCESSTOKEN": "b5f71512616d452391c029ef47a82815",
 * "FBILLTYPEID": "YCD01_SYS",
 * "FFOREORGID": "100",
 * "FBILLNO": "",
 * "FDATE": "2019/7/12",
 * "Pln_ForeCastDetail": [{
 * "FSUPPLYORGID": "100",
 * "FMATERIALID": "0.6950290608209.1101-1",
 * "FQTY": "1",
 * "FSTARTDATE": "2019/7/12",
 * "FENDDATE": "2019/7/13",
 * "FAVERATYPE": "N",
 * "FRESERVETYPE": "1"
 * }]
 * }
 */
public class CreatePlnForeCastRequest extends BaseTokenRequest {

    @Override
    public String getPath() {
        return "CreatePln_ForeCast";
    }

    private String FBillTypeID = "YCD01_SYS"; //单据类型，固定
    private String FBILLNO = "";
    private String FDATE; //业务日期，格式2019/07/13
    private String FFOREORGID; //预测组织
    private String F_LHR_DIVISION; //事业部
    private String FB2B_BILLNO; //B2B单据编号

    private List<PlnForeCastEntry> Pln_ForeCastDetail; //明细
    private String FDerivedFrom = "B2B";

    public String getFBillTypeID() {
        return FBillTypeID;
    }

    public void setFBillTypeID(String fBillTypeID) {
        FBillTypeID = fBillTypeID;
    }

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String fBILLNO) {
        FBILLNO = fBILLNO;
    }

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String fDATE) {
        FDATE = fDATE;
    }

    public String getFFOREORGID() {
        return FFOREORGID;
    }

    public void setFFOREORGID(String fFOREORGID) {
        FFOREORGID = fFOREORGID;
    }

    public List<PlnForeCastEntry> getPln_ForeCastDetail() {
        return Pln_ForeCastDetail;
    }

    public void setPln_ForeCastDetail(List<PlnForeCastEntry> pln_ForeCastDetail) {
        Pln_ForeCastDetail = pln_ForeCastDetail;
    }

    public String getFDerivedFrom() {
        return FDerivedFrom;
    }

    public void setFDerivedFrom(String fDerivedFrom) {
        FDerivedFrom = fDerivedFrom;
    }

    public String getF_LHR_DIVISION() {
        return F_LHR_DIVISION;
    }

    public void setF_LHR_DIVISION(String f_LHR_DIVISION) {
        F_LHR_DIVISION = f_LHR_DIVISION;
    }

    public String getFB2B_BILLNO() {
        return FB2B_BILLNO;
    }

    public void setFB2B_BILLNO(String FB2B_BILLNO) {
        this.FB2B_BILLNO = FB2B_BILLNO;
    }
}
