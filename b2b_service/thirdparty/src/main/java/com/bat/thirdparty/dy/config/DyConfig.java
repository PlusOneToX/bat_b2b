package com.bat.thirdparty.dy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 沙漠
 */
@Data
@Component
@RefreshScope
public class DyConfig {

    @Value("${dy.code2.session.url:null}")
    private String dyCode2SessionUrl;

}
