package com.bat.financial.api.subaccount.dto;

import lombok.Data;

import java.util.List;

@Data
public class WechatPaySubAccountCmd {

    //子商户号
    private String sub_mchid;

    //微信分配的服务商appid
    private String appid;

    //微信分配的子商户公众账号ID，分账接收方类型包含PERSONAL_SUB_OPENID时必填。
    private String sub_appid;

    //微信支付订单号
    private String transaction_id;

    //服务商系统内部的分账单号，在服务商系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母
    /**
     * 对于多次分账接口，请求的参数out_order_no是系统的订单号，第一分账是我们自己的订单号，第二次，第三次请求分账，out_order_no参数是上一次返回的order_id。out_order_no如果一直是系统内部的单号，则会报错，报错信息是：分账接收方与原请求方不一致。
     *
     * 　 例如：
     *
     * 　　第一次分账：out_order_no 是系统内部的订单号，分账成功微信官方返回一个order_id，记得保存好。
     *
     * 　　第二次分账：out_order_no 是第一次分账成功微信官方返回一个order_id。分账成功后，依然返回一个order_id，保存好，待第三次使用。
     */
    private String out_order_no;

    /**
     * 分账接收方列表
     */
    private List<WechatPaySubAccountReceiverCmd> receivers;

    /**
     *  1、如果为true，该笔订单剩余未分账的金额会解冻回分账方商户；
     *  2、如果为false，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。
     */
    private Boolean unfreeze_unsplit;

}
