package com.bat.thirdparty.goods.api;

import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.goods.api.dto.ItemCodeCmd;

public interface GoodsServiceI {

    /**
     * 同步商品价格（根据货品编码）
     * 
     * @param cmd
     * @return
     */
    Response syncGoodsPriceByItemCodes(ItemCodeCmd cmd);

    void downSaleStatus();

    void upperSaleStatus();
}
