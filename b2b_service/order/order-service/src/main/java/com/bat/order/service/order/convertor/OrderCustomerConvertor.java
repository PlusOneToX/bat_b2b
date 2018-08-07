package com.bat.order.service.order.convertor;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.order.dao.order.dataobject.*;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.data.dto.*;
import com.bat.order.service.common.enumtype.*;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeOrderDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeSimpleDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.warehouse.stock.dto.*;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.order.dto.common.OrderInvoiceCmd;
import com.bat.order.api.order.dto.customer.*;
import com.bat.order.api.order.dto.distributor.OrderLogisticsCmd;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.service.common.data.dto.*;
import com.bat.order.service.common.enumtype.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/19 12:48
 */
public class OrderCustomerConvertor {

    public static OrderInfoDTO toOrderDTO(OrderInfoCustomerCmd cmd, String platform) {
        OrderInfoDTO dto = new OrderInfoDTO();
        BeanUtils.copyProperties(cmd, dto);
        dto.setOrderSource(platform);
        List<OrderGoodsDTO> goodsDTOS = new ArrayList<>();
        dto.setGoodss(goodsDTOS);
        List<OrderGoodsCustomerCmd> goodss = cmd.getGoodss();
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
        return dto;
    }

    public static OrderInfoDTO toOrderDTO(OrderInfoExchangeCmd cmd, String platform) {
        OrderInfoDTO dto = new OrderInfoDTO();
        BeanUtils.copyProperties(cmd, dto);
        dto.setOrderSource(platform);
        List<OrderGoodsDTO> goodsDTOS = new ArrayList<>();
        dto.setGoodss(goodsDTOS);
        List<OrderGoodsExchangeCmd> goodss = cmd.getGoodss();
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
        return dto;
    }

