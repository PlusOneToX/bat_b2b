package com.bat.distributor.service.distributor.executor;

import com.bat.distributor.tenant.TenantContext;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.DistributorConfig;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantCommonRpcDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class DistributorConfigExe {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private DistributorConfig config;

    public DistributorConfig getConfig() {
        DistributorConfig distributorConfig=new DistributorConfig();
        BeanUtils.copyProperties(config,distributorConfig);
        Response<PlatformTenantCommonRpcDTO> response =
                platformTenantServiceRpc.commonConfig(TenantContext.getTenantNo());
        if (!response.isSuccess()) {
            throw DistributorException.buildException(CommonErrorCode.SYSTEM_EXCEPTION);
        }
        PlatformTenantCommonRpcDTO platformTenantErpRpcDTO = response.getData();
        distributorConfig.setWxMiniProgramAppId(platformTenantErpRpcDTO.getWxProgramAppId());
        distributorConfig.setWxMiniProgramAppSecret(platformTenantErpRpcDTO.getWxProgramAppSecret());
        return distributorConfig;
    }

}
