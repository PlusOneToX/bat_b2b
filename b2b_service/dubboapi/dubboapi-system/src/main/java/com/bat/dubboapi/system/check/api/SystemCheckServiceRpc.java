package com.bat.dubboapi.system.check.api;

import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.dubboapi.system.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/8/26 11:41
 */
public interface SystemCheckServiceRpc {

    /**
     * 获取审批配置
     * 
     * @param ext
     * @return
     */
    Response<CheckConfigDetailRpcDTO> getCheckConfigDetail(Short ext);
}
