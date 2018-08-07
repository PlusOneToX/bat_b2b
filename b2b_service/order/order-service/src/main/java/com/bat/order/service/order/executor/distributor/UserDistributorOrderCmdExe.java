package com.bat.order.service.order.executor.distributor;

import static java.math.RoundingMode.HALF_UP;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.order.service.common.CommonRpcQryExe;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.data.dao.OrderDistributorDO;
import com.bat.order.service.common.data.dto.OrderGoodsDTO;
import com.bat.order.service.common.data.dto.OrderGoodsDiyDTO;
import com.bat.order.service.common.data.dto.OrderInfoDTO;
import com.bat.order.service.common.data.dto.OrderLogisticsDTO;
import com.bat.order.service.common.enumtype.*;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.utils.CommonUtil;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.convertor.OrderConvertor;
import com.bat.order.service.order.convertor.OrderDistributorConvertor;
import com.bat.order.service.order.executor.OrderCmdExe;
import com.bat.order.service.stock.executor.OrderGoodsStockCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRateRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.promotion.dto.data.RebateVoucherRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.order.dto.distributor.OrderCheckCmd;
import com.bat.order.api.order.dto.distributor.OrderInfoCmd;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.OrderGoodsDistributorCostMapper;
import com.bat.order.dao.cost.OrderInvoiceDOMapper;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
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
import com.bat.order.service.common.enumtype.*;
import com.bat.order.service.order.executor.OrderRpcExe;
import com.bat.order.service.order.executor.OrderTypeQryExe;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 16:46
 */
@Slf4j
@Component
public class UserDistributorOrderCmdExe {

    @Resource
    CommonValidator commonValidator;
    @Resource
    OrderRpcExe orderRpcExe;
    @Resource
    private OrderInfoDOMapper orderInfoDOMapper;
    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;
    @Resource
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostDOMapper;
    @Resource
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;
    @Resource
    private OrderGoodsStockDOMapper orderGoodsStockDOMapper;
    @Resource
    private OrderDistributorCostMapper orderDistributorCostDOMapper;
    @Resource
    private OrderDeliveryDOMapper orderDeliveryDOMapper;
    @Resource
    private OrderInvoiceDOMapper orderInvoiceDOMapper;
    @Resource
    private OrderDistributorDataMapper orderDistributorDataDOMapper;
    @Resource
    private OrderExtendDataMapper orderExtendDataMapper;
    @Resource
    private MessageSendService sendService;
    @Resource
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;

    @Resource
    private OrderTypeQryExe orderTypeQryExe;

    // @Resource
    // private UserDistributorOrderQryExe userDistributorOrderQryExe;

    @Resource
    private HttpServletRequest request;
    @Resource
    private CommonRpcQryExe commonRpcQryExe;

    /**
     * 前台下单
     * 
     * @param cmd
     * @param userId
     * @return
     */
    public List<OrderInfoDO> createOrder(OrderInfoCmd cmd, String userId, String userName, String contactId,
        String contactName, String platform, String language) {
        commonValidator.checkDistributorAndPlatformValidity(userId, platform);
        OrderInfoDTO orderDTO = OrderDistributorConvertor.toOrderInfoDTO(cmd, platform);
        // 检查省市区 是否完整
        // commonValidator.checkRegion(orderDTO.getDelivery());
        return createOrder(orderDTO, Integer.valueOf(userId), userName, contactId, contactName, language);
    }

