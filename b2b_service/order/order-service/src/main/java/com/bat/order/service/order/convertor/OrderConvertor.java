package com.bat.order.service.order.convertor;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.data.dto.OrderGoodsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeB2BOrderDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderVoucherErpDTO;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.order.dto.common.*;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.co.OrderExtendDataCO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 17:03
 */
public class OrderConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConvertor.class);

    public static void updateOrderGoodsExchangeCodeDO(List<ExchangeCodeB2BOrderDTORpcQry> exchangeCodeB2BOrders,
        List<OrderGoodsExchangeCodeDO> exchangeCodeDOS, DistributorRpcDTO exchangeDistributor) {
        Map<String, ExchangeCodeB2BOrderDTORpcQry> exchangeCodeB2BOrderMap =
            exchangeCodeB2BOrders.stream().collect(Collectors.toMap(ExchangeCodeB2BOrderDTORpcQry::getSecretCode,
                exchangeCodeB2BOrder -> exchangeCodeB2BOrder));
        exchangeCodeDOS.forEach(exchangeCodeDO -> {
            ExchangeCodeB2BOrderDTORpcQry exchangeCodeB2BOrder =
                exchangeCodeB2BOrderMap.get(exchangeCodeDO.getSecretCode());
            exchangeCodeDO.setExchangeOrderId(exchangeCodeB2BOrder.getDistributorOrderId());
            exchangeCodeDO.setExchangeOrderGoodsId(exchangeCodeB2BOrder.getDistributorOrderGoodsId());
            exchangeCodeDO.setDistributorId(exchangeCodeB2BOrder.getDistributorId());
            exchangeCodeDO.setDistributorCompanyName(exchangeCodeB2BOrder.getDistributorName());
            exchangeCodeDO.setSalesId(exchangeDistributor.getSalesId());
            exchangeCodeDO.setSalesName(exchangeDistributor.getSalesName());
        });
    }

    public static List<OrderOneMoreDTO> toOrderOneMoreDTOList(List<OrderGoodsDO> orderGoodsDOS,
        List<OrderGoodsDiyDO> orderGoodsDiyDOS, List<OrderGoodsDistributorCostDO> distributorCostDOS,
        List<GoodsItemPromotionRpcDTO> rpcDTOS, List<GoodsItemPriceRpcDTO> priceRpcDTOS,
        List<GoodsItemBoxRpcDTO> boxRpcDTOS, List<GoodsItemRpcDTO> itemRpcDTOS) {
        List<OrderOneMoreDTO> dtos = new ArrayList<>();
        Map<Integer, OrderGoodsDistributorCostDO> distributorCostDOMap = new HashMap<>();
        distributorCostDOMap.putAll(distributorCostDOS.stream().collect(
            Collectors.toMap(OrderGoodsDistributorCostDO::getOrderGoodsId, distributorCostDO -> distributorCostDO)));
        Map<Integer, OrderGoodsDiyDO> orderGoodsDiyDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderGoodsDiyDOS)) {
            orderGoodsDiyDOMap.putAll(orderGoodsDiyDOS.stream()
                .collect(Collectors.toMap(OrderGoodsDiyDO::getOrderGoodsId, orderGoodsDiyDO -> orderGoodsDiyDO)));
        }
        Map<Integer, GoodsItemPromotionRpcDTO> rpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOMap
                .putAll(rpcDTOS.stream().collect(Collectors.toMap(GoodsItemPromotionRpcDTO::getId, rpcDTO -> rpcDTO)));
        }
        Map<Integer, List<GoodsItemBoxRpcDTO>> boxRpcDTOListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(boxRpcDTOS)) {
            boxRpcDTOListMap
                .putAll(boxRpcDTOS.stream().collect(Collectors.groupingBy(GoodsItemBoxRpcDTO::getGoodsItemId)));
        }
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(priceRpcDTOS)) {
            priceRpcDTOMap.putAll(priceRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO)));
        }
        Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap =
            itemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, itemRpcDTO -> itemRpcDTO));
        Map<Integer, PromotionRuleConditionRpcDTO> promotionConditionMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
            orderGoodsDOS.forEach(orderGoodsDO -> {
                OrderGoodsDistributorCostDO distributorCostDO = distributorCostDOMap.get(orderGoodsDO.getId());
                OrderOneMoreDTO dto = new OrderOneMoreDTO();
                BeanUtils.copyProperties(orderGoodsDO, dto);
                dto.setItemType(distributorCostDO.getItemType());
                if (orderGoodsDO.getItemInCount() == null) {
                    dto.setItemInCount(0);
                }
                if (orderGoodsDO.getItemVmiCount() != null) {
                    dto.setItemInCount(dto.getItemInCount() + orderGoodsDO.getItemVmiCount());
                }
                OrderGoodsDiyDO diyDO = orderGoodsDiyDOMap.get(orderGoodsDO.getId());
                if (diyDO != null) {
                    OrderGoodsDiyDTO diy = new OrderGoodsDiyDTO();
                    BeanUtils.copyProperties(diyDO, diy);
                    dto.setDiy(diy);
                }
                GoodsItemPromotionRpcDTO rpcDTO = rpcDTOMap.get(orderGoodsDO.getItemId());
                if (rpcDTO != null) {
                    // 促销活动处理
                    List<PromotionRpcDTO> promotions = rpcDTO.getPromotions();
                    if (!CollectionUtils.isEmpty(promotions)) {
                        List<PromotionDTO> promotionDTOS = new ArrayList<>();
                        dto.setPromotions(promotionDTOS);
                        promotions.forEach(promotion -> {
                            PromotionDTO promotionDTO = new PromotionDTO();
                            BeanUtils.copyProperties(promotion, promotionDTO);
                            promotionDTOS.add(promotionDTO);
                            List<PromotionRuleRpcDTO> rules = promotion.getRules();
                            if (!CollectionUtils.isEmpty(rules)) {
                                List<PromotionRuleDTO> ruleDTOS = new ArrayList<>();
                                promotionDTO.setRules(ruleDTOS);
                                rules.forEach(rule -> {
                                    PromotionRuleDTO ruleDTO = new PromotionRuleDTO();
                                    BeanUtils.copyProperties(rule, ruleDTO);
                                    ruleDTOS.add(ruleDTO);
                                    List<PromotionRuleConditionRpcDTO> conditions = rule.getConditions();
                                    if (!CollectionUtils.isEmpty(conditions)) {
                                        List<PromotionRuleConditionDTO> conditionDTOS = new ArrayList<>();
                                        ruleDTO.setConditions(conditionDTOS);
                                        conditions.forEach(condition -> {
                                            PromotionRuleConditionDTO conditionDTO = new PromotionRuleConditionDTO();
                                            BeanUtils.copyProperties(condition, conditionDTO);
                                            conditionDTOS.add(conditionDTO);
                                            if (distributorCostDO.getGoodsPromotionId() != null && distributorCostDO
                                                .getGoodsPromotionId().intValue() == condition.getId().intValue()) {
                                                promotionConditionMap.put(condition.getId(), condition);
                                                dto.setGoodsPromotionId(ruleDTO.getId());
                                            }
                                            if (distributorCostDO.getOrderPromotionId() != null && distributorCostDO
                                                .getOrderPromotionId().intValue() == condition.getId().intValue()) {
                                                promotionConditionMap.put(condition.getId(), condition);
                                                dto.setOrderPromotionId(ruleDTO.getId());
                                            }
                                            List<PromotionRuleConditionPresentRpcDTO> presents =
                                                condition.getPresents();
                                            if (!CollectionUtils.isEmpty(presents)) {
                                                List<PromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
                                                conditionDTO.setPresents(presentDTOS);
                                                presents.forEach(present -> {
                                                    PromotionRuleConditionPresentDTO presentDTO =
                                                        new PromotionRuleConditionPresentDTO();
                                                    BeanUtils.copyProperties(present, presentDTO);
                                                    presentDTOS.add(presentDTO);
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    // 拼团秒杀处理
                    List<GoodsItemGroupSeckillRpcDTO> groupSeckills = rpcDTO.getGroupSeckills();
                    if (!CollectionUtils.isEmpty(groupSeckills)) {
                        List<GroupSeckillDTO> groupSeckillDTOS = new ArrayList<>();
                        dto.setGroupSeckills(groupSeckillDTOS);
                        groupSeckills.forEach(groupSeckill -> {
                            GroupSeckillDTO groupSeckillDTO = new GroupSeckillDTO();
                            BeanUtils.copyProperties(groupSeckill, groupSeckillDTO);
                            groupSeckillDTOS.add(groupSeckillDTO);
                            if (distributorCostDO.getSpellGroupId() != null && distributorCostDO.getSpellGroupId()
                                .intValue() == groupSeckill.getGroupSeckillId().intValue()) {
                                dto.setGroupSeckillId(distributorCostDO.getSpellGroupId());
                            }
                        });
                    }
                }
                // 处理价格
                GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(orderGoodsDO.getItemId());
                if (priceRpcDTO != null) {
                    dto.setRetailPrice(priceRpcDTO.getRetailPrice());
                    dto.setSalePrice(priceRpcDTO.getSalePrice());
                }
                // 处理装箱数据
                List<GoodsItemBoxRpcDTO> boxRpcDTOList = boxRpcDTOListMap.get(dto.getItemId());
                if (!CollectionUtils.isEmpty(boxRpcDTOList)) {
                    List<GoodsItemBoxDTO> boxDTOS = new ArrayList<>();
                    dto.setBoxs(boxDTOS);
                    boxRpcDTOList.forEach(boxRpcDTO -> {
                        GoodsItemBoxDTO boxDTO = new GoodsItemBoxDTO();
                        BeanUtils.copyProperties(boxRpcDTO, boxDTO);
                        boxDTOS.add(boxDTO);
                    });
                }
                // 赠品处理活动数据
                if (distributorCostDO.getItemType() != null && distributorCostDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                    dto.setOrderPromotionId(distributorCostDO.getOrderPromotionId());
                    dto.setGoodsPromotionId(distributorCostDO.getGoodsPromotionId());
                }
                // 处理商品有效、是否支持预售、直发客户是否支持在途
                GoodsItemRpcDTO goodsItemRpcDTO = itemRpcDTOMap.get(dto.getItemId());
                dto.setAdvanceSaleFlag(goodsItemRpcDTO.getAdvanceSaleFlag());
                dto.setOnwaySaleFlag(goodsItemRpcDTO.getOnwaySaleFlag());
                if (goodsItemRpcDTO.getSaleStatus().equals(Constant.SALE_STATUS_3)) {
                    dto.setOpenFlag(Constant.OPEN_FLAG_1);
                } else {
                    dto.setOpenFlag(Constant.OPEN_FLAG_0);
                }
                dtos.add(dto);
            });
        }
        for (int i = 0; i < dtos.size(); i++) {
            OrderOneMoreDTO dto = dtos.get(i);
            // 当活动实效时，赠品应剔除
            if (dto.getItemType().equals(Constant.ITEM_TYPE_2)) {
                if (dto.getOrderPromotionId() != null) {
                    PromotionRuleConditionRpcDTO conditionRpcDTO = promotionConditionMap.get(dto.getOrderPromotionId());
                    if (conditionRpcDTO != null) {
                        dto.setOrderPromotionId(conditionRpcDTO.getPromotionRuleId());
                    } else {
                        dtos.remove(dto);
                        i--;
                    }
                } else if (dto.getGoodsPromotionId() != null) {
                    PromotionRuleConditionRpcDTO conditionRpcDTO = promotionConditionMap.get(dto.getGoodsPromotionId());
                    if (conditionRpcDTO != null) {
                        dto.setGoodsPromotionId(conditionRpcDTO.getPromotionRuleId());
                    } else {
                        dtos.remove(dto);
                        i--;
                    }
                }
            }
        }
        return dtos;
    }

    public static OrderExtendDataDO toOrderExtendDataDO(Integer orderId, DistributorRpcDTO distributor) {
        OrderExtendDataDO extendDataDO = new OrderExtendDataDO();
        extendDataDO.setOrderId(orderId);
        Date time = new Date(System.currentTimeMillis());
        extendDataDO.setAutoDelivery(distributor.getAutoDelivery());
        extendDataDO.setCreateTime(time);
        extendDataDO.setUpdateTime(time);
        return extendDataDO;
    }

    public static List<OrderVoucherErpDTO> toOrderVoucherErpDTOList(List<OrderExtendDataCO> orderExtendDataCOS,
        List<OrderDistributorCostDO> distributorCostDOS) {
        List<OrderVoucherErpDTO> erpDTOS = new ArrayList<>();
        Map<Integer, OrderExtendDataCO> orderExtendDataCOMap = orderExtendDataCOS.stream()
            .collect(Collectors.toMap(OrderExtendDataCO::getOrderId, orderExtendDataCO -> orderExtendDataCO));
        distributorCostDOS.forEach(distributorCostDO -> {
            OrderVoucherErpDTO erpDTO = new OrderVoucherErpDTO();
            OrderExtendDataCO orderExtendDataCO = orderExtendDataCOMap.get(distributorCostDO.getOrderId());
            erpDTO.setOrderId(distributorCostDO.getOrderId());
            erpDTO.setOrderErpNo(orderExtendDataCO.getOrderErpNo());
            if (distributorCostDO.getPlatformAmount() != null
                && distributorCostDO.getPlatformAmount().doubleValue() > 0) {
                erpDTO.setPaidAmount(distributorCostDO.getPlatformAmount());
            } else {
                erpDTO.setPaidAmount(distributorCostDO.getPaidAmount());
            }
            erpDTOS.add(erpDTO);
        });
        return erpDTOS;
    }

    public static Map<String, ItemStockLockDTO> toItemStockLockDTOMap(List<ItemStockLockDTO> itemStockLockDTOS,
                                                                      List<OrderGoodsDTO> goodss, Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap) {
        Map<String, ItemStockLockDTO> itemStockMap = new HashMap<>();
        if (CollectionUtils.isEmpty(itemStockLockDTOS)) {
            return itemStockMap;
        }
        if (!CollectionUtils.isEmpty(goodss)) {
            Integer serialNumber = 0;
            for (int i = 0; i < goodss.size(); i++) {
                OrderGoodsDTO orderGoodsDTO = goodss.get(i);
                GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(orderGoodsDTO.getItemCode());
                if (rpcDTO.getGoodsType().equals(Constant.GOODS_TYPE_1) && orderGoodsDTO.getMtoType().equals(Constant.MTO_TYPE_0)) {
                    // 锁库也是这个条件
                    for (int x = serialNumber; x < itemStockLockDTOS.size(); x++) {
                        ItemStockLockDTO itemStockLockDTO = itemStockLockDTOS.get(x);
                        String key = itemStockLockDTO.getItemId() + "_" + orderGoodsDTO.getSerialNumber();
                        if (itemStockLockDTO.getItemId() - rpcDTO.getId() == 0) {
                            // 按照顺序、得到锁库明细
                            itemStockMap.put(key, itemStockLockDTO);
                            // 坐标右移
                            serialNumber = x + 1;
                            break;
                        }
                    }
                }
            }
        }
        return itemStockMap;
    }

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list = new ArrayList<>();
        list.add(111);
        list.add(222);
        list1.forEach(integer -> {
            for (int x = 0; x < list.size(); x++) {
                if (list.get(x) > 0) {
                    break;
                }
                System.out.println(list.get(x));
            }
            System.out.println(integer);
        });
    }

    /**
     * //锁库数量是按照货品合并进行锁库、但下单可能出现同一个货品多个明细、需要对锁库进行拆分（key是itemId_行序号）
     * 
     * @param goodss
     * @param itemStockLockDTOS
     */
    /* public static Map<String, ItemStockLockDTO> splitItemLockStock(List<OrderGoodsDTO> goodss, List<ItemStockLockDTO> itemStockLockDTOS) {
        Map<String, ItemStockLockDTO> itemStockMap = new HashMap<>();
        if(itemStockLockDTOS ==null ||itemStockLockDTOS.size()==0){
            return itemStockMap;
        }
        //先按照货品id转换
        Map<Integer, ItemStockLockDTO> map = itemStockLockDTOS.stream().collect(Collectors.toMap(ItemStockLockDTO::getItemId, itemStockLockDTO -> itemStockLockDTO));
        //下单明细列表进行itemId升序
        goodss.sort((o1, o2) -> o1.getItemId() - o2.getItemId());
        //货品id（基准、判断是否跟上一个订单明细都是同一个货品）
        Integer itemId =0;
        //同一个货品的索引列表（数量满足的不在里面）
        List<Integer> itemIndexList = new ArrayList<>();
        for(int x=0;x<goodss.size();x++){
            //
            OrderGoodsDTO orderGoodsDTO = goodss.get(x);
            ItemStockLockDTO itemStockLockDTO = map.get(orderGoodsDTO.getItemId());
            Integer lockCount = calculateLockCount(itemStockLockDTO);
            if(lockCount==orderGoodsDTO.getItemCount()){
                //锁库数量一致
                itemStockMap.put(orderGoodsDTO.getItemId()+"_"+orderGoodsDTO.getSerialNumber(),itemStockLockDTO);
            }else if(lockCount >orderGoodsDTO.getItemCount()){
                //需要分拆
    
            }else{
                //数量错误
                throw OrderException.buildException("数量错误");
            }
        }
        //最后需要对行序号进行升序
        goodss.sort((o1, o2) -> o1.getSerialNumber()-o2.getSerialNumber());
        return itemStockMap;
    }
    
    *//**
        * 计算货品锁的数量
        * 
        * @param itemStockLockDTO
        * @return
        *//*
          private static Integer calculateLockCount(ItemStockLockDTO itemStockLockDTO) {
           //计算数量
           Integer lockCount = 0;
           VminStockLockDTO vmiLock = itemStockLockDTO.getVmiLock();
           OnWayStockLockDTO onWayLock = itemStockLockDTO.getOnWayLock();
           List<WarehouseInStockLockDTO> inStockLockDTOList = itemStockLockDTO.getInStockLockDTOList();
           if(vmiLock !=null){
               lockCount +=vmiLock.getLockNum();
           }
           if(onWayLock !=null){
               lockCount+=onWayLock.getLockNum();
           }
           if(inStockLockDTOList !=null && inStockLockDTOList.size()>0){
               for(int x=0;x<inStockLockDTOList.size();x++){
                   lockCount+=inStockLockDTOList.get(x).getLockNum();
               }
           }
           return lockCount;
          }
          
          public static void main(String[] args) {
           List<OrderGoodsDTO> goodss = new ArrayList<>();
           OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
           orderGoodsDTO.setSerialNumber(4);
           orderGoodsDTO.setItemCode("123");
           orderGoodsDTO.setItemCount(123);
           goodss.add(orderGoodsDTO);
           OrderGoodsDTO orderGoodsDTO2 = new OrderGoodsDTO();
           orderGoodsDTO2.setSerialNumber(1);
           orderGoodsDTO2.setItemCode("123");
           orderGoodsDTO2.setItemCount(1234);
           goodss.add(orderGoodsDTO2);
           OrderGoodsDTO orderGoodsDTO3 = new OrderGoodsDTO();
           orderGoodsDTO3.setItemCode("1234");
           orderGoodsDTO3.setItemCount(1234);
           orderGoodsDTO3.setSerialNumber(3);
           goodss.add(orderGoodsDTO3);
           OrderGoodsDTO orderGoodsDTO4 = new OrderGoodsDTO();
           orderGoodsDTO4.setItemCode("122");
           orderGoodsDTO4.setItemCount(122);
           orderGoodsDTO4.setSerialNumber(2);
           goodss.add(orderGoodsDTO4);
           goodss.sort((o1, o2) -> o1.getItemCode().compareTo(o2.getItemCode()));
           System.out.println(JSON.toJSONString(goodss));
           goodss.sort((o1, o2) -> o1.getSerialNumber()-o2.getSerialNumber());
           System.out.println(JSON.toJSONString(goodss));
          }*/

}
