package com.bat.goods.service.common;

public class Constant {

    /**
     * 可视范围常量 0 不指定 1 指定全部 2 指定分销商等级 3 指定分销商 4 指定销售部门 5 指定业务员 6 指定分销商分组
     */
    public static final Short SCOPE_NULL = 0;
    public static final Short SCOPE_ALL = 1;
    public static final Short SCOPE_SCALE_PRICE = 2;
    public static final Short SCOPE_DISTRIBUTOR = 3;
    public static final Short SCOPE_DEPARTMENT = 4;
    public static final Short SCOPE_ADMIN = 5;
    public static final Short SCOPE_DISTRIBUTOR_GROUP = 6;

    /**
     * 停用启用状态 1 启用 0 停用
     */
    public final static Short OPEN_NO = 0;
    public final static Short OPEN_YES = 1;

    /**
     * 操作类型 1 新增 2 修改 3 删除 4.置顶
     */
    public final static Short OPERATION_TYPE_1 = 1;
    public final static Short OPERATION_TYPE_2 = 2;
    public final static Short OPERATION_TYPE_3 = 3;
    public final static Short OPERATION_TYPE_4 = 4;

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
     * 算数运算符：1 乘2 加 3 除 4 减
     */
    public final static Short ARITHMETIC_TYPE_1 = 1;
    public final static Short ARITHMETIC_TYPE_2 = 2;
    public final static Short ARITHMETIC_TYPE_3 = 3;
    public final static Short ARITHMETIC_TYPE_4 = 4;

    /**
     * 1、是 0、否
     */
    public final static Short DEFAULT_FLAG_0 = 1;
    public final static Short DEFAULT_FLAG_1 = 2;

    /**
     * 1 活动热销(只有登录后的用户支持) 2 新品上市
     */
    public final static Short HOT_TYPE_1 = 1;
    public final static Short HOT_TYPE_2 = 2;

    /**
     * 是否新品 0 否 1 是
     */
    public final static Short NEW_FLAG_0 = 0;
    public final static Short NEW_FLAG_1 = 1;
    /**
     * 是否筛选收藏 0-否 1-是
     */
    public final static Short COLLECTION_FLAG_0 = 0;
    public final static Short COLLECTION_FLAG_1 = 1;

    /**
     * 规则对象，1订单，2商品，3货品
     */
    public final static Short RULE_TYPE_1 = 1;
    public final static Short RULE_TYPE_2 = 2;
    public final static Short RULE_TYPE_3 = 3;

    /**
     * 是否特价，1是 0否
     */
    public final static Short SPECIAL_FLAG_0 = 0;
    public final static Short SPECIAL_FLAG_1 = 1;

    /**
     * 满减类型，1减免 2折扣
     */
    public final static Short REDUCE_TYPE_1 = 1;
    public final static Short REDUCE_TYPE_2 = 2;

    /**
     * 规则形式：1金额 2数量
     */
    public final static Short MONEY_COUNT_1 = 1;
    public final static Short MONEY_COUNT_2 = 2;

    /**
     * 促销统计方式：1满减 2满赠
     */
    public final static Short CONDITION_REDUCE = 1;
    public final static Short CONDITION_PRESENT = 2;

    /**
     * 是否促销 0-否 1-是
     */
    public final static Short PROMOTION_FLAG_0 = 0;
    public final static Short PROMOTION_FLAG_1 = 1;

    /**
     * 活动类型，1 普通活动，2 阶梯活动
     */
    public final static Short PROMO_TYPE_1 = 1;
    public final static Short PROMO_TYPE_2 = 2;
    /**
     * 是否拼团 0-否 1-是
     */
    public final static Short GROUP_FLAG_0 = 0;
    public final static Short GROUP_FLAG_1 = 1;
    /**
     * 是否秒杀 0-否 1-是
     */
    public final static Short SECKILL_FLAG_0 = 0;
    public final static Short SECKILL_FLAG_1 = 1;

    /**
     * 活动类型，0 没活动 1促销活动 2拼团 3秒杀
     */
    public final static Short PROMOTION_TYPE_0 = 0;
    public final static Short PROMOTION_TYPE_1 = 1;
    public final static Short PROMOTION_TYPE_2 = 2;
    public final static Short PROMOTION_TYPE_3 = 3;

    /**
     * 拼团秒杀：1拼团 2秒杀
     */
    public final static Short GROUP_SECKILL_TYPE_1 = 1;
    public final static Short GROUP_SECKILL_TYPE_2 = 2;

    /**
     * 是否获取建议零售价：1是 0否
     */
    public final static Short RETAIL_PRICE_FLAG_1 = 1;
    public final static Short RETAIL_PRICE_FLAG_0 = 0;

    /**
     * 商品类型 1-普通 2-定制
     */
    public final static Short GOODS_TYPE_GENERAL = 1;
    public final static Short GOODS_TYPE_CUSTOM = 2;

    /**
     * 销量展示类型: 1 销售数量,2虚拟销量+销售数量
     */
    public final static Short SHOW_TYPE_1 = 1;
    public final static Short SHOW_TYPE_2 = 2;

    /**
     * 数量变更类型：1 增加 2 减少
     */
    public final static Short CHANGE_TYPE_1 = 1;
    public final static Short CHANGE_TYPE_2 = 2;

    /**
     * 否建议零售价, 0否，1是retail_flag
     */
    public final static Short RETAIL_FLAG_0 = 0;
    public final static Short RETAIL_FLAG_1 = 1;
    /**
     * 服务模块：1-商品服务
     */
    public static final Short MODEL_TYPE1 = 1;

    /**
     * 服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb 11-redis
     */
    public static final Short REDISDB_TYPE = 11;

    /**
     * 是否推荐 0否 1是
     */
    public final static Short RECOMMEND_FLAG_0 = 0;
    public final static Short RECOMMEND_FLAG_1 = 1;

    /**
     * 更新商品价格调用来源 1 指定 2全部
     */
    public final static Short UPDATE_GOODS_PRICE_ORIGIN_APPOINT = 1;
    public final static Short UPDATE_GOODS_PRICE_ORIGIN_ALL = 2;
    /**
     * 商品生命周期 1.导入初期，2.成长期，3.成熟期，4.衰退期，5.项目终止
     */
    public final static Short LIFE_CYCLE_1 = 1;
    public final static Short LIFE_CYCLE_2 = 2;
    public final static Short LIFE_CYCLE_3 = 3;
    public final static Short LIFE_CYCLE_4 = 4;
    public final static Short LIFE_CYCLE_5 = 5;

    /**
     * 是否直发:1.是 0.否
     */
    public final static Short AUTODELIVERY_0 = 0;
    public final static Short AUTODELIVERY_1 = 1;

    /**
     * 是否支持在途库存 1是 0否 默认是1
     */
    public final static Short ONWAYFLAG_0 = 0;
    public final static Short ONWAYFLAG_1 = 1;
}
