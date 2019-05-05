package com.bat.dubboapi.system.storesetting.api;

import com.bat.dubboapi.system.storesetting.dto.ShopSettingRpcDTO;
import com.bat.dubboapi.system.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 14:28
 */
public interface SystemGlobeShopSettingServiceRpc {
    /**
     * 上架多少天内为新品时间
     * 
     * @return
     */
    Response<Integer> getNewproductTime();

    /**
     * 获取系统配置
     * @return
     */
    Response<ShopSettingRpcDTO> getSystemConfig();
}
