package com.bat.warehouse.manager.warehouse;

import java.util.List;

import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.base.dto.WarehouseOpenFlagDTO;
import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.api.warehouse.dto.WarehouseCmd;
import com.bat.warehouse.manager.warehouse.executor.WarehouseCmdExe;
import com.bat.warehouse.manager.warehouse.executor.WarehouseQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.bat.warehouse.api.warehouse.dto.WarehousePageQry;
import com.bat.warehouse.dao.warehouse.co.WarehouseCO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;

@Service
public class WarehouseServiceImpl implements WarehouseServiceI {

    @Autowired
    private WarehouseCmdExe warehouseCmdExe;

    @Autowired
    private WarehouseQryExe warehouseQryExe;

    @Override
    public Response<PageInfo<WarehouseCO>> page(WarehousePageQry warehousePageQry) {
        return warehouseQryExe.adminPage(warehousePageQry);
    }

    @Override
    @Transactional
    public Response createWarehouse(WarehouseCmd warehouseCmd, AdminResponse currentUser) {
        return warehouseCmdExe.createWarehouse(warehouseCmd, currentUser);
    }

    @Override
    @Transactional
    public Response updateWarehouse(WarehouseCmd warehouseCmd, AdminResponse currentUser) {
        return warehouseCmdExe.updateWarehouse(warehouseCmd, currentUser);
    }

    @Override
    @Transactional
    public Response delete(Integer id, AdminResponse currentUser) {
        return warehouseCmdExe.delete(id, currentUser);
    }

    @Override
    public Response upOrDown(Integer id, Boolean flag, AdminResponse currentUser) {
        return warehouseCmdExe.upOrDown(id, flag, currentUser);
    }

    /**
     * 根据状态和区域id查询仓库列表
     * 
     * @param areaIdList
     * @param openFlag
     * @return
     */
    @Override
    public List<WarehouseDO> listByAreaIdListAndOpenFlag(List<Integer> areaIdList, Short openFlag) {
        return warehouseQryExe.listByAreaIdListAndOpenFlagAndSyncType(areaIdList, openFlag, null);
    }

    @Override
    public WarehouseDO getByWarehouseNo(String warehouseNo) {
        return warehouseQryExe.getByWarehouseNo(warehouseNo);
    }

    @Override
    public Response<WarehouseCO> detailById(Integer id) {
        WarehouseCO warehouseCO = warehouseQryExe.detailById(id);
        return Response.of(warehouseCO);
    }

    @Override
    @Transactional
    public Response updateOpenFlag(WarehouseOpenFlagDTO warehouseOpenFlagDTO, AdminResponse adminResponse) {
        warehouseCmdExe.updateOpenFlag(warehouseOpenFlagDTO, adminResponse);
        return Response.buildSuccess();
    }

    @Override
    public List<WarehouseDO> listUsableWarehouse(Short syncType) {
        return warehouseQryExe.listByAreaIdListAndOpenFlagAndSyncType(null,
            WarehouseCommonConstant.COMMON_OPEN_FLAG_YES, syncType);

    }

    @Override
    public List<WarehouseDO> listWarehouse(Short syncType, Short openFlag) {
        return warehouseQryExe.listByAreaIdListAndOpenFlagAndSyncType(null, openFlag, syncType);
    }

    @Override
    public WarehouseDO getById(Integer id) {
        return warehouseQryExe.getById(id);
    }

}
