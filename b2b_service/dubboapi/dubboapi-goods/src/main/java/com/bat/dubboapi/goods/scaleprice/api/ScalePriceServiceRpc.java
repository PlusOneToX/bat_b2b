package com.bat.dubboapi.goods.scaleprice.api;

import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.scaleprice.dto.ScalePriceRpcDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 20:38
 */
public interface ScalePriceServiceRpc {
    /**
     * 根据分销商id查询商品列表
     *
     * @return
     */
    Response<ScalePriceRpcDTO> getScalePriceByScalePriceId(Integer scalePriceId);

}
