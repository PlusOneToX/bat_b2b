package com.bat.promotion.service.user.executor;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorPromitonGroupSeckillRpcDTO;
import com.bat.dubboapi.goods.goods.dto.UserGoodsItemListRpcQry;
import com.bat.dubboapi.goods.goods.dto.UserGoodsSortListRpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.user.dto.distributor.UserGroupSeckillListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionRuleGoodsListQry;
import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.dao.groupseckill.UserGroupSeckillMapper;
import com.bat.promotion.dao.groupseckill.dataobject.UserGoodsItemGroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.UserGroupSeckillGoodsDO;
import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 8:44
 */
@Component
@Slf4j
public class UserDistributorQryExe {

    @Resource
    private UserPromotionMapper userPromotionMapper;
    @Resource
    private PromotionRuleMapper promotionRuleMapper;
    @Resource
    private PromotionRuleGoodsMapper promotionRuleGoodsMapper;
    @Resource
    private PromotionRuleConditionMapper ruleConditionMapper;
    @Resource
    private PromotionRuleConditionPresentMapper ruleConditionPresentMapper;
    @Resource
    private PromotionRuleConditionSpecialMapper ruleConditionSpecialMapper;
    @Resource
    private UserRpcQryExe rpcQryExe;
    @Resource
    private UserGroupSeckillMapper userGroupSeckillMapper;

