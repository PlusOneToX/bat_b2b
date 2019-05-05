package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/6/31 16:15
 */
public class RebateVoucherRpcDTO implements Serializable {

    /**
     * 返利代金券id
     */
    private Integer id;

    /**
     * 分销商id
     */
    private Integer distributorId;

    /**
     * 分销商名称
     */
    private String distributorName;

    /**
     * 代金券券名
     */
    private String name;

    /**
     * 代金券编号
     */
    private String voucherNo;

    /**
     * 面值
     */
    private BigDecimal faceValue;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 申请状态 0草稿 1待审核 2审核通过(可用) 3审核拒绝
     */
    private Short applyStatus;

    /**
     * 冻结状态 10未冻结(可用) 11冻结
     */
    private Short freezeStatus;

    /**
     * 代金券状态（汇总） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结
     */
    private Short voucherStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Short getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Short applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Short getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(Short freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public Short getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(Short voucherStatus) {
        this.voucherStatus = voucherStatus;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
