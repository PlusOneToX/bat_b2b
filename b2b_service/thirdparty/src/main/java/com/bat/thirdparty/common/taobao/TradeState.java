package com.bat.thirdparty.common.taobao;

/**
 * @author lx
 */

public enum TradeState {
    //：等待买家付款
    WAIT_BUYER_PAY,
    //：等待卖家发货
    WAIT_SELLER_SEND_GOODS,
    //：卖家部分发货
    SELLER_CONSIGNED_PART,
    //：等待买家确认收货
    WAIT_BUYER_CONFIRM_GOODS,
    //：买家已签收（货到付款专用）
    TRADE_BUYER_SIGNED,
    //：交易成功
    TRADE_FINISHED,
    //：交易关闭
    TRADE_CLOSED,
    //：交易被淘宝关闭
    TRADE_CLOSED_BY_TAOBAO,
    //：没有创建外部交易（支付宝交易）
    TRADE_NO_CREATE_PAY,
    //：余额宝0元购合约中
    WAIT_PRE_AUTH_CONFIRM,
    //：外卡支付付款确认中
    PAY_PENDING,
    //：所有买家未付款的交易（包含：WAIT_BUYER_PAY、TRADE_NO_CREATE_PAY）
    ALL_WAIT_PAY,
    //：所有关闭的交易（包含：TRADE_CLOSED、TRADE_CLOSED_BY_TAOBAO）
    ALL_CLOSED,
    //，该状态代表订单已付款但是处于禁止发货状态。
    PAID_FORBID_CONSIGN
}
