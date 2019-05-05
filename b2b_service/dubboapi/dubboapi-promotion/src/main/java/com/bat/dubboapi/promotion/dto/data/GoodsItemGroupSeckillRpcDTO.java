package com.bat.dubboapi.promotion.dto.data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsItemGroupSeckillRpcDTO implements Serializable {
    private Integer id;
    private Integer groupSeckillId;
    private String name;
    private Short groupSeckillDesc;
    private Short groupSeckillType;
    private Date startTime;
    private Date endTime;
    private Short groupSeckillStatus;
    private Integer itemId;
    private Short existFlag;
    private Short mtoFlag;
    private Integer maxNum;
    private Integer minNum;
    private BigDecimal groupSeckillPrice;
    private Integer virtualSum;
    private Integer realSum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupSeckillId() {
        return groupSeckillId;
    }

    public void setGroupSeckillId(Integer groupSeckillId) {
        this.groupSeckillId = groupSeckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getGroupSeckillDesc() {
        return groupSeckillDesc;
    }

    public void setGroupSeckillDesc(Short groupSeckillDesc) {
        this.groupSeckillDesc = groupSeckillDesc;
    }

    public Short getGroupSeckillType() {
        return groupSeckillType;
    }

    public void setGroupSeckillType(Short groupSeckillType) {
        this.groupSeckillType = groupSeckillType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getGroupSeckillStatus() {
        return groupSeckillStatus;
    }

    public void setGroupSeckillStatus(Short groupSeckillStatus) {
        this.groupSeckillStatus = groupSeckillStatus;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Short getExistFlag() {
        return existFlag;
    }

    public void setExistFlag(Short existFlag) {
        this.existFlag = existFlag;
    }

    public Short getMtoFlag() {
        return mtoFlag;
    }

    public void setMtoFlag(Short mtoFlag) {
        this.mtoFlag = mtoFlag;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Integer getMinNum() {
        return minNum;
    }

    public void setMinNum(Integer minNum) {
        this.minNum = minNum;
    }

    public BigDecimal getGroupSeckillPrice() {
        return groupSeckillPrice;
    }

    public void setGroupSeckillPrice(BigDecimal groupSeckillPrice) {
        this.groupSeckillPrice = groupSeckillPrice;
    }

    public Integer getVirtualSum() {
        return virtualSum;
    }

    public void setVirtualSum(Integer virtualSum) {
        this.virtualSum = virtualSum;
    }

    public Integer getRealSum() {
        return realSum;
    }

    public void setRealSum(Integer realSum) {
        this.realSum = realSum;
    }
}
