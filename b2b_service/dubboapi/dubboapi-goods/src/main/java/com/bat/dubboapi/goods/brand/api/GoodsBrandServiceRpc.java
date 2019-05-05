package com.bat.dubboapi.goods.brand.api;

import com.bat.dubboapi.goods.brand.dto.UserBrandListRpcQry;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.goods.brand.dto.BrandDistributorRpcCmd;
import com.bat.dubboapi.goods.common.Response;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 20:38
 */
public interface GoodsBrandServiceRpc {
    /**
     * 根据分销商id查询品牌列表
     *
     * @param qry
     * @return
     */
    Response<List<UserBrandRpcDTO>> listBrand(UserBrandListRpcQry qry);

    /**
     * 根据分销商id查询品牌列表
     *
     * @return
     */
    Response<UserBrandRpcDTO> getBrandByBrandId(Integer brandId);

    /**
     * 根据品牌ids查询品牌列表
     *
     * @return
     */
    Response<List<UserBrandRpcDTO>> getBrandByBrandIds(List<Integer> brandIds);

    /**
     * 更新品牌与分销商之间的关系
     *
     * @param cmds
     * @return
     */
    Response brandDistributorRelevance(List<BrandDistributorRpcCmd> cmds);

    /**
     * 刷新品牌与分销商之间的关系
     *
     * @param cmds
     * @param distributorId
     * @return
     */
    Response brandDistributorRelevanceByDistributorId(List<BrandDistributorRpcCmd> cmds, Integer distributorId);

    /**
     * 根据分销商分组重新保存值
     * @param distributorGroupIds
     * @return
     */
    Response reSetBrandByDistributorGroupIds(String distributorGroupIds);
}
