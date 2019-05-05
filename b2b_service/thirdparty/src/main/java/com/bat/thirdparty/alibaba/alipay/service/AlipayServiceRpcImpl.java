package com.bat.thirdparty.alibaba.alipay.service;

import com.bat.dubboapi.thirdparty.alibaba.alipay.api.AlipayServiceRpc;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.AlipayProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data.AlipayProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.thirdparty.alibaba.alipay.executor.AlipayRpcExe;
import com.bat.thirdparty.common.base.ThirdPartyException;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/12/3 20:22
 */
@DubboService
public class AlipayServiceRpcImpl implements AlipayServiceRpc {

    @Resource
    private AlipayRpcExe alipayRpcExe;

    @Override
    public Response<AlipayProgramAuthorizeRpcDTO> alipayMiniProgramAuthorize(AlipayProgramAuthorizeRpcCmd rpcCmd) {
        try {
            AlipayProgramAuthorizeRpcDTO rpcDTO = alipayRpcExe.alipayMiniProgramAuthorize(rpcCmd);
            return Response.of(rpcDTO);
        } catch (ThirdPartyException e) {
            return Response.buildFailure(e.getCode(), e.getMessage());
        }
    }
}
