package com.bat.dubboapi.distributor.distributor.api;

import java.util.List;

import com.bat.dubboapi.distributor.common.Response;

public interface GoodsPromotionServiceRpc {

    /**
     * 根据分销商价格等级ids查询分销商ids列表
     *
     * @param scalePriceIds
     * @return
     */
    Response<List<Integer>> listDistributorIdByScalePriceIdsTwo(List<Integer> scalePriceIds);

    /**
     * 根据商品推广可视范围调整分销商可视商品推广关系
     *
     * @param distributorIds
     * @param id
     * @return
     */
    Response distributorGoodsPromotionRelevanceByGoodsId(List<Integer> distributorIds, Integer id);

    /**
     * 查询该分销商最大级的分销商
     */
    Response<Integer> goodsPromotionMaximumDistributor(Integer distributorId);
}
