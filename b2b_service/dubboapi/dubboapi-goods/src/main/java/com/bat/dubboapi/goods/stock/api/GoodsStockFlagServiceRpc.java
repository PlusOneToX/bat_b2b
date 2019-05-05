package com.bat.dubboapi.goods.stock.api;

import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;

import java.util.List;

public interface GoodsStockFlagServiceRpc {

    /**
     * 初始化
     * @param goodsStockFlagDTOList
     * @return
     */
    Response createList(List<GoodsStockFlagDTO> goodsStockFlagDTOList);

    /**
     * 修改
     * @param goodsStockFlagDTOList
     * @return
     */
    Response updateList(List<GoodsStockFlagDTO> goodsStockFlagDTOList);

    /**
     * 删除货品缺货标记
     * @param itemIdList
     * @return
     */
    Response deleteByItemIdList(List<Integer> itemIdList);
}
