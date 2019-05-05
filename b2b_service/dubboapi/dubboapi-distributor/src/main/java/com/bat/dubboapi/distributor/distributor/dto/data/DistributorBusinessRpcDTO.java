package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorBusinessRpcDTO implements Serializable {
    private Integer id;
    /**
     * 资质申请状态 0未提交 1申请中 2申请通过 3申请失败
     */
    private Short applyStatus;
    /**
     * 资料审核状态 0未提交 1资料审核中  2资料审核通过  3资料审核失败
     */
    private Short profileStatus;
    /**
     * 冻结状态 1,冻结 2,未冻结
     */
    private Short freezeStatus;
    /**
     * 多级分销级数
     */
    private Short treeNode;
    /**
     * 自动下推出库 1.是 0.否
     */
    private Short autoDelivery;
    /**
     * 是否支持在途库存 1是 0否 默认是1
     */
    private Short onWayFlag;
    /**
     * 是否启用柔性店铺开关，1启用 0 不启用
     */
    private Short rxShopSwitch;
    /**
     * 分销商销售区域ids
     */
    private List<Integer> salesAreaIds;

    public List<Integer> getSalesAreaIds() {
        return salesAreaIds;
    }

    public void setSalesAreaIds(List<Integer> salesAreaIds) {
        this.salesAreaIds = salesAreaIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getAutoDelivery() {
        return autoDelivery;
    }

    public void setAutoDelivery(Short autoDelivery) {
        this.autoDelivery = autoDelivery;
    }

    public Short getOnWayFlag() {
        return onWayFlag;
    }

    public void setOnWayFlag(Short onWayFlag) {
        this.onWayFlag = onWayFlag;
    }

    public Short getRxShopSwitch() {
        return rxShopSwitch;
    }

    public void setRxShopSwitch(Short rxShopSwitch) {
        this.rxShopSwitch = rxShopSwitch;
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

    public Short getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(Short freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public Short getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Short treeNode) {
        this.treeNode = treeNode;
    }
}
