package com.bat.financial.pay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

import java.util.List;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/6 15:59
 */
@Data
@Component
public class AlipayConfig {
    @Value("#{'${alipay.test_appid}'.split(',')}")
    private List<String> testAppIds;
}
