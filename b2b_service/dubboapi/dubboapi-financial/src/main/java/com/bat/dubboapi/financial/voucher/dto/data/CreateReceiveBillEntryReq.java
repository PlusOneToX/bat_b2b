package com.bat.dubboapi.financial.voucher.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/17 21:25
 */
public class CreateReceiveBillEntryReq implements Serializable {
    // @Override
    public String getPath() {
        return "CreateReceiveBillEntry";
    }

    private String FBillTypeID = "SKDLX01_SYS"; // 单据类型，固定
    private String FDATE; // 收款时间，格式2019/07/13
    private String FPAYORGID; // 分销商对应的业务员的销售组织erp编码字段
    private String FSETTLEORGID; // 分销商对应的业务员的销售组织erp编码字段 -- 同上一致
    private String FCONTACTUNITTYPE = "BD_Customer"; // 往来单位,固定
    private String FCONTACTUNIT; // 分销商的erp编码
    private String FPAYUNITTYPE = "BD_Customer"; // 付款单位，固定
    private String FPAYUNIT; // 分销商的erp编码，同上
    private String FCURRENCYID = "PRE001"; // 币别，固定
    private List<ReceiveBillEntry> ReceiveBillEntryDetail; // 明细
    private String FDerivedFrom = "B2B";
    private String FB2B_BILLNO;

    private String FSALEORGID;// 销售组织id

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

    public String getFPAYUNITTYPE() {
        return FPAYUNITTYPE;
    }

    public void setFPAYUNITTYPE(String FPAYUNITTYPE) {
        this.FPAYUNITTYPE = FPAYUNITTYPE;
    }

    public String getFPAYUNIT() {
        return FPAYUNIT;
    }

    public void setFPAYUNIT(String FPAYUNIT) {
        this.FPAYUNIT = FPAYUNIT;
    }

    public String getFCURRENCYID() {
        return FCURRENCYID;
    }

    public void setFCURRENCYID(String FCURRENCYID) {
        this.FCURRENCYID = FCURRENCYID;
    }

    public List<ReceiveBillEntry> getReceiveBillEntryDetail() {
        return ReceiveBillEntryDetail;
    }

    public void setReceiveBillEntryDetail(List<ReceiveBillEntry> receiveBillEntryDetail) {
        ReceiveBillEntryDetail = receiveBillEntryDetail;
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

    public String getFB2B_BILLNO() {
        return FB2B_BILLNO;
    }

    public void setFB2B_BILLNO(String FB2B_BILLNO) {
        this.FB2B_BILLNO = FB2B_BILLNO;
    }

    public String getFSALEORGID() {
        return FSALEORGID;
    }

    public void setFSALEORGID(String FSALEORGID) {
        this.FSALEORGID = FSALEORGID;
    }

    @Override
    public String toString() {
        return "CreateReceiveBillEntryReq{" + "FBillTypeID='" + FBillTypeID + '\'' + ", FDATE='" + FDATE + '\''
            + ", FPAYORGID='" + FPAYORGID + '\'' + ", FSETTLEORGID='" + FSETTLEORGID + '\'' + ", FCONTACTUNITTYPE='"
            + FCONTACTUNITTYPE + '\'' + ", FCONTACTUNIT='" + FCONTACTUNIT + '\'' + ", FPAYUNITTYPE='" + FPAYUNITTYPE
            + '\'' + ", FPAYUNIT='" + FPAYUNIT + '\'' + ", FCURRENCYID='" + FCURRENCYID + '\''
            + ", ReceiveBillEntryDetail=" + ReceiveBillEntryDetail + ", FDerivedFrom='" + FDerivedFrom + '\''
            + ", FB2B_BILLNO='" + FB2B_BILLNO + '\'' + ", FSALEORGID='" + FSALEORGID + '\'' + '}';
    }
}
