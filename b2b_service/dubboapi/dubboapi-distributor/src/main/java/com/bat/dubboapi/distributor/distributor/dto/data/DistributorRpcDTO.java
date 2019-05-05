package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String companyName;
    private Short distributionPayWay;
    /**
     * 父级分销商信息（有可能是空）
     */
    private Integer distributorAncestorId;
    private String distributorAncestorName;
    private String distributorAncestorCompanyName;
    /**
     * 父级分销结算方式： 1 平台方收款(比如：bat收款，bat收款，如果现款情况，二级以上分销商只支持现款线上支付，一级分销商可支持现款线下支付及线下银行转账), 2 自己收款(分销商自己收款)
     */
    private Short ancestorDistributionMode;
    /**
     * 是否开启C端模式 0 不开启, 1 开启
     */
    private Short customerFlag;
    /**
     * C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)
     */
    private Short customerMode;
    /**
     * 业务员
     */
    private Integer salesId;
    private String salesName;
    /**
     * 商务员
     */
    private Integer coordinatorId;
    private String coordinatorName;
    /**
     * 分销商组织归属
     */
    private Integer organizationId;
    private String organizationErp;
    private String organizationName;
    /**
     * 冻结状态 1,冻结 2,未冻结
     */
    private Short freezeStatus;
    /**
     * 分销商级数
     */
    private Integer treeNode;
    /**
     * 信息是否同步到erp 1.是 0.否
     */
    private Short erpFlag;
    private Integer erpId;
    private String erpNo;
    /**
     * ERP余额是否同步 1.是 0.否
     */
    private Short erpBalanceFlag;

    /**
     * 是否直发
     */
    private Short autoDelivery;
    /**
     * 结算方式id
     */
    private Integer tradeId;
    /**
     * 结算方式类型，1为立即支付，2为期间结算
     */
    private Short payWay;
    /**
     * erp结算方式编码
     */
    private String erpSettleAccountNo;
    /**
     * 是否支持在途库存 1是 0否
     */
    private Short onWayFlag;
    /**
     * 分销商区域
     */
    private List<Integer> salesAreaIds;
    /**
     * 资质申请状态 0未提交 1申请中 2申请通过 3申请失败
     */
    private Short applyStatus;
    /**
     * 资料审核状态 0未提交 1资料审核中 2资料审核通过 3资料审核失败
     */
    private Short profileStatus;
    /**
     * 订单类型id
     */
    private Integer orderTypeId;
    /**
     * 父级分销订单是否自动审核： 1 是 2 否（注意：下级分销订单自动审核）
     */
    private Short ancestorDistributionAutoFlag;

    public Short getAncestorDistributionMode() {
        return ancestorDistributionMode;
    }

    public void setAncestorDistributionMode(Short ancestorDistributionMode) {
        this.ancestorDistributionMode = ancestorDistributionMode;
    }

    public Short getAncestorDistributionAutoFlag() {
        return ancestorDistributionAutoFlag;
    }

    public void setAncestorDistributionAutoFlag(Short ancestorDistributionAutoFlag) {
        this.ancestorDistributionAutoFlag = ancestorDistributionAutoFlag;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationErp() {
        return organizationErp;
    }

    public void setOrganizationErp(String organizationErp) {
        this.organizationErp = organizationErp;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Short getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(Short customerFlag) {
        this.customerFlag = customerFlag;
    }

    public Short getCustomerMode() {
        return customerMode;
    }

    public void setCustomerMode(Short customerMode) {
        this.customerMode = customerMode;
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

    public Integer getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(Integer orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public Short getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Short applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Short getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(Short profileStatus) {
        this.profileStatus = profileStatus;
    }

    public Short getErpFlag() {
        return erpFlag;
    }

    public void setErpFlag(Short erpFlag) {
        this.erpFlag = erpFlag;
    }

    public Short getOnWayFlag() {
        return onWayFlag;
    }

    public void setOnWayFlag(Short onWayFlag) {
        this.onWayFlag = onWayFlag;
    }

    public List<Integer> getSalesAreaIds() {
        return salesAreaIds;
    }

    public void setSalesAreaIds(List<Integer> salesAreaIds) {
        this.salesAreaIds = salesAreaIds;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public String getErpSettleAccountNo() {
        return erpSettleAccountNo;
    }

    public void setErpSettleAccountNo(String erpSettleAccountNo) {
        this.erpSettleAccountNo = erpSettleAccountNo;
    }

    public Integer getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public Short getAutoDelivery() {
        return autoDelivery;
    }

    public void setAutoDelivery(Short autoDelivery) {
        this.autoDelivery = autoDelivery;
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

    public Short getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(Short freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public Integer getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Integer treeNode) {
        this.treeNode = treeNode;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
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

    public Short getErpBalanceFlag() {
        return erpBalanceFlag;
    }

    public void setErpBalanceFlag(Short erpBalanceFlag) {
        this.erpBalanceFlag = erpBalanceFlag;
    }

    public Short getDistributionPayWay() {
        return distributionPayWay;
    }

    public void setDistributionPayWay(Short distributionPayWay) {
        this.distributionPayWay = distributionPayWay;
    }
}