    /**
     * 根据搜索条件查找促销活动列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserPromotionDTO> listPromotion(UserPromotionListQry qry, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw PromotionException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        PageInfo pageInfo = new PageInfo();
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            rpcQryExe.getDistributorPromotionGroupSeckill(Integer.valueOf(userId));
        List<UserPromotionDO> promotionDOS = new ArrayList<>();
        if (promitonGroupSeckillRpcDTO != null) {
            BeanMap beanMap = BeanMap.create(qry);
            Map<String, Object> qryMap = new HashMap();
            qryMap.putAll(beanMap);
            List<Integer> promotionIds = promitonGroupSeckillRpcDTO.getPromotionIds();
            if (!CollectionUtils.isEmpty(promotionIds)) {
                qryMap.put("promotionIds", promotionIds);
            }
            String promotionTypes = promitonGroupSeckillRpcDTO.getPromotionTypes();
            if (StringUtils.isNotBlank(promotionTypes)) {
                List<String> promoTypes = Arrays.asList(promotionTypes.split(","));
                qryMap.put("promoTypes", promoTypes);
            }
            PageHelper.startPage(qry.getPage(), qry.getSize());
            promotionDOS = userPromotionMapper.listPromotion(qryMap);
            pageInfo.setList(promotionDOS);
            List<UserPromotionDTO> dtos = UserConvertor.toUserPromotionDTOList(pageInfo.getList());
            pageInfo.setList(dtos);
        }
        return pageInfo;
    }

    /**
     * 根据搜索条件查找促销活动id查找活动规则列表(不分页)
     * 
     * @param qry
     * @return
     */
    public List<UserPromotionRuleDTO> listPromotionRule(BaseId qry) {
        List<PromotionRuleDO> ruleDOS = promotionRuleMapper.listByPromotionId(qry.getId());
        List<PromotionRuleConditionDO> conditionDOS = ruleConditionMapper.listByPromotionId(qry.getId());
        List<PromotionRuleConditionPresentDO> presentDOS = ruleConditionPresentMapper.listByPromotionId(qry.getId());
        List<PromotionRuleConditionSpecialDO> specialDOS = ruleConditionSpecialMapper.listByPromotionId(qry.getId());
        List<GoodsItemRpcDTO> goodsItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(presentDOS)) {
            List<Integer> itemIds =
                presentDOS.stream().map(PromotionRuleConditionPresentDO::getItemId).collect(Collectors.toList());
            goodsItems = rpcQryExe.listGoodsItemByIds(itemIds);
        }
        List<UserPromotionRuleDTO> dtos =
            UserConvertor.toUserPromotionRuleDTOList(ruleDOS, conditionDOS, specialDOS, presentDOS, goodsItems);
        return dtos;
    }

    /**
     * 根据促销活动规则id查找货品列表(分页)
     * 
     * @param qry
     * @param userId
     * @return
     */
    public PageInfo<UserPromotionRuleGoodsDTO> listPromotionRuleGoods(UserPromotionRuleGoodsListQry qry,
                                                                      String userId) {
        if (StringUtils.isBlank(userId)) {
            throw PromotionException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        PromotionRuleDO ruleDO = promotionRuleMapper.selectByPrimaryKey(qry.getId());
        PageInfo pageInfo = new PageInfo();
        UserGoodsItemListRpcQry rpcQry = new UserGoodsItemListRpcQry();
        rpcQry.setDistributorId(Integer.valueOf(userId));
        rpcQry.setPage(qry.getPage());
        rpcQry.setSize(qry.getSize());
        if (ruleDO.getRuleType().equals(Constant.RULE_TYPE_2)) {
            List<Integer> goodsIds = promotionRuleGoodsMapper.selectGoodsIdsByPromotionRuleId(qry.getId());
            rpcQry.setGoodsIds(goodsIds);
        } else if (ruleDO.getRuleType().equals(Constant.RULE_TYPE_3)) {
            List<Integer> itemIds = promotionRuleGoodsMapper.selectItemIdsByPromotionRuleId(qry.getId());
            rpcQry.setItemIds(itemIds);
        }
        if (!CollectionUtils.isEmpty(rpcQry.getGoodsIds()) || !CollectionUtils.isEmpty(rpcQry.getItemIds())) {
            pageInfo = rpcQryExe.listDistributorGoodsItem(rpcQry);
        }
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            pageInfo.setList(UserConvertor.toUserPromotionRuleGoodsDTOList(ruleDO.getPromotionId(), ruleDO.getId(),
                pageInfo.getList()));
        }
        return pageInfo;
    }

    /**
     * 根据搜索条件查找拼团秒杀商品列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserGroupSeckillGoodsDTO> listGroupseckillGoods(UserGroupSeckillListQry qry, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw PromotionException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        PageInfo pageInfo = new PageInfo();
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            rpcQryExe.getDistributorPromotionGroupSeckill(Integer.valueOf(userId));
        List<UserGroupSeckillGoodsDO> goodsDOS = new ArrayList<>();
        // 判断是否参与活动
        if (promitonGroupSeckillRpcDTO != null
            && (promitonGroupSeckillRpcDTO.getPromotionScope().equals(Constant.PROMOTION_SCOPE_1)
                || StringUtils.isBlank(promitonGroupSeckillRpcDTO.getPromotionTypes())
                || promitonGroupSeckillRpcDTO.getPromotionTypes().contains(Constant.PROMOTION_TYPE_3))) {
            BeanMap beanMap = BeanMap.create(qry);
            Map<String, Object> qryMap = new HashMap();
            qryMap.putAll(beanMap);
            List<Integer> groupSeckillIds = promitonGroupSeckillRpcDTO.getGroupSeckilIds();
            if (!CollectionUtils.isEmpty(groupSeckillIds)) {
                qryMap.put("groupSeckillIds", groupSeckillIds);
            }
            goodsDOS = userGroupSeckillMapper.listGoodsItemIds(qryMap);
        }
        if (!CollectionUtils.isEmpty(goodsDOS)) {
            List<Integer> goodsIds = new ArrayList<>();
            goodsDOS.forEach(goodsDO -> {
                if (!goodsIds.contains(goodsDO.getGoodsId())) {
                    goodsIds.add(goodsDO.getGoodsId());
                }
            });
            UserGoodsSortListRpcQry rpcQry = new UserGoodsSortListRpcQry();
            rpcQry.setDistributorId(Integer.valueOf(userId));
            rpcQry.setPage(qry.getPage());
            rpcQry.setSize(qry.getSize());
            rpcQry.setGoodsIds(goodsIds);
            pageInfo = rpcQryExe.listDistributorGoodsSort(rpcQry);
            if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                pageInfo.setList(UserConvertor.toUserGroupSeckillGoodsDTOList(goodsDOS, pageInfo.getList()));
            } else {
                pageInfo.setList(new ArrayList());
            }
        } else {
            pageInfo.setList(new ArrayList());
        }
        return pageInfo;
    }

    /**
     * 根据商品id查找活动
     * 
     * @param qry
     * @param userId
     * @return
     */
    public UserGoodsPromotionDTO promotionGroupSeckillGoods(BaseId qry, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw PromotionException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            rpcQryExe.getDistributorPromotionGroupSeckill(Integer.valueOf(userId));
        if (promitonGroupSeckillRpcDTO != null) {
            // 促销活动
            List<UserPromotionDO> promotionDOS = new ArrayList<>();
            // 促销活动规则
            List<PromotionRuleDO> ruleDOS = new ArrayList<>();
            // 规则条件
            List<PromotionRuleConditionDO> ruleConditionDOS = new ArrayList<>();
            // 条件对应的赠品
            List<PromotionRuleConditionPresentDO> conditionPresentDOS = new ArrayList<>();
            // 条件对应的特价
            List<PromotionRuleConditionSpecialDO> conditionSpecialDOS = new ArrayList<>();
            // 商品拼团秒杀活动
            List<UserGoodsItemGroupSeckillDO> groupSeckillDOS = new ArrayList<>();
            // 赠品货品信息
            List<GoodsItemRpcDTO> itemRpcDTOS = new ArrayList<>();
            List<Integer> promotionIds = promitonGroupSeckillRpcDTO.getPromotionIds();
            String promotionTypes = promitonGroupSeckillRpcDTO.getPromotionTypes();
            List<String> promoTypes = null;
            if (StringUtils.isNotBlank(promotionTypes) && !promotionTypes.equals("null")) {
                promoTypes = Arrays.asList(promotionTypes.split(","));
            }
            // 组装商品促销活动
            // 促销活动规则商品列表
            List<PromotionRuleGoodsDO> ruleGoodsDOS = userPromotionMapper
                .listPromotionRuleGoodsByPromotionIdsAndGoodsId(promotionIds, promoTypes, qry.getId());
            if (!CollectionUtils.isEmpty(ruleGoodsDOS)) {
                List<Integer> goodsRuleIds = ruleGoodsDOS.stream().map(PromotionRuleGoodsDO::getPromotionRuleId)
                    .distinct().collect(Collectors.toList());
                List<PromotionRuleDO> goodsRuleDOS = userPromotionMapper.listPromotionRuleByIds(goodsRuleIds);
                if (!CollectionUtils.isEmpty(goodsRuleDOS)) {
                    ruleDOS.addAll(goodsRuleDOS);
                }
            }
            // 组装订单活动
            List<PromotionRuleDO> orderRuleDOS = userPromotionMapper.listOrderPromotionRule(promotionIds, promoTypes);
            if (!CollectionUtils.isEmpty(orderRuleDOS)) {
                ruleDOS.addAll(orderRuleDOS);
            }
            List<Integer> ruleIds =
                ruleDOS.stream().map(PromotionRuleDO::getId).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(ruleIds)) {
                ruleConditionDOS = userPromotionMapper.listPromotionRuleConditionByPromotionRuleIds(ruleIds);
                List<Integer> presentConditionIds = ruleConditionDOS.stream()
                    .filter(ruleConditionDO -> (ruleConditionDO.getSpecialFlag() == null
                        || ruleConditionDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_0))
                        && ruleConditionDO.getReduceOrPresent().equals(Constant.CONDITION_PRESENT))
                    .map(PromotionRuleConditionDO::getId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(presentConditionIds)) {
                    conditionPresentDOS =
                        userPromotionMapper.listPresentByPromotionRuleConditionIds(presentConditionIds);
                    List<Integer> itemIds = conditionPresentDOS.stream().map(PromotionRuleConditionPresentDO::getItemId)
                        .collect(Collectors.toList());
                    itemRpcDTOS = rpcQryExe.listGoodsItemByIds(itemIds);
                }
                List<Integer> specialConditionIds = ruleConditionDOS.stream()
                    .filter(ruleConditionDO -> ruleConditionDO.getSpecialFlag() != null
                        && ruleConditionDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_1))
                    .map(PromotionRuleConditionDO::getId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(specialConditionIds)) {
                    conditionSpecialDOS = userPromotionMapper
                        .listSpecialByPromotionRuleConditionIdsAndGoodsId(specialConditionIds, qry.getId());
                }
                promotionIds = ruleDOS.stream().map(PromotionRuleDO::getPromotionId).collect(Collectors.toList());
                promotionDOS = userPromotionMapper.listPromotionByIds(promotionIds);
            }
            // 拼团秒杀活动
            if (promitonGroupSeckillRpcDTO.getPromotionScope().equals(Constant.PROMOTION_SCOPE_1)
                || StringUtils.isBlank(promotionTypes) || promotionTypes.contains(Constant.PROMOTION_TYPE_3)) {
                List<Integer> groupSeckilIds = promitonGroupSeckillRpcDTO.getGroupSeckilIds();
                groupSeckillDOS = userGroupSeckillMapper
                    .listGroupSeckillGoodsByGroupSeckillIdsAndGoodsId(groupSeckilIds, qry.getId());
            }
            UserGoodsPromotionDTO dto =
                UserConvertor.toUserGoodsItemPromotionDTO(promotionDOS, ruleDOS, ruleConditionDOS, conditionPresentDOS,
                    itemRpcDTOS, conditionSpecialDOS, ruleGoodsDOS, groupSeckillDOS, qry.getId());
            return dto;
        }
        return null;
    }

    /**
     * 获取订单促销活动列表
     * 
     * @param userId
     * @return
     */
    public List<UserPromotionDTO> listPromotionOrder(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw PromotionException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            rpcQryExe.getDistributorPromotionGroupSeckill(Integer.valueOf(userId));
        if (promitonGroupSeckillRpcDTO != null) {
            // 促销活动
            List<UserPromotionDO> promotionDOS = new ArrayList<>();
            // 促销活动规则
            List<PromotionRuleDO> ruleDOS = new ArrayList<>();
            // 规则条件
            List<PromotionRuleConditionDO> ruleConditionDOS = new ArrayList<>();
            // 条件对应的赠品
            List<PromotionRuleConditionPresentDO> conditionPresentDOS = new ArrayList<>();
            // 赠品货品信息
            List<GoodsItemRpcDTO> itemRpcDTOS = new ArrayList<>();
            List<Integer> promotionIds = promitonGroupSeckillRpcDTO.getPromotionIds();
            String promotionTypes = promitonGroupSeckillRpcDTO.getPromotionTypes();
            List<String> promoTypes = null;
            if (StringUtils.isNotBlank(promotionTypes)) {
                promoTypes = Arrays.asList(promotionTypes.split(","));
            }
            ruleDOS = userPromotionMapper.listOrderPromotionRule(promotionIds, promoTypes);
            if (!CollectionUtils.isEmpty(ruleDOS)) {
                List<Integer> ruleIds = ruleDOS.stream().map(PromotionRuleDO::getId).collect(Collectors.toList());
                ruleConditionDOS = userPromotionMapper.listPromotionRuleConditionByPromotionRuleIds(ruleIds);
                List<Integer> presentConditionIds = ruleConditionDOS.stream()
                    .filter(ruleConditionDO -> ruleConditionDO.getSpecialFlag().equals(Constant.SPECIAL_FLAG_0)
                        && ruleConditionDO.getReduceOrPresent().equals(Constant.CONDITION_PRESENT))
                    .map(PromotionRuleConditionDO::getId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(presentConditionIds)) {
                    conditionPresentDOS =
                        userPromotionMapper.listPresentByPromotionRuleConditionIds(presentConditionIds);
                    List<Integer> itemIds = conditionPresentDOS.stream().map(PromotionRuleConditionPresentDO::getItemId)
                        .collect(Collectors.toList());
                    itemRpcDTOS = rpcQryExe.listGoodsItemByIds(itemIds);
                }
                promotionIds = ruleDOS.stream().map(PromotionRuleDO::getPromotionId).collect(Collectors.toList());
                promotionDOS = userPromotionMapper.listPromotionByIds(promotionIds);
            }
            List<UserPromotionDTO> dtos = UserConvertor.toUserPromotionDTOList(promotionDOS, ruleDOS, ruleConditionDOS,
                conditionPresentDOS, itemRpcDTOS);
            return dtos;
        }
        return null;
    }

    /**
     * 根据促销活动条件id获取赠品列表
     * 
     * @param qry
     * @return
     */
    public List<UserPromotionRuleConditionPresentDTO> listConditionPresent(BaseId qry) {
        List<PromotionRuleConditionPresentDO> presentDOS = ruleConditionPresentMapper.listByConditionId(qry.getId());
        List<GoodsItemRpcDTO> itemRpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(presentDOS)) {
            List<Integer> itemIds =
                presentDOS.stream().map(PromotionRuleConditionPresentDO::getItemId).collect(Collectors.toList());
            itemRpcDTOS = rpcQryExe.listGoodsItemByIds(itemIds);
        }
        List<UserPromotionRuleConditionPresentDTO> presentDTOS =
            UserConvertor.toUserPromotionRuleConditionPresentDTOList(presentDOS, itemRpcDTOS);
        return presentDTOS;
    }
}
