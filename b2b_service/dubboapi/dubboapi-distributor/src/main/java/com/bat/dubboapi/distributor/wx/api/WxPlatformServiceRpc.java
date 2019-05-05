package com.bat.dubboapi.distributor.wx.api;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;

import java.util.List;

public interface WxPlatformServiceRpc {

    /**
     * 根据AppId获取微信平台配置
     */
    Response<WxPlatformRpcDTO> getWxPlatformRpcDTOByAppId(String appId);

    /**
     * 获取 配置中心授权小程序的appId appSecret 有一定程度的耦合关系
     * @return
     */
    Response<WxPlatformRpcDTO> getMiniProgramAuthorize();

    /**
     * 根据分销商id和平台类型获取微信平台配置列表
     * @param distributorId
     * @param type 平台类型：1 公众号 2 小程序
     * @return
     */
    Response<List<WxPlatformRpcDTO>> listByDistributorIdAndType(Integer distributorId, Short type);
}
