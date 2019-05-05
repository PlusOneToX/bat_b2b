package com.bat.promotion.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Data
@Component
@RefreshScope
public class PromotionConfig {
    private Logger logger = LoggerFactory.getLogger(PromotionConfig.class);

    @Value("${promotion.defaultTenantNo:100}")
    private String defaultTenantNo;
    @Value("${promotion.modelType:6}")
    private Short modelType;
    @Value("${promotion.intervalTime:3}")
    private Long intervalTime;

    @Value("${promotion.cronJobTime:24}")
    private Long cronJobTime;

    @Value("${promotion.jobGroup}")
    private Integer jobGroup;

    @Value("${promotion.author}")
    private String author;
    @Value("${promotion.promotionStartJobHandler}")
    private String promotionStartJobHandler;
    @Value("${promotion.promotionStopJobHandler}")
    private String promotionStopJobHandler;
    @Value("${promotion.groupSeckillStartJobHandler}")
    private String groupSeckillStartJobHandler;
    @Value("${promotion.groupSeckillStopJobHandler}")
    private String groupSeckillStopJobHandler;
    @Value("${promotion.couponStartJobHandler}")
    private String couponStartJobHandler;
    @Value("${promotion.couponStopJobHandler}")
    private String couponStopJobHandler;

    @Value("${promotion.rebateVoucherStartJobHandler}")
    private String rebateVoucherStartJobHandler;
    @Value("${promotion.rebateVoucherStopJobHandler}")
    private String rebateVoucherStopJobHandler;

    @Value("${promotion.importtemp:}")
    private String importTemp;
    @Value("${promotion.couponTobatDelayLevel}")
    private String couponTobatDelayLevel;

}