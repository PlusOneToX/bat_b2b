package com.bat.goods.service.brand.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.brand.dataobject.BrandDO;
import com.bat.goods.dao.brand.dataobject.BrandDistributorRelevanceDO;
import com.bat.goods.service.common.Constant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.ChangeBrandRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorChangeBrandRpcCmd;
import com.bat.dubboapi.goods.brand.dto.BrandDistributorRpcCmd;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.brand.dto.BrandCmd;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/6 13:55
 */
@Component
public class BrandRpcCmdExe {

    @Resource
    private BrandMapper brandMapper;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 更新品牌与分销商之间的关系(可视范围)
     * 
     * @param cmds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void brandDistributorRelevance(List<BrandDistributorRpcCmd> cmds) {
        DistributorChangeBrandRpcCmd changeBrandRpcCmd = new DistributorChangeBrandRpcCmd();
        changeBrandRpcCmd.setDistributorId(cmds.get(0).getDistributorId());
        List<ChangeBrandRpcCmd> brandRpcCmds = new ArrayList<>();
        cmds.forEach(cmd -> {
            BrandDO brandDO = brandMapper.getById(cmd.getBrandId());
            if (brandDO != null) {
                ChangeBrandRpcCmd brandRpcCmd = new ChangeBrandRpcCmd();
                brandRpcCmd.setBrandId(cmd.getBrandId());
                brandRpcCmd.setOperationType(cmd.getOperationType());
                if (brandDO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR)) {
                    List<Integer> ids = brandMapper.listBrandDistributorRelevanceId(brandDO.getId());
                    if (!ids.contains(cmd.getDistributorId()) && !cmd.getOperationType().equals(Constant.OPERATION_TYPE_3)) {
                        BrandDistributorRelevanceDO relevanceDO = new BrandDistributorRelevanceDO();
                        relevanceDO.setBrandId(cmd.getBrandId());
                        relevanceDO.setDistributorId(cmd.getDistributorId());
                        brandMapper.createBrandDistributorRelevance(relevanceDO);
                    } else if (ids.contains(cmd.getDistributorId())
                        && cmd.getOperationType().equals(Constant.OPERATION_TYPE_3)) {
                        brandMapper.deleteBrandDistributorRelevanceByDistributorIdAndBrandId(cmd.getBrandId(),
                            cmd.getDistributorId());
                    }
                    brandRpcCmds.add(brandRpcCmd);
                } else if (brandDO.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE)) {
                    List<Integer> scalePriceIds = brandMapper.listBrandScalePriceRelevanceId(brandDO.getId());
                    if (!CollectionUtils.isEmpty(scalePriceIds) && scalePriceIds.contains(cmd.getScalePriceId())) {
                        brandRpcCmds.add(brandRpcCmd);
                    }
                }
                if (brandDO.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)) {
                    brandMapper.deleteBrandDistributorRelevanceNoByDistributorId(cmd.getBrandId(),
                        cmd.getDistributorId());
                }
            } else {
                brandMapper.deleteBrandDistributorRelevance(cmd.getBrandId());
                brandMapper.deleteBrandDistributorRelevanceNo(cmd.getBrandId());
            }
        });
        /** 更新分销商品牌可视 */
        if (!CollectionUtils.isEmpty(brandRpcCmds)) {
            changeBrandRpcCmd.setBrandCmds(brandRpcCmds);
            distributorServiceRpc.changeDistributorBrandRelevance(changeBrandRpcCmd);
        }
    }

    /**
     * 刷新品牌与分销商之间的关系
     * 
     * @param cmds
     * @param distributorId
     */
    public void brandDistributorRelevanceByDistributorId(List<BrandDistributorRpcCmd> cmds, Integer distributorId) {
        brandMapper.deleteBrandDistributorRelevanceByDistributorId(distributorId);
        List<Integer> updateBrandIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            List<Integer> brandIds = cmds.stream().map(BrandDistributorRpcCmd::getBrandId).collect(Collectors.toList());
            List<BrandDO> brandDOS = brandMapper.listBrandByBrandIds(brandIds);
            Map<Integer, BrandDO> brandDOMap =
                brandDOS.stream().collect(Collectors.toMap(BrandDO::getId, brandDO -> brandDO));
            cmds.forEach(cmd -> {
                BrandDO brandDO = brandDOMap.get(cmd.getBrandId());
                if (brandDO != null) {
                    List<Integer> distributorIds = brandMapper.listBrandDistributorRelevanceIdNo(brandDO.getId());
                    if (brandDO.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)
                        && !CollectionUtils.isEmpty(distributorIds) && distributorIds.contains(distributorId)) {
                        return;
                    }
                    if (brandDO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR)) {
                        updateBrandIds.add(brandDO.getId());
                    } else if (brandDO.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE)) {
                        List<Integer> scalePriceIds = brandMapper.listBrandScalePriceRelevanceId(brandDO.getId());
                        if (!CollectionUtils.isEmpty(scalePriceIds) && scalePriceIds.contains(cmd.getScalePriceId())) {
                            updateBrandIds.add(brandDO.getId());
                        }
                    }
                }
            });
            if (!CollectionUtils.isEmpty(updateBrandIds)) {
                List<BrandDistributorRelevanceDO> relevanceDOS = new ArrayList<>();
                updateBrandIds.forEach(updateBrandId -> {
                    BrandDistributorRelevanceDO relevanceDO = new BrandDistributorRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setBrandId(updateBrandId);
                    relevanceDOS.add(relevanceDO);
                });
                brandMapper.createBrandDistributorRelevanceList(relevanceDOS);
            }
        }
        distributorServiceRpc.distributorBrandRelevance(updateBrandIds, distributorId);
    }

    /**
     * 更新分销商对应品牌的可视范围
     * 
     * @param brandId
     * @param cmd
     */
    public void distributorBrandRelevance(Integer brandId, BrandCmd cmd) {
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
            listResponse =
                distributorServiceRpc.listIdByDistributorGroupIdsAndOneTreeNode(cmd.getDistributorGroupIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        }
        if (listResponse != null && !listResponse.isSuccess()) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_BRAND_DISTRIBUTOR_ERROR);
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
            Response response = distributorServiceRpc.distributorBrandRelevanceByBrandId(addDistributorIds, brandId);
            if (!response.isSuccess()) {
                throw GoodsException.buildException(ErrorCode.B_GOODS_BRAND_DISTRIBUTOR_ERROR);
            }
        }
    }
}
