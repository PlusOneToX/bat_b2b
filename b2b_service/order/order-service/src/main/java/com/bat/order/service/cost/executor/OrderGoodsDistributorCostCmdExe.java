package com.bat.order.service.cost.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.cost.convertor.OrderCostConvertor;
import com.bat.order.service.order.executor.OrderRpcExe;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeDetailCmd;
import com.bat.order.dao.cost.OrderGoodsDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.mq.dto.GoodsSaleDTO;

@Component
public class OrderGoodsDistributorCostCmdExe {
    @Resource
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostMapper;
    @Resource
    private OrderRpcExe orderRpcExe;

    public void createList(List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList) {
        orderGoodsDistributorCostDOList.stream().forEach(orderGoodsDistributorCostDO -> {
            orderGoodsDistributorCostDO.setCreateTime(new Date());
            orderGoodsDistributorCostDO.setUpdateTime(new Date());
            orderGoodsDistributorCostMapper.insert(orderGoodsDistributorCostDO);
        });
    }

    /**
     * 根据erp变更单变更分销商明细费用（只修改同步erp层数据）
     * 
     * @param orderDetails
     * @param orderGoodsDOS
     * @param changeOrderGoodss
     * @param addOrderGoodsDOS
     * @param deleteOrderGoodsDOS
     * @param erpDistributorData
     * @param changeMap
     * @return
     */
    public List<OrderGoodsDistributorCostDO> changeOrderGoodsDistributorCost(List<ErpOrderChangeDetailCmd> orderDetails,
        List<OrderGoodsDO> orderGoodsDOS, List<ErpOrderChangeDetailCmd> changeOrderGoodss,
        List<OrderGoodsDO> addOrderGoodsDOS, List<OrderGoodsDO> deleteOrderGoodsDOS,
        OrderDistributorDataDO erpDistributorData, Map<String, List<Object>> changeMap, List<GoodsSaleDTO> saleDTOS) {
        Map<Integer, OrderGoodsDO> orderGoodsDOMap = orderGoodsDOS.stream()
            .collect(Collectors.toMap(OrderGoodsDO::getSerialNumber, orderGoodsDO -> orderGoodsDO));
        List<OrderGoodsDistributorCostDO> distributorCostDOS = orderGoodsDistributorCostMapper
            .listByOrderIdAndDistributorId(erpDistributorData.getOrderId(), erpDistributorData.getDistributorId());
        Date time = new Date(System.currentTimeMillis());
        List<OrderGoodsDistributorCostDO> addDistributorCostDOS = new ArrayList<>();
        List<OrderGoodsDistributorCostDO> changeDistributorCostDOS = new ArrayList<>();
        List<Integer> deleteOrderGoodsIds = new ArrayList<>();
        // 增加行项目情况
        if (!CollectionUtils.isEmpty(addOrderGoodsDOS)) {
            List<GoodsItemPriceRpcDTO> priceRpcDTOS =
                orderRpcExe.listDistributorGoodsItemPrice(addOrderGoodsDOS, erpDistributorData.getDistributorId());
            addDistributorCostDOS.addAll(OrderCostConvertor.toAddOrderGoodsDistributorCostDOList(orderDetails,
                addOrderGoodsDOS, priceRpcDTOS, erpDistributorData, time));
        }
        // 修改行项目
        if (!CollectionUtils.isEmpty(changeOrderGoodss)) {
            changeDistributorCostDOS.addAll(OrderCostConvertor.toChangeOrderGoodsDistributorCostDOList(orderGoodsDOMap,
                changeOrderGoodss, distributorCostDOS, changeMap));
        }
        // 删除行项目
        if (!CollectionUtils.isEmpty(deleteOrderGoodsDOS)) {
            deleteOrderGoodsIds
                .addAll(deleteOrderGoodsDOS.stream().map(OrderGoodsDO::getId).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(addDistributorCostDOS)) {
            orderGoodsDistributorCostMapper.insertList(addDistributorCostDOS);
            distributorCostDOS.addAll(addDistributorCostDOS);
        }
        if (!CollectionUtils.isEmpty(deleteOrderGoodsIds)) {
            orderGoodsDistributorCostMapper.deleteByOrderGoodsIds(deleteOrderGoodsIds);
            distributorCostDOS = distributorCostDOS.stream()
                .filter(distributorCostDO -> !deleteOrderGoodsIds.contains(distributorCostDO.getOrderGoodsId()))
                .collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(changeDistributorCostDOS)) {
            orderGoodsDistributorCostMapper.updateList(changeDistributorCostDOS);
        }
        // 组装分销层明细费用和订单明细
        OrderCostConvertor.toOrderGoodsDistributorCostDOList(orderGoodsDOS, distributorCostDOS, saleDTOS);
        return distributorCostDOS;
    }

    public void updateList(List<OrderGoodsDistributorCostDO> distributorCostDOS) {
        orderGoodsDistributorCostMapper.updateList(distributorCostDOS);
    }
}
