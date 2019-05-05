package com.bat.thirdparty.erp.api.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by apple on 2019/7/1.
 */
@Data
public class DistributorERPEntry {
    // 创建组织
    @JsonProperty("FCreateOrgId")
    private ERPCreateOrg FCreateOrgId;
    // 使用组织
    @JsonProperty("FUseOrgId")
    private ERPFUseOrgId FUseOrgId;
    // 客户内码
    @JsonProperty("FCUSTID")
    private String FCUSTID;
    // 客户名称
    @JsonProperty("FName")
    private String FName;
    // 客户编码
    @JsonProperty("FNumber")
    private String FNumber;
    // 是否交易客户(写死)
    @JsonProperty("FIsTrade")
    private Boolean FIsTrade;
    // 销售员
    @JsonProperty("FSELLER")
    private ERPSeller FSELLER;
    // 销售部门
    @JsonProperty("FSALDEPTID")
    private ERPSellDepId FSALDEPTID;
    // 客户分组
    @JsonProperty("FGroup")
    private ERPGroup FGroup;
    // 客户类别
    @JsonProperty("FCustTypeId")
    private ERPCusType FCustTypeId;
    // 客户归属、固定传B2B（写死）
    @JsonProperty("F_PAEZ_KHGS")
    private String F_PAEZ_KHGS;
    // 是否直发客户
    @JsonProperty("F_SOZF")
    private Boolean F_SOZF;
    // 是否款到发货 1、是、 0、否
    @JsonProperty("F_PAEZ_YETOB2B")
    private String F_PAEZ_YETOB2B;
    // 是否同步B2B余额 0表示否 1表示是
    @JsonProperty("F_PAEZ_YEB2BView")
    private String F_PAEZ_YEB2BView;
    // 收款条件
    @JsonProperty("FRECCONDITIONID")
    private ERPFrecondition FRECCONDITIONID;
    // 国家
    @JsonProperty("FCOUNTRY")
    private ERPCountry FCOUNTRY;
    // 通讯地址
    @JsonProperty("FADDRESS")
    private String FADDRESS;
    // 邮政编码
    @JsonProperty("FZIP")
    private String FZIP;
    // 联系人（使用帐号拥有者）
    @JsonProperty("F_PAEZ_Text")
    private String F_PAEZ_Text;
    // 联系电话（使用帐号拥有者）
    @JsonProperty("FTEL")
    private String FTEL;
    // 结算币别（配置）
    @JsonProperty("FTRADINGCURRID")
    private ERPTrade FTRADINGCURRID;
    // 默认税率（配置）
    @JsonProperty("FtaxRate")
    private ErpFtaxRate FtaxRate;
    // 税分类（配置）
    @JsonProperty("FTaxType")
    private ErpFtaxType FTaxType;
    // 发票抬头
    @JsonProperty("FINVOICETITLE")
    private String FINVOICETITLE;
    // 纳税登记号
    @JsonProperty("FTAXREGISTERCODE")
    private String FTAXREGISTERCODE;
    // 开户银行
    @JsonProperty("FINVOICEBANKNAME")
    private String FINVOICEBANKNAME;
    // 银行账号
    @JsonProperty("FINVOICEBANKACCOUNT")
    private String FINVOICEBANKACCOUNT;
    // 开票联系电话
    @JsonProperty("FINVOICETEL")
    private String FINVOICETEL;
    // 开票通讯地址
    @JsonProperty("FINVOICEADDRESS")
    private String FINVOICEADDRESS;

    /**
     * 结算方式
     */
    @Data
    @AllArgsConstructor
    public static class ERPTrade {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    /**
     * 销售组织
     */
    @Data
    @AllArgsConstructor
    public static class ERPCreateOrg {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    /**
     * 使用组织
     */
    @Data
    @AllArgsConstructor
    public static class ERPFUseOrgId {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ErpFtaxRate {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ErpFtaxType {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ERPOrderOrgId {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ERPSeller {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ERPSellDepId {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ERPGroup {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ERPCusType {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    /**
     * 结算方式ID
     */
    @Data
    @AllArgsConstructor
    public static class ERPFrecondition {
        @JsonProperty("FNumber")
        private String FNumber;
    }

    /**
     * 国家
     */
    @Data
    @AllArgsConstructor
    public static class ERPCountry {
        @JsonProperty("FNumber")
        private String FNumber;
    }
}
