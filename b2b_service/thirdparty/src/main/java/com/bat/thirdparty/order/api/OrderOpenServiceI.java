package com.bat.thirdparty.order.api;

import com.bat.thirdparty.alibaba.taobao.api.dto.TaoBaoHttpRequestDTO;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.order.api.dto.moleji.MolejiOrderCreateCmd;
import com.bat.thirdparty.order.api.dto.provisional.ProvisionalOrderInfo;
import com.bat.dubboapi.order.order.dto.OrderCancelCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface OrderOpenServiceI {
    /**
     * 基于id的下单接口
     * @param request
     * @param jiuJiOrderCmd
     * @param taoBaoHttpRequestDTO
     * @param logId 日志id 不为空表示后台重推
     * @return
     */
    ResponseEntity<Object> createOrderBaseOnId(HttpServletRequest request, OrderBaseOnIdCmd jiuJiOrderCmd, TaoBaoHttpRequestDTO taoBaoHttpRequestDTO, Integer logId);

    /**
     * 基于编码的下单接口
     * @param orderBaseOnCodeCmd
     * @param request
     * @return
     */
    ResponseEntity<Object> createOrderBaseOnCode(OrderBaseOnCodeCmd orderBaseOnCodeCmd, HttpServletRequest request,Integer logId);

    /**
     * 分销商系统取消订单
     * @param distributorId
     * @param orderNo B2B的订单编号 、非之前的订单id
     * @return
     */
    ResponseEntity<Object> cancelOrderFromDistributor(String distributorId, String orderNo);

    /**
     * 提交订单到第三方系统（临时订单）
     * @param orderInformation
     * @param servletRequest
     * @return
     */
    Response submitToThird(ProvisionalOrderInfo orderInformation, HttpServletRequest servletRequest);

    /**
     * 核销第三方兑换码订单
     */
    void writeOffThirdCodeOrder();

    /**
     * 分销商取消订单标准接口
     * @param orderCancelCmd
     * @param request
     * @return
     */
    Response cancelOrderFromDistributorStandard(OrderCancelCmd orderCancelCmd, HttpServletRequest request);

    /**
     * 摩乐吉订单推送接口
     * @param molejiOrderCreateCmd
     * @return
     */
    ResponseEntity<Object> createOrderFromMoleji(MolejiOrderCreateCmd molejiOrderCreateCmd);

    void sumsungOrder(Integer orderId);
}
