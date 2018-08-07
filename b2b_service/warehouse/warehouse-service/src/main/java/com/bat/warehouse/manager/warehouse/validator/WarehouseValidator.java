package com.bat.warehouse.manager.warehouse.validator;

import java.util.List;

import javax.annotation.Resource;

import com.bat.warehouse.manager.common.config.ConfigQry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;
import com.bat.warehouse.manager.warehouse.executor.WarehouseQryExe;

@Component
public class WarehouseValidator {

    @Autowired
    private WarehouseQryExe warehouseQryExe;

    @Resource
    private ConfigQry vmiConfigExe;

    public void validWarehouseOpenFlag(Integer warehouseId, Integer areaId, Short openFlag, String warehouseNo) {
        if (WarehouseCommonConstant.COMMON_OPEN_FLAG_NO.equals(openFlag)) {
            return;
        }
        validAreaIdExist(warehouseId, areaId, openFlag);
        validWarehouseNoExist(warehouseId, openFlag, warehouseNo);
    }

    private void validWarehouseNoExist(Integer warehouseId, Short openFlag, String warehouseNo) {
        String vmiWarehouse = vmiConfigExe.getTenantErpConfig().getVmiWarehouse();
        List<WarehouseDO> warehouseDOList = warehouseQryExe.listByCondition(null, openFlag, warehouseNo);
        if (warehouseDOList == null || warehouseDOList.size() == 0) {
            return;
        }
        if (warehouseId == null) {
            throw new WarehouseException("仓库编码重复不能启用");
        }
        if (warehouseDOList.size() > 1 || warehouseDOList.get(0).getId() - warehouseId != 0) {
            throw new WarehouseException("仓库编码重复不能启用");
        }
        if (vmiWarehouse.equals(warehouseNo)) {
            throw new WarehouseException("VMI编码已使用、请选择其他编码");
        }
    }

    private void validAreaIdExist(Integer warehouseId, Integer areaId, Short openFlag) {
        List<WarehouseDO> warehouseDOList = warehouseQryExe.listByCondition(areaId, openFlag, null);
        if (warehouseDOList == null || warehouseDOList.size() == 0) {
            return;
        }
        if (warehouseId == null) {
            throw new WarehouseException("销售区域Id不能启用");
        }
        if (warehouseDOList.size() > 1 || warehouseDOList.get(0).getId() - warehouseId != 0) {
            throw new WarehouseException("销售区域Id不能启用");
        }
    }
}
