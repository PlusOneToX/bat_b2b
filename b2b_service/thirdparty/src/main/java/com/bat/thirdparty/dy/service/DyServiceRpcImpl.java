package com.bat.thirdparty.dy.service;

import com.bat.thirdparty.dy.service.executor.DyRpcExe;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.dy.api.DyServiceRpc;
import com.bat.dubboapi.thirdparty.dy.dto.DyMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 10:23
 */
@DubboService
public class DyServiceRpcImpl implements DyServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(DyServiceRpcImpl.class);

    @Resource
    private DyRpcExe dyRpcExe;

    @Override
    public Response<DyMiniProgramAuthorizeRpcDTO> dyMiniProgramAuthorize(DyMiniProgramAuthorizeRpcCmd cmd) {
        return dyRpcExe.dyMiniProgramAuthorize(cmd);
    }


}
