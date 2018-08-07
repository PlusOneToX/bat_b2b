package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorTreePathDO {
    private Integer id;

    private Integer distributorAncestorId;

    private Integer distributorDescendantId;

    private Integer treeNode;
}