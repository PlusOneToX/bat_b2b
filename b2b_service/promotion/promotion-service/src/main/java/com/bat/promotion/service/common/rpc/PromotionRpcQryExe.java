package com.bat.promotion.service.common.rpc;

import static com.bat.promotion.service.common.CommonErrorCode.B_IMPORT_PROMOTION_DEPARTMENT_NULL;
import static com.bat.promotion.service.common.Constant.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.groupseckill.executor.GroupSeckillQryExe;
import com.bat.promotion.service.promotion.executor.PromotionQryExe;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorPromitonGroupSeckillRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorTreePathRpcDTO;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsRpcDTO;
import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.OrderPromotionRpcQry;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.dubboapi.system.check.api.SystemCheckServiceRpc;
import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.dubboapi.system.organization.api.SysDepartmentServiceRpc;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.promotion.api.base.MessageUtils;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.dao.groupseckill.GroupSeckillGoodsMapper;
import com.bat.promotion.dao.groupseckill.GroupSeckillMapper;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.UserGoodsItemGroupSeckillDO;
import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.common.CommonErrorCode;
import com.bat.promotion.service.user.executor.UserRpcQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:15
 */
@Component
public class PromotionRpcQryExe {

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private SystemCheckServiceRpc systemCheckServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private SysDepartmentServiceRpc sysDepartmentServiceRpc;

    @Resource
    private PromotionRuleGoodsMapper promotionRuleGoodsMapper;
    @Resource
    private GroupSeckillGoodsMapper groupSeckillGoodsMapper;
    @Resource
    private GroupSeckillMapper groupSeckillMapper;
    @Resource
    private GroupSeckillQryExe groupSeckillQryExe;
    @Resource
    private PromotionQryExe promotionQryExe;
    @Resource
    private UserRpcQryExe rpcQryExe;
    @Resource
    private PromotionRuleMapper promotionRuleMapper;
    @Resource
    private PromotionRuleConditionMapper promotionRuleConditionMapper;
    @Resource
    private PromotionRuleConditionSpecialMapper conditionSpecialMapper;
    @Resource
    private PromotionMapper promotionMapper;
    @Resource
    private UserPromotionMapper userPromotionMapper;
    @Resource
    private PromotionRuleConditionPresentMapper conditionPresentMapper;

