package com.bat.distributor.service.distributor;

import com.bat.distributor.service.distributor.executor.DistributorRpcCmdExe;
import com.bat.distributor.service.distributor.executor.DistributorRpcQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.GoodsPromotionServiceRpc;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/29 8:59
 */
@DubboService
public class GoodsPromotionServiceRpcImpl implements GoodsPromotionServiceRpc {
    @Resource
    private DistributorRpcQryExe rpcQryExe;

    @Resource
    private DistributorRpcCmdExe rpcCmdExe;


    @Override
    public Response<List<Integer>> listDistributorIdByScalePriceIdsTwo(List<Integer> list) {
        List<Integer> distributorIds = rpcQryExe.listDistributorIdByScalePriceIdsTwo(list);
        return com.bat.dubboapi.distributor.common.Response.of(distributorIds);
    }

    @Override
    public Response distributorGoodsPromotionRelevanceByGoodsId(List<Integer> list, Integer integer) {
        rpcCmdExe.distributorGoodsPromotionRelevanceByGoodsId(list, integer);
        return com.bat.dubboapi.distributor.common.Response.buildSuccess();
    }


    /**
     * 查询该分销商最大级的分销商
     */
    @Override
    public Response<Integer> goodsPromotionMaximumDistributor(Integer distributorId) {
        Integer distributorAncestorId = rpcQryExe.goodsPromotionMaximumDistributor(distributorId);
        return Response.of(distributorAncestorId);
    }
}
