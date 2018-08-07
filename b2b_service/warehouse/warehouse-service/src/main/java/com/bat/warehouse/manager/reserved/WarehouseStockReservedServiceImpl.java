package com.bat.warehouse.manager.reserved;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.common.constant.WarehouseStockReservedConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.inStock.executor.WarehouseStockSyncCmdExe;
import com.bat.warehouse.manager.reserved.convertor.WarehouseStockReservedConvertor;
import com.bat.warehouse.manager.reserved.executor.WarehouseStockReservedCmdExe;
import com.bat.warehouse.manager.reserved.executor.WarehouseStockReservedQryExe;
import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.reservedDetail.WarehouseStockReservedDetailServiceI;
import com.bat.warehouse.api.warehouseStockReserved.WarehouseStockReservedServiceI;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedCmd;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedDetailDTO;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedPageQry;
import com.bat.warehouse.dao.stockReserved.dataobject.WarehouseStockReservedDO;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.warehouse.executor.WarehouseQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseStockReservedServiceImpl implements WarehouseStockReservedServiceI {

    @Autowired
    private WarehouseStockReservedQryExe warehouseStockReservedQryExe;

    @Autowired
    private WarehouseQryExe warehouseQryExe;

    @Autowired
    private WarehouseStockReservedCmdExe warehouseStockReservedCmdExe;

    @Lazy
    @Autowired
    private WarehouseStockReservedDetailServiceI warehouseStockReservedDetailServiceI;

    @Lazy
    @Autowired
    private WarehouseStockSyncCmdExe warehouseStockSyncCmdExe;



    @Override
    public Response page(WarehouseStockReservedPageQry warehouseStockReservedPageQry) {
        return warehouseStockReservedQryExe.page(warehouseStockReservedPageQry);
    }

    @Override
    public Response getDetailById(Integer id) {
       
        WarehouseStockReservedDO warehouseStockReservedDO = warehouseStockReservedQryExe.getById(id);
        WarehouseStockReservedDetailDTO reservedDetailDTO = WarehouseStockReservedConvertor.toWarehouseStockReservedDetailDTO(warehouseStockReservedDO);
        WarehouseDO warehouseDO = warehouseQryExe.getById(warehouseStockReservedDO.getWarehouseId());
        reservedDetailDTO.setWarehouseName(warehouseDO.getName());
        List<WarehouseStockReservedDetailDO> detailDOList = warehouseStockReservedDetailServiceI.listByReservedId(warehouseStockReservedDO.getId());
        reservedDetailDTO.setDetailDOList(detailDOList);
        return Response.of(reservedDetailDTO);
    }

    /**
     * 后台添加
     * @param warehouseStockReservedCmd
     * @param currentAdmin
     * @return
     */
    @Override
    public Response createByAdmin(WarehouseStockReservedCmd warehouseStockReservedCmd, AdminResponse currentAdmin) {
        WarehouseStockReservedDO warehouseStockReservedDO = new WarehouseStockReservedDO();
        //校验仓库id
        WarehouseDO warehouseDO = warehouseQryExe.getById(warehouseStockReservedCmd.getWarehouseId());
        warehouseStockReservedDO.setRemark(warehouseStockReservedCmd.getRemark());
        warehouseStockReservedDO.setWarehouseId(warehouseStockReservedCmd.getWarehouseId());
        warehouseStockReservedDO.setStatus(WarehouseStockReservedConstant.RESERVED_STATUS_UN_DEAL_WITH);
        warehouseStockReservedDO.setSource(WarehouseStockReservedConstant.SOURCE_B2B_ADMIN);
        setAdminMsg(warehouseStockReservedDO,currentAdmin);
        warehouseStockReservedCmdExe.create(warehouseStockReservedDO);
        //保存关联关系
        List<WarehouseStockReservedDetailDO> detailDOList = warehouseStockReservedDetailServiceI.save(warehouseStockReservedCmd.getDetailCmdList(),currentAdmin,warehouseStockReservedDO.getSource(),warehouseStockReservedDO.getId());
        //设置库存预留
        Map<Integer, Integer> itemStockMap = WarehouseStockReservedConvertor.toItemReservedCountMap(detailDOList,true);
        //操作预留库存
        warehouseStockSyncCmdExe.operateReservedStock(itemStockMap,warehouseStockReservedDO.getWarehouseId());
        return Response.buildSuccess();
    }

    @Override
    public Response release(Integer id, AdminResponse currentAdmin) {
        WarehouseStockReservedDO stockReservedDO = warehouseStockReservedQryExe.getById(id);
        if(WarehouseStockReservedConstant.RESERVED_STATUS_ALREADY_DEAL_WITH.equals(stockReservedDO.getStatus())){
            //已经处理
            throw new WarehouseException(WarehouseCommonErrorCode.COMMON_OPERATE_REPEAT);
        }

        List<WarehouseStockReservedDetailDO> detailDOList  = warehouseStockReservedDetailServiceI.listByReservedId(id);
        Map<Integer, Integer> itemStockMap = WarehouseStockReservedConvertor.toItemReservedCountMap(detailDOList,false);
        //操作预留库存
        warehouseStockSyncCmdExe.operateReservedStock(itemStockMap,stockReservedDO.getWarehouseId());
        stockReservedDO.setStatus(WarehouseStockReservedConstant.RESERVED_STATUS_ALREADY_DEAL_WITH);
        setAdminMsg(stockReservedDO,currentAdmin);
        warehouseStockReservedCmdExe.update(stockReservedDO);
        return Response.buildSuccess();
    }



    private void setAdminMsg(WarehouseStockReservedDO warehouseStockReservedDO, AdminResponse currentAdmin) {
        if(warehouseStockReservedDO.getId() ==null){
            warehouseStockReservedDO.setCreateTime(new Date());
            warehouseStockReservedDO.setCreateUserId(currentAdmin.getId());
            warehouseStockReservedDO.setCreateUserName(currentAdmin.getName());
        }
        warehouseStockReservedDO.setUpdateTime(new Date());
        warehouseStockReservedDO.setUpdateUserId(currentAdmin.getId());
        warehouseStockReservedDO.setUpdateUserName(currentAdmin.getName());
    }
}
