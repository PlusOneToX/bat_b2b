package com.bat.thirdparty.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "nine.machine")
public class NineMachineConfig {
    private String hostName;
    private String apiCustomInfo;
    private String appKey;
    private Map<String,String> logistics;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getApiCustomInfo() {
        return apiCustomInfo;
    }

    public void setApiCustomInfo(String apiCustomInfo) {
        this.apiCustomInfo = apiCustomInfo;
    }

    public Map<String, String> getLogistics() {
        return logistics;
    }

    public void setLogistics(Map<String, String> logistics) {
        this.logistics = logistics;
    }
}
