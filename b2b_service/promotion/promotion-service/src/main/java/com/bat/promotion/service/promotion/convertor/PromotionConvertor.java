package com.bat.promotion.service.promotion.convertor;

import static com.bat.promotion.service.common.CommonErrorCode.*;
import static com.bat.promotion.service.common.Constant.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.api.promotion.dto.data.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.promotion.dto.ImportPromotionExcelDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.*;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.groupseckill.dto.GroupSeckillCmd;
import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.api.promotion.dto.data.*;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillGoodsDO;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.common.CommonErrorCode;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.api.base.MessageUtils;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:11
 */
public class PromotionConvertor {

    public static PromotionDO toPromotionDO(PromotionCmd cmd, Date date) {
        PromotionDO promotionDO = new PromotionDO();
        BeanUtils.copyProperties(cmd, promotionDO);
        promotionDO.setCreateTime(date);
        promotionDO.setUpdateTime(date);
        return promotionDO;
    }

    public static PromotionDO toPromotionDO(String indexMsg, List<DepartmentRpcDTO> departments,
                                            ImportPromotionExcelDTO dto, Date date, List<PromotionDO> promotions, Map<String, PromotionDO> promotionMap,
                                            Map<String, List<DepartmentRpcDTO>> departmentListMap) {
        String promotionNo = dto.getPromotionNo();
        if (StringUtils.isBlank(promotionNo)) {
            throw PromotionException.buildException(B_IMPORT_PROMOTION_NO_NULL,
                indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_NO_NULL));
        }
        PromotionDO promotion = promotionMap.get(promotionNo);
        if (promotion == null) {
            promotion = new PromotionDO();
            Short promoType = dto.getPromoType();
            if (promoType == null || !promoType.equals(Short.valueOf(PROMOTION_TYPE_2))) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_NO_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_NO_NULL));
            }
            promotion.setPromoType(promoType);
            String name = dto.getName();
            if (StringUtils.isBlank(name)) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_NAME_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_NAME_NULL));
            }
            promotion.setName(name);
            promotion.setNameEn(dto.getNameEn());
            String promoDesc = dto.getPromoDesc();
            if (StringUtils.isBlank(promoDesc)) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_DESCRIBE_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_DESCRIBE_NULL));
            }
            promotion.setPromoDesc(promoDesc);
            Short onWayFlag = dto.getOnWayFlag();
            if (onWayFlag == null || (onWayFlag != 0 && onWayFlag != 1)) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_NO_WAY_ERROR,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_NO_WAY_ERROR));
            }
            promotion.setOnWayFlag(onWayFlag);
            Date startTime = dto.getStartTime();
            if (startTime == null) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_START_TIME_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_START_TIME_NULL));
            }
            promotion.setStartTime(startTime);
            Date endTime = dto.getEndTime();
            if (endTime == null || endTime.getTime() <= startTime.getTime()) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_END_TIME_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_END_TIME_NULL));
            }
            promotion.setEndTime(endTime);
            if (!CollectionUtils.isEmpty(departments)) {
                promotion.setDistributorScope(SCOPE_DEPARTMENT);
                departmentListMap.put(promotionNo, departments);
            } else {
                promotion.setDistributorScope(SCOPE_ALL);
            }
            if (dto.getAdvanceDay() == null) {
                promotion.setAdvanceFlag(ADVANCE_FLAG_1);
            } else {
                promotion.setAdvanceFlag(ADVANCE_FLAG_2);
            }
            promotion.setApplyStatus(APPLY_STATUS_0);// 草稿
            promotion.setPromoStatus(PROMO_STATUS_0);// 未开始
            promotion.setPromoSource(PROMO_SOURCE_2);// 批量导入
            promotion.setCreateTime(date);
            promotion.setUpdateTime(date);
            promotionMap.put(promotionNo, promotion);
            promotions.add(promotion);
        }
        return promotion;
    }

    public static List toPromotionRelevance(Integer promotionId, PromotionCmd cmd) {
        List<Integer> scalePriceIds = cmd.getScalePriceIds();
        List<PromotionDistributorScopeCmd> distributors = cmd.getDistributors();
        List<Integer> departmentIds = cmd.getDepartmentIds();
        List<Integer> adminIds = cmd.getAdminIds();
        if (cmd.getDistributorScope().equals(SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceIds)) {
            List<PromotionScalePriceRelevanceDO> scalePriceRelevanceDOS = new ArrayList<>();
            scalePriceIds.forEach(id -> {
                PromotionScalePriceRelevanceDO scalePriceRelevanceDO = new PromotionScalePriceRelevanceDO();
                scalePriceRelevanceDO.setPromotionId(promotionId);
                scalePriceRelevanceDO.setScalePriceId(id);
                scalePriceRelevanceDOS.add(scalePriceRelevanceDO);
            });
            return scalePriceRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributors)) {
            List<PromotionDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
            distributors.forEach(distributor -> {
                PromotionDistributorRelevanceDO distributorRelevanceDO = new PromotionDistributorRelevanceDO();
                distributorRelevanceDO.setPromotionId(promotionId);
                distributorRelevanceDO.setDistributorId(distributor.getDistributorId());
                distributorRelevanceDO.setName(distributor.getName());
                distributorRelevanceDO.setCompanyName(distributor.getCompanyName());
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
            return distributorRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentIds)) {
            List<PromotionDepartmentRelevanceDO> departmentRelevanceDOS = new ArrayList<>();
            departmentIds.forEach(id -> {
                PromotionDepartmentRelevanceDO departmentRelevanceDO = new PromotionDepartmentRelevanceDO();
                departmentRelevanceDO.setPromotionId(promotionId);
                departmentRelevanceDO.setDepartmentId(id);
                departmentRelevanceDOS.add(departmentRelevanceDO);
            });
            return departmentRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminIds)) {
            List<PromotionAdminRelevanceDO> adminRelevanceDOS = new ArrayList<>();
            adminIds.forEach(id -> {
                PromotionAdminRelevanceDO adminRelevanceDO = new PromotionAdminRelevanceDO();
                adminRelevanceDO.setPromotionId(promotionId);
                adminRelevanceDO.setAdminId(id);
                adminRelevanceDOS.add(adminRelevanceDO);
            });
            return adminRelevanceDOS;
        } else {
            return null;
        }
    }

    public static List toPromotionRelevanceNO(Integer promotionId, PromotionCmd cmd) {
        List<Integer> scalePriceNoIds = cmd.getScalePriceNoIds();
        List<Integer> distributorNoIds = cmd.getDistributorNoIds();
        List<Integer> departmentNoIds = cmd.getDepartmentNoIds();
        List<Integer> adminNoIds = cmd.getAdminNoIds();
        if (cmd.getDistributorScopeNo().equals(SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceNoIds)) {
            List<PromotionScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS = new ArrayList<>();
            scalePriceNoIds.forEach(id -> {
                PromotionScalePriceRelevanceNoDO scalePriceRelevanceNoDO = new PromotionScalePriceRelevanceNoDO();
                scalePriceRelevanceNoDO.setPromotionId(promotionId);
                scalePriceRelevanceNoDO.setScalePriceId(id);
                scalePriceRelevanceNoDOS.add(scalePriceRelevanceNoDO);
            });
            return scalePriceRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_DISTRIBUTOR)
            && !CollectionUtils.isEmpty(distributorNoIds)) {
            List<PromotionDistributorRelevanceNoDO> distributorRelevanceNoDOS = new ArrayList<>();
            distributorNoIds.forEach(id -> {
                PromotionDistributorRelevanceNoDO distributorRelevanceNoDO = new PromotionDistributorRelevanceNoDO();
                distributorRelevanceNoDO.setPromotionId(promotionId);
                distributorRelevanceNoDO.setDistributorId(id);
                distributorRelevanceNoDOS.add(distributorRelevanceNoDO);
            });
            return distributorRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentNoIds)) {
            List<PromotionDepartmentRelevanceNoDO> departmentRelevanceNoDOS = new ArrayList<>();
            departmentNoIds.forEach(id -> {
                PromotionDepartmentRelevanceNoDO departmentRelevanceNoDO = new PromotionDepartmentRelevanceNoDO();
                departmentRelevanceNoDO.setPromotionId(promotionId);
                departmentRelevanceNoDO.setDepartmentId(id);
                departmentRelevanceNoDOS.add(departmentRelevanceNoDO);
            });
            return departmentRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminNoIds)) {
            List<PromotionAdminRelevanceNoDO> adminRelevanceNoDOS = new ArrayList<>();
            adminNoIds.forEach(id -> {
                PromotionAdminRelevanceNoDO adminRelevanceNoDO = new PromotionAdminRelevanceNoDO();
                adminRelevanceNoDO.setPromotionId(promotionId);
                adminRelevanceNoDO.setAdminId(id);
                adminRelevanceNoDOS.add(adminRelevanceNoDO);
            });
            return adminRelevanceNoDOS;
        } else {
            return null;
        }
    }

    public static PromotionRuleDO toPromotionRuleDO(Integer promotionId, PromotionRuleCmd cmd) {
        PromotionRuleDO ruleDO = new PromotionRuleDO();
        BeanUtils.copyProperties(cmd, ruleDO);
        ruleDO.setPromotionId(promotionId);
        return ruleDO;
    }

    public static PromotionRuleDO toPromotionRuleDO(String indexMsg, ImportPromotionExcelDTO dto,
        Map<String, PromotionRuleDO> promotionRuleMap) {
        String promotionNo = dto.getPromotionNo();
        PromotionRuleDO ruleDO = promotionRuleMap.get(promotionNo);
        if (ruleDO == null) {
            ruleDO = new PromotionRuleDO();// 规则名称统一用活动名称
            ruleDO.setRuleName(dto.getName());
            ruleDO.setRuleNameEn(dto.getNameEn());
        }
        Short ruleType = dto.getRuleType();
        if (ruleType == null || (!ruleType.equals(RULE_TYPE_2) && !ruleType.equals(RULE_TYPE_3))) {
            throw PromotionException.buildException(B_IMPORT_PROMOTION_TARGET_ERROR,
                indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_TARGET_ERROR));
        }
        ruleDO.setRuleType(ruleType);
        ruleDO.setMoneyOrCount(COUNT);
        Short addUpFlag = dto.getAddUpFlag();
        if (addUpFlag == null || addUpFlag != 0 && addUpFlag != 1) {
            throw PromotionException.buildException(B_IMPORT_PROMOTION_ADD_FLAG_ERROR,
                indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_ADD_FLAG_ERROR));
        }
        ruleDO.setAddUpFlag(addUpFlag);
        promotionRuleMap.put(promotionNo, ruleDO);
        return ruleDO;
    }

    public static PromotionRuleGoodsDO toPromotionRuleGoodsDO(String indexMsg, String promotionNo, Integer goodsId,
        String goodsItemNo, GoodsItemRpc itemRpc, PromotionRuleDO ruleDO,
        Map<String, List<PromotionRuleGoodsDO>> ruleGoodsListMap) {
        List<PromotionRuleGoodsDO> promotionRuleGoodss = ruleGoodsListMap.get(promotionNo);
        if (promotionRuleGoodss == null) {
            promotionRuleGoodss = new ArrayList<>();
            ruleGoodsListMap.put(promotionNo, promotionRuleGoodss);
        }
        PromotionRuleGoodsDO promotionRuleGoods = new PromotionRuleGoodsDO();
        promotionRuleGoodss.add(promotionRuleGoods);
        if (ruleDO.getRuleType().equals(RULE_TYPE_2)) {
            promotionRuleGoods.setGoodsId(goodsId);
            promotionRuleGoods.setGoodsNo(goodsItemNo);
        } else if (ruleDO.getRuleType().equals(RULE_TYPE_3)) {
            promotionRuleGoods.setGoodsId(itemRpc.getGoodsId());
            promotionRuleGoods.setItemId(itemRpc.getItemId());
            promotionRuleGoods.setGoodsNo(itemRpc.getGoodsNo());
            promotionRuleGoods.setItemCode(itemRpc.getItemCode());
        }
        if (goodsId == null && itemRpc == null) {
            throw PromotionException.buildException(B_IMPORT_PROMOTION_GOODS_ITEM_NULL,
                indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_GOODS_ITEM_NULL));
        }
        return promotionRuleGoods;
    }

    public static List<PromotionRuleConditionDO> toPromotionRuleConditionDOList(String indexMsg, GoodsItemRpc itemRpc,
        Map<String, List<PromotionRuleConditionDO>> conditionMap, ImportPromotionExcelDTO dto,
        Map<PromotionRuleConditionDO, List<PromotionRuleConditionSpecialDO>> specialMap, PromotionRuleDO ruleDO) {
        List<PromotionRuleConditionDO> conditions = conditionMap.get(dto.getPromotionNo());
        PromotionRuleConditionDO condition1 = null;
        PromotionRuleConditionDO condition2 = null;
        PromotionRuleConditionDO condition3 = null;
        if (conditions == null) {
            conditions = new ArrayList<>();
            // 满减方式1.特价 2.打折 3.减免
            Short conditionType = dto.getConditionType();
            if (conditionType == null || conditionType != 1 && conditionType != 2 && conditionType != 3) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_CONDITION_TYPE_ERROR,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_CONDITION_TYPE_ERROR));
            }
            // 条件1标签
            String conditionName1 = dto.getConditionName1();
            if (StringUtils.isNotBlank(conditionName1)) {
                condition1 = new PromotionRuleConditionDO();
                condition1.setConditionName(conditionName1);
                condition1.setReductionPresentAddFlag(REDUCTION_PRESENT_ADD_FLAG_1);
                condition1.setConditionNameEn(dto.getConditionNameEn1());
                Integer oneBuyCount1 = dto.getOneBuyCount1();
                if (oneBuyCount1 == null) {
                    throw PromotionException.buildException(B_IMPORT_PROMOTION_ONE_BUY_COUNT_ERROR,
                        indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_ONE_BUY_COUNT_ERROR));
                }
                condition1.setOneBuyCount(oneBuyCount1);
                conditions.add(condition1);
                // 条件2标签
                String conditionName2 = dto.getConditionName2();
                if (StringUtils.isNotBlank(conditionName2)) {
                    condition2 = new PromotionRuleConditionDO();
                    condition2.setConditionName(conditionName2);
                    condition2.setReductionPresentAddFlag(REDUCTION_PRESENT_ADD_FLAG_1);
                    condition2.setConditionNameEn(dto.getConditionNameEn2());
                    Integer oneBuyCount2 = dto.getOneBuyCount2();
                    if (oneBuyCount2 == null) {
                        throw PromotionException.buildException(B_IMPORT_PROMOTION_ONE_BUY_COUNT_ERROR,
                            indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_ONE_BUY_COUNT_ERROR));
                    }
                    condition2.setOneBuyCount(oneBuyCount2);
                    conditions.add(condition2);
                }
                // 条件3标签
                String conditionName3 = dto.getConditionName3();
                if (StringUtils.isNotBlank(conditionName3)) {
                    condition3 = new PromotionRuleConditionDO();
                    condition3.setConditionName(conditionName3);
                    condition3.setReductionPresentAddFlag(REDUCTION_PRESENT_ADD_FLAG_1);
                    condition3.setConditionNameEn(dto.getConditionNameEn3());
                    Integer oneBuyCount3 = dto.getOneBuyCount3();
                    if (oneBuyCount3 == null) {
                        throw PromotionException.buildException(B_IMPORT_PROMOTION_ONE_BUY_COUNT_ERROR,
                            indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_ONE_BUY_COUNT_ERROR));
                    }
                    condition3.setOneBuyCount(oneBuyCount3);
                    conditions.add(condition3);
                }
            } else {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_CONDITION_TYPE_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_CONDITION_TYPE_NULL));
            }
            BigDecimal discountReduction1 = dto.getDiscountReduction1();// 满减额度1
            BigDecimal discountReduction2 = dto.getDiscountReduction2();// 满减额度1
            BigDecimal discountReduction3 = dto.getDiscountReduction3();// 满减额度1
            if (condition1 != null && discountReduction1 == null) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_DISCOUNT_REDUCTION_ERROR,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_DISCOUNT_REDUCTION_ERROR));
            }
            if (condition2 != null && discountReduction2 == null) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_DISCOUNT_REDUCTION_ERROR,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_DISCOUNT_REDUCTION_ERROR));
            }
            if (condition3 != null && discountReduction3 == null) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_DISCOUNT_REDUCTION_ERROR,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_DISCOUNT_REDUCTION_ERROR));
            }
            // 特价
            if (conditionType == 1) {
                if (!ruleDO.getRuleType().equals(RULE_TYPE_3)) {
                    throw PromotionException.buildException(B_IMPORT_PROMOTION_TARGET_CONDITION_TYPE_ERROR,
                        indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_TARGET_CONDITION_TYPE_ERROR));
                }
                if (condition1 != null) {
                    condition1.setSpecialFlag(SPECIAL_FLAG_1);
                    List<PromotionRuleConditionSpecialDO> specials = specialMap.get(condition1);
                    if (specials == null) {
                        specials = new ArrayList<>();
                        specialMap.put(condition1, specials);
                    }
                    PromotionRuleConditionSpecialDO special = new PromotionRuleConditionSpecialDO();
                    special.setGoodsId(itemRpc.getGoodsId());
                    special.setItemId(itemRpc.getItemId());
                    special.setSpecialPrice(discountReduction1);
                    specials.add(special);
                }
                if (condition2 != null) {
                    condition2.setSpecialFlag(SPECIAL_FLAG_1);
                    List<PromotionRuleConditionSpecialDO> specials = specialMap.get(condition2);
                    if (specials == null) {
                        specials = new ArrayList<>();
                        specialMap.put(condition2, specials);
                    }
                    PromotionRuleConditionSpecialDO special = new PromotionRuleConditionSpecialDO();
                    special.setGoodsId(itemRpc.getGoodsId());
                    special.setItemId(itemRpc.getItemId());
                    special.setSpecialPrice(discountReduction2);
                    specials.add(special);
                }
                if (condition3 != null) {
                    condition3.setSpecialFlag(SPECIAL_FLAG_1);
                    List<PromotionRuleConditionSpecialDO> specials = specialMap.get(condition3);
                    if (specials == null) {
                        specials = new ArrayList<>();
                        specialMap.put(condition3, specials);
                    }
                    PromotionRuleConditionSpecialDO special = new PromotionRuleConditionSpecialDO();
                    special.setGoodsId(itemRpc.getGoodsId());
                    special.setItemId(itemRpc.getItemId());
                    special.setSpecialPrice(discountReduction3);
                    specials.add(special);
                }
            } else {
                if (conditionType == 2) {// 折扣
                    if (condition1 != null) {
                        condition1.setSpecialFlag(SPECIAL_FLAG_0);
                        condition1.setReduceOrPresent(CONDITION_REDUCE);
                        condition1.setReduceType(REDUCE_TYPE_2);
                        condition1.setDiscount(discountReduction1);
                    }
                    if (condition2 != null) {
                        condition2.setSpecialFlag(SPECIAL_FLAG_0);
                        condition2.setReduceOrPresent(CONDITION_REDUCE);
                        condition2.setReduceType(REDUCE_TYPE_2);
                        condition2.setDiscount(discountReduction2);
                    }
                    if (condition3 != null) {
                        condition3.setSpecialFlag(SPECIAL_FLAG_0);
                        condition3.setReduceOrPresent(CONDITION_REDUCE);
                        condition3.setReduceType(REDUCE_TYPE_2);
                        condition3.setDiscount(discountReduction3);
                    }
                } else if (conditionType == 3) {// 满减
                    if (condition1 != null) {
                        condition1.setSpecialFlag(SPECIAL_FLAG_0);
                        condition1.setReduceOrPresent(CONDITION_REDUCE);
                        condition1.setReduceType(REDUCE_TYPE_1);
                        condition1.setDiscount(discountReduction1);
                    }
                    if (condition2 != null) {
                        condition2.setSpecialFlag(SPECIAL_FLAG_0);
                        condition2.setReduceOrPresent(CONDITION_REDUCE);
                        condition2.setReduceType(REDUCE_TYPE_1);
                        condition2.setDiscount(discountReduction1);
                    }
                    if (condition3 != null) {
                        condition3.setSpecialFlag(SPECIAL_FLAG_0);
                        condition3.setReduceOrPresent(CONDITION_REDUCE);
                        condition3.setReduceType(REDUCE_TYPE_1);
                        condition3.setDiscount(discountReduction1);
                    }
                }
            }
            conditionMap.put(dto.getPromotionNo(), conditions);
        }
        return conditions;
    }

    public static List<PromotionRuleDO> toPromotionRuleDOList(Integer promotionId, List<PromotionRuleCmd> cmds) {
        List<PromotionRuleDO> ruleDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                PromotionRuleDO ruleDO = new PromotionRuleDO();
                BeanUtils.copyProperties(cmd, ruleDO);
                ruleDO.setPromotionId(promotionId);
                ruleDOS.add(ruleDO);
            });
        }
        return ruleDOS;
    }

    public static List<PromotionRuleGoodsDO> toPromotionRuleGoodsDOList(Integer promotionId, Integer promotionRuleId,
        List<PromotionRuleGoodsCmd> cmds) {
        List<PromotionRuleGoodsDO> ruleDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                PromotionRuleGoodsDO ruleDO = new PromotionRuleGoodsDO();
                BeanUtils.copyProperties(cmd, ruleDO);
                ruleDO.setPromotionId(promotionId);
                ruleDO.setPromotionRuleId(promotionRuleId);
                ruleDOS.add(ruleDO);
            });
        }
        return ruleDOS;
    }

    public static PromotionRuleConditionDO toPromotionRuleConditionDO(Integer promotionId, Integer promotionRuleId,
        PromotionRuleConditionCmd cmd) {
        PromotionRuleConditionDO ruleConditionDO = new PromotionRuleConditionDO();
        BeanUtils.copyProperties(cmd, ruleConditionDO);
        ruleConditionDO.setPromotionId(promotionId);
        ruleConditionDO.setPromotionRuleId(promotionRuleId);
        return ruleConditionDO;
    }

    public static List<PromotionRuleConditionSpecialDO> toPromotionRuleConditionSpecialDOList(Integer promotionId,
        Integer promotionRuleConditionId, List<PromotionRuleConditionSpecialCmd> cmds) {
        List<PromotionRuleConditionSpecialDO> specialDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                PromotionRuleConditionSpecialDO specialDO = new PromotionRuleConditionSpecialDO();
                BeanUtils.copyProperties(cmd, specialDO);
                specialDO.setPromotionId(promotionId);
                specialDO.setPromotionRuleConditionId(promotionRuleConditionId);
                specialDOS.add(specialDO);
            });
        }
        return specialDOS;
    }

    public static List<PromotionRuleConditionPresentDO> toPromotionRuleConditionPresentDOList(Integer promotionId,
        Integer promotionRuleConditionId, List<PromotionRuleConditionPresentCmd> cmds) {
        List<PromotionRuleConditionPresentDO> presentDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                PromotionRuleConditionPresentDO presentDO = new PromotionRuleConditionPresentDO();
                BeanUtils.copyProperties(cmd, presentDO);
                presentDO.setPromotionId(promotionId);
                presentDO.setPromotionRuleConditionId(promotionRuleConditionId);
                presentDOS.add(presentDO);
            });
        }
        return presentDOS;
    }

    public static List<PromotionListDTO> toPromotionListDTOList(List<PromotionDO> promotionDOS) {
        List<PromotionListDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(promotionDOS)) {
            promotionDOS.forEach(promotionDO -> {
                PromotionListDTO dto = new PromotionListDTO();
                BeanUtils.copyProperties(promotionDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static PromotionDTO toPromotionDTO(PromotionDO promotionDO) {
        PromotionDTO dto = new PromotionDTO();
        BeanUtils.copyProperties(promotionDO, dto);
        return dto;
    }

    public static List<PromotionDistributorScopeDTO>
        toPromotionDistributorScopeDTOList(List<PromotionDistributorRelevanceDO> distributorRelevanceDOS) {
        List<PromotionDistributorScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorRelevanceDOS)) {
            distributorRelevanceDOS.forEach(distributorRelevanceDO -> {
                PromotionDistributorScopeDTO dto = new PromotionDistributorScopeDTO();
                BeanUtils.copyProperties(distributorRelevanceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<PromotionDistributorScopeDTO>
        toPromotionDistributorScopeDTOListForRpc(List<DistributorRpcDTO> rpcDTOS) {
        List<PromotionDistributorScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOS.forEach(rpcDTO -> {
                PromotionDistributorScopeDTO dto = new PromotionDistributorScopeDTO();
                BeanUtils.copyProperties(rpcDTO, dto);
                dto.setDistributorId(rpcDTO.getId());
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<PromotionRuleDTO> toPromotionRuleDTOList(List<PromotionRuleDO> ruleDOS,
        List<PromotionRuleGoodsDO> ruleGoodsDOS, List<PromotionRuleConditionDO> conditionDOS,
        List<PromotionRuleConditionSpecialDO> specialDOS, List<PromotionRuleConditionPresentDO> presentDOS,
        List<GoodsRpcDTO> goodsRpcDTOS, List<GoodsItemRpcDTO> itemRpcDTOS) {
        List<PromotionRuleDTO> ruleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ruleDOS)) {
            ruleDOS.forEach(ruleDO -> {
                PromotionRuleDTO dto = new PromotionRuleDTO();
                BeanUtils.copyProperties(ruleDO, dto);
                // 组装商品数据
                List<PromotionRuleGoodsDO> ruleGoodsCollect =
                    ruleGoodsDOS.stream().filter(ruleGoodsDO -> ruleGoodsDO.getPromotionRuleId().equals(ruleDO.getId()))
                        .collect(Collectors.toList());
                if (ruleDO.getRuleType().equals(RULE_TYPE_2) && !CollectionUtils.isEmpty(ruleGoodsDOS)) {
                    if (!CollectionUtils.isEmpty(ruleGoodsCollect)) {
                        List<PromotionRuleGoodsDTO> ruleGoodsDTOS = new ArrayList<>();
                        ruleGoodsCollect.forEach(ruleGoods -> {
                            PromotionRuleGoodsDTO ruleGoodsDTO = new PromotionRuleGoodsDTO();
                            BeanUtils.copyProperties(ruleGoods, ruleGoodsDTO);
                            Optional<GoodsRpcDTO> optional = goodsRpcDTOS.stream()
                                .filter(goodsRpcDTO -> goodsRpcDTO.getGoodsId().equals(ruleGoods.getGoodsId()))
                                .findFirst();
                            if (optional != null && optional.isPresent()) {
                                GoodsRpcDTO goodsRpcDTO = optional.get();
                                ruleGoodsDTO.setGoodsName(goodsRpcDTO.getGoodsName());
                            }
                            ruleGoodsDTOS.add(ruleGoodsDTO);
                        });
                        dto.setGoods(ruleGoodsDTOS);
                    }
                } else if (ruleDO.getRuleType().equals(RULE_TYPE_3) && !CollectionUtils.isEmpty(ruleGoodsDOS)) {
                    if (!CollectionUtils.isEmpty(ruleGoodsCollect)) {
                        List<PromotionRuleGoodsDTO> ruleGoodsDTOS = new ArrayList<>();
                        ruleGoodsCollect.forEach(ruleGoods -> {
                            PromotionRuleGoodsDTO ruleGoodsDTO = new PromotionRuleGoodsDTO();
                            BeanUtils.copyProperties(ruleGoods, ruleGoodsDTO);
                            Optional<GoodsItemRpcDTO> optional = itemRpcDTOS.stream()
                                .filter(itemRpcDTO -> itemRpcDTO.getId().equals(ruleGoods.getItemId())).findFirst();
                            if (optional != null && optional.isPresent()) {
                                GoodsItemRpcDTO goodsItemRpcDTO = optional.get();
                                ruleGoodsDTO.setGoodsName(goodsItemRpcDTO.getGoodsName());
                                ruleGoodsDTO.setItemName(goodsItemRpcDTO.getItemName());
                            }
                            ruleGoodsDTOS.add(ruleGoodsDTO);
                        });
                        dto.setGoods(ruleGoodsDTOS);
                    }
                }
                // 组装条件数据
                if (!CollectionUtils.isEmpty(conditionDOS)) {
                    List<PromotionRuleConditionDO> conditionCollect = conditionDOS.stream()
                        .filter(conditionDO -> conditionDO.getPromotionRuleId().equals(dto.getId()))
                        .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(conditionCollect)) {
                        List<PromotionRuleConditionDTO> conditionDTOS = new ArrayList<>();
                        conditionDOS.forEach(conditionDO -> {
                            PromotionRuleConditionDTO conditionDTO = new PromotionRuleConditionDTO();
                            BeanUtils.copyProperties(conditionDO, conditionDTO);
                            // 特价
                            if (conditionDO.getSpecialFlag().equals(SPECIAL_FLAG_1)
                                && !CollectionUtils.isEmpty(specialDOS)) {
                                List<PromotionRuleConditionSpecialDO> specialCollect = specialDOS.stream().filter(
                                    specialDO -> specialDO.getPromotionRuleConditionId().equals(conditionDTO.getId()))
                                    .collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(specialCollect)) {
                                    List<PromotionRuleConditionSpecialDTO> specialDTOS = new ArrayList<>();
                                    specialCollect.forEach(special -> {
                                        PromotionRuleConditionSpecialDTO specialDTO =
                                            new PromotionRuleConditionSpecialDTO();
                                        BeanUtils.copyProperties(special, specialDTO);
                                        Optional<GoodsItemRpcDTO> optional = itemRpcDTOS.stream()
                                            .filter(itemRpcDTO -> itemRpcDTO.getId().equals(specialDTO.getItemId()))
                                            .findFirst();
                                        if (optional != null && optional.isPresent()) {
                                            GoodsItemRpcDTO goodsItemRpcDTO = optional.get();
                                            specialDTO.setGoodsNo(goodsItemRpcDTO.getGoodsNo());
                                            specialDTO.setGoodsName(goodsItemRpcDTO.getGoodsName());
                                            specialDTO.setItemCode(goodsItemRpcDTO.getItemCode());
                                            specialDTO.setItemName(goodsItemRpcDTO.getItemName());
                                        }
                                        specialDTOS.add(specialDTO);
                                    });
                                    conditionDTO.setSpecials(specialDTOS);
                                }
                            } else if (conditionDO.getReduceOrPresent().equals(CONDITION_PRESENT)
                                && !CollectionUtils.isEmpty(presentDOS)) { // 满赠情况
                                List<PromotionRuleConditionPresentDO> presentCollect = presentDOS.stream().filter(
                                    presentDO -> presentDO.getPromotionRuleConditionId().equals(conditionDTO.getId()))
                                    .collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(presentCollect)) {
                                    List<PromotionRuleConditionPresentDTO> presentDTOS = new ArrayList<>();
                                    presentCollect.forEach(present -> {
                                        PromotionRuleConditionPresentDTO presentDTO =
                                            new PromotionRuleConditionPresentDTO();
                                        BeanUtils.copyProperties(present, presentDTO);
                                        Optional<GoodsItemRpcDTO> optional = itemRpcDTOS.stream()
                                            .filter(itemRpcDTO -> itemRpcDTO.getId().equals(presentDTO.getItemId()))
                                            .findFirst();
                                        if (optional != null && optional.isPresent()) {
                                            GoodsItemRpcDTO goodsItemRpcDTO = optional.get();
                                            presentDTO.setGoodsNo(goodsItemRpcDTO.getGoodsNo());
                                            presentDTO.setGoodsName(goodsItemRpcDTO.getGoodsName());
                                            presentDTO.setItemCode(goodsItemRpcDTO.getItemCode());
                                            presentDTO.setItemName(goodsItemRpcDTO.getItemName());
                                            presentDTO.setImageUrl1(goodsItemRpcDTO.getImageUrl1());
                                            presentDTO.setImageUrl1en(goodsItemRpcDTO.getImageUrl1en());
                                            presentDTO.setItemImg(goodsItemRpcDTO.getItemImg());
                                        }
                                        presentDTOS.add(presentDTO);
                                    });
                                    conditionDTO.setPresents(presentDTOS);
                                }
                            }
                            conditionDTOS.add(conditionDTO);
                        });
                        dto.setConditions(conditionDTOS);
                    }
                }
                ruleDTOS.add(dto);
            });
        }
        return ruleDTOS;
    }

    public static GoodsPromotionRpcCmd toGoodsPromotionRpcCmd(Integer promotionId, PromotionCmd cmd) {
        GoodsPromotionRpcCmd rpcCmd = new GoodsPromotionRpcCmd();
        List<GoodsPromotionRuleRpcCmd> ruleRpcCmds = new ArrayList<>();
        rpcCmd.setRules(ruleRpcCmds);
        rpcCmd.setId(promotionId);
        cmd.getRules().forEach(rule -> {
            List<PromotionRuleGoodsCmd> goodsCmds = rule.getGoods();
            if (!rule.getRuleType().equals(RULE_TYPE_1) && !CollectionUtils.isEmpty(goodsCmds)) {
                GoodsPromotionRuleRpcCmd ruleRpcCmd = new GoodsPromotionRuleRpcCmd();
                ruleRpcCmd.setMoneyOrCount(rule.getMoneyOrCount());
                ruleRpcCmd.setRuleType(rule.getRuleType());
                ruleRpcCmds.add(ruleRpcCmd);
                if (rule.getRuleType().equals(RULE_TYPE_2)) {
                    ruleRpcCmd
                        .setIds(goodsCmds.stream().map(PromotionRuleGoodsCmd::getGoodsId).collect(Collectors.toList()));
                } else if (rule.getRuleType().equals(RULE_TYPE_3)) {
                    ruleRpcCmd
                        .setIds(goodsCmds.stream().map(PromotionRuleGoodsCmd::getItemId).collect(Collectors.toList()));
                }
                List<GoodsConditionRpcCmd> conditionRpcCmds = new ArrayList<>();
                ruleRpcCmd.setConditions(conditionRpcCmds);
                rule.getConditions().forEach(condition -> {
                    if (condition.getReduceOrPresent() != null
                        && condition.getReduceOrPresent().equals(CONDITION_REDUCE)) {
                        GoodsConditionRpcCmd conditionRpcCmd = new GoodsConditionRpcCmd();
                        BeanUtils.copyProperties(condition, conditionRpcCmd);
                        conditionRpcCmds.add(conditionRpcCmd);
                        if (condition.getSpecialFlag().equals(SPECIAL_FLAG_1)) {
                            List<GoodsConditionSpecialRpcCmd> specialRpcCmds = new ArrayList<>();
                            conditionRpcCmd.setSpecials(specialRpcCmds);
                            condition.getSpecials().forEach(special -> {
                                GoodsConditionSpecialRpcCmd specialRpcCmd = new GoodsConditionSpecialRpcCmd();
                                BeanUtils.copyProperties(special, specialRpcCmd);
                                specialRpcCmds.add(specialRpcCmd);
                            });
                        }
                    }
                });
            }
        });
        return rpcCmd;
    }

    public static GoodsPromotionRpcCmd toGoodsPromotionRpcCmd(PromotionDO promotionDO, List<PromotionRuleDO> ruleDOS,
        List<PromotionRuleGoodsDO> ruleGoodsDOS, List<PromotionRuleConditionDO> ruleConditionDOS,
        List<PromotionRuleConditionSpecialDO> conditionSpecialDOS) {
        Map<Integer, List<PromotionRuleGoodsDO>> ruleGoodsListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(ruleGoodsDOS)) {
            ruleGoodsListMap
                .putAll(ruleGoodsDOS.stream().collect(Collectors.groupingBy(PromotionRuleGoodsDO::getPromotionRuleId)));
        }
        Map<Integer, List<PromotionRuleConditionDO>> conditionListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(ruleConditionDOS)) {
            conditionListMap.putAll(
                ruleConditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId)));
        }
        Map<Integer, List<PromotionRuleConditionSpecialDO>> conditionSpecialListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(conditionSpecialDOS)) {
            conditionSpecialListMap.putAll(conditionSpecialDOS.stream()
                .collect(Collectors.groupingBy(PromotionRuleConditionSpecialDO::getPromotionRuleConditionId)));
        }
        GoodsPromotionRpcCmd rpcCmd = new GoodsPromotionRpcCmd();
        List<GoodsPromotionRuleRpcCmd> ruleRpcCmds = new ArrayList<>();
        rpcCmd.setRules(ruleRpcCmds);
        rpcCmd.setId(promotionDO.getId());
        ruleDOS.forEach(ruleDO -> {
            List<PromotionRuleGoodsDO> goodsDOS = ruleGoodsListMap.get(ruleDO.getId());
            if (!ruleDO.getRuleType().equals(RULE_TYPE_1) && !CollectionUtils.isEmpty(goodsDOS)) {
                GoodsPromotionRuleRpcCmd ruleRpcCmd = new GoodsPromotionRuleRpcCmd();
                ruleRpcCmd.setMoneyOrCount(ruleDO.getMoneyOrCount());
                ruleRpcCmd.setRuleType(ruleDO.getRuleType());
                ruleRpcCmds.add(ruleRpcCmd);
                if (ruleDO.getRuleType().equals(RULE_TYPE_2)) {
                    ruleRpcCmd.setIds(
                        ruleGoodsDOS.stream().map(PromotionRuleGoodsDO::getGoodsId).collect(Collectors.toList()));
                } else if (ruleDO.getRuleType().equals(RULE_TYPE_3)) {
                    ruleRpcCmd
                        .setIds(goodsDOS.stream().map(PromotionRuleGoodsDO::getItemId).collect(Collectors.toList()));
                }
                List<GoodsConditionRpcCmd> conditionRpcCmds = new ArrayList<>();
                ruleRpcCmd.setConditions(conditionRpcCmds);
                List<PromotionRuleConditionDO> conditionDOS = conditionListMap.get(ruleDO.getId());
                conditionDOS.forEach(condition -> {
                    GoodsConditionRpcCmd conditionRpcCmd = new GoodsConditionRpcCmd();
                    BeanUtils.copyProperties(condition, conditionRpcCmd);
                    conditionRpcCmds.add(conditionRpcCmd);
                    if (condition.getSpecialFlag().equals(SPECIAL_FLAG_1)) {
                        List<PromotionRuleConditionSpecialDO> specialDOS =
                            conditionSpecialListMap.get(condition.getId());
                        List<GoodsConditionSpecialRpcCmd> specialRpcCmds = new ArrayList<>();
                        conditionRpcCmd.setSpecials(specialRpcCmds);
                        specialDOS.forEach(special -> {
                            GoodsConditionSpecialRpcCmd specialRpcCmd = new GoodsConditionSpecialRpcCmd();
                            BeanUtils.copyProperties(special, specialRpcCmd);
                            specialRpcCmds.add(specialRpcCmd);
                        });
                    }
                });
            }
        });
        return rpcCmd;
    }

    public static GoodsGroupSeckillRpcCmd toGoodsGroupSeckillRpcCmd(Integer groupSeckillId, GroupSeckillCmd cmd) {
        GoodsGroupSeckillRpcCmd rpcCmd = new GoodsGroupSeckillRpcCmd();
        rpcCmd.setId(groupSeckillId);
        rpcCmd.setGroupSeckillType(cmd.getGroupSeckillType());
        List<GoodsGroupSeckillGoodsRpcCmd> goodsRpcCmds = new ArrayList<>();
        rpcCmd.setGoods(goodsRpcCmds);
        cmd.getGoods().forEach(goodsCmd -> {
            GoodsGroupSeckillGoodsRpcCmd goodsRpcCmd = new GoodsGroupSeckillGoodsRpcCmd();
            goodsRpcCmd.setItemId(goodsCmd.getItemId());
            goodsRpcCmd.setGroupSeckillPrice(goodsCmd.getGroupSeckillPrice());
            goodsRpcCmds.add(goodsRpcCmd);
        });
        return rpcCmd;
    }

    public static GoodsGroupSeckillRpcCmd toGoodsGroupSeckillRpcCmd(GroupSeckillDO groupSeckillDO,
        List<GroupSeckillGoodsDO> goodsDOS) {
        GoodsGroupSeckillRpcCmd rpcCmd = new GoodsGroupSeckillRpcCmd();
        rpcCmd.setId(groupSeckillDO.getId());
        rpcCmd.setGroupSeckillType(groupSeckillDO.getGroupSeckillType());
        List<GoodsGroupSeckillGoodsRpcCmd> goodsRpcCmds = new ArrayList<>();
        rpcCmd.setGoods(goodsRpcCmds);
        goodsDOS.forEach(goodsDO -> {
            GoodsGroupSeckillGoodsRpcCmd goodsRpcCmd = new GoodsGroupSeckillGoodsRpcCmd();
            goodsRpcCmd.setItemId(goodsDO.getItemId());
            goodsRpcCmd.setGroupSeckillPrice(goodsDO.getGroupSeckillPrice());
            goodsRpcCmds.add(goodsRpcCmd);
        });
        return rpcCmd;
    }

    public static List<PromotionStatusDO> toPromotionStatusDOList(List<Integer> ids, Short promotionStatus) {
        List<PromotionStatusDO> statusDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            Date date = new Date(System.currentTimeMillis());
            ids.forEach(id -> {
                PromotionStatusDO statusDO = new PromotionStatusDO();
                statusDO.setUpdateTime(date);
                statusDO.setId(id);
                statusDO.setPromoStatus(promotionStatus);
                statusDOS.add(statusDO);
            });
        }
        return statusDOS;
    }

    public static List<GoodsItemPromotionPriceRpcDTO> toGoodsItemPromotionPriceRpcDTOList(
        List<GoodsItemPriceRpcQry> goodsPromotionPrices, List<PromotionDO> promotionDOS, List<PromotionRuleDO> ruleDOS,
        List<PromotionRuleConditionDO> conditionDOS, List<PromotionRuleConditionSpecialDO> specialDOS,
        List<PromotionRuleConditionPresentDO> presentDOS, Date time) {
        List<GoodsItemPromotionPriceRpcDTO> rpcDTOS = new ArrayList<>();
        Map<Integer,
            List<GoodsItemPriceRpcQry>> goodsPromotionPricesMap = goodsPromotionPrices.stream()
                .filter(goodsPromotionPrice -> goodsPromotionPrice.getGoodsPromotionId() != null)
                .collect(Collectors.groupingBy(GoodsItemPriceRpcQry::getGoodsPromotionId));
        Map<Integer, PromotionDO> promotionDOMap =
            promotionDOS.stream().collect(Collectors.toMap(PromotionDO::getId, promotionDO -> promotionDO));
        Map<Integer, PromotionRuleDO> ruleDOSMap =
            ruleDOS.stream().collect(Collectors.toMap(PromotionRuleDO::getId, ruleDO -> ruleDO));
        Map<Integer, PromotionRuleConditionDO> conditionDOSMap = conditionDOS.stream()
            .collect(Collectors.toMap(PromotionRuleConditionDO::getId, conditionDO -> conditionDO));
        // 计算商品促销活动
        goodsPromotionPricesMap.forEach((goodsPromotionId, goodss) -> {
            PromotionRuleConditionDO ruleConditionDO = conditionDOSMap.get(goodsPromotionId);
            // 判断活动有效性
            List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOS =
                checkGoodsPromotion(ruleConditionDO, promotionDOMap, goodss, time);
            PromotionDO promotionDO = promotionDOMap.get(ruleConditionDO.getPromotionId());
            PromotionRuleDO ruleDO = ruleDOSMap.get(ruleConditionDO.getPromotionRuleId());
            // 开始计算活动价格
            calculateGoodsPromotion(promotionDO, ruleDO, ruleConditionDO, goodsPromotionRpcDTOS, specialDOS,
                presentDOS);
            rpcDTOS.addAll(goodsPromotionRpcDTOS);
        });
        // 计算订单促销活动
        return rpcDTOS;
    }

    private static void calculateGoodsPromotion(PromotionDO promotionDO, PromotionRuleDO ruleDO,
        PromotionRuleConditionDO ruleConditionDO, List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOS,
        List<PromotionRuleConditionSpecialDO> specialDOS, List<PromotionRuleConditionPresentDO> presentDOS) {
        List<GoodsItemPromotionPriceRpcDTO> addGoodsPromotionRpcDTOS = new ArrayList<>();
        Map<Integer, PromotionRuleConditionSpecialDO> specialDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(specialDOS)) {
            specialDOMap.putAll(specialDOS.stream()
                .collect(Collectors.toMap(PromotionRuleConditionSpecialDO::getItemId, specialDO -> specialDO)));
        }
        // 是否累计
        if (ruleDO.getAddUpFlag().equals(Constant.ADD_UP_FLAG_1)) {
            // 按金额还是数量
            if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                double sum = 0.00;
                // 在途是否参与活动
                if (promotionDO.getOnWayFlag() != null && promotionDO.getOnWayFlag().equals(ON_WAY_FLAG_1)) {
                    sum = goodsPromotionRpcDTOS.stream()
                        .filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() != null
                            && !goodsPromotionRpcDTO.getItemType().equals(Constant.ITEM_TYPE_2))
                        .mapToDouble(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getSalePrice().doubleValue()
                            * goodsPromotionRpcDTO.getItemCount())
                        .sum();
                } else {
                    sum = goodsPromotionRpcDTOS.stream()
                        .filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() != null
                            && !goodsPromotionRpcDTO.getItemType().equals(Constant.ITEM_TYPE_2))
                        .mapToDouble(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getSalePrice().doubleValue()
                            * (goodsPromotionRpcDTO.getItemCount() - (goodsPromotionRpcDTO.getItemOnWayCount() != null
                                ? goodsPromotionRpcDTO.getItemOnWayCount() : 0)))
                        .sum();
                }
                // 是否达到条件
                if (sum < ruleConditionDO.getOneBuyMoney().doubleValue()) {
                    promotionConditionError(goodsPromotionRpcDTOS);
                } else {
                    // 计算商品活动价格或检查赠品
                    calculateGoodsPromotion(promotionDO, ruleDO, ruleConditionDO, goodsPromotionRpcDTOS,
                        addGoodsPromotionRpcDTOS, specialDOMap, presentDOS, sum);
                }
            } else {
                // 在途是否参与活动
                double count = 0.00;
                if (promotionDO.getOnWayFlag() != null && promotionDO.getOnWayFlag().equals(ON_WAY_FLAG_1)) {
                    count = goodsPromotionRpcDTOS.stream()
                        .filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() != null
                            && !goodsPromotionRpcDTO.getItemType().equals(Constant.ITEM_TYPE_2))
                        .mapToInt(goodsPromotionRpcDTO -> {
                            Integer itemCount = goodsPromotionRpcDTO.getItemCount();
                            return itemCount;
                        }).sum();
                } else {
                    count = goodsPromotionRpcDTOS.stream()
                        .filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() != null
                            && !goodsPromotionRpcDTO.getItemType().equals(Constant.ITEM_TYPE_2))
                        .mapToInt(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemCount()
                            - (goodsPromotionRpcDTO.getItemOnWayCount() != null
                                ? goodsPromotionRpcDTO.getItemOnWayCount() : 0))
                        .sum();
                }
                // 是否达到条件
                if (count < ruleConditionDO.getOneBuyCount()) {
                    promotionConditionError(goodsPromotionRpcDTOS);
                } else {
                    calculateGoodsPromotion(promotionDO, ruleDO, ruleConditionDO, goodsPromotionRpcDTOS,
                        addGoodsPromotionRpcDTOS, specialDOMap, presentDOS, count);
                }
            }
        } else {
            AtomicInteger giftCount = new AtomicInteger();
            goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                // 非赠品参与活动计算
                if (!goodsPromotionRpcDTO.getItemType().equals(ITEM_TYPE_2)) {
                    double moneyOrCount = 0.00;
                    // 按金额还是数量
                    if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                        moneyOrCount =
                            goodsPromotionRpcDTO.getSalePrice().doubleValue() * goodsPromotionRpcDTO.getItemCount();
                        if (goodsPromotionRpcDTO.getItemOnWayCount() != null
                            && goodsPromotionRpcDTO.getItemOnWayCount() != 0 && promotionDO.getOnWayFlag() != null
                            && promotionDO.getOnWayFlag().equals(ON_WAY_FLAG_0)) {
                            moneyOrCount = goodsPromotionRpcDTO.getSalePrice().doubleValue()
                                * (goodsPromotionRpcDTO.getItemCount() - goodsPromotionRpcDTO.getItemOnWayCount());
                        }
                        // 是否达到条件
                        if (moneyOrCount < ruleConditionDO.getOneBuyMoney().doubleValue()) {
                            promotionConditionError(goodsPromotionRpcDTOS);
                        }
                    } else {
                        moneyOrCount = goodsPromotionRpcDTO.getItemCount();
                        if (goodsPromotionRpcDTO.getItemOnWayCount() != null
                            && goodsPromotionRpcDTO.getItemOnWayCount() != 0 && promotionDO.getOnWayFlag() != null
                            && promotionDO.getOnWayFlag().equals(ON_WAY_FLAG_0)) {
                            moneyOrCount =
                                goodsPromotionRpcDTO.getItemCount() - goodsPromotionRpcDTO.getItemOnWayCount();
                        }
                        // 是否达到条件
                        if (moneyOrCount < ruleConditionDO.getOneBuyCount().intValue()) {
                            promotionConditionError(goodsPromotionRpcDTOS);
                        }
                    }
                    giftCount.set(calculateGoodsPromotion(promotionDO, ruleDO, ruleConditionDO, goodsPromotionRpcDTO,
                        addGoodsPromotionRpcDTOS, specialDOMap, presentDOS, moneyOrCount, giftCount.get()));
                } else {
                    goodsPromotionRpcDTO.setActualPrice(new BigDecimal(0));
                }
            });
            if (ruleConditionDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_0)
                && ruleConditionDO.getReduceOrPresent().equals(Constant.CONDITION_PRESENT)) {
                // 赠品检查
                checkGiftGoods(goodsPromotionRpcDTOS, presentDOS, giftCount.get());
            }
        }
    }

    /**
     * 累计商品活动计算
     * 
     * @param promotionDO
     * @param ruleDO
     * @param ruleConditionDO
     * @param goodsPromotionRpcDTOS
     * @param specialDOMap
     * @param presentDOS
     * @param moneyOrCount
     */
    private static void calculateGoodsPromotion(PromotionDO promotionDO, PromotionRuleDO ruleDO,
        PromotionRuleConditionDO ruleConditionDO, List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOS,
        List<GoodsItemPromotionPriceRpcDTO> addGoodsPromotionRpcDTOS,
        Map<Integer, PromotionRuleConditionSpecialDO> specialDOMap, List<PromotionRuleConditionPresentDO> presentDOS,
        double moneyOrCount) {
        // 是否特价
        if (ruleConditionDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_1)) {
            goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                PromotionRuleConditionSpecialDO conditionSpecialDO = specialDOMap.get(goodsPromotionRpcDTO.getItemId());
                goodsPromotionRpcDTO.setActualPrice(conditionSpecialDO.getSpecialPrice());
            });
        } else {
            // 满减还是满赠
            if (ruleConditionDO.getReduceOrPresent().equals(Constant.CONDITION_REDUCE)) {
                // 折扣还是减免
                if (ruleConditionDO.getReduceType().equals(Constant.REDUCE_TYPE_1)) {
                    // 分摊单价后活动优惠金额合计
                    AtomicReference<BigDecimal> totalPromotion = new AtomicReference<>(new BigDecimal(0));
                    // 分摊单价前活动优惠金额合计
                    AtomicReference<BigDecimal> actualPromotion = new AtomicReference<>(new BigDecimal(0));
                    // 叠加还是不叠加
                    if (ruleConditionDO.getReductionPresentAddFlag().equals(Constant.REDUCTION_PRESENT_ADD_FLAG_0)) {
                        actualPromotion.set(ruleConditionDO.getReduction());
                        goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                            goodsPromotionRpcDTO.setOnWayFlag(promotionDO.getOnWayFlag());
                            BigDecimal goodsPromotion = new BigDecimal(0);
                            if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                                goodsPromotion = new BigDecimal(goodsPromotionRpcDTO.getSalePrice().doubleValue()
                                    * ruleConditionDO.getReduction().doubleValue() / moneyOrCount)
                                        .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                goodsPromotion =
                                    new BigDecimal(ruleConditionDO.getReduction().doubleValue() / moneyOrCount)
                                        .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            // 在途是否参与活动
                            if (goodsPromotionRpcDTO.getOnWayFlag() != null
                                && goodsPromotionRpcDTO.getOnWayFlag().equals(ON_WAY_FLAG_1)) {
                                totalPromotion.set(totalPromotion.get()
                                    .add(new BigDecimal(
                                        goodsPromotion.doubleValue() * goodsPromotionRpcDTO.getItemCount().intValue())
                                            .setScale(3, BigDecimal.ROUND_HALF_UP)
                                            .setScale(2, BigDecimal.ROUND_HALF_UP)));
                            } else {
                                totalPromotion.set(totalPromotion.get().add(
                                    new BigDecimal(goodsPromotion.doubleValue() * (goodsPromotionRpcDTO.getItemCount()
                                        - (goodsPromotionRpcDTO.getItemOnWayCount() != null
                                            ? goodsPromotionRpcDTO.getItemOnWayCount() : 0)))));
                            }
                            goodsPromotionRpcDTO.setActualPrice(new BigDecimal(
                                (goodsPromotionRpcDTO.getSalePrice().doubleValue() - goodsPromotion.doubleValue()))
                                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                            // 如果是最后一个情况，需反算活动金额是否相等,不相等情况通过拆出一个商品承担差价
                            if (goodsPromotionRpcDTOS.indexOf(goodsPromotionRpcDTO) == goodsPromotionRpcDTOS.size() - 1
                                && totalPromotion.get().compareTo(actualPromotion.get()) != 0) {
                                BigDecimal difPrice = actualPromotion.get().subtract(totalPromotion.get());
                                // 拆商品
                                GoodsItemPromotionPriceRpcDTO priceRpcDTO =
                                    getDifGoodsItemPromotionPriceRpcDTO(goodsPromotionRpcDTO, difPrice);
                                if (priceRpcDTO != null) {
                                    addGoodsPromotionRpcDTOS.add(priceRpcDTO);
                                }
                            }
                        });
                    } else {
                        if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                            actualPromotion
                                .set(new BigDecimal(moneyOrCount * ruleConditionDO.getReduction().doubleValue()
                                    / ruleConditionDO.getOneBuyMoney().doubleValue())
                                        .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            actualPromotion
                                .set(new BigDecimal(moneyOrCount * ruleConditionDO.getReduction().doubleValue()
                                    / ruleConditionDO.getOneBuyCount().intValue()).setScale(3, BigDecimal.ROUND_HALF_UP)
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                            goodsPromotionRpcDTO.setOnWayFlag(promotionDO.getOnWayFlag());
                            BigDecimal goodsPromotion = new BigDecimal(0);
                            if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                                goodsPromotion = new BigDecimal(goodsPromotionRpcDTO.getSalePrice().doubleValue()
                                    * ruleConditionDO.getReduction().doubleValue()
                                    / ruleConditionDO.getOneBuyMoney().doubleValue())
                                        .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                goodsPromotion = new BigDecimal(ruleConditionDO.getReduction().doubleValue()
                                    / ruleConditionDO.getOneBuyCount().intValue()).setScale(3, BigDecimal.ROUND_HALF_UP)
                                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            // 在途是否参与活动
                            if (goodsPromotionRpcDTO.getOnWayFlag() != null
                                && goodsPromotionRpcDTO.getOnWayFlag().equals(ON_WAY_FLAG_1)) {
                                totalPromotion.set(totalPromotion.get()
                                    .add(new BigDecimal(
                                        goodsPromotion.doubleValue() * goodsPromotionRpcDTO.getItemCount().intValue()))
                                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else {
                                totalPromotion.set(totalPromotion.get()
                                    .add(new BigDecimal(
                                        goodsPromotion.doubleValue() * (goodsPromotionRpcDTO.getItemCount()
                                            - (goodsPromotionRpcDTO.getItemOnWayCount() != null
                                                ? goodsPromotionRpcDTO.getItemOnWayCount() : 0))))
                                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                            goodsPromotionRpcDTO.setActualPrice(new BigDecimal(
                                (goodsPromotionRpcDTO.getSalePrice().doubleValue() - goodsPromotion.doubleValue()))
                                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                            // 如果是最后一个情况，需反算活动金额是否相等,不相等情况通过拆出一个商品承担差价(购买金额或数量刚好等于活动金额)
                            if (goodsPromotionRpcDTOS.indexOf(goodsPromotionRpcDTO) == goodsPromotionRpcDTOS.size() - 1
                                && totalPromotion.get().compareTo(actualPromotion.get()) != 0) {
                                BigDecimal difPrice = actualPromotion.get().subtract(totalPromotion.get());
                                // 拆商品
                                GoodsItemPromotionPriceRpcDTO priceRpcDTO =
                                    getDifGoodsItemPromotionPriceRpcDTO(goodsPromotionRpcDTO, difPrice);
                                if (priceRpcDTO != null) {
                                    addGoodsPromotionRpcDTOS.add(priceRpcDTO);
                                }
                            }
                        });
                    }
                    if (!CollectionUtils.isEmpty(addGoodsPromotionRpcDTOS)) {
                        goodsPromotionRpcDTOS.addAll(addGoodsPromotionRpcDTOS);
                    }
                } else if (ruleConditionDO.getReduceType().equals(Constant.REDUCE_TYPE_2)) {
                    goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                        goodsPromotionRpcDTO.setActualPrice(goodsPromotionRpcDTO.getSalePrice()
                            .multiply(ruleConditionDO.getDiscount().divide(new BigDecimal(100)))
                            .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                    });
                }
            } else if (ruleConditionDO.getReduceOrPresent() != null
                && ruleConditionDO.getReduceOrPresent().equals(Constant.CONDITION_PRESENT)) {
                goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                    if (goodsPromotionRpcDTO.getItemType().equals(ITEM_TYPE_2)) {
                        goodsPromotionRpcDTO.setActualPrice(new BigDecimal(0));
                    } else {
                        goodsPromotionRpcDTO.setActualPrice(goodsPromotionRpcDTO.getSalePrice());
                    }
                });
                // 赠品检查
                checkGiftGoods(ruleDO, ruleConditionDO, goodsPromotionRpcDTOS, presentDOS, moneyOrCount);
            }
        }
    }

    /**
     * 不累计单个商品活动计算
     * 
     * @param promotionDO
     * @param ruleDO
     * @param ruleConditionDO
     * @param goodsPromotionRpcDTO
     * @param specialDOMap
     * @param presentDOS
     * @param moneyOrCount
     * @param giftCount
     * @return
     */
    private static int calculateGoodsPromotion(PromotionDO promotionDO, PromotionRuleDO ruleDO,
        PromotionRuleConditionDO ruleConditionDO, GoodsItemPromotionPriceRpcDTO goodsPromotionRpcDTO,
        List<GoodsItemPromotionPriceRpcDTO> addGoodsPromotionRpcDTOS,
        Map<Integer, PromotionRuleConditionSpecialDO> specialDOMap, List<PromotionRuleConditionPresentDO> presentDOS,
        double moneyOrCount, int giftCount) {
        // 是否特价
        if (ruleConditionDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_1)) {
            PromotionRuleConditionSpecialDO conditionSpecialDO = specialDOMap.get(goodsPromotionRpcDTO.getItemId());
            goodsPromotionRpcDTO.setActualPrice(conditionSpecialDO.getSpecialPrice());
        } else {
            // 满减还是满赠
            if (ruleConditionDO.getReduceOrPresent().equals(Constant.CONDITION_REDUCE)) {
                // 折扣还是减免
                if (ruleConditionDO.getReduceType().equals(Constant.REDUCE_TYPE_1)) {
                    // 分摊单价后活动优惠金额合计
                    BigDecimal totalPromotion = new BigDecimal(0);
                    // 分摊单价前活动优惠金额合计
                    BigDecimal actualPromotion = new BigDecimal(0);
                    // 叠加还是不叠加
                    if (ruleConditionDO.getReductionPresentAddFlag().equals(Constant.REDUCTION_PRESENT_ADD_FLAG_0)) {
                        goodsPromotionRpcDTO.setOnWayFlag(promotionDO.getOnWayFlag());
                        BigDecimal goodsPromotion = new BigDecimal(0);
                        actualPromotion = ruleConditionDO.getReduction();
                        if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                            goodsPromotion = new BigDecimal(goodsPromotionRpcDTO.getSalePrice().doubleValue()
                                * ruleConditionDO.getReduction().doubleValue() / moneyOrCount)
                                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                        } else {
                            goodsPromotion =
                                new BigDecimal(ruleConditionDO.getReduction().doubleValue() / moneyOrCount);
                        }
                        // 在途是否参与活动
                        if (goodsPromotionRpcDTO.getOnWayFlag() != null
                            && goodsPromotionRpcDTO.getOnWayFlag().equals(ON_WAY_FLAG_1)) {
                            totalPromotion = totalPromotion.add(new BigDecimal(
                                goodsPromotion.doubleValue() * goodsPromotionRpcDTO.getItemCount().intValue())
                                    .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            totalPromotion = totalPromotion
                                .add(new BigDecimal(goodsPromotion.doubleValue() * (goodsPromotionRpcDTO.getItemCount()
                                    - (goodsPromotionRpcDTO.getItemOnWayCount() != null
                                        ? goodsPromotionRpcDTO.getItemOnWayCount() : 0))));
                        }
                        goodsPromotionRpcDTO.setActualPrice(new BigDecimal(
                            (goodsPromotionRpcDTO.getSalePrice().doubleValue() - goodsPromotion.doubleValue()))
                                .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                        // 需反算活动金额是否相等,不相等情况通过拆出一个商品承担差价
                        if (totalPromotion.compareTo(actualPromotion) != 0) {
                            BigDecimal difPrice = actualPromotion.subtract(totalPromotion);
                            // 拆商品
                            GoodsItemPromotionPriceRpcDTO priceRpcDTO =
                                getDifGoodsItemPromotionPriceRpcDTO(goodsPromotionRpcDTO, difPrice);
                            if (priceRpcDTO != null) {
                                addGoodsPromotionRpcDTOS.add(priceRpcDTO);
                            }
                        }
                    } else {
                        goodsPromotionRpcDTO.setOnWayFlag(promotionDO.getOnWayFlag());
                        BigDecimal goodsPromotion = new BigDecimal(0);
                        // 计算单价分摊前活动优惠金额
                        if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                            actualPromotion = new BigDecimal(moneyOrCount * ruleConditionDO.getReduction().doubleValue()
                                / ruleConditionDO.getOneBuyMoney().doubleValue()).setScale(3, BigDecimal.ROUND_HALF_UP)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                        } else {
                            actualPromotion = new BigDecimal(moneyOrCount * ruleConditionDO.getReduction().doubleValue()
                                / ruleConditionDO.getOneBuyCount().intValue()).setScale(3, BigDecimal.ROUND_HALF_UP)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                            goodsPromotion = new BigDecimal(goodsPromotionRpcDTO.getSalePrice().doubleValue()
                                * ruleConditionDO.getReduction().doubleValue()
                                / ruleConditionDO.getOneBuyMoney().doubleValue()).setScale(3, BigDecimal.ROUND_HALF_UP)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                        } else {
                            goodsPromotion = new BigDecimal(ruleConditionDO.getReduction().doubleValue()
                                / ruleConditionDO.getOneBuyCount().intValue()).setScale(3, BigDecimal.ROUND_HALF_UP)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        // 在途是否参与活动
                        if (goodsPromotionRpcDTO.getOnWayFlag() != null
                            && goodsPromotionRpcDTO.getOnWayFlag().equals(ON_WAY_FLAG_1)) {
                            totalPromotion = totalPromotion.add(new BigDecimal(
                                goodsPromotion.doubleValue() * goodsPromotionRpcDTO.getItemCount().intValue()));
                        } else {
                            totalPromotion = totalPromotion
                                .add(new BigDecimal(goodsPromotion.doubleValue() * (goodsPromotionRpcDTO.getItemCount()
                                    - (goodsPromotionRpcDTO.getItemOnWayCount() != null
                                        ? goodsPromotionRpcDTO.getItemOnWayCount() : 0))));
                        }
                        goodsPromotionRpcDTO.setActualPrice(new BigDecimal(
                            (goodsPromotionRpcDTO.getSalePrice().doubleValue() - goodsPromotion.doubleValue()))
                                .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                        // 需反算活动金额是否相等,不相等情况通过拆出一个商品承担差价(购买金额或数量刚好等于活动金额)
                        if (totalPromotion.compareTo(actualPromotion) != 0) {
                            BigDecimal difPrice = ruleConditionDO.getReduction().subtract(totalPromotion);
                            // 拆商品
                            GoodsItemPromotionPriceRpcDTO priceRpcDTO =
                                getDifGoodsItemPromotionPriceRpcDTO(goodsPromotionRpcDTO, difPrice);
                            if (priceRpcDTO != null) {
                                addGoodsPromotionRpcDTOS.add(priceRpcDTO);
                            }
                        }
                    }
                } else if (ruleConditionDO.getReduceType().equals(Constant.REDUCE_TYPE_2)) {
                    goodsPromotionRpcDTO.setActualPrice(goodsPromotionRpcDTO.getSalePrice()
                        .multiply(ruleConditionDO.getDiscount().divide(new BigDecimal(100)))
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            } else {
                if (goodsPromotionRpcDTO.getItemType().equals(ITEM_TYPE_2)) {
                    goodsPromotionRpcDTO.setActualPrice(new BigDecimal(0));
                } else {
                    goodsPromotionRpcDTO.setActualPrice(goodsPromotionRpcDTO.getSalePrice());
                }
                // 叠加与不叠加情况，赠品计算
                if (ruleConditionDO.getReductionPresentAddFlag().equals(Constant.REDUCTION_PRESENT_ADD_FLAG_0)) {
                    giftCount = giftCount + ruleConditionDO.getPresentCount().intValue();
                } else {
                    if (ruleDO.getMoneyOrCount().equals(Constant.MONEY)) {
                        giftCount = (int)(giftCount + Math.ceil(Math.floor(moneyOrCount
                            * ruleConditionDO.getPresentCount() / ruleConditionDO.getOneBuyMoney().doubleValue())));
                    } else {
                        giftCount = (int)(giftCount + Math.ceil(Math.floor(moneyOrCount
                            * ruleConditionDO.getPresentCount() / ruleConditionDO.getOneBuyCount().doubleValue())));
                    }
                }
            }
        }
        return giftCount;
    }

    /**
     * 累计赠品计算
     * 
     * @param ruleConditionDO
     * @param goodsPromotionRpcDTOS
     * @param presentDOS
     * @param moneyOrCount
     */
    private static void checkGiftGoods(PromotionRuleDO ruleDO, PromotionRuleConditionDO ruleConditionDO,
        List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOS, List<PromotionRuleConditionPresentDO> presentDOS,
        double moneyOrCount) {
        // 赠品是否符合条件
        AtomicBoolean b = new AtomicBoolean(true);
        List<GoodsItemPromotionPriceRpcDTO> giftGoodss =
            goodsPromotionRpcDTOS.stream().filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() != null
                && goodsPromotionRpcDTO.getItemType().equals(ITEM_TYPE_2)).collect(Collectors.toList());

        Map<Integer, List<PromotionRuleConditionPresentDO>> presentDOSMap =
            presentDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionPresentDO::getItemId));
        int totalCount = giftGoodss.stream().mapToInt(GoodsItemPromotionPriceRpcDTO::getItemCount).sum();
        // 叠加还是不叠加,检查赠品赠送总数量是否符合条件
        if (ruleConditionDO.getReductionPresentAddFlag().equals(Constant.REDUCTION_PRESENT_ADD_FLAG_0)
            && totalCount > ruleConditionDO.getPresentCount().intValue()) {
            b.set(false);
        } else if (ruleDO.getMoneyOrCount().equals(MONEY) && totalCount > Math.floor(
            moneyOrCount * (ruleConditionDO.getPresentCount() / ruleConditionDO.getOneBuyMoney().doubleValue()))) {
            b.set(false);
        } else if (ruleDO.getMoneyOrCount().equals(COUNT) && totalCount > Math.floor(
            moneyOrCount * (ruleConditionDO.getPresentCount() / ruleConditionDO.getOneBuyCount().doubleValue()))) {
            b.set(false);
        }
        if (b.get()) {
            giftGoodss.forEach(giftGoods -> {
                List<PromotionRuleConditionPresentDO> conditionPresentDOS = presentDOSMap.get(giftGoods.getItemId());
                Optional<PromotionRuleConditionPresentDO> optional = conditionPresentDOS.stream()
                    .filter(conditionPresentDO -> (giftGoods.getGoodsPromotionId() != null
                        && conditionPresentDO.getPromotionRuleConditionId().equals(giftGoods.getGoodsPromotionId()))
                        || (giftGoods.getOrderPromotionId() != null && conditionPresentDO.getPromotionRuleConditionId()
                            .equals(giftGoods.getOrderPromotionId())))
                    .findFirst();
                // 检查是否在赠送范围
                if (optional == null || !optional.isPresent()) {
                    b.set(false);
                    return;
                } else {
                    // 检查单次赠送数量限制
                    PromotionRuleConditionPresentDO presentDO = optional.get();
                    if (presentDO.getCount() != null
                        && presentDO.getCount().intValue() < giftGoods.getItemCount().intValue()) {
                        b.set(false);
                    } else {
                        // TODO 检查赠送商品总赠送数量限制（需考虑并发）
                    }
                }
            });
        }
        if (!b.get()) {
            goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                goodsPromotionRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_GIFT_ERROR);
                goodsPromotionRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_GIFT_ERROR));
            });
        }
    }

    /**
     * 不累计赠品计算
     * 
     * @param goodsPromotionRpcDTOS
     * @param presentDOS
     * @param giftCount
     */
    private static void checkGiftGoods(List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOS,
        List<PromotionRuleConditionPresentDO> presentDOS, int giftCount) {
        // 赠品是否符合条件
        AtomicBoolean b = new AtomicBoolean(true);
        List<GoodsItemPromotionPriceRpcDTO> giftGoodss =
            goodsPromotionRpcDTOS.stream().filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() != null
                && goodsPromotionRpcDTO.getItemType().equals(ITEM_TYPE_2)).collect(Collectors.toList());
        Map<Integer, List<PromotionRuleConditionPresentDO>> presentDOSMap =
            presentDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionPresentDO::getItemId));
        int totalCount = giftGoodss.stream().mapToInt(GoodsItemPromotionPriceRpcDTO::getItemCount).sum();
        // 检查赠品赠送总数量是否符合条件
        if (totalCount > giftCount) {
            b.set(false);
        }
        if (b.get()) {
            long count =
                goodsPromotionRpcDTOS.stream().filter(goodsPromotionRpcDTO -> goodsPromotionRpcDTO.getItemType() == null
                    || goodsPromotionRpcDTO.getItemType().equals(ITEM_TYPE_1)).count();
            giftGoodss.forEach(giftGoods -> {
                List<PromotionRuleConditionPresentDO> conditionPresentDOS = presentDOSMap.get(giftGoods.getItemId());
                Optional<PromotionRuleConditionPresentDO> optional = conditionPresentDOS.stream()
                    .filter(conditionPresentDO -> (giftGoods.getGoodsPromotionId() != null
                        && conditionPresentDO.getPromotionRuleConditionId().equals(giftGoods.getGoodsPromotionId()))
                        || (giftGoods.getOrderPromotionId() != null && conditionPresentDO.getPromotionRuleConditionId()
                            .equals(giftGoods.getOrderPromotionId())))
                    .findFirst();
                // 检查是否在赠送范围
                if (optional == null || !optional.isPresent()) {
                    b.set(false);
                    return;
                } else {
                    // 检查单次赠送数量限制
                    PromotionRuleConditionPresentDO presentDO = optional.get();
                    if (presentDO.getCount() != null
                        && presentDO.getCount().intValue() * count < giftGoods.getItemCount().intValue()) {
                        b.set(false);
                    } else {
                        // TODO 检查赠送商品总赠送数量限制（需考虑并发）
                    }
                }
            });
        }
        if (!b.get()) {
            goodsPromotionRpcDTOS.forEach(goodsPromotionRpcDTO -> {
                goodsPromotionRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_GIFT_ERROR);
                goodsPromotionRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_GIFT_ERROR));
            });
        }
    }

    /**
     * 活动价格精度商品拆分处理
     * 
     * @param goodsPromotionRpcDTO
     * @param difPrice
     * @return
     */
    private static GoodsItemPromotionPriceRpcDTO
        getDifGoodsItemPromotionPriceRpcDTO(GoodsItemPromotionPriceRpcDTO goodsPromotionRpcDTO, BigDecimal difPrice) {
        // 商品数量大于1情况才需要拆分，否则无需拆分
        if (goodsPromotionRpcDTO.getItemCount() > 1) {
            GoodsItemPromotionPriceRpcDTO difRpcDTO = new GoodsItemPromotionPriceRpcDTO();
            BeanUtils.copyProperties(goodsPromotionRpcDTO, difRpcDTO);
            goodsPromotionRpcDTO.setItemCount(goodsPromotionRpcDTO.getItemCount() - 1);
            difRpcDTO.setItemCount(1);
            difRpcDTO.setItemInCount(0);
            difRpcDTO.setItemVmiCount(0);
            difRpcDTO.setItemOnWayCount(0);
            if (goodsPromotionRpcDTO.getItemInCount() != null && goodsPromotionRpcDTO.getItemInCount() > 0) {
                difRpcDTO.setItemInCount(1);
                goodsPromotionRpcDTO.setItemInCount(goodsPromotionRpcDTO.getItemInCount() - 1);
            } else if (goodsPromotionRpcDTO.getItemVmiCount() != null && goodsPromotionRpcDTO.getItemVmiCount() > 0) {
                difRpcDTO.setItemVmiCount(1);
                goodsPromotionRpcDTO.setItemVmiCount(goodsPromotionRpcDTO.getItemVmiCount() - 1);
            } else {
                difRpcDTO.setItemOnWayCount(1);
                goodsPromotionRpcDTO.setItemOnWayCount(goodsPromotionRpcDTO.getItemOnWayCount() - 1);
            }
            difRpcDTO.setActualPrice(difRpcDTO.getActualPrice().subtract(difPrice));
            return difRpcDTO;
        } else {
            goodsPromotionRpcDTO.setActualPrice(goodsPromotionRpcDTO.getActualPrice().subtract(difPrice));
            return null;
        }
    }

    private static void promotionConditionError(List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOs) {
        goodsPromotionRpcDTOs.forEach(goodsPromotionRpcDTO -> {
            promotionConditionError(goodsPromotionRpcDTO);
        });
    }

    private static void promotionConditionError(GoodsItemPromotionPriceRpcDTO goodsPromotionRpcDTO) {
        goodsPromotionRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_CONDITION_ERROR);
        goodsPromotionRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_CONDITION_ERROR));
    }

    private static List<GoodsItemPromotionPriceRpcDTO> checkGoodsPromotion(PromotionRuleConditionDO ruleConditionDO,
        Map<Integer, PromotionDO> promotionDOMap, List<GoodsItemPriceRpcQry> goodss, Date time) {
        List<GoodsItemPromotionPriceRpcDTO> goodsPromotionRpcDTOs = new ArrayList<>();
        PromotionDO promotionDO = promotionDOMap.get(ruleConditionDO.getPromotionId());
        goodss.forEach(goods -> {
            GoodsItemPromotionPriceRpcDTO rpcDTO = new GoodsItemPromotionPriceRpcDTO();
            BeanUtils.copyProperties(goods, rpcDTO);
            rpcDTO.setOnWayFlag(promotionDO.getOnWayFlag());
            rpcDTO.setFlag(CommonErrorCode.B_PROMOTION_SUCCESS);
            goodsPromotionRpcDTOs.add(rpcDTO);
        });
        if (ruleConditionDO == null) {
            goodsPromotionRpcDTOs.forEach(goodsPromotionRpcDTO -> {
                goodsPromotionRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_CONDITION_NULL);
                goodsPromotionRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_CONDITION_NULL));
            });
        }
        if ((time != null
            && (promotionDO.getPromoStatus().equals(Constant.PROMO_STATUS_3)
                && promotionDO.getUpdateTime().getTime() < time.getTime())
            || promotionDO.getEndTime().getTime() < time.getTime())) {
            goodsPromotionRpcDTOs.forEach(goodsPromotionRpcDTO -> {
                goodsPromotionRpcDTO.setFlag(CommonErrorCode.B_PROMOTION_EXPIRE_ERROR);
                goodsPromotionRpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_EXPIRE_ERROR));
            });
        }
        return goodsPromotionRpcDTOs;
    }

    public static List<ImportPromotionExcelDTO> toImportPromotionExcelDTOList(List<PromotionDO> promotionDOS,
        Map<Integer, List<PromotionRuleDO>> ruleDOSMap, Map<Integer, List<PromotionRuleConditionDO>> conditionDOSMap,
        Map<Integer, List<PromotionRuleGoodsDO>> ruleGoodsDOSMap,
        Map<Integer, List<PromotionRuleConditionSpecialDO>> specialDOSMap,
        Map<Integer, List<PromotionDepartmentRelevanceDO>> departmentRelevanceDOSMap,
        List<DepartmentRpcDTO> departments) {
        List<ImportPromotionExcelDTO> promotionExcelDTOS = new ArrayList<>();
        promotionDOS.forEach(promotionDO -> {
            List<PromotionRuleDO> ruleDOS = ruleDOSMap.get(promotionDO.getId());
            String departmentNames = null;
            List<PromotionDepartmentRelevanceDO> departmentRelevanceDOS =
                departmentRelevanceDOSMap.get(promotionDO.getId());
            if (promotionDO.getDistributorScope().equals(SCOPE_DEPARTMENT)
                && !CollectionUtils.isEmpty(departmentRelevanceDOS)) {
                List<Integer> departmentIds = departmentRelevanceDOS.stream()
                    .map(PromotionDepartmentRelevanceDO::getDepartmentId).collect(Collectors.toList());
                departmentNames = departments.stream().filter(department -> departmentIds.contains(department.getId()))
                    .map(DepartmentRpcDTO::getDepartmentName).collect(Collectors.joining(","));
            }
            String finalDepartmentNames = departmentNames;
            ruleDOS.forEach(ruleDO -> {
                List<PromotionRuleGoodsDO> ruleGoodsDOS = ruleGoodsDOSMap.get(ruleDO.getId());
                if (CollectionUtils.isEmpty(ruleGoodsDOS)) {
                    return;
                }
                ruleGoodsDOS.forEach(ruleGoodsDO -> {
                    ImportPromotionExcelDTO promotionExcelDTO = new ImportPromotionExcelDTO();
                    promotionExcelDTO.setPromotionNo(String.valueOf(promotionDO.getId()));
                    promotionExcelDTO.setPromoType(promotionDO.getPromoType());
                    promotionExcelDTO.setName(promotionDO.getName());
                    promotionExcelDTO.setNameEn(promotionDO.getNameEn());
                    promotionExcelDTO.setPromoDesc(promotionDO.getPromoDesc());
                    promotionExcelDTO.setOnWayFlag(promotionDO.getOnWayFlag());
                    promotionExcelDTO.setStartTime(promotionDO.getStartTime());
                    promotionExcelDTO.setEndTime(promotionDO.getEndTime());
                    promotionExcelDTO.setAdvanceDay((int)promotionDO.getAdvanceFlag());
                    promotionExcelDTO.setRuleType(ruleDO.getRuleType());
                    if (ruleDO.getRuleType().equals(RULE_TYPE_2)) {// 单个商品
                        promotionExcelDTO.setGoodsItemNo(ruleGoodsDO.getGoodsNo());
                    } else if (ruleDO.getRuleType().equals(RULE_TYPE_3)) {
                        promotionExcelDTO.setGoodsItemNo(ruleGoodsDO.getItemCode());
                    }
                    List<PromotionRuleConditionDO> conditionDOS = conditionDOSMap.get(ruleDO.getId());
                    if (!CollectionUtils.isEmpty(conditionDOS)) {
                        conditionDOS.forEach(conditionDO -> {
                            int index = conditionDOS.indexOf(conditionDO);
                            if (conditionDO.getOneBuyCount() == null) {
                                return;
                            }
                            if (index == 0) {
                                promotionExcelDTO.setConditionName1(conditionDO.getConditionName());
                                promotionExcelDTO.setConditionNameEn1(conditionDO.getConditionNameEn());
                                promotionExcelDTO.setOneBuyCount1(conditionDO.getOneBuyCount());
                            } else if (index == 1) {
                                promotionExcelDTO.setConditionName2(conditionDO.getConditionName());
                                promotionExcelDTO.setConditionNameEn2(conditionDO.getConditionNameEn());
                                promotionExcelDTO.setOneBuyCount2(conditionDO.getOneBuyCount());
                            } else if (index == 2) {
                                promotionExcelDTO.setConditionName3(conditionDO.getConditionName());
                                promotionExcelDTO.setConditionNameEn3(conditionDO.getConditionNameEn());
                                promotionExcelDTO.setOneBuyCount3(conditionDO.getOneBuyCount());
                            }
                            if (conditionDO.getSpecialFlag() != null
                                && conditionDO.getSpecialFlag().equals(SPECIAL_FLAG_1)) {
                                promotionExcelDTO.setConditionType((short)1);
                                List<PromotionRuleConditionSpecialDO> specialDOS =
                                    specialDOSMap.get(conditionDO.getId());
                                List<PromotionRuleConditionSpecialDO> goodsSpecialDOS = specialDOS.stream()
                                    .filter(specialDO -> ruleGoodsDO.getItemId() != null
                                        && specialDO.getItemId().equals(ruleGoodsDO.getItemId()))
                                    .collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(goodsSpecialDOS)) {
                                    if (index == 0) {
                                        promotionExcelDTO
                                            .setDiscountReduction1(goodsSpecialDOS.get(0).getSpecialPrice());
                                    } else if (index == 1) {
                                        promotionExcelDTO
                                            .setDiscountReduction2(goodsSpecialDOS.get(0).getSpecialPrice());
                                    } else if (index == 2) {
                                        promotionExcelDTO
                                            .setDiscountReduction3(goodsSpecialDOS.get(0).getSpecialPrice());
                                    }
                                }
                            } else {
                                if (conditionDO.getReduceType() != null
                                    && conditionDO.getReduceType().equals(REDUCE_TYPE_1)) {
                                    // 减免
                                    promotionExcelDTO.setConditionType((short)3);
                                    if (index == 0) {
                                        promotionExcelDTO.setDiscountReduction1(conditionDO.getReduction());
                                    } else if (index == 1) {
                                        promotionExcelDTO.setDiscountReduction2(conditionDO.getReduction());
                                    } else if (index == 2) {
                                        promotionExcelDTO.setDiscountReduction3(conditionDO.getReduction());
                                    }
                                } else if (conditionDO.getReduceType() != null
                                    && conditionDO.getReduceType().equals(REDUCE_TYPE_2)) {
                                    // 折扣
                                    promotionExcelDTO.setConditionType((short)2);
                                    if (index == 0) {
                                        promotionExcelDTO.setDiscountReduction1(conditionDO.getDiscount());
                                    } else if (index == 1) {
                                        promotionExcelDTO.setDiscountReduction2(conditionDO.getDiscount());
                                    } else if (index == 2) {
                                        promotionExcelDTO.setDiscountReduction3(conditionDO.getDiscount());
                                    }
                                }
                            }
                        });
                    }
                    if (StringUtils.isNotBlank(finalDepartmentNames)) {
                        promotionExcelDTO.setDepartmentSStr(finalDepartmentNames);
                    }
                    promotionExcelDTOS.add(promotionExcelDTO);
                });
            });
        });
        return promotionExcelDTOS;
    }

}
