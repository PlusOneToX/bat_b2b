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

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(WxOpenService.class)
@EnableConfigurationProperties(WxOpenProperties.class)
public class WxOpenConfiguration {
    private WxOpenProperties properties;

    @Autowired
    public WxOpenConfiguration(WxOpenProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WxOpenService wxOpenService() {
        WxOpenService wxOpenService = new WxOpenServiceImpl();
        WxOpenInMemoryConfigStorage wxMpInRedisConfigStorage = new WxOpenInMemoryConfigStorage();
        wxMpInRedisConfigStorage.setComponentAppId(properties.getComponentAppId());
        wxMpInRedisConfigStorage.setComponentAppSecret(properties.getComponentSecret());
        wxMpInRedisConfigStorage.setComponentToken(properties.getComponentToken());
        wxMpInRedisConfigStorage.setComponentAesKey(properties.getComponentAesKey());
        wxOpenService.setWxOpenConfigStorage(wxMpInRedisConfigStorage);
        return wxOpenService;
    }

    @Bean
    public WxOpenMessageRouter openMessageRouter(WxOpenService wxOpenService) {
        final WxOpenMessageRouter wxOpenMessageRouter = new WxOpenMessageRouter(wxOpenService);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();

        return wxOpenMessageRouter;
    }
}
