package com.bat.thirdparty.factory.feikuai.service;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.feikuai.api.FeiKuaiServiceRpc;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.factory.feikuai.dto.FeiKuaiCancelOrderReq;
import com.bat.thirdparty.factory.feikuai.executor.FeiKuaiCmdExe;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:08
 */
@DubboService
public class FeiKuaiServiceRpcImpl implements FeiKuaiServiceRpc {

    @Resource
    private FeiKuaiCmdExe cmdExe;


    @Override
    public Response<Boolean> cancelOrderToFactory(String factoryCode, String orderNo) {
        Boolean b = cmdExe.cancelOrderToFactory(factoryCode, orderNo);
        return Response.of(b);
    }
}
