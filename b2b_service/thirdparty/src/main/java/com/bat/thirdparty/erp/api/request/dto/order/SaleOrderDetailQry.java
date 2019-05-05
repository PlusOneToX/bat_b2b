package com.bat.thirdparty.erp.api.request.dto.order;


import lombok.Data;

import java.io.Serializable;
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
@Data
public class SaleOrderDetailQry implements Serializable {

    private static final long serialVersionUID = 4024871577554994287L;
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



    /** 机型*/
    private String F_FlexProductType;

    /** 机型编码*/
    private String F_FlexProductTypeNo;


    /**
     * 兑换明细
     */
    private List<ExchangePlainCodeOrderErp> F_PAEZ_CardOrderEntity;


    /**
     * 兑换卡归属单号
     */
    private String F_CardSONo;
    /**
     * 发卡分销商ERP编号
     */
    private String F_CardCustId;
    /**
     * 发卡业务员ERP编号
     */
    private String F_CardSalerId;

    /**
     * 明细返利金额单价（返利金额/数量）
     */
    private String F_FLPrice;

    /**
     * 明细返利金额
     */
    private String F_FLAmount;

    //B2B门店编码
    private String F_B2BStoresNo;

    //B2B门店名称
    private String F_B2BStoresName;

}