    /**
     * 通用下单方法（所有分销下单都通过此方法）
     * 
     * @param orderDTO
     *            下单时统一转换DTO数据
     * @param distributorId
     *            下单分销商
     * @param contactId
     *            下单分销商联系人id（前台分销商联系人登录情况有值）
     * @param contactName
     *            下单分销商联系人名称（前台分销商联系人登录情况有值）
     * @param language
     *            下单语言（“zh”为中文 “en”为英文，暂时只支持中英文两种语言）
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<OrderInfoDO> createOrder(OrderInfoDTO orderDTO, Integer distributorId, String distributorName,
        String contactId, String contactName, String language) {
        // 检查发票
        commonValidator.checkInvoice(orderDTO);
        List<OrderGoodsDTO> goodss = orderDTO.getGoodss();
        // 核查分销商有效性
        DistributorRpcDTO distributor = commonValidator.checkDistributorValidity(distributorId);
        // 检查和获取商品信息
        List<String> itemCodes = goodss.stream().map(OrderGoodsDTO::getItemCode).collect(Collectors.toList());
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = commonValidator.checkGoodsItems(itemCodes);

        // itemCode->goodsItem Map
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap = goodsItemRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getItemCode, goodsItemRpcDTO -> goodsItemRpcDTO));
        // 统一时间
        Date time = new Date(System.currentTimeMillis());
        // 计算商品价格
        List<GoodsItemPriceRpcDTO> priceRpcDTOS = orderRpcExe.listDistributorGoodsItemPrice(
            OrderDistributorConvertor.toGoodsItemPriceRpcQry(distributor.getId(), goodss, goodsItemRpcDTOMap));
        // ItemId->price Map
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = new HashMap<>();
        List<GoodsItemPromotionPriceRpcDTO> goodsItemPrices = new ArrayList<>();
        if (!CollectionUtils.isEmpty(priceRpcDTOS)) {
            priceRpcDTOMap.putAll(priceRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO)));
            // 计算商品活动价格(最终价格)
            goodsItemPrices = orderRpcExe.goodsItemPromotionPriceDistributor(OrderDistributorConvertor
                .toGoodsItemPromotionPriceRpcQry(distributor.getId(), time, goodss, priceRpcDTOS, goodsItemRpcDTOMap));
        }
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(goodsItemPrices)) {
            goodsItemPricesMap.putAll(goodsItemPrices.stream()
                .collect(Collectors.groupingBy(GoodsItemPromotionPriceRpcDTO::getSerialNumber)));
        }
        // 计算库存
        List<ItemStockLockDTO> itemStockLockDTOS =
            orderRpcExe.summaryLockStock(distributor, goodss, goodsItemRpcDTOMap, goodsItemPricesMap);
        // 锁库数量是按照货品合并进行锁库、但下单可能出现同一个货品多个明细、需要对锁库进行拆分（key是itemId_行序号）
        Map<String, ItemStockLockDTO> itemStockMap =
            OrderConvertor.toItemStockLockDTOMap(itemStockLockDTOS, goodss, goodsItemRpcDTOMap);
        // 任何原因导致生成订单失败情况，已锁定的库存需反锁
        boolean error = false;
        try {
            // 组装订单数据（在途在库有可能拆单、定制订单和其他订单一定是拆单、mto订单和其他订单一定是拆单）
            List<OrderDistributorDO> orderDistributorDOList =
                getOrderDistributorDOList(distributor, orderDTO, goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap,
                    priceRpcDTOMap, contactId, contactName, language, time);

            // 检查前端传的金额和后端计算金额是否一致（只有分销商前台下单情况需进行对比）
            if (orderDTO.getOrderAmount() != null) {
                commonValidator.checkDistributorOrderAmount(orderDTO, orderDistributorDOList);
            }

            // 订单数据保存
            List<Integer> orderIds = new ArrayList<>();
            List<OrderInfoDO> orders = new ArrayList<>();
            orderDistributorDOList.forEach(orderDistributorDO -> {
                OrderInfoDO order = saveDistributorOrder(orderDistributorDO, distributor.getDistributionPayWay());
                orderIds.add(order.getId());
                orders.add(order);
            });
            // 发送订单处理消息(如：生成分销层数据、同步erp订单、日志等)
            sendOrderMessage(distributor, contactId, contactName, orderDTO, time, orderIds, orderDistributorDOList);
            return orders;
        } catch (Exception e) {
            error = true;
            throw e;
        } finally {
            if (error && !CollectionUtils.isEmpty(itemStockLockDTOS)) {
                // 创建订单失败 解锁库存
                sendService.orderUnLockStock(itemStockLockDTOS, null);
            }
        }
    }

    /**
     * 发送订单处理消息
     * 
     * @param distributor
     * @param contactId
     * @param contactName
     * @param orderDTO
     * @param time
     * @param orderIds
     * @param orderDistributorDOList
     */
    private void sendOrderMessage(DistributorRpcDTO distributor, String contactId, String contactName,
        OrderInfoDTO orderDTO, Date time, List<Integer> orderIds, List<OrderDistributorDO> orderDistributorDOList) {
        String platform = orderDTO.getOrderSource();
        try {
            // 同步erp订单或分销层数据处理消息（一级分销订单无需生成分销层数据，直接处理同步erp订单）
            orderDistributorDOList.forEach(orderDistributorDO -> {
                OrderDistributorDataDO distributorData = orderDistributorDO.getOrderDistributorData();
                if (distributorData.getTreeNode() == 1) {
                    // 非现款订单发送同步erp消息队列,现款线上订单发送未支付定时关闭订单消息队列
                    if (distributorData.getPayWay().equals(PayWay.Period_settlement.getValue())
                        || distributorData.getPayWay().equals(PayWay.Offline_payment.getValue())
                        || distributorData.getPayStatus().equals(PayStatus.PAID.getValue())) {
                        // 如果是定制订单需发送标签生成消息
                        if (orderDistributorDO.getDiyOrderTypeDO() != null && orderDistributorDO.getOrder()
                            .getOrderTypeId().equals(orderDistributorDO.getDiyOrderTypeDO().getId())) {
                            List<OrderGoodsDiyDO> diyDOS =
                                orderDistributorDO.getOrderGoodss().stream().map(OrderGoodsDO::getOrderGoodsDiy)
                                    .filter(Objects::nonNull).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(diyDOS)) {
                                sendService.orderGoodsDiyLabel(distributorData.getDistributorId(),
                                    distributorData.getOrderId(), diyDOS);
                            }
                        }
                        // 发送erp同步消息
                        sendService.orderAsynErpNew(distributorData.getOrderId());
                    } else {
                        sendService.orderTimerPowerOffD(distributorData.getDistributorId(),
                            distributorData.getOrderId());
                    }
                    // 定制订单同步工厂消息(一级分销商订单才开始发送)
                    if (orderDistributorDO.getDiyOrderTypeDO() != null && orderDistributorDO.getOrder().getOrderTypeId()
                        .equals(orderDistributorDO.getDiyOrderTypeDO().getId())) {
                        // 获取定制订单同步工厂时间
                        Integer orderAsynFactoryTime =
                            orderRpcExe.getOrderAsynFactoryTime(distributorData.getDistributorId());
                        sendService.orderAsynFactoryNew(orderDistributorDO.getOrder().getId(), orderAsynFactoryTime);
                    }
                } else if (distributorData.getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
                    OrderTreeNodeDataDTO treeNodeDataDTO = new OrderTreeNodeDataDTO();
                    treeNodeDataDTO.setDistributorId(distributor.getId());
                    treeNodeDataDTO.setCounterpartyType(Constant.COUNTERPARTY_TYPE_1);
                    treeNodeDataDTO.setOrderId(distributorData.getOrderId());
                    /**
                     * 生成分销层数据
                     * 
                     * @see OrderCmdExe#createOrderTreeNodeData(OrderTreeNodeDataDTO)
                     */
                    sendService.orderTreeNodeData(treeNodeDataDTO);
                } else {
                    if (!PayWay.Offline_payment.getValue().equals(distributorData.getPayWay())) {
                        // 子级分销商线下转账不关闭
                        log.info("子级分销商待确认订单单（非银行转账）、放入定时器{}", distributorData.getOrderId());
                        sendService.orderTimerPowerOffD(distributorData.getDistributorId(),
                            distributorData.getOrderId());
                    }

                }
                // 发送商品订单销售数量更新消息
                sendService.orderGoodsSale(orderDistributorDO.getOrderGoodss(), orderDistributorDO.getOrder().getId(),
                    Constant.CHANGE_TYPE_1);
            });
            // 发送抵扣代金券
            sendService.deductionRebateVoucher(orderDistributorDOList, orderDTO);
            // 发送订单日志消息
            if (contactId != null) {
                sendService.orderLog(orderIds, platform, Integer.valueOf(contactId), contactName,
                    "分销商客户" + OperateType.Order.getName(), "下单成功", null, time);
            } else {
                sendService.orderLog(orderIds, platform, distributor.getId(), distributor.getName(),
                    "分销商客户" + OperateType.Order.getName(), "下单成功", null, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO 预警功能
            log.info("B端订单消息发送失败:{}", JSONObject.toJSONString(orderIds));
        }
    }

    /**
     * 生成的订单数据保存
     * 
     * @param distributorDO
     * @return
     */
    public OrderInfoDO saveDistributorOrder(OrderDistributorDO distributorDO, Short distributionPayWay) {
        // 订单基本信息保存
        OrderInfoDO order = distributorDO.getOrder();
        saveOrderInfo(order);
        Integer orderId = order.getId();
        // 订单明细保存
        List<OrderGoodsDO> orderGoodss = distributorDO.getOrderGoodss();
        orderGoodss.forEach(orderGoods -> {
            orderGoods.setOrderId(orderId);
            orderGoods.setSerialNumber(orderGoodss.indexOf(orderGoods) + 1);
        });
        orderGoodsDOMapper.insertList(orderGoodss);
        // 订单分销商信息
        OrderDistributorDataDO orderDistributorData = distributorDO.getOrderDistributorData();
        if (orderDistributorData != null) {
            orderDistributorData.setOrderId(orderId);

            // // 如果付款分销商设置的是自己收款，那么它与上级分销商生成的分层数据中的付款方式是当前分销商设置的付款方式
            // if (orderDistributorData.getDistributionMode().equals(DISTRIBUTION_MODE_3)
            // && ObjectUtils.isNotEmpty(distributionPayWay)) {
            // orderDistributorData.setPayWay(distributionPayWay);
            // }

            orderDistributorDataDOMapper.insert(orderDistributorData);
            sendService.newOrderMsg(orderId, orderDistributorData.getDistributorId());
        }
        // 订单明细费用
        List<OrderGoodsDistributorCostDO> distributorCostDOS = new ArrayList<>();
        // 订单明细定制信息
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = new ArrayList<>();
        // 订单明细锁库
        List<OrderGoodsStockDO> orderGoodsStockDOS = new ArrayList<>();
        orderGoodss.forEach(orderGoods -> {
            OrderGoodsDistributorCostDO distributorCostDO = orderGoods.getOrderGoodsDistributorCost();
            distributorCostDO.setOrderId(orderId);
            distributorCostDO.setOrderGoodsId(orderGoods.getId());
            // 如果分销结算方式是平台收款时，设置平台收款金额
            // if (orderDistributorData != null
            // && orderDistributorData.getDistributionMode().equals(DISTRIBUTION_MODE_1)) {
            // distributorCostDO.setPlatformPrice(distributorCostDO.getActualPrice());
            // }
            distributorCostDOS.add(distributorCostDO);
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
        if (!CollectionUtils.isEmpty(distributorCostDOS)) {
            orderGoodsDistributorCostDOMapper.insertList(distributorCostDOS);
        }
        if (!CollectionUtils.isEmpty(orderGoodsDiyDOS)) {
            orderGoodsDiyDOMapper.insertList(orderGoodsDiyDOS);
        }
        log.info("订单锁库明细{}", JSON.toJSONString(orderGoodsStockDOS));
        if (!CollectionUtils.isEmpty(orderGoodsStockDOS)) {
            orderGoodsStockDOMapper.insertList(orderGoodsStockDOS);
        }
        // 订单费用（分销商归属）
        OrderDistributorCostDO orderCost = distributorDO.getOrderCost();
        // 如果分销结算方式是平台收款时，设置平台收款金额
        // if (orderDistributorData != null && orderDistributorData.getDistributionMode().equals(DISTRIBUTION_MODE_1)) {
        // orderCost.setPlatformAmount(orderCost.getPayAmount());
        // }
        orderCost.setOrderId(orderId);
        orderDistributorCostDOMapper.insert(orderCost);
        // 订单收货信息
        OrderDeliveryDO delivery = distributorDO.getDelivery();
        if (delivery != null) {
            delivery.setOrderId(orderId);
            orderDeliveryDOMapper.insert(delivery);
        }
        // 订单发票信息
        OrderInvoiceDO invoice = distributorDO.getInvoice();
        if (invoice != null) {
            invoice.setOrderId(orderId);
            orderInvoiceDOMapper.insert(invoice);
        }
        // 订单扩展数据
        OrderExtendDataDO extendData = distributorDO.getExtendData();
        if (extendData != null) {
            extendData.setOrderId(orderId);
            orderExtendDataMapper.insert(extendData);
        }
        return order;
    }

    /**
     * 生成订单数据（在途在库有可能拆单、定制订单和其他订单一定是拆单、mto订单和其他订单一定是拆单）
     * 
     * @param distributor
     * @param orderDTO
     * @param goodsItemRpcDTOMap
     * @param itemStockMap
     * @param goodsItemPricesMap
     * @param contactId
     * @param contactName
     * @param language
     * @param time
     * @return
     */
    private List<OrderDistributorDO> getOrderDistributorDOList(DistributorRpcDTO distributor, OrderInfoDTO orderDTO,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap, Map<String, ItemStockLockDTO> itemStockMap,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap, String contactId, String contactName, String language,
        Date time) {
        List<OrderGoodsDTO> goodss = orderDTO.getGoodss();
        List<OrderDistributorDO> orderDOS = new ArrayList<>();
        // 预售MTO订单明细
        List<OrderGoodsDTO> mtoGoodsList = new ArrayList<>();
        // 定制订单明细 key 工厂
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
                List<OrderGoodsDTO> orderGoodsDTOS =
                    diyGoodsListMap.computeIfAbsent(diy.getManufactors(), k -> new ArrayList<>());
                orderGoodsDTOS.add(goods);
            } else if (orderDTO.getOnWaySplitFlag() != null
                && orderDTO.getOnWaySplitFlag().equals(Constant.ON_WAY_SPLIT_FLAG_1)) {
                // 拆单
                if (goods.getItemInCount() != null && goods.getItemInCount() > 0) {
                    OrderGoodsDTO goodsDTO = new OrderGoodsDTO();
                    BeanUtils.copyProperties(goods, goodsDTO);
                    goodsDTO.setItemCount(goodsDTO.getItemInCount());
                    goodsDTO.setItemOnWayCount(0);
                    inGoodsList.add(goodsDTO);
                }
                if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount() > 0) {
                    OrderGoodsDTO goodsDTO = new OrderGoodsDTO();
                    BeanUtils.copyProperties(goods, goodsDTO);
                    goodsDTO.setItemCount(goodsDTO.getItemOnWayCount());
                    goodsDTO.setItemInCount(0);
                    onWayGoodsList.add(goodsDTO);
                }
            } else if (goods.getItemOnWayCount() != null && goods.getItemOnWayCount() > 0
                && goods.getItemInCount() != null && goods.getItemInCount() > 0) {
                inOnWayGoodsList.add(goods);
            } else if (goods.getItemOnWayCount() == null || goods.getItemOnWayCount() == 0) {
                inGoodsList.add(goods);
            } else if (goods.getItemInCount() == null || goods.getItemInCount() == 0) {
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
            OrderDistributorDO mtoOrderDistributorDO = getOrderDistributorDO(distributor, orderDTO, mtoGoodsList,
                goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, priceRpcDTOMap, contactId, contactName, language,
                time, OrderType.MTO.getValue(), null);
            orderDOS.add(mtoOrderDistributorDO);
        }
        if (!CollectionUtils.isEmpty(diyGoodsListMap)) {
            for (Map.Entry<String, List<OrderGoodsDTO>> entry : diyGoodsListMap.entrySet()) {
                String manufactors = entry.getKey();
                List<OrderGoodsDTO> diyGoodsList = entry.getValue();
                OrderDistributorDO diyOrderDistributorDO = getOrderDistributorDO(distributor, orderDTO, diyGoodsList,
                    goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, priceRpcDTOMap, contactId, contactName,
                    language, time, OrderType.DIY.getValue(), manufactors);
                orderDOS.add(diyOrderDistributorDO);
            }
        }
        if (!CollectionUtils.isEmpty(inGoodsList)) {
            OrderDistributorDO inOrderDistributorDO = getOrderDistributorDO(distributor, orderDTO, inGoodsList,
                goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, priceRpcDTOMap, contactId, contactName, language,
                time, OrderType.IN.getValue(), null);
            orderDOS.add(inOrderDistributorDO);
        }
        if (!CollectionUtils.isEmpty(onWayGoodsList)) {
            OrderDistributorDO onWayOrderDistributorDO = getOrderDistributorDO(distributor, orderDTO, onWayGoodsList,
                goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, priceRpcDTOMap, contactId, contactName, language,
                time, OrderType.ON_WAY.getValue(), null);
            orderDOS.add(onWayOrderDistributorDO);
        }
        if (!CollectionUtils.isEmpty(inOnWayGoodsList)) {
            OrderDistributorDO inOnWayOrderDistributorDO = getOrderDistributorDO(distributor, orderDTO,
                inOnWayGoodsList, goodsItemRpcDTOMap, itemStockMap, goodsItemPricesMap, priceRpcDTOMap, contactId,
                contactName, language, time, OrderType.IN_ON_WAY.getValue(), null);
            orderDOS.add(inOnWayOrderDistributorDO);
        }
        // 计算返利金
        calcRebateVoucherAmount(distributor.getId(), orderDTO, orderDOS);
        return orderDOS;
    }

    /**
     * 计算返利代金券金额
     *
     * 这一块的逻辑 一次下单拆单成多个订单，每个订单按订单金额 按比例分配返利金
     *
     * 每个订单中 先抵商品金额 再抵物流费，如果
     *
     * 每个订单中 按商品金额（单价*数量）比例 分配订单内的返利金，重新设置实际单价
     * 
     * @param distributorId
     * @param orderDTO
     * @param orderDOS
     */
    private void calcRebateVoucherAmount(Integer distributorId, OrderInfoDTO orderDTO,
        List<OrderDistributorDO> orderDOS) {
        if (CollectionUtils.isEmpty(orderDTO.getRebateVoucherIds())) {
            return;
        }
        log.info("开始进行返利金的计算：distributorId：{},代金券id:{}", distributorId, orderDTO.getRebateVoucherIds());
        log.info("====================================校验======================================");
        log.info("返回的订单明细 orderDOS:{}", JSON.toJSONString(orderDOS));
        log.info("返回的订单明细 orderDTO:{}", JSON.toJSONString(orderDTO));
        // 查询 所属的代金券的 每个代金券
        List<RebateVoucherRpcDTO> vouchers =
            orderRpcExe.listRebateVoucher(distributorId, orderDTO.getRebateVoucherIds());
        // 代金券余额总额
        BigDecimal rebateVoucherAmountSum =
            vouchers.stream().map(RebateVoucherRpcDTO::getBalance).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        // 没钱可抵
        if (rebateVoucherAmountSum.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("无可用余额");
            return;
        }
        // 先选出可以使用代金券的 订单信息
        List<OrderDistributorDO> orderDistributorDOS = orderDOS.stream().filter(orderDistributorDO -> {
            OrderTypeDO diyOrderTypeDO = orderDistributorDO.getDiyOrderTypeDO();
            if (diyOrderTypeDO != null) {
                // 定制订单 并且不是PC前台下单 不能使用代金券（即柔性订单只有PC前台下单的订单才能抵）
                if (diyOrderTypeDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_4)) {
                    return Constant.GF60001.equals(orderDistributorDO.getOrder().getOrderSource());
                }
            }
            return true;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(orderDistributorDOS)) {
            log.error("无有效订单");
            return;
        }
        // 应付款总金额
        BigDecimal sumPayAmount = orderDistributorDOS.stream().map(OrderDistributorDO::getOrderCost)
            .map(OrderDistributorCostDO::getPayAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        if (sumPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("应付款总额小于等于0 不需要抵扣");
            return;
        }
        // 判断 代金券余额总额 与 应付款总金额 如果代金券余额总额超出 代金券抵扣总金额 = 应付款总金额
        if (rebateVoucherAmountSum.compareTo(sumPayAmount) > 0) {
            rebateVoucherAmountSum = sumPayAmount;
        }

        log.info("====================================正式抵扣======================================");
        log.info("orderDistributorDOS:{}", JSON.toJSONString(orderDistributorDOS));
        log.info("rebateVoucherAmountSum:{}", rebateVoucherAmountSum);
        log.info("sumPayAmount:{}", sumPayAmount);
        // 抵扣的代金券总额 按订单金额比例平分
        for (OrderDistributorDO orderDistributorDO : orderDistributorDOS) {
            OrderDistributorCostDO orderCost = orderDistributorDO.getOrderCost();
            // （旧）订单商品金额 - 促销金额 + 物流费 = 分销商归属应收款总额
            // （新）订单商品金额 - 促销金额 + 物流费 - 抵扣金额 = 分销商归属应收款总额
            // 该订单 需要抵扣的返利金额（多个订单拆分的情况）
            BigDecimal newGoodsRebateAmount = orderRebate(rebateVoucherAmountSum, sumPayAmount, orderCost);
            BigDecimal newGoodsAmountSum = orderDetailRebate(orderDistributorDO, orderCost, newGoodsRebateAmount);
            // ====================================数据矫正======================================
            log.info("====================================数据矫正======================================");
            // 订单（商品-促销）的金额 与 明细商品（单价-促销）*数量 是否相等
            BigDecimal orderGoodsRebateAmount = orderCost.getPayAmount().subtract(orderCost.getDistributionAmount());
            BigDecimal difference = orderGoodsRebateAmount.subtract(newGoodsAmountSum);
            if (difference.compareTo(BigDecimal.ZERO) != 0) {
                log.info("订单商品的金额 与 明细商品单价*数量，orderGoodsRebateAmount：{},newGoodsAmountSum：{}", orderGoodsRebateAmount,
                    newGoodsAmountSum);
                List<Integer> arr = Arrays.asList(1, 2, 4, 8, null);
                for (Integer itemCount : arr) {
                    if (correct(orderDistributorDO, difference, itemCount)) {
                        break;
                    }
                }
            }
            // 抵扣之后金额为0
            if (orderCost.getPayAmount().equals(BigDecimal.ZERO)) {
                OrderDistributorDataDO orderDistributorData = orderDistributorDO.getOrderDistributorData();
                orderDistributorData.setPayStatus(PayStatus.PAID.getValue());
                orderDistributorData.setOrderStatus(OrderStatus.CONFIRMED.getValue());
            }
        }
        log.info("返回的订单明细 使用返利金之后{}", JSON.toJSONString(orderDOS));
    }

    /**
     * 订单明细维度的代金券抵扣
     * 
     * @param orderDistributorDO
     * @param orderCost
     * @param newGoodsRebateAmount
     * @return 抵扣之后 实际价格*数量之和
     */
    public BigDecimal orderDetailRebate(OrderDistributorDO orderDistributorDO, OrderDistributorCostDO orderCost,
        BigDecimal newGoodsRebateAmount) {
        // 默认订单 应付款金额-物流费用+促销=订单(每个商品分销商归属实际结算单价*数量)之和
        // 处理完 订单总抵扣 现在处理商品详情的抵扣
        BigDecimal newGoodsAmountSum = BigDecimal.ZERO;
        // 订单维度 折扣后金额
        BigDecimal amountAfterDiscount = orderCost.getGoodsAmount().subtract(orderCost.getOrderPromotionAmount())
            .subtract(orderCost.getGoodsPromotionAmount());
        log.info("订单维度 折扣后金额:{}", amountAfterDiscount);
        for (OrderGoodsDO orderGoodss : orderDistributorDO.getOrderGoodss()) {
            BigDecimal itemCount = new BigDecimal(orderGoodss.getItemCount());
            OrderGoodsDistributorCostDO orderGoodsDistributorCost = orderGoodss.getOrderGoodsDistributorCost();
            if (itemCount.compareTo(BigDecimal.ZERO) == 0
                || orderGoodsDistributorCost.getActualPrice().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            BigDecimal goodsActualPriceSum = orderGoodsDistributorCost.getActualPrice().multiply(itemCount);
            // log.info("goodsActualPriceSum:{}",goodsActualPriceSum);
            BigDecimal goodsRebateVoucherSum =
                goodsActualPriceSum.divide(amountAfterDiscount, 16, HALF_UP).multiply(newGoodsRebateAmount);
            // log.info("goodsRebateVoucherSum:{}",goodsRebateVoucherSum);
            BigDecimal goodsRebateVoucherAmount = goodsRebateVoucherSum.divide(itemCount, 2, HALF_UP);
            // log.info("goodsRebateVoucherAmount:{}",goodsRebateVoucherAmount);
            orderGoodsDistributorCost.setRebateVoucherAmount(goodsRebateVoucherAmount);
            BigDecimal newActualPrice = orderGoodsDistributorCost.getActualPrice().subtract(goodsRebateVoucherAmount);
            if (newActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                newActualPrice = BigDecimal.ZERO;
            }
            orderGoodsDistributorCost.setActualPrice(newActualPrice);
            newGoodsAmountSum = newGoodsAmountSum.add(newActualPrice.multiply(itemCount));
        }
        return newGoodsAmountSum;
    }

    /**
     * 订单层面 代金券 抵扣计算
     * 
     * @param rebateVoucherAmountSum
     * @param sumPayAmount
     * @param orderCost
     * @return 对应（商品-促销）的抵扣金额
     */
    public BigDecimal orderRebate(BigDecimal rebateVoucherAmountSum, BigDecimal sumPayAmount,
        OrderDistributorCostDO orderCost) {
        BigDecimal rebateVoucherAmount =
            orderCost.getPayAmount().divide(sumPayAmount, HALF_UP).multiply(rebateVoucherAmountSum);
        log.info("该订单应该抵扣的返利金额：{}", rebateVoucherAmount);
        // 抵扣后的应付款金额
        BigDecimal newPayAmount = orderCost.getPayAmount().subtract(rebateVoucherAmount);
        // 旧物流费
        BigDecimal oldDistributionAmount = orderCost.getDistributionAmount();
        log.info("抵扣前的物流费：{}", oldDistributionAmount);
        if (newPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
            newPayAmount = BigDecimal.ZERO;
            orderCost.setDistributionAmount(BigDecimal.ZERO);
        } else if (newPayAmount.compareTo(oldDistributionAmount) < 0) {
            orderCost.setDistributionAmount(newPayAmount);
        }
        orderCost.setPayAmount(newPayAmount);
        orderCost.setRebateVoucherAmount(rebateVoucherAmount);
        // 新物流费
        BigDecimal newDistributionAmount = orderCost.getDistributionAmount();
        log.info("抵扣后的物流费：{}", newDistributionAmount);

        BigDecimal newGoodsRebateAmount;
        if (newDistributionAmount.compareTo(oldDistributionAmount) < 0) {
            // 如果新物流费小于 旧物流费 代表(商品-促销)已经抵扣完了 在抵物流费
            newGoodsRebateAmount = rebateVoucherAmount.subtract(oldDistributionAmount.subtract(newDistributionAmount));
        } else {
            // 如果新物流费大于等于 旧物流费 代表(商品-促销)没有抵扣完
            newGoodsRebateAmount = rebateVoucherAmount;
        }

        log.info("实际归属该订单 (商品-促销)的抵扣金额：{}", newGoodsRebateAmount);
        log.info("订单维度处理完成 商品总金额：{} - 促销总金额：({}|{}) + 物流费总金额：{} - 代金券总金额(物流抵扣|其他抵扣)：({}|{}) = 分销商归属应收款总额：{}",
            orderCost.getGoodsAmount(), orderCost.getOrderPromotionAmount(), orderCost.getGoodsPromotionAmount(),
            orderCost.getDistributionAmount(), newDistributionAmount.subtract(oldDistributionAmount),
            newGoodsRebateAmount, orderCost.getPayAmount());
        return newGoodsRebateAmount;
    }

    private boolean correct(OrderDistributorDO orderDistributorDO, BigDecimal difference, Integer itemCount) {
        OrderGoodsDO goodsDO;
        if (itemCount != null) {
            goodsDO = orderDistributorDO.getOrderGoodss().stream()
                .filter(orderGoodsDO -> orderGoodsDO.getItemCount().equals(itemCount)
                    && orderGoodsDO.getItemType().equals(Constant.ITEM_TYPE_1))
                .findFirst().orElse(null);
        } else {
            goodsDO = orderDistributorDO.getOrderGoodss().stream()
                .filter(orderGoodsDO -> orderGoodsDO.getItemType().equals(Constant.ITEM_TYPE_1)).findFirst().orElse(null);
        }
        if (goodsDO != null) {
            log.info("误差：{}", difference);
            log.info("矫正修改的行项 修改前 goodsDO:{}", JSON.toJSONString(goodsDO));
            BigDecimal itemCountValue = new BigDecimal(goodsDO.getItemCount());
            BigDecimal divide = difference.divide(itemCountValue, HALF_UP);
            OrderGoodsDistributorCostDO orderGoodsDistributorCost = goodsDO.getOrderGoodsDistributorCost();
            orderGoodsDistributorCost.setActualPrice(orderGoodsDistributorCost.getActualPrice().add(divide));
            orderGoodsDistributorCost
                .setRebateVoucherAmount(orderGoodsDistributorCost.getRebateVoucherAmount().subtract(divide));
            log.info("矫正修改的行项 修改后 goodsDO:{}", JSON.toJSONString(goodsDO));
            return true;
        }
        return false;
    }

    /**
     * 组装订单数据
     * 
     * @param distributor
     * @param orderDTO
     * @param goodss
     * @param goodsItemRpcDTOMap
     * @param itemStockMap
     * @param contactId
     * @param contactName
     * @param language
     * @param time
     * @param orderType
     * @param manufactors
     *            定制订单生产工厂
     * @return
     */
    private OrderDistributorDO getOrderDistributorDO(DistributorRpcDTO distributor, OrderInfoDTO orderDTO,
        List<OrderGoodsDTO> goodss, Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap,
        Map<String, ItemStockLockDTO> itemStockMap,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap,
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap, String contactId, String contactName, String language,
        Date time, Short orderType, String manufactors) {
        OrderDistributorDO orderDO = new OrderDistributorDO();
        // 组装订单基本信息
        OrderTypeDO diyMtoOrderType = getOrderTypeDO(orderType);
        // 定制信息
        orderDO.setDiyOrderTypeDO(diyMtoOrderType);
        OrderInfoDO order =
            OrderDistributorConvertor.toOrderInfoDO(distributor, orderDTO, time, orderType, diyMtoOrderType);
        // 订单基本信息
        orderDO.setOrder(order);
        // 组装订单明细、锁库、定制信息、明细费用
        List<OrderGoodsDO> orderGoodss =
            OrderDistributorConvertor.toOrderGoodsDOList(distributor.getId(), goodss, goodsItemRpcDTOMap, itemStockMap,
                goodsItemPricesMap, priceRpcDTOMap, language, time, orderType, diyMtoOrderType);
        // 订单明细
        orderDO.setOrderGoodss(orderGoodss);
        // 订单费用（分销商归属）
        OrderDistributorCostDO orderDistributorCostDO =
            OrderDistributorConvertor.toOrderDistributorCostDO(distributor.getId(), orderGoodss, time);
        orderDO.setOrderCost(orderDistributorCostDO);
        // 计算运费
        OrderLogisticsDTO logistics = getOrderLogisticsDTO(orderDTO, orderType, manufactors);
        LogisticsCalculationRpcDTO logisticsCalculation = orderRpcExe
            .getLogisticsCalculation(OrderDistributorConvertor.toLogisticsCalculationRpcQry(logistics.getLogisticsId(),
                orderDTO.getDelivery(), commonRpcQryExe.calcDiyWeight(orderGoodss), orderDistributorCostDO));
        if (logisticsCalculation.getCost() != null) {
            orderDistributorCostDO.setDistributionAmount(logisticsCalculation.getCost());
        } else {
            orderDistributorCostDO.setDistributionAmount(BigDecimal.ZERO);
        }
        // 计算应付款金额 (商品金额-商品促销金额-订单促销金额+物流费)
        BigDecimal actualAmount =
            orderDistributorCostDO.getGoodsAmount().subtract(orderDistributorCostDO.getGoodsPromotionAmount())
                .subtract(orderDistributorCostDO.getOrderPromotionAmount());
        if (actualAmount.compareTo(BigDecimal.ZERO) > 0) {
            orderDistributorCostDO.setPayAmount(actualAmount.add(orderDistributorCostDO.getDistributionAmount()));
        } else {
            orderDistributorCostDO.setPayAmount(orderDistributorCostDO.getDistributionAmount());
        }
        // 订单收货信息
        OrderDeliveryDO orderDeliveryDO = OrderDistributorConvertor.toOrderDeliveryDO(orderDTO, logistics, time);
        orderDO.setDelivery(orderDeliveryDO);
        // 订单发票信息
        OrderInvoiceDO invoiceDO = OrderDistributorConvertor.toOrderInvoiceDO(orderDTO, time);
        orderDO.setInvoice(invoiceDO);
        // 订单分销商信息
        OrderDistributorDataDO orderDistributorDataDO = OrderDistributorConvertor.toOrderDistributorDataDO(distributor,
            contactId, contactName, orderDTO, orderType, orderDistributorCostDO.getPayAmount(), time);
        // TODO 获取汇率（非本位币情况需获取汇率，本位币是相对于收款方而言）
        if (orderDistributorDataDO.getCurrencyType().equals("CNY")) {
            orderDistributorDataDO.setCurrentRates(new BigDecimal(1));
        } else {
            CurrencyRateRpcDTO rateRpcDTO =
                orderRpcExe.getCurrencyRate(orderDistributorDataDO.getCurrencyType(), "CNY");
            orderDistributorDataDO.setCurrentRates(rateRpcDTO.getExchangeRate());
        }
        orderDO.setOrderDistributorData(orderDistributorDataDO);
        // 订单扩展数据(同步erp情况或第三方客户订单号情况才生成)
        OrderExtendDataDO extendDataDO = OrderDistributorConvertor.toOrderExtendDataDO(distributor, orderDTO, time);
        orderDO.setExtendData(extendDataDO);
        log.info("orderDO:{}", JSONObject.toJSONString(orderDO));
        return orderDO;
    }

    /**
     * 获取订单类型
     * 
     * @param orderType
     * @return
     */
    private OrderTypeDO getOrderTypeDO(Short orderType) {
        if (orderType.equals(OrderType.DIY.getValue())) {
            List<OrderTypeDO> diyOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_4);
            if (CollectionUtils.isEmpty(diyOrderTypes)) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_TYPE_DIY_NULL);
            }
            return diyOrderTypes.get(0);
        }
        if (orderType.equals(OrderType.MTO.getValue())) {
            List<OrderTypeDO> mtoOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_2);
            if (CollectionUtils.isEmpty(mtoOrderTypes)) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_TYPE_MTO_NULL);
            }
            return mtoOrderTypes.get(0);
        }
        return null;
    }

    /**
     * 获取物流信息
     * 
     * @param orderDTO
     * @param orderType
     * @param manufactors
     * @return
     */
    private OrderLogisticsDTO getOrderLogisticsDTO(OrderInfoDTO orderDTO, Short orderType, String manufactors) {
        Optional<OrderLogisticsDTO> optional;
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
        if (!optional.isPresent()) {
            throw OrderException.buildException(OrderInfoErrorCode.P_ORDER_LOGISTICS_NULL, MessageUtils.get(OrderInfoErrorCode.P_ORDER_LOGISTICS_NULL));
        }
        return optional.get();
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
     * 分销订单审核接口(支持批量审核)
     * 
     * @param cmd
     * @param userId
     */
    public void checkOrder(OrderCheckCmd cmd, String userId) {
        commonValidator.checkUserId(userId);
        List<OrderDistributorDataDO> distributorDataDOS =
            orderDistributorDataDOMapper.listByOrderIdsAndAncestorDistributor(cmd.getIds(), Integer.valueOf(userId));
        List<OrderTreeNodeDataDTO> treeNodeDataDTOS =
            OrderDistributorConvertor.toOrderTreeNodeDataDTOList(cmd, distributorDataDOS);
        // 更新订单状态
        orderDistributorDataDOMapper.updateList(distributorDataDOS);
        for (OrderDistributorDataDO orderDistributorDataDO : distributorDataDOS) {
            if (orderDistributorDataDO.getOrderStatus() != null
                && orderDistributorDataDO.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()) {
                sendService.orderConfirmMsg(orderDistributorDataDO.getOrderId());
                break;
            }
        }
        if (cmd.getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
            // 发送分销层级数据消息
            sendService.orderTreeNodeData(treeNodeDataDTOS);
        } else {
            // 非定制订单或非预售订单拒绝 需解锁库存
            List<OrderTypeDO> diyOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_4);
            List<OrderTypeDO> mtoOrderTypes = orderTypeQryExe.listBySpecialFlag(Constant.SPECIAL_FLAG_2);
            List<Integer> releaseOrderIds = new ArrayList<>();
            List<OrderInfoDO> orderInfoDOS = orderInfoDOMapper.listByIds(cmd.getIds());
            orderInfoDOS.forEach(order -> {
                if ((CollectionUtils.isEmpty(diyOrderTypes)
                    || !order.getOrderTypeId().equals(diyOrderTypes.get(0).getId()))
                    && (CollectionUtils.isEmpty(mtoOrderTypes)
                        || !order.getOrderTypeId().equals(mtoOrderTypes.get(0).getId()))) {
                    releaseOrderIds.add(order.getId());
                }
            });
            if (!CollectionUtils.isEmpty(releaseOrderIds)) {
                orderGoodsStockCmdExe.releaseOrderGoodsStock(releaseOrderIds);
            }
        }
        try {
            Integer userIdInt = null;
            if (StringUtils.isNotBlank(userId)) {
                userIdInt = Integer.valueOf(userId);
            }
            sendService.orderLog(cmd.getIds(), request.getHeader("platform"), userIdInt, getUserName(), "分销订单审核",
                "审核成功", JSONObject.toJSONString(cmd), new Date());

        } catch (Exception e) {
            log.error("保存订单日志出现异常:{}", e.getMessage());
        }
    }

    private String getUserName() {
        String userName = request.getHeader("userName");
        if (StringUtils.isNotBlank(userName)) {
            try {
                return URLDecoder.decode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
