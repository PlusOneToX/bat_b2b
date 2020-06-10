package com.bat.flexible.manager.common;

import javax.annotation.Resource;

import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantCommonRpcDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;
import com.bat.flexible.Tenant.TenantContext;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/26 11:43
 */
@Component
public class ConfigQry {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    FlexibleConfig flexibleConfig;

    public FlexibleConfig getTenantExchangeDistributorId() {
        FlexibleConfig newFlexibleConfig=new FlexibleConfig();
        BeanUtils.copyProperties(flexibleConfig,newFlexibleConfig);
        Response<PlatformTenantCommonRpcDTO> response =
            platformTenantServiceRpc.commonConfig(TenantContext.getTenantNo());
        if (response.isSuccess()) {
            PlatformTenantCommonRpcDTO tenantCommonRpcDTO = response.getData();
            newFlexibleConfig.setExchangeDistributorId(tenantCommonRpcDTO.getExchangeDistributorId());
            return newFlexibleConfig;
        } else {
            throw new FlexibleCustomException(response.getErrMessage());
        }
    }

    public FlexibleConfig getTenantShopUrl() {
        FlexibleConfig newFlexibleConfig=new FlexibleConfig();
        BeanUtils.copyProperties(flexibleConfig,newFlexibleConfig);
        Response<PlatformTenantUrlRpcDTO> response =
            platformTenantServiceRpc.urlConfig(TenantContext.getTenantNo(), FlexibleCommonConstant.URL_TYPE_4);
        if (response.isSuccess()) {
            PlatformTenantUrlRpcDTO data = response.getData();
            newFlexibleConfig.setWxurl(data.getUrl());
            return newFlexibleConfig;
        } else {
            throw new FlexibleCustomException(response.getErrMessage());
        }
    }

}
