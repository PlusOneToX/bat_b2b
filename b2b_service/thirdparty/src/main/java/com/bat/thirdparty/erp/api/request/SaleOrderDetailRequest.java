package com.bat.thirdparty.erp.api.request;


import com.bat.thirdparty.erp.api.request.dto.order.ExchangePlainCodeOrderErp;

import java.util.List;

/**
 * {
 "FMATERIALID": "0.6950290608209.1101-1",
 "FMATERIALNAME": "P38/P39无线充数显移动电源8000mAh 底盒(含挂钩)",
 "FUnitID": "Pcs",
 "FQTY": "22",
 "FTAXRATE": "3.2",
 "FENTRYDISCOUNTRATE": "2019/7/13",
 "FSTOCKORGID": "100",
 "FIsFree": "0",
 "FSerialNumber": "1",
 "FSETTLEORGID": "100",
 "FPrice": "22",
 "FTaxPrice": "22.2"
 }
 */
public class SaleOrderDetailRequest {

    private String FMATERIALID; // 物料编码(必填)
    private String FMATERIALNAME = "";
    private String FUnitID="";
    private String FQTY; // 物料数量(必填)
    private String FTAXRATE = "";
    private String FENTRYDISCOUNTRATE; // 要货日期(必填)
    private String FSTOCKORGID;
    private String FIsFree; // 是否赠品(必填)
    private String FSerialNumber =""; // 行序号(必填)
    private String FSETTLEORGID; // 结算组织(必填)
    private String FPrice="";
    private String FTaxPrice=""; // 含税单价(必填)
    private String FSOSTOCKID="";
    private String F_B2BVIPPrice;

    private String FB2BMaterName;//材质名称

    private String FB2BMaterNo; //B2B材质编码

    private String FBomId;//BOM版本编号

    private String F_PictureID; //图片名称

    /** 服务费盒内数量(算总价)*/
    private String serviceBoxNum;

    /** 服务费单价*/
    private String serviceFee;

    /** 机型*/
    private String F_FlexProductType;

    /** 机型编码*/
    private String F_FlexProductTypeNo;


    /**
     * 兑换明细
     */
    private List<ExchangePlainCodeOrderErp> F_PAEZ_CardOrderEntity;


    public String getFMATERIALID() {
        return FMATERIALID;
    }

    public void setFMATERIALID(String FMATERIALID) {
        this.FMATERIALID = FMATERIALID;
    }


    public String getFTAXRATE() {
        return FTAXRATE;
    }

    public void setFTAXRATE(String FTAXRATE) {
        this.FTAXRATE = FTAXRATE;
    }

    public String getFENTRYDISCOUNTRATE() {
        return FENTRYDISCOUNTRATE;
    }

    public void setFENTRYDISCOUNTRATE(String FENTRYDISCOUNTRATE) {
        this.FENTRYDISCOUNTRATE = FENTRYDISCOUNTRATE;
    }


    public String getFSETTLEORGID() {
        return FSETTLEORGID;
    }

    public void setFSETTLEORGID(String FSETTLEORGID) {
        this.FSETTLEORGID = FSETTLEORGID;
    }

    public String getFQTY() {
        return FQTY;
    }

    public void setFQTY(String FQTY) {
        this.FQTY = FQTY;
    }

    public String getFIsFree() {
        return FIsFree;
    }

    public void setFIsFree(String FIsFree) {
        this.FIsFree = FIsFree;
    }

    public String getFSerialNumber() {
        return FSerialNumber;
    }

    public void setFSerialNumber(String FSerialNumber) {
        this.FSerialNumber = FSerialNumber;
    }

    public String getFPrice() {
        return FPrice;
    }

    public void setFPrice(String FPrice) {
        this.FPrice = FPrice;
    }

    public String getFTaxPrice() {
        return FTaxPrice;
    }

    public void setFTaxPrice(String FTaxPrice) {
        this.FTaxPrice = FTaxPrice;
    }

    public String getFMATERIALNAME() {
        return FMATERIALNAME;
    }

    public void setFMATERIALNAME(String FMATERIALNAME) {
        this.FMATERIALNAME = FMATERIALNAME;
    }

    public String getFUnitID() {
        return FUnitID;
    }

    public void setFUnitID(String FUnitID) {
        this.FUnitID = FUnitID;
    }

    public String getFSTOCKORGID() {
        return FSTOCKORGID;
    }

    public void setFSTOCKORGID(String FSTOCKORGID) {
        this.FSTOCKORGID = FSTOCKORGID;
    }

	public String getFSOSTOCKID() {
		return FSOSTOCKID;
	}

	public void setFSOSTOCKID(String fSOSTOCKID) {
		FSOSTOCKID = fSOSTOCKID;
	}

    public String getF_B2BVIPPrice() {
        return F_B2BVIPPrice;
    }

    public void setF_B2BVIPPrice(String f_B2BVIPPrice) {
        F_B2BVIPPrice = f_B2BVIPPrice;
    }

    public List<ExchangePlainCodeOrderErp> getF_PAEZ_CardOrderEntity() {
        return F_PAEZ_CardOrderEntity;
    }

    public void setF_PAEZ_CardOrderEntity(List<ExchangePlainCodeOrderErp> f_PAEZ_CardOrderEntity) {
        F_PAEZ_CardOrderEntity = f_PAEZ_CardOrderEntity;
    }

    public String getFB2BMaterName() {
        return FB2BMaterName;
    }

    public void setFB2BMaterName(String FB2BMaterName) {
        this.FB2BMaterName = FB2BMaterName;
    }

    public String getFB2BMaterNo() {
        return FB2BMaterNo;
    }

    public void setFB2BMaterNo(String FB2BMaterNo) {
        this.FB2BMaterNo = FB2BMaterNo;
    }

    public String getFBomId() {
        return FBomId;
    }

    public void setFBomId(String FBomId) {
        this.FBomId = FBomId;
    }

    public String getF_PictureID() {
        return F_PictureID;
    }

    public void setF_PictureID(String f_PictureID) {
        F_PictureID = f_PictureID;
    }

    public String getServiceBoxNum() {
        return serviceBoxNum;
    }

    public void setServiceBoxNum(String serviceBoxNum) {
        this.serviceBoxNum = serviceBoxNum;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getF_FlexProductType() {
        return F_FlexProductType;
    }

    public void setF_FlexProductType(String f_FlexProductType) {
        F_FlexProductType = f_FlexProductType;
    }

    public String getF_FlexProductTypeNo() {
        return F_FlexProductTypeNo;
    }

    public void setF_FlexProductTypeNo(String f_FlexProductTypeNo) {
        F_FlexProductTypeNo = f_FlexProductTypeNo;
    }
}
