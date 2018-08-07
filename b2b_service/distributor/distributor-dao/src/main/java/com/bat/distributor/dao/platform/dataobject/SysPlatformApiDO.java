package com.bat.distributor.dao.platform.dataobject;

import lombok.Data;

@Data
public class SysPlatformApiDO {
    private Integer id;

    private Integer sysPlatformId;

    private Short apiType;
    private String method;

    private String uri;

    private Short developSource;

}