package com.bat.dubboapi.order.order.api;

import com.bat.dubboapi.order.common.Response;

import java.util.List;

public interface ShoppingCartServiceRpc {

    /**
     * 根据商品Ids更新购物车商品状态
     *
     * @param goodsIds
     * @param openFlag
     * @return
     */
    Response changeGoodsOpenFlag(List<Integer> goodsIds, Short openFlag);

    /**
     * 根据货品Ids更新购物车货品状态
     *
     * @param itemIds
     * @param openFlag
     * @return
     */
    Response changeGoodsOpenFlagByItemIds(List<Integer> itemIds, Short openFlag);
}
