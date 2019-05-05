package com.bat.dubboapi.system.globalsetting.api;


import com.bat.dubboapi.system.globalsetting.dto.BaseSettingRpcDTO;
import com.bat.dubboapi.system.common.Response;

/**
 * 沙漠
 */
public interface BaseSettingServiceRpc {

    /**
     * 通过key获取基本配置
     * 
     * @param key
     * @return
     */
    Response<BaseSettingRpcDTO>  getBaseSettingByKey(String key);
}
