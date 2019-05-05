package com.bat.promotion.service.user.convertor;

import static com.bat.promotion.service.common.Constant.*;
import static com.bat.promotion.service.user.executor.ErrorCode.B_PROMOTION_COUPON_RECEIVED;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.dao.coupon.dataobject.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.UserGoodsRpcDTO;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponDTO;
import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.dao.coupon.dataobject.*;
import com.bat.promotion.dao.groupseckill.dataobject.UserGoodsItemGroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.UserGroupSeckillGoodsDO;
import com.bat.promotion.dao.promotion.dataobject.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 8:44
 */
public class UserConvertor {

    public static List<UserPromotionDTO> toUserPromotionDTOList(List<UserPromotionDO> promotionDOS) {
        List<UserPromotionDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(promotionDOS)) {
            promotionDOS.forEach(userPromotionDO -> {
                UserPromotionDTO dto = new UserPromotionDTO();
                BeanUtils.copyProperties(userPromotionDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<UserPromotionRuleDTO> toUserPromotionRuleDTOList(List<PromotionRuleDO> ruleDOS,
                                                                        List<PromotionRuleConditionDO> conditionDOS, List<PromotionRuleConditionSpecialDO> specialDOS,
                                                                        List<PromotionRuleConditionPresentDO> presentDOS, List<GoodsItemRpcDTO> goodsItems) {
        Map<Integer, GoodsItemRpcDTO> goodsItemRpcDTOMap = new HashMap<>();
        Map<Integer, List<PromotionRuleConditionDO>> conditionGroup = new HashMap<>();
        Map<Integer, List<PromotionRuleConditionSpecialDO>> specialGroup = new HashMap<>();
        Map<Integer, List<PromotionRuleConditionPresentDO>> presentGroup = new HashMap<>();
        List<UserPromotionRuleDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItems)) {
            goodsItemRpcDTOMap
                .putAll(goodsItems.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, goodsItem -> goodsItem)));
        }
        if (!CollectionUtils.isEmpty(conditionDOS)) {
            conditionGroup.putAll(
                conditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId)));
        }
        if (!CollectionUtils.isEmpty(specialDOS)) {
            specialGroup.putAll(specialDOS.stream()
                .collect(Collectors.groupingBy(PromotionRuleConditionSpecialDO::getPromotionRuleConditionId)));
        }
        if (!CollectionUtils.isEmpty(presentDOS)) {
            presentGroup.putAll(presentDOS.stream()
                .collect(Collectors.groupingBy(PromotionRuleConditionPresentDO::getPromotionRuleConditionId)));
        }
        if (!CollectionUtils.isEmpty(ruleDOS)) {
            ruleDOS.forEach(ruleDO -> {
                UserPromotionRuleDTO dto = new UserPromotionRuleDTO();
                BeanUtils.copyProperties(ruleDO, dto);
                dtos.add(dto);
                List<PromotionRuleConditionDO> ruleConditionDOS = conditionGroup.get(dto.getId());
                List<UserPromotionRuleConditionDTO> conditionDTOS = new ArrayList<>();
                dto.setConditions(conditionDTOS);
                if (!CollectionUtils.isEmpty(ruleConditionDOS)) {
                    ruleConditionDOS.forEach(ruleConditionDO -> {
                        UserPromotionRuleConditionDTO conditionDTO = new UserPromotionRuleConditionDTO();
                        BeanUtils.copyProperties(ruleConditionDO, conditionDTO);
                        conditionDTOS.add(conditionDTO);
                        List<PromotionRuleConditionSpecialDO> conditionSpecialDOS =
                            specialGroup.get(conditionDTO.getId());
                        if (!CollectionUtils.isEmpty(conditionSpecialDOS)) {
                            List<UserPromotionRuleConditionSpecialDTO> specialDTOS = new ArrayList<>();
                            conditionDTO.setSpecials(specialDTOS);
                            conditionSpecialDOS.forEach(conditionSpecialDO -> {
                                UserPromotionRuleConditionSpecialDTO specialDTO =
                                    new UserPromotionRuleConditionSpecialDTO();
                                BeanUtils.copyProperties(conditionSpecialDO, specialDTO);
                                specialDTOS.add(specialDTO);
                            });
                        }
                        List<PromotionRuleConditionPresentDO> conditionPresentDOS =
                            presentGroup.get(conditionDTO.getId());
                        if (!CollectionUtils.isEmpty(conditionPresentDOS)) {
                            List<UserPromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
                            conditionDTO.setPresents(presentDTOS);
                            conditionPresentDOS.forEach(conditionPresentDO -> {
                                UserPromotionRuleConditionPresentDTO presentDTO =
                                    new UserPromotionRuleConditionPresentDTO();
                                BeanUtils.copyProperties(conditionPresentDO, presentDTO);
                                GoodsItemRpcDTO goodsItemRpcDTO = goodsItemRpcDTOMap.get(presentDTO.getItemId());
                                BeanUtils.copyProperties(goodsItemRpcDTO, presentDTO);
                                presentDTOS.add(presentDTO);
                            });
                        }
                    });
                }
            });
        }
        return dtos;
    }

    public static List<UserPromotionRuleGoodsDTO> toUserPromotionRuleGoodsDTOList(Integer promotionId,
                                                                                  Integer promotionRuleId, List<GoodsItemRpcDTO> rpcDTOS) {
        List<UserPromotionRuleGoodsDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOS.forEach(rpcDTO -> {
                UserPromotionRuleGoodsDTO dto = new UserPromotionRuleGoodsDTO();
                BeanUtils.copyProperties(rpcDTO, dto);
                dto.setPromotionId(promotionId);
                dto.setPromotionRuleId(promotionRuleId);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<UserGroupSeckillGoodsDTO> toUserGroupSeckillGoodsDTOList(List<UserGroupSeckillGoodsDO> goodsDOS,
                                                                                List<UserGoodsRpcDTO> goodsRpcDTOS) {
        List<UserGroupSeckillGoodsDTO> dtos = new ArrayList<>();
        Map<Integer, List<UserGroupSeckillGoodsDO>> goodsDOListMap =
            goodsDOS.stream().collect(Collectors.groupingBy(UserGroupSeckillGoodsDO::getGoodsId));
        if (!CollectionUtils.isEmpty(goodsRpcDTOS)) {
            goodsRpcDTOS.forEach(goodsRpcDTO -> {
                List<UserGroupSeckillGoodsDO> goodsDOList = goodsDOListMap.get(goodsRpcDTO.getId());
                if (!CollectionUtils.isEmpty(goodsDOList)) {
                    UserGroupSeckillGoodsDTO dto = new UserGroupSeckillGoodsDTO();
                    BeanUtils.copyProperties(goodsRpcDTO, dto);
                    goodsDOList.forEach(goods -> {
                        // BeanUtils.copyProperties(goods, dto);
                        if (dto.getStartTime() == null) {
                            dto.setStartTime(goods.getStartTime());
                        }
                        if (dto.getEndTime() == null) {
                            dto.setEndTime(goods.getEndTime());
                        }
                        if (dto.getGroupSeckillId() == null) {
                            dto.setGroupSeckillId(goods.getGroupSeckillId());
                        }
                        if (dto.getMaxNum() == null) {
                            dto.setMaxNum(goods.getMaxNum());
                        } else if (dto.getMaxNum() != 0 && goods.getMaxNum() != 0) {
                            dto.setMaxNum(dto.getMaxNum().intValue() + goods.getMaxNum().intValue());
                        } else {
                            dto.setMaxNum(0);
                        }
                        if (dto.getVirtualSum() == null) {
                            dto.setVirtualSum(goods.getVirtualSum());
                        } else if (goods.getVirtualSum() != null) {
                            dto.setVirtualSum(dto.getVirtualSum().intValue() + goods.getVirtualSum().intValue());
                        }
                        if (dto.getRealSum() == null) {
                            dto.setRealSum(goods.getRealSum());
                        } else if (goods.getRealSum() != null) {
                            dto.setRealSum(dto.getRealSum().intValue() + goods.getRealSum().intValue());
                        }
                        if (dto.getGroupSeckillPrice() == null) {
                            dto.setGroupSeckillPrice(goods.getGroupSeckillPrice());
                        } else if (goods.getGroupSeckillPrice().doubleValue() < dto.getGroupSeckillPrice()
                            .doubleValue()) {
                            dto.setGroupSeckillPrice(goods.getGroupSeckillPrice());
                        }
                    });
                    dtos.add(dto);
                }
            });
        }
        return dtos;
    }

    public static UserGoodsPromotionDTO toUserGoodsItemPromotionDTO(List<UserPromotionDO> promotionDOS,
        List<PromotionRuleDO> ruleDOS, List<PromotionRuleConditionDO> ruleConditionDOS,
        List<PromotionRuleConditionPresentDO> conditionPresentDOS, List<GoodsItemRpcDTO> itemRpcDTOS,
        List<PromotionRuleConditionSpecialDO> conditionSpecialDOS, List<PromotionRuleGoodsDO> ruleGoodsDOS,
        List<UserGoodsItemGroupSeckillDO> groupSeckillDOS, Integer goodsId) {
        UserGoodsPromotionDTO dto = new UserGoodsPromotionDTO();
        List<UserPromotionDTO> orderPromotions = new ArrayList<>();
        dto.setOrderPromotions(orderPromotions);
        List<UserPromotionDTO> goodsPromotions = new ArrayList<>();
        dto.setGoodsPromotions(goodsPromotions);
        List<UserGoodsItemPromotionDTO> goodsItemPromotions = new ArrayList<>();
        dto.setGoodsItemPromotions(goodsItemPromotions);
        dto.setId(goodsId);
        Map<Integer, UserPromotionDTO> orderPromotionDTOMap = new HashMap<>();
        Map<Integer, UserPromotionDTO> goodsPromotionDTOMap = new HashMap<>();
        Map<String, UserPromotionDTO> itemPromotionDTOMap = new HashMap<>();
        Map<Integer, UserGoodsItemPromotionDTO> goodsItemPromotionDTOMap = new HashMap<>();
        Map<Integer, UserPromotionDO> promotionDOMap =
            promotionDOS.stream().collect(Collectors.toMap(UserPromotionDO::getId, promotionDO -> promotionDO));
        dto.setId(goodsId);
        Map<Integer, List<PromotionRuleConditionDO>> conditionDOSMap =
            ruleConditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId));
        Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
            itemRpcDTOMap.putAll(
                itemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, itemRpcDTO -> itemRpcDTO)));
        }
        Map<Integer, PromotionRuleDO> ruleDOMap =
            ruleDOS.stream().collect(Collectors.toMap(PromotionRuleDO::getId, ruleDO -> ruleDO));
        // 处理订单促销活动
        if (!CollectionUtils.isEmpty(ruleDOS)) {
            ruleDOS.forEach(ruleDO -> {
                if (ruleDO.getRuleType().equals(RULE_TYPE_1)) {// 订单规则
                    UserPromotionRuleDTO ruleDTO = new UserPromotionRuleDTO();
                    BeanUtils.copyProperties(ruleDO, ruleDTO);
                    // 处理促销活动
                    UserPromotionDO userPromotionDO = promotionDOMap.get(ruleDO.getPromotionId());
                    UserPromotionDTO promotionDTO = orderPromotionDTOMap.get(userPromotionDO.getId());
                    if (promotionDTO == null) {
                        promotionDTO = new UserPromotionDTO();
                        BeanUtils.copyProperties(userPromotionDO, promotionDTO);
                        orderPromotionDTOMap.put(promotionDTO.getId(), promotionDTO);
                    }
                    orderPromotions.add(promotionDTO);
                    // 处理促销活动规则
                    List<UserPromotionRuleDTO> rules = promotionDTO.getRules();
                    if (rules == null) {
                        rules = new ArrayList<>();
                        promotionDTO.setRules(rules);
                    }
                    rules.add(ruleDTO);
                    // 处理促销活动规则条件
                    List<UserPromotionRuleConditionDTO> ruleConditionDTOS = new ArrayList<>();
                    ruleDTO.setConditions(ruleConditionDTOS);
                    List<PromotionRuleConditionDO> conditionDOS = conditionDOSMap.get(ruleDO.getId());
                    conditionDOS.forEach(conditionDO -> {
                        UserPromotionRuleConditionDTO ruleConditionDTO = new UserPromotionRuleConditionDTO();
                        BeanUtils.copyProperties(conditionDO, ruleConditionDTO);
                        ruleConditionDTOS.add(ruleConditionDTO);
                        // 处理条件赠品
                        if (conditionDO.getReduceOrPresent() != null
                            && conditionDO.getReduceOrPresent().equals(CONDITION_PRESENT)
                            && !CollectionUtils.isEmpty(conditionPresentDOS)) {
                            List<PromotionRuleConditionPresentDO> presentDOS =
                                conditionPresentDOS
                                    .stream().filter(conditionPresentDO -> conditionPresentDO
                                        .getPromotionRuleConditionId().equals(ruleConditionDTO.getId()))
                                    .collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(presentDOS)) {
                                List<UserPromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
                                ruleConditionDTO.setPresents(presentDTOS);
                                presentDOS.forEach(presentDO -> {
                                    UserPromotionRuleConditionPresentDTO presentDTO =
                                        new UserPromotionRuleConditionPresentDTO();
                                    GoodsItemRpcDTO goodsItemRpcDTO = itemRpcDTOMap.get(presentDO.getItemId());
                                    BeanUtils.copyProperties(goodsItemRpcDTO, presentDTO);
                                    BeanUtils.copyProperties(presentDO, presentDTO);
                                    presentDTOS.add(presentDTO);
                                });
                            }
                        }
                    });
                }
            });
        }
        // 处理商品或货品促销活动
        if (!CollectionUtils.isEmpty(ruleGoodsDOS)) {
            ruleGoodsDOS.forEach(ruleGoodsDO -> {
                PromotionRuleDO ruleDO = ruleDOMap.get(ruleGoodsDO.getPromotionRuleId());
                UserPromotionRuleDTO ruleDTO = new UserPromotionRuleDTO();
                BeanUtils.copyProperties(ruleDO, ruleDTO);
                // 处理促销活动
                UserPromotionDO userPromotionDO = promotionDOMap.get(ruleDO.getPromotionId());
                UserPromotionDTO promotionDTO = null;
                if (ruleDO.getRuleType().equals(RULE_TYPE_2)) {// 商品规则
                    promotionDTO = goodsPromotionDTOMap.get(userPromotionDO.getId());
                    if (promotionDTO == null) {
                        promotionDTO = new UserPromotionDTO();
                        BeanUtils.copyProperties(userPromotionDO, promotionDTO);
                        goodsPromotionDTOMap.put(promotionDTO.getId(), promotionDTO);
                    }
                    goodsPromotions.add(promotionDTO);
                } else {
                    String promotionItemId =
                        String.valueOf(userPromotionDO.getId() + ",") + String.valueOf(ruleGoodsDO.getItemId());
                    promotionDTO = itemPromotionDTOMap.get(promotionItemId);
                    if (promotionDTO == null) {
                        promotionDTO = new UserPromotionDTO();
                        BeanUtils.copyProperties(userPromotionDO, promotionDTO);
                        itemPromotionDTOMap.put(promotionItemId, promotionDTO);
                    }
                    UserGoodsItemPromotionDTO goodsItemPromotion =
                        goodsItemPromotionDTOMap.get(ruleGoodsDO.getItemId());
                    if (goodsItemPromotion == null) {
                        goodsItemPromotion = new UserGoodsItemPromotionDTO();
                        goodsItemPromotion.setId(ruleGoodsDO.getItemId());
                        goodsItemPromotions.add(goodsItemPromotion);
                        goodsItemPromotionDTOMap.put(ruleGoodsDO.getItemId(), goodsItemPromotion);
                    }
                    List<UserPromotionDTO> promotionDTOS = goodsItemPromotion.getPromotions();
                    if (promotionDTOS == null) {
                        promotionDTOS = new ArrayList<>();
                        goodsItemPromotion.setPromotions(promotionDTOS);
                    }
                    promotionDTOS.add(promotionDTO);
                }
                // 处理促销活动规则
                List<UserPromotionRuleDTO> rules = promotionDTO.getRules();
                if (rules == null) {
                    rules = new ArrayList<>();
                    promotionDTO.setRules(rules);
                }
                rules.add(ruleDTO);
                // 处理促销活动规则条件
                List<UserPromotionRuleConditionDTO> ruleConditionDTOS = new ArrayList<>();
                ruleDTO.setConditions(ruleConditionDTOS);
                List<PromotionRuleConditionDO> conditionDOS = conditionDOSMap.get(ruleDO.getId());
                conditionDOS.forEach(conditionDO -> {
                    UserPromotionRuleConditionDTO ruleConditionDTO = new UserPromotionRuleConditionDTO();
                    BeanUtils.copyProperties(conditionDO, ruleConditionDTO);
                    ruleConditionDTOS.add(ruleConditionDTO);
                    // 处理条件特价
                    if (ruleDTO.getRuleType().equals(RULE_TYPE_3) && conditionDO.getSpecialFlag() != null
                        && conditionDO.getSpecialFlag().equals(SPECIAL_FLAG_1)) {
                        Optional<PromotionRuleConditionSpecialDO> optional = conditionSpecialDOS.stream()
                            .filter(conditionSpecialDO -> conditionSpecialDO.getPromotionRuleConditionId()
                                .equals(ruleConditionDTO.getId())
                                && conditionSpecialDO.getItemId().equals(ruleGoodsDO.getItemId()))
                            .findFirst();
                        if (optional != null && optional.isPresent()) {
                            ruleConditionDTO.setSpecialPrice(optional.get().getSpecialPrice());
                        }
                    }
                    // 处理条件赠品
                    if (conditionDO.getReduceOrPresent() != null
                        && conditionDO.getReduceOrPresent().equals(CONDITION_PRESENT)
                        && !CollectionUtils.isEmpty(conditionPresentDOS)) {
                        List<
                            PromotionRuleConditionPresentDO> presentDOS =
                                conditionPresentDOS
                                    .stream().filter(conditionPresentDO -> conditionPresentDO
                                        .getPromotionRuleConditionId().equals(ruleConditionDTO.getId()))
                                    .collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(presentDOS)) {
                            List<UserPromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
                            ruleConditionDTO.setPresents(presentDTOS);
                            presentDOS.forEach(presentDO -> {
                                UserPromotionRuleConditionPresentDTO presentDTO =
                                    new UserPromotionRuleConditionPresentDTO();
                                GoodsItemRpcDTO goodsItemRpcDTO = itemRpcDTOMap.get(presentDO.getItemId());
                                BeanUtils.copyProperties(goodsItemRpcDTO, presentDTO);
                                BeanUtils.copyProperties(presentDO, presentDTO);
                                presentDTOS.add(presentDTO);
                            });
                        }
                    }
                });
            });
        }
        // 处理拼团秒杀活动
        if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
            groupSeckillDOS.forEach(groupSeckillDO -> {
                UserGoodsItemGroupSeckillDTO groupSeckillDTO = new UserGoodsItemGroupSeckillDTO();
                BeanUtils.copyProperties(groupSeckillDO, groupSeckillDTO);
                UserGoodsItemPromotionDTO goodsItemPromotion = goodsItemPromotionDTOMap.get(groupSeckillDO.getItemId());
                if (goodsItemPromotion == null) {
                    goodsItemPromotion = new UserGoodsItemPromotionDTO();
                    goodsItemPromotion.setId(groupSeckillDO.getItemId());
                    goodsItemPromotions.add(goodsItemPromotion);
                    goodsItemPromotionDTOMap.put(goodsItemPromotion.getId(), goodsItemPromotion);
                }
                List<UserGoodsItemGroupSeckillDTO> groupSeckills = goodsItemPromotion.getGroupSeckills();
                if (groupSeckills == null) {
                    groupSeckills = new ArrayList<>();
                    goodsItemPromotion.setGroupSeckills(groupSeckills);
                }
                groupSeckills.add(groupSeckillDTO);
            });
        }
        return dto;
    }

    private static void doPromotionRule() {

    }

    public static List<UserPromotionDTO> toUserPromotionDTOList(List<UserPromotionDO> promotionDOS,
        List<PromotionRuleDO> ruleDOS, List<PromotionRuleConditionDO> ruleConditionDOS,
        List<PromotionRuleConditionPresentDO> conditionPresentDOS, List<GoodsItemRpcDTO> itemRpcDTOS) {
        List<UserPromotionDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(promotionDOS)) {
            Map<Integer, List<PromotionRuleDO>> ruleDOListMap =
                ruleDOS.stream().collect(Collectors.groupingBy(PromotionRuleDO::getPromotionId));
            Map<Integer, List<PromotionRuleConditionDO>> conditionDOSMap =
                ruleConditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId));
            Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap =
                itemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, itemRpcDTO -> itemRpcDTO));
            promotionDOS.forEach(promotionDO -> {
                UserPromotionDTO dto = new UserPromotionDTO();
                BeanUtils.copyProperties(promotionDO, dto);
                dtos.add(dto);
                List<PromotionRuleDO> ruleDOList = ruleDOListMap.get(dto.getId());
                List<UserPromotionRuleDTO> ruleDTOS = new ArrayList<>();
                dto.setRules(ruleDTOS);
                ruleDOList.forEach(ruleDO -> {
                    UserPromotionRuleDTO ruleDTO = new UserPromotionRuleDTO();
                    BeanUtils.copyProperties(ruleDO, ruleDTO);
                    ruleDTOS.add(ruleDTO);
                    List<PromotionRuleConditionDO> ruleConditionDOSList = conditionDOSMap.get(ruleDTO.getId());
                    List<UserPromotionRuleConditionDTO> conditionDTOS = new ArrayList<>();
                    ruleDTO.setConditions(conditionDTOS);
                    ruleConditionDOSList.forEach(ruleConditionDO -> {
                        UserPromotionRuleConditionDTO conditionDTO = new UserPromotionRuleConditionDTO();
                        BeanUtils.copyProperties(ruleConditionDO, conditionDTO);
                        conditionDTOS.add(conditionDTO);
                        // 处理条件赠品
                        if (ruleConditionDO.getReduceOrPresent() != null
                            && ruleConditionDO.getReduceOrPresent().equals(CONDITION_PRESENT)
                            && !CollectionUtils.isEmpty(conditionPresentDOS)) {
                            List<PromotionRuleConditionPresentDO> presentDOS =
                                conditionPresentDOS
                                    .stream().filter(conditionPresentDO -> conditionPresentDO
                                        .getPromotionRuleConditionId().equals(ruleConditionDO.getId()))
                                    .collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(presentDOS)) {
                                List<UserPromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
                                conditionDTO.setPresents(presentDTOS);
                                presentDOS.forEach(presentDO -> {
                                    UserPromotionRuleConditionPresentDTO presentDTO =
                                        new UserPromotionRuleConditionPresentDTO();
                                    GoodsItemRpcDTO goodsItemRpcDTO = itemRpcDTOMap.get(presentDO.getItemId());
                                    BeanUtils.copyProperties(goodsItemRpcDTO, presentDTO);
                                    BeanUtils.copyProperties(presentDO, presentDTO);
                                    presentDTOS.add(presentDTO);
                                });
                            }
                        }
                    });
                });
            });
        }
        return dtos;
    }

    public static List<UserCustomerCouponDTO> toUserCustomerCouponDTOList(
            List<UserCustomerCouponDO> userCustomerCouponDOS, List<CouponMaterialRelevanceDO> materialRelevanceDOS,
            List<CouponModelRelevanceDO> modelRelevanceDOS) {
        List<UserCustomerCouponDTO> dtos = new ArrayList<>();
        Map<Integer, List<CouponMaterialRelevanceDO>> materialsMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(materialRelevanceDOS)) {
            materialsMap.putAll(
                materialRelevanceDOS.stream().collect(Collectors.groupingBy(CouponMaterialRelevanceDO::getCouponId)));
        }
        Map<Integer, List<CouponModelRelevanceDO>> modelsMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(modelRelevanceDOS)) {
            modelsMap
                .putAll(modelRelevanceDOS.stream().collect(Collectors.groupingBy(CouponModelRelevanceDO::getCouponId)));
        }
        if (!CollectionUtils.isEmpty(userCustomerCouponDOS)) {
            userCustomerCouponDOS.forEach(userCustomerCouponDO -> {
                UserCustomerCouponDTO dto = new UserCustomerCouponDTO();
                BeanUtils.copyProperties(userCustomerCouponDO, dto);
                List<CouponMaterialRelevanceDO> materials = materialsMap.get(dto.getCouponId());
                if (!CollectionUtils.isEmpty(materials)) {
                    dto.setMaterialIds(
                        materials.stream().map(CouponMaterialRelevanceDO::getMaterialId).collect(Collectors.toList()));
                }
                List<CouponModelRelevanceDO> models = modelsMap.get(dto.getCouponId());
                if (!CollectionUtils.isEmpty(models)) {
                    dto.setModelIds(
                        models.stream().map(CouponModelRelevanceDO::getModelId).collect(Collectors.toList()));
                }
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static UserCustomerCouponDTO toOptimalUserCustomerCouponDTO(List<UserCustomerCouponDO> userCustomerCouponDOS,
        BigDecimal totalAmount) {
        UserCustomerCouponDTO dto = null;
        AtomicReference<UserCustomerCouponDO> optimalCoupon = new AtomicReference<>(null);
        AtomicReference<BigDecimal> optimalAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));
        if (!CollectionUtils.isEmpty(userCustomerCouponDOS)) {
            for (UserCustomerCouponDO userCustomerCouponDO : userCustomerCouponDOS) {
                // 1满减 2满折 3兑换 对比最优
                if (optimalCoupon.get() == null) {
                    optimalCoupon.set(userCustomerCouponDO);
                    if (userCustomerCouponDO.getCouponMethod().equals(COUPON_METHOD_1)) {
                        optimalAmount.set(userCustomerCouponDO.getReduction());
                    } else if (userCustomerCouponDO.getCouponMethod().equals(COUPON_METHOD_2)) {
                        optimalAmount
                            .set(totalAmount.subtract(totalAmount.multiply(userCustomerCouponDO.getDiscount())));
                    } else {
                        optimalCoupon.set(userCustomerCouponDO);
                        break;
                    }
                } else {
                    if (userCustomerCouponDO.getCouponMethod().equals(COUPON_METHOD_1)
                        && optimalAmount.get().doubleValue() < userCustomerCouponDO.getReduction().doubleValue()) {
                        optimalAmount.set(userCustomerCouponDO.getReduction());
                        optimalCoupon.set(userCustomerCouponDO);
                    } else if (userCustomerCouponDO.getCouponMethod().equals(COUPON_METHOD_2) && optimalAmount.get()
                        .doubleValue() < userCustomerCouponDO.getDiscount().doubleValue() * totalAmount.doubleValue()) {
                        optimalAmount
                            .set(totalAmount.subtract(totalAmount.multiply(userCustomerCouponDO.getDiscount())));
                        optimalCoupon.set(userCustomerCouponDO);
                    } else if (userCustomerCouponDO.getCouponMethod().equals(COUPON_METHOD_3)) {
                        // 兑换券邮费 最优
                        optimalCoupon.set(userCustomerCouponDO);
                        break;
                    }
                }
            }
        }
        if (optimalCoupon.get() != null) {
            dto = new UserCustomerCouponDTO();
            BeanUtils.copyProperties(optimalCoupon.get(), dto);
        }
        return dto;
    }

    public static List<CouponCustomerDO> toCouponCustomerDOList(Integer customerId, String customerName,
                                                                Integer distributorId, String platform, String openId, List<CouponDO> couponDOS,
                                                                List<CouponCustomerDO> couponCustomerDOS) {
        Date date = new Date(System.currentTimeMillis());
        List<CouponCustomerDO> customerDOS = new ArrayList<>();
        Map<Integer, List<CouponCustomerDO>> customerDOListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(couponCustomerDOS)) {
            customerDOListMap
                .putAll(couponCustomerDOS.stream().collect(Collectors.groupingBy(CouponCustomerDO::getCouponId)));
        }
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponDOS.forEach(couponDO -> {
                CouponCustomerDO customerDO = new CouponCustomerDO();
                List<CouponCustomerDO> customerDOList = customerDOListMap.get(couponDO.getId());
                customerDO.setPieces(1);
                if (!CollectionUtils.isEmpty(customerDOList)) {
                    Optional<Integer> max = customerDOList.stream().map(CouponCustomerDO::getPieces)
                        .max(Comparator.comparing(Function.identity()));
                    // 调整接口
                    if (max.isPresent() && couponDO.getLimitCount() != null && couponDO.getLimitCount() > 0
                        && max.get() >= couponDO.getLimitCount()) {
                        throw PromotionException.buildException(B_PROMOTION_COUPON_RECEIVED);
                    }
                    customerDO.setPieces(customerDOList.size() + 1);
                }
                if (couponDO.getGeneratedCount() == null) {
                    couponDO.setGeneratedCount(0);
                }
                if (couponDO.getGeneratedCount() >= couponDO.getGenerateCount()) {
                    throw PromotionException.buildException(B_PROMOTION_COUPON_RECEIVED);
                }
                couponDO.setGeneratedCount(couponDO.getGeneratedCount() + 1);
                customerDO.setCouponId(couponDO.getId());
                customerDO.setCouponStatus(couponDO.getCouponStatus());
                customerDO.setCustomerId(customerId);
                customerDO.setCustomerName(customerName);
                customerDO.setDistributorId(distributorId);
                customerDO.setCreateTime(date);
                customerDO.setUpdateTime(date);
                customerDO.setPlatform(platform);
                customerDO.setOpenId(openId);
                customerDOS.add(customerDO);
            });
        }
        return customerDOS;
    }

    public static List<UserPromotionRuleConditionPresentDTO> toUserPromotionRuleConditionPresentDTOList(
        List<PromotionRuleConditionPresentDO> presentDOS, List<GoodsItemRpcDTO> itemRpcDTOS) {
        List<UserPromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
            Map<Integer, PromotionRuleConditionPresentDO> presentDOMap = presentDOS.stream()
                .collect(Collectors.toMap(PromotionRuleConditionPresentDO::getItemId, presentDO -> presentDO));
            itemRpcDTOS.forEach(itemRpcDTO -> {
                UserPromotionRuleConditionPresentDTO presentDTO = new UserPromotionRuleConditionPresentDTO();
                PromotionRuleConditionPresentDO presentDO = presentDOMap.get(itemRpcDTO.getId());
                BeanUtils.copyProperties(itemRpcDTO, presentDTO);
                BeanUtils.copyProperties(presentDO, presentDTO);
                presentDTOS.add(presentDTO);
            });
        }
        return presentDTOS;
    }

}
