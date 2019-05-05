package com.bat.promotion.service.common.rpc;

import static com.bat.promotion.service.common.Constant.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.promotion.dao.promotion.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.promotion.dao.coupon.dataobject.CouponMaterialRelevanceDO;
import com.bat.promotion.dao.coupon.dataobject.CouponModelRelevanceDO;
import com.bat.promotion.dao.coupon.dataobject.UserCustomerCouponDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.UserGoodsItemGroupSeckillDO;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.common.CommonErrorCode;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.api.base.MessageUtils;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/15 16:24
 */
public class PromotionRpcConvertor {

    public static List<GoodsItemPromotionRpcDTO> toGoodsItemPromotionRpcDTOList(List<PromotionDO> promotionDOS,
                                                                                List<PromotionRuleDO> ruleDOS, List<PromotionRuleConditionDO> ruleConditionDOS,
                                                                                List<PromotionRuleConditionPresentDO> conditionPresentDOS, List<GoodsItemRpcDTO> itemRpcDTOS,
                                                                                List<PromotionRuleConditionSpecialDO> conditionSpecialDOS, List<PromotionRuleGoodsDO> ruleGoodsDOS,
                                                                                List<UserGoodsItemGroupSeckillDO> groupSeckillDOS, List<GoodsItemRpcQry> goodsItems) {
        List<GoodsItemPromotionRpcDTO> rpcDTOS = new ArrayList<>();
        Map<Integer, PromotionDO> promotionDOMap =
            promotionDOS.stream().collect(Collectors.toMap(PromotionDO::getId, promotionDO -> promotionDO));
        Map<Integer, List<PromotionRuleConditionDO>> conditionDOSMap =
            ruleConditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId));
        Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
            itemRpcDTOMap.putAll(
                itemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, itemRpcDTO -> itemRpcDTO)));
        }
        goodsItems.forEach(goodsItem -> {
            if (!CollectionUtils.isEmpty(ruleDOS) || !CollectionUtils.isEmpty(groupSeckillDOS)) {
                GoodsItemPromotionRpcDTO rpcDTO = new GoodsItemPromotionRpcDTO();
                rpcDTO.setId(goodsItem.getItemId());
                rpcDTOS.add(rpcDTO);
                // 处理促销活动
                if (!CollectionUtils.isEmpty(ruleDOS)) {
                    Map<Integer, PromotionRpcDTO> goodsPromotionDTOMap = new HashMap<>();
                    List<PromotionRpcDTO> promotions = new ArrayList<>();
                    rpcDTO.setPromotions(promotions);
                    ruleDOS.forEach(ruleDO -> {
                        List<PromotionRuleGoodsDO> promotionRuleGoodsDOS = ruleGoodsDOS.stream()
                            .filter(ruleGoodsDO -> ((ruleDO.getRuleType().equals(RULE_TYPE_2)
                                && ruleGoodsDO.getGoodsId() != null
                                && ruleGoodsDO.getGoodsId().equals(goodsItem.getGoodsId()))
                                || (ruleDO.getRuleType().equals(RULE_TYPE_3) && ruleGoodsDO.getItemId() != null
                                    && ruleGoodsDO.getItemId().equals(goodsItem.getItemId())))
                                && ruleGoodsDO.getPromotionRuleId().equals(ruleDO.getId()))
                            .collect(Collectors.toList());

                        if (ruleDO.getRuleType().equals(RULE_TYPE_1)
                            || !CollectionUtils.isEmpty(promotionRuleGoodsDOS)) {
                            // 活动
                            PromotionRpcDTO promotionRpcDTO = goodsPromotionDTOMap.get(ruleDO.getPromotionId());
                            if (promotionRpcDTO == null) {
                                promotionRpcDTO = new PromotionRpcDTO();
                                PromotionDO promotionDO = promotionDOMap.get(ruleDO.getPromotionId());
                                BeanUtils.copyProperties(promotionDO, promotionRpcDTO);
                                promotions.add(promotionRpcDTO);
                                goodsPromotionDTOMap.put(promotionRpcDTO.getId(), promotionRpcDTO);
                            }
                            // 活动规则
                            List<PromotionRuleRpcDTO> rules = promotionRpcDTO.getRules();
                            if (rules == null) {
                                rules = new ArrayList<>();
                                promotionRpcDTO.setRules(rules);
                            }
                            PromotionRuleRpcDTO ruleRpcDTO = new PromotionRuleRpcDTO();
                            BeanUtils.copyProperties(ruleDO, ruleRpcDTO);
                            rules.add(ruleRpcDTO);
                            List<PromotionRuleConditionDO> conditionDOS = conditionDOSMap.get(ruleDO.getId());
                            // 活动规则条件
                            if (!CollectionUtils.isEmpty(conditionDOS)) {
                                List<PromotionRuleConditionRpcDTO> conditions = new ArrayList<>();
                                ruleRpcDTO.setConditions(conditions);
                                conditionDOS.forEach(conditionDO -> {
                                    PromotionRuleConditionRpcDTO conditionRpcDTO = new PromotionRuleConditionRpcDTO();
                                    BeanUtils.copyProperties(conditionDO, conditionRpcDTO);
                                    conditions.add(conditionRpcDTO);
                                    // 处理条件特价
                                    if (ruleRpcDTO.getRuleType().equals(RULE_TYPE_3)
                                        && conditionDO.getSpecialFlag() != null
                                        && conditionDO.getSpecialFlag().equals(SPECIAL_FLAG_1)) {
                                        Optional<PromotionRuleConditionSpecialDO> optional =
                                            conditionSpecialDOS.stream()
                                                .filter(conditionSpecialDO -> conditionSpecialDO
                                                    .getPromotionRuleConditionId().equals(conditionRpcDTO.getId())
                                                    && conditionSpecialDO.getItemId().equals(goodsItem.getItemId()))
                                                .findFirst();
                                        if (optional != null && optional.isPresent()) {
                                            conditionRpcDTO.setSpecialPrice(optional.get().getSpecialPrice());
                                        }
                                    }
                                    // 处理条件赠品
                                    if (conditionDO.getReduceOrPresent() != null
                                        && conditionDO.getReduceOrPresent().equals(CONDITION_PRESENT)
                                        && !CollectionUtils.isEmpty(conditionPresentDOS)) {
                                        List<PromotionRuleConditionPresentDO> presentDOS = conditionPresentDOS.stream()
                                            .filter(conditionPresentDO -> conditionPresentDO
                                                .getPromotionRuleConditionId().equals(conditionDO.getId()))
                                            .collect(Collectors.toList());
                                        if (!CollectionUtils.isEmpty(presentDOS)) {
                                            List<PromotionRuleConditionPresentRpcDTO> presentDTOS = new ArrayList<>();
                                            conditionRpcDTO.setPresents(presentDTOS);
                                            presentDOS.forEach(presentDO -> {
                                                PromotionRuleConditionPresentRpcDTO presentDTO =
                                                    new PromotionRuleConditionPresentRpcDTO();
                                                if (itemRpcDTOMap != null && itemRpcDTOMap.size() > 0) {
                                                    GoodsItemRpcDTO goodsItemRpcDTO =
                                                        itemRpcDTOMap.get(presentDO.getItemId());
                                                    BeanUtils.copyProperties(goodsItemRpcDTO, presentDTO);
                                                }
                                                BeanUtils.copyProperties(presentDO, presentDTO);
                                                presentDTOS.add(presentDTO);
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                // 处理拼团秒杀活动
                if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
                    List<UserGoodsItemGroupSeckillDO> goodsItemGroupSeckillDOS = groupSeckillDOS.stream()
                        .filter(groupSeckillDO -> groupSeckillDO.getItemId().equals(goodsItem.getItemId()))
                        .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(goodsItemGroupSeckillDOS)) {
                        List<GoodsItemGroupSeckillRpcDTO> groupSeckills = new ArrayList<>();
                        rpcDTO.setGroupSeckills(groupSeckills);
                        goodsItemGroupSeckillDOS.forEach(groupSeckillDO -> {
                            GoodsItemGroupSeckillRpcDTO groupSeckillDTO = new GoodsItemGroupSeckillRpcDTO();
                            BeanUtils.copyProperties(groupSeckillDO, groupSeckillDTO);
                            groupSeckills.add(groupSeckillDTO);
                        });
                    }
                }
            }
        });
        return rpcDTOS;
    }

    public static List<GoodsItemPromotionRpcDTO> toGoodsItemPromotionRpcDTOList(List<PromotionDO> promotionDOS,
        List<PromotionRuleDO> ruleDOS, List<PromotionRuleConditionDO> ruleConditionDOS,
        List<GoodsItemRpcDTO> itemRpcDTOS, List<PromotionRuleConditionSpecialDO> conditionSpecialDOS,
        List<PromotionRuleGoodsDO> ruleGoodsDOS, List<UserGoodsItemGroupSeckillDO> groupSeckillDOS,
        List<GoodsItemRpcQry> goodsItems) {
        List<GoodsItemPromotionRpcDTO> rpcDTOS = new ArrayList<>();
        Map<Integer, PromotionDO> promotionDOMap =
            promotionDOS.stream().collect(Collectors.toMap(PromotionDO::getId, promotionDO -> promotionDO));
        Map<Integer, List<PromotionRuleConditionDO>> conditionDOSMap =
            ruleConditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId));
        Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
            itemRpcDTOMap.putAll(
                itemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, itemRpcDTO -> itemRpcDTO)));
        }
        goodsItems.forEach(goodsItem -> {
            GoodsItemPromotionRpcDTO rpcDTO = new GoodsItemPromotionRpcDTO();
            rpcDTO.setId(goodsItem.getItemId());
            // 处理促销活动
            if (!CollectionUtils.isEmpty(ruleDOS)) {
                Map<Integer, PromotionRpcDTO> goodsPromotionDTOMap = new HashMap<>();
                List<PromotionRpcDTO> promotions = new ArrayList<>();
                rpcDTO.setPromotions(promotions);
                rpcDTOS.add(rpcDTO);
                ruleDOS.forEach(ruleDO -> {
                    List<PromotionRuleGoodsDO> promotionRuleGoodsDOS = ruleGoodsDOS.stream()
                        .filter(ruleGoodsDO -> ((ruleDO.getRuleType().equals(RULE_TYPE_2)
                            && ruleGoodsDO.getGoodsId() != null
                            && ruleGoodsDO.getGoodsId().equals(goodsItem.getGoodsId()))
                            || (ruleDO.getRuleType().equals(RULE_TYPE_3) && ruleGoodsDO.getItemId() != null
                                && ruleGoodsDO.getItemId().equals(goodsItem.getItemId())))
                            && ruleGoodsDO.getPromotionRuleId().equals(ruleDO.getId()))
                        .collect(Collectors.toList());
                    if (ruleDO.getRuleType().equals(RULE_TYPE_1) || !CollectionUtils.isEmpty(promotionRuleGoodsDOS)) {
                        // 活动
                        PromotionRpcDTO promotionRpcDTO = goodsPromotionDTOMap.get(ruleDO.getPromotionId());
                        PromotionDO promotionDO = promotionDOMap.get(ruleDO.getPromotionId());
                        if (promotionDO == null) {
                            return;
                        }
                        if (promotionRpcDTO == null) {
                            promotionRpcDTO = new PromotionRpcDTO();
                            BeanUtils.copyProperties(promotionDO, promotionRpcDTO);
                            promotions.add(promotionRpcDTO);
                            goodsPromotionDTOMap.put(promotionRpcDTO.getId(), promotionRpcDTO);
                        }
                        // 活动规则
                        List<PromotionRuleRpcDTO> rules = promotionRpcDTO.getRules();
                        if (rules == null) {
                            rules = new ArrayList<>();
                            promotionRpcDTO.setRules(rules);
                        }
                        PromotionRuleRpcDTO ruleRpcDTO = new PromotionRuleRpcDTO();
                        BeanUtils.copyProperties(ruleDO, ruleRpcDTO);
                        rules.add(ruleRpcDTO);
                        List<PromotionRuleConditionDO> conditionDOS = conditionDOSMap.get(ruleDO.getId());
                        // 活动规则条件
                        if (!CollectionUtils.isEmpty(conditionDOS)) {
                            List<PromotionRuleConditionRpcDTO> conditions = new ArrayList<>();
                            ruleRpcDTO.setConditions(conditions);
                            conditionDOS.forEach(conditionDO -> {
                                PromotionRuleConditionRpcDTO conditionRpcDTO = new PromotionRuleConditionRpcDTO();
                                BeanUtils.copyProperties(conditionDO, conditionRpcDTO);
                                conditions.add(conditionRpcDTO);
                                // 处理条件特价
                                if (ruleRpcDTO.getRuleType().equals(RULE_TYPE_3) && conditionDO.getSpecialFlag() != null
                                    && conditionDO.getSpecialFlag().equals(SPECIAL_FLAG_1)) {
                                    Optional<PromotionRuleConditionSpecialDO> optional = conditionSpecialDOS.stream()
                                        .filter(conditionSpecialDO -> conditionSpecialDO.getPromotionRuleConditionId()
                                            .equals(conditionRpcDTO.getId())
                                            && conditionSpecialDO.getItemId().equals(goodsItem.getItemId()))
                                        .findFirst();
                                    if (optional != null && optional.isPresent()) {
                                        conditionRpcDTO.setSpecialPrice(optional.get().getSpecialPrice());
                                    }
                                }
                            });
                        }
                    }
                });
            }
            // 处理拼团秒杀活动
            if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
                List<UserGoodsItemGroupSeckillDO> goodsItemGroupSeckillDOS = groupSeckillDOS.stream()
                    .filter(groupSeckillDO -> groupSeckillDO.getItemId().equals(goodsItem.getItemId()))
                    .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(goodsItemGroupSeckillDOS)) {
                    List<GoodsItemGroupSeckillRpcDTO> groupSeckills = new ArrayList<>();
                    rpcDTO.setGroupSeckills(groupSeckills);
                    goodsItemGroupSeckillDOS.forEach(groupSeckillDO -> {
                        GoodsItemGroupSeckillRpcDTO groupSeckillDTO = new GoodsItemGroupSeckillRpcDTO();
                        BeanUtils.copyProperties(groupSeckillDO, groupSeckillDTO);
                        groupSeckills.add(groupSeckillDTO);
                    });
                }
            }
        });
        return rpcDTOS;
    }

    public static List<GoodsItemPromotionPriceRpcDTO> toGoodsItemPromotionPriceRpcDTOList(
        List<GoodsItemPriceRpcQry> goodsItems, List<UserCustomerCouponDO> couponDOS,
        List<CouponMaterialRelevanceDO> materialDOS, List<CouponModelRelevanceDO> modelDOS, Date time) {
        List<GoodsItemPromotionPriceRpcDTO> priceRpcDTOS = new ArrayList<>();
        Map<String, UserCustomerCouponDO> couponDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponDOMap.putAll(
                couponDOS.stream().collect(Collectors.toMap(UserCustomerCouponDO::getCouponNo, couponDO -> couponDO)));
        }
        Map<Integer, List<CouponMaterialRelevanceDO>> materialDOSMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(materialDOS)) {
            materialDOSMap
                .putAll(materialDOS.stream().collect(Collectors.groupingBy(CouponMaterialRelevanceDO::getCouponId)));
        }
        Map<Integer, List<CouponModelRelevanceDO>> modelDOSMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(modelDOS)) {
            modelDOSMap.putAll(modelDOS.stream().collect(Collectors.groupingBy(CouponModelRelevanceDO::getCouponId)));
        }
        Map<String, List<GoodsItemPriceRpcQry>> goodsItemsMap =
            goodsItems.stream().filter(goodsItem -> StringUtils.isNotBlank(goodsItem.getCouponNo()))
                .collect(Collectors.groupingBy(GoodsItemPriceRpcQry::getCouponNo));
        goodsItemsMap.entrySet().forEach(entry -> {
            List<GoodsItemPromotionPriceRpcDTO> subPriceRpcDTOS = new ArrayList<>();
            String couponNo = entry.getKey();
            List<GoodsItemPriceRpcQry> goodsItemList = entry.getValue();
            UserCustomerCouponDO couponDO = couponDOMap.get(couponNo);
            if (couponDO == null) {
                goodsItemList.forEach(goodsItem -> {
                    GoodsItemPromotionPriceRpcDTO priceRpcDTO = new GoodsItemPromotionPriceRpcDTO();
                    BeanUtils.copyProperties(goodsItem, priceRpcDTO);
                    priceRpcDTO.setActualPrice(priceRpcDTO.getSalePrice());
                    priceRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_CONDITION_NULL);
                    priceRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_CONDITION_NULL));
                    subPriceRpcDTOS.add(priceRpcDTO);
                });
                priceRpcDTOS.addAll(subPriceRpcDTOS);
                return;
            }
            if (!couponDO.getCouponStatus().equals(COUPON_STATUS_2)
                || time.getTime() > couponDO.getEndTime().getTime()) {
                goodsItemList.forEach(goodsItem -> {
                    GoodsItemPromotionPriceRpcDTO priceRpcDTO = new GoodsItemPromotionPriceRpcDTO();
                    BeanUtils.copyProperties(goodsItem, priceRpcDTO);
                    priceRpcDTO.setActualPrice(priceRpcDTO.getSalePrice());
                    priceRpcDTO.setFlag(CommonErrorCode.B_COUPON_USE_FLAG_ERROR);
                    priceRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_COUPON_USE_FLAG_ERROR));
                    subPriceRpcDTOS.add(priceRpcDTO);
                });
                priceRpcDTOS.addAll(subPriceRpcDTOS);
                return;
            }
            AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(new BigDecimal(0));
            goodsItemList.forEach(goodsItem -> {
                GoodsItemPromotionPriceRpcDTO priceRpcDTO = new GoodsItemPromotionPriceRpcDTO();
                BeanUtils.copyProperties(goodsItem, priceRpcDTO);
                priceRpcDTO.setActualPrice(priceRpcDTO.getSalePrice());
                priceRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_SUCCESS);
                subPriceRpcDTOS.add(priceRpcDTO);
                if (couponDO.getMaterialScope().equals(MATERIAL_SCOPE_2)) {
                    List<CouponMaterialRelevanceDO> materialRelevanceDOS = materialDOSMap.get(couponDO.getCouponId());
                    List<Integer> materialIds = materialRelevanceDOS.stream()
                        .map(CouponMaterialRelevanceDO::getMaterialId).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialIds) || !materialIds.contains(priceRpcDTO.getMaterialId())) {
                        priceRpcDTO.setFlag(CommonErrorCode.B_COUPON_USE_FLAG_ERROR);
                        priceRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_COUPON_USE_FLAG_ERROR));
                        return;
                    }
                }
                if (couponDO.getModelScope().equals(MODEL_SCOPE_2)) {
                    List<CouponModelRelevanceDO> modelRelevanceDOS = modelDOSMap.get(couponDO.getCouponId());
                    List<Integer> modelIds =
                        modelRelevanceDOS.stream().map(CouponModelRelevanceDO::getModelId).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(modelIds) || !modelIds.contains(priceRpcDTO.getModelId())) {
                        priceRpcDTO.setFlag(CommonErrorCode.B_COUPON_USE_FLAG_ERROR);
                        priceRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_COUPON_USE_FLAG_ERROR));
                        return;
                    }
                }
                totalAmount.set(totalAmount.get()
                    .add(new BigDecimal(goodsItem.getSalePrice().doubleValue() * goodsItem.getItemCount().intValue()))
                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
            });
            BigDecimal orderMoney = couponDO.getOrderMoney();
            if (orderMoney != null && orderMoney.doubleValue() > 0
                && totalAmount.get().doubleValue() < orderMoney.doubleValue()) {
                subPriceRpcDTOS.forEach(priceRpcDTO -> {
                    priceRpcDTO.setFlag(CommonErrorCode.B_COUPON_USE_FLAG_ERROR);
                    priceRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_COUPON_USE_FLAG_ERROR));
                });
                priceRpcDTOS.addAll(subPriceRpcDTOS);
                return;
            }
            // 1满减 2满折 3兑换
            if (couponDO.getCouponMethod().equals(COUPON_METHOD_1)) {
                AtomicReference<BigDecimal> totalCouponAmount = new AtomicReference<>(new BigDecimal(0));
                List<GoodsItemPromotionPriceRpcDTO> differPriceRpcDTOS = new ArrayList<>();
                subPriceRpcDTOS.forEach(priceRpcDTO -> {
                    priceRpcDTO.setCouponMethod(couponDO.getCouponMethod());
                    priceRpcDTO.setDeliveryFeeFlag(couponDO.getDeliveryFeeFlag());
                    double couponAmount = priceRpcDTO.getSalePrice().doubleValue()
                        * couponDO.getReduction().doubleValue() / totalAmount.get().doubleValue();
                    BigDecimal actualPrice = priceRpcDTO.getSalePrice().subtract(new BigDecimal(couponAmount)
                        .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                    priceRpcDTO.setActualPrice(actualPrice);
                    totalCouponAmount.set(totalCouponAmount.get()
                        .add(new BigDecimal(priceRpcDTO.getSalePrice().subtract(actualPrice).doubleValue()
                            * priceRpcDTO.getItemCount().intValue()))
                        .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                    if (subPriceRpcDTOS.indexOf(priceRpcDTO) == subPriceRpcDTOS.size() - 1
                        && totalCouponAmount.get().compareTo(couponDO.getReduction()) != 0) {
                        // 优惠差价
                        BigDecimal differ = couponDO.getReduction().subtract(totalCouponAmount.get());
                        if (priceRpcDTO.getItemCount() == 1) {
                            priceRpcDTO.setActualPrice(actualPrice.subtract(differ));
                        } else {
                            GoodsItemPromotionPriceRpcDTO differPriceRpcDTO = new GoodsItemPromotionPriceRpcDTO();
                            BeanUtils.copyProperties(priceRpcDTO, differPriceRpcDTO);
                            differPriceRpcDTO.setActualPrice(actualPrice.subtract(differ));
                            differPriceRpcDTO.setItemCount(1);
                            differPriceRpcDTO.setItemInCount(1);
                            priceRpcDTO.setItemCount(priceRpcDTO.getItemCount() - 1);
                            if (priceRpcDTO.getItemInCount() != null && priceRpcDTO.getItemInCount() > 0) {
                                priceRpcDTO.setItemInCount(priceRpcDTO.getItemInCount() - 1);
                            }
                            differPriceRpcDTOS.add(differPriceRpcDTO);
                        }
                    }
                });
                if (!CollectionUtils.isEmpty(differPriceRpcDTOS)) {
                    subPriceRpcDTOS.addAll(differPriceRpcDTOS);
                }
            } else if (couponDO.getCouponMethod().equals(COUPON_METHOD_2)) {
                subPriceRpcDTOS.forEach(priceRpcDTO -> {
                    priceRpcDTO.setCouponMethod(couponDO.getCouponMethod());
                    priceRpcDTO.setDeliveryFeeFlag(couponDO.getDeliveryFeeFlag());
                    priceRpcDTO.setActualPrice(priceRpcDTO.getSalePrice().multiply(couponDO.getDiscount())
                        .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                });
            } else if (couponDO.getCouponMethod().equals(COUPON_METHOD_3)) {
                List<GoodsItemPromotionPriceRpcDTO> differPriceRpcDTOS = new ArrayList<>();
                subPriceRpcDTOS.forEach(priceRpcDTO -> {
                    priceRpcDTO.setCouponMethod(couponDO.getCouponMethod());
                    priceRpcDTO.setDeliveryFeeFlag(couponDO.getDeliveryFeeFlag());
                    if (couponDO.getDeliveryFeeFlag().equals(DELIVERY_FEE_FLAG_1)) {
                        priceRpcDTO.setActualPrice(couponDO.getDeliveryFee());
                    } else {
                        priceRpcDTO.setActualPrice(new BigDecimal(0));
                    }
                    // 兑换优惠券只支持兑换一个，多个需价格需拆分
                    if (priceRpcDTO.getItemCount() > 1) {
                        GoodsItemPromotionPriceRpcDTO differPriceRpcDTO = new GoodsItemPromotionPriceRpcDTO();
                        BeanUtils.copyProperties(priceRpcDTO, differPriceRpcDTO);
                        priceRpcDTO.setItemCount(1);
                        differPriceRpcDTO.setCouponNo(null);
                        differPriceRpcDTO.setCouponMethod(null);
                        differPriceRpcDTO.setDeliveryFeeFlag(null);
                        differPriceRpcDTO.setItemCount(differPriceRpcDTO.getItemInCount() - 1);
                        differPriceRpcDTO.setActualPrice(differPriceRpcDTO.getSalePrice());
                        differPriceRpcDTOS.add(differPriceRpcDTO);
                    }
                });
                if (!CollectionUtils.isEmpty(differPriceRpcDTOS)) {
                    subPriceRpcDTOS.addAll(differPriceRpcDTOS);
                }
            }
            priceRpcDTOS.addAll(subPriceRpcDTOS);
        });
        return priceRpcDTOS;
    }

    public static List<GoodsItemRpcQry> toGoodsItemRpcQryList(List<GoodsItemPriceRpcQry> noGoodsItemPromotions) {
        List<Integer> itemIds = new ArrayList<>();
        List<GoodsItemRpcQry> rpcQrys = new ArrayList<>();
        noGoodsItemPromotions.forEach(noGoodsItemPromotion -> {
            if (!itemIds.contains(noGoodsItemPromotion.getItemId())) {
                GoodsItemRpcQry rpcQry = new GoodsItemRpcQry();
                rpcQry.setGoodsId(noGoodsItemPromotion.getGoodsId());
                rpcQry.setItemId(noGoodsItemPromotion.getItemId());
                rpcQrys.add(rpcQry);
            }
        });
        return rpcQrys;
    }

    public static void getGoodsItemPromotion(List<GoodsItemPriceRpcQry> noGoodsItemPromotions,
        List<GoodsItemPromotionRpcDTO> goodsItemPromotions) {
        Map<Integer, GoodsItemPromotionRpcDTO> goodsItemPromotionMap = goodsItemPromotions.stream()
            .collect(Collectors.toMap(GoodsItemPromotionRpcDTO::getId, goodsItemPromotion -> goodsItemPromotion));
        Map<PromotionRuleRpcDTO, List<GoodsItemPriceRpcQry>> promotionRpcDTOListMap = new HashMap<>();
        for (GoodsItemPriceRpcQry noGoodsItemPromotion : noGoodsItemPromotions) {
            GoodsItemPromotionRpcDTO rpcDTO = goodsItemPromotionMap.get(noGoodsItemPromotion.getItemId());
            List<GoodsItemGroupSeckillRpcDTO> groupSeckills = rpcDTO.getGroupSeckills();
            List<PromotionRpcDTO> promotions = rpcDTO.getPromotions();
            if (!CollectionUtils.isEmpty(groupSeckills)) {
                noGoodsItemPromotion.setSpellGroupId(groupSeckills.get(0).getGroupSeckillId());
            } else if (!CollectionUtils.isEmpty(promotions)) {
                // 促销活动默认获取第一个活动
                List<PromotionRuleRpcDTO> rules = new ArrayList<>();
                promotions.forEach(promotion -> {
                    rules.addAll(promotion.getRules());
                });
                PromotionRuleRpcDTO ruleRpcDTO =
                    rules.stream().max(Comparator.comparing(PromotionRuleRpcDTO::getRuleType)).get();
                List<GoodsItemPriceRpcQry> goodsItems = promotionRpcDTOListMap.get(ruleRpcDTO);
                if (goodsItems == null) {
                    goodsItems = new ArrayList<>();
                    promotionRpcDTOListMap.put(ruleRpcDTO, goodsItems);
                }
                goodsItems.add(noGoodsItemPromotion);
            }
        }
        // 计算满足条件的活动
        if (!CollectionUtils.isEmpty(promotionRpcDTOListMap)) {
            promotionRpcDTOListMap.forEach((ruleRpcDTO, goodsItems) -> {
                // 是否达到条件(最佳条件)
                AtomicReference<PromotionRuleConditionRpcDTO> selectCondition = new AtomicReference<>(null);
                List<PromotionRuleConditionRpcDTO> conditions = ruleRpcDTO.getConditions();
                // 是否累计
                if (ruleRpcDTO.getAddUpFlag().equals(Constant.ADD_UP_FLAG_1)) {
                    // 按金额还是数量
                    if (ruleRpcDTO.getMoneyOrCount().equals(Constant.MONEY)) {
                        double sum = goodsItems.stream()
                            .mapToDouble(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getSalePrice().doubleValue()
                                * (goodsPromotionRpcDTO.getItemCount()))
                            .sum();

                        conditions.forEach(condition -> {
                            if (sum >= condition.getOneBuyMoney().doubleValue()) {
                                if (selectCondition.get() == null || selectCondition.get().getOneBuyMoney()
                                    .doubleValue() < condition.getOneBuyMoney().doubleValue()) {
                                    selectCondition.set(condition);
                                }
                            }
                        });
                    } else {
                        double count = goodsItems.stream()
                            .mapToInt(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemCount()).sum();
                        conditions.forEach(condition -> {
                            if (count >= condition.getOneBuyCount().intValue()) {
                                if (selectCondition.get() == null || selectCondition.get().getOneBuyCount()
                                    .intValue() < condition.getOneBuyCount().intValue()) {
                                    selectCondition.set(condition);
                                }
                            }
                        });
                    }
                } else {
                    goodsItems.forEach(goodsItem -> {
                        // 按金额还是数量
                        if (ruleRpcDTO.getMoneyOrCount().equals(Constant.MONEY)) {
                            double sum = goodsItem.getSalePrice().doubleValue() * goodsItem.getItemCount();
                            // 是否达到条件
                            conditions.forEach(condition -> {
                                if (sum >= condition.getOneBuyMoney().doubleValue()) {
                                    if (selectCondition.get() == null || selectCondition.get().getOneBuyMoney()
                                        .doubleValue() < condition.getOneBuyMoney().doubleValue()) {
                                        selectCondition.set(condition);
                                    }
                                }
                            });
                        } else {
                            double count = goodsItem.getItemCount();
                            // 是否达到条件
                            conditions.forEach(condition -> {
                                if (count >= condition.getOneBuyCount().intValue()) {
                                    if (selectCondition.get() == null || selectCondition.get().getOneBuyCount()
                                        .intValue() < condition.getOneBuyCount().intValue()) {
                                        selectCondition.set(condition);
                                    }
                                }
                            });
                        }
                    });
                }
                if (selectCondition.get() != null) {
                    goodsItems.forEach(goodsItem -> {
                        if (ruleRpcDTO.getRuleType().equals(RULE_TYPE_1)) {
                            goodsItem.setOrderPromotionId(selectCondition.get().getId());
                        } else {
                            goodsItem.setGoodsPromotionId(selectCondition.get().getId());
                        }
                    });
                }
            });
        }
    }

    public static OrderPromotionRpcDTO toOrderPromotionRpcDTO(List<PromotionRuleConditionDO> conditionDOS,
        List<PromotionRuleDO> ruleDOS, List<PromotionDO> promotionDOS, List<GroupSeckillDO> groupSeckillDOS) {
        OrderPromotionRpcDTO rpcDTO = new OrderPromotionRpcDTO();
        List<PromotionRpcDTO> promotions = new ArrayList<>();
        rpcDTO.setPromotions(promotions);
        List<GoodsItemGroupSeckillRpcDTO> spellGroups = new ArrayList<>();
        rpcDTO.setSpellGroups(spellGroups);
        // 促销活动
        if (!CollectionUtils.isEmpty(conditionDOS)) {
            Map<Integer, PromotionRuleDO> ruleDOMap =
                ruleDOS.stream().collect(Collectors.toMap(PromotionRuleDO::getId, ruleDO -> ruleDO));
            Map<Integer, PromotionDO> promotionDOMap =
                promotionDOS.stream().collect(Collectors.toMap(PromotionDO::getId, promotionDO -> promotionDO));
            Map<Integer, PromotionRpcDTO> promotionRpcDTOMap = new HashMap<>();
            Map<Integer, PromotionRuleRpcDTO> rpcDTOMap = new HashMap<>();
            conditionDOS.forEach(conditionDO -> {
                PromotionRpcDTO promotionRpcDTO = promotionRpcDTOMap.get(conditionDO.getPromotionId());
                if (promotionRpcDTO == null) {
                    promotionRpcDTO = new PromotionRpcDTO();
                    PromotionDO promotionDO = promotionDOMap.get(conditionDO.getPromotionId());
                    BeanUtils.copyProperties(promotionDO, promotionRpcDTO);
                    promotionRpcDTOMap.put(promotionRpcDTO.getId(), promotionRpcDTO);
                    promotions.add(promotionRpcDTO);
                }
                List<PromotionRuleRpcDTO> rules = promotionRpcDTO.getRules();
                if (rules == null) {
                    rules = new ArrayList<>();
                    promotionRpcDTO.setRules(rules);
                }
                // 促销活动规则
                PromotionRuleRpcDTO ruleRpcDTO = rpcDTOMap.get(conditionDO.getPromotionRuleId());
                if (ruleRpcDTO == null) {
                    ruleRpcDTO = new PromotionRuleRpcDTO();
                    PromotionRuleDO ruleDO = ruleDOMap.get(conditionDO.getPromotionRuleId());
                    BeanUtils.copyProperties(ruleDO, ruleRpcDTO);
                    rpcDTOMap.put(ruleRpcDTO.getId(), ruleRpcDTO);
                    rules.add(ruleRpcDTO);
                }
                List<PromotionRuleConditionRpcDTO> conditions = ruleRpcDTO.getConditions();
                if (conditions == null) {
                    conditions = new ArrayList<>();
                    ruleRpcDTO.setConditions(conditions);
                }
                // 促销活动条件
                PromotionRuleConditionRpcDTO conditionRpcDTO = new PromotionRuleConditionRpcDTO();
                BeanUtils.copyProperties(conditionDO, conditionRpcDTO);
                conditions.add(conditionRpcDTO);
            });
        }
        if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
            groupSeckillDOS.forEach(groupSeckillDO -> {
                GoodsItemGroupSeckillRpcDTO seckillRpcDTO = new GoodsItemGroupSeckillRpcDTO();
                BeanUtils.copyProperties(groupSeckillDO, seckillRpcDTO);
                spellGroups.add(seckillRpcDTO);
            });
        }
        return rpcDTO;
    }

}
