package com.bat.promotion.service.common;

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
     * 适用材质，1全部材质可用 2指定材质可用
     */
    public static final Short MATERIAL_SCOPE_1 = 1;
    public static final Short MATERIAL_SCOPE_2 = 2;
    /**
     * 适用范围，1全部可用 2指定分销商可用
     */
    public static final Short MODEL_SCOPE_1 = 1;
    public static final Short MODEL_SCOPE_2 = 2;

    /**
     * 停用启用状态 0 启用 1 停用
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
     * 申请状态：0草稿 1申请中 2申请通过 3申请失败
     */
    public final static Short APPLY_STATUS_0 = 0;
    public final static Short APPLY_STATUS_1 = 1;
    public final static Short APPLY_STATUS_2 = 2;
    public final static Short APPLY_STATUS_3 = 3;

    /**
     * 0 未开始, 1 促销中 2 已过期 3 提前结束
     */
    public final static Short PROMO_STATUS_0 = 0;
    public final static Short PROMO_STATUS_1 = 1;
    public final static Short PROMO_STATUS_2 = 2;
    public final static Short PROMO_STATUS_3 = 3;

    /**
     * 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束
     */
    public final static Short GROUP_SECKILL_STATUS_0 = 0;
    public final static Short GROUP_SECKILL_STATUS_1 = 1;
    public final static Short GROUP_SECKILL_STATUS_2 = 2;
    public final static Short GROUP_SECKILL_STATUS_3 = 3;
    public final static Short GROUP_SECKILL_STATUS_4 = 4;
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
     * 更新类型为：是否更新已领取的优惠券 1为是 0为否
     */
    public final static Short COUPON_STATUS_TYPE_0 = 0;
    public final static Short COUPON_STATUS_TYPE_1 = 1;

    /**
     * 1满减 2满折 3兑换
     */
    public final static Short COUPON_METHOD_1 = 1;
    public final static Short COUPON_METHOD_2 = 2;
    public final static Short COUPON_METHOD_3 = 3;

    /**
     * 是否收取快递费 0否 1是
     */
    public final static Short DELIVERY_FEE_FLAG_0 = 0;
    public final static Short DELIVERY_FEE_FLAG_1 = 1;

    /**
     * 冻结状态，1未冻结，2冻结
     */
    public final static Short FREEZE_STATUS_1 = 1;
    public final static Short FREEZE_STATUS_2 = 2;
    /**
     * 规则对象，1订单，2商品，3货品
     */
    public final static Short RULE_TYPE_1 = 1;
    public final static Short RULE_TYPE_2 = 2;
    public final static Short RULE_TYPE_3 = 3;
    /**
     * 是否累计，1是 0否
     */
    public final static Short ADD_UP_FLAG_0 = 0;
    public final static Short ADD_UP_FLAG_1 = 1;
    /**
     * 规则形式：1金额 2数量
     */
    public final static Short MONEY = 1;
    public final static Short COUNT = 2;

    /**
     * 在途商品是否参与活动 1.参与，0.不参与
     */
    public final static Short ON_WAY_FLAG_0 = 0;
    public final static Short ON_WAY_FLAG_1 = 1;

    /**
     * 是否赠品 1 非赠品 2 赠品
     */
    public final static Short ITEM_TYPE_1 = 1;
    public final static Short ITEM_TYPE_2 = 2;
    /**
     * 是否特价，1是 0否
     */
    public final static Short SPECIAL_FLAG_0 = 0;
    public final static Short SPECIAL_FLAG_1 = 1;
    /**
     * 促销统计方式：1满减 2满赠
     */
    public final static Short CONDITION_REDUCE = 1;
    public final static Short CONDITION_PRESENT = 2;

    /**
     * 满减类型，1减免 2折扣
     */
    public final static Short REDUCE_TYPE_1 = 1;
    public final static Short REDUCE_TYPE_2 = 2;

    /**
     * 满减满赠是否叠加，1是 0否
     */
    public final static Short REDUCTION_PRESENT_ADD_FLAG_0 = 0;
    public final static Short REDUCTION_PRESENT_ADD_FLAG_1 = 1;

    /**
     * 搜索内容类型，1 促销活动名称 2 商品编号 3 货品编号 4 分销商 5 规则标签 6 条件标签
     */
    public final static Short CONTENT_TYPE_1 = 1;
    public final static Short CONTENT_TYPE_2 = 2;
    public final static Short CONTENT_TYPE_3 = 3;
    public final static Short CONTENT_TYPE_4 = 4;
    public final static Short CONTENT_TYPE_5 = 5;
    public final static Short CONTENT_TYPE_6 = 6;
    /**
     * 分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)
     */
    public final static Short PROMOTION_FLAG_0 = 0;
    public final static Short PROMOTION_FLAG_1 = 1;

    /**
     * 促销拼团秒杀活动类型： 1 促销 2拼团秒杀
     */
    public final static Short PROMOTION_GROUP_SECKILL_TYPE_1 = 1;
    public final static Short PROMOTION_GROUP_SECKILL_TYPE_2 = 2;
    /**
     * 拼团秒杀：1拼团 2秒杀
     */
    public final static Short GROUP_SECKILL_TYPE_1 = 1;
    public final static Short GROUP_SECKILL_TYPE_2 = 2;

    /**
     * 参与活动 0-不参与活动 1-全部活动 2-指定活动类型
     */
    public final static Short PROMOTION_SCOPE_0 = 0;
    public final static Short PROMOTION_SCOPE_1 = 1;
    public final static Short PROMOTION_SCOPE_2 = 2;

    /**
     * 可参与活动类型 1-营销活动 2-阶梯活动 3-拼团活动
     */
    public final static String PROMOTION_TYPE_1 = "1";
    public final static String PROMOTION_TYPE_2 = "2";
    public final static String PROMOTION_TYPE_3 = "3";

    /**
     * 商品是否能用, 1 能用 0 不能用
     */
    public final static Short GOODS_ENABLE_0 = 0;
    public final static Short GOODS_ENABLE_1 = 1;

    /**
     * 是否正在使用活动 1 是 0 否
     */
    public final static Short USE_FLAG_0 = 0;
    public final static Short USE_FLAG_1 = 1;

    /**
     * 是否获取赠品 1 是 0 否
     */
    public final static Short PRESENT_FLAG_0 = 0;
    public final static Short PRESENT_FLAG_1 = 1;

    /**
     * 标签类型：1,我发起的 2,待我审批 3,我审批的
     */
    public final static Short LABEL_TYPE_1 = 1;
    public final static Short LABEL_TYPE_2 = 2;
    public final static Short LABEL_TYPE_3 = 3;

    /**
     * 审批类型: 1 新增 2 修改
     */
    public final static Short CHECK_TYPE_1 = 1;
    public final static Short CHECK_TYPE_2 = 2;

    /**
     * 活动来源，1 后台新增, 2 批量导入
     */
    public final static Short PROMO_SOURCE_1 = 1;
    public final static Short PROMO_SOURCE_2 = 2;

    /**
     * 活动类型：1 促销活动 2 拼团秒杀活动 3 优惠券 4 返利代金券
     */
    public final static Short PROMOTION_CHECK_TYPE_1 = 1;
    public final static Short PROMOTION_CHECK_TYPE_2 = 2;
    public final static Short PROMOTION_CHECK_TYPE_3 = 3;
    public final static Short PROMOTION_CHECK_TYPE_4 = 4;

    /**
     * 活动审批类型：13 促销活动 14 优惠券 15 拼团秒杀活动 16 返利代金券
     */
    public final static Short PROMOTION_TYPE_13 = 13;
    public final static Short PROMOTION_TYPE_14 = 14;
    public final static Short PROMOTION_TYPE_15 = 15;
    public final static Short PROMOTION_TYPE_16 = 16;

    /**
     * flag 1 是 0 否
     */
    public final static Short FLAG_0 = 0;
    public final static Short FLAG_1 = 1;

    /**
     * 审批状态 0,未审批 1,审批通过，2审批未通过
     */
    public final static Short CHECK_STATUS_0 = 0;
    public final static Short CHECK_STATUS_1 = 1;
    public final static Short CHECK_STATUS_2 = 2;

    /**
     * 服务模块：6-营销推广服务
     */
    public static final Short MODEL_TYPE6 = 6;

    /**
     * 是否提前展示：1 准时2 提前
     */
    public static final Short ADVANCE_FLAG_1 = 1;
    public static final Short ADVANCE_FLAG_2 = 2;

    /**
     * 数量变更类型：1 增加 2 减少
     */
    public final static Short CHANGE_TYPE_1 = 1;
    public final static Short CHANGE_TYPE_2 = 2;

    /**
     * 代金券状态（汇总） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结
     * 
     */
    public final static Short REBATE_VOUCHER_STATUS_0 = 0;
    public final static Short REBATE_VOUCHER_STATUS_1 = 1;
    public final static Short REBATE_VOUCHER_STATUS_2 = 2;
    public final static Short REBATE_VOUCHER_STATUS_3 = 3;
    public final static Short REBATE_VOUCHER_STATUS_4 = 4;
    public final static Short REBATE_VOUCHER_STATUS_5 = 5;
    public final static Short REBATE_VOUCHER_STATUS_7 = 7;
    public final static Short REBATE_VOUCHER_STATUS_9 = 9;
    public final static Short REBATE_VOUCHER_STATUS_11 = 11;

    /**
     * 冻结状态 10未冻结(可用) 11冻结
     */
    public final static Short REBATE_VOUCHER_FREEZE_STATUS_10 = 10;
    public final static Short REBATE_VOUCHER_FREEZE_STATUS_11 = 11;

    /**
     * 优惠券缓存数据key
     */
    public final static  String COUPON_USE_FLAG_KEY = "COUPON_USE_FLAG_KEY:";

}
