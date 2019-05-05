package com.bat.thirdparty.erp.api.request;

import java.util.List;

/**
 * Created by apple on 2019/7/13.
 * json示例
 * {
 * "ACCESSTOKEN": "6501c8559b0f4496b0c5876d3b6bc1b8",
 * "FBILLTYPEID": "XSDD01_SYS",
 * "FBILLNO": "",
 * "FSALEORGID": "100",
 * "FBUSINESSTYPE": "1",
 * "FDATE": "2019/6/25",
 * "FSETTLECURRID": "PRE001",
 * "FCUSTID": "CUST0037",
 * "FSALERID": "DOS019",
 * "whetherAudit": "0",
 * "FNeedRecAdvance": "True",
 * "FLogisticsCost": "100",
 * "SALEORDERDETAIL": [
 * {
 * "FMATERIALID": "0.6950290608209.1101-1",
 * "FMATERIALNAME": "P38/P39无线充数显移动电源8000mAh 底盒(含挂钩)",
 * "FUnitID": "Pcs",
 * "FQTY": "22",
 * "FTAXRATE": "3.2",
 * "FENTRYDISCOUNTRATE": "2019/7/13",
 * "FSTOCKORGID": "100",
 * "FIsFree": "0",
 * "FSerialNumber": "1",
 * "FSETTLEORGID": "100",
 * "FPrice": "22",
 * "FTaxPrice": "22.2"
 * }
 * ]
 * }
 */
public class CreateSaleOrderRequest extends BaseTokenRequest {
    @Override
    public String getPath() {
        return "CreateSalesOrder";
    }

    private String whetherAudit; //是否自动审核(必填 0是，1否)
    private String FSALEORGID;  //销售组织(必填)
    private String FBILLTYPEID = "";
    private String FBILLNO = "";
    private String FBUSINESSTYPE = "";
    // 分销商内码
    private String FCUSTID = "";
    private String FSALERID = "";
    private String FLogisticsCost = "";
    private String FDATE;  // 业务日期(必填)
    private String FSETTLECURRID = "PRE001";
    private String FNeedRecAdvance; // 是否自动预收 订单已支付情况传true
    private String FControlSend;// BUY：订购 SEND：发货 OUTSTOCK：出库 PRODUCT：生产
    private String FDirectShipment; // 是否直运 True：是 False：否
    private String FSOMTO;//是否MTO True：是 False：否
    private List<SaleOrderDetailRequest> SALEORDERDETAIL;
    private String FDerivedFrom = "B2B";
    private String FRecConditionId;
    private String F_LHR_DIVISION; //事业部

    private String FNote;//基本信息备注：FNote
    private String FContact; //    联系人：FContact
    private String FContactPhone; //      联系电话：FContactPhone
    private String FContactAddress; //收货地址：FContactAddress
    private String FB2B_BILLNO; //B2B单据编号
    private String F_SOZF;//订单是否直发 True：是 False：否
    private String F_SOPT;//订单是否拼团 True：是 False:否

    //是否合并发货  False True
    private String F_SOHDFH;

    //是否兑换 True� False
    private String F_SODH;

    public String getFNote() {
        return FNote;
    }

    public void setFNote(String fNote) {
        FNote = fNote;
    }

    public String getFContact() {
        return FContact;
    }

    public void setFContact(String fContact) {
        FContact = fContact;
    }

    public String getFContactPhone() {
        return FContactPhone;
    }

    public void setFContactPhone(String fContactPhone) {
        FContactPhone = fContactPhone;
    }

    public String getFContactAddress() {
        return FContactAddress;
    }

    public void setFContactAddress(String fContactAddress) {
        FContactAddress = fContactAddress;
    }

    public String getWhetherAudit() {
        return whetherAudit;
    }

    public void setWhetherAudit(String whetherAudit) {
        this.whetherAudit = whetherAudit;
    }

    public String getFSALEORGID() {
        return FSALEORGID;
    }

    public void setFSALEORGID(String FSALEORGID) {
        this.FSALEORGID = FSALEORGID;
    }

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getFSETTLECURRID() {
        return FSETTLECURRID;
    }

    public void setFSETTLECURRID(String FSETTLECURRID) {
        this.FSETTLECURRID = FSETTLECURRID;
    }

    public String getFNeedRecAdvance() {
        return FNeedRecAdvance;
    }

    public void setFNeedRecAdvance(String FNeedRecAdvance) {
        this.FNeedRecAdvance = FNeedRecAdvance;
    }

    public List<SaleOrderDetailRequest> getSALEORDERDETAIL() {
        return SALEORDERDETAIL;
    }

