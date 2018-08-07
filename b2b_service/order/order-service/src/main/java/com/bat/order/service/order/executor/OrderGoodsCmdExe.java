package com.bat.order.service.order.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.config.ConfigQry;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.order.convertor.OrderGoodsConvertor;
import com.bat.order.service.stock.executor.OrderGoodsStockCmdExe;
import com.bat.order.service.stock.executor.OrderGoodsStockQryExe;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeDetailCmd;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.mq.dto.GoodsSaleDTO;
import com.bat.order.service.cost.executor.OrderGoodsDistributorCostCmdExe;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderGoodsCmdExe {

    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;
    @Resource
    private ConfigQry configQry;
    @Resource
    CommonValidator commonValidator;
    @Resource
    OrderRpcExe orderRpcExe;
    @Resource
    private OrderGoodsDistributorCostCmdExe distributorCostCmdExe;
    @Resource
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;
    @Resource
    private OrderGoodsStockQryExe orderGoodsStockQryExe;

    @Resource
    private OrderTypeQryExe orderTypeQryExe;

    public void create(OrderGoodsDO orderGoodsDO) {
        orderGoodsDOMapper.insert(orderGoodsDO);
    }

    public void update(OrderGoodsDO orderGoodsDO) {
        orderGoodsDOMapper.updateByPrimaryKey(orderGoodsDO);
    }

    /**
     * ERP订单明细变更(返回变更后的分销层明细费用)
     * 
     * @param cmd
     * @param erpDistributorData
     */
    public List<OrderGoodsDistributorCostDO> orderChangeByErp(ErpOrderChangeCmd cmd, OrderInfoDO order,
        DistributorRpcDTO distributor, OrderDistributorDataDO erpDistributorData, List<ItemStockLockDTO> unLockDTOS,
        List<ItemStockLockDTO> lockDTOS, Map<String, List<Object>> changeMap, List<GoodsSaleDTO> saleDTOS) {
        OrderConfig orderConfig = configQry.getTenantErpConfig();
        List<ErpOrderChangeDetailCmd> orderDetails = cmd.getOrderDetails().stream()
            .filter(orderDetail -> !orderDetail.getItemNo().equals(orderConfig.getWlfItemNo()))
            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(orderDetails)) {
            List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(erpDistributorData.getOrderId());
            List<ErpOrderChangeDetailCmd> addOrderGoodss = new ArrayList<>();
            List<ErpOrderChangeDetailCmd> changeOrderGoodss = new ArrayList<>();
            List<ErpOrderChangeDetailCmd> deleteOrderGoodss = new ArrayList<>();
            // 物料费用明细，增 改 删
            orderDetails.forEach(orderDetail -> {
                if (orderDetail.getItemFChangeType().equals(Constant.ORDER_ERP_CHANGE_A)) {
                    addOrderGoodss.add(orderDetail);
                } else if (orderDetail.getItemFChangeType().equals(Constant.ORDER_ERP_CHANGE_B)) {
                    changeOrderGoodss.add(orderDetail);
                } else if (orderDetail.getItemFChangeType().equals(Constant.ORDER_ERP_CHANGE_D)) {
                    deleteOrderGoodss.add(orderDetail);
                }
            });
            List<OrderTypeDO> mtoOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_2);
            // 增加行项目
            List<OrderGoodsDO> addOrderGoodsDOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(addOrderGoodss)) {
                List<String> itemCodes = addOrderGoodss.stream().map(ErpOrderChangeDetailCmd::getItemNo).distinct()
                    .collect(Collectors.toList());
                List<GoodsItemRpcDTO> itemRpcDTOS = commonValidator.checkGoodsItems(itemCodes);
                Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap = itemRpcDTOS.stream()
                    .collect(Collectors.toMap(GoodsItemRpcDTO::getItemCode, itemRpcDTO -> itemRpcDTO));
                // 库存计算锁库数据(预售订单不参与锁库)
                List<GoodsItemCountDTO> goodsItemCountDTOS = new ArrayList<>();
                List<ItemStockLockDTO> addLockDTOS = new ArrayList<>();
                if (mtoOrderTypes == null || !order.getOrderTypeId().equals(mtoOrderTypes.get(0).getId())) {
                    goodsItemCountDTOS = OrderGoodsConvertor.toGoodsItemCountDTO(goodsItemRpcDTOMap, addOrderGoodss);
                    addLockDTOS = orderRpcExe.summaryLockStock(goodsItemCountDTOS, distributor.getSalesAreaIds(),
                        distributor.getId(), null);
                }
                addOrderGoodsDOS = OrderGoodsConvertor.toAddOrderGoodsDTOList(addOrderGoodss, goodsItemRpcDTOMap,
                    erpDistributorData, addLockDTOS, changeMap);
                orderGoodsDOMapper.insertList(addOrderGoodsDOS);
                // 商品订单销售量变更
                addOrderGoodsDOS.forEach(orderGoodsDO -> {
                    GoodsSaleDTO saleDTO = new GoodsSaleDTO();
                    saleDTO.setOrderGoodsId(orderGoodsDO.getId());
                    saleDTO.setItemId(orderGoodsDO.getItemId());
                    saleDTO.setGoodsId(orderGoodsDO.getGoodsId());
                    saleDTO.setChangeType(Constant.CHANGE_TYPE_1);
                    saleDTO.setSaleNum(orderGoodsDO.getItemCount());
                    saleDTOS.add(saleDTO);
                });
                // 订单明细库存锁定更新
                if (CollectionUtils.isEmpty(mtoOrderTypes)
                    || !order.getOrderTypeId().equals(mtoOrderTypes.get(0).getId())) {
                    orderGoodsStockCmdExe.saveOrderGoodsStock(addOrderGoodsDOS);
                    lockDTOS.addAll(addLockDTOS);
                }
                orderGoodsDOS.addAll(addOrderGoodsDOS);
            }
            // 修改、删除行项目
            List<OrderGoodsDO> changeOrderGoodsDOS;
            List<OrderGoodsDO> deleteOrderGoodsDOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(changeOrderGoodss) || !CollectionUtils.isEmpty(deleteOrderGoodss)) {
                List<OrderGoodsStockDO> orderGoodsStockDOS =
                    orderGoodsStockQryExe.listByOrderId(erpDistributorData.getOrderId());
                Map<Integer, List<OrderGoodsStockDO>> orderGoodsStockDOSMap =
                    orderGoodsStockDOS.stream().collect(Collectors.groupingBy(OrderGoodsStockDO::getOrderGoodsId));
                List<OrderGoodsStockDO> changeStockDOS = new ArrayList<>();
                Map<OrderGoodsDO, GoodsItemCountDTO> goodsItemCountDTOMap = new HashMap<>();
                List<Integer> deleteOrderStockIds = new ArrayList<>();
                Map<Integer, OrderGoodsDO> orderGoodsDOMap = orderGoodsDOS.stream()
                    .collect(Collectors.toMap(OrderGoodsDO::getSerialNumber, orderGoodsDO -> orderGoodsDO));
                // 库存变更处理（明细和反锁库）
                changeOrderGoodsDOS = OrderGoodsConvertor.toChangeOrderGoodsDTOList(changeOrderGoodss, orderGoodsDOMap,
                    unLockDTOS, goodsItemCountDTOMap, orderGoodsStockDOSMap, changeStockDOS, deleteOrderStockIds,
                    changeMap, saleDTOS);
                // 库存计算锁库数据,更新订单明细锁库信息
                if (!CollectionUtils.isEmpty(goodsItemCountDTOMap)
                    && (mtoOrderTypes == null || !order.getOrderTypeId().equals(mtoOrderTypes.get(0).getId()))) {
                    List<ItemStockLockDTO> addLockDTOS =
                        orderRpcExe.summaryLockStock(new ArrayList<>(goodsItemCountDTOMap.values()),
                            distributor.getSalesAreaIds(), distributor.getId(), null);
                    lockDTOS.addAll(addLockDTOS);
                    // 针对订单数量增多情况需增加一条新的锁库记录
                    orderGoodsStockCmdExe.saveOrderGoodsStock(goodsItemCountDTOMap, addLockDTOS);
                }
                if (!CollectionUtils.isEmpty(deleteOrderStockIds)) {
                    orderGoodsStockCmdExe.deleteByIds(deleteOrderStockIds);
                }
                if (!CollectionUtils.isEmpty(changeStockDOS)) {
                    orderGoodsStockCmdExe.batchUpdate(changeStockDOS);
                }
                deleteOrderGoodsDOS = OrderGoodsConvertor.toDeleteOrderGoodsDTOList(deleteOrderGoodss, orderGoodsDOMap,
                    orderGoodsStockDOSMap, unLockDTOS, changeMap, saleDTOS);
                if (!CollectionUtils.isEmpty(changeOrderGoodsDOS)) {
                    orderGoodsDOMapper.updateList(changeOrderGoodsDOS);
                }
                if (!CollectionUtils.isEmpty(deleteOrderGoodsDOS)) {
                    List<Integer> ids =
                        deleteOrderGoodsDOS.stream().map(OrderGoodsDO::getId).collect(Collectors.toList());
                    orderGoodsStockCmdExe.deleteOrderGoodsStockByOrderGoodsIds(ids);
                    orderGoodsDOMapper.deleteByIds(ids);
                }
            }
            List<OrderGoodsDistributorCostDO> distributorCostDOS =
                distributorCostCmdExe.changeOrderGoodsDistributorCost(orderDetails, orderGoodsDOS, changeOrderGoodss,
                    addOrderGoodsDOS, deleteOrderGoodsDOS, erpDistributorData, changeMap, saleDTOS);
            return distributorCostDOS;
        }
        return null;
    }

    public void batchUpdate(List<OrderGoodsDO> orderGoodsDOList) {
        orderGoodsDOMapper.updateList(orderGoodsDOList);
    }
}
