package com.bat.thirdparty.common.error.flexible.exchange;

/**
 * 兑换卡对erp异常枚举类
 */
public enum ExchangeErpErrorConstant {

    ItemCodeNullError(10201,"物料编号不能为空"),

    BoxCodeNullError(10202,"盒码列表不能为空"),

    ItemNotBelongExchangeError(10203,"不属于兑换卡"),

    SyncBoxCodeAndPlainCodeToERPFail(10204,"同步盒码和明码到erp失败"),

    SyncExchangeCodeInvalidToERPFail(10205,"同步失效的兑换码到erp失败"),

    OrderNoNull(10206,"销售订单号不能为空"),

    BoxCodeAndPlainCodeNull(10207,"盒码和明码列表不能同时为空"),

    BoxCodeUpdateFailByNoSync(10208,"盒码尚未同步B2B、不需要修改"),

    BoxCodeUpdateFailByUsed(10209,"盒码中已有兑换卡被核销、不能修改"),

    BoxCodeUpdateFailByEnd(10210,"盒码中已有兑换卡过期、不能修改"),

    BoxCodeUpdateFailByInvalid(10211,"盒码中已有兑换卡作废、不能修改"),

    BoxCodeUpdateFailByNoChange(10212,"盒码跟新增的一样、不能修改还原"),

    CancelFailWithoutExchangeCode(10213,"该订单无关联已核销的兑换码"),

    BoxCodeRelaOrderFailByAlready(10214,"已分配B2B订单、不允许再发货"),

    BoxCodeRelaItemCodeError(10215,"对应的物料编码与兑换卡所属物料编码不一致"),

    OutboundNotBelongFirst(10216,"该出库单不属于首次出库盒码"),

    OutboundBelongFirst(10217,"该出库单属于首次出库盒码"),

    OutboundNoNotHaveThisOrder(10218,"该出库单之前出库没有该订单号"),

    OutboundNoHaveThisOrder(10219,"该出库单之前出库含有该订单号"),

    BoxCodeBindOrderUpdateFailBySame(10220,"该出库单关联的盒码前后一样、不需要修改"),

    BoxCodeBindOrderMoreThenOne(10221,"绑定订单超过一个"),

    BoxCodeByOneMustBeContinuous(10222,"单张卡一盒的兑换卡出库,盒码必须要连续"),

    BoxCodeOutbandUpdateFailByNoContinus(10223,"修改出库盒码、区间内还有这些盒码未出库、请先出库这些：【"),

    BoxCodeListNullError(10224,"无符合条件盒码明码同步至ERP"),
    ;


    private Integer code;

    private String msg;

    ExchangeErpErrorConstant(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
