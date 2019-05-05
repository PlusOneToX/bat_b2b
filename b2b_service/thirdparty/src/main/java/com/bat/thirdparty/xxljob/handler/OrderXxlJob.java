package com.bat.thirdparty.xxljob.handler;

import javax.annotation.Resource;

import com.bat.thirdparty.xxljob.api.response.dto.TenantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.vmall.dto.TimeDTO;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.bat.thirdparty.xxljob.service.executor.OrderJobExe;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * 沙漠
 */
@Component
public class OrderXxlJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderXxlJob.class);

    @Resource
    private OrderJobExe orderJobExe;

    @Resource
    private CommonUtils commonUtils;

    /**
     * 核销第三方兑换码订单
     * 
     * @throws Exception
     */
    @XxlJob("writeOffThirdCodeOrderJobHandler")
    public void writeOffThirdCodeOrder() throws Exception {
        commonUtils.getTenantNoByParam();
        orderJobExe.writeOffThirdCodeOrder();
        TenantContext.removeTenantNo();
    }

    /**
     * 同步erp物流单号定时器
     * @throws Exception
     */
    @XxlJob("syncErpExpressNoJobHandler")
    public void syncErpExpressNo() throws Exception {
        commonUtils.getTenantNoByParam();
        orderJobExe.syncErpExpressNo();
        TenantContext.removeTenantNo();
    }

    /**
     * 自动同步物流
     */
    @XxlJob("automaticallySynchronizeLogistics")
    public void automaticallySynchronizeLogistics() {
        TenantDTO tenantDTO = commonUtils.getTenantDTOByParam();
        // 自动同步物流
        orderJobExe.automaticallySynchronizeLogistics(TenantContext.getTenantNo(),tenantDTO.getStartTime());
        TenantContext.removeTenantNo();
    }

    @XxlJob("diyOrderSyncToFactory")
    public void diyOrderSyncToFactory() {
        TenantDTO tenantDTO = commonUtils.getTenantDTOByParam();
        orderJobExe.diyOrderSyncToFactory(TenantContext.getTenantNo(),tenantDTO.getStartTime());
        TenantContext.removeTenantNo();
    }

    /**
     * 批量同步erp订单
     * 
     * @throws Exception
     */
    @XxlJob("orderSyncToErp")
    public void orderSyncToErp() throws Exception {
        TenantDTO tenantDTO = commonUtils.getTenantDTOByParam();
        orderJobExe.orderSyncToErp(TenantContext.getTenantNo(),tenantDTO.getStartTime());
        TenantContext.removeTenantNo();
    }

    /**
     * 批量同步erp柔性订单出库单
     *
     * @throws Exception
     */
    @XxlJob("orderPurchaseAndOutboundSyncToErp")
    public void orderPurchaseAndOutboundSyncToErp() throws Exception {
        TenantDTO tenantDTO = commonUtils.getTenantDTOByParam();
        orderJobExe.orderPurchaseAndOutboundSyncToErp(TenantContext.getTenantNo(),tenantDTO.getStartTime());
        TenantContext.removeTenantNo();
    }

    /**
     * 
     * @throws Exception
     */
     @XxlJob("vmallOrderPull")
    public void vmallOrderPull() throws Exception {
        commonUtils.getTenantNoByParam();
        String param = XxlJobHelper.getJobParam();
        // 获取时间参数
        TimeDTO time = JSON.parseObject(param, TimeDTO.class);
        orderJobExe.vmallOrderPull(TenantContext.getTenantNo(), time);
        TenantContext.removeTenantNo();
    }

    /**
     * 生成标签（针对一小时前与一天前区间没有生成标签进行处理）
     *
     * @throws Exception
     */
    @XxlJob("createOrderLatelyNullLabel")
    public void createOrderLatelyNullLabel() throws Exception {
        commonUtils.getTenantNoByParam();
        orderJobExe.createOrderLatelyNullLabel(TenantContext.getTenantNo());
        TenantContext.removeTenantNo();
    }

    /**
     * 款到发货+直发订单，超3天未出货，自动关闭订单
     *
     * @throws Exception
     */
    @XxlJob("autoCloseOrderJobHandler")
    public void autoCloseOrderJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        orderJobExe.autoCloseOrderJobHandler(TenantContext.getTenantNo());
        TenantContext.removeTenantNo();
    }
}
