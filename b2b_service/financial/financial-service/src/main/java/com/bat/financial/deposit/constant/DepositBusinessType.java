package com.bat.financial.deposit.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/28 18:26
 */
public class DepositBusinessType {
    /**
     * 业务类型 1充值 2 提现 3 订单消费 4 订单取消增加 5调整 6 ERP增量变化 7 ERP全量变化 8 分销佣金
     */
    public static final short RECHARGE = 1;
    public static final short WITHDRAWAL = 2;
    public static final short ORDER_CREATE_SUB = 3;
    public static final short ORDER_CANCEL_ADD = 4;
    public static final short ORDER_ADJUSTMENT = 5;
    public static final short ERP_INCREMENT = 6;
    public static final short ERP_TOTAL = 7;
    public static final short DISTRIBUTION_COMMISSION = 8;

    /**
     * 业务类型 1,提现冻结 2,其他冻结
     */
    public static final short WITHDRAWAL_FREEZING = 1;
    public static final short OTHER_FREEZING = 2;
}
