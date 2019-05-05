package com.bat.thirdparty.xxljob.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.bat.thirdparty.xxljob.service.executor.SystemExe;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/15 20:52
 */
@Component
@Slf4j
public class SystemXxlJob {

    @Resource
    private SystemExe systemExe;

    @Resource
    private CommonUtils commonUtils;

    /**
     * 更新延迟推送时间
     * 
     * @throws Exception
     */
    @XxlJob("updateDelayPushesPushTimeJobHandler")
    public void updateDelayPushesPushTimeJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        systemExe.updateDelayPushesPushTimeJobHandler();
        TenantContext.removeTenantNo();
    }
}
