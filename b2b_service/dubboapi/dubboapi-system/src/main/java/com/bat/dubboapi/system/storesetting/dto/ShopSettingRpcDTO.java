package com.bat.dubboapi.system.storesetting.dto;

import java.io.Serializable;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/6/13 10:28
 */
public class ShopSettingRpcDTO implements Serializable {
    /**
     * 定制商品是否参与活动
     */
    private Integer customizedAttendEventFlag;
    /**
     * 直运订单是否参与活动
     */
    private Integer directTransportationEventFlag;
    /**
     * 提示语
     */
    private String hint;
    /**
     * mto订单是否参与活动
     */
    private Integer mtoAttendEventFlag;
    /**
     * 上架多少天内为新品时间
     */
    private Integer newproductTime;
    /**
     * 非直发分销商采用提示语 0否 1是
     */
    private Integer noStiffUseHint;
    /**
     * 在途商品是否参与活动
     */
    private Integer onWayAttendEventFlag;
    /**
     * 直发分销商采用提示语 0否 1是
     */
    private Integer stiffUseHint;
    /**
     * 库存显示方式：0-显示实际库存，1-显示模糊库存
     */
    private Integer stockShowFlag;
    /**
     * 库存显示临界值
     */
    private Integer stockShowNumber;

    public Integer getCustomizedAttendEventFlag() {
        return customizedAttendEventFlag;
    }

    public void setCustomizedAttendEventFlag(Integer customizedAttendEventFlag) {
        this.customizedAttendEventFlag = customizedAttendEventFlag;
    }

    public Integer getDirectTransportationEventFlag() {
        return directTransportationEventFlag;
    }

    public void setDirectTransportationEventFlag(Integer directTransportationEventFlag) {
        this.directTransportationEventFlag = directTransportationEventFlag;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getMtoAttendEventFlag() {
        return mtoAttendEventFlag;
    }

    public void setMtoAttendEventFlag(Integer mtoAttendEventFlag) {
        this.mtoAttendEventFlag = mtoAttendEventFlag;
    }

    public Integer getNewproductTime() {
        return newproductTime;
    }

    public void setNewproductTime(Integer newproductTime) {
        this.newproductTime = newproductTime;
    }

    public Integer getNoStiffUseHint() {
        return noStiffUseHint;
    }

    public void setNoStiffUseHint(Integer noStiffUseHint) {
        this.noStiffUseHint = noStiffUseHint;
    }

    public Integer getOnWayAttendEventFlag() {
        return onWayAttendEventFlag;
    }

    public void setOnWayAttendEventFlag(Integer onWayAttendEventFlag) {
        this.onWayAttendEventFlag = onWayAttendEventFlag;
    }

    public Integer getStiffUseHint() {
        return stiffUseHint;
    }

    public void setStiffUseHint(Integer stiffUseHint) {
        this.stiffUseHint = stiffUseHint;
    }

    public Integer getStockShowFlag() {
        return stockShowFlag;
    }

    public void setStockShowFlag(Integer stockShowFlag) {
        this.stockShowFlag = stockShowFlag;
    }

    public Integer getStockShowNumber() {
        return stockShowNumber;
    }

    public void setStockShowNumber(Integer stockShowNumber) {
        this.stockShowNumber = stockShowNumber;
    }
}
