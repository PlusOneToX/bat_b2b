package com.bat.order.api.order.constants;

/**
 * @author: lim
 * @description: 用来标记订单查询来源 某些场景下有用
 * @date: 2018/6/29 10:42
 */
public class SearchType {
    /**
     * 查询类别
     * 
     * 后台 1后台订单列表 2后台分销订单 4后台柔性定制订单 5后台柔性同步订单 6同步erp失败订单 7 同步工厂失败订单 8 长时间未发货订单 9 VIM 订单明细
     * 
     * 前台 10 订单中心 订单列表 11 柔性店铺订单
     * 
     * 移动端 12 分销商自己的分销订单 13 下级分销商的分销订单（审核用）
     */
    public static final Short ADMIN_ERP_ORDER_LIST = 1;
    public static final Short ADMIN_DISTRIBUTOR_ORDER_LIST = 2;
    public static final Short ADMIN_CUSTOMER_DIY_LIST = 4;
    public static final Short ADMIN_CUSTOMER_SYNC_LIST = 5;
    public static final Short ADMIN_ERP_SYNC_FAIL_LIST = 6;
    public static final Short ADMIN_FACTORY_SYNC_FAIL_LIST = 7;
    public static final Short ADMIN_SEND_GOODS_FAIL_LIST = 8;
    public static final Short ADMIN_VIM_ORDER_LIST = 9;
    public static final Short USER_PC_ORDER_LIST = 10;
    public static final Short USER_PC_SHOP_ORDER_LIST = 11;
    public static final Short USER_MB_DISTRIBUTOR_ORDER_LIST = 12;
    public static final Short USER_MB_NEXT_DISTRIBUTOR_ORDER_LIST = 13;
}
