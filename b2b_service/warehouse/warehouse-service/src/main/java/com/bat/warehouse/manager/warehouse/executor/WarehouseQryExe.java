package com.bat.warehouse.manager.warehouse.executor;

import java.util.List;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.util.MessageUtils;
import com.bat.warehouse.api.warehouse.dto.WarehousePageQry;
import com.bat.warehouse.dao.warehouse.WarehouseDOMapper;
import com.bat.warehouse.dao.warehouse.co.WarehouseCO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;

@Component
public class WarehouseQryExe {

    @Autowired
    private WarehouseDOMapper warehouseDOMapper;

    /**
     * 分页查询
     * 
     * @param warehousePageQry
     * @return
     */
    public Response<PageInfo<WarehouseCO>> adminPage(WarehousePageQry warehousePageQry) {
        PageHelper.startPage(warehousePageQry.getPage(), warehousePageQry.getSize());
        List<WarehouseCO> list =
            warehouseDOMapper.listByOpenFlagAndName(warehousePageQry.getOpenFlag(), warehousePageQry.getContent());
        if (list != null && list.size() > 0) {
            // 处理所属区域
            list.stream().forEach(warehouseCO -> {
                warehouseCO.setAreaName("等reids");
            });
        }
        PageInfo<WarehouseCO> pageInfo = new PageInfo<>(list);
        return Response.of(pageInfo);
    }

    public WarehouseDO getById(Integer id) {
        if (id == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        WarehouseDO warehouseDO = warehouseDOMapper.selectByPrimaryKey(id);
        if (warehouseDO == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ID_ERROR);
        }
        if (warehouseDO.getDelFlag() - WarehouseCommonConstant.COMMON_DEL_FLAG_YES == 0) {
            throw new WarehouseException(WarehouseErrorCode.W_DEL_FLAG_YES);
        }
        return warehouseDO;
    }

    public List<WarehouseDO> listByAreaIdListAndOpenFlagAndSyncType(List<Integer> areaIdList, Short openFlag,
        Short syncType) {
        return warehouseDOMapper.listByAreaIdListAndOpenFlagAndSyncType(areaIdList, openFlag, syncType);
    }

    public WarehouseDO getByWarehouseNo(String warehouseNo) {
        WarehouseDO warehouseDO = warehouseDOMapper.getByWarehouseNo(warehouseNo);
        if (warehouseDO == null) {
            throw new WarehouseException(
                "【" + warehouseNo + "】" + MessageUtils.get(WarehouseErrorCode.W_WAREHOUSE_NO_ERROR));
        }
        if (warehouseDO.getDelFlag() - WarehouseCommonConstant.COMMON_DEL_FLAG_YES == 0) {
            throw new WarehouseException(WarehouseErrorCode.W_DEL_FLAG_YES);
        }
        return warehouseDO;
    }

    public WarehouseCO detailById(Integer id) {
        if (id == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        WarehouseCO warehouseCO = warehouseDOMapper.detailById(id);
        if (warehouseCO == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ID_ERROR);
        }
        return warehouseCO;
    }

    public List<WarehouseDO> listByCondition(Integer areaId, Short openFlag, String warehouseNo) {
        return warehouseDOMapper.listByCondition(areaId, openFlag, warehouseNo);
    }

    /**
     * 获取所有库存
     * 
     * @return
     */
    public List<WarehouseDO> listAll() {
        return warehouseDOMapper.listAll();
    }
}
