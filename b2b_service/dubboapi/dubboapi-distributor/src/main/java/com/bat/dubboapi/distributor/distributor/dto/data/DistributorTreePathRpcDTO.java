package com.bat.dubboapi.distributor.distributor.dto.data;
import java.io.Serializable;

public class DistributorTreePathRpcDTO implements Serializable {

    private Integer id;

    private Integer distributorAncestorId;

    private Integer distributorDescendantId;

    private Integer treeNode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorAncestorId() {
        return distributorAncestorId;
    }

    public void setDistributorAncestorId(Integer distributorAncestorId) {
        this.distributorAncestorId = distributorAncestorId;
    }

    public Integer getDistributorDescendantId() {
        return distributorDescendantId;
    }

    public void setDistributorDescendantId(Integer distributorDescendantId) {
        this.distributorDescendantId = distributorDescendantId;
    }

    public Integer getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Integer treeNode) {
        this.treeNode = treeNode;
    }
}
