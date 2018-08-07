package com.bat.warehouse.manager.reserved;

import com.bat.warehouse.manager.common.constant.WarehouseStockReservedConstant;
import com.bat.warehouse.manager.reserved.executor.WarehouseStockReservedDetailCmdExe;
import com.bat.warehouse.manager.reserved.executor.WarehouseStockReservedDetailQryExe;
import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.reservedDetail.WarehouseStockReservedDetailServiceI;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedDetailCmd;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WarehouseStockReservedDetailServiceImpl implements WarehouseStockReservedDetailServiceI {


    @Autowired
    private WarehouseStockReservedDetailQryExe warehouseStockReservedDetailQryExe;


    @Autowired
    private WarehouseStockReservedDetailCmdExe warehouseStockReservedDetailCmdExe;

    @Override
    public List<WarehouseStockReservedDetailDO> listByReservedId(Integer reservedId) {
        return warehouseStockReservedDetailQryExe.listByReservedId(reservedId);
    }

    @Override
    @Transactional
    public List<WarehouseStockReservedDetailDO>  save(List<WarehouseStockReservedDetailCmd> detailCmdList, AdminResponse currentAdmin,  Short source,Integer reservedId) {
        //处理修改、不做修改
       // dealWithByUpdate(detailCmdList,currentAdmin,isAdd,source,reservedId);
        List<WarehouseStockReservedDetailDO> detailDOList = new ArrayList<>();
        if(detailCmdList !=null && detailCmdList.size()>0){
            detailCmdList.stream().forEach(warehouseStockReservedDetailCmd -> {
                WarehouseStockReservedDetailDO detailDO = new WarehouseStockReservedDetailDO();
                detailDO.setItemId(warehouseStockReservedDetailCmd.getItemId());
                detailDO.setNumReserved(warehouseStockReservedDetailCmd.getNumReserved());
                detailDO.setGoodsId(warehouseStockReservedDetailCmd.getGoodsId());
                detailDO.setItemName(warehouseStockReservedDetailCmd.getItemName());
                detailDO.setReservedId(reservedId);
                if(WarehouseStockReservedConstant.SOURCE_B2B_ADMIN.equals(source)){
                    setAdminMsg(detailDO,currentAdmin);
                }else{
                    detailDO.setUpdateTime(new Date());
                }
                warehouseStockReservedDetailCmdExe.create(detailDO);
                detailDOList.add(detailDO);
            });
        }
        return detailDOList;
    }

    private void dealWithByUpdate(List<WarehouseStockReservedDetailCmd> detailCmdList, AdminResponse currentAdmin, Boolean isAdd, Short source, Integer reservedId) {
        if(isAdd){
            return;
        }
        List<WarehouseStockReservedDetailDO> detailDOList = warehouseStockReservedDetailQryExe.listByReservedId(reservedId);
        if(detailDOList !=null && detailDOList.size()>0){
            for(int x=0;x<detailDOList.size();x++){
                for(int y=0;y<detailCmdList.size();y++){
                    if(detailCmdList.get(y).getId()!=null && detailDOList.get(x).getId() - detailCmdList.get(y).getId()==0){
                        //修改
                        WarehouseStockReservedDetailDO reservedDetailDO = detailDOList.get(x);
                        reservedDetailDO.setNumReserved(detailCmdList.get(x).getNumReserved());
                        if(WarehouseStockReservedConstant.SOURCE_B2B_ADMIN.equals(source)){
                            setAdminMsg(reservedDetailDO,currentAdmin);
                        }else{
                            reservedDetailDO.setUpdateTime(new Date());
                        }
                        warehouseStockReservedDetailCmdExe.update(reservedDetailDO);
                        detailDOList.remove(x);
                        x--;
                        detailCmdList.remove(y);
                        y--;
                        break;
                    }
                }
            }
            if(detailDOList !=null && detailDOList.size()>0){
                //删除了
                detailDOList.stream().forEach(warehouseStockReservedDetailDO -> {
                    warehouseStockReservedDetailCmdExe.deleteById(warehouseStockReservedDetailDO.getId());
                });
            }
        }
    }

    private void setAdminMsg(WarehouseStockReservedDetailDO reservedDetailDO, AdminResponse currentAdmin) {
        if(reservedDetailDO.getId() ==null){
            reservedDetailDO.setCreateTime(new Date());
            reservedDetailDO.setCreateUserId(currentAdmin.getId());
            reservedDetailDO.setCreateUserName(currentAdmin.getName());
        }
        reservedDetailDO.setUpdateTime(new Date());
        reservedDetailDO.setUpdateUserId(currentAdmin.getId());
        reservedDetailDO.setUpdateUserName(currentAdmin.getName());
    }
}
