package com.bat.system.service.globalsetting;

import javax.annotation.Resource;

import com.bat.system.api.base.MessageUtils;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingOrderInvalidDO;
import com.bat.system.service.globalsetting.executor.ErrorCode;
import com.bat.system.service.globalsetting.executor.FactorySettingCmdExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.globalsetting.api.FactorySettingServiceRpc;
import com.bat.dubboapi.system.globalsetting.dto.FactorySettingOrderInvalidRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 15:59
 */
@DubboService
@Slf4j
public class FactorySettingServiceRpcImpl implements FactorySettingServiceRpc {

    @Resource
    private FactorySettingCmdExc factorySettingCmdExc;

    @Override
    public Response updateDelayPushesPushTimeJobHandler() {
        log.info("更新延迟推送时间");
        factorySettingCmdExc.updateDelayPushesPushTimeByTask();
        return Response.buildSuccess();
    }

    @Override
    public Response<FactorySettingOrderInvalidRpcDTO> getFactorySettingOrderInvalid(Integer integer) {
        FactorySettingOrderInvalidDO invalidDO =
            factorySettingCmdExc.getFactorySettingOrderInvalidByDistributorId(integer);
        if (invalidDO != null) {
            FactorySettingOrderInvalidRpcDTO dto = new FactorySettingOrderInvalidRpcDTO();
            BeanUtils.copyProperties(invalidDO, dto);
            return Response.of(dto);
        }
        return Response.buildFailure(ErrorCode.B_ORDER_INVALID_NOT_EXISTS,
            MessageUtils.get(ErrorCode.B_ORDER_INVALID_NOT_EXISTS));
    }
}
