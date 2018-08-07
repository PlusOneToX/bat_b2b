package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorErpIdRpcDO {
    private Integer id;

    private String name;

    private String companyName;

    private Integer treeNode;

    private Integer erpId;
}