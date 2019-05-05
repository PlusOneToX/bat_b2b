package com.bat.thirdparty.xxljob.handler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.bat.thirdparty.xxljob.service.executor.TaoBaoJobExe;
import com.xxl.job.core.handler.annotation.XxlJob;

import javax.annotation.Resource;

/**
 * 沙漠
 */
@Component
public class TaoBaoXxlJob {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoXxlJob.class);

    @Autowired
    private TaoBaoJobExe taoBaoJobExe;

    @Resource
    private CommonUtils commonUtils;

    /**
     * 第一版 不考虑拆单 与定制与普通单混合的情况，后续完善 从0时开始每隔三小时 拉前四小时的数据避免数据遗漏
     */
    @XxlJob("pullTaoBaoOrderJobHandler")
    public void pullTaoBaoOrder() throws Exception {
        commonUtils.getTenantNoByParam();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startTime = endTime.plusHours(-7);
        taoBaoJobExe.doPullTaoBaoOrder(startTime, endTime);
        TenantContext.removeTenantNo();
    }

}
