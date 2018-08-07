package com.bat.distributor.service.platform;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.distributor.service.platform.executor.PlatformQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.api.PlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 11:30
 */
@DubboService
public class PlatformServiceRpcImpl implements PlatformServiceRpc {

    @Resource
    private PlatformQryExe platformQryExe;

    @Override
    public Response<List<PlatformRpcDTO>> listPlatformByPlatformNos(List<String> platformNos) {
        List<PlatformRpcDTO> rpcDTOS = platformQryExe.listPlatformByPlatformNos(platformNos);
        return Response.of(rpcDTOS);
    }

    @Override
    public Response<List<PlatformRpcDTO>> listByOpenFlag(Short openFlag) {
        List<PlatformRpcDTO> platformRpcDTOS = platformQryExe.listByOpenFlag(openFlag);
        return Response.of(platformRpcDTOS);
    }

    /**
     * 根据名称模糊匹配
     * 
     * @param name
     * @return
     */
    @Override
    public Response<List<PlatformRpcDTO>> listByOpenFlagAndNameLike(Short openFlag, String name) {
        if (StringUtils.isBlank(name)) {
            return Response.buildSuccess();
        }
        List<PlatformRpcDTO> list = platformQryExe.listByOpenFlagAndNameLike(openFlag, name);
        return Response.of(list);
    }
}
