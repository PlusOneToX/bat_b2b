package com.bat.order.service.stock.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.stock.convertor.OrderGoodsStockConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO2;
import com.bat.order.dao.stock.OrderGoodsStockDOMapper;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO2;
import com.bat.order.mq.dto.OrderErpNoLineDTO;
import com.bat.order.service.order.executor.OrderRpcExe;

@Component
public class OrderGoodsStockCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsStockCmdExe.class);

    @Resource
    private OrderGoodsStockDOMapper orderGoodsStockDOMapper;
    @Resource
    private OrderGoodsStockRpcExe orderGoodsStockRpcExe;
    @Resource
    private MessageSendService sendService;
    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;
    @Resource
    OrderRpcExe orderRpcExe;

    /**
     * 根据订单id解锁库存 在线支付未支付自动关闭
     *
     * 来源：在线支付未支付自动关闭
     *
     * @param orderId
     */
    @Transactional(rollbackFor = Exception.class)
    public void releaseOrderGoodsStock(Integer orderId) {
        LOGGER.info("库存解锁 来源：在线支付未支付自动关闭 releaseOrderGoodsStock orderId：{}", orderId);
        List<OrderGoodsStockDO> orderGoodsStockDOS = orderGoodsStockDOMapper.listByOrderId(orderId);
        List<ItemStockLockDTO> lockDTOS = OrderGoodsStockConvertor.toItemStockLockDTOList(orderGoodsStockDOS);
        // 根据订单id解锁库存 在线支付未支付自动关闭
        sendService.orderUnLockStock(lockDTOS, orderId);
        orderGoodsStockDOMapper.deleteByOrderId(orderId);
    }

    /**
     * 根据订单id解锁库存消息
     *
     * 来源：取消B2B2C订单
     *
     * @param orderId
     */
    public List<ItemStockLockDTO> getReleaseOrderGoodsStock(Integer orderId) {
        LOGGER.info("库存解锁 来源：取消B2B2C订单 getReleaseOrderGoodsStock orderId：{}", orderId);
        List<OrderGoodsStockDO> orderGoodsStockDOS = orderGoodsStockDOMapper.listByOrderId(orderId);
        List<ItemStockLockDTO> dtos = OrderGoodsStockConvertor.toItemStockLockDTOList(orderGoodsStockDOS);
        orderGoodsStockDOMapper.deleteByOrderId(orderId);
        return dtos;
    }

    /**
     * 根据订单ids解锁库存
     *
     * 来源：分销订单审核接口(支持批量审核)
     *
     * @param orderIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void releaseOrderGoodsStock(List<Integer> orderIds) {
        LOGGER.info("库存解锁 来源：分销订单审核接口(支持批量审核) releaseOrderGoodsStock orderIds：{}", orderIds);
        List<OrderGoodsStockDO> orderGoodsStockDOS = orderGoodsStockDOMapper.listByOrderIds(orderIds);
        orderGoodsStockRpcExe.releaseOrderGoodsStock(orderGoodsStockDOS);
        orderGoodsStockDOMapper.deleteByOrderIds(orderIds);
    }

    /**
     * 订单商品锁库记录解锁
     * 
     * @param orderGoodsStockUnLockList
     */
    public void unLock(List<OrderGoodsStockSimpleDTO> orderGoodsStockUnLockList) {
        LOGGER.info("库存解锁 来源：(仓库服务)发送消息、进行订单商品锁库解锁操作 unLock orderGoodsStockUnLockList：{}",
            JSON.toJSONString(orderGoodsStockUnLockList));
        if (CollectionUtils.isEmpty(orderGoodsStockUnLockList)) {
            return;
        }
        for (OrderGoodsStockSimpleDTO orderGoodsStockSimpleDTO : orderGoodsStockUnLockList) {
            if (orderGoodsStockSimpleDTO.getLockNum() == 0) {
                // 已经全都解锁、删除记录（LockNum在仓库服务进行了计算）
                orderGoodsStockDOMapper.deleteByPrimaryKey(orderGoodsStockSimpleDTO.getId());
                continue;
            }
            LOGGER.info("货品部分出库、需要修改锁定数据、{}", JSON.toJSONString(orderGoodsStockSimpleDTO));
            OrderGoodsStockDO orderGoodsStockDO =
                orderGoodsStockDOMapper.selectByPrimaryKey(orderGoodsStockSimpleDTO.getId());
            orderGoodsStockDO.setLockNum(orderGoodsStockSimpleDTO.getLockNum());
            orderGoodsStockDO.setUpdateTime(new Date());
            orderGoodsStockDOMapper.updateByPrimaryKey(orderGoodsStockDO);
        }
    }

    public void batchCreate(List<OrderGoodsStockDO> createList) {
        orderGoodsStockDOMapper.insertList(createList);
    }

    public void batchUpdate(List<OrderGoodsStockDO> updateGoodsStockList) {
        orderGoodsStockDOMapper.batchUpdate(updateGoodsStockList);
    }

    /**
     * 根据订单明细锁定库存
     *
     * @param addOrderGoodsDOS
     */
    public void saveOrderGoodsStock(List<OrderGoodsDO> addOrderGoodsDOS) {
        List<OrderGoodsStockDO> stockDOS = new ArrayList<>();
        addOrderGoodsDOS.forEach(addOrderGoodsDO -> {
            List<OrderGoodsStockDO> orderGoodsStocks = addOrderGoodsDO.getOrderGoodsStocks();
            if (!CollectionUtils.isEmpty(orderGoodsStocks)) {
                orderGoodsStocks.forEach(orderGoodsStock -> {
                    orderGoodsStock.setOrderId(addOrderGoodsDO.getOrderId());
                    orderGoodsStock.setOrderGoodsId(addOrderGoodsDO.getId());
                    stockDOS.add(orderGoodsStock);
                });
            }
        });
        if (!CollectionUtils.isEmpty(stockDOS)) {
            orderGoodsStockDOMapper.insertList(stockDOS);
        }
    }

    /**
     * 根据明细锁定库存
     * 
     * @param goodsItemCountDTOMap
     * @param addLockDTOS
     */
    public void saveOrderGoodsStock(Map<OrderGoodsDO, GoodsItemCountDTO> goodsItemCountDTOMap,
        List<ItemStockLockDTO> addLockDTOS) {
        List<OrderGoodsStockDO> orderGoodsStockDOS =
            OrderGoodsStockConvertor.toOrderGoodsStockDOList(goodsItemCountDTOMap, addLockDTOS);
        if (!CollectionUtils.isEmpty(orderGoodsStockDOS)) {
            orderGoodsStockDOMapper.insertList(orderGoodsStockDOS);
        }
    }

    /**
     * 根据订单明细删除锁库明细
     *
     * 来源：ERP订单明细变更(返回变更后的分销层明细费用)
     * 
     * @param orderGoodsIds
     */
    public void deleteOrderGoodsStockByOrderGoodsIds(List<Integer> orderGoodsIds) {
        LOGGER.info("库存解锁 来源：ERP订单明细变更(返回变更后的分销层明细费用) deleteOrderGoodsStockByOrderGoodsIds orderGoodsIds：{}",
            orderGoodsIds);
        orderGoodsStockDOMapper.deleteByOrderGoodsIds(orderGoodsIds);
    }

    /**
     * 根据ids删除锁库记录
     *
     * 来源：ERP订单明细变更(返回变更后的分销层明细费用)
     * 
     * @param ids
     */
    public void deleteByIds(List<Integer> ids) {
        LOGGER.info("库存解锁 来源：ERP订单明细变更(返回变更后的分销层明细费用) deleteByIds ids：{}", ids);
        orderGoodsStockDOMapper.deleteByIds(ids);
    }

    /**
     * 根据ERP订单号和行序号列表解锁或加锁B2B订单
     *
     * @param orderErpNoLineDTOS
     */
    public void unLockOrderErpNoLineDTO(List<OrderErpNoLineDTO> orderErpNoLineDTOS) {
        List<ItemStockLockDTO> stockLockDTOS = new ArrayList<>();
        try {
            List<String> orderErpNos =
                orderErpNoLineDTOS.stream().map(OrderErpNoLineDTO::getOrderErpNo).collect(Collectors.toList());
            List<OrderGoodsStockDO2> orderGoodsStockDO2S = orderGoodsStockDOMapper.listByOrderErpNos(orderErpNos);
            List<Integer> deleteIds = new ArrayList<>();
            List<OrderGoodsStockDO2> changeOrderGoodsStockDOS = new ArrayList<>();
            // 加锁明细
            List<OrderGoodsStockDO> addOrderGoodsStockDOS = new ArrayList<>();
            // 加锁列表
            List<OrderErpNoLineDTO> orderErpNoLineDTOS2 = new ArrayList<>();
            Map<Integer, List<ItemStockLockDTO>> unOrderLockStockMap = OrderGoodsStockConvertor.toOrderUnLockStock(
                orderErpNoLineDTOS, orderGoodsStockDO2S, deleteIds, changeOrderGoodsStockDOS, orderErpNoLineDTOS2);
            // 订单取消发货加锁情况
            if (!CollectionUtils.isEmpty(orderErpNoLineDTOS2)) {
                List<OrderGoodsDO2> orderGoodsDO2S = orderGoodsDOMapper.listOrderGoodsDO2ByOrderErpNos(orderErpNos);
                if (!CollectionUtils.isEmpty(orderGoodsDO2S)) {
                    Integer distributorId = orderGoodsDO2S.get(0).getDistributorId();
                    List<GoodsItemCountDTO> goodsItemCountDTOS = OrderGoodsStockConvertor
                        .toOrderLockStock(orderErpNoLineDTOS2, orderGoodsDO2S, addOrderGoodsStockDOS);
                    // 锁定库存
                    List<ItemStockLockDTO> itemStockLockDTOS =
                        orderRpcExe.summaryLockStock(goodsItemCountDTOS, null, distributorId, null);
                    if (!CollectionUtils.isEmpty(itemStockLockDTOS)) {
                        stockLockDTOS.addAll(itemStockLockDTOS);
                        LOGGER.info("订单锁库明细：{}", JSONObject.toJSONString(itemStockLockDTOS));
                        OrderGoodsStockConvertor.toOrderGoodsStockDOS(addOrderGoodsStockDOS, stockLockDTOS);
                    }
                }
            }
            // 更新订单行项目锁库数据
            if (!CollectionUtils.isEmpty(changeOrderGoodsStockDOS)) {
                orderGoodsStockDOMapper.batchUpdateOrderGoodsStock(changeOrderGoodsStockDOS);
            }
            if (!CollectionUtils.isEmpty(deleteIds)) {
                orderGoodsStockDOMapper.deleteByIds(deleteIds);
            }
            if (!CollectionUtils.isEmpty(addOrderGoodsStockDOS)) {
                orderGoodsStockDOMapper.insertList(addOrderGoodsStockDOS);
            }
            // 订单解锁库存消息
            if (!CollectionUtils.isEmpty(unOrderLockStockMap)) {
                unOrderLockStockMap.forEach((k, v) -> {
                    sendService.orderUnLockStock(v, k);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!CollectionUtils.isEmpty(stockLockDTOS)) {
                sendService.orderUnLockStock(stockLockDTOS, null);
            }
            throw e;
        }

    }
}
