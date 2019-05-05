package com.bat.dubboapi.distributor.platform.dto;


import java.io.Serializable;

public class DistributorPlatformApiRpcDTO implements Serializable {


    private static final long serialVersionUID = 8952741691636458629L;
    private Integer sysPlatformId;

    private String hostName;

    private String uri;

    /**
     * 开发源头
     */
    private Short developSource;


    private String method;


    private String appId;


    private String appKey;

    public Integer getSysPlatformId() {
        return sysPlatformId;
    }

    public void setSysPlatformId(Integer sysPlatformId) {
        this.sysPlatformId = sysPlatformId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Short getDevelopSource() {
        return developSource;
    }

    public void setDevelopSource(Short developSource) {
        this.developSource = developSource;
    }



    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
