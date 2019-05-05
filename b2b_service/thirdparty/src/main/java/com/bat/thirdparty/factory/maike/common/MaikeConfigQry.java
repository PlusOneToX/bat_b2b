package com.bat.thirdparty.factory.maike.common;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.utils.CommonUtil;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/26 13:49
 */
@Component
public class MaikeConfigQry {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private MaikeFactoryConfig config;

    public MaikeFactoryConfig getTenantFactoryConfig(String factoryNo) {
        MaikeFactoryConfig maikeFactoryConfig=new MaikeFactoryConfig();
        BeanUtils.copyProperties(config,maikeFactoryConfig);
        Response<PlatformTenantDiyFactoryRpcDTO> response =
            platformTenantServiceRpc.diyFactoryConfig(TenantContext.getTenantNo(), factoryNo);
        if (response.isSuccess()) {
            PlatformTenantDiyFactoryRpcDTO factoryRpcDTO = response.getData();
            maikeFactoryConfig.setAddOrderUrl(factoryRpcDTO.getOrderAddUrl());
            maikeFactoryConfig.setCancelOrderUrl(factoryRpcDTO.getOrderCancelUrl());
            maikeFactoryConfig.setMaikeKey(factoryRpcDTO.getAppKey());
            maikeFactoryConfig.setMaikeSecret(factoryRpcDTO.getAppSecret());
            maikeFactoryConfig.setWarehouseNo(factoryRpcDTO.getWarehouseNo());
            maikeFactoryConfig.setSupplierNo(factoryRpcDTO.getSupplierNo());
            if (StringUtils.isNotBlank(factoryRpcDTO.getConfigOther())) {
                Map<String, String> changeMap = JSONObject.parseObject(factoryRpcDTO.getConfigOther(), Map.class);
                CommonUtil.copyFieldValue(maikeFactoryConfig, changeMap);
            }
        } else {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return maikeFactoryConfig;
    }
}
