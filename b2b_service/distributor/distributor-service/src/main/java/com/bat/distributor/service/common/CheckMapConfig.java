package com.bat.distributor.service.common;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("check")
@PropertySource(value = "classpath:check-map.properties")
public class CheckMapConfig {

    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @PostConstruct
    public void init() {
        System.out.print(map);
    }
}
