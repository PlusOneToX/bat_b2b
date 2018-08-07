package com.bat.order.service.cost.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.cost.convertor.OrderCostConvertor;
import com.bat.order.service.data.executor.OrderCustomerDataCmdExe;
import com.bat.order.service.data.executor.OrderDistributorDataQryExe;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.executor.OrderGoodsQryExe;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.mq.dto.OrderRefundDTO;
import com.bat.order.service.data.executor.OrderCustomerDataQryExe;
import com.bat.order.service.data.executor.OrderDistributorDataCmdExe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCostCmdExe {
    @Resource
    private OrderDistributorCostMapper orderDistributorCostMapper;
    @Resource
    private OrderCustomerCostMapper orderCustomerCostMapper;
    @Resource
    private OrderDistributorDataQryExe orderDistributorDataQryExe;
    @Resource
    private OrderCustomerDataQryExe orderCustomerDataQryExe;
    @Resource
    private OrderCustomerDataCmdExe orderCustomerDataCmdExe;
    @Resource
    private OrderGoodsQryExe orderGoodsQryExe;
    @Resource
    private OrderGoodsCustomerCostQryExe orderGoodsCustomerCostQryExe;
    @Resource
    private OrderGoodsDistributorCostQryExe orderGoodsDistributorCostQryExe;
    @Resource
    private OrderDistributorDataCmdExe orderDistributorDataCmdExe;
    @Resource
    private MessageSendService sendService;

    /**
     * 更新订单层级费用数据
     * 
     * @param orderId
     */
    public void orderChangeCost(Integer orderId) {
        List<OrderGoodsDO> orderGoodsDOS = orderGoodsQryExe.listByOrderId(orderId);
        log.info("商品明细:{}", JSON.toJSONString(orderGoodsDOS));
        OrderCustomerDataDO customerDataDO = orderCustomerDataQryExe.getOrderCustomerData(orderId);
        OrderCustomerCostDO customerCostDO = null;
        List<OrderGoodsCustomerCostDO> goodsCustomerCostDOS = new ArrayList<>();
        if (customerDataDO != null) {
            customerCostDO = orderCustomerCostMapper.getByOrderId(orderId);
            goodsCustomerCostDOS = orderGoodsCustomerCostQryExe.listOrderGoodsCustomerCost(customerCostDO.getOrderId(),
                customerCostDO.getCustomerId());
        }
        List<OrderDistributorCostDO> changeDistributorCostDOS = new ArrayList<>();
        List<OrderDistributorDataDO> changeDistributorDataDOS = new ArrayList<>();
        List<OrderDistributorDataDO> distributorDataDOS = orderDistributorDataQryExe.listByOrderId(orderId);
        List<OrderDistributorCostDO> distributorCostDOS = orderDistributorCostMapper.listByOrderId(orderId);
        List<OrderGoodsDistributorCostDO> goodsDistributorCostDOS =
            orderGoodsDistributorCostQryExe.listOrderGoodsDistributorCostByOrderId(orderId);
        List<OrderRefundDTO> refundDTOS = OrderCostConvertor.toOrderChangeRefundDTO(orderGoodsDOS, customerDataDO,
            customerCostDO, goodsCustomerCostDOS, distributorDataDOS, distributorCostDOS, goodsDistributorCostDOS,
            changeDistributorCostDOS, changeDistributorDataDOS, "取消订单退款", null, "系统");
        if (!CollectionUtils.isEmpty(changeDistributorDataDOS)) {
            orderDistributorDataCmdExe.updateList(changeDistributorDataDOS);
        }
        if (!CollectionUtils.isEmpty(changeDistributorCostDOS)) {
            orderDistributorCostMapper.updateList(changeDistributorCostDOS);
        }
        if (!CollectionUtils.isEmpty(refundDTOS)) {
            if (customerDataDO != null) {
                orderCustomerDataCmdExe.updateOrderCustomerData(customerDataDO);
            }
            if (customerCostDO != null) {
                orderCustomerCostMapper.updateByPrimaryKey(customerCostDO);
            }
            sendService.orderRefund(refundDTOS);
        }
    }

}
