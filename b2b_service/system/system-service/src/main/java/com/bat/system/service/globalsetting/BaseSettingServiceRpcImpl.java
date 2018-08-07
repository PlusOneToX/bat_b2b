package com.bat.system.service.globalsetting;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.service.globalsetting.executor.BaseSettingQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.globalsetting.api.BaseSettingServiceRpc;
import com.bat.dubboapi.system.globalsetting.dto.BaseSettingRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 沙漠
 */
@DubboService
@Slf4j
public class BaseSettingServiceRpcImpl implements BaseSettingServiceRpc {

    @Resource
    private BaseSettingQryExc baseSettingQryExc;

    @Override
    public Response<BaseSettingRpcDTO> getBaseSettingByKey(String key) {
        BaseSettingDTO baseSettingDTO = baseSettingQryExc.getBaseSettingByKey(key);
        BaseSettingRpcDTO dto = new BaseSettingRpcDTO();
        if (baseSettingDTO != null) {
            BeanUtils.copyProperties(baseSettingDTO, dto);
        }
        return Response.of(dto);
    }
}
