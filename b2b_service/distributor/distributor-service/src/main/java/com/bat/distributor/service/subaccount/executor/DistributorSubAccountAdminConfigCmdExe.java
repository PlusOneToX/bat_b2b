package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.api.subaccount.DistributorSubAccountLevelServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import com.bat.distributor.dao.subaccount.DistributorSubAccountAdminConfigDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountAdminConfigDO;
import com.bat.distributor.service.subaccount.validator.SubAccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DistributorSubAccountAdminConfigCmdExe {

    @Autowired
    private DistributorSubAccountAdminConfigDOMapper distributorSubAccountAdminConfigDOMapper;

    @Autowired
    private DistributorSubAccountLevelServiceI distributorSubAccountLevelServiceI;

    public void create(SubAccountAdminConfigCmd subAccountAdminConfigCmd, String userId, String userName) {
        //校验延迟时间
        SubAccountValidator.validateDelayTime(subAccountAdminConfigCmd);
        DistributorSubAccountAdminConfigDO subAccountAdminConfigDO = new DistributorSubAccountAdminConfigDO();
        subAccountAdminConfigDO.setAgingType(subAccountAdminConfigCmd.getAgingType());
        subAccountAdminConfigDO.setDelayTime(subAccountAdminConfigCmd.getDelayTime());
        subAccountAdminConfigDO.setDistributorId(subAccountAdminConfigCmd.getDistributorId());
        //设置操作人
        setAdmin(subAccountAdminConfigDO,userId,userName);
        distributorSubAccountAdminConfigDOMapper.insert(subAccountAdminConfigDO);
        //处理等级
        distributorSubAccountLevelServiceI.save(true, subAccountAdminConfigCmd.getDistributorId(),subAccountAdminConfigCmd.getLevelNameList(),userId,userName);
    }

    private void setAdmin(DistributorSubAccountAdminConfigDO subAccountAdminConfigDO, String userId, String userName) {
        if(subAccountAdminConfigDO.getId() ==null){
            subAccountAdminConfigDO.setCreateTime(new Date());
            subAccountAdminConfigDO.setCreateUserId(Integer.parseInt(userId));
            subAccountAdminConfigDO.setCreateUserName(userName);
        }
        subAccountAdminConfigDO.setUpdateTime(new Date());
        subAccountAdminConfigDO.setUpdateUserName(userName);
        subAccountAdminConfigDO.setUpdateUserId(Integer.parseInt(userId));
    }


    public void update(SubAccountAdminConfigCmd subAccountAdminConfigCmd, String userId, String userName) {
        //校验延迟时间
        SubAccountValidator.validateDelayTime(subAccountAdminConfigCmd);
        DistributorSubAccountAdminConfigDO subAccountAdminConfigDO = distributorSubAccountAdminConfigDOMapper.getByDistributorId(subAccountAdminConfigCmd.getDistributorId());
        if(subAccountAdminConfigDO ==null){
            subAccountAdminConfigDO = new DistributorSubAccountAdminConfigDO();
            subAccountAdminConfigDO.setDistributorId(subAccountAdminConfigCmd.getDistributorId());
        }
        subAccountAdminConfigDO.setAgingType(subAccountAdminConfigCmd.getAgingType());
        subAccountAdminConfigDO.setDelayTime(subAccountAdminConfigCmd.getDelayTime());
        //设置操作人
        setAdmin(subAccountAdminConfigDO,userId,userName);
        if(subAccountAdminConfigDO.getId() !=null){
            distributorSubAccountAdminConfigDOMapper.updateByPrimaryKey(subAccountAdminConfigDO);
            //处理等级
            distributorSubAccountLevelServiceI.save(false, subAccountAdminConfigCmd.getDistributorId(),subAccountAdminConfigCmd.getLevelNameList(),userId,userName);
        }else{
            distributorSubAccountAdminConfigDOMapper.insert(subAccountAdminConfigDO);
            //处理等级
            distributorSubAccountLevelServiceI.save(true, subAccountAdminConfigCmd.getDistributorId(),subAccountAdminConfigCmd.getLevelNameList(),userId,userName);
        }
    }




}
