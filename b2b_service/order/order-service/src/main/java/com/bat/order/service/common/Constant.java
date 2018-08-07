package com.bat.order.service.common;

public class Constant {

    /**
     * 可视范围常量 0 不指定 1 指定全部 2 指定分销商等级 3 指定分销商 4 指定销售部门 5 指定业务员
     */
    public static final Short SCOPE_NULL = 0;
    public static final Short SCOPE_ALL = 1;
    public static final Short SCOPE_SCALE_PRICE = 2;
    public static final Short SCOPE_DISTRIBUTOR = 3;
    public static final Short SCOPE_DEPARTMENT = 4;
    public static final Short SCOPE_ADMIN = 5;

    /**
     * 停用启用状态 1 启用 0 停用
     */
    public final static Short OPEN_NO = 0;
    public final static Short OPEN_YES = 1;

    /**
     * 操作类型 1 新增 2 修改 3 删除
     */
    public final static Short OPERATION_TYPE_1 = 1;
    public final static Short OPERATION_TYPE_2 = 2;
    public final static Short OPERATION_TYPE_3 = 3;

    /**
     * 货品规格类型 1.规格，2.颜色
     */
    public final static Short ATTRIBUTE_TYPE_1 = 1;
    public final static Short ATTRIBUTE_TYPE_2 = 2;

    /**
     * 上下架状态 1未上架，2审批中，3已上架
     */
    public final static Short SALE_STATUS_1 = 1;
    public final static Short SALE_STATUS_2 = 2;
    public final static Short SALE_STATUS_3 = 3;

    /**
     * 冻结状态，1未冻结，2冻结
     */
    public final static Short FREEZE_STATUS_1 = 1;
    public final static Short FREEZE_STATUS_2 = 2;

    /**
     * 数据是否删除 0 正常 1 删除
     */
    public final static Short COMMON_DEL_FLAG_NO = 0;
    public final static Short COMMON_DEL_FLAG_YES = 1;

    /**
     * 定制类型 0-标准定制 1-DIY定制
     */
    public static final Short COMMON_CUSTOM_TYPE_STANDARD = 0;

    /**
     * 定制类型 0-标准定制 1-DIY定制
     */
    public static final Short COMMON_CUSTOM_TYPE_DIY = 1;

    /**
     * 商品是否有效 1、有效 0、无效
     */
    public static final Short OPEN_FLAG_0 = 0;
    public static final Short OPEN_FLAG_1 = 1;

    /**
     * 商品类型 1-普通 2-定制
     */
    public static final Short GOODS_TYPE_1 = 1;
    public static final Short GOODS_TYPE_2 = 2;

    /**
     * 是否赠品 1 非赠品 2 赠品
     */
    public static final Short ITEM_TYPE_1 = 1;
    public static final Short ITEM_TYPE_2 = 2;

    /**
     * 信息是否同步到erp 1.是 0.否
     */
    public static final Short ERP_FLAG_0 = 0;
    public static final Short ERP_FLAG_1 = 1;

    /**
     * 资质申请状态 0未提交 1申请中 2申请通过 3申请失败
     */
    public final static Short APPLY_STATUS_0 = 0;
    public final static Short APPLY_STATUS_1 = 1;
    public final static Short APPLY_STATUS_2 = 2;
    public final static Short APPLY_STATUS_3 = 3;

    /**
     * 资料审核状态 0未提交 1资料审核中 2资料审核通过 3资料审核失败
     */
    public final static Short PROFILE_STATUS_0 = 0;
    public final static Short PROFILE_STATUS_1 = 1;
    public final static Short PROFILE_STATUS_2 = 2;
    public final static Short PROFILE_STATUS_3 = 3;
    /**
     * 是否支持在途库存 1是 0否
     */
    public final static Short ON_WAY_FLAG_0 = 0;
    public final static Short ON_WAY_FLAG_1 = 1;

    /**
     * 自动下推出库 1.是 0.否
     */
    public final static Short AUTO_DELIVERY_0 = 0;
    public final static Short AUTO_DELIVERY_1 = 1;

    /**
     * 语言 en英文 zh中文
     */
    public final static String LANGUAGE_EN = "en";
    public final static String LANGUAGE_ZH = "zh";

    /**
     * 是否预售 1 是 0 否
     */
    public final static Short MTO_TYPE_0 = 0;
    public final static Short MTO_TYPE_1 = 1;

    /**
     * 是否获取建议零售价：1是 0否
     */
    public final static Short RETAIL_PRICE_FLAG_1 = 1;
    public final static Short RETAIL_PRICE_FLAG_0 = 0;

