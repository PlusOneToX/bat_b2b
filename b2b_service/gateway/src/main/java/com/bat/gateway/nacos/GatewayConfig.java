package com.bat.gateway.nacos;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/13 21:35
 */
@Data
@Component
@ConfigurationProperties(value = "authentication")
@RefreshScope
public class GatewayConfig {

    private List<String> onLimitsURI;
    private List<String> registerLoginURI;
    @Value("${swaggerURI:noURI}")
    private String swaggerURI;
    @Value("${payNotify:noURI}")
    private String payNotify;
    @Value("${refundNotify:noURI}")
    private String refundNotify;
    @Value("${thirdpartyOrderCancel:noURI}")
    private String thirdpartyOrderCancel;
    @Value("${platformURI:/platform/v1/web}")
    private String platformURI;

}
