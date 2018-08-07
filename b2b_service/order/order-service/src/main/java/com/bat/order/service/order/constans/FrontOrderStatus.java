package com.bat.order.service.order.constans;

/**
 * @author: lim
 * @description: 前端订单状态
 * @date: 2018/7/6 16:47
 */
public class FrontOrderStatus {
    /**
     *
     * 计算 并转换状态
     *
     * 0全部 订单全部 发货全部
     *
     * 1待确认 orderStatus1 payStatus3 订单待确认 已付款 （区间结算 线下付款可以未付款）
     *
     * 2待发货 orderStatus2 deliverStatus1 deliverStatus2 订单已确认 发货待发货 发货出库中
     *
     * 3部分发货 orderStatus2 deliverStatus3 订单已确认 发货部分发货
     *
     * 4待收货 orderStatus2 deliverStatus4 订单已确认 发货已发货
     *
     * 5已关闭 orderStatus4 订单已取消
     *
     * 6已完成 orderStatus5 订单已完成
     *
     * 7待付款 orderStatus1 payStatus3 订单待确认 待付款 （区间结算 线下付款不算）
     * 
     * 8已拒绝 orderStatus3 订单已拒绝
     *
     * 9出库中 deliver_status为2、order_status为2时
     */
    public static Short ALL = 0;
    public static Short WAIT_CONFIRM = 1;
    public static Short WAIT_SEND_GOODS = 2;
    public static Short WAIT_PART_SEND_GOODS = 3;
    public static Short WAIT_RECEIVE_GOODS = 4;
    public static Short CLOSED = 5;
    public static Short FINISHED = 6;
    public static Short WAIT_PAYMENT = 7;
    public static Short REJECT = 8;

    public static Short OUTBOUNDING = 9;

    public static String getStatusStr(Short status) {
        if (status == 0) {
            return "全部";
        }
        if (status == 1) {
            return "待确认";
        }
        if (status == 2) {
            return "待发货";
        }
        if (status == 3) {
            return "部分发货";
        }
        if (status == 4) {
            return "待收货";
        }
        if (status == 5) {
            return "已关闭";
        }
        if (status == 6) {
            return "已完成";
        }
        if (status == 7) {
            return "待付款";
        }
        if (status == 8) {
            return "已拒绝";
        }
        if (status == 9) {
            return "出库中";
        }
        return "未知状态";
    }
}