    public void setSALEORDERDETAIL(List<SaleOrderDetailRequest> SALEORDERDETAIL) {
        this.SALEORDERDETAIL = SALEORDERDETAIL;
    }

    public String getFBILLTYPEID() {
        return FBILLTYPEID;
    }

    public void setFBILLTYPEID(String FBILLTYPEID) {
        this.FBILLTYPEID = FBILLTYPEID;
    }

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }

    public String getFBUSINESSTYPE() {
        return FBUSINESSTYPE;
    }

    public void setFBUSINESSTYPE(String FBUSINESSTYPE) {
        this.FBUSINESSTYPE = FBUSINESSTYPE;
    }

    public String getFCUSTID() {
        return FCUSTID;
    }

    public void setFCUSTID(String FCUSTID) {
        this.FCUSTID = FCUSTID;
    }

    public String getFSALERID() {
        return FSALERID;
    }

    public void setFSALERID(String FSALERID) {
        this.FSALERID = FSALERID;
    }

    public String getFLogisticsCost() {
        return FLogisticsCost;
    }

    public void setFLogisticsCost(String FLogisticsCost) {
        this.FLogisticsCost = FLogisticsCost;
    }

    public String getFControlSend() {
        return FControlSend;
    }

    public void setFControlSend(String FControlSend) {
        this.FControlSend = FControlSend;
    }

    public String getFDirectShipment() {
        return FDirectShipment;
    }

    public void setFDirectShipment(String FDirectShipment) {
        this.FDirectShipment = FDirectShipment;
    }

    public String getFSOMTO() {
        return FSOMTO;
    }

    public void setFSOMTO(String FSOMTO) {
        this.FSOMTO = FSOMTO;
    }

    public String getF_SOPT() {
        return F_SOPT;
    }

    public void setF_SOPT(String f_SOPT) {
        F_SOPT = f_SOPT;
    }

    public String getFDerivedFrom() {
        return FDerivedFrom;
    }

    public void setFDerivedFrom(String fDerivedFrom) {
        FDerivedFrom = fDerivedFrom;
    }

    public String getFRecConditionId() {
        return FRecConditionId;
    }

    public void setFRecConditionId(String FRecConditionId) {
        this.FRecConditionId = FRecConditionId;
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

    public String getF_SOZF() {
        return F_SOZF;
    }

    public void setF_SOZF(String f_SOZF) {
        F_SOZF = f_SOZF;
    }

    public String getF_SOHDFH() {
        return F_SOHDFH;
    }

    public void setF_SOHDFH(String f_SOHDFH) {
        F_SOHDFH = f_SOHDFH;
    }

    public String getF_SODH() {
        return F_SODH;
    }

    public void setF_SODH(String f_SODH) {
        F_SODH = f_SODH;
    }

    @Override
    public String toString() {
        return "CreateSaleOrderRequest{" +
                "whetherAudit='" + whetherAudit + '\'' +
                ", FSALEORGID='" + FSALEORGID + '\'' +
                ", FBILLTYPEID='" + FBILLTYPEID + '\'' +
                ", FBILLNO='" + FBILLNO + '\'' +
                ", FBUSINESSTYPE='" + FBUSINESSTYPE + '\'' +
                ", FCUSTID='" + FCUSTID + '\'' +
                ", FSALERID='" + FSALERID + '\'' +
                ", FLogisticsCost='" + FLogisticsCost + '\'' +
                ", FDATE='" + FDATE + '\'' +
                ", FSETTLECURRID='" + FSETTLECURRID + '\'' +
                ", FNeedRecAdvance='" + FNeedRecAdvance + '\'' +
                ", FControlSend='" + FControlSend + '\'' +
                ", FDirectShipment='" + FDirectShipment + '\'' +
                ", FSOMTO='" + FSOMTO + '\'' +
                ", SALEORDERDETAIL=" + SALEORDERDETAIL +
                ", FDerivedFrom='" + FDerivedFrom + '\'' +
                ", FRecConditionId='" + FRecConditionId + '\'' +
                ", F_LHR_DIVISION='" + F_LHR_DIVISION + '\'' +
                ", FNote='" + FNote + '\'' +
                ", FContact='" + FContact + '\'' +
                ", FContactPhone='" + FContactPhone + '\'' +
                ", FContactAddress='" + FContactAddress + '\'' +
                ", FB2B_BILLNO='" + FB2B_BILLNO + '\'' +
                ", F_SOZF='" + F_SOZF + '\'' +
                ", F_SOPT='" + F_SOPT + '\'' +
                ", F_SOHDFH='" + F_SOHDFH + '\'' +
                ", F_SODH='" + F_SODH + '\'' +
                '}';
    }
}
