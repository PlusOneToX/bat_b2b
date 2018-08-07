package com.bat.goods.service.brand.executor;

public class ErrorCode {

    // 品牌下的商品不为空
    public static final String B_BRAND_GOODSNOTNULL = "B_BRAND_GOODSNOTNULL";
    // 品牌不存在
    public static final String B_BRAND_NULL = "B_BRAND_NULL";
    // 品牌需先停用
    public static final String B_BRAND_DELETE_OPEN_ERROR = "B_BRAND_DELETE_OPEN_ERROR";
    // 新增或修改分销商品牌的可视范围失败
    public static final String B_GOODS_BRAND_DISTRIBUTOR_ERROR = "B_GOODS_BRAND_DISTRIBUTOR_ERROR";
}
