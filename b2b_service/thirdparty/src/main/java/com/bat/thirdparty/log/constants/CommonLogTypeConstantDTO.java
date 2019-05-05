package com.bat.thirdparty.log.constants;

public class CommonLogTypeConstantDTO {

    public static final String SamsungCheckToken = "客户token验证";
    public static final String SamsungIsReceived = "侧判断客户是否有领过券";
    public static final String ErpOrderChange = "ERP集成订单变更";
    public static final String ErpOrderCheck = "ERP集成订单审核";
    public static final String ErpDistributorSync = "ERP集成分销商同步";
    public static final String ErpVouchersSync = "ERP集成收款单同步";
    public static final String ErpWarehouseStockChange = "ERP集成库存变更接口（订单解锁和非B2B订单货品同步库存）";
    public static final String ErpExchangeCardBindOrderAndBoxCode = "ERP兑换卡-erp出库、绑定订单和盒码关系";
    public static final String ErpExchangeCardRefund = "ERP兑换卡退货";
    public static final String MaikeDeliverOrder = "麦客发货";
    public static final String DuohongDeliverOrder = "多鸿发货";
    public static final String KDSFKDeliverOrder = "壳大师发货";
    public static final String HaixingDeliverOrder = "海星发货";
    public static final String OrderOpenOrderConfirm = "第三方对外订单接口订单确认、基于id对接";
    public static final String OrderOpenSyncOrder = "第三方对外订单接口（基于编码的对接）";
    public static final String OrderOpenCancel = "第三方对外订单接口第三方取消订单接口";
    public static final String OrderOpenCreate = "第三方对外订单接口创建临时订单、提交到第三方接口";
    public static final String ErpSyncDistributor = "ERP集成分销商同步";
    public static final String ErpCreateVouchers = "ERP集成ERP创建收款单";
    public static final String ErpChangeDeliverOrder = "ERP集成ERP出库单状态变更";
    public static final String ErpCreateDeliverOrder = "ERP集成ERP出库单创建";
    public static final String ErpGoodsCustomInfoIsStockOut = "ERP集成ERP设置型号和材质关系是否缺货";
    public static final String ErpAccountBalanceChange = "ERP集成余额变更";
    public static final String GoodsUpdateItemPrice = "后台更新商品价格";
}
