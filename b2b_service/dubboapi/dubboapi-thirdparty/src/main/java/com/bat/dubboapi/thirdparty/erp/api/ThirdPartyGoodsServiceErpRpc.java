package com.bat.dubboapi.thirdparty.erp.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemErpListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemPriceErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/17 16:01
 */
public interface ThirdPartyGoodsServiceErpRpc {
    /**
     * 根据搜索条件查询erp货品列表
     *
     * @return
     */
    Response<List<GoodsItemErpRpcDTO>> listGoodsItemErp(GoodsItemErpListRpcQry qry);

    /**
     * 同步商品价格
     *
     * @param qry
     * @return
     */
    Response<List<GoodsItemPriceRpcDTO>> listGoodsItemPriceErp(GoodsItemPriceErpRpcQry qry);

    /**
     * 根据itemErpIds获取更新货品信息
     *
     * @param itemErpIds
     * @return
     */
    Response<List<GoodsItemErpRpcDTO>> listGoodsItemErp(List<Integer> itemErpIds);

    /**
     * 根据itemErpIds获取更新货品装箱信息
     *
     * @param itemErpIds
     * @return
     */
    Response<List<GoodsItemBoxRpcDTO>> listGoodsItemBoxErp(List<Integer> itemErpIds);
}