    /**
     * 根据分销商ids获取分销商名称等信息
     * 
     * @param distributorIds
     * @return
     */
    public List<DistributorRpcDTO> listDistributorNameRpcDTO(List<Integer> distributorIds) {
        com.bat.dubboapi.distributor.common.Response<List<DistributorRpcDTO>> listResponse =
            distributorServiceRpc.distributorByIds(distributorIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    /**
     * 根据商品ids获取商品名称等信息
     * 
     * @param goodsIds
     * @return
     */
    public List<GoodsRpcDTO> listGoods(List<Integer> goodsIds) {
        com.bat.dubboapi.goods.common.Response<List<GoodsRpcDTO>> listResponse =
            goodsServiceRpc.listGoods(goodsIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    /**
     * 根据商品ids获取商品名称等信息
     * 
     * @param itemIds
     * @return
     */
    public List<GoodsItemRpcDTO> listGoodsItem(List<Integer> itemIds) {
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> listResponse =
            goodsServiceRpc.listGoodsItemByIds(itemIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    /**
     * 获取活动商品id列表
     * 
     * @param promotionIds
     * @param groupSeckillIds
     * @return
     */
    public PromotionGroupSeckillGoodsRpcDTO getPromotionGroupSeckillGoods(List<Integer> promotionIds,
        List<Integer> groupSeckillIds) {
        PromotionGroupSeckillGoodsRpcDTO goodsRpcDTO = new PromotionGroupSeckillGoodsRpcDTO();
        // 查询全部分销商可见的促销活动
        if (ObjectUtils.isEmpty(promotionIds)) {
            promotionIds = promotionRuleGoodsMapper.salesPromotionAll();
        } else {
            List<Integer> salesPromotionIds = promotionRuleGoodsMapper.salesPromotionAll();
            promotionIds.addAll(salesPromotionIds);
        }

        // 查询全部分销商可见的拼团活动
        if (ObjectUtils.isEmpty(groupSeckillIds)) {
            groupSeckillIds = groupSeckillGoodsMapper.groupActivities();
        } else {
            List<Integer> groupActivitiesIds = groupSeckillGoodsMapper.groupActivities();
            groupSeckillIds.addAll(groupActivitiesIds);
        }

        if (!CollectionUtils.isEmpty(promotionIds)) {
            List<Integer> promotionGoodsIds = promotionRuleGoodsMapper.promotionGoodsIdsByPromotionIds(promotionIds);
            goodsRpcDTO.setPromotionGoodsIds(promotionGoodsIds);
            List<Integer> promotionStepGoodsIds =
                promotionRuleGoodsMapper.promotionStepGoodsIdsByPromotionIds(promotionIds);
            goodsRpcDTO.setPromotionStepGoodsIds(promotionStepGoodsIds);
        }
        if (!CollectionUtils.isEmpty(groupSeckillIds)) {
            List<Integer> groupGoodsIds = groupSeckillGoodsMapper.groupGoodsIdsByGroupSeckillIds(groupSeckillIds);
            List<Integer> seckillGoodsIds = groupSeckillGoodsMapper.seckillGoodsIdsByGroupSeckillIds(groupSeckillIds);
            goodsRpcDTO.setGroupGoodsIds(groupGoodsIds);
            goodsRpcDTO.setSeckillGoodsIds(seckillGoodsIds);
        }
        return goodsRpcDTO;
    }

    /**
     * 计算商品活动价格
     * 
     * @param qry
     * @return
     */
    public Response<List<GoodsItemPromotionPriceRpcDTO>> goodsItemPromotionPrice(GoodsItemPromotionPriceRpcQry qry) {
        List<GoodsItemPriceRpcQry> goodsItemPrices = qry.getGoodsItemPrices();
        List<GoodsItemPromotionPriceRpcDTO> rpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemPrices)) {
            // 先处理未选择活动的商品，默认选上一个活动（优先选择拼团描述活动商品）
            List<GoodsItemPriceRpcQry> noGoodsItemPromotions = new ArrayList<>();
            for (GoodsItemPriceRpcQry goodsItemPrice : goodsItemPrices) {
                if (goodsItemPrice.getSpellGroupId() == null && goodsItemPrice.getOrderPromotionId() == null
                    && goodsItemPrice.getGoodsPromotionId() == null) {
                    noGoodsItemPromotions.add(goodsItemPrice);
                }
            }
            if (!CollectionUtils.isEmpty(noGoodsItemPromotions)) {
                Response<List<GoodsItemPromotionRpcDTO>> listResponse =
                    goodsItemPromotion(PromotionRpcConvertor.toGoodsItemRpcQryList(noGoodsItemPromotions),
                        qry.getDistributorId(), PRESENT_FLAG_0);
                List<GoodsItemPromotionRpcDTO> goodsItemPromotions = listResponse.getData();
                if (listResponse.isSuccess() && !CollectionUtils.isEmpty(goodsItemPromotions)) {
                    PromotionRpcConvertor.getGoodsItemPromotion(noGoodsItemPromotions, goodsItemPromotions);
                }
            }
            // 先计算拼团秒杀活动价格
            List<GoodsItemPriceRpcQry> goodsSpellGroupPrices = goodsItemPrices.stream()
                .filter(goodsItemPrice -> goodsItemPrice.getSpellGroupId() != null).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(goodsSpellGroupPrices)) {
                rpcDTOS.addAll(groupSeckillQryExe.goodsSpellGroupPrices(goodsSpellGroupPrices, qry.getOrderTime()));
            }
            // 再计算促销活动价格
            List<GoodsItemPriceRpcQry> goodsPromotionPrices =
                goodsItemPrices.stream().filter(goodsItemPrice -> goodsItemPrice.getGoodsPromotionId() != null
                    || goodsItemPrice.getOrderPromotionId() != null).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(goodsPromotionPrices)) {
                rpcDTOS.addAll(promotionQryExe.goodsPromotionPrices(goodsPromotionPrices, qry.getOrderTime()));
            }
        }
        return Response.of(rpcDTOS);
    }

    /**
     * 根据货品ids获取活动数据
     * 
     * @param goodsItems
     * @return
     */
    public Response<List<GoodsItemPromotionRpcDTO>> goodsItemPromotion(List<GoodsItemRpcQry> goodsItems,
        Integer distributorId, Short presentFlag) {
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            rpcQryExe.getDistributorPromotionGroupSeckill(distributorId);
        if (promitonGroupSeckillRpcDTO != null && !CollectionUtils.isEmpty(goodsItems)) {
            // 促销活动
            List<PromotionDO> promotionDOS = new ArrayList<>();
            // 促销活动规则商品列表
            List<PromotionRuleGoodsDO> ruleGoodsDOS = new ArrayList<>();
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
            List<Integer> goodsIds =
                goodsItems.stream().map(GoodsItemRpcQry::getGoodsId).distinct().collect(Collectors.toList());
            List<Integer> promotionIds = promitonGroupSeckillRpcDTO.getPromotionIds();
            String promotionTypes = promitonGroupSeckillRpcDTO.getPromotionTypes();
            List<String> promoTypes = null;
            if (StringUtils.isNotBlank(promotionTypes)) {
                promoTypes = Arrays.asList(promotionTypes.split(","));
            }
            // 商品促销活动
            ruleGoodsDOS = promotionRuleGoodsMapper.listPromotionRuleGoodsByPromotionIdsAndGoodsIds(promotionIds,
                promoTypes, goodsIds);
            if (!CollectionUtils.isEmpty(ruleGoodsDOS)) {
                List<Integer> goodsRuleIds = ruleGoodsDOS.stream().map(PromotionRuleGoodsDO::getPromotionRuleId)
                    .distinct().collect(Collectors.toList());
                List<PromotionRuleDO> goodsRuleDOS = promotionRuleMapper.listByIds(goodsRuleIds);
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
                ruleConditionDOS = promotionRuleConditionMapper.listByPromotionRuleIds(ruleIds);
                // 赠品数据组装
                if (presentFlag.equals(PRESENT_FLAG_1)) {
                    List<Integer> presentConditionIds = ruleConditionDOS.stream()
                        .filter(ruleConditionDO -> ruleConditionDO.getSpecialFlag().equals(SPECIAL_FLAG_0)
                            && ruleConditionDO.getReduceOrPresent().equals(CONDITION_PRESENT))
                        .map(PromotionRuleConditionDO::getId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(presentConditionIds)) {
                        conditionPresentDOS = conditionPresentMapper.listByConditionIds(presentConditionIds);
                        List<Integer> itemIds = conditionPresentDOS.stream()
                            .map(PromotionRuleConditionPresentDO::getItemId).collect(Collectors.toList());
                        itemRpcDTOS = rpcQryExe.listGoodsItemByIds(itemIds);
                    }
                }
                List<Integer> specialConditionIds = ruleConditionDOS.stream()
                    .filter(ruleConditionDO -> ruleConditionDO.getSpecialFlag().equals(SPECIAL_FLAG_1))
                    .map(PromotionRuleConditionDO::getId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(specialConditionIds)) {
                    conditionSpecialDOS =
                        conditionSpecialMapper.listByConditionIdsAndGoodsIds(specialConditionIds, goodsIds);
                }
                promotionIds = ruleDOS.stream().map(PromotionRuleDO::getPromotionId).collect(Collectors.toList());
                promotionDOS = promotionMapper.listByIds(promotionIds);
            }
            // 拼团秒杀活动
            if (promitonGroupSeckillRpcDTO.getPromotionScope().equals(PROMOTION_SCOPE_1)
                || StringUtils.isBlank(promotionTypes) || promotionTypes.contains(PROMOTION_TYPE_3)) {
                List<Integer> groupSeckilIds = promitonGroupSeckillRpcDTO.getGroupSeckilIds();
                groupSeckillDOS =
                    groupSeckillGoodsMapper.listGroupSeckillGoodsByGroupSeckillIdsAndGoodsIds(groupSeckilIds, goodsIds);
            }
            List<GoodsItemPromotionRpcDTO> rpcDTOS =
                PromotionRpcConvertor.toGoodsItemPromotionRpcDTOList(promotionDOS, ruleDOS, ruleConditionDOS,
                    conditionPresentDOS, itemRpcDTOS, conditionSpecialDOS, ruleGoodsDOS, groupSeckillDOS, goodsItems);
            return Response.of(rpcDTOS);
        }
        return Response.buildSuccess();
    }

    /**
     * 获取所有分销商可视活动ids
     * 
     * @return
     */
    public Response<PromotionGroupSeckillIdsByAllDistributorDTO> promotionGroupSeckillIdsByAllDistributor() {
        List<Integer> promotionIds = promotionMapper.listIdByAllDistributor();
        List<Integer> groupSeckillIds = groupSeckillMapper.listIdByAllDistributor();
        PromotionGroupSeckillIdsByAllDistributorDTO allDistributorDTO =
            new PromotionGroupSeckillIdsByAllDistributorDTO();
        allDistributorDTO.setPromotionIds(promotionIds);
        allDistributorDTO.setGroupSeckillIds(groupSeckillIds);
        return Response.of(allDistributorDTO);
    }

    /**
     * 根据订单活动条件ids获取活动信息
     * 
     * @param qry
     * @return
     */
    public Response<OrderPromotionRpcDTO> promotionByOrderPromotionIds(OrderPromotionRpcQry qry) {
        List<PromotionRuleConditionDO> conditionDOS = new ArrayList<>();
        List<PromotionRuleDO> ruleDOS = new ArrayList<>();
        List<PromotionDO> promotionDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(qry.getPromotionIds())) {
            conditionDOS = promotionRuleConditionMapper.listByIds(qry.getPromotionIds());
            List<Integer> ruleIds =
                conditionDOS.stream().map(PromotionRuleConditionDO::getPromotionRuleId).collect(Collectors.toList());
            ruleDOS = promotionRuleMapper.listByIds(ruleIds);
            List<Integer> promotionIds =
                ruleDOS.stream().map(PromotionRuleDO::getPromotionId).collect(Collectors.toList());
            promotionDOS = promotionMapper.listByIds(promotionIds);
        }
        List<GroupSeckillDO> groupSeckillDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(qry.getSpellGroupIds())) {
            groupSeckillDOS = groupSeckillMapper.listGroupSeckillByIds(qry.getSpellGroupIds());
        }
        OrderPromotionRpcDTO rpcDTO =
            PromotionRpcConvertor.toOrderPromotionRpcDTO(conditionDOS, ruleDOS, promotionDOS, groupSeckillDOS);
        return Response.of(rpcDTO);
    }

