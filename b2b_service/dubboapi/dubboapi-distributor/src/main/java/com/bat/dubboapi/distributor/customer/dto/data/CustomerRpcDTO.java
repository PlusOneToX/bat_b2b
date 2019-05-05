package com.bat.dubboapi.distributor.customer.dto.data;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class CustomerRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String nikeName;
    /**
     * C端客户状态
     */
    private Short customerStatus;
    /**
     * C端客户归属平台状态
     */
    private Short platformStatus;
    private String platform;
    /**
     * 分销商平台状态, 1启用,0停用
     */
    private Short platformOpenFlag;
    /**
     * C端客户分销商信息
     */
    private DistributorRpcDTO distributor;

    private String otherId;

    private String openId;

    private String headPortrait;

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

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public Short getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Short customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Short getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(Short platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Short getPlatformOpenFlag() {
        return platformOpenFlag;
    }

    public void setPlatformOpenFlag(Short platformOpenFlag) {
        this.platformOpenFlag = platformOpenFlag;
    }

    public DistributorRpcDTO getDistributor() {
        return distributor;
    }

    public void setDistributor(DistributorRpcDTO distributor) {
        this.distributor = distributor;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
