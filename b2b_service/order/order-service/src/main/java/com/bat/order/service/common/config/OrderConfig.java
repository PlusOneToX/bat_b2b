package com.bat.order.service.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * config
 *
 * @author 2017-04-28
 */
@Data
@Component
@RefreshScope
public class OrderConfig {

    @Value("${order.modelType}")
    private Short modelType;
    @Value("${order.defaultTenantNo:100}")
    private String defaultTenantNo;
    @Value("${order.type.diyid:1}")
    private Integer diyOrderTypeId;
    @Value("${order.platform.exchange.cards}")
    private List<String> platformCards;
    @Value("${order.platform.exchange.rongyaoexchange}")
    private String rongyaoexchange;
    private String wlfItemNo;
    @Value("${order.orderAsynErpDelayLevel}")
    private String orderAsynErpDelayLevel;
    @Value("${order.orderTreeNodeDataLevel:2}")
    private String orderTreeNodeDataLevel;
    @Value("${order.orderFlagbatLevel:2}")
    private String orderFlagbatLevel;
    @Value("${order.orderTimerPowerOffDLevel}")
    private String orderTimerPowerOffDLevel;
    @Value("${order.orderTimerPowerOffCLevel}")
    private String orderTimerPowerOffCLevel;
    @Value("${order.country.china:37}")
    private Integer countryChina;
    @Value("${order.erpPurchase:0}")
    private Short erpPurchase;
    /**
     * B端订单即将关闭消息延迟时间
     */
    @Value("${order.orderTimerWillPowerOffDLevel}")
    private String orderTimerWillPowerOffDLevel;
    /**
     * C端订单即将关闭消息延迟时间
     */
    @Value("${order.orderTimerWillPowerOffCLevel}")
    private String orderTimerWillPowerOffCLevel;
    @Value("${order.notDeliveryAutoCloseSwitch:0}")
    private Short notDeliveryAutoCloseSwitch;
}