    public PromotionDistributorRpcDTO getVisiblePromotionByDistributor(Integer distributorId, Integer scalePriceId,
        Integer salesId, Integer departmentId) {
        List<Integer> promotionIds = new ArrayList<>();
        List<Integer> distributorIdPromotionIds = promotionMapper.listIdByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(distributorIdPromotionIds)) {
            promotionIds.addAll(distributorIdPromotionIds);
        }
        List<Integer> scalePriceIdPromotionIds = promotionMapper.listIdByScalePriceId(scalePriceId);
        if (!CollectionUtils.isEmpty(scalePriceIdPromotionIds)) {
            promotionIds.addAll(scalePriceIdPromotionIds);
        }
        List<Integer> salesIdPromotionIds = promotionMapper.listIdBySalesId(salesId);
        if (!CollectionUtils.isEmpty(salesIdPromotionIds)) {
            promotionIds.addAll(salesIdPromotionIds);
        }
        List<Integer> departmentIdPromotionIds = promotionMapper.listIdByDepartmentId(departmentId);
        if (!CollectionUtils.isEmpty(departmentIdPromotionIds)) {
            promotionIds.addAll(departmentIdPromotionIds);
        }
        List<Integer> groupSeckillIds = new ArrayList<>();
        List<Integer> distributorIdGroupSeckillIds = groupSeckillMapper.listIdByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(distributorIdGroupSeckillIds)) {
            groupSeckillIds.addAll(distributorIdGroupSeckillIds);
        }
        List<Integer> scalePriceIdGroupSeckillIds = groupSeckillMapper.listIdByScalePriceId(scalePriceId);
        if (!CollectionUtils.isEmpty(scalePriceIdGroupSeckillIds)) {
            groupSeckillIds.addAll(scalePriceIdGroupSeckillIds);
        }
        List<Integer> salesIdGroupSeckillIds = groupSeckillMapper.listIdBySalesId(salesId);
        if (!CollectionUtils.isEmpty(salesIdGroupSeckillIds)) {
            groupSeckillIds.addAll(salesIdGroupSeckillIds);
        }
        List<Integer> departmentIdGroupSeckillIds = groupSeckillMapper.listIdByDepartmentId(departmentId);
        if (!CollectionUtils.isEmpty(departmentIdGroupSeckillIds)) {
            groupSeckillIds.addAll(departmentIdGroupSeckillIds);
        }
        PromotionDistributorRpcDTO rpcDTO = new PromotionDistributorRpcDTO();
        rpcDTO.setPromotionIds(promotionIds);
        rpcDTO.setGroupSeckillIds(groupSeckillIds);
        return rpcDTO;
    }

    /**
     * 获取分销商审批流
     *
     * @param ext
     * @return
     */
    public CheckConfigDetailRpcDTO getCheckFlows(Short ext) {
        com.bat.dubboapi.system.common.Response<CheckConfigDetailRpcDTO> response =
            systemCheckServiceRpc.getCheckConfigDetail(ext);
        if (response != null && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 获取该分销商所有上级以及本身
     * 
     * @return
     */
    public List<Integer> getDistributorTreePaths(Integer distributorId) {
        List<Integer> distributorIds = new ArrayList<>();
        if (distributorId == null) {
            return distributorIds;
        }
        com.bat.dubboapi.distributor.common.Response<List<DistributorTreePathRpcDTO>> response =
            distributorServiceRpc.getDistributorTreePaths(distributorId);
        if (response == null) {
            throw PromotionException.buildException(CommonErrorCode.B_PROMOTION_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        List<DistributorTreePathRpcDTO> distributorTreePaths = response.getData();
        if (distributorTreePaths == null) {
            throw PromotionException.buildException(CommonErrorCode.B_PROMOTION_DEAL_ERROR);
        }
        distributorIds = distributorTreePaths.stream().map(DistributorTreePathRpcDTO::getDistributorAncestorId)
            .collect(Collectors.toList());
        distributorIds.add(distributorId);
        return distributorIds;
    }

    /**
     * 根据商品编码获取商品id
     *
     * @param goodsNo
     * @return
     */
    public Integer getGoodsIdByGoodsNo(String goodsNo) {
        com.bat.dubboapi.goods.common.Response<Integer> response = goodsServiceRpc.getGoodsIdByGoodsNo(goodsNo);
        return response.getData();
    }

    /**
     * 根据货品编码或条形码获取货品ids
     *
     * @param itemCodeOrBarCode
     * @return
     */
    public List<GoodsItemRpc> getGoodsItemIdsByItemCodeOrBarCode(String itemCodeOrBarCode) {
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpc>> response =
            goodsServiceRpc.getGoodsItemIdsByItemCodeOrBarCode(itemCodeOrBarCode);
        return response.getData();
    }

    /**
     * 根据部门ids获取部门列表
     * 
     * @param ids
     * @return
     */
    public List<DepartmentRpcDTO> getDepartmentsByIds(List<Integer> ids) {
        com.bat.dubboapi.system.common.Response<List<DepartmentRpcDTO>> response =
            sysDepartmentServiceRpc.getDepartmentByIds(ids);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw PromotionException.buildException(B_IMPORT_PROMOTION_DEPARTMENT_NULL,
                MessageUtils.get(B_IMPORT_PROMOTION_DEPARTMENT_NULL));
        }
    }
}
