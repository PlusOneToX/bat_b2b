package com.bat.dubboapi.system.globalsetting.api;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.globalsetting.dto.FactorySettingOrderInvalidRpcDTO;

public interface FactorySettingServiceRpc {
    /**
     * 更新延迟推送时间
     */
    Response updateDelayPushesPushTimeJobHandler();

    /**
     * 获取订单时效
     * @param distributorId
     * @return
     */
    Response<FactorySettingOrderInvalidRpcDTO> getFactorySettingOrderInvalid(Integer distributorId);
}
