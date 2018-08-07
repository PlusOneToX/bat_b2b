package com.bat.order.service.order.executor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.order.dao.order.dataobject.OrderFactoryNoDO;
import com.bat.order.service.order.convertor.OrderConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.order.dto.data.OrderVoucherErpDTO;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.co.OrderExtendDataCO;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

@Component
public class OrderQryExe {

    @Autowired
    private OrderInfoDOMapper orderInfoDOMapper;
    @Resource
    private OrderExtendDataMapper orderExtendDataMapper;
    @Resource
    private OrderDistributorCostMapper orderDistributorCostMapper;

    public OrderInfoDO getById(Integer orderId) {
        return orderInfoDOMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 根据订单ids获取同步订单收款单相关信息
     * 
     * @param orderIds
     * @return
     */
    public List<OrderVoucherErpDTO> orderVoucherErp(List<Integer> orderIds) {
        List<OrderExtendDataCO> orderExtendDataCOS = orderExtendDataMapper.listExtendDataSimpleByOrderIdList(orderIds);
        List<OrderDistributorCostDO> distributorCostDOS = orderDistributorCostMapper.listByOrderIdsAndErpFlag(orderIds);
        List<OrderVoucherErpDTO> erpDTOS =
            OrderConvertor.toOrderVoucherErpDTOList(orderExtendDataCOS, distributorCostDOS);
        return erpDTOS;
    }

    public void updateShoppingCartItemStatus(Integer goodsId, Integer cartItemStatus) {
        orderExtendDataMapper.updateShoppingCartItemStatus(goodsId, cartItemStatus);
    }

    /**
     * 获取未发货的工厂订单
     * @param factoryCode
     * @return
     */
    public List<OrderFactoryNoDO> orderDiyByFactoryCodeAndNoLogistics(String factoryCode, Date startTime){
        List<OrderFactoryNoDO> orderFactoryNoDOS = orderInfoDOMapper.listOrderFactoryNoByFactoryCodeAndNoLogistics(factoryCode,startTime);
        return orderFactoryNoDOS;
    }
}
