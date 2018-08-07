package com.bat.system.service.promotion.executor;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.dao.promotion.GoodsPromotionMapper;
import com.bat.system.dao.promotion.dataobject.DistributorGoodsPromotionRelevanceDO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.api.GoodsPromotionServiceRpc;
import com.bat.system.api.base.SystemException;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/29 9:10
 */
@Component
public class GoodsPromotionRpcCmdExe {

    @DubboReference(check = false, timeout = 30000)
    private GoodsPromotionServiceRpc goodsPromotionServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @Resource
    private GoodsPromotionMapper goodsPromotionMapper;

    public void distributorGoodsRelevance(Integer id, Short distributorScope, List<Integer> scalePriceIds,
        List<Integer> distributorIds, List<Integer> departmentIds, List<Integer> adminIds) {

        // 可视关系
        List<Integer> addDistributorIds = new ArrayList<>();
        Response<List<Integer>> listResponse = null;
        if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
            listResponse = goodsPromotionServiceRpc.listDistributorIdByScalePriceIdsTwo(scalePriceIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
            addDistributorIds.addAll(distributorIds);
        } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(departmentIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdBySalesIdsAndOneTreeNode(adminIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        }

        if (listResponse != null && !listResponse.isSuccess()) {
            throw SystemException.buildException(ErrorCode.B_GOODS_PROMOTION_DISTRIBUTOR_ERROR);
        }

        distributorGoodsPromotionRelevanceByGoodsId(addDistributorIds, id);
    }

    /***
     * <h2>更新商品推广的可视范围</h2>
     * 
     * @param distributorIds
     * @param id
     */
    public void distributorGoodsPromotionRelevanceByGoodsId(List<Integer> distributorIds, Integer id) {
        // 根据分销商id查询分销商与商品推广关联记录
        List<DistributorGoodsPromotionRelevanceDO> relevanceDOS1 = null;
        if (ObjectUtils.isNotEmpty(distributorIds)) {
            relevanceDOS1 = goodsPromotionMapper.listByDistributorIds(distributorIds);
        }

        // 根据商品推广id查询该商品与所有分销商推广的记录
        List<DistributorGoodsPromotionRelevanceDO> relevanceDOS2 =
            goodsPromotionMapper.listByGoodsPromotionId("," + String.valueOf(id) + ",");

        List<DistributorGoodsPromotionRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorGoodsPromotionRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视商品推广 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            // 过滤出需要修改的DistributorGoodsPromotionRelevanceDO
            List<DistributorGoodsPromotionRelevanceDO> updateOrDeleteRelevanceDOS =
                relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<Integer> goodsPromotionId = Arrays
                        .stream(relevanceDO.getGoodsPromotionId()
                            .substring(1, relevanceDO.getGoodsPromotionId().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    goodsPromotionId =
                        goodsPromotionId.stream().filter(gpid -> !gpid.equals(id)).collect(Collectors.toList());

                    if (!CollectionUtils.isEmpty(goodsPromotionId)) {
                        relevanceDO.setGoodsPromotionId("," + StringUtils.join(goodsPromotionId, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                });
            }
        }
        /** 添加分销商可视商品 */
        Map<Integer, DistributorGoodsPromotionRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream().collect(
                Collectors.toMap(DistributorGoodsPromotionRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorGoodsPromotionRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                String goodsIdStr = String.valueOf(id) + ",";
                if (relevanceDO1 != null && !relevanceDO1.getGoodsPromotionId().contains(goodsIdStr)) {
                    relevanceDO1.setGoodsPromotionId(relevanceDO1.getGoodsPromotionId() + goodsIdStr);
                    updateRelevanceDOS.add(relevanceDO1);
                } else if (relevanceDO1 == null) {
                    DistributorGoodsPromotionRelevanceDO relevanceDO = new DistributorGoodsPromotionRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setGoodsPromotionId("," + goodsIdStr);
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            goodsPromotionMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            goodsPromotionMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            goodsPromotionMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * <h2>查询最大的分销商</h2>
     * 
     * @param userId
     * @return
     */
    public Integer goodsPromotionMaximumDistributor(Integer userId) {
        Response<Integer> response = goodsPromotionServiceRpc.goodsPromotionMaximumDistributor(userId);
        Integer distributorMaxId = null;
        if (response.isSuccess()) {
            distributorMaxId = response.getData();
        }
        return distributorMaxId;
    }
}
