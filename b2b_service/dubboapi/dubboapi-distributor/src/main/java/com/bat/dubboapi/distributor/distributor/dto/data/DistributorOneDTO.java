package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorOneDTO implements Serializable {
    private Integer id;
    private String name;
    private String companyName;
    private Integer treeNode;
    /**
     * 一级分销商信息（treeNode为1时，有可能是空）
     */
    private Integer distributorOneId;
    private String distributorOneName;
    private String distributorOneCompanyName;

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

    public Integer getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Integer treeNode) {
        this.treeNode = treeNode;
    }

    public Integer getDistributorOneId() {
        return distributorOneId;
    }

    public void setDistributorOneId(Integer distributorOneId) {
        this.distributorOneId = distributorOneId;
    }

    public String getDistributorOneName() {
        return distributorOneName;
    }

    public void setDistributorOneName(String distributorOneName) {
        this.distributorOneName = distributorOneName;
    }

    public String getDistributorOneCompanyName() {
        return distributorOneCompanyName;
    }

    public void setDistributorOneCompanyName(String distributorOneCompanyName) {
        this.distributorOneCompanyName = distributorOneCompanyName;
    }
}
