package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorERPRpcDTO implements Serializable {
    private Integer id;
    private String companyName;
    /**
     * 业务员
     */
    private Integer salesId;
    /**
     * erp内码
     */
    private Integer erpId;
    /**
     * erp编码
     */
    private String erpNo;
    /**
     * 是否直发
     */
    private Short autoDelivery;
    /**
     * erp分组编码
     */
    private String erpGroupNo;

    /**
     * erp分类编码
     */
    private String erpCategoryNo;
    /**
     * 客户地址
     */
    private DistributorAddressRpcDTO address;
    /**
     * 客户联系人（主要联系人）
     */
    private DistributorContactsRpcDTO contacts;
    /**
     * 客户财务信息（收款条件、发票抬头等）
     */
    private DistributorFinancialRpcDO financial;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
    }

    public Integer getErpId() {
        return erpId;
    }

    public void setErpId(Integer erpId) {
        this.erpId = erpId;
    }

    public String getErpNo() {
        return erpNo;
    }

    public void setErpNo(String erpNo) {
        this.erpNo = erpNo;
    }

    public Short getAutoDelivery() {
        return autoDelivery;
    }

    public void setAutoDelivery(Short autoDelivery) {
        this.autoDelivery = autoDelivery;
    }

    public String getErpGroupNo() {
        return erpGroupNo;
    }

    public void setErpGroupNo(String erpGroupNo) {
        this.erpGroupNo = erpGroupNo;
    }

    public String getErpCategoryNo() {
        return erpCategoryNo;
    }

    public void setErpCategoryNo(String erpCategoryNo) {
        this.erpCategoryNo = erpCategoryNo;
    }

    public DistributorAddressRpcDTO getAddress() {
        return address;
    }

    public void setAddress(DistributorAddressRpcDTO address) {
        this.address = address;
    }

    public DistributorContactsRpcDTO getContacts() {
        return contacts;
    }

    public void setContacts(DistributorContactsRpcDTO contacts) {
        this.contacts = contacts;
    }

    public DistributorFinancialRpcDO getFinancial() {
        return financial;
    }

    public void setFinancial(DistributorFinancialRpcDO financial) {
        this.financial = financial;
    }
}
