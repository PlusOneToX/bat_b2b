package com.bat.thirdparty.msgcenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信小程序订阅消息相关配置
 */
@Component
public class WechatProgramMsgConfig {

    //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
    public static String miniprogramState;

    //消息跳转订单列表页面
    public static String orderListUrl;

    //消息跳转订单详情页面
    public static String orderDetailUrl;

    //消息跳转分销商列表页面
    public static String distributorListUrl;

    //消息跳转PC订单列表页面
    public static String pcOrderListUrl;

    //消息跳转PC订单详情页面
    public static String pcOrderDetailUrl;

    //消息跳转PC分销商列表页面
    public static String pcDistributorListUrl;

    @Value("${wechat.program.msg.miniprogramState}")
    public void setMiniprogramState(String miniprogramState) {
        WechatProgramMsgConfig.miniprogramState = miniprogramState;
    }

    @Value("${wechat.program.msg.orderDetailUrl}")
    public void setOrderDetailUrl(String orderDetailUrl) {
        WechatProgramMsgConfig.orderDetailUrl = orderDetailUrl;
    }

    @Value("${wechat.program.msg.distributorListUrl}")
    public void setDistributorListUrl(String distributorListUrl) {
        WechatProgramMsgConfig.distributorListUrl = distributorListUrl;
    }

    @Value("${wechat.program.msg.orderListUrl}")
    public void setOrderListUrl(String orderListUrl) {
        WechatProgramMsgConfig.orderListUrl = orderListUrl;
    }

    @Value("${wechat.program.msg.pc.orderListUrl}")
    public void setPcOrderListUrl(String pcOrderListUrl) {
        WechatProgramMsgConfig.pcOrderListUrl = pcOrderListUrl;
    }

    @Value("${wechat.program.msg.pc.orderDetailUrl}")
    public void setPcOrderDetailUrl(String pcOrderDetailUrl) {
        WechatProgramMsgConfig.pcOrderDetailUrl = pcOrderDetailUrl;
    }

    @Value("${wechat.program.msg.pc.distributorListUrl}")
    public void setPcDistributorListUrl(String pcDistributorListUrl) {
        WechatProgramMsgConfig.pcDistributorListUrl = pcDistributorListUrl;
    }
}
