package com.bat.dubboapi.thirdparty.erp.dto.system.data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 13:50
 */
public class UserErpRpcDTO implements Serializable {
    private String name;
    private String erpUserNo;
    private String departmentName;
    private String erpDepartmentNo;
    private String organizationName;
    private String erpOrganizationNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErpUserNo() {
        return erpUserNo;
    }

    public void setErpUserNo(String erpUserNo) {
        this.erpUserNo = erpUserNo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getErpDepartmentNo() {
        return erpDepartmentNo;
    }

    public void setErpDepartmentNo(String erpDepartmentNo) {
        this.erpDepartmentNo = erpDepartmentNo;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getErpOrganizationNo() {
        return erpOrganizationNo;
    }

    public void setErpOrganizationNo(String erpOrganizationNo) {
        this.erpOrganizationNo = erpOrganizationNo;
    }

    @Override
    public String toString() {
        return "UserErpRpcDTO{" + "name='" + name + '\'' + ", erpUserNo='" + erpUserNo + '\'' + ", departmentName='"
            + departmentName + '\'' + ", erpDepartmentNo='" + erpDepartmentNo + '\'' + ", organizationName='"
            + organizationName + '\'' + ", erpOrganizationNo='" + erpOrganizationNo + '\'' + '}';
    }
}
