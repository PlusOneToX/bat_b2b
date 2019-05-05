package com.bat.dubboapi.distributor.wx.dto;

import java.io.Serializable;


public class WxPlatformRpcDTO implements Serializable {
    private static final long serialVersionUID = 5368235198473162440L;
    private Integer id;

    private String platform;

    private Short type;

    private String name;

    private String appId;

    private String appSecret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}