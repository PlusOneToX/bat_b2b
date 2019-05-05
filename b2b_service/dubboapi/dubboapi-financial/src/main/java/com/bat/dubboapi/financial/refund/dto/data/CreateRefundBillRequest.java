package com.bat.dubboapi.financial.refund.dto.data;

import java.io.Serializable;
import java.util.List;

public class CreateRefundBillRequest implements Serializable {

    public String getPath() {
        return "CreateRefundBill";
    }

    private String FBillTypeID = "SKDLX01_SYS"; // 单据类型，固定
    private String FDATE; // 退款时间，格式2019/07/13
    private String FPAYORGID; // 分销商对应的业务员的销售组织erp编码字段
    private String FSETTLEORGID; // 分销商对应的业务员的销售组织erp编码字段 -- 同上一致
    private String FCONTACTUNITTYPE = "BD_Customer"; // 往来单位类型,固定
    private String FCONTACTUNIT; // 分销商的erp编码
    private String FRECTUNITTYPE = "BD_Customer"; // 收款单位类型，固定
    private String FRECTUNIT; // 分销商的erp编码，同上
    private String FCURRENCYID = "PRE001"; // 币别，固定
    private List<RefundBillEntry> refundBillDetail; // 退款明细
    private String FDerivedFrom = "B2B";
    private String FB2B_BILLNO;

    public String getFB2B_BILLNO() {
        return FB2B_BILLNO;
    }

    public void setFB2B_BILLNO(String FB2B_BILLNO) {
        this.FB2B_BILLNO = FB2B_BILLNO;
    }

    public String getFBillTypeID() {
        return FBillTypeID;
    }

    public void setFBillTypeID(String FBillTypeID) {
        this.FBillTypeID = FBillTypeID;
    }

    public String getFPAYORGID() {
        return FPAYORGID;
    }

    public void setFPAYORGID(String FPAYORGID) {
        this.FPAYORGID = FPAYORGID;
    }

    public String getFSETTLEORGID() {
        return FSETTLEORGID;
    }

    public void setFSETTLEORGID(String FSETTLEORGID) {
        this.FSETTLEORGID = FSETTLEORGID;
    }

    public String getFCONTACTUNITTYPE() {
        return FCONTACTUNITTYPE;
    }

    public void setFCONTACTUNITTYPE(String FCONTACTUNITTYPE) {
        this.FCONTACTUNITTYPE = FCONTACTUNITTYPE;
    }

    public String getFCONTACTUNIT() {
        return FCONTACTUNIT;
    }

    public void setFCONTACTUNIT(String FCONTACTUNIT) {
        this.FCONTACTUNIT = FCONTACTUNIT;
    }

    public String getFRECTUNITTYPE() {
        return FRECTUNITTYPE;
    }

    public void setFRECTUNITTYPE(String FRECTUNITTYPE) {
        this.FRECTUNITTYPE = FRECTUNITTYPE;
    }

    public String getFRECTUNIT() {
        return FRECTUNIT;
    }

    public void setFRECTUNIT(String FRECTUNIT) {
        this.FRECTUNIT = FRECTUNIT;
    }

    public String getFCURRENCYID() {
        return FCURRENCYID;
    }

    public void setFCURRENCYID(String FCURRENCYID) {
        this.FCURRENCYID = FCURRENCYID;
    }

    public List<RefundBillEntry> getRefundBillDetail() {
        return refundBillDetail;
    }

    public void setRefundBillDetail(List<RefundBillEntry> refundBillDetail) {
        this.refundBillDetail = refundBillDetail;
    }

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getFDerivedFrom() {
        return FDerivedFrom;
    }

    public void setFDerivedFrom(String fDerivedFrom) {
        FDerivedFrom = fDerivedFrom;
    }

    @Override
    public String toString() {
        return "CreateRefundBillRequest{" +
                "FBillTypeID='" + FBillTypeID + '\'' +
                ", FDATE='" + FDATE + '\'' +
                ", FPAYORGID='" + FPAYORGID + '\'' +
                ", FSETTLEORGID='" + FSETTLEORGID + '\'' +
                ", FCONTACTUNITTYPE='" + FCONTACTUNITTYPE + '\'' +
                ", FCONTACTUNIT='" + FCONTACTUNIT + '\'' +
                ", FRECTUNITTYPE='" + FRECTUNITTYPE + '\'' +
                ", FRECTUNIT='" + FRECTUNIT + '\'' +
                ", FCURRENCYID='" + FCURRENCYID + '\'' +
                ", refundBillDetail=" + refundBillDetail +
                ", FDerivedFrom='" + FDerivedFrom + '\'' +
                ", FB2B_BILLNO='" + FB2B_BILLNO + '\'' +
                '}';
    }
}
