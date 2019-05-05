package com.bat.thirdparty.xxljob.service.executor;

import javax.annotation.Resource;

import com.bat.dubboapi.order.order.api.OrderGoodsDiyServiceRpc;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.info.OrderIdListQry;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.thirdparty.factory.common.service.CommonFactoryServiceImpl;
import com.bat.thirdparty.factory.maike.api.MaikeServiceI;
import com.bat.thirdparty.order.service.AdminOrderServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.common.flexible.FactoryConstant;
import com.bat.thirdparty.erp.service.executor.OrderDeliverBillExe;
import com.bat.thirdparty.order.api.AdminOrderServiceI;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.vmall.api.VmallOrderServiceI;
import com.bat.thirdparty.vmall.dto.TimeDTO;

import java.util.List;

/**
 * 沙漠
 */
@Component
public class OrderJobExe {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private OrderGoodsDiyServiceRpc orderGoodsDiyServiceRpc;

    @Resource
    private AdminOrderServiceImpl adminOrderService;

    @Autowired
    private OrderOpenServiceI orderOpenServiceI;

    @Resource
    private AdminOrderServiceI adminOrderServiceI;

    @Autowired
    private OrderDeliverBillExe orderDeliverBillExe;

    @Autowired
    private CommonFactoryServiceImpl factoryService;

    @Autowired
    private VmallOrderServiceI vmallOrderServiceI;

    public void writeOffThirdCodeOrder() {
        orderOpenServiceI.writeOffThirdCodeOrder();
    }

    public void syncErpExpressNo() {
        orderDeliverBillExe.syncErpExpressNo();
    }

    // 推单到工厂
    @Async
    public void diyOrderSyncToFactory(String tenantNo,String startTime) {
        TenantContext.setTenantNo(tenantNo);
        factoryService.diyOrderSyncToFactory(startTime);
        TenantContext.removeTenantNo();
    }

    @Async
    public void vmallOrderPull(String tenantNo, TimeDTO time) {
        TenantContext.setTenantNo(tenantNo);
        vmallOrderServiceI.vmallOrderPull(time);
        TenantContext.removeTenantNo();
    }

    /**
     * 批量同步erp订单
     * 
     * @param tenantNo
     */
    @Async
    public void orderSyncToErp(String tenantNo,String startTime) {
        TenantContext.setTenantNo(tenantNo);
        adminOrderServiceI.orderSyncToErp(startTime);
        TenantContext.removeTenantNo();
    }

    /**
     * 批量同步erp柔性订单出库单
     *
     * @param tenantNo
     */
    @Async
    public void orderPurchaseAndOutboundSyncToErp(String tenantNo,String startTime) {
        TenantContext.setTenantNo(tenantNo);
        adminOrderServiceI.orderPurchaseAndOutboundSyncToErp(startTime);
        TenantContext.removeTenantNo();
    }

    @Async
    public void createOrderLatelyNullLabel(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        adminOrderServiceI.createOrderLatelyNullLabel();
        TenantContext.removeTenantNo();
    }

    /**
     * 款到发货+直发订单，超3天未出货，自动关闭订单
     * 
     * @param tenantNo
     */
    @Async
    public void autoCloseOrderJobHandler(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        adminOrderServiceI.autoCloseOrderJobHandler();
        TenantContext.removeTenantNo();
    }

    /**
     * 自动同步物流
     *
     * @param tenantNo
     */
    @Async
    public void automaticallySynchronizeLogistics(String tenantNo,String startTime) {
        TenantContext.setTenantNo(tenantNo);
        try {
            adminOrderService.synchronizedLogisticsByFactoryCode(FactoryEnum.KDS_FK.name(),startTime);
        }finally {
            TenantContext.removeTenantNo();
        }
    }
}
