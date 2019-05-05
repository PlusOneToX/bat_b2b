package com.bat.thirdparty.suning.request;

import lombok.Data;

@Data
public class CreateOrderRequest {

    /**
     * 直连供应商编码
     */
    private String supplierCode;
    /**
     * 服务订单号
     */
    private String orderId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 订单类型
     */
    private String orderTypeDesc;
    /**
     * 服务渠道
     */
    private String orderChannel;
    /**
     * 销售门店
     */
    private String storeCodeDec;
    /**
     * 服务单状态
     */
    private String statusCode;
    /**
     * 家电品牌编码
     */
    private String cmmdtyBand;
    /**
     * 家电品牌
     */
    private String cmmdtyBandDec;
    /**
     * 电器商品组名称
     */
    private String cmmdtyCtgryDec;
    /**
     * 服务商品名称
     */
    private String srvCmmdtyName;
    /**
     * 电器商品名称
     */
    private String cmmdtyName;
    /**
     * 作业项目描述
     */
    private String operateItemDec;
    /**
     * 购机日期
     */
    private String buyTime;
    /**
     * 服务资质代码
     */
    private String serviceAptitude;
    /**
     * 服务资质描述
     */
    private String serviceAptitudeDec;
    /**
     * 质保标识
     */
    private String cmmdtyQaType;
    /**
     * 顾客姓名
     */
    private String consignee;
    /**
     * 用户地址
     */
    private String srvAddress;
    /**
     * 电话
     */
    private String mobPhone;
    /**
     * 客户固话
     */
    private String phoneNum;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 城市描述
     */
    private String cityName;
    /**
     * 服务备注
     */
    private String srvMemo;
    /**
     * 物流预估到货时间
     */
    private String hopeArrivalTime;
    /**
     * 物流实际送货完成时间
     */
    private String operateTime;
    /**
     * 预约作业时间
     */
    private String srvTime;
    /**
     * 作业网点代码
     */
    private String wd;
    /**
     * 作业网点名称
     */
    private String wdName;
    /**
     * 作业网点电话
     */
    private String wdTel;
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
     * 故障类型编码
     */
    private String faultCode;
    /**
     * 待修机器故障描述
     */
    private String agencyFaultText;
    /**
     * 收旧问题描述
     */
    private String oldProblemDec;
    /**
     * 预估价格（收旧）改传OMS销售单号
     */
    private String evaluatePrice;
    /**
     * 二次预约价格（收旧
     */
    private String aferEvaluatePrice;
    /**
     * 附件信息
     */
    private String attachInfo;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 服务商品编码
     */
    private String srvcmmdtyCode;
    /**
     * 运输区域描述
     */
    private String srvAreaDec;
    /**
     * 运输区域
     */
    private String srvAreaCode;
    /**
     * 收旧list
     */
    private String sjwtList;


}
