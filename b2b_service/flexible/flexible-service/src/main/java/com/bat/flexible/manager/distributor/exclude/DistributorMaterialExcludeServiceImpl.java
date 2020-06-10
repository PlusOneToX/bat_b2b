package com.bat.flexible.manager.distributor.exclude;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorMaterialExcludeCmdExe;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorMaterialExcludeQryExe;
import com.bat.flexible.api.distributor.exclude.DistributorMaterialExcludeServiceI;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DistributorMaterialExcludeServiceImpl implements DistributorMaterialExcludeServiceI {

    @Autowired
    private DistributorMaterialExcludeQryExe distributorMaterialExcludeQryExe;

    @Autowired
    private DistributorMaterialExcludeCmdExe distributorMaterialExcludeCmdExe;

    @Override
    public void save(Integer materialId, List<Integer> distributorIdList, AdminResponse currentAdmin, Boolean isAdd) {
        dealWithByUpdate(materialId,distributorIdList,currentAdmin,isAdd);
        if(distributorIdList !=null && distributorIdList.size()>0){
            distributorIdList.stream().forEach(distributorId -> {
                DistributorMaterialExcludeDO excludeDO = new DistributorMaterialExcludeDO();
                excludeDO.setMaterialId(materialId);
                excludeDO.setDistributorId(distributorId);
                setAdminMsg(excludeDO,currentAdmin);
                excludeDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                distributorMaterialExcludeCmdExe.create(excludeDO);
            });
        }
    }

    @Override
    public List<DistributorMaterialExcludeDO> listByMaterialIdAndDelFlag(Integer materialId, Short delFlag) {
        return distributorMaterialExcludeQryExe.listByMaterialIdAndDelFlag(materialId,delFlag);
    }

    @Override
    public List<DistributorMaterialExcludeDO> listByDistributorIds(List<Integer> distributorIds) {
        return distributorMaterialExcludeQryExe.listByDistributorIds(distributorIds);
    }

    private void dealWithByUpdate(Integer materialId, List<Integer> distributorIdList, AdminResponse currentAdmin, Boolean isAdd) {
        if(isAdd){
            return;
        }
        List<DistributorMaterialExcludeDO>  excludeDOList = distributorMaterialExcludeQryExe.listByMaterialIdAndDelFlag(materialId,FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        if(excludeDOList !=null && excludeDOList.size()>0){
            for(int x=0;x<excludeDOList.size();x++){
                if(distributorIdList !=null && distributorIdList.size()>0){
                    for(int y=0;y<distributorIdList.size();y++){
                        if(excludeDOList.get(x).getDistributorId() - distributorIdList.get(y)==0){
                            excludeDOList.remove(x);
                            x--;
                            distributorIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
            }
            if(excludeDOList !=null && excludeDOList.size()>0){
                excludeDOList.stream().forEach(distributorMaterialExcludeDO -> {
                    setAdminMsg(distributorMaterialExcludeDO,currentAdmin);
                    distributorMaterialExcludeDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                    distributorMaterialExcludeCmdExe.update(distributorMaterialExcludeDO);
                });
            }
        }
    }

    private void setAdminMsg(DistributorMaterialExcludeDO excludeDO, AdminResponse currentAdmin) {
        if(excludeDO.getId() ==null){
            excludeDO.setCreateTime(new Date());
            excludeDO.setCreateUserId(currentAdmin.getId());
            excludeDO.setCreateUserName(currentAdmin.getUserName());
        }
        excludeDO.setUpdateTime(new Date());
        excludeDO.setUpdateUserId(currentAdmin.getId());
        excludeDO.setUpdateUserName(currentAdmin.getUserName());
    }
}
