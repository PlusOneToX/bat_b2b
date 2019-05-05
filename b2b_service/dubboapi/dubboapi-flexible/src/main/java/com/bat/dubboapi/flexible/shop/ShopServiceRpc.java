package com.bat.dubboapi.flexible.shop;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.shop.dto.ShopDTORpcQry;

import java.util.List;

public interface ShopServiceRpc {

    /**
     * 根据店铺名称模糊查询分销商分账配置id列表
     * @param shopName
     * @return
     */
    Response<List<Integer>> searchUserConfigIdByShopName(String shopName);

    /**
     * 根据分销商id和门店编码查询门店
     * @param distributorId
     * @param shopCode
     * @return
     */
    Response<ShopDTORpcQry> getByDistributorIdAndShopCode(Integer distributorId, String shopCode);

    /**
     * 还原配置id为空
     * @param userConfigId
     */
    Response restoreUserConfigIdNull(Integer userConfigId);
}
