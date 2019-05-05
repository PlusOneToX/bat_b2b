package com.bat.thirdparty.erp.service.executor;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.OrderCheckCmd;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/11 12:01
 */
@Component
public class OrderRpcExe {

    @DubboReference(check = false, retries = 0, timeout = 30000)
    private OrderServiceRpc orderServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WarehouseServiceRpc warehouseServiceRpc;

    /**
     * ERP订单变更
     * 
     * @param cmd
     */
    public void orderChangeByErp(ErpOrderChangeCmd cmd) {
        Response response = orderServiceRpc.orderChangeByErp(cmd);
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public void orderCheckByErp(OrderCheckCmd cmd) {
        Response response = orderServiceRpc.orderCheckByErp(cmd);
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public WarehouseDTORpcQry getWarehouseDTORpcQry(Integer warehouseId, String warehouseNo) {
        com.bat.dubboapi.warehouse.common.Response<WarehouseDTORpcQry> warehouseResp =
            warehouseServiceRpc.getByIdOrWarehouseNo(warehouseId, warehouseNo);
        if (warehouseResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_WAREHOUSE_SERVICE_EXCEPTION);
        }
        if (!warehouseResp.isSuccess()) {
            throw ThirdPartyException.buildException(warehouseResp.getErrCode(), warehouseResp.getErrMessage());
        }
        return warehouseResp.getData();
    }

}
