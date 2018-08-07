package com.bat.order.service.order;

import javax.annotation.Resource;

import com.bat.order.service.order.executor.OrderCmdExe;
import com.bat.order.service.order.executor.customer.UserCustomerExchangerOrderCmdExe;
import com.bat.order.service.order.executor.customer.UserCustomerOrderCmdExe;
import com.bat.order.service.order.executor.customer.UserCustomerOrderQryExe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.order.UserCustomerOrderServiceI;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderGoodsThirdCodeDTO;
import com.bat.order.api.order.dto.customer.OrderInfoCustomerCmd;
import com.bat.order.api.order.dto.customer.OrderInfoExchangeCmd;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListDTO;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListQry;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 11:31
 */
@Service
public class UserCustomerOrderServiceImpl implements UserCustomerOrderServiceI {

    @Resource
    private UserCustomerOrderCmdExe cmdExe;

    @Resource
    private UserCustomerOrderQryExe qryExe;

    @Resource
    private UserCustomerExchangerOrderCmdExe exchangerOrderCmdExe;

    @Resource
    private OrderCmdExe orderCmdExe;

    @Override
    public BaseIds createOrder(OrderInfoCustomerCmd cmd, String userId, String userName, String distributorId,
        String platform, String language) {
        return cmdExe.createOrder(cmd, userId, userName, distributorId, platform, language);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseIds createExchangeOrder(OrderInfoExchangeCmd cmd, String userId, String userName, String distributorId,
        String platform, String language) {
        return exchangerOrderCmdExe.createExchangeOrder(cmd, userId, userName, distributorId, platform, language);
    }

    @Override
    public PageInfo<UserCustomerOrderInfoListDTO> listCustomerOrderInfoByCustomerId(UserCustomerOrderInfoListQry qry) {
        return qryExe.listCustomerOrderInfoByCustomerId(qry);
    }

    @Override
    public UserCustomerOrderInfoListDTO getCustomerOrderDetailInfoById(Integer id) {
        return qryExe.getCustomerOrderDetailInfoById(id);
    }

    @Override
    public OrderGoodsThirdCodeDTO findOrderByThirdCode(String code) {
        return qryExe.findOrderByThirdCode(code);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void writeOffThirdCodeOrder(String code) {
        exchangerOrderCmdExe.writeOffThirdCodeOrder(code);
    }

    @Override
    public void orderCancel(OrderCancelCmd cmd, String userId, String userName) {
        orderCmdExe.cancelOrderByOrderId(cmd.getId(), cmd.getRemark(), userId, userName);
    }

    @Override
    public Integer getLoseTime() {
        return qryExe.getLoseTime();
    }
}
