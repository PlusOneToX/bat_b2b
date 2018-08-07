package com.bat.system.service.promotion.executor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
public class ErrorCode {

    // 新增记录的开始时间不能在上一条记录的结束时间之前
    public static final String B_GOODS_PROMOTION_DATE_BEFORE = "B_GOODS_PROMOTION_DATE_BEFORE";

    public static final String B_GOODSPROMOTIONDO_NOTNULL = "B_GoodsPromotionDO_NOTNULL";

    public static final String B_GOODS_PROMOTION_DISTRIBUTOR_ERROR = "B_GOODS_PROMOTION_DISTRIBUTOR_ERROR";

    public static final String B_GOODSPROMOTION_DELETE_ERROR = "B_GOODSPROMOTION_DELETE_ERROR";
}
