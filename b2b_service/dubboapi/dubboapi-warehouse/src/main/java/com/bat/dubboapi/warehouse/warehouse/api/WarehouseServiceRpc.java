package com.bat.dubboapi.warehouse.warehouse.api;

import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;

import java.util.List;

public interface WarehouseServiceRpc {

    /**
     * 根据仓库id或者仓库编码查询对象
     * @param id
     * @param warehouseNo
     * @return
     */
    public Response<WarehouseDTORpcQry> getByIdOrWarehouseNo(Integer id,String warehouseNo);

    void test(Integer index);

    byte[] testInputStream();

    /**
     * 根据销售区域id列表查询仓库id列表
     * @param salesAreaIds
     * @return
     */
    Response<List<WarehouseDTORpcQry>> listByAreaIdList(List<Integer> salesAreaIds);
}
