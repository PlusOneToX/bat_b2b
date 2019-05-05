package com.bat.promotion.service.user.executor;

import static com.bat.promotion.service.common.Constant.PROMOTION_FLAG_1;
import static com.bat.promotion.service.common.Constant.PROMOTION_SCOPE_0;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorPromitonGroupSeckillRpcDTO;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.UserGoodsItemListRpcQry;
import com.bat.dubboapi.goods.goods.dto.UserGoodsSortListRpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.UserGoodsRpcDTO;
import com.bat.promotion.Tenant.TenantContext;
import com.bat.promotion.api.base.PromotionException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:12
 */
@Component
@Slf4j
public class UserRpcQryExe {

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 根据货品ids获取货品信息
     *
     * @param goodsItemIds
     * @return
     */
    public List<GoodsItemRpcDTO> listGoodsItemByIds(List<Integer> goodsItemIds) {
        Response<List<GoodsItemRpcDTO>> listResponse = goodsServiceRpc.listGoodsItemByIds(goodsItemIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_USER_GOODS_ERROR);
        }
    }

    /**
     * 根据分销商id获取可视促销活动或拼团秒杀活动
     * 
     * @param distributorId
     * @return
     */
    public DistributorPromitonGroupSeckillRpcDTO getDistributorPromotionGroupSeckill(Integer distributorId) {
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            distributorServiceRpc.getDistributorPromotionGroupSeckill(distributorId);
        log.info("根据分销商id获取可视促销活动或拼团秒杀活动:分销商id {} 返回值：{}", distributorId,
            JSON.toJSONString(promitonGroupSeckillRpcDTO));
        if (!promitonGroupSeckillRpcDTO.getPromotionScope().equals(PROMOTION_SCOPE_0)
            && promitonGroupSeckillRpcDTO.getDistributionPromotionFlag().equals(PROMOTION_FLAG_1)) {
            return promitonGroupSeckillRpcDTO;
        }
        log.info("租户编码{}分销商活动参数{}", TenantContext.getTenantNo(), JSONObject.toJSON(promitonGroupSeckillRpcDTO));
        return null;
    }

    /**
     * 根据商品ids或货品ids获取分销商可视货品列表（分页）
     * 
     * @return
     */
    public PageInfo<GoodsItemRpcDTO> listDistributorGoodsItem(UserGoodsItemListRpcQry qry) {
        Response<PageInfo<GoodsItemRpcDTO>> response = goodsServiceRpc.listDistributorGoodsItem(qry);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_USER_GOODS_ERROR);
        }
    }

    /**
     * 根据分销商id和商品ids查询商品列表（分页，有序（顺序按商品ids顺序））
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsRpcDTO> listDistributorGoodsSort(UserGoodsSortListRpcQry qry) {
        Response<PageInfo<UserGoodsRpcDTO>> response = goodsServiceRpc.listDistributorGoodsSort(qry);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_USER_GOODS_ERROR);
        }
    }
}
