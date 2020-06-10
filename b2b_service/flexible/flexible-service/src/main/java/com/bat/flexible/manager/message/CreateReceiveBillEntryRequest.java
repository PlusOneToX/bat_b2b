package com.bat.flexible.manager.message;

import lombok.Data;

@Data
public class CreateReceiveBillEntryRequest  {


    public String getPath() {
        return "CreateReceiveBillEntry";
    }

    private String FBillTypeID = "SKDLX01_SYS"; //单据类型，固定
    private String FDATE; //收款时间，格式2019/07/13
    private String FPAYORGID; //分销商对应的业务员的销售组织erp编码字段
    private String FSETTLEORGID; //分销商对应的业务员的销售组织erp编码字段  -- 同上一致
    private String FCONTACTUNITTYPE = "BD_Customer"; //往来单位,固定
    private String FCONTACTUNIT; //分销商的erp编码
    private String FPAYUNITTYPE = "BD_Customer"; //付款单位，固定
    private String FPAYUNIT; //分销商的erp编码，同上
    private String FCURRENCYID = "PRE001"; //币别，固定

    private String FDerivedFrom = "B2B";
    private String FB2B_BILLNO;

    private String FSALEORGID;//销售组织id


}