    /**
     * 是否支持超卖 1、支持 0 不支持
     */
    public final static Short OVERSOLD_FLAG_1 = 1;
    public final static Short OVERSOLD_FLAG_0 = 0;
    /**
     * 订单是否拆分(在途在库) 1、拆 0、否
     */
    public final static Short ON_WAY_SPLIT_FLAG_0 = 0;
    public final static Short ON_WAY_SPLIT_FLAG_1 = 1;

    /**
     * 在途商品是否参与活动 1.参与，0.不参与
     */
    public final static Short ON_WAY_PROMOTION_FLAG_0 = 0;
    public final static Short ON_WAY_PROMOTION_FLAG_1 = 1;

    /**
     * 是否开启C端模式 0 不开启, 1 开启
     */
    public final static Short CUSTOMER_FLAG_0 = 0;
    public final static Short CUSTOMER_FLAG_1 = 1;

    /**
     * C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)
     */
    public final static Short CUSTOMER_MODE_1 = 1;
    public final static Short CUSTOMER_MODE_2 = 2;
    public final static Short CUSTOMER_MODE_3 = 3;

    /**
     * 配送方式类型：1 普通商品（标品） 2 定制工厂
     */
    public final static Short LOGISTICS_TYPE_1 = 1;
    public final static Short LOGISTICS_TYPE_2 = 2;

    /**
     * 是否直接下单 1.是认0.否
     */
    public final static Short DIRECT_FLAG_0 = 0;
    public final static Short DIRECT_FLAG_1 = 1;
    /**
     * 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
     */
    public final static Short MAIL_SETTING_1 = 1;
    public final static Short MAIL_SETTING_2 = 2;
    public final static Short MAIL_SETTING_3 = 3;

    /**
     * 交易方类型： 1分销商 2 C端客户
     */
    public final static Short COUNTERPARTY_TYPE_1 = 1;
    public final static Short COUNTERPARTY_TYPE_2 = 2;

    /**
     * 分销订单是否自动审核： 1 是 2 否（注意：下级分销订单自动审核）
     */
    public final static Short AUTO_FLAG_0 = 0;
    public final static Short AUTO_FLAG_1 = 1;

    /**
     * 分销结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)
     */
    public final static Short DISTRIBUTION_MODE_1 = 1;
    public final static Short DISTRIBUTION_MODE_2 = 2;
    public final static Short DISTRIBUTION_MODE_3 = 3;

    public final static Short DISTRIBUTION_PAY_WAY_0 = 0;
    public final static Short DIRECTFLAG_1 = 1;

    /**
     * 1 平台方收款(比如：bat收款，bat收款), 2 非平台方（按分销商收款）
     */
    public final static Short TRADE_MODE_1 = 1;
    public final static Short TRADE_MODE_2 = 2;

    /**
     * 佣金类型 1.增加，2.减少
     */
    public final static Short COMMISSION_TYPE_1 = 1;
    public final static Short COMMISSION_TYPE_2 = 2;
    /**
     * 结算方式类型，1为立即支付，2为期间结算
     */
    public final static Short PAY_TYPE_1 = 1;
    public final static Short PAY_TYPE_2 = 2;

    /**
     * 是否正在使用活动 1 是 0 否
     */
    public final static Short USE_FLAG_0 = 0;
    public final static Short USE_FLAG_1 = 1;

    /**
     * 0 未发布,1 未开始, 2 进行中, 3 已过期,4 提前结束 5已作废 6已使用
     */
    public final static Short COUPON_STATUS_0 = 0;
    public final static Short COUPON_STATUS_1 = 1;
    public final static Short COUPON_STATUS_2 = 2;
    public final static Short COUPON_STATUS_3 = 3;
    public final static Short COUPON_STATUS_4 = 4;
    public final static Short COUPON_STATUS_5 = 5;
    public final static Short COUPON_STATUS_6 = 6;

    /**
     * 销售订单明细变更类型：A 增加 B 修改 D 删除
     */
    public final static String ORDER_ERP_CHANGE_A = "A";
    public final static String ORDER_ERP_CHANGE_B = "B";
    public final static String ORDER_ERP_CHANGE_D = "D";

    /**
     * 锁定类型 1.在库 2.在途 3.vmi
     */
    public final static Short LOCK_TYPE_1 = 1;
    public final static Short LOCK_TYPE_2 = 2;
    public final static Short LOCK_TYPE_3 = 3;

    /**
     * 发票类型 1.普通 2.增值税发票
     */
    public final static Short INVOICE_TYPE_1 = 1;
    public final static Short INVOICE_TYPE_2 = 2;

