package com.bat.distributor.dao.platform.dataobject;

import lombok.Data;

@Data
public class DistributorPlatformApiRpcDO {

    private Integer sysPlatformId;

    private String hostName;

    private String uri;

    /**
     * 开发源头
     */
    private Short developSource;

    /**
     * 回调uri
     */
    private String queryUri;

    private String appId;

    private String appKey;

    private Short apiType;
    private String method;
}
