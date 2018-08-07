package com.bat.distributor.dao.platform.dataobject;

import lombok.Data;

@Data
public class WxPlatformDistributorDO {
    private Integer id;

    private Integer wxPlatformId;

    private Integer distributorId;

    private String name;

    private String companyName;

}