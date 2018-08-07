package com.bat.distributor.dao.platform.dataobject;

import lombok.Data;

@Data
public class SysPlatformDistributorDO {
    private Integer id;

    private Integer sysPlatformId;

    private Integer distributorId;

    private String name;

    private String companyName;

}