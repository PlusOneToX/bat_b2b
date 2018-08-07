package com.bat.order.service.utils.warehouse;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;

@Component
public class WarehouseServiceRpcUtils {

    private static WarehouseServiceRpc warehouseServiceRpc;

    @DubboReference(check = false, retries = 0, timeout = 5000)
    public void setWarehouseServiceRpc(WarehouseServiceRpc warehouseServiceRpc) {
        WarehouseServiceRpcUtils.warehouseServiceRpc = warehouseServiceRpc;
    }

    /**
     * 根据仓库id或者仓库编码查询
     * 
     * @param id
     * @param warehouseNo
     * @return
     */
    public static WarehouseDTORpcQry getByIdAndWarehouseNo(Integer id, String warehouseNo) {
        Response<WarehouseDTORpcQry> warehouseDTORpcQryResponse =
            warehouseServiceRpc.getByIdOrWarehouseNo(id, warehouseNo);
        return warehouseDTORpcQryResponse.getData();
    }
}
