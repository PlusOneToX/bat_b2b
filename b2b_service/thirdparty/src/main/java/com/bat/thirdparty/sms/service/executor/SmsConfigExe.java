package com.bat.thirdparty.sms.service.executor;

import java.util.ArrayList;
import java.util.List;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantSmsRpcDTO;
import com.bat.thirdparty.sms.config.SmsConfig;

@Component
public class SmsConfigExe {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    public SmsConfig getSmsConfig() {
        Response<PlatformTenantSmsRpcDTO> platformTenantSmsRpcDTOResponse =
            platformTenantServiceRpc.smsConfig(TenantContext.getTenantNo(), ThirdCommonConstant.SMS_TYPE_ALI);
        if (!platformTenantSmsRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        PlatformTenantSmsRpcDTO platformTenantSmsRpcDTO = platformTenantSmsRpcDTOResponse.getData();
        SmsConfig smsConfig = new SmsConfig();
        smsConfig.setSmsSign(platformTenantSmsRpcDTO.getSign());
        smsConfig.setVerifyCodeLength(platformTenantSmsRpcDTO.getVerifyCodeLength());
        smsConfig.setCodeVerifyTime(platformTenantSmsRpcDTO.getCodeVerifyTime());
        smsConfig.setVerifyCodeCountdown(platformTenantSmsRpcDTO.getVerifyCodeCountDown());
        smsConfig.setAccessKeyId(platformTenantSmsRpcDTO.getAccessKeyId());
        smsConfig.setAccessKeySecret(platformTenantSmsRpcDTO.getAccessKeySecret());
        List<String> templateCodes = new ArrayList<>();
        for (PlatformTenantSmsRpcDTO.TenantSmsTemplate tenantSmsTemplate : platformTenantSmsRpcDTO
            .getTenantSmsTemplates()) {
            templateCodes.add(tenantSmsTemplate.getTemplateCode());
        }
        smsConfig.setTemplateCodes(templateCodes);
        return smsConfig;
    }

}
