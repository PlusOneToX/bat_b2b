package com.bat.thirdparty.quanyi.common;

public class QuanyiConstant {

    /**
     * 请求方向 1第三方访问-本系统 2.本系统访问第三方
     */
    public static final Short direction1=1;

    /**
     * 请求方向 1第三方访问-本系统 2.本系统访问第三方
     */
    public static final Short direction2=2;

    /**
     * 操作类型 1 苏宁传输订单  2 系统对苏宁订单进行派工 3 c端客户领取权益码 4 系统对苏宁订单进行签到 5 系统对苏宁订单进行核销 6 苏宁端取消订单 7 管理员对权益进行取消 8 管理员撤销权益取消
     */
    public static final Short operateType1=1;

    /**
     * 操作类型 2 系统对苏宁订单进行派工
     */
    public static final Short operateType2=2;

    /**
     * 操作类型 3 c端客户领取权益码
     */
    public static final Short operateType3=3;
    /**
     * 操作类型 4 系统对苏宁订单进行签到
     */
    public static final Short operateType4=4;

    /**
     * 操作类型 5 系统对苏宁订单进行核销
     */
    public static final Short operateType5=5;

    /**
     * 操作类型 6 苏宁端取消订单
     */
    public static final Short operateType6=6;

    /**
     * 操作类型 7 管理员对权益进行取消
     */
    public static final Short operateType7=7;

    /**
     * 操作类型 8 管理员撤销权益取消
     */
    public static final Short operateType8=8;

    /**
     * 操作类型 9 客户重新下单
     */
    public static final Short operateType9=9;

    /**
     * 操作人分类 1、苏宁 2、b2b系统 3、系统管理员 4、c端用户
     */
    public static final Short operatorUserType1=1;
    public static final Short operatorUserType2=2;
    public static final Short operatorUserType3=3;
    public static final Short operatorUserType4=4;

    public static final String invalidReason="权益取消";
    public static final String unInvalidReason="撤销权益取消";

    /**
     * 状态 1.未验证 2.未兑换 3.已兑换 4已作废
     */
    public static final short status1=1;
    public static final short status2=2;
    public static final short status3=3;
    public static final short status4=4;



}
