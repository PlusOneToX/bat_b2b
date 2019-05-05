package com.bat.thirdparty.msgcenter.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 小程序订阅消息请求参数
 */
@Data
public class SubscribeSend {

    /**
     * 接收者（用户）的 openid
     */
    @JSONField(ordinal = 1)
    private String touser;

    /**
     * 所需下发的订阅模板id
     */
    @JSONField(ordinal = 2)
    private String template_id;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
     */
    @JSONField(ordinal = 3)
    private String page;

    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     */
    @JSONField(ordinal = 4)
    private Object data;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    @JSONField(ordinal = 5)
    private String miniprogram_state;

    /**
     * 进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    @JSONField(ordinal = 6)
    private String lang;

}
