package com.bat.order.api.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderGoodsThirdCodeDTO;
import com.bat.order.api.order.dto.customer.OrderInfoCustomerCmd;
import com.bat.order.api.order.dto.customer.OrderInfoExchangeCmd;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListDTO;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListQry;
import com.bat.order.api.basic.BaseIds;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 11:30
 */
public interface UserCustomerOrderServiceI {
    /**
     * C端客户下单接口(非兑换卡下单)
     * 
     * @param cmd
     * @param userId
     *            C端客户id
     * @param distributorId
     *            归属分销商id
     * @param platform
     *            平台编码
     * @param language
     *            语言，默认中文
     * @return
     */
    BaseIds createOrder(OrderInfoCustomerCmd cmd, String userId, String userName, String distributorId, String platform,
                        String language);

    /**
     * C端客户兑换卡下单接口
     * 
     * @param cmd
     * @param userId
     *            C端客户id
     * @param distributorId
     *            归属分销商id
     * @param platform
     *            平台编码
     * @param language
     *            语言，默认中文
     * @return
     */
    BaseIds createExchangeOrder(OrderInfoExchangeCmd cmd, String userId, String userName, String distributorId,
                                String platform, String language);

    /**
     * 柔性获取订单列表
     * 
     * @param qry
     * @return
     */
    PageInfo<UserCustomerOrderInfoListDTO> listCustomerOrderInfoByCustomerId(UserCustomerOrderInfoListQry qry);

    /**
     * 获取柔性订单详情
     * 
     * @param id
     * @return
     */
    UserCustomerOrderInfoListDTO getCustomerOrderDetailInfoById(Integer id);

    /**
     * 根据第三方兑换码找对应的订单
     * 
     * @param code
     * @return
     */
    OrderGoodsThirdCodeDTO findOrderByThirdCode(String code);

    /**
     * 根据第三方兑换码核销订单
     * 
     * @param code
     * @return
     */
    void writeOffThirdCodeOrder(String code);

    /**
     * 根据订单id取消订单
     * 
     */
    void orderCancel(OrderCancelCmd cmd, String userId, String userName);

    /**
     * C端获取订单失效时间（单位分钟）
     * 
     * @return
     */
    Integer getLoseTime();
}
