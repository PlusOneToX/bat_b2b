package com.bat.thirdparty.xxljob.service.executor;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.globalsetting.api.FactorySettingServiceRpc;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/15 21:42
 */
@Component
public class SystemExe {

    @DubboReference(check = false, timeout = 10000)
    private FactorySettingServiceRpc factorySettingServiceRpc;

    public void updateDelayPushesPushTimeJobHandler() {
        Response response = factorySettingServiceRpc.updateDelayPushesPushTimeJobHandler();
    }
}
