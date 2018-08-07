package com.bat.order.service.cart.executor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.common.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 16:58
 */
@Component
@Slf4j
public class ShoppingCartRpcExe {

    @Resource
    private ShoppingCartCustomerCmdExe cartCustomerCmdExe;
    @Resource
    private ShoppingCartDistributorCmdExe cartDistributorCmdExe;

    /**
     * 根据商品Ids更新购物车商品状态
     * 
     * @param goodsIds
     * @param openFlag
     * @return
     */
    public Response changeGoodsOpenFlag(List<Integer> goodsIds, Short openFlag) {
        cartDistributorCmdExe.changeGoodsOpenFlag(goodsIds, openFlag);
        cartCustomerCmdExe.changeGoodsOpenFlag(goodsIds, openFlag);
        return Response.buildSuccess();
    }

    /**
     * 根据货品Ids更新购物车货品状态
     *
     * @param itemIds
     * @param openFlag
     * @return
     */
    public Response changeGoodsOpenFlagByItemIds(List<Integer> itemIds, Short openFlag) {
        cartDistributorCmdExe.changeGoodsOpenFlagByItemIds(itemIds, openFlag);
        cartCustomerCmdExe.changeGoodsOpenFlagByItemIds(itemIds, openFlag);
        return Response.buildSuccess();
    }

}
