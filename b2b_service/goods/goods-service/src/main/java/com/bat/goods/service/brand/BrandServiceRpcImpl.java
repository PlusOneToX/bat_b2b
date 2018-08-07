package com.bat.goods.service.brand;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.dataobject.BrandDistributorGroupRelevanceDO;
import com.bat.goods.service.brand.executor.BrandQryExe;
import com.bat.goods.service.brand.executor.BrandRpcCmdExe;
import com.bat.goods.service.brand.executor.BrandRpcQryExe;
import com.bat.goods.service.common.Constant;
import com.bat.goods.api.brand.BrandServiceI;
import com.bat.goods.api.brand.dto.BrandCmd;
import com.bat.goods.api.brand.dto.BrandId;
import com.bat.goods.api.brand.dto.data.BrandDTO;
import com.bat.goods.api.brand.dto.data.BrandDistributorGroupDTO;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.goods.brand.api.GoodsBrandServiceRpc;
import com.bat.dubboapi.goods.brand.dto.BrandDistributorRpcCmd;
import com.bat.dubboapi.goods.brand.dto.UserBrandListRpcQry;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.goods.common.Response;
import org.springframework.beans.BeanUtils;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 20:43
 */
@DubboService
public class BrandServiceRpcImpl implements GoodsBrandServiceRpc {
    @Resource
    private BrandRpcQryExe rpcQryExe;

    @Resource
    private BrandRpcCmdExe rpcCmdExe;

    @Resource
    private BrandQryExe brandQryExe;


    @Resource
    private BrandServiceI brandService;

    /**
     * 根据分销商获取品牌列表（支持多级）
     * 
     * @param qry
     * @return
     */
    @Override
    public Response<List<UserBrandRpcDTO>> listBrand(UserBrandListRpcQry qry) {
        List<UserBrandRpcDTO> dtos = rpcQryExe.listBrand(qry);
        return Response.of(dtos);
    }

    @Override
    public Response<UserBrandRpcDTO> getBrandByBrandId(Integer brandId) {
        UserBrandRpcDTO brand = rpcQryExe.getBrand(brandId);
        return Response.of(brand);
    }

    @Override
    public Response<List<UserBrandRpcDTO>> getBrandByBrandIds(List<Integer> brandIds) {
        List<UserBrandRpcDTO> brands = rpcQryExe.getBrandByIds(brandIds);
        return Response.of(brands);
    }

    /**
     * 更新品牌与分销商之间的关系
     * 
     * @param cmds
     * @return
     */
    @Override
    public Response brandDistributorRelevance(List<BrandDistributorRpcCmd> cmds) {
        rpcCmdExe.brandDistributorRelevance(cmds);
        return Response.buildSuccess();
    }

    @Override
    public Response brandDistributorRelevanceByDistributorId(List<BrandDistributorRpcCmd> cmds, Integer distributorId) {
        rpcCmdExe.brandDistributorRelevanceByDistributorId(cmds, distributorId);
        return Response.buildSuccess();
    }

    @Override
    public Response reSetBrandByDistributorGroupIds(String distributorGroupIds) {
        List<BrandDistributorGroupRelevanceDO> brandDistributorGroupRelevanceDOS = brandQryExe.listByDistributorGroupIds(distributorGroupIds);
        if (brandDistributorGroupRelevanceDOS.size() == 0) {
            return Response.buildSuccess();
        }
        for (BrandDistributorGroupRelevanceDO brandDistributorGroupRelevanceDO : brandDistributorGroupRelevanceDOS) {
            BrandId brandId = new BrandId();
            brandId.setId(brandDistributorGroupRelevanceDO.getBrandId());
            BrandDTO brandDTO = brandService.getBrand(brandId);
            if (!brandDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
                continue;
            }
            BrandCmd brandCmd = new BrandCmd();
            BeanUtils.copyProperties(brandDTO, brandCmd);
            List<Integer> distributorGroups = brandDTO.getDistributorGroups().stream().map(BrandDistributorGroupDTO::getDistributorGroupId).collect(Collectors.toList());
            brandCmd.setDistributorGroupIds(distributorGroups);
            brandService.updateBrand(brandCmd);
        }
        return Response.buildSuccess();
    }

}
