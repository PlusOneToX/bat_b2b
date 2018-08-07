package com.bat.order.service.order.executor.customer;

import static com.bat.order.service.common.CommonErrorCode.B_COUPON_USE_FLAG_ERROR;
import static com.bat.order.service.common.CommonErrorCode.B_PROMOTION_CONDITION_NULL;
import static com.bat.order.tenant.TenantContext.getTenantNo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.data.dao.OrderCustomerDO;
import com.bat.order.service.common.data.dto.OrderGoodsDTO;
import com.bat.order.service.common.data.dto.OrderGoodsDiyDTO;
import com.bat.order.service.common.data.dto.OrderInfoDTO;
import com.bat.order.service.common.data.dto.OrderLogisticsDTO;
import com.bat.order.service.common.enumtype.OperateType;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.OrderType;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.utils.CommonUtil;
import com.bat.order.service.message.MessageSendService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.order.dto.customer.OrderInfoCustomerCmd;
import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.OrderGoodsCustomerCostMapper;
import com.bat.order.dao.cost.OrderInvoiceDOMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;
import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;
import com.bat.order.dao.stock.OrderGoodsStockDOMapper;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.mq.dto.OrderTreeNodeDataDTO;
import com.bat.order.service.order.convertor.OrderCustomerConvertor;
import com.bat.order.service.order.executor.OrderRpcExe;
import com.bat.order.service.order.executor.OrderTypeQryExe;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/23 9:45
 */
@Slf4j
@Component
public class UserCustomerOrderCmdExe {

    @Resource
    private CommonValidator commonValidator;
    @Resource
    OrderRpcExe orderRpcExe;
    @Resource
    private OrderConfig orderConfig;
    @Resource
    private OrderInfoDOMapper orderInfoDOMapper;
    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;
    @Resource
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostMapper;
    @Resource
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;
    @Resource
    private OrderGoodsStockDOMapper orderGoodsStockDOMapper;
    @Resource
    private OrderCustomerCostMapper orderCustomerCostMapper;
    @Resource
    private OrderDeliveryDOMapper orderDeliveryDOMapper;
    @Resource
    private OrderInvoiceDOMapper orderInvoiceDOMapper;
    @Resource
    private OrderCustomerDataMapper orderCustomerDataMapper;
    @Resource
    private MessageSendService sendService;

    @Resource
    private OrderTypeQryExe orderTypeQryExe;

