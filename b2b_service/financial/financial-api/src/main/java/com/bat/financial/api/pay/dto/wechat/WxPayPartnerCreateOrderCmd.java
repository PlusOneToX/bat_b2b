package com.bat.financial.api.pay.dto.wechat;

import lombok.Data;

/**
 * 微信服务商模式下单参数 V3版本
 */
@Data
public class WxPayPartnerCreateOrderCmd {

    /**
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_1.shtml
     */

    /**
     * 由微信生成的应用ID，全局唯一。请求基础下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     */
    private String sp_appid;


    /**
     * 服务商户号，由微信支付生成并下发
     */
    private String sp_mchid;

    /**
     * 子商户申请的应用ID，全局唯一。请求基础下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     * 若sub_openid有传的情况下，sub_appid必填，且sub_appid需与sub_openid对应
     */
    private String sub_appid;

    /**
     * 子商户的商户号，由微信支付生成并下发。
     */
    private String sub_mchid;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商户单号
     */
    private String out_trade_no;

    /**
     * 订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，
     * HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     */
    private String time_expire;

    /**
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     */
    private String attach;

    /**
     * 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
     */
    private String notify_url;

    /**
     * 结算信息
     */
    private WxPaySettleInfo settle_info;

    /**
     * 订单金额信息
     */
    private WxPayAmount amount;

    /**
     * 支付者信息
     */
    private WxPayPayer payer;

    /**
     * 场景信息
     */
    private WxPaySceneInfo scene_info;
}
