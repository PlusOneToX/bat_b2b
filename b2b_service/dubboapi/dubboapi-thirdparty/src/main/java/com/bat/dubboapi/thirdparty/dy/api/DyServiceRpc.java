package com.bat.dubboapi.thirdparty.dy.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.dy.dto.DyMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;

/**
 * 沙漠
 */
public interface DyServiceRpc {
    /**
     * 抖音小程序授权
     * @param rpcCmd
     * @return
     */
    Response<DyMiniProgramAuthorizeRpcDTO> dyMiniProgramAuthorize(DyMiniProgramAuthorizeRpcCmd rpcCmd);
}
