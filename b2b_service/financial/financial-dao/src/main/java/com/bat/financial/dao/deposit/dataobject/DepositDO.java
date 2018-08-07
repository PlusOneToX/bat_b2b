package com.bat.financial.dao.deposit.dataobject;

public class DepositDO {
    private Integer id;

    private Byte isShowPrestore;

    private Byte isOpenOnlineTopup;

    private Float onlineMinAmount;

    private Byte isOpenOfflineTopup;

    private Byte isOpenWithdrawal;

    private Long createTime;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getIsShowPrestore() {
        return isShowPrestore;
    }

    public void setIsShowPrestore(Byte isShowPrestore) {
        this.isShowPrestore = isShowPrestore;
    }

    public Byte getIsOpenOnlineTopup() {
        return isOpenOnlineTopup;
    }

    public void setIsOpenOnlineTopup(Byte isOpenOnlineTopup) {
        this.isOpenOnlineTopup = isOpenOnlineTopup;
    }

    public Float getOnlineMinAmount() {
        return onlineMinAmount;
    }

    public void setOnlineMinAmount(Float onlineMinAmount) {
        this.onlineMinAmount = onlineMinAmount;
    }

    public Byte getIsOpenOfflineTopup() {
        return isOpenOfflineTopup;
    }

    public void setIsOpenOfflineTopup(Byte isOpenOfflineTopup) {
        this.isOpenOfflineTopup = isOpenOfflineTopup;
    }

    public Byte getIsOpenWithdrawal() {
        return isOpenWithdrawal;
    }

    public void setIsOpenWithdrawal(Byte isOpenWithdrawal) {
        this.isOpenWithdrawal = isOpenWithdrawal;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}