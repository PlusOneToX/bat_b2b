package com.bat.dubboapi.thirdparty.wx.dto.subaccount;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxSubAccountReleCmd implements Serializable {


    /**
     * 微信支付分配的子商户号，即分账的出资商户号。
     */
    private String sub_mchid;

    /**
     * 微信分配的公众账号ID
     */
    private String appid;

    /**
     * 接收方类型 枚举值：
     * MERCHANT_ID：商户ID
     * PERSONAL_OPENID：个人openid（由父商户APPID转换得到）
     * PERSONAL_SUB_OPENID：个人sub_openid（由子商户APPID转换得到）
     */
    private String type;

    /**
     * 分账接收方账号
     * 类型是MERCHANT_ID时，是商户号 类型是PERSONAL_OPENID时，是个人openid 类型是PERSONAL_SUB_OPENID时，是个人sub_openid
     */
    private String  account;

    /**
     * 与分账方的关系类型
     * 子商户与接收方的关系。 本字段值为枚举：
     * SERVICE_PROVIDER：服务商
     * STORE：门店
     * STAFF：员工
     * STORE_OWNER：店主
     * PARTNER：合作伙伴
     * HEADQUARTER：总部
     * BRAND：品牌方
     * DISTRIBUTOR：分销商
     * USER：用户
     * SUPPLIER： 供应商
     * CUSTOM：自定义
     */
    private String relation_type;

    /**
     *
     */
    private String name;


}
