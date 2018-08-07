package com.bat.financial.deposit.executor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
public class ErrorCode {

    public static final String B_FREEZING_AMOUNT_ZERO = "B_FREEZING_AMOUNT_ZERO";
    public static final String B_DEPOSIT_DISTRIBUTOR_NOT_EXISTS = "B_DEPOSIT_DISTRIBUTOR_NOT_EXISTS";
    public static final String B_DEPOSIT_FREEZING_NOT_EXISTS = "B_DEPOSIT_FREEZING_NOT_EXISTS";
    public static final String B_WITHDRAW_AMOUNT_ERROR = "B_WITHDRAW_AMOUNT_ERROR";
    public static final String B_WITHDRAW_AMOUNT_NOT_ENOUGH = "B_WITHDRAW_AMOUNT_NOT_ENOUGH";
    public static final String B_DEPOSIT_ERP_DISTRIBUTOR_EXISTS = "B_DEPOSIT_ERP_DISTRIBUTOR_EXISTS";
    public static final String B_ERP_DISTRIBUTOR_ID_B2B_NOT_EXISTS = "B_ERP_DISTRIBUTOR_ID_B2B_NOT_EXISTS";
    /**
     * B2B未开启ERP余额同步，B2B未更新余额
     */
    public static final String B_BALANCE_NO_SYNC = "B_BALANCE_NO_SYNC";
    public static final String B_DEPOSIT_BALANCE_SYNC_ERROR = "B_DEPOSIT_BALANCE_SYNC_ERROR";
    public static final String B_DEPOSIT_BALANCE_DISTRIBUTOR_ERP_ID_NULL = "B_DEPOSIT_BALANCE_DISTRIBUTOR_ERP_ID_NULL";
}
