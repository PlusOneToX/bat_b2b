package com.bat.distributor.service.common;

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
public class DistributorConfig {

    private String wxMiniProgramAppId;

    private String wxMiniProgramAppSecret;

    @Value("${country.china}")
    private Integer countryChina;
    @Value("${updateDistributorVisibleTime:2}")
    private String updateDistributorVisibleTime;
    @Value("${distributor.modelType:2}")
    private Short modelType;
    @Value("${distributor.defaultTenantNo:100}")
    private String defaultTenantNo;
}