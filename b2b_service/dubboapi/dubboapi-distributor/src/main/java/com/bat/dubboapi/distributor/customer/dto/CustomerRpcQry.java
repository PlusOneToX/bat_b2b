package com.bat.dubboapi.distributor.customer.dto;

import java.io.Serializable;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/3/30 19:16
 */
public class CustomerRpcQry implements Serializable {
    /**
     * 第三方系统唯一标识码
     */
    private String openId;
    /**
     * 第三方系统其他标识码
     */
    private String otherUid;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 分销商id
     */
    private Integer distributorId;
    /**
     * 此用户是否已经被冻结 1为否,2为已冻结
     */
    private Short status = 1;

    /**
     * 平台绑定编码(分销商平台编码)
     */
    private String platform;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOtherUid() {
        return otherUid;
    }

    public void setOtherUid(String otherUid) {
        this.otherUid = otherUid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