    public static GoodsItemPromotionPriceRpcQry toGoodsItemPromotionPriceRpcQry(Integer distributorId,
        Integer customerId, Date time, List<OrderGoodsDTO> goodss, List<DistributorCustomerPriceDTO> customerPriceDTOS,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap) {
        GoodsItemPromotionPriceRpcQry rpcQry = new GoodsItemPromotionPriceRpcQry();
        rpcQry.setUseFlag(Constant.USE_FLAG_1);
        Map<Integer, DistributorCustomerPriceDTO> priceRpcDTOMap = customerPriceDTOS.stream()
            .collect(Collectors.toMap(DistributorCustomerPriceDTO::getItemId, priceRpcDTO -> priceRpcDTO));
        rpcQry.setDistributorId(distributorId);
        rpcQry.setCustomerId(customerId);
        rpcQry.setOrderTime(time);
        List<com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry> goodsItemPrices = new ArrayList<>();
        rpcQry.setGoodsItemPrices(goodsItemPrices);
        goodss.forEach(goods -> {
            if (StringUtils.isNotBlank(goods.getCouponNo()) || goods.getGoodsPromotionId() != null
                || goods.getOrderPromotionId() != null || goods.getGroupSeckillId() != null) {
                GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
                DistributorCustomerPriceDTO priceRpcDTO = priceRpcDTOMap.get(rpcDTO.getId());
                if (priceRpcDTO == null || priceRpcDTO.getPrice() == null) {
                    throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_PRICE_ERROR,
                        "\"" + goods.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_PRICE_ERROR));
                }
                com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry priceRpcQry =
                    new com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry();
                BeanUtils.copyProperties(goods, priceRpcQry);
                OrderGoodsDiyDTO diy = goods.getDiy();
                if (diy != null) {
                    priceRpcQry.setMaterialId(diy.getMaterialId());
                    priceRpcQry.setModelId(diy.getModelId());
                }
                goodsItemPrices.add(priceRpcQry);
                priceRpcQry.setSalePrice(priceRpcDTO.getPrice());
            }
        });
        return rpcQry;
    }

    public static List<GoodsItemCountDTO> toGoodsItemCountDTOList(List<OrderGoodsDTO> goodss,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap) {
        Map<Integer, GoodsItemCountDTO> countDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(goodss)) {
            goodss.forEach(goods -> {
                GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
                if (rpcDTO.getGoodsType().equals(Constant.GOODS_TYPE_1) && goods.getMtoType().equals(Constant.MTO_TYPE_0)) {
                    GoodsItemCountDTO countDTO = countDTOMap.get(rpcDTO.getId());
                    if (countDTO == null) {
                        countDTO = new GoodsItemCountDTO();
                        countDTO.setItemId(rpcDTO.getId());
                        countDTO.setInStockCount(goods.getItemInCount());
                        countDTO.setOnWayCount(goods.getItemOnWayCount());
                    } else {
                        countDTO
                            .setInStockCount(countDTO.getInStockCount().intValue() + goods.getItemInCount().intValue());
                        countDTO
                            .setOnWayCount(countDTO.getOnWayCount().intValue() + goods.getItemOnWayCount().intValue());
                    }
                    if (goods.getOversoldFlag().equals(Constant.OVERSOLD_FLAG_1)) {
                        countDTO.setSupportOversoldFlag(true);
                    }
                }
            });
        }
        return countDTOMap.values().stream().collect(Collectors.toList());
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
        rpcQry.setPrice(orderDistributorCostDO.getGoodsAmount());
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
        if (orderType.equals(OrderType.DIY.getValue())) {
            order.setOrderTypeId(diyMtoOrderType.getId());
        } else if (orderType.equals(OrderType.MTO.getValue())) {
            order.setOrderTypeId(diyMtoOrderType.getId());
        }
        order.setCreateTime(time);
        order.setUpdateTime(time);
        return order;
    }

    public static List<OrderGoodsDO> toOrderGoodsDOList(Integer distributorId, Integer customerId,
                                                        List<OrderGoodsDTO> goodss, Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap,
                                                        Map<Integer, ItemStockLockDTO> itemStockMap,
                                                        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
                                                        Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap, Map<String, ExchangeCodeSimpleDTO> exchangeMap,
                                                        String language, Date time, Short orderType, OrderConfig orderConfig, String orderSource,
                                                        OrderTypeDO diyMtoOrderType) {
        List<OrderGoodsDO> orderGoodss = new ArrayList<>();
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
            orderGoods.setSerialNumber(goods.getSerialNumber());
            // 库存数据
            List<OrderGoodsStockDO> orderGoodsStocks = new ArrayList<>();
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
                ItemStockLockDTO stockLockDTO = itemStockMap.get(orderGoods.getItemId());
                if (stockLockDTO == null) {
                    throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_STOCK_NULL,
                        "\"" + orderGoods.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_STOCK_NULL));
                }
                // 在库锁定(以及在途锁定)
                List<WarehouseInStockLockDTO> stocks = stockLockDTO.getInStockLockDTOList();
                // vmi锁定
                VminStockLockDTO vmiLock = stockLockDTO.getVmiLock();
                // 在途锁定
                List<WarehouseInStockLockDTO> onWays = stockLockDTO.getOnWayLockDTOList();
                // 在库订单和在库在途订单
                if ((orderType.equals(OrderType.IN.getValue()) || orderType.equals(OrderType.IN_ON_WAY.getValue()))
                    && orderGoods.getItemInCount() != null && orderGoods.getItemInCount() > 0
                    && !CollectionUtils.isEmpty(stocks)) {
                    // 多仓库情况(拆分行项目)
                    if (stocks.size() > 1) {
                        stocks.forEach(stock -> {
                            if (stocks.indexOf(stock) == 0 && stock.getNumInWarehouseLock() != null
                                && stock.getNumInWarehouseLock() > 0) {
                                orderGoods.setItemCount(stock.getNumInWarehouseLock());
                                orderGoods.setItemInCount(stock.getNumInWarehouseLock());
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
                        onWays.forEach(stock -> {
                            if (stocks.indexOf(stock) == 0 && stock.getNumInWarehouseLock() != null
                                    && stock.getNumInWarehouseLock() > 0) {
                                orderGoods.setItemCount(stock.getNumInWarehouseLock());
                                orderGoods.setItemInCount(stock.getNumInWarehouseLock());
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
//                    if (orderGoods.getItemCount() != null) {
//                        orderGoods
//                            .setItemCount(orderGoods.getItemCount() + onWayLock.getLockNum().intValue());
//                    } else {
//                        orderGoods.setItemCount(onWayLock.getLockNum().intValue());
//                    }
//                    orderGoods.setItemOnWayCount(onWayLock.getLockNum().intValue());
//                    if (orderGoods.getWarehouseId() == null) {
//                        orderGoods.setWarehouseId(onWayLock.getWarehouseId());
//                    }
//                    OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
//                    orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
//                    orderGoodsStock.setLockNum(onWayLock.getLockNum().intValue());
//                    orderGoodsStock.setGoodsId(orderGoods.getGoodsId());
//                    orderGoodsStock.setItemId(orderGoods.getItemId());
//                    orderGoodsStock.setStockId(onWayLock.getStockId());
//                    orderGoodsStock.setWarehouseId(onWayLock.getWarehouseId());
//                    orderGoodsStock.setCreateTime(time);
//                    orderGoodsStock.setUpdateTime(time);
//                    orderGoodsStocks.add(orderGoodsStock);
//                }
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
        if (orderConfig.getPlatformCards().contains(orderSource)) {
            // 计算商品费用明细(兑换卡)
            toOrderGoodsCustomerCostDO(customerId, goodss, orderGoodss, customerPriceDTOMap, exchangeMap, time);
        } else if (orderConfig.getRongyaoexchange().equals(orderSource)) {
            // 计算商品费用明细(第三方兑换业务)
            toOrderGoodsCustomerCostDO(distributorId, customerId, goodss, orderGoodss, orderSource, time);
        } else {
            // 计算商品费用明细(非兑换卡)
            toOrderGoodsCustomerCostDO(customerId, orderGoodss, goodsItemPricesMap, customerPriceDTOMap, time);
        }
        return orderGoodss;
    }

    public static void toOrderGoodsCustomerCostDO(Integer customerId, List<OrderGoodsDO> orderGoodss,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap, Date time) {
        List<OrderGoodsDO> addOrderGoodss = new ArrayList<>();
        orderGoodss.forEach(goods -> {
            List<GoodsItemPromotionPriceRpcDTO> goodsItemPrices = goodsItemPricesMap.get(goods.getSerialNumber());
            DistributorCustomerPriceDTO customerPriceDTO = customerPriceDTOMap.get(goods.getItemId());
            OrderGoodsCustomerCostDO costDO = new OrderGoodsCustomerCostDO();
            costDO.setCustomerId(customerId);
            costDO.setItemType(goods.getItemType());
            costDO.setSalePrice(customerPriceDTO.getPrice());
            costDO.setCreateTime(time);
            costDO.setUpdateTime(time);
            if (goods.getItemType().equals(Constant.ITEM_TYPE_2)) {
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
                                goods.setItemInCount(goods.getItemCount() - goodsItemPrice.getItemCount());
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
                                        .findFirst().get();
                                    orderGoodsStock1.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                                    orderGoodsStock1.setLockNum(goodsItemPrice.getItemOnWayCount());
                                    stock.setLockNum(stock.getLockNum() - goodsItemPrice.getItemOnWayCount());
                                    orderGoodsStock1.setStockId(stock.getStockId());
                                    orderGoodsStock1.setWarehouseId(stock.getWarehouseId());
                                }
                            }
                            // 费用计算 在途商品不参与活动，且只有在途数量情况(非赠品)
                            OrderGoodsCustomerCostDO addCostDO1 = new OrderGoodsCustomerCostDO();
                            BeanUtils.copyProperties(costDO, addCostDO1);
                            if (goodsItemPrice.getOnWayFlag() != null
                                && goodsItemPrice.getOnWayFlag().equals(Constant.ON_WAY_PROMOTION_FLAG_0)
                                && addOrderGoods1.getItemOnWayCount() != null
                                && addOrderGoods1.getItemOnWayCount().intValue() > 0
                                && addOrderGoods1.getItemType().equals(Constant.ITEM_TYPE_1)) {
                                addCostDO1.setActualPrice(goodsItemPrice.getSalePrice());
                                addCostDO1.setOrderPromotionId(null);
                                addCostDO1.setGoodsPromotionId(null);
                                addCostDO1.setSpellGroupId(null);
                                addCostDO1.setCouponNo(null);
                                addCostDO1.setCouponMethod(null);
                                addCostDO1.setDeliveryFeeFlag(null);
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
                                addCostDO1.setCouponNo(goodsItemPrice.getCouponNo());
                                addCostDO1.setCouponMethod(goodsItemPrice.getCouponMethod());
                                addCostDO1.setDeliveryFeeFlag(goodsItemPrice.getDeliveryFeeFlag());
                            }
                            addOrderGoods1.setOrderGoodsCustomerCost(addCostDO1);
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
                                    OrderGoodsCustomerCostDO addCostDO = new OrderGoodsCustomerCostDO();
                                    BeanUtils.copyProperties(costDO, addCostDO);
                                    addCostDO.setActualPrice(addCostDO.getSalePrice());
                                    addCostDO.setOrderPromotionId(goodsItemPrice.getOrderPromotionId());
                                    addCostDO.setGoodsPromotionId(goodsItemPrice.getGoodsPromotionId());
                                    addCostDO.setSpellGroupId(goodsItemPrice.getSpellGroupId());
                                    addCostDO.setCouponNo(goodsItemPrice.getCouponNo());
                                    addCostDO.setCouponMethod(goodsItemPrice.getCouponMethod());
                                    addCostDO.setDeliveryFeeFlag(goodsItemPrice.getDeliveryFeeFlag());
                                    addOrderGoods.setOrderGoodsCustomerCost(addCostDO);
                                    // 在库
                                    goods.setItemCount(
                                        goods.getItemCount().intValue() - goods.getItemOnWayCount().intValue());
                                    goods.setItemInCount(goods.getItemCount());
                                    goods.setUnDeliverCount(goods.getItemCount());
                                    goods.setDeliverCount(0);
                                    goods.setItemOnWayCount(0);
                                }
                            }
                            // 在途商品不参与活动，且只有在途数量情况(非赠品)
                            if (goodsItemPrice != null && goodsItemPrice.getOnWayFlag() != null
                                && goodsItemPrice.getOnWayFlag().equals(Constant.ON_WAY_PROMOTION_FLAG_0)
                                && goods.getItemOnWayCount() != null && goods.getItemOnWayCount().intValue() > 0
                                && goods.getItemType().equals(Constant.ITEM_TYPE_1)) {
                                costDO.setActualPrice(goodsItemPrice.getSalePrice());
                                costDO.setOrderPromotionId(null);
                                costDO.setGoodsPromotionId(null);
                                costDO.setSpellGroupId(null);
                                costDO.setCouponNo(null);
                                costDO.setCouponMethod(null);
                                costDO.setDeliveryFeeFlag(null);
                            } else {
                                if (goodsItemPrice.getActualPrice().doubleValue() > 0) {
                                    costDO.setActualPrice(goodsItemPrice.getActualPrice());
                                } else {
                                    costDO.setActualPrice(new BigDecimal(0));
                                }
                                costDO.setOrderPromotionId(goodsItemPrice.getOrderPromotionId());
                                costDO.setGoodsPromotionId(goodsItemPrice.getGoodsPromotionId());
                                costDO.setSpellGroupId(goodsItemPrice.getSpellGroupId());
                                costDO.setCouponNo(goodsItemPrice.getCouponNo());
                                costDO.setCouponMethod(goodsItemPrice.getCouponMethod());
                                costDO.setDeliveryFeeFlag(goodsItemPrice.getDeliveryFeeFlag());
                            }
                        }
                    });
                } else {
                    costDO.setActualPrice(customerPriceDTO.getPrice());
                }
            }
            goods.setOrderGoodsCustomerCost(costDO);
        });
        if (!CollectionUtils.isEmpty(addOrderGoodss)) {
            orderGoodss.addAll(addOrderGoodss);
        }
    }

    public static void toOrderGoodsCustomerCostDO(Integer distributorId, Integer customerId, List<OrderGoodsDTO> goodss,
        List<OrderGoodsDO> orderGoodss, String orderSource, Date time) {
        Map<Integer, OrderGoodsDTO> goodsMap =
            goodss.stream().collect(Collectors.toMap(OrderGoodsDTO::getSerialNumber, goods -> goods));
        orderGoodss.forEach(orderGoods -> {
            OrderGoodsDTO orderGoodsDTO = goodsMap.get(orderGoods.getSerialNumber());
            OrderGoodsCustomerCostDO costDO = new OrderGoodsCustomerCostDO();
            orderGoods.setOrderGoodsCustomerCost(costDO);
            costDO.setCustomerId(customerId);
            costDO.setItemType(orderGoods.getItemType());
            List<OrderGoodsThirdCodeDO> thirdCodes = new ArrayList<>();
            orderGoods.setThirdCodes(thirdCodes);
            costDO.setCreateTime(time);
            costDO.setUpdateTime(time);
            List<String> codes = orderGoodsDTO.getCodes();
            // 可能存在运费价格不一样的情况，此时需要拆分行项目
            codes.forEach(code -> {
                costDO.setSalePrice(new BigDecimal(0));
                costDO.setActualPrice(new BigDecimal(0));
                thirdCodes.add(toOrderGoodsThirdCodeDO(code, distributorId, orderSource));
            });
        });
    }

    public static OrderGoodsThirdCodeDO toOrderGoodsThirdCodeDO(String code, Integer distributorId,
        String orderSource) {
        OrderGoodsThirdCodeDO thirdCodeDO = new OrderGoodsThirdCodeDO();
        thirdCodeDO.setCode(code);
        thirdCodeDO.setDistributorId(distributorId);
        thirdCodeDO.setWriteOffFlag(Constant.WRITE_OFF_FLAG_0);
        thirdCodeDO.setPlatform(orderSource);
        return thirdCodeDO;
    }

    public static void toOrderGoodsCustomerCostDO(Integer customerId, List<OrderGoodsDTO> goodss,
        List<OrderGoodsDO> orderGoodss, Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap,
        Map<String, ExchangeCodeSimpleDTO> exchangeMap, Date time) {
        Map<Integer, OrderGoodsDTO> goodsMap =
            goodss.stream().collect(Collectors.toMap(OrderGoodsDTO::getSerialNumber, goods -> goods));
        List<OrderGoodsDO> addAllOrderGoodss = new ArrayList<>();
        orderGoodss.forEach(orderGoods -> {
            List<OrderGoodsDO> addOrderGoodss = new ArrayList<>();
            OrderGoodsDTO orderGoodsDTO = goodsMap.get(orderGoods.getSerialNumber());
            OrderGoodsCustomerCostDO costDO = new OrderGoodsCustomerCostDO();
            orderGoods.setOrderGoodsCustomerCost(costDO);
            costDO.setCustomerId(customerId);
            costDO.setItemType(orderGoods.getItemType());
            List<OrderGoodsExchangeCodeDO> exchangeCodes = new ArrayList<>();
            orderGoods.setExchangeCodes(exchangeCodes);
            costDO.setCreateTime(time);
            costDO.setUpdateTime(time);
            List<String> secretCodes = orderGoodsDTO.getCodes();
            // 可能存在运费价格不一样的情况，此时需要拆分行项目
            secretCodes.forEach(secretCode -> {
                ExchangeCodeSimpleDTO codeSimpleDTO = exchangeMap.get(secretCode);
                // 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
                if (codeSimpleDTO.getMailSetting().equals(Constant.MAIL_SETTING_1)) {
                    costDO.setSalePrice(new BigDecimal(0));
                    costDO.setActualPrice(new BigDecimal(0));
                    exchangeCodes.add(toOrderGoodsExchangeCodeDO(codeSimpleDTO));
                } else if (codeSimpleDTO.getMailSetting().equals(Constant.MAIL_SETTING_2)) {
                    if (costDO.getActualPrice() != null
                        && costDO.getActualPrice().subtract(codeSimpleDTO.getMailFee()).doubleValue() > 0) {
                        Optional<OrderGoodsDO> first =
                            addOrderGoodss
                                .stream().filter(addOrderGoods -> addOrderGoods.getOrderGoodsCustomerCost()
                                    .getActualPrice().subtract(codeSimpleDTO.getMailFee()).doubleValue() == 0)
                                .findFirst();
                        if (first == null || first.isPresent()) {
                            OrderGoodsDO addOrderGoods = new OrderGoodsDO();
                            BeanUtils.copyProperties(first.get(), addOrderGoods);
                            List<OrderGoodsExchangeCodeDO> addExchangeCodes = new ArrayList<>();
                            addOrderGoods.setExchangeCodes(addExchangeCodes);
                            addExchangeCodes.add(toOrderGoodsExchangeCodeDO(codeSimpleDTO));
                            addOrderGoods.setItemCount(1);
                            addOrderGoods.setUnDeliverCount(addOrderGoods.getItemCount());
                            addOrderGoods.setDeliverCount(0);
                            addOrderGoods.getOrderGoodsCustomerCost().setActualPrice(codeSimpleDTO.getMailFee());
                            addOrderGoodss.add(addOrderGoods);
                        } else {
                            OrderGoodsDO addOrderGoods = first.get();
                            addOrderGoods.getExchangeCodes().add(toOrderGoodsExchangeCodeDO(codeSimpleDTO));
                            addOrderGoods.setItemCount(addOrderGoods.getItemCount().intValue() + 1);
                            addOrderGoods.setUnDeliverCount(addOrderGoods.getItemCount());
                            addOrderGoods.setDeliverCount(0);
                        }
                        orderGoods.setItemCount(orderGoods.getItemCount().intValue() - 1);
                        orderGoods.setUnDeliverCount(orderGoods.getItemCount());
                        orderGoods.setDeliverCount(0);
                    } else {
                        costDO.setSalePrice(codeSimpleDTO.getMailFee());
                        costDO.setActualPrice(codeSimpleDTO.getMailFee());
                        exchangeCodes.add(toOrderGoodsExchangeCodeDO(codeSimpleDTO));
                    }
                }
            });
            addAllOrderGoodss.addAll(addOrderGoodss);
        });
        if (!CollectionUtils.isEmpty(addAllOrderGoodss)) {
            orderGoodss.addAll(addAllOrderGoodss);
        }
    }

    private static OrderGoodsExchangeCodeDO toOrderGoodsExchangeCodeDO(ExchangeCodeSimpleDTO codeSimpleDTO) {
        OrderGoodsExchangeCodeDO exchangeCodeDO = new OrderGoodsExchangeCodeDO();
        BeanUtils.copyProperties(codeSimpleDTO, exchangeCodeDO);
        exchangeCodeDO.setDistributorId(codeSimpleDTO.getB2bDistributorId());
        return exchangeCodeDO;
    }

    public static OrderCustomerCostDO toOrderCustomerCostDO(Integer customerId, List<OrderGoodsDO> orderGoodss,
        Date time) {
        OrderCustomerCostDO customerCostDO = new OrderCustomerCostDO();
        customerCostDO.setCustomerId(customerId);
        // 商品总价
        AtomicReference<BigDecimal> goodsAmount = new AtomicReference<>(new BigDecimal(0));
        // 商品促销金额
        AtomicReference<BigDecimal> goodsPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        // 订单促销金额
        AtomicReference<BigDecimal> orderPromotionAmount = new AtomicReference<>(new BigDecimal(0));
        // 优惠券促销金额
        AtomicReference<BigDecimal> orderCouponAmount = new AtomicReference<>(new BigDecimal(0));
        orderGoodss.forEach(orderGoods -> {
            OrderGoodsCustomerCostDO orderGoodsCost = orderGoods.getOrderGoodsCustomerCost();
            BigDecimal goodsSaleAmount =
                orderGoodsCost.getSalePrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            goodsAmount.set(goodsAmount.get().add(goodsSaleAmount));
            BigDecimal goodsActualAmount =
                orderGoodsCost.getActualPrice().multiply(new BigDecimal(orderGoods.getItemCount()));
            if (orderGoodsCost.getGoodsPromotionId() != null || orderGoodsCost.getSpellGroupId() != null) {
                goodsPromotionAmount.set(goodsPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
            } else if (orderGoodsCost.getOrderPromotionId() != null) {
                orderPromotionAmount.set(orderPromotionAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (StringUtils.isNotBlank(orderGoodsCost.getCouponNo())) {
                orderCouponAmount.set(orderCouponAmount.get().add(goodsSaleAmount.subtract(goodsActualAmount))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        });
        customerCostDO.setGoodsAmount(goodsAmount.get());
        customerCostDO.setGoodsPromotionAmount(goodsPromotionAmount.get());
        customerCostDO.setOrderPromotionAmount(orderPromotionAmount.get());
        customerCostDO.setOrderCouponAmount(orderCouponAmount.get());
        customerCostDO.setCreateTime(time);
        customerCostDO.setUpdateTime(time);
        return customerCostDO;
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

    public static OrderCustomerDataDO toOrderCustomerDataDO(CustomerRpcDTO customer, DistributorRpcDTO distributor,
        OrderInfoDTO orderDTO, BigDecimal payAmount, Date time) {
        OrderCustomerDataDO orderCustomerDataDO = new OrderCustomerDataDO();
        orderCustomerDataDO.setCustomerId(customer.getId());
        if (StringUtils.isNotBlank(customer.getName())) {
            orderCustomerDataDO.setCustomerName(customer.getName());
        } else {
            orderCustomerDataDO.setCustomerName(customer.getNikeName());
        }
        orderCustomerDataDO.setDistributorId(distributor.getId());
        orderCustomerDataDO.setDistributorName(distributor.getName());
        orderCustomerDataDO.setCompanyName(distributor.getCompanyName());
        orderCustomerDataDO.setCustomerMode(distributor.getCustomerMode());
        orderCustomerDataDO.setRemark(orderDTO.getRemark());
        orderCustomerDataDO.setPayStatus(PayStatus.UNPAID.getValue());
        orderCustomerDataDO.setPayWay(orderDTO.getPayWay());
        if (payAmount.doubleValue() == 0) {
            orderCustomerDataDO.setOrderStatus(OrderStatus.CONFIRMED.getValue());
            orderCustomerDataDO.setPayStatus(PayStatus.PAID.getValue());
        } else {
            orderCustomerDataDO.setOrderStatus(OrderStatus.PENDING.getValue());
        }
        orderCustomerDataDO.setShopCode(orderDTO.getShopCode());
        orderCustomerDataDO.setShopName(orderDTO.getShopName());
        orderCustomerDataDO.setCreateTime(time);
        orderCustomerDataDO.setUpdateTime(time);
        return orderCustomerDataDO;
    }

    public static List<ExchangeCodeOrderDTO> toExchangeCodeOrderDTOList(List<OrderGoodsDTO> goodss) {
        List<ExchangeCodeOrderDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodss)) {
            goodss.forEach(goods -> {
                ExchangeCodeOrderDTO dto = new ExchangeCodeOrderDTO();
                BeanUtils.copyProperties(goods.getDiy(), dto);
                dto.setSecretCodeList(goods.getCodes());
                dtos.add(dto);
            });
        }
        return dtos;
    }
}
