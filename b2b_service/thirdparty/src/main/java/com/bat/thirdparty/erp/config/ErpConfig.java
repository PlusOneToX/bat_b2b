package com.bat.thirdparty.erp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/13 21:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "erp")
@RefreshScope
public class ErpConfig {

    private String username;

    private String password;

    private String dbId;

    private String lang;

    private String baseUrl;

    private String baseExtUrl;

    private String platform;
    /**
     * erp默认结算方式
     */
    private String erpSettleDefault;
    /**
     * erp线上结算方式
     */
    private String onlinePayType;
    /**
     * erp线下结算方式
     */
    private String offlinePayType;
    /**
     * 月结结算方式
     */
    private String monthPayType;

    @Value("${syncErpPurchaseAndOutboundOrderTime:5}")
    private Integer syncErpPurchaseAndOutboundOrderTime;
}
