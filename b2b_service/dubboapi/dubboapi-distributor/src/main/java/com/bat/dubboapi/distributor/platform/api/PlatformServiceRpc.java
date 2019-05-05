package com.bat.dubboapi.distributor.platform.api;


import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 20:38
 */
public interface PlatformServiceRpc {
    /**
     * 根据分销商平台编码获取平台信息
     *
     * @param platformNos
     * @return
     */
    Response<List<PlatformRpcDTO>> listPlatformByPlatformNos(List<String> platformNos);


    Response<List<PlatformRpcDTO>> listByOpenFlag(Short openFlag);

    /**
     * 根据名称模糊匹配
     *
     * @param name
     * @return
     */
    Response<List<PlatformRpcDTO>> listByOpenFlagAndNameLike(Short openFlag, String name);
}
