package com.bat.order.service.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.error.DubboServiceErrorCode;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.stock.api.OrderGoodsStockServiceRpc;
import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberIdList;
import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberList;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.service.stock.executor.OrderGoodsStockCmdExe;
import com.bat.order.service.stock.executor.OrderGoodsStockQryExe;
import com.bat.order.service.stock.executor.OrderGoodsStockRpcExe;

@DubboService
public class OrderGoodsStockServiceRpcImpl implements OrderGoodsStockServiceRpc {

    @Resource
    private OrderGoodsStockQryExe orderGoodsStockQryExe;

    @Resource
    private OrderGoodsStockRpcExe orderGoodsStockRpcExe;

    @Resource
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;

    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;

    @Override
    public Response<List<OrderGoodsStockSimpleDTO>>
        listByOrderErpNoAndSerialNumber(List<OrderErpNoLineNumberIdList> lineNumberIdLists) {
        try {
            List<OrderGoodsStockSimpleDTO> list =
                orderGoodsStockQryExe.listByOrderErpNoAndSerialNumber(lineNumberIdLists);
            // 类型转换
            return Response.of(list);
        } catch (OrderException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    /**
     * 根据ERP订单编号和行序号进行解锁库存
     * 
     * @param lineNumberLists
     * @return
     */
    @Override
    public Response unLockOrderErpNoAndSerialNumber(List<OrderErpNoLineNumberList> lineNumberLists) {
        try {
            orderGoodsStockRpcExe.unLockOrderErpNoAndSerialNumber(lineNumberLists);
            // 类型转换
            return Response.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(DubboServiceErrorCode.DUBBO_ORDER_UNLOCK_EXCEPTION, MessageUtils.get(DubboServiceErrorCode.DUBBO_ORDER_UNLOCK_EXCEPTION));
        }
    }

    /**
     *
     * @param warehousesId
     * @param lockType
     *            锁定类型 1.在库 2.在途 3.vmi
     * @return
     */
    @Override
    public Response<List<OrderGoodsStockSimpleDTO>> sumByWarehouseIdAndLockTypeGroupByItemId(Integer warehousesId,
        Short lockType, Integer itemId) {
        List<OrderGoodsStockDO> orderGoodsStockDOList =
            orderGoodsStockQryExe.sumByWarehouseIdAndLockTypeGroupByItemId(lockType, warehousesId, itemId);

        return Response.of(BeanUtils.copyList(orderGoodsStockDOList, OrderGoodsStockSimpleDTO.class));
    }

    /**
     * 根据货品ids刷新锁库记录
     * 
     * @param itemIds
     * @return
     */
    @Override
    public Response<List<OrderGoodsStockSimpleDTO>> sumByItemIds(List<Integer> itemIds) {
        List<OrderGoodsStockDO> orderGoodsStockDOS = orderGoodsStockQryExe.sumByWarehouseIdAndByItemIds(itemIds);
        List<Integer> deleteIds = new ArrayList<>();
        List<OrderGoodsStockDO> changeOrderGoodsStockDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderGoodsStockDOS)) {
            List<Integer> orderGoodsIds = orderGoodsStockDOS.stream().map(OrderGoodsStockDO::getOrderGoodsId).distinct()
                .collect(Collectors.toList());
            List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByIdsAndOrderStatus(orderGoodsIds);
            List<Integer> exitOrderGoodsIds =
                orderGoodsDOS.stream().map(OrderGoodsDO::getId).collect(Collectors.toList());
            // 删除可能关闭或完成的订单锁库记录
            List<OrderGoodsStockDO> notExitOrderGoodsStockDOs = orderGoodsStockDOS.stream()
                .filter(orderGoodsStockDO -> !exitOrderGoodsIds.contains(orderGoodsStockDO.getOrderGoodsId()))
                .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(notExitOrderGoodsStockDOs)) {
                orderGoodsStockDOS.removeAll(notExitOrderGoodsStockDOs);
                deleteIds.addAll(
                    notExitOrderGoodsStockDOs.stream().map(OrderGoodsStockDO::getId).collect(Collectors.toList()));
            }
            // 根据订单明细发货情况重置锁库记录
            orderGoodsDOS.forEach(orderGoodsDO -> {
                List<OrderGoodsStockDO> goodsStockDOS = orderGoodsStockDOS.stream()
                    .filter(orderGoodsStockDO -> orderGoodsStockDO.getOrderGoodsId().equals(orderGoodsDO.getId()))
                    .collect(Collectors.toList());
                AtomicReference<Integer> unDeliverCount = new AtomicReference<>(orderGoodsDO.getUnDeliverCount());
                if (unDeliverCount.get() == 0) {// 已全部发货，删除锁库记录
                    deleteIds.addAll(goodsStockDOS.stream().map(OrderGoodsStockDO::getId).collect(Collectors.toList()));
                    orderGoodsStockDOS.removeAll(goodsStockDOS);
                } else if (unDeliverCount.get() > 0) {
                    goodsStockDOS.forEach(goodsStockDO -> {
                        if (unDeliverCount.get() == 0) {// 循环处理，当未发货数量为零时说明锁库记录的数量已经足够，其他锁库记录多余需删除
                            deleteIds.add(goodsStockDO.getId());
                            orderGoodsStockDOS.remove(goodsStockDO);
                        } else {
                            int lockNum = unDeliverCount.get() - goodsStockDO.getLockNum();
                            if (lockNum < 0) {// 当锁库记录大于未发数量时，需修改锁库记录
                                goodsStockDO.setLockNum(unDeliverCount.get());
                                changeOrderGoodsStockDOS.add(goodsStockDO);
                                unDeliverCount.set(0);
                            } else {
                                unDeliverCount.set(lockNum);
                            }
                        }
                    });
                }
            });
            if (!CollectionUtils.isEmpty(deleteIds)) {
                orderGoodsStockCmdExe.deleteByIds(deleteIds);
            }
            if (!CollectionUtils.isEmpty(changeOrderGoodsStockDOS)) {
                orderGoodsStockCmdExe.batchUpdate(changeOrderGoodsStockDOS);
            }
        }
        return Response.of(BeanUtils.copyList(orderGoodsStockDOS, OrderGoodsStockSimpleDTO.class));
    }
}
