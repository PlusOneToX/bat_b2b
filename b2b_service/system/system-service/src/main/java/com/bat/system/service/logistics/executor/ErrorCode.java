package com.bat.system.service.logistics.executor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
public class ErrorCode {
    /**
     * 首重重量不能为空
     */
    public static final String B_LOGISTICS_FIRST_WEIGHT_NULL = "B_LOGISTICS_FIRST_WEIGHT_NULL";
    /**
     * 续重重量单位不能为空
     */
    public static final String B_LOGISTICS_ADDITIONAL_WEIGHT_NULL = "B_LOGISTICS_ADDITIONAL_WEIGHT_NULL";
    /**
     * 首重体积不能为空
     */
    public static final String B_LOGISTICS_FIRST_VOLUME_NULL = "B_LOGISTICS_FIRST_VOLUME_NULL";
    /**
     * 续重体积单位不能为空
     */
    public static final String B_LOGISTICS_ADDITIONAL_VOLUME_NULL = "B_LOGISTICS_ADDITIONAL_VOLUME_NULL";
    /**
     * 计费方式不合法
     */
    public static final String B_LOGISTICS_BILLING_METHOD_ILLEGAL = "B_LOGISTICS_BILLING_METHOD_ILLEGAL";
    public static final String B_LOGISTICS_GRADE_IDS_NULL = "B_LOGISTICS_GRADE_NULL";
    public static final String B_LOGISTICS_DEPARTMENT_IDS_NULL = "B_LOGISTICS_DEPARTMENT_IDS_NULL";
    public static final String B_LOGISTICS_USER_IDS_NULL = "B_LOGISTICS_USER_IDS_NULL";
    public static final String B_LOGISTICS_DISTRIBUTOR_IDS_NULL = "B_LOGISTICS_DISTRIBUTOR_IDS_NULL";
    public static final String B_LOGISTICS_DISTRIBUTOR_SCOPE_ILLEGAL = "B_LOGISTICS_DISTRIBUTOR_SCOPE_ILLEGAL";
    /**
     * 定制商品 工厂配送方式ID不能为空
     */
    public static final String B_LOGISTICS_DIY_FACTORY_ID_NULL = "B_LOGISTICS_DIY_FACTORY_ID_NULL";
    public static final String B_LOGISTICS_COST_NULL = "B_LOGISTICS_LOGISTICS_COST_NULL";
    public static final String B_LOGISTICS_FIRST_WEIGHT_COST_NULL = "B_LOGISTICS_FIRST_WEIGHT_COST_NULL";
    public static final String B_LOGISTICS_ADDITIONAL_WEIGHT_COST_NULL = "B_LOGISTICS_ADDITIONAL_WEIGHT_COST_NULL";
    public static final String B_LOGISTICS_FIRST_VOLUME_COST_NULL = "B_LOGISTICS_FIRST_VOLUME_COST_NULL";
    public static final String B_LOGISTICS_ADDITIONAL_VOLUME_COST_NULL = "B_LOGISTICS_ADDITIONAL_VOLUME_COST_NULL";
    public static final String B_LOGISTICS_FORMULA_NULL = "B_LOGISTICS_FORMULA_NULL";
    public static final String B_LOGISTICS_ID_EXISTS = "B_LOGISTICS_ID_EXISTS";
    public static final String B_ORDER_GOODS_ITEM_NULL = "B_ORDER_GOODS_ITEM_NULL";
    public static final String B_LOGISTICS_NOT_EXISTS = "B_LOGISTICS_NOT_EXISTS";
    public static final String B_LOGISTICS_COST_NOT_EXISTS = "B_LOGISTICS_COST_NOT_EXISTS";
    public static final String B_LOGISTICS_WEIGHT_NULL = "B_LOGISTICS_WEIGHT_NULL";
    public static final String B_LOGISTICS_PRICE_NULL = "B_LOGISTICS_PRICE_NULL";
    public static final String B_LOGISTICS_VOLUME_NULL = "B_LOGISTICS_VOLUME_NULL";
    public static final String B_THIRD_LOGISTICS_NOT_EXISTS = " B_THIRD_LOGISTICS_NOT_EXISTS";
    public static final String B_LOGISTICS_FORMULA_ERROR = "B_LOGISTICS_FORMULA_ERROR";
}
