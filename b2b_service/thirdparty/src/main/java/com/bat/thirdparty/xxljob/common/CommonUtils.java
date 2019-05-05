package com.bat.thirdparty.xxljob.common;

import com.bat.thirdparty.xxljob.api.response.dto.PromotionDTO;
import com.bat.thirdparty.xxljob.service.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.api.response.dto.TenantDTO;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.stereotype.Component;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/11/12 9:58
 */
@Component
public class CommonUtils {

    /**
     * 租户设置
     * 
     * @param tenantNo
     */
    public void getTenantNo(String tenantNo) {
        if (StringUtils.isBlank(tenantNo)) {
            throw ThirdPartyException.buildException(ErrorCode.B_TENANT_NO_NULL);
        }
        TenantContext.setTenantNo(tenantNo);
    }

    /**
     * 租户设置
     *
     */
    public void getTenantNoByParam() {
        String param = checkParam();
        TenantDTO tenantNo = JSON.parseObject(param, TenantDTO.class);
        getTenantNo(tenantNo.getTenantNo());
    }

    /**
     * 租户信息(包括时间)
     *
     */
    public TenantDTO getTenantDTOByParam() {
        String param = checkParam();
        TenantDTO dto = JSON.parseObject(param, TenantDTO.class);
        getTenantNo(dto.getTenantNo());
        return dto;
    }

    /**
     * 定时任务参数校验
     * 
     * @return
     */
    public String checkParam() {
        String param = XxlJobHelper.getJobParam();
        if (StringUtils.isBlank(param)) {
            throw ThirdPartyException.buildException(ErrorCode.B_JOB_PARAM_NULL);
        }
        return param;
    }


    /**
     * 促销活动参数
     *
     * @return
     */
    public PromotionDTO getPromotionDTO() {
        String param = checkParam();
        PromotionDTO dto = JSON.parseObject(param, PromotionDTO.class);
        getTenantNo(dto.getTenantNo());
        return dto;
    }

}
