package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorNextNameRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String companyName;
    private Integer treeNode;
    private Integer erpId;

    private Integer distributorAncestorId;


    private String distributorAncestorName;
    private String distributorAncestorCompanyName;

    public Integer getErpId() {
        return erpId;
    }

    public void setErpId(Integer erpId) {
        this.erpId = erpId;
    }

    public Integer getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Integer treeNode) {
        this.treeNode = treeNode;
    }

    public Integer getDistributorAncestorId() {
        return distributorAncestorId;
    }

    public void setDistributorAncestorId(Integer distributorAncestorId) {
        this.distributorAncestorId = distributorAncestorId;
    }

    public String getDistributorAncestorName() {
        return distributorAncestorName;
    }

    public void setDistributorAncestorName(String distributorAncestorName) {
        this.distributorAncestorName = distributorAncestorName;
    }

    public String getDistributorAncestorCompanyName() {
        return distributorAncestorCompanyName;
    }

    public void setDistributorAncestorCompanyName(String distributorAncestorCompanyName) {
        this.distributorAncestorCompanyName = distributorAncestorCompanyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