    @Value("${sanxing.distributorId}")
    private Integer sanxingDistributorId;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCustomerOrderCmdExe.class);

    /**
     * C端客户下单接口(非兑换卡下单)
     * 
     * @param cmd
     * @param userId
     * @param distributorId
     * @param platform
     * @param language
     * @return
     */
    public BaseIds createOrder(OrderInfoCustomerCmd cmd, String userId, String userName, String distributorId,
        String platform, String language) {
        // 检查参数
        commonValidator.checkCustomerValidity(userId, distributorId, platform);
        OrderInfoDTO orderDTO = OrderCustomerConvertor.toOrderDTO(cmd, platform);
        return createOrder(orderDTO, Integer.valueOf(userId), userName, Integer.valueOf(distributorId), platform,
            language);
    }

    /**
     * C端客户下单接口
     * 
     * @param orderDTO
     * @param customerId
     * @param distributorId
     * @param platform
     * @param language
     * @param customerName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseIds createOrder(OrderInfoDTO orderDTO, Integer customerId, String customerName, Integer distributorId,
        String platform, String language) {
        Date time = new Date(System.currentTimeMillis());
        // 检查C端客户有效性
        CustomerRpcDTO customer = commonValidator.checkCustomerValidity(customerId, distributorId, platform);
        DistributorRpcDTO distributor = commonValidator.checkDistributorValidity(distributorId);
        if (distributor.getCustomerFlag() == null || distributor.getCustomerFlag().equals(Constant.CUSTOMER_FLAG_0)) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_DISTRIBUTOR_CUSTOMER_FLAG_ERROR);
        } // 检查发票
        commonValidator.checkInvoice(orderDTO);
        List<OrderGoodsDTO> goodss = orderDTO.getGoodss();
        // 检查和获取商品信息
        List<String> itemCodes = goodss.stream().map(OrderGoodsDTO::getItemCode).collect(Collectors.toList());
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = commonValidator.checkGoodsItems(itemCodes);
        List<Integer> itemIds =
            goodsItemRpcDTOS.stream().map(GoodsItemRpcDTO::getId).distinct().collect(Collectors.toList());
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap = goodsItemRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getItemCode, goodsItemRpcDTO -> goodsItemRpcDTO));
        // 计算库存(适配标品)
        List<ItemStockLockDTO> itemStockLockDTOS =
            orderRpcExe.summaryLockStock(customer.getDistributor(), goodss, goodsItemRpcDTOMap, null);
        Map<Integer, ItemStockLockDTO> itemStockMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(itemStockLockDTOS)) {
            itemStockMap.putAll(itemStockLockDTOS.stream()
                .collect(Collectors.toMap(ItemStockLockDTO::getItemId, itemStockLockDTO -> itemStockLockDTO)));
        }
        // 任何原因导致生成订单失败情况，已锁定的库存需反锁
        boolean error = false;
        boolean couponError = false;
        try {
            // 计算商品价格
            List<DistributorCustomerPriceDTO> customerPriceDTOS =
                orderRpcExe.listDistributorCustomerGoodsItemPrice(distributorId, itemIds);
            Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap = customerPriceDTOS.stream().collect(
                Collectors.toMap(DistributorCustomerPriceDTO::getItemId, customerPriceDTO -> customerPriceDTO));
            // 计算商品活动价格(最终价格)
            List<GoodsItemPromotionPriceRpcDTO> goodsItemPrices =
                orderRpcExe.goodsItemPromotionPriceCustomer(OrderCustomerConvertor.toGoodsItemPromotionPriceRpcQry(
                    distributorId, customerId, time, goodss, customerPriceDTOS, goodsItemRpcDTOMap));
            Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(goodsItemPrices)) {
                goodsItemPricesMap.putAll(goodsItemPrices.stream()
                    .collect(Collectors.groupingBy(GoodsItemPromotionPriceRpcDTO::getSerialNumber)));
            }
            // 组装订单数据（在途在库有可能拆单、定制订单和其他订单一定是拆单、mto订单和其他订单一定是拆单）
            List<OrderCustomerDO> orderCustomerDOList = getOrderCustomerDOList(customer, distributor, orderDTO,
                goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, customerPriceDTOMap, language, time);
            // 检查前端传的金额和后端计算金额是否一致
            if (orderDTO.getOrderAmount() != null) {
                commonValidator.checkCustomerOrderAmount(orderDTO, orderCustomerDOList);
            }
            // 订单数据保存
            List<Integer> orderIds = new ArrayList<>();
            orderCustomerDOList.forEach(orderCustomerDO -> {
                Integer orderId = saveCustomerOrder(orderCustomerDO);
                orderIds.add(orderId);
            });
            BaseIds ids = new BaseIds();
            ids.setIds(orderIds);
            // 发送订单处理消息
            sendOrderMessage(customerId, customerName, orderDTO.getOrderSource(), time, orderIds, orderCustomerDOList);
            return ids;
        } catch (OrderException e) {
            error = true;
            if(e.getCode().equals(B_COUPON_USE_FLAG_ERROR) ||
                    e.getCode().equals(B_PROMOTION_CONDITION_NULL)){
                couponError = true;
            }
            throw e;
        } catch (Exception e) {
            error = true;
            throw e;
        } finally {
            if (error && !CollectionUtils.isEmpty(itemStockLockDTOS)) {
                // 下单失败，库存反锁
                sendService.orderUnLockStock(itemStockLockDTOS, null);
            }
            // 下单失败，优惠券退回
            if(!couponError) {
                List<String> couponNos = goodss.stream().filter(goods -> StringUtils.isNotBlank(goods.getCouponNo()))
                        .map(OrderGoodsDTO::getCouponNo).distinct().collect(Collectors.toList());
                if (error && !CollectionUtils.isEmpty(couponNos)) {
                    sendService.updateCouponStatusForOrderFail(couponNos, Constant.COUPON_STATUS_2);
                }
            }
        }
    }

    /**
     * 发送订单处理消息
     */
    public void sendOrderMessage(Integer customerId, String customerName, String platform, Date time,
        List<Integer> orderIds, List<OrderCustomerDO> orderCustomerDOList) {
        try {
            orderCustomerDOList.forEach(orderCustomerDO -> {
                OrderCustomerDataDO orderCustomerData = orderCustomerDO.getOrderCustomerData();
                // 已确认的订单发生分销层级数据
                if (orderCustomerData.getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
                    OrderTreeNodeDataDTO treeNodeDataDTO = new OrderTreeNodeDataDTO();
                    treeNodeDataDTO.setCustomerId(customerId);
                    treeNodeDataDTO.setCounterpartyType(Constant.COUNTERPARTY_TYPE_2);
                    treeNodeDataDTO.setOrderId(orderCustomerData.getOrderId());
                    sendService.orderTreeNodeData(treeNodeDataDTO);
                } else {
                    sendService.orderTimerPowerOffC(orderCustomerData.getCustomerId(), orderCustomerData.getOrderId());
                }
                // 发送商品订单销售数量更新消息
                sendService.orderGoodsSale(orderCustomerDO.getOrderGoodss(), orderCustomerDO.getOrder().getId(),
                    Constant.CHANGE_TYPE_1);
            });
            // 下单成功，发送下单成功日志
            sendService.orderLog(orderIds, platform, customerId, customerName, "C端客户" + OperateType.Order.getName(),
                "下单成功", null, time);
        } catch (Exception e) {
            // TODO 预警功能
            log.info("C订单消息发送失败:{}", JSONObject.toJSONString(orderIds));
        }
    }

    /**
     * 生成的订单数据保存
     *
     * @param customerDO
     * @return
     */
    public Integer saveCustomerOrder(OrderCustomerDO customerDO) {
        // 订单基本信息保存
        OrderInfoDO order = customerDO.getOrder();
        saveOrderInfo(order);
        Integer orderId = order.getId();
        // 订单明细保存
        List<OrderGoodsDO> orderGoodss = customerDO.getOrderGoodss();
        orderGoodss.forEach(orderGoods -> {
            orderGoods.setOrderId(orderId);
            orderGoods.setSerialNumber(orderGoodss.indexOf(orderGoods) + 1);
        });
        orderGoodsDOMapper.insertList(orderGoodss);
        // 订单明细费用
        List<OrderGoodsCustomerCostDO> customerCostDOS = new ArrayList<>();
        // 订单明细定制信息
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = new ArrayList<>();
        // 订单明细锁库
        List<OrderGoodsStockDO> orderGoodsStockDOS = new ArrayList<>();
        orderGoodss.forEach(orderGoods -> {
            OrderGoodsCustomerCostDO customerCostDO = orderGoods.getOrderGoodsCustomerCost();
            customerCostDO.setOrderId(orderId);
            customerCostDO.setOrderGoodsId(orderGoods.getId());
            customerCostDOS.add(customerCostDO);
            OrderGoodsDiyDO orderGoodsDiy = orderGoods.getOrderGoodsDiy();
            if (orderGoodsDiy != null) {
                orderGoodsDiy.setOrderId(orderId);
                orderGoodsDiy.setOrderGoodsId(orderGoods.getId());
                orderGoodsDiyDOS.add(orderGoodsDiy);
            }
            List<OrderGoodsStockDO> orderGoodsStocks = orderGoods.getOrderGoodsStocks();
            if (!CollectionUtils.isEmpty(orderGoodsStocks)) {
                orderGoodsStocks.forEach(orderGoodsStock -> {
                    orderGoodsStock.setOrderId(orderId);
                    orderGoodsStock.setOrderGoodsId(orderGoods.getId());
                    orderGoodsStockDOS.add(orderGoodsStock);
                });
            }
        });
        if (!CollectionUtils.isEmpty(customerCostDOS)) {
            orderGoodsCustomerCostMapper.insertList(customerCostDOS);
        }
        if (!CollectionUtils.isEmpty(orderGoodsDiyDOS)) {
            orderGoodsDiyDOMapper.insertList(orderGoodsDiyDOS);
        }
        if (!CollectionUtils.isEmpty(orderGoodsStockDOS)) {
            orderGoodsStockDOMapper.insertList(orderGoodsStockDOS);
        }
        // 订单费用（C端客户）
        OrderCustomerCostDO orderCost = customerDO.getOrderCost();
        orderCost.setOrderId(orderId);
        orderCustomerCostMapper.insert(orderCost);
        // 订单收货信息
        OrderDeliveryDO delivery = customerDO.getDelivery();
        if (delivery != null) {
            delivery.setOrderId(orderId);
            orderDeliveryDOMapper.insert(delivery);
        }
        // 订单发票信息
        OrderInvoiceDO invoice = customerDO.getInvoice();
        if (invoice != null) {
            invoice.setOrderId(orderId);
            orderInvoiceDOMapper.insert(invoice);
        }
        // 订单分销商信息
        OrderCustomerDataDO orderCustomerData = customerDO.getOrderCustomerData();
        if (orderCustomerData != null) {
            orderCustomerData.setOrderId(orderId);
            orderCustomerDataMapper.insert(orderCustomerData);
        }
        // 处理订单同步操作
        dealFlagSendToSangxing(orderId);
        return orderId;
    }

    /**
     * 保存订单基本信息
     *
     * @return
     */
    private OrderInfoDO saveOrderInfo(OrderInfoDO order) {
        try {
            order.setOrderNo(CommonUtil.getOrderNo());
            orderInfoDOMapper.insert(order);
        } catch (DuplicateKeyException e) {
            saveOrderInfo(order);
        }
        return order;
    }

    /**
     * 生成订单数据（在途在库有可能拆单、定制订单和其他订单一定是拆单、mto订单和其他订单一定是拆单）
     * 
     * @param customer
     * @param distributor
     * @param orderDTO
     * @param goodsItemRpcDTOMap
     * @param itemStockMap
     * @param goodsItemPricesMap
     * @param customerPriceDTOMap
     * @param language
     * @param time
     * @return
     */
    private List<OrderCustomerDO> getOrderCustomerDOList(CustomerRpcDTO customer, DistributorRpcDTO distributor,
        OrderInfoDTO orderDTO, Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap,
        Map<Integer, ItemStockLockDTO> itemStockMap,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap, String language, Date time) {
        List<OrderGoodsDTO> goodss = orderDTO.getGoodss();
        List<OrderCustomerDO> orderDOS = new ArrayList<>();
        // 预售MTO订单明细
        List<OrderGoodsDTO> mtoGoodsList = new ArrayList<>();
        // 定制订单明细
        Map<String, List<OrderGoodsDTO>> diyGoodsListMap = new HashMap<>();
        // 在库订单明细
        List<OrderGoodsDTO> inGoodsList = new ArrayList<>();
        // 在途订单明细
        List<OrderGoodsDTO> onWayGoodsList = new ArrayList<>();
        // 在库在途订单明细
        List<OrderGoodsDTO> inOnWayGoodsList = new ArrayList<>();
        // 订单拆分
        goodss.forEach(goods -> {
            GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
            if (goods.getMtoType() != null && goods.getMtoType().equals(Constant.MTO_TYPE_1)) {
                mtoGoodsList.add(goods);
            } else if (rpcDTO.getGoodsType().equals(Constant.GOODS_TYPE_2)) {
                // 定制订单按生产工厂拆分订单
                OrderGoodsDiyDTO diy = goods.getDiy();
                List<OrderGoodsDTO> orderGoodsDTOS = diyGoodsListMap.get(diy.getManufactors());
                if (orderGoodsDTOS == null) {
                    orderGoodsDTOS = new ArrayList<>();
                    diyGoodsListMap.put(diy.getManufactors(), orderGoodsDTOS);
                }
                orderGoodsDTOS.add(goods);
            } else if (orderDTO.getOnWaySplitFlag() != null
                && orderDTO.getOnWaySplitFlag().equals(Constant.ON_WAY_SPLIT_FLAG_1)) {
                if (goods.getItemInCount() != null && goods.getItemInCount().intValue() > 0) {
                    inGoodsList.add(goods);
                }
                if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount().intValue() > 0) {
                    onWayGoodsList.add(goods);
                }
            } else if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount().intValue() > 0
                && goods.getItemInCount() != null && goods.getItemInCount().intValue() > 0) {
                inOnWayGoodsList.add(goods);
            } else if (goods.getItemOnWayCount() == null || goods.getItemOnWayCount().intValue() == 0) {
                inGoodsList.add(goods);
            } else if (goods.getItemInCount() == null || goods.getItemInCount().intValue() == 0) {
                onWayGoodsList.add(goods);
            }
        });
        // 在途在库拆单情况考虑
        if (orderDTO.getOnWaySplitFlag() != null && orderDTO.getOnWaySplitFlag().equals(Constant.ON_WAY_SPLIT_FLAG_0)
            && (!CollectionUtils.isEmpty(inOnWayGoodsList)
                || (!CollectionUtils.isEmpty(inGoodsList) && !CollectionUtils.isEmpty(onWayGoodsList)))) {
            inOnWayGoodsList.addAll(inGoodsList);
            inOnWayGoodsList.addAll(onWayGoodsList);
            inGoodsList.clear();
            onWayGoodsList.clear();
        }
        if (!CollectionUtils.isEmpty(mtoGoodsList)) {
            OrderCustomerDO mtoOrderDO =
                getOrderCustomerDO(customer, distributor, orderDTO, mtoGoodsList, goodsItemRpcDTOMap, itemStockMap,
                    goodsItemPricesMap, customerPriceDTOMap, language, time, OrderType.MTO.getValue(), null);
            orderDOS.add(mtoOrderDO);
        }
        if (!CollectionUtils.isEmpty(diyGoodsListMap)) {
            for (Map.Entry<String, List<OrderGoodsDTO>> entry : diyGoodsListMap.entrySet()) {
                String manufactors = entry.getKey();
                List<OrderGoodsDTO> diyGoodsList = entry.getValue();
                OrderCustomerDO diyOrderDO =
                    getOrderCustomerDO(customer, distributor, orderDTO, diyGoodsList, goodsItemRpcDTOMap, itemStockMap,
                        goodsItemPricesMap, customerPriceDTOMap, language, time, OrderType.DIY.getValue(), manufactors);
                orderDOS.add(diyOrderDO);
            }
        }
        if (!CollectionUtils.isEmpty(inGoodsList)) {
            OrderCustomerDO inOrderDO =
                getOrderCustomerDO(customer, distributor, orderDTO, inGoodsList, goodsItemRpcDTOMap, itemStockMap,
                    goodsItemPricesMap, customerPriceDTOMap, language, time, OrderType.IN.getValue(), null);
            orderDOS.add(inOrderDO);
        }
        if (!CollectionUtils.isEmpty(onWayGoodsList)) {
            OrderCustomerDO onWayOrderDO =
                getOrderCustomerDO(customer, distributor, orderDTO, onWayGoodsList, goodsItemRpcDTOMap, itemStockMap,
                    goodsItemPricesMap, customerPriceDTOMap, language, time, OrderType.ON_WAY.getValue(), null);
            orderDOS.add(onWayOrderDO);
        }
        if (!CollectionUtils.isEmpty(inOnWayGoodsList)) {
            OrderCustomerDO inOnWayOrderDO =
                getOrderCustomerDO(customer, distributor, orderDTO, inOnWayGoodsList, goodsItemRpcDTOMap, itemStockMap,
                    goodsItemPricesMap, customerPriceDTOMap, language, time, OrderType.IN_ON_WAY.getValue(), null);
            orderDOS.add(inOnWayOrderDO);
        }
        return orderDOS;
    }

    /**
     * 组装订单数据
     *
     * @param orderDTO
     * @param goodss
     * @param goodsItemRpcDTOMap
     * @param itemStockMap
     * @param language
     * @param time
     * @param orderType
     * @param manufactors
     *            定制订单生产工厂
     * @return
     */
    private OrderCustomerDO getOrderCustomerDO(CustomerRpcDTO customer, DistributorRpcDTO distributor,
        OrderInfoDTO orderDTO, List<OrderGoodsDTO> goodss, Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap,
        Map<Integer, ItemStockLockDTO> itemStockMap,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap, String language, Date time, Short orderType,
        String manufactors) {
        OrderCustomerDO orderDO = new OrderCustomerDO();
        // 组装订单基本信息
        OrderTypeDO diyMtoOrderType = null;
        if (orderType.equals(OrderType.DIY.getValue())) {
            List<OrderTypeDO> diyOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_4);
            if (CollectionUtils.isEmpty(diyOrderTypes)) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_TYPE_DIY_NULL);
            }
            diyMtoOrderType = diyOrderTypes.get(0);
        }
        if (orderType.equals(OrderType.MTO.getValue())) {
            List<OrderTypeDO> mtoOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_2);
            if (!CollectionUtils.isEmpty(mtoOrderTypes)) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_TYPE_MTO_NULL);
            }
            diyMtoOrderType = mtoOrderTypes.get(0);
        }
        OrderInfoDO order =
            OrderCustomerConvertor.toOrderInfoDO(distributor, orderDTO, time, orderType, diyMtoOrderType);
        orderDO.setOrder(order);
        // 组装订单明细、锁库、定制信息、明细费用
        List<OrderGoodsDO> orderGoodss = OrderCustomerConvertor.toOrderGoodsDOList(distributor.getId(),
            customer.getId(), goodss, goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, customerPriceDTOMap, null,
            language, time, orderType, orderConfig, orderDTO.getOrderSource(), diyMtoOrderType);
        orderDO.setOrderGoodss(orderGoodss);
        // 订单费用（C端客户）
        OrderCustomerCostDO orderCustomerCostDO =
            OrderCustomerConvertor.toOrderCustomerCostDO(customer.getId(), orderGoodss, time);
        orderDO.setOrderCost(orderCustomerCostDO);
        // 计算运费（C端客户暂时没有运费）
        orderCustomerCostDO.setDistributionAmount(new BigDecimal(0));
        Optional<OrderLogisticsDTO> optional = null;
        OrderLogisticsDTO logistics = null;
        if (orderType.equals(OrderType.DIY.getValue())) {
            optional = orderDTO.getLogisticss().stream()
                .filter(logisticsDTO -> logisticsDTO.getLogisticsType().equals(Constant.LOGISTICS_TYPE_2)
                    && StringUtils.isNotBlank(logisticsDTO.getManufactors())
                    && logisticsDTO.getManufactors().contains(manufactors))
                .findFirst();
        } else {
            optional = orderDTO.getLogisticss().stream()
                .filter(logisticsDTO -> logisticsDTO.getLogisticsType().equals(Constant.LOGISTICS_TYPE_1)).findFirst();
        }
        if (optional == null || !optional.isPresent()) {
            throw OrderException.buildException(OrderInfoErrorCode.P_ORDER_LOGISTICS_NULL);
        }
        logistics = optional.get();
        orderCustomerCostDO.setDistributionAmount(new BigDecimal(0));
        // 计算应付款金额
        BigDecimal actualAmount =
            orderCustomerCostDO.getGoodsAmount().subtract(orderCustomerCostDO.getGoodsPromotionAmount())
                .subtract(orderCustomerCostDO.getOrderPromotionAmount())
                .subtract(orderCustomerCostDO.getOrderCouponAmount());
        if (actualAmount.doubleValue() > 0) {
            orderCustomerCostDO.setPayAmount(actualAmount.add(orderCustomerCostDO.getDistributionAmount()));
        } else {
            orderCustomerCostDO.setPayAmount(orderCustomerCostDO.getDistributionAmount());
        }
        // 订单收货信息
        OrderDeliveryDO orderDeliveryDO = OrderCustomerConvertor.toOrderDeliveryDO(orderDTO, logistics, time);
        orderDO.setDelivery(orderDeliveryDO);
        // 订单发票信息
        OrderInvoiceDO invoiceDO = OrderCustomerConvertor.toOrderInvoiceDO(orderDTO, time);
        orderDO.setInvoice(invoiceDO);
        // 订单分销商信息
        OrderCustomerDataDO orderCustomerDataDO = OrderCustomerConvertor.toOrderCustomerDataDO(customer, distributor,
            orderDTO, orderCustomerCostDO.getPayAmount(), time);
        // TODO 获取汇率（C端客户暂不支持多币种）
        orderDO.setOrderCustomerData(orderCustomerDataDO);
        return orderDO;
    }

    public void dealFlagSendToSangxing(Integer orderId) {
        try {
            OrderCustomerDataDO orderCustomerDataDO = orderCustomerDataMapper.getByOrderId(orderId);
            if (orderCustomerDataDO != null
                && orderCustomerDataDO.getDistributorId().intValue() == sanxingDistributorId) {
                sendService.orderTobatNew(orderId,orderConfig.getOrderFlagbatLevel());
            }
        } catch (Exception e) {
            LOGGER.error("发送订单同步到消息失败:{}", e);
        }
    }

}
