package com.bat.dubboapi.thirdparty.erp.dto.system.data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 13:50
 */
public class DepartmentErpRpcDTO implements Serializable {

    /**
     * erp 编码
     */
    private String erpDepartmentId;

    /**
     * erp 部门名称
     */
    private String departmentName;

    /**
     * 上级部门ERP 编码
     */
    private String parentErpId;

    /**
     * erp 上级部门名称
     */
    private String parentName;

    /**
     * 使用机构ERP 编码
     */
    private String orgId;

    /**
     * 使用机构ERP 名称
     */
    private String orgName;

    public String getErpDepartmentId() {
        return erpDepartmentId;
    }

    public void setErpDepartmentId(String erpDepartmentId) {
        this.erpDepartmentId = erpDepartmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getParentErpId() {
        return parentErpId;
    }

    public void setParentErpId(String parentErpId) {
        this.parentErpId = parentErpId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String toString() {
        return "DepartmentErpDTO{" + "erpDepartmentId='" + erpDepartmentId + '\'' + ", departmentName='"
            + departmentName + '\'' + ", parentErpId='" + parentErpId + '\'' + ", parentName='" + parentName + '\''
            + ", orgId='" + orgId + '\'' + ", orgName='" + orgName + '\'' + '}';
    }
}
