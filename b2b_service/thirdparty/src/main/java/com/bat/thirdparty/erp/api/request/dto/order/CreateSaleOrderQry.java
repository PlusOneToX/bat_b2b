package com.bat.thirdparty.erp.api.request.dto.order;

import lombok.Data;

import java.io.Serializable;
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
@Data
public class CreateSaleOrderQry implements Serializable {

    private static final long serialVersionUID = -4255410326853759095L;


    private String whetherAudit; //是否自动审核(必填 0是，1否)
    private String FSALEORGID;  //销售组织(必填)
    private String FBILLTYPEID = "";
    private String FBILLNO = "";
    private String FBUSINESSTYPE = "";
    private String FCUSTID = "";
    private String FSALERID = "";
    private String FLogisticsCost = "";
    private String FDATE;  // 业务日期(必填)
    private String FSETTLECURRID = "PRE001";
    private String FNeedRecAdvance; // 是否自动预收 订单已支付情况传true
    private String FControlSend;// BUY：订购 SEND：发货 OUTSTOCK：出库 PRODUCT：生产
    private String FDirectShipment; // 是否直运 True：是 False：否
    private String FSOMTO;//是否MTO True：是 False：否
    private List<SaleOrderDetailQry> SALEORDERDETAIL;
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

    private String ACCESSTOKEN;

    //B2B门店编码
    private String F_B2BStoresNo;

    //B2B门店名称
    private String F_B2BStoresName;

    //是否需要同步收款单(ERP不需要、返回订单发消息)
    private Boolean syncVoucherFlag=false;

    //是否定制订单(ERP不需要、标品直发需要调用ERP)
    private Boolean customDiyFlag=false;

    //返利金
    private String F_PAEZ_FLAMOUNT;

    public String getPath() {
        return "CreateSalesOrder";
    }
}
