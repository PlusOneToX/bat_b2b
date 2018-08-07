package com.bat.distributor.service.trade.executor;

public class ErrorCode {
    // 收款条件下的分销商不为空
    public static final String B_DISTRIBUTOR_TRADE_DISTRIBUTORNOTNUL = "B_DISTRIBUTOR_TRADE_DISTRIBUTORNOTNUL";
    // 收款条件不存在
    public static final String B_DISTRIBUTOR_TRADE_NULL = "B_DISTRIBUTOR_TRADE_NULL";
    // 收款条件必需先停用
    public static final String B_DISTRIBUTOR_TRADE_DELETE_OPEN_ERROR = "B_DISTRIBUTOR_TRADE_DELETE_OPEN_ERROR";

}