    /**
     * 是否开具发票 0.否，1.是
     */
    public final static Short INVOICE_FLAG_0 = 0;
    public final static Short INVOICE_FLAG_1 = 1;

    /**
     * 接收方类型 1分销商 2 C端客户
     */
    public final static Short RECEIVER_TYPE_1 = 1;
    public final static Short RECEIVER_TYPE_2 = 2;

    /**
     * 业务类型 1订单取消 2订单变更
     */
    public final static Short REFUND_BUSINESS_TYPE_1 = 1;
    public final static Short REFUND_BUSINESS_TYPE_2 = 2;

    /**
     * 退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式 (如果不传默认不处理退款申请单，人工处理) 4 混合退款方式
     */
    public final static Short REFUND_TYPE_1 = 1;
    public final static Short REFUND_TYPE_2 = 2;
    public final static Short REFUND_TYPE_3 = 3;
    public final static Short REFUND_TYPE_4 = 4;

    /**
     * 退款账号（谁的账号出）1 平台方退款(比如：bat收款，bat收款), 2 分销商
     */
    public final static Short REFUND_MODE_1 = 1;
    public final static Short REFUND_MODE_2 = 2;

    /**
     * ERP余额是否同步 1.是 0.否
     */
    public final static Short ERP_BALANCE_FLAG_0 = 0;
    public final static Short ERP_BALANCE_FLAG_1 = 1;

    /**
     * 1 作废 2 关闭（包括提前关闭）erpCancelType
     */
    public final static Short ERP_CANCEL_TYPE_1 = 1;
    public final static Short ERP_CANCEL_TYPE_2 = 2;

    /**
     * 是否获取赠品 1 是 0 否
     */
    public final static Short PRESENT_FLAG_0 = 0;
    public final static Short PRESENT_FLAG_1 = 1;

    /**
     * 是否已核销 0否 1是
     */
    public final static Short WRITE_OFF_FLAG_0 = 0;
    public final static Short WRITE_OFF_FLAG_1 = 1;

    /**
     * 数量变更类型：1 增加 2 减少
     */
    public final static Short CHANGE_TYPE_1 = 1;
    public final static Short CHANGE_TYPE_2 = 2;

    /**
     * 是否支持超卖或预售：1、支持 0、不支持
     */
    public final static Short MTO_FRAG_0 = 0;
    public final static Short MTO_FRAG_1 = 1;

    /**
     * 服务模块：7-订单服务
     */
    public static final Short MODEL_TYPE7 = 7;

    /**
     * 特殊类型, 1 普通,2 mto,3 现款,4 定制,5 直运
     */
    public final static Short SPECIAL_FLAG_1 = 1;
    public final static Short SPECIAL_FLAG_2 = 2;
    public final static Short SPECIAL_FLAG_3 = 3;
    public final static Short SPECIAL_FLAG_4 = 4;
    public final static Short SPECIAL_FLAG_5 = 5;

    /**
     * 平台租户ERP配置类型 1 金蝶
     */
    public static final Short ERP_TYPE_JD = 1;

    /**
     * 下单状态 1:已下单 2:未下单
     */
    public final static Short HANDLE_FLAG_1 = 1;
    public final static Short HANDLE_FLAG_2 = 2;

    /**
     * 提交状态 1.未提交，2.提交中，3.提交成功，4.提交失败
     */
    public final static Short SUBMIT_STATUS_1 = 1;
    public final static Short SUBMIT_STATUS_2 = 2;
    public final static Short SUBMIT_STATUS_3 = 3;
    public final static Short SUBMIT_STATUS_4 = 4;

    /**
     * 1 现款线上 2 现款线下
     */
    public final static Short CASH_LINE_1 = 1;
    public final static Short CASH_LINE_2 = 2;

    /**
     * 商品是否支持预售：1-支持 商品是否支持预售：0-不支持
     */
    public final static Short ADVANCE_SALE_ENABLE = 1;
    public final static Short ADVANCE_SALE_DISABLE = 0;

    public final static Short IMPORT_OPERATION_LIST = 0;
    public final static Short IMPORT_OPERATION_SUBMIT = 1;

    /**
     * 促销统计方式：1满减 2满赠
     */
    public final static Short CONDITION_REDUCE = 1;
    public final static Short CONDITION_PRESENT = 2;

    /**
     * 规则形式：1金额 2数量
     */
    public final static Short MONEY = 1;
    public final static Short COUNT = 2;

    /**
     * PC前台下单
     */
    public final static String GF60001 = "GF60001";

    /**
     * 是否收取快递费：0不收取 1收取
     */
    public final static Short DELIVERY_FEE_FLAG_0 = 0;
    public final static Short DELIVERY_FEE_FLAG_1 = 1;
}
