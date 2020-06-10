package com.bat.flexible.manager.common.constant.redis;

public class FlexibleKeyConstant {

    /**
     * 材质单个 redis前缀
     */
    public static final String MATERIAL_DO_PRE="FLEXIBLE:MATERIAL:";

    /**
     * 型号单个 redis前缀
     */
    public static final String MODEL_DO_PRE="FLEXIBLE:MODEL:";

    /**
     * 图片单个 redis前缀
     */
    public static final String PICTURE_DO_PRE="FLEXIBLE:PICTURE:DO:";

    /**
     * 图片分类单个 redis前缀
     */
    public static final String PICTURE_CATEGORY_DO_PRE="FLEXIBLE:PICTURE_CATEGORY:";

    /**
     * 图片分类可用树结构 redisKey
     */
    public static final String PICTURE_CATEGORY_TREE_USABLE_PRE="FLEXIBLE:PICTURE_CATEGORY_TREE_USABLE:";

    /**
     * 图片分类所有树结构 redisKey（除了删除的）
     */
    public static final String PICTURE_CATEGORY_TREE_ALL_PRE="FLEXIBLE:PICTURE_CATEGORY_TREE_ALL:";

    /**
     * 材质可用树结构 redisKey
     */
    public static final String MATERIAL_TREE_USABLE_PRE="FLEXIBLE:MATERIAL_TREE_USABLE:";

    /**
     * 所有材质树结构 redisKey
     */
    public static final String MATERIAL_TREE_ALL_PRE="FLEXIBLE:MATERIAL_TREE_ALL:";

    /**
     * 门店单个 redis前缀
     */
    public static final String SHOP_DO_PRE="FLEXIBLE:SHOP:DO:";

    /**
     * IP图片缓存单个 redis前缀
     */
    public static final String PICTURE_MODEL_MATERIAL_DIY_DO_PRE="FLEXIBLE:PICTURE_MODEL_MATERIAL_DIY:DO:";

    /**
     * 材质和型号组合关系
     */
    public static final String PICTURE_MODEL_MATERIAL_RELEVANCE_DO_PRE ="FLEXIBLE:MODEL_MATERIAL_RELEVANCE:DO:" ;

    /**
     * 字体前缀
     */
    public static final String FONT_DO_PRE = "FLEXIBLE:FONT:";

    /**
     * 型号可用树结构 redisKey
     */
    public static final String MODEL_TREE_USABLE_PRE="FLEXIBLE:MODEL_TREE_USABLE_PRE:";

    /**
     * 型号全部树结构 redisKey
     */
    public static final String MODEL_TREE_ALL_PRE="FLEXIBLE:MODEL_TREE_ALL_PRE:";

    /**
     * 兑换卡转赠分布式锁
     */
    public static final String EXCHANGE_TRANSFER_PRE="FLEXIBLE:EXCHANGE_TRANSFER_PRE:";

    /**
     * 兑换卡导出分布式锁
     */
    public static final String EXCHANGE_EXPORT_OUT_PRE="FLEXIBLE:EXCHANGE_EXPORT_OUT_PRE:";
}
