package com.bat.dubboapi.thirdparty.alibaba.alipay.api;

import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data.AlipayProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.AlipayProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.common.Response;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/12/3 20:17
 */
public interface AlipayServiceRpc {
    /**
     * 抖音小程序授权
     * @param rpcCmd
     * @return
     */
    Response<AlipayProgramAuthorizeRpcDTO> alipayMiniProgramAuthorize(AlipayProgramAuthorizeRpcCmd rpcCmd);
}
