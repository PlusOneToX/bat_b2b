package com.bat.distributor.service.Message;

import java.util.Map;

import javax.annotation.Resource;

import com.bat.distributor.mq.api.Sink;
import com.bat.distributor.mq.dto.SalesmanDTO;
import com.bat.distributor.service.distributor.executor.DistributorCmdExe;
import com.bat.distributor.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.bat.distributor.service.common.DistributorConfig;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 13:54
 */
@Service
public class ReceiveService {

    @Resource
    private DistributorCmdExe distributorCmdExe;
    @Resource
    private DistributorConfig config;

    /**
     * 根据业务员id刷新分销商可视范围（暂时只支持商品可视范围）
     *
     * @param saleDTO
     */
    @StreamListener(value = Sink.DISTRIBUTOR_SYSTEM_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'updateDistributorVisibleBySalesId'")
    public void updateDistributorVisibleBySalesId(@Headers Map headers, @Payload SalesmanDTO saleDTO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        distributorCmdExe.updateDistributorVisibleBySalesId(tenantNo, saleDTO);
        TenantContext.removeTenantNo();
    }

    /**
     * 根据分销商id刷新分销商可视范围
     * 
     * @param distributorId
     */
    @StreamListener(value = Sink.DISTRIBUTOR_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'updateDistributorVisibleByDistributorId'")
    public void updateDistributorVisibleByDistributorId(@Headers Map headers, @Payload Integer distributorId) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        distributorCmdExe.updateDistributorVisibleByDistributorId(tenantNo, distributorId);
        TenantContext.removeTenantNo();
    }

}
