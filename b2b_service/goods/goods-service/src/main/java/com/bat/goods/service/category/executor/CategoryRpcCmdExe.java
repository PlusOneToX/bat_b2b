package com.bat.goods.service.category.executor;

import static com.bat.goods.service.brand.executor.ErrorCode.B_GOODS_BRAND_DISTRIBUTOR_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.service.common.Constant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.category.dto.CategoryCmd;
import com.bat.goods.dao.category.CategoryMapper;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/6 13:55
 */
@Component
public class CategoryRpcCmdExe {

    @Resource
    private CategoryMapper categoryMapper;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 更新分销商对应品类的可视范围
     * 
     * @param categoryId
     * @param cmd
     */
    public void distributorCategoryRelevance(Integer categoryId, CategoryCmd cmd) {
        // 可视关系
        List<Integer> addDistributorIds = new ArrayList<>();
        Short distributorScope = cmd.getDistributorScope();
        Response<List<Integer>> listResponse = null;
        if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
            listResponse = distributorServiceRpc.listDistributorIdByScalePriceIds(cmd.getScalePriceIds(), cmd.getId());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
            addDistributorIds.addAll(cmd.getDistributorIds());
        } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(cmd.getDepartmentIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdBySalesIdsAndOneTreeNode(cmd.getAdminIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
            listResponse = distributorServiceRpc.listIdByDistributorGroupIdsAndOneTreeNode(cmd.getAdminIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        }
        if (listResponse != null && !listResponse.isSuccess()) {
            throw GoodsException.buildException(B_GOODS_BRAND_DISTRIBUTOR_ERROR);
        }
        // 不可视关系
        List<Integer> delDistributorIds = new ArrayList<>();
        Short distributorScopeNo = cmd.getDistributorScopeNo();
        if (distributorScopeNo.equals(Constant.SCOPE_SCALE_PRICE)) {
            listResponse =
                distributorServiceRpc.listDistributorIdByScalePriceIds(cmd.getScalePriceNoIds(), cmd.getId());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScopeNo.equals(Constant.SCOPE_DISTRIBUTOR)) {
            delDistributorIds.addAll(cmd.getDistributorNoIds());
        } else if (distributorScopeNo.equals(Constant.SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(cmd.getDepartmentNoIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScopeNo.equals(Constant.SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdByDefaultScalePriceIds(cmd.getAdminIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        }
        // 剔除不可视范围
        addDistributorIds =
            addDistributorIds.stream().filter(distributorId -> !delDistributorIds.contains(distributorId)).distinct()
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(addDistributorIds)) {
            Response response =
                distributorServiceRpc.distributorCategoryRelevanceByCategoryId(addDistributorIds, categoryId);
            if (!response.isSuccess()) {
                throw GoodsException.buildException(B_GOODS_BRAND_DISTRIBUTOR_ERROR);
            }
        }
    }
}
