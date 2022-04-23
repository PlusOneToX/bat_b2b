/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.config;

import cn.binarywang.wx.miniapp.api.WxMaAnalysisService;
import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaMsgServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.modules.wx.entity.WxMaConfigEntity;
import com.platform.modules.wx.service.WxMaConfigService;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * wechat ma configuration
 *
 * @author 李鹏军
 */
@Configuration
public class WxMaConfiguration {
    @Autowired
    private WxMaConfigService maConfigService;

    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaDefaultConfig());
        return wxMaService;
    }

    @Bean
    public WxMaQrcodeService wxMaQrcodeService() {
        return wxMaService().getQrcodeService();
    }

    @Bean
    public WxMaAnalysisService wxMaAnalysisService() {
        return wxMaService().getAnalysisService();
    }

    @Bean
    public WxMaMsgService wxMaMsgService(WxMaService service) {
        return new WxMaMsgServiceImpl(service);
    }

    @Bean
    public WxMaDefaultConfigImpl wxMaDefaultConfig() {
        WxMaDefaultConfigImpl wxMaInMemoryConfig = new WxMaDefaultConfigImpl();

        //todo 如果多个小程序需要优化，现在只支持一个小程序
        WxMaConfigEntity maConfigEntity = maConfigService.getOne(new QueryWrapper<>(), false);

        wxMaInMemoryConfig.setAppid(maConfigEntity.getAppId());
        wxMaInMemoryConfig.setSecret(maConfigEntity.getSecret());
        wxMaInMemoryConfig.setToken(maConfigEntity.getToken());
        wxMaInMemoryConfig.setAesKey(maConfigEntity.getAesKey());
        wxMaInMemoryConfig.setMsgDataFormat(maConfigEntity.getMsgDataFormat());
        return wxMaInMemoryConfig;
    }

    @Bean
    public WxMaMessageRouter newRouter(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router.rule().handler(logHandler).next()
                .rule().async(false).content("旗舰版").handler(textHandler).end()
                .rule().async(false).content("推广码").handler(qrcodeHandler).end();
        return router;
    }

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        System.out.println("收到消息：" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(
                WxMaKefuMessage.newTextBuilder().content("fromUser：" + wxMessage.getFromUser() + "；自动回复：您好，微同商城商业版，销售价格两万元，提供所有源码以及文档！如有需要请加客服微信（15209831990）咨询")
                        .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(
                WxMaKefuMessage.newTextBuilder().content("您好，旗舰版支持多商户模式，使用uni-app重构移动端，支持iOS、Android、H5、以及微信小程序等多个平台。如有需要请加客服微信（15209831990）咨询。")
                        .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            String page = "/pages/index/index?referrer=" + wxMessage.getFromUser();
            final File file = service.getQrcodeService().createWxaCode(page, 500, false, new WxMaCodeLineColor("0", "0", "0"), true);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .newImageBuilder()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

}
