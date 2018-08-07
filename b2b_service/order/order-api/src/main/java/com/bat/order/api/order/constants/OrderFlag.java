package com.bat.order.api.order.constants;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/2 21:03
 */
public class OrderFlag {
    /**
     * TOP_FLAG 顶级分销商 即tree_node = 1
     * 
     * ERP_FLAG 同步erp分销商
     * 
     * DIRECT_FLAG 直接下单分销商
     */
    public static final Short TOP_FLAG = 1;
    public static final Short ERP_FLAG = 2;
    public static final Short DIRECT_FLAG = 3;
}
