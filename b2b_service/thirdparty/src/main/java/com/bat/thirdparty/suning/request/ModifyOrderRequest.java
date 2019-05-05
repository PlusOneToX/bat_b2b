package com.bat.thirdparty.suning.request;

import lombok.Data;

@Data
public class ModifyOrderRequest {

    /**
     * 服务订单号
     */
    private String serviceNum;
    /**
     * 物流预估到货时间
     */
    private String hopeArrivalTime;
    /**
     * 物流实际送货完成时间
     */
    private String operateTime;
    /**
     * 家电品牌
     */
    private String cmmdtyBandDec;
    /**
     * 电器商品名称
     */
    private String cmmdtyName;
    /**
     * 电器商品组名称
     */
    private String cmmdtyCtgryDec;
    /**
     * 服务商品名称
     */
    private String srvCmmdtyName;
    /**
     * 作业项目描述
     */
    private String operateItemDec;

    /**
     * 质保标识
     */
    private String cmmdtyQaType;
    /**
     * 延保单号
     */
    private String ybOrderItemId;
    /**
     * 延保供应商
     */
    private String ybSupply;
    /**
     * 延保供应商名称
     */
    private String ybSupplyDec;
    /**
     * 顾客姓名
     */
    private String Consignee;
    /**
     * 电话
     */
    private String mobPhone;
    /**
     * 用户地址
     */
    private String srvAddress;
    /**
     * 预约作业时间
     */
    private String srvTime;
    /**
     *  更改预约时间原因描述
     */
    private String changeReasonDes;
    /**
     * 服务备注
     */
    private String srvMemo;
    /**
     * 改单原因
     */
    private String orderChangeReason;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 运输区域描述
     */
    private String srvAreaDec;
    /**
     * 运输区域
     */
    private String srvAreaCode;
    /**
     * 直连供应商编码
     */
    private String supplierCode;

}
