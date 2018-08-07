package com.bat.financial.api.subaccount.dto;

import lombok.Data;

@Data
public class WechatPaySubAccountReceiverCmd {

    /**
     *1、MERCHANT_ID：商户号
     * 2、PERSONAL_OPENID：个人openid（由父商户APPID转换得到）
     * 3、PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）
     */
    private String type;

    /**
     * 1、分账接收方类型为MERCHANT_ID时，分账接收方账号为商户号
     * 2、分账接收方类型为PERSONAL_OPENID时，分账接收方账号为个人openid
     * 3、分账接收方类型为
     * PERSONAL_SUB_OPENID时，分账接收方账号为个人sub_openid
     */
    private String account;

    /**
     *  可选项，在接收方类型为个人的时可选填，若有值，会检查与 name 是否实名匹配，不匹配会拒绝分账请求
     * 1、分账接收方类型是PERSONAL_OPENID或PERSONAL_SUB_OPENID时，是个人姓名的密文（选传，传则校验） 此字段的加密方法详见：敏感信息加密说明
     * 2、使用微信支付平台证书中的公钥
     * 3、使用RSAES-OAEP算法进行加密
     * 4、将请求中HTTP头部的Wechatpay-Serial设置为证书序列号
     */
    private String name;

    /**
     *  分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
     */
    private Integer amount;

    /**
     *  分账的原因描述，分账账单中需要体现
     */
    private String description;
}
