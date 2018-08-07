package com.bat.warehouse.manager.warehouse.executor;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseErrorCode;
import com.bat.warehouse.manager.warehouse.validator.WarehouseValidator;
import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.dto.WarehouseOpenFlagDTO;
import com.bat.warehouse.api.base.util.BeanUtils;
import com.bat.warehouse.api.warehouse.dto.WarehouseCmd;
import com.bat.warehouse.dao.warehouse.WarehouseDOMapper;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import java.util.Date;

@Component
public class WarehouseCmdExe {

    @Autowired
    private WarehouseDOMapper warehouseDOMapper;

    @Autowired
    private WarehouseValidator warehouseValidator;

    public Response createWarehouse(WarehouseCmd warehouseCmd, AdminResponse currentUser) {
        //校验
        warehouseValidator.validWarehouseOpenFlag(null,warehouseCmd.getAreaId(),warehouseCmd.getOpenFlag(),warehouseCmd.getWarehouseNo());
        WarehouseDO warehouseDO =BeanUtils.copy(warehouseCmd,WarehouseDO.class);
        setAdminMsg(currentUser, warehouseDO);
        warehouseDO.setDelFlag(WarehouseCommonConstant.COMMON_DEL_FLAG_NO);
        //查询当前最大的排序号
        Integer sort = warehouseDOMapper.findMaxSortNo();
        warehouseDO.setSortNo(sort+1);

        warehouseDOMapper.insert(warehouseDO);
        return Response.buildSuccess();
    }



    /**
     * 设置操作人信息
     * @param currentUser
     * @param warehouseDO
     */
    private void setAdminMsg(AdminResponse currentUser, WarehouseDO warehouseDO) {
        if(warehouseDO.getId() ==null){
            warehouseDO.setCreateTime(new Date());
            warehouseDO.setCreateUserId(currentUser.getId());
            warehouseDO.setCreateUserName(currentUser.getUserName());
        }
        warehouseDO.setUpdateTime(new Date());
        warehouseDO.setUpdateUserId(currentUser.getId());
        warehouseDO.setUpdateUserName(currentUser.getUserName());
    }

    public Response updateWarehouse(WarehouseCmd warehouseCmd, AdminResponse currentUser) {
        if(warehouseCmd.getId() ==null){
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        WarehouseDO warehouseDO = findById(warehouseCmd.getId());
        warehouseDO.setAreaId(warehouseCmd.getAreaId());
        warehouseDO.setCalculationType(warehouseCmd.getCalculationType());
        warehouseDO.setIsPlatform(warehouseCmd.getIsPlatform());
        warehouseDO.setName(warehouseCmd.getName());
        warehouseDO.setRemark(warehouseCmd.getRemark());
        warehouseDO.setWarehouseNo(warehouseCmd.getWarehouseNo());
        setAdminMsg(currentUser,warehouseDO);
        warehouseDO.setOpenFlag(warehouseCmd.getOpenFlag());
        warehouseDO.setSyncType(warehouseCmd.getSyncType());
        //校验
        warehouseValidator.validWarehouseOpenFlag(warehouseCmd.getId(),warehouseCmd.getAreaId(),warehouseCmd.getOpenFlag(),warehouseCmd.getWarehouseNo());
        warehouseDOMapper.updateByPrimaryKey(warehouseDO);
        return Response.buildSuccess();
    }


    public WarehouseDO findById(Integer id){
        if(id ==null){
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        WarehouseDO warehouseDO = warehouseDOMapper.selectByPrimaryKey(id);
        if(warehouseDO==null){
            throw new WarehouseException(WarehouseCommonErrorCode.ID_ERROR);
        }
        if(warehouseDO.getDelFlag() - WarehouseCommonConstant.COMMON_DEL_FLAG_YES ==0){
            throw new WarehouseException(WarehouseErrorCode.W_DEL_FLAG_YES);
        }
        return warehouseDO;
    }

    public Response delete(Integer id, AdminResponse currentUser) {
        WarehouseDO warehouseDO = findById(id);
        setAdminMsg(currentUser,warehouseDO);
        warehouseDO.setDelFlag(WarehouseCommonConstant.COMMON_DEL_FLAG_YES);
        warehouseDO.setDeleteTime(new Date());
        warehouseDOMapper.updateByPrimaryKey(warehouseDO);
        return Response.buildSuccess();
    }

    public Response upOrDown(Integer id, Boolean flag, AdminResponse currentUser) {
        if(flag ==null){
            throw new WarehouseException(WarehouseErrorCode.W_UP_OR_DOWN_NULL);
        }
        WarehouseDO warehouseDO = findById(id);
        WarehouseDO effect = warehouseDOMapper.findEffect(warehouseDO.getSortNo(),flag);
        if(effect==null){
            String error = flag?WarehouseErrorCode.W_UP_TOP_ERROR:WarehouseErrorCode.W_DOWN_LOWEST_ERROR;
            throw new WarehouseException(error);
        }
        Integer sortNo = warehouseDO.getSortNo();
        warehouseDO.setSortNo(effect.getSortNo());
        effect.setSortNo(sortNo);
        setAdminMsg(currentUser,effect);
        setAdminMsg(currentUser,warehouseDO);
        warehouseDOMapper.updateByPrimaryKey(warehouseDO);
        warehouseDOMapper.updateByPrimaryKey(effect);
        return Response.buildSuccess();
    }

    public void updateOpenFlag(WarehouseOpenFlagDTO warehouseOpenFlagDTO, AdminResponse adminResponse) {
        WarehouseDO warehouseDO = findById(warehouseOpenFlagDTO.getId());
        if(warehouseOpenFlagDTO.getOpenFlag() - warehouseDO.getOpenFlag() ==0){
            throw new WarehouseException(WarehouseCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        warehouseDO.setOpenFlag(warehouseOpenFlagDTO.getOpenFlag());
        //校验
        warehouseValidator.validWarehouseOpenFlag(warehouseDO.getId(),warehouseDO.getAreaId(),warehouseDO.getOpenFlag(),warehouseDO.getWarehouseNo());

        setAdminMsg(adminResponse,warehouseDO);
        warehouseDOMapper.updateByPrimaryKey(warehouseDO);
    }

    public void create(WarehouseDO warehouseDO) {
        warehouseDOMapper.insert(warehouseDO);
    }
}
