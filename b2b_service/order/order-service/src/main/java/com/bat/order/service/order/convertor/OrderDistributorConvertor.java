package com.bat.order.service.order.convertor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.data.dto.*;
import com.bat.order.service.common.enumtype.*;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.VminStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.WarehouseInStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.cost.dto.data.OrderDistributorCostDTO;
import com.bat.order.api.data.dto.data.OrderDistributorDataDTO;
import com.bat.order.api.order.dto.common.OrderInvoiceCmd;
import com.bat.order.api.order.dto.distributor.*;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.mq.dto.OrderTreeNodeDataDTO;
import com.bat.order.service.common.data.dto.*;
import com.bat.order.service.common.enumtype.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/19 12:48
 */
public class OrderDistributorConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDistributorConvertor.class);

    public static OrderInfoDTO toOrderInfoDTO(OrderInfoCmd cmd, String platform) {
        OrderInfoDTO dto = new OrderInfoDTO();
        BeanUtils.copyProperties(cmd, dto);
        dto.setOrderSource(platform);
        List<OrderGoodsDTO> goodsDTOS = new ArrayList<>();
        dto.setGoodss(goodsDTOS);
        List<OrderGoodsCmd> goodss = cmd.getGoodss();
        goodss.forEach(goods -> {
            OrderGoodsDTO goodsDTO = new OrderGoodsDTO();
            BeanUtils.copyProperties(goods, goodsDTO);
            goodsDTO.setSerialNumber(goodss.indexOf(goods) + 1);
            goodsDTOS.add(goodsDTO);
            OrderGoodsDiyCmd diy = goods.getDiy();
            if (diy != null) {
                OrderGoodsDiyDTO diyDTO = new OrderGoodsDiyDTO();
                BeanUtils.copyProperties(diy, diyDTO);
                goodsDTO.setDiy(diyDTO);
            }
        });
        List<OrderLogisticsCmd> logisticss = cmd.getLogisticss();
        if (!CollectionUtils.isEmpty(logisticss)) {
            List<OrderLogisticsDTO> logisticsDTOS = new ArrayList<>();
            dto.setLogisticss(logisticsDTOS);
            logisticss.forEach(logistics -> {
                OrderLogisticsDTO logisticsDTO = new OrderLogisticsDTO();
                BeanUtils.copyProperties(logistics, logisticsDTO);
                logisticsDTOS.add(logisticsDTO);
            });
        }
        OrderDeliveryCmd delivery = cmd.getDelivery();
        if (delivery != null) {
            OrderDeliveryDTO deliveryDTO = new OrderDeliveryDTO();
            BeanUtils.copyProperties(delivery, deliveryDTO);
            dto.setDelivery(deliveryDTO);
        }
        OrderInvoiceCmd invoice = cmd.getInvoice();
        if (invoice != null) {
            OrderInvoiceDTO invoiceDTO = new OrderInvoiceDTO();
            BeanUtils.copyProperties(invoice, invoiceDTO);
            dto.setInvoice(invoiceDTO);
        }
        BigDecimal rebateVoucherAmount = cmd.getRebateVoucherAmount();
        boolean amount = rebateVoucherAmount != null && rebateVoucherAmount.compareTo(BigDecimal.ZERO) != 0;
        boolean ids = !CollectionUtils.isEmpty(cmd.getRebateVoucherIds());
        if (amount && ids) {
            dto.setRebateVoucherAmount(rebateVoucherAmount);
            dto.setRebateVoucherIds(cmd.getRebateVoucherIds());
        }
        return dto;
    }

    public static GoodsItemPriceRpcQry toGoodsItemPriceRpcQry(Integer distributorId, List<OrderGoodsDTO> goodss,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap) {
        GoodsItemPriceRpcQry rpcQry = new GoodsItemPriceRpcQry();
        rpcQry.setDistributorId(distributorId);
        rpcQry.setRetailPriceFlag(Constant.RETAIL_PRICE_FLAG_0);
        List<Integer> itemIds = new ArrayList<>();
        List<GoodsItemRpc> goodsItems = new ArrayList<>();
        rpcQry.setGoodsItems(goodsItems);
        goodss.forEach(goods -> {
            GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
            if (!itemIds.contains(rpcDTO.getId()) && goods.getSalePrice() == null) {
                GoodsItemRpc rpc = new GoodsItemRpc();
                rpc.setItemId(rpcDTO.getId());
                rpc.setGoodsId(rpcDTO.getGoodsId());
                itemIds.add(rpc.getItemId());
                goodsItems.add(rpc);
            }
        });
        return rpcQry;
    }

    public static GoodsItemPromotionPriceRpcQry toGoodsItemPromotionPriceRpcQry(Integer distributorId, Date time,
        List<OrderGoodsDTO> goodss, List<GoodsItemPriceRpcDTO> priceRpcDTOS,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap) {
        GoodsItemPromotionPriceRpcQry rpcQry = new GoodsItemPromotionPriceRpcQry();
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = priceRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO));
        rpcQry.setDistributorId(distributorId);
        rpcQry.setOrderTime(time);
        List<com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry> goodsItemPrices = new ArrayList<>();
        rpcQry.setGoodsItemPrices(goodsItemPrices);
        goodss.forEach(goods -> {
            if ((goods.getGroupSeckillId() == null && goods.getOrderPromotionId() == null
                && goods.getGoodsPromotionId() == null) || goods.getSalePrice() != null) {
                return;
            }
            GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
            GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(rpcDTO.getId());
            if (priceRpcDTO == null || priceRpcDTO.getSalePrice() == null) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_PRICE_ERROR,
                    "\"" + goods.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_PRICE_ERROR));
            }
            com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry priceRpcQry =
                new com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry();
            BeanUtils.copyProperties(goods, priceRpcQry);
            BeanUtils.copyProperties(rpcDTO, priceRpcQry);
            OrderGoodsDiyDTO diy = goods.getDiy();
            if (diy != null) {
                priceRpcQry.setMaterialId(diy.getMaterialId());
                priceRpcQry.setModelId(diy.getModelId());
            }
            priceRpcQry.setItemId(rpcDTO.getId());
            priceRpcQry.setSpellGroupId(goods.getGroupSeckillId());
            goodsItemPrices.add(priceRpcQry);
            priceRpcQry.setSalePrice(priceRpcDTO.getSalePrice());
        });
        return rpcQry;
    }

    public static List<GoodsItemCountDTO> toGoodsItemCountDTOList(List<OrderGoodsDTO> goodss,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap) {
        // Map<Integer, GoodsItemCountDTO> countDTOMap = new HashMap<>();
        List<GoodsItemCountDTO> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodss)) {
            goodss.forEach(goods -> {
                GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
                if (rpcDTO.getGoodsType().equals(Constant.GOODS_TYPE_1) && goods.getMtoType().equals(Constant.MTO_TYPE_0)) {
                    /*相同货品不合并数量
                     GoodsItemCountDTO countDTO = countDTOMap.get(rpcDTO.getId());
                    if (countDTO == null) {
                        countDTO = new GoodsItemCountDTO();
                        countDTO.setItemId(rpcDTO.getId());
                        countDTO.setInStockCount(goods.getItemInCount());
                        countDTO.setOnWayCount(goods.getItemOnWayCount());
                        countDTOMap.put(rpcDTO.getId(), countDTO);
                    } else {
                        countDTO
                            .setInStockCount(countDTO.getInStockCount().intValue() + goods.getItemInCount().intValue());
                        countDTO
                            .setOnWayCount(countDTO.getOnWayCount().intValue() + goods.getItemOnWayCount().intValue());
                    }*/
                    GoodsItemCountDTO goodsItemCountDTO = new GoodsItemCountDTO();
                    goodsItemCountDTO.setItemId(rpcDTO.getId());
                    goodsItemCountDTO.setInStockCount(goods.getItemInCount());
                    goodsItemCountDTO.setOnWayCount(goods.getItemOnWayCount());
                    if (goods.getOversoldFlag().equals(Constant.OVERSOLD_FLAG_1)) {
                        goodsItemCountDTO.setSupportOversoldFlag(true);
                    }
                    resultList.add(goodsItemCountDTO);
                }
            });
        }
        // return countDTOMap.values().stream().collect(Collectors.toList());
        return resultList;
    }

    public static LogisticsCalculationRpcQry toLogisticsCalculationRpcQry(Integer logisticsId,
        OrderDeliveryDTO delivery, List<OrderGoodsDO> orderGoodss, OrderDistributorCostDO orderDistributorCostDO) {
        LogisticsCalculationRpcQry rpcQry = new LogisticsCalculationRpcQry();
        rpcQry.setLogisticsId(logisticsId);
        BeanUtils.copyProperties(delivery, rpcQry);
        AtomicReference<Double> weight = new AtomicReference<>(0.00);
        orderGoodss.forEach(orderGoods -> {
            if (orderGoods.getWeight() == null) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_WEIGHT_NULL,
                    "\"" + orderGoods.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_WEIGHT_NULL));
            }
            weight.set(weight.get() + orderGoods.getWeight().doubleValue() * orderGoods.getItemCount());
        });
        rpcQry.setWeight(weight.get());
        // 按实际金额计算
        LOGGER.info("orderDistributorCostDO.getGoodsAmount():{}", orderDistributorCostDO.getGoodsAmount());
        LOGGER.info("orderDistributorCostDO.getGoodsPromotionAmount():{}",
            orderDistributorCostDO.getGoodsPromotionAmount());
        LOGGER.info("orderDistributorCostDO.getOrderPromotionAmount():{}",
            orderDistributorCostDO.getOrderPromotionAmount());
        BigDecimal actualAmount =
            orderDistributorCostDO.getGoodsAmount().subtract(orderDistributorCostDO.getGoodsPromotionAmount())
                .subtract(orderDistributorCostDO.getOrderPromotionAmount());
        rpcQry.setPrice(actualAmount);
        LOGGER.info("actualAmount:{}", actualAmount);
        return rpcQry;
    }

    public static OrderInfoDO toOrderInfoDO(DistributorRpcDTO distributor, OrderInfoDTO orderDTO, Date time,
        Short orderType, OrderTypeDO diyMtoOrderType) {
        OrderInfoDO order = new OrderInfoDO();
        order.setDeliverStatus(orderType);
        order.setSalesId(distributor.getSalesId());
        order.setSalesName(distributor.getSalesName());
        order.setCoordinatorId(distributor.getCoordinatorId());
        order.setCoordinatorName(distributor.getCoordinatorName());
        order.setOrganizationId(distributor.getOrganizationId());
        order.setOrganizationErp(distributor.getOrganizationErp());
        order.setOrganizationName(distributor.getOrganizationName());
        order.setInvoiceFlag(orderDTO.getInvoiceFlag());
        order.setOrderSource(orderDTO.getOrderSource());
        order.setOrderTypeId(distributor.getOrderTypeId());
        order.setStockType(orderType);
        order.setDeliverStatus(DeliverStatus.Undelivered.getValue());
        // 订单类型重新确定(主要针对预售订单和定制订单类型)
        if (orderType.equals(OrderType.DIY.getValue()) || orderType.equals(OrderType.MTO.getValue())) {
            order.setOrderTypeId(diyMtoOrderType.getId());
        }
        order.setCreateTime(time);
        order.setUpdateTime(time);
        return order;
    }

    /**
     * 订单商品信息
     * 
     * @param distributorId
     * @param goodss
     * @param goodsItemRpcDTOMap
     * @param itemStockMap
     * @param goodsItemPricesMap
     * @param priceRpcDTOMap
     * @param language
     * @param time
     * @param orderType
     * @param diyMtoOrderType
     * @return
     */
    public static List<OrderGoodsDO> toOrderGoodsDOList(Integer distributorId, List<OrderGoodsDTO> goodss,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap, Map<String, ItemStockLockDTO> itemStockMap,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap, String language, Date time, Short orderType,
        OrderTypeDO diyMtoOrderType) {
        List<OrderGoodsDO> orderGoodss = new ArrayList<>();
        LOGGER.info("转换订单明细,goodss{},itemStockMap{},priceRpcDTOMap{},goodsItemPricesMap{}", JSON.toJSONString(goodss),
            JSON.toJSONString(itemStockMap), JSON.toJSONString(priceRpcDTOMap), JSON.toJSONString(goodsItemPricesMap));
        // 递增的行序号、默认是0（可能order_goods会拆分成多条）
        // AtomicReference<Integer> serialNumberAdd = new AtomicReference<>(0);
        goodss.forEach(goods -> {
            GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
            OrderGoodsDO orderGoods = new OrderGoodsDO();
            BeanUtils.copyProperties(rpcDTO, orderGoods);
            // 初始化数量
            orderGoods.setItemCount(0);
            orderGoods.setItemInCount(0);
            orderGoods.setItemVmiCount(0);
            orderGoods.setItemOnWayCount(0);
            orderGoodss.add(orderGoods);
            orderGoods.setItemType(goods.getItemType());
            // 此序号用作获取促销活动价格（生成订单明细时，序号会重置）
            orderGoods.setSerialNumber(goods.getSerialNumber());
            // 库存数据
            List<OrderGoodsStockDO> orderGoodsStocks = new ArrayList<>();
            // 货品销售价格，如果货品销售价格有传值时，已传值为准
            if (goods.getSalePrice() != null) {
                orderGoods.setSalePrice(goods.getSalePrice());
            }
            orderGoods.setOrderGoodsStocks(orderGoodsStocks);
            if (StringUtils.isNotBlank(rpcDTO.getItemImg())) {
                orderGoods.setImageUrl(rpcDTO.getItemImg());
            } else {
                orderGoods.setImageUrl(rpcDTO.getImageUrl1());
            }
            if (StringUtils.isNotBlank(language) && language.equals(Constant.LANGUAGE_EN)) {
                if (StringUtils.isNotBlank(rpcDTO.getGoodsNameEn())) {
                    orderGoods.setGoodsName(rpcDTO.getGoodsNameEn());
                } else {
                    orderGoods.setGoodsName(rpcDTO.getGoodsName());
                }
                if (StringUtils.isNotBlank(rpcDTO.getItemNameEn())) {
                    orderGoods.setItemName(rpcDTO.getItemNameEn());
                } else {
                    orderGoods.setItemName(rpcDTO.getItemName());
                }
                if (StringUtils.isNotBlank(rpcDTO.getSpecsNameEn())) {
                    orderGoods.setSpecsName(rpcDTO.getSpecsNameEn());
                } else {
                    orderGoods.setSpecsName(rpcDTO.getSpecsName());
                }
                if (StringUtils.isNotBlank(rpcDTO.getColorNameEn())) {
                    orderGoods.setColorName(rpcDTO.getColorNameEn());
                } else {
                    orderGoods.setColorName(rpcDTO.getColorName());
                }
                if (StringUtils.isNotBlank(rpcDTO.getItemImg())) {
                    orderGoods.setImageUrl(rpcDTO.getItemImg());
                } else {
                    if (StringUtils.isNotBlank(rpcDTO.getImageUrl1en())) {
                        orderGoods.setImageUrl(rpcDTO.getImageUrl1en());
                    }
                }
            }
            orderGoods.setId(null);
            orderGoods.setItemId(rpcDTO.getId());
            orderGoods.setCreateTime(time);
            orderGoods.setUpdateTime(time);
            // 标品或非定制商品时，需判断库存锁库
            if (!orderType.equals(OrderType.DIY.getValue()) && !orderType.equals(OrderType.MTO.getValue())) {
                // 货品id和原来的行序号（不是order_goods的行序号）
                String key = orderGoods.getItemId() + "_" + goods.getSerialNumber();
                LOGGER.info("获取锁库key{}", key);
                ItemStockLockDTO stockLockDTO = itemStockMap.get(key);
                if (stockLockDTO == null) {
                    throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_STOCK_NULL,
                        "\"" + orderGoods.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_STOCK_NULL));
                }
                // 在库锁定
                List<WarehouseInStockLockDTO> stocks = stockLockDTO.getInStockLockDTOList();
                // vmi锁定
                VminStockLockDTO vmiLock = stockLockDTO.getVmiLock();
                // 在途锁定
                List<WarehouseInStockLockDTO> onWays = stockLockDTO.getOnWayLockDTOList();
                // OnWayStockLockDTO onWayLock = stockLockDTO.getOnWayLock();
                // 在库订单和在库在途订单
                if ((orderType.equals(OrderType.IN.getValue()) || orderType.equals(OrderType.IN_ON_WAY.getValue()))
                    && goods.getItemInCount() != null && goods.getItemInCount() > 0
                    && !CollectionUtils.isEmpty(stocks)) {
                    // 多仓库情况(拆分行项目)
                    if (stocks.size() > 1) {
                        stocks.forEach(stock -> {
                            if (stocks.indexOf(stock) == 0 && stock.getNumInWarehouseLock() != null
                                && stock.getNumInWarehouseLock() > 0) {
                                // 第一个仓库
                                orderGoods.setItemCount(stock.getNumInWarehouseLock());
                                orderGoods.setItemInCount(stock.getNumInWarehouseLock());
                                orderGoods.setWarehouseId(stock.getWarehouseId());
                                OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                                orderGoodsStock.setLockType(StockLockType.LOCK_IN.getValue());
                                orderGoodsStock.setLockNum(stock.getNumInWarehouseLock());
                                orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
                                orderGoodsStock.setItemId(orderGoods.getItemId());
                                orderGoodsStock.setStockId(stock.getStockId());
                                orderGoodsStock.setWarehouseId(stock.getWarehouseId());
                                orderGoodsStock.setCreateTime(time);
                                orderGoodsStock.setUpdateTime(time);
                                orderGoodsStocks.add(orderGoodsStock);
                            } else if (stock.getNumInWarehouseLock() != null && stock.getNumInWarehouseLock() > 0) {
                                // 非第一个仓库
                                OrderGoodsDO orderGoods1 = new OrderGoodsDO();
                                BeanUtils.copyProperties(orderGoods, orderGoods1);
                                // 定制信息重新组装新的（理论上不存在定制信息组装情况）
                                if (orderGoods.getOrderGoodsDiy() != null) {
                                    OrderGoodsDiyDO diyDO1 = new OrderGoodsDiyDO();
                                    BeanUtils.copyProperties(orderGoods.getOrderGoodsDiy(), diyDO1);
                                    orderGoods1.setOrderGoodsDiy(diyDO1);
                                }
                                orderGoods1.setItemCount(stock.getNumInWarehouseLock());
                                orderGoods1.setWarehouseId(stock.getWarehouseId());
                                orderGoods1.setItemInCount(stock.getNumInWarehouseLock());
                                orderGoods1.setUnDeliverCount(orderGoods1.getItemCount());
                                orderGoods1.setDeliverCount(0);
                                List<OrderGoodsStockDO> orderGoodsStocks1 = new ArrayList<>();
                                orderGoods1.setOrderGoodsStocks(orderGoodsStocks1);
                                OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                                orderGoodsStock.setLockType(StockLockType.LOCK_IN.getValue());
                                orderGoodsStock.setLockNum(stock.getNumInWarehouseLock());
                                orderGoodsStock.setGoodsId(orderGoods1.getGoodsId());
                                orderGoodsStock.setItemId(orderGoods1.getItemId());
                                orderGoodsStock.setStockId(stock.getStockId());
                                orderGoodsStock.setWarehouseId(stock.getWarehouseId());
                                orderGoodsStock.setCreateTime(time);
                                orderGoodsStock.setUpdateTime(time);
                                orderGoodsStocks1.add(orderGoodsStock);
                                orderGoodss.add(orderGoods1);
                            }
                        });
                    } else {
                        // 总数量
                        WarehouseInStockLockDTO stockLock = stocks.get(0);
                        orderGoods.setItemCount(stockLock.getNumInWarehouseLock());
                        orderGoods.setWarehouseId(stockLock.getWarehouseId());
                        if (stockLock.getNumInWarehouseLock() != null && stockLock.getNumInWarehouseLock() > 0) {
                            orderGoods.setItemInCount(stockLock.getNumInWarehouseLock());
                            OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                            orderGoodsStock.setLockType(StockLockType.LOCK_IN.getValue());
                            orderGoodsStock.setLockNum(stockLock.getNumInWarehouseLock());
                            orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
                            orderGoodsStock.setItemId(orderGoods.getItemId());
                            orderGoodsStock.setStockId(stockLock.getStockId());
                            orderGoodsStock.setWarehouseId(stockLock.getWarehouseId());
                            orderGoodsStock.setCreateTime(time);
                            orderGoodsStock.setUpdateTime(time);
                            orderGoodsStocks.add(orderGoodsStock);
                        }
                    }
                }
                if (goods.getItemInCount() != null && goods.getItemInCount() > 0 && vmiLock != null
                    && vmiLock.getLockNum() > 0) {
                    if (orderGoods.getItemCount() != null) {
                        orderGoods.setItemCount(orderGoods.getItemCount() + vmiLock.getLockNum());
                    } else {
                        orderGoods.setItemCount(vmiLock.getLockNum());
                    }
                    orderGoods.setItemVmiCount(vmiLock.getLockNum());
                    if (orderGoods.getWarehouseId() == null) {
                        orderGoods.setWarehouseId(vmiLock.getWarehouseId());
                    }
                    OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                    orderGoodsStock.setLockType(StockLockType.LOCK_VMI.getValue());
                    orderGoodsStock.setLockNum(vmiLock.getLockNum());
                    orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
                    orderGoodsStock.setItemId(orderGoods.getItemId());
                    orderGoodsStock.setStockId(vmiLock.getStockId());
                    orderGoodsStock.setWarehouseId(vmiLock.getWarehouseId());
                    orderGoodsStock.setCreateTime(time);
                    orderGoodsStock.setUpdateTime(time);
                    orderGoodsStocks.add(orderGoodsStock);
                }
                if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount() > 0
                    && !CollectionUtils.isEmpty(onWays)) {
                    if (onWays.size() > 1) {
                        onWays.forEach(onWay -> {
                            if (onWays.indexOf(onWay) == 0 && onWay.getNumOnWayLock() != null
                                && onWay.getNumOnWayLock() > 0) {
                                if (orderGoods.getItemCount() != null) {
                                    orderGoods.setItemCount(orderGoods.getItemCount() + onWay.getNumOnWayLock());
                                } else {
                                    orderGoods.setItemCount(onWay.getNumOnWayLock());
                                }
                                orderGoods.setItemOnWayCount(onWay.getNumOnWayLock());
                                OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                                orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                                orderGoodsStock.setLockNum(onWay.getNumOnWayLock());
                                orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
                                orderGoodsStock.setItemId(orderGoods.getItemId());
                                orderGoodsStock.setStockId(onWay.getStockId());
                                orderGoodsStock.setWarehouseId(onWay.getWarehouseId());
                                orderGoodsStock.setCreateTime(time);
                                orderGoodsStock.setUpdateTime(time);
                                orderGoodsStocks.add(orderGoodsStock);
                            } else if (onWay.getNumOnWayLock() != null && onWay.getNumOnWayLock() > 0) {
                                // 非第一个仓库
                                // 有可能在库已经添加过这个仓库的对象，先尝试取出
                                boolean isExist = true;
                                Integer warehouseId = onWay.getWarehouseId();
                                OrderGoodsDO orderGoods1 = orderGoodss.stream()
                                    .filter(orderGoodsDO -> orderGoodsDO.getWarehouseId().equals(warehouseId))
                                    .findFirst().orElse(null);
                                if (orderGoods1 == null) {
                                    isExist = false;
                                    orderGoods1 = new OrderGoodsDO();
                                    BeanUtils.copyProperties(orderGoods, orderGoods1);
                                    // 定制信息重新组装新的（理论上不存在定制信息组装情况）
                                    if (orderGoods.getOrderGoodsDiy() != null) {
                                        OrderGoodsDiyDO diyDO1 = new OrderGoodsDiyDO();
                                        BeanUtils.copyProperties(orderGoods.getOrderGoodsDiy(), diyDO1);
                                        orderGoods1.setOrderGoodsDiy(diyDO1);
                                    }
                                }
                                if (orderGoods1.getItemCount() != null) {
                                    orderGoods1.setItemCount(orderGoods1.getItemCount() + onWay.getNumOnWayLock());
                                } else {
                                    orderGoods1.setItemCount(onWay.getNumOnWayLock());
                                }
                                orderGoods1.setWarehouseId(onWay.getWarehouseId());
                                orderGoods1.setItemOnWayCount(onWay.getNumOnWayLock());
                                orderGoods1.setUnDeliverCount(orderGoods1.getItemCount());
                                orderGoods1.setDeliverCount(0);
                                List<OrderGoodsStockDO> orderGoodsStocks1 = new ArrayList<>();
                                OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                                if (isExist) {
                                    orderGoodsStocks1 = orderGoods1.getOrderGoodsStocks();
                                }
                                orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                                orderGoodsStock.setLockNum(onWay.getNumOnWayLock());
                                orderGoodsStock.setGoodsId(orderGoods1.getGoodsId());
                                orderGoodsStock.setItemId(orderGoods1.getItemId());
                                orderGoodsStock.setStockId(onWay.getStockId());
                                orderGoodsStock.setWarehouseId(onWay.getWarehouseId());
                                orderGoodsStock.setCreateTime(time);
                                orderGoodsStock.setUpdateTime(time);
                                orderGoodsStocks1.add(orderGoodsStock);
                                // 如果之前在库没有添加，现在添加
                                if (!isExist) {
                                    orderGoodss.add(orderGoods1);
                                }
                            }
                        });
                    } else {
                        // 总数量
                        WarehouseInStockLockDTO stockLock = onWays.get(0);
                        if (orderGoods.getItemCount() != null) {
                            orderGoods.setItemCount(orderGoods.getItemCount() + stockLock.getNumOnWayLock());
                        } else {
                            orderGoods.setItemCount(stockLock.getNumOnWayLock());
                        }
                        orderGoods.setWarehouseId(stockLock.getWarehouseId());
                        if (stockLock.getNumOnWayLock() != null && stockLock.getNumOnWayLock() > 0) {
                            orderGoods.setItemOnWayCount(stockLock.getNumOnWayLock());
                            OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                            orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                            orderGoodsStock.setLockNum(stockLock.getNumOnWayLock());
                            orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
                            orderGoodsStock.setItemId(orderGoods.getItemId());
                            orderGoodsStock.setStockId(stockLock.getStockId());
                            orderGoodsStock.setWarehouseId(stockLock.getWarehouseId());
                            orderGoodsStock.setCreateTime(time);
                            orderGoodsStock.setUpdateTime(time);
                            orderGoodsStocks.add(orderGoodsStock);
                        }
                    }
                }
                // if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount() > 0 &&
                // && onWays.getLockNum().intValue() > 0) {
                // if (orderGoods.getItemCount() != null) {
                // orderGoods
                // .setItemCount(orderGoods.getItemCount().intValue() + onWayLock.getLockNum().intValue());
                // } else {
                // orderGoods.setItemCount(onWayLock.getLockNum().intValue());
                // }
                // orderGoods.setItemOnWayCount(onWayLock.getLockNum().intValue());
                // if (orderGoods.getWarehouseId() == null) {
                // orderGoods.setWarehouseId(onWayLock.getWarehouseId());
                // }
                // OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                // orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                // orderGoodsStock.setLockNum(onWayLock.getLockNum().intValue());
                // orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
                // orderGoodsStock.setItemId(orderGoods.getItemId());
                // orderGoodsStock.setStockId(onWayLock.getStockId());
                // orderGoodsStock.setWarehouseId(onWayLock.getWarehouseId());
                // orderGoodsStock.setCreateTime(time);
                // orderGoodsStock.setUpdateTime(time);
                // orderGoodsStocks.add(orderGoodsStock);
                // }
            } else {
                orderGoods.setItemCount(goods.getItemCount());
                if (orderType.equals(OrderType.MTO.getValue())) {
                    orderGoods.setWarehouseId(diyMtoOrderType.getWarehouseId());
                }
                if (orderType.equals(OrderType.DIY.getValue())) {
                    orderGoods.setWarehouseId(diyMtoOrderType.getWarehouseId());
                    OrderGoodsDiyDTO diy = goods.getDiy();
                    if (diy != null) {
                        OrderGoodsDiyDO diyDO = new OrderGoodsDiyDO();
                        BeanUtils.copyProperties(diy, diyDO);
                        diyDO.setCreateTime(time);
                        diyDO.setUpdateTime(time);
                        orderGoods.setOrderGoodsDiy(diyDO);
                    }
                }
            }
            orderGoods.setUnDeliverCount(orderGoods.getItemCount());
            orderGoods.setDeliverCount(0);
        });
        LOGGER.info("根据库存拆分后的订单明细{}", JSON.toJSONString(orderGoodss));
        // 计算商品费用明细
        toOrderGoodsDistributorCostDO(distributorId, orderGoodss, goodsItemPricesMap, priceRpcDTOMap, time);
        return orderGoodss;
    }

    /**
     * 计算商品明细费用
     * 
     * @param distributorId
     * @param orderGoodss
     * @param goodsItemPricesMap
     * @param priceRpcDTOMap
     * @param time
     */
    public static void toOrderGoodsDistributorCostDO(Integer distributorId, List<OrderGoodsDO> orderGoodss,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap, Date time) {
        List<OrderGoodsDO> addOrderGoodss = new ArrayList<>();
        orderGoodss.forEach(goods -> {
            List<GoodsItemPromotionPriceRpcDTO> goodsItemPrices = goodsItemPricesMap.get(goods.getSerialNumber());
            GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(goods.getItemId());
            OrderGoodsDistributorCostDO costDO = new OrderGoodsDistributorCostDO();
            costDO.setDistributorId(distributorId);
            costDO.setItemType(goods.getItemType());
            if (priceRpcDTO != null) {
                costDO.setSalePrice(priceRpcDTO.getSalePrice());
            }
            costDO.setCreateTime(time);
            costDO.setUpdateTime(time);
            if (goods.getSalePrice() != null) {// 如果货品销售价有传值时，已传值为准
                costDO.setSalePrice(goods.getSalePrice());
                costDO.setActualPrice(goods.getSalePrice());
            } else if (goods.getItemType() != null && goods.getItemType().equals(Constant.ITEM_TYPE_2)) {// 赠品
                costDO.setActualPrice(new BigDecimal(0));
                if (!CollectionUtils.isEmpty(goodsItemPrices)) {
                    costDO.setOrderPromotionId(goodsItemPrices.get(0).getOrderPromotionId());
                    costDO.setGoodsPromotionId(goodsItemPrices.get(0).getGoodsPromotionId());
                    costDO.setSpellGroupId(goodsItemPrices.get(0).getSpellGroupId());
                }
            } else {
                // 活动价格时
                if (!CollectionUtils.isEmpty(goodsItemPrices)) {
                    goodsItemPrices.forEach(goodsItemPrice -> {
                        LOGGER.info("单个goodsItemPrice{}", JSON.toJSONString(goodsItemPrice));
                        // 注意：促销活动满减金额小数位有可能拆分行项目
                        if (goodsItemPrices.indexOf(goodsItemPrice) > 0) {
                            OrderGoodsDO addOrderGoods1 = new OrderGoodsDO();
                            BeanUtils.copyProperties(goods, addOrderGoods1);
                            // 定制信息重新组装新的
                            if (goods.getOrderGoodsDiy() != null) {
                                OrderGoodsDiyDO diyDO1 = new OrderGoodsDiyDO();
                                BeanUtils.copyProperties(goods.getOrderGoodsDiy(), diyDO1);
                                addOrderGoods1.setOrderGoodsDiy(diyDO1);
                            }
                            addOrderGoodss.add(addOrderGoods1);
                            goods.setItemCount(goods.getItemCount() - goodsItemPrice.getItemCount());
                            goods.setUnDeliverCount(goods.getItemCount());
                            goods.setDeliverCount(0);
                            addOrderGoods1.setItemCount(goodsItemPrice.getItemCount());
                            addOrderGoods1.setItemInCount(goodsItemPrice.getItemInCount());
                            addOrderGoods1.setItemVmiCount(goodsItemPrice.getItemVmiCount());
                            addOrderGoods1.setItemOnWayCount(goodsItemPrice.getItemOnWayCount());
                            addOrderGoods1.setUnDeliverCount(addOrderGoods1.getItemCount());
                            addOrderGoods1.setDeliverCount(0);
                            if (goods.getItemInCount() != null && goods.getItemInCount() > 0) {
                                goods.setItemInCount(goods.getItemInCount() - goodsItemPrice.getItemInCount());
                            } else if (goods.getItemVmiCount() != null && goods.getItemVmiCount() > 0) {
                                goods.setItemInCount(goods.getItemVmiCount() - goodsItemPrice.getItemVmiCount());
                            } else if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount() > 0) {
                                goods.setItemOnWayCount(goods.getItemOnWayCount() - goodsItemPrice.getItemOnWayCount());
                            }
                            // 计算锁库明细
                            if (!CollectionUtils.isEmpty(goods.getOrderGoodsStocks())) {
                                List<OrderGoodsStockDO> orderGoodsStocks1 = new ArrayList<>();
                                addOrderGoods1.setOrderGoodsStocks(orderGoodsStocks1);
                                OrderGoodsStockDO orderGoodsStock1 = new OrderGoodsStockDO();
                                orderGoodsStock1.setGoodsId(addOrderGoods1.getGoodsId());
                                orderGoodsStock1.setItemId(addOrderGoods1.getItemId());
                                orderGoodsStock1.setCreateTime(time);
                                orderGoodsStock1.setUpdateTime(time);
                                orderGoodsStocks1.add(orderGoodsStock1);
                                if (addOrderGoods1.getItemInCount() != null && addOrderGoods1.getItemInCount() > 0) {
                                    OrderGoodsStockDO stock = goods.getOrderGoodsStocks().stream()
                                        .filter(
                                            stockDO -> stockDO.getLockType().equals(StockLockType.LOCK_IN.getValue()))
                                        .findFirst().get();
                                    orderGoodsStock1.setLockType(StockLockType.LOCK_IN.getValue());
                                    orderGoodsStock1.setLockNum(goodsItemPrice.getItemCount());
                                    stock.setLockNum(stock.getLockNum() - goodsItemPrice.getItemCount());
                                    orderGoodsStock1.setStockId(stock.getStockId());
                                    orderGoodsStock1.setWarehouseId(stock.getWarehouseId());
                                } else if (addOrderGoods1.getItemVmiCount() != null
                                    && addOrderGoods1.getItemVmiCount() > 0) {
                                    OrderGoodsStockDO stock = goods.getOrderGoodsStocks().stream()
                                        .filter(
                                            stockDO -> stockDO.getLockType().equals(StockLockType.LOCK_VMI.getValue()))
                                        .findFirst().get();
                                    orderGoodsStock1.setLockType(StockLockType.LOCK_VMI.getValue());
                                    orderGoodsStock1.setLockNum(goodsItemPrice.getItemVmiCount());
                                    stock.setLockNum(stock.getLockNum() - goodsItemPrice.getItemVmiCount());
                                    orderGoodsStock1.setStockId(stock.getStockId());
                                    orderGoodsStock1.setWarehouseId(stock.getWarehouseId());
                                } else if (addOrderGoods1.getItemOnWayCount() != null
                                    && addOrderGoods1.getItemOnWayCount() > 0) {
                                    OrderGoodsStockDO stock = goods.getOrderGoodsStocks().stream().filter(
                                        stockDO -> stockDO.getLockType().equals(StockLockType.LOCK_ON_WAY.getValue()))
                                        .findFirst().orElse(null);
                                    orderGoodsStock1.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                                    orderGoodsStock1.setLockNum(goodsItemPrice.getItemOnWayCount());
                                    stock.setLockNum(stock.getLockNum() - goodsItemPrice.getItemOnWayCount());
                                    orderGoodsStock1.setStockId(stock.getStockId());
                                    orderGoodsStock1.setWarehouseId(stock.getWarehouseId());
                                }
                            }
                            // 费用计算 在途商品不参与活动，且只有在途数量情况(非赠品)
                            OrderGoodsDistributorCostDO addCostDO1 = new OrderGoodsDistributorCostDO();
                            BeanUtils.copyProperties(costDO, addCostDO1);
                            if (goodsItemPrice.getOnWayFlag().equals(Constant.ON_WAY_PROMOTION_FLAG_0)
                                && addOrderGoods1.getItemOnWayCount() != null && addOrderGoods1.getItemOnWayCount() > 0
                                && addOrderGoods1.getItemType().equals(Constant.ITEM_TYPE_1)) {
                                addCostDO1.setActualPrice(goodsItemPrice.getSalePrice());
                                addCostDO1.setOrderPromotionId(null);
                                addCostDO1.setGoodsPromotionId(null);
                                addCostDO1.setSpellGroupId(null);
                            } else {
                                if (goodsItemPrice.getActualPrice() != null
                                    && goodsItemPrice.getActualPrice().doubleValue() > 0) {
                                    addCostDO1.setActualPrice(goodsItemPrice.getActualPrice());
                                } else {
                                    addCostDO1.setActualPrice(new BigDecimal(0));
                                }
                                addCostDO1.setOrderPromotionId(goodsItemPrice.getOrderPromotionId());
                                addCostDO1.setGoodsPromotionId(goodsItemPrice.getGoodsPromotionId());
                                addCostDO1.setSpellGroupId(goodsItemPrice.getSpellGroupId());
                            }
                            addOrderGoods1.setOrderGoodsDistributorCost(addCostDO1);
                        } else {
                            // 如果在途不参与活动，且在途在库一起下单的情况，商品明细拆分（按在途在库明细拆分）
                            if (goodsItemPrice.getOnWayFlag() != null
                                && goodsItemPrice.getOnWayFlag().equals(Constant.ON_WAY_PROMOTION_FLAG_0)
                                && goodsItemPrice.getActualPrice() != null
                                && !goodsItemPrice.getActualPrice().equals(goodsItemPrice.getSalePrice())) {
                                if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount().intValue() > 0
                                    && goods.getItemCount().intValue() > goods.getItemOnWayCount().intValue()) {
                                    // 商品明细拆分（实际价格不一样，在库活动价，在途原价）
                                    // 在途
                                    OrderGoodsDO addOrderGoods = new OrderGoodsDO();
                                    BeanUtils.copyProperties(goods, addOrderGoods);
                                    addOrderGoodss.add(addOrderGoods);
                                    addOrderGoods.setItemCount(addOrderGoods.getItemOnWayCount());
                                    addOrderGoods.setUnDeliverCount(addOrderGoods.getItemCount());
                                    addOrderGoods.setDeliverCount(0);
                                    addOrderGoods.setItemInCount(0);
                                    addOrderGoods.setItemVmiCount(0);
                                    // 计算锁库明细
                                    if (!CollectionUtils.isEmpty(goods.getOrderGoodsStocks())) {
                                        OrderGoodsStockDO stock =
                                            goods.getOrderGoodsStocks().stream().filter(stockDO -> stockDO.getLockType()
                                                .equals(StockLockType.LOCK_ON_WAY.getValue())).findFirst().get();
                                        List<OrderGoodsStockDO> orderGoodsStocks = new ArrayList<>();
                                        addOrderGoods.setOrderGoodsStocks(orderGoodsStocks);
                                        OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                                        orderGoodsStock.setGoodsId(addOrderGoods.getGoodsId());
                                        orderGoodsStock.setItemId(addOrderGoods.getItemId());
                                        orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                                        orderGoodsStock.setLockNum(goodsItemPrice.getItemOnWayCount());
                                        goods.getOrderGoodsStocks().remove(stock);
                                        orderGoodsStock.setStockId(stock.getStockId());
                                        orderGoodsStock.setWarehouseId(stock.getWarehouseId());
                                        orderGoodsStock.setCreateTime(time);
                                        orderGoodsStock.setUpdateTime(time);
                                        orderGoodsStocks.add(orderGoodsStock);
                                    }
                                    // 明细费用重新算
                                    OrderGoodsDistributorCostDO addCostDO = new OrderGoodsDistributorCostDO();
                                    BeanUtils.copyProperties(costDO, addCostDO);
                                    addCostDO.setActualPrice(addCostDO.getSalePrice());
                                    addCostDO.setOrderPromotionId(null);
                                    addCostDO.setGoodsPromotionId(null);
                                    addCostDO.setSpellGroupId(null);
                                    addOrderGoods.setOrderGoodsDistributorCost(addCostDO);
                                    // 在库
                                    goods.setItemCount(goods.getItemCount() - goods.getItemOnWayCount());
                                    goods.setItemInCount(goods.getItemCount());
                                    goods.setUnDeliverCount(goods.getItemCount());
                                    goods.setDeliverCount(0);
                                    goods.setItemOnWayCount(0);
                                }
                            }
                            // 在途商品不参与活动，且只有在途数量情况(非赠品)
                            if (goodsItemPrice.getOnWayFlag() != null
                                && goodsItemPrice.getOnWayFlag().equals(Constant.ON_WAY_PROMOTION_FLAG_0)
                                && goods.getItemOnWayCount() != null && goods.getItemOnWayCount() > 0
                                && goods.getItemType().equals(Constant.ITEM_TYPE_1)) {
                                costDO.setActualPrice(goodsItemPrice.getSalePrice());
                                costDO.setOrderPromotionId(null);
                                costDO.setGoodsPromotionId(null);
                                costDO.setSpellGroupId(null);
                            } else {
                                if (goodsItemPrice.getActualPrice().doubleValue() > 0) {
                                    costDO.setActualPrice(goodsItemPrice.getActualPrice());
                                } else {
                                    costDO.setActualPrice(new BigDecimal(0));
                                }
                                costDO.setOrderPromotionId(goodsItemPrice.getOrderPromotionId());
                                costDO.setGoodsPromotionId(goodsItemPrice.getGoodsPromotionId());
                                costDO.setSpellGroupId(goodsItemPrice.getSpellGroupId());
                            }
                        }
                    });
                } else {
                    costDO.setActualPrice(priceRpcDTO.getSalePrice());
                }
            }
            goods.setOrderGoodsDistributorCost(costDO);
        });
        if (!CollectionUtils.isEmpty(addOrderGoodss)) {
            orderGoodss.addAll(addOrderGoodss);
        }
    }

    /**
     * 计算订单金额 toOrderDistributorCostDO
     * 
     * @param distributorId
     * @param orderGoodss
     * @param time
     * @return
     */
    public static OrderDistributorCostDO toOrderDistributorCostDO(Integer distributorId, List<OrderGoodsDO> orderGoodss,
        Date time) {
        LOGGER.info("计算订单金额 toOrderDistributorCostDO==============================================");
        LOGGER.info("params distributorId:{},time:{},orderGoodss:{}", distributorId, time,
            JSON.toJSONString(orderGoodss));
        OrderDistributorCostDO distributorCostDO = new OrderDistributorCostDO();
        distributorCostDO.setDistributorId(distributorId);
        // 商品总价
        AtomicReference<BigDecimal> goodsAmount = new AtomicReference<>(new BigDecimal(0));
        // 商品促销金额
        AtomicReference<BigDecimal> goodsPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        // 订单促销金额
        AtomicReference<BigDecimal> orderPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        orderGoodss.forEach(orderGoods -> {
            OrderGoodsDistributorCostDO orderGoodsCost = orderGoods.getOrderGoodsDistributorCost();
            // 单个商品销售价总和
            BigDecimal goodsSaleAmount =
                orderGoodsCost.getSalePrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            LOGGER.info("货品编码：{} 销售价：{} * 数量：{} = 单个商品销售价总和：{}", orderGoods.getItemCode(),
                orderGoodsCost.getSalePrice(), orderGoods.getItemCount(), goodsSaleAmount);
            // 所有商品销售价和
            goodsAmount.set(goodsAmount.get().add(goodsSaleAmount));
            // 单个商品实际结算价总和
            BigDecimal goodsActualAmount =
                orderGoodsCost.getActualPrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            LOGGER.info("货品编码：{} 实际结算价：{} * 数量：{} = 单个商品实际结算价总和：{}", orderGoods.getItemCode(),
                orderGoodsCost.getActualPrice(), orderGoods.getItemCount(), goodsActualAmount);
            if (orderGoodsCost.getGoodsPromotionId() != null || orderGoodsCost.getSpellGroupId() != null) {
                // 促销金额 = 销售价 - 实际结算价
                goodsPromotionAmount.set(goodsPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount))
                    .setScale(2, RoundingMode.HALF_UP));
            } else if (orderGoodsCost.getOrderPromotionId() != null) {
                orderPromotionAmount.set(orderPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount))
                    .setScale(2, RoundingMode.HALF_UP));
            }
        });
        distributorCostDO.setGoodsAmount(goodsAmount.get());
        distributorCostDO.setGoodsPromotionAmount(goodsPromotionAmount.get());
        distributorCostDO.setOrderPromotionAmount(orderPromotionAmount.get());
        distributorCostDO.setCreateTime(time);
        distributorCostDO.setUpdateTime(time);
        return distributorCostDO;
    }

    public static OrderDeliveryDO toOrderDeliveryDO(OrderInfoDTO orderDTO, OrderLogisticsDTO logistics, Date time) {
        OrderDeliveryDTO deliveryDTO = orderDTO.getDelivery();
        if (deliveryDTO != null) {
            OrderDeliveryDO delivery = new OrderDeliveryDO();
            BeanUtils.copyProperties(deliveryDTO, delivery);
            delivery.setDistributionId(logistics.getLogisticsId());
            delivery.setDistributionName(logistics.getLogisticsName());
            delivery.setCreateTime(time);
            delivery.setUpdateTime(time);
            return delivery;
        }
        return null;
    }

    public static OrderInvoiceDO toOrderInvoiceDO(OrderInfoDTO orderDTO, Date time) {
        OrderInvoiceDTO invoiceDTO = orderDTO.getInvoice();
        if (invoiceDTO != null) {
            OrderInvoiceDO invoiceDO = new OrderInvoiceDO();
            BeanUtils.copyProperties(invoiceDTO, invoiceDO);
            invoiceDO.setCreateTime(time);
            invoiceDO.setUpdateTime(time);
            return invoiceDO;
        }
        return null;
    }

    public static OrderDistributorDataDO toOrderDistributorDataDO(DistributorRpcDTO distributor, String contactId,
        String contactName, OrderInfoDTO orderDTO, Short orderType, BigDecimal payAmount, Date time) {
        OrderDistributorDataDO orderDistributorData = new OrderDistributorDataDO();
        orderDistributorData.setDistributorId(distributor.getId());
        orderDistributorData.setDistributorName(distributor.getName());
        orderDistributorData.setDistributionMode(distributor.getAncestorDistributionMode());
        orderDistributorData.setDirectFlag(Constant.DIRECT_FLAG_1);
        orderDistributorData.setErpFlag(distributor.getErpFlag());
        if (StringUtils.isNotBlank(contactId)) {
            orderDistributorData.setDistributorContactId(Integer.valueOf(contactId));
            orderDistributorData.setDistributorContactName(contactName);
        }
        orderDistributorData.setDistributorAncestorId(distributor.getDistributorAncestorId());
        orderDistributorData.setDistributorAncestorName(distributor.getDistributorAncestorName());
        orderDistributorData.setDistributorAncestorCompanyName(distributor.getDistributorAncestorCompanyName());
        orderDistributorData.setCompanyName(distributor.getCompanyName());
        orderDistributorData.setTreeNode(distributor.getTreeNode());
        orderDistributorData.setRemark(orderDTO.getRemark());
        orderDistributorData.setCurrencyType(orderDTO.getCurrencyType());
        orderDistributorData.setPayStatus(PayStatus.UNPAID.getValue());
        orderDistributorData.setPayWay(orderDTO.getPayWay());
        orderDistributorData.setOrderStatus(OrderStatus.PENDING.getValue());
        // 多级分销情况需经过上级分销商确认(支持自动审核确认)订单
        if (distributor.getTreeNode() > 1 && distributor.getAncestorDistributionAutoFlag().equals(Constant.AUTO_FLAG_1)
            && distributor.getErpFlag().equals(Constant.ERP_FLAG_0)
            && (orderDTO.getPayWay().equals(PayWay.Period_settlement.getValue())
                || orderDTO.getPayWay().equals(PayWay.Offline_payment.getValue()))) {
            // 多级、自动审核且不同步erp
            orderDistributorData.setOrderStatus(OrderStatus.CONFIRMED.getValue());
        } else if (distributor.getTreeNode() == 1 || (distributor.getAncestorDistributionAutoFlag().equals(Constant.AUTO_FLAG_1)
            && distributor.getErpFlag().equals(Constant.ERP_FLAG_1))) {
            // 订单应付金额为0自动确认
            if (payAmount.doubleValue() == 0) {
                orderDistributorData.setOrderStatus(OrderStatus.CONFIRMED.getValue());
                orderDistributorData.setPayStatus(PayStatus.PAID.getValue());
            } else {
                // 一级或多级、自动审核且同步erp
                // 期间结算：在库订单、在途订单和定制订单为已确认、MTO订单为待确认
                // 现款线上支付（支付宝、微信、快钱、余额支付）订单：未支付情况统一为待确认
                // 现款线下订单：在库订单和在途订单为已确认。定制订单和MTO订单为待确认
                if (distributor.getErpFlag().equals(Constant.ERP_FLAG_1)) {
                    if ((orderType.equals(OrderType.IN.getValue()) || orderType.equals(OrderType.IN_ON_WAY.getValue())
                        || orderType.equals(OrderType.ON_WAY.getValue()))
                        && (orderDTO.getPayWay().equals(PayWay.Period_settlement.getValue())
                            || orderDTO.getPayWay().equals(PayWay.Offline_payment.getValue()))) {
                        orderDistributorData.setOrderStatus(OrderStatus.CONFIRMED.getValue());
                    } else if (orderType.equals(OrderType.DIY.getValue())
                        && orderDTO.getPayWay().equals(PayWay.Period_settlement.getValue())) {
                        orderDistributorData.setOrderStatus(OrderStatus.CONFIRMED.getValue());
                    }
                }
            }
        }
        orderDistributorData.setCreateTime(time);
        orderDistributorData.setUpdateTime(time);
        return orderDistributorData;
    }

    public static OrderExtendDataDO toOrderExtendDataDO(DistributorRpcDTO distributor, OrderInfoDTO orderDTO,
        Date time) {
        if (distributor.getErpFlag().equals(Constant.ERP_FLAG_1) || StringUtils.isNotBlank(orderDTO.getOrderThirdpartyNo())) {
            OrderExtendDataDO extendDataDO = new OrderExtendDataDO();
            extendDataDO.setAutoDelivery(distributor.getAutoDelivery());
            extendDataDO.setOrderThirdpartyNo(orderDTO.getOrderThirdpartyNo());
            extendDataDO.setCreateTime(time);
            extendDataDO.setUpdateTime(time);
            extendDataDO.setOpenId(orderDTO.getOpenId());
            return extendDataDO;
        }
        return null;
    }

    public static OrderDistributorDataDTO toOrderDistributorDataDTO(OrderDistributorDataDO orderDistributorDataDO) {
        if (orderDistributorDataDO != null) {
            OrderDistributorDataDTO dto = new OrderDistributorDataDTO();
            org.springframework.beans.BeanUtils.copyProperties(orderDistributorDataDO, dto);
            return dto;
        }
        return null;
    }

    public static OrderDistributorCostDTO toOrderDistributorCostDTO(OrderDistributorCostDO orderDistributorCostDO) {
        if (orderDistributorCostDO != null) {
            OrderDistributorCostDTO dto = new OrderDistributorCostDTO();
            org.springframework.beans.BeanUtils.copyProperties(orderDistributorCostDO, dto);
            return dto;
        }
        return null;
    }

    public static List<OrderTreeNodeDataDTO> toOrderTreeNodeDataDTOList(OrderCheckCmd cmd,
        List<OrderDistributorDataDO> distributorDataDOS) {
        List<OrderTreeNodeDataDTO> treeNodeDataDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorDataDOS)) {
            distributorDataDOS.forEach(distributorDataDO -> {
                distributorDataDO.setOrderStatus(cmd.getOrderStatus());
                OrderTreeNodeDataDTO treeNodeDataDTO = new OrderTreeNodeDataDTO();
                treeNodeDataDTO.setOrderId(distributorDataDO.getOrderId());
                treeNodeDataDTO.setCounterpartyType(Constant.COUNTERPARTY_TYPE_1);
                treeNodeDataDTO.setDistributorId(distributorDataDO.getDistributorId());
                treeNodeDataDTOS.add(treeNodeDataDTO);
            });
        }
        return treeNodeDataDTOS;
    }

    public static void main(String[] args) {
        Map<Integer, Integer> map = null;
        System.out.println(JSON.toJSONString(map));
    }
}
