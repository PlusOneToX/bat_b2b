package com.bat.flexible.manager.distributor.exclude;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorModelExcludeCmdExe;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorModelExcludeQryExe;
import com.bat.flexible.api.distributor.exclude.DistributorModelExcludeServiceI;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DistributorModelExcludeServiceImpl implements DistributorModelExcludeServiceI {

    @Autowired
    private DistributorModelExcludeQryExe distributorModelExcludeQryExe;

    @Autowired
    private DistributorModelExcludeCmdExe distributorModelExcludeCmdExe;

    @Transactional
    @Override
    public void saveModelRela(Integer modelId, List<Integer> distributorIdList, AdminResponse currentAdmin, Boolean isAdd) {
        dealWithByUpdate(modelId,distributorIdList,currentAdmin,isAdd);
        if(distributorIdList !=null && distributorIdList.size()>0){
            distributorIdList.stream().forEach(distributorId -> {
                DistributorModelExcludeDO excludeDO = new DistributorModelExcludeDO();
                excludeDO.setCreateTime(new Date());
                excludeDO.setCreateUserId(currentAdmin.getId());
                excludeDO.setCreateUserName(currentAdmin.getUserName());
                excludeDO.setDistributorId(distributorId);
                excludeDO.setModelId(modelId);
                distributorModelExcludeCmdExe.create(excludeDO);
            });
        }
    }

    @Override
    public List<DistributorModelExcludeDO> listByModelId(Integer modelId) {
        return distributorModelExcludeQryExe.listByModelId(modelId);
    }

    @Override
    public List<DistributorModelExcludeDO> listByDistributorIds(List<Integer> distributorIds) {
        return distributorModelExcludeQryExe.listByDistributorIds(distributorIds);
    }

    /**
     * 处理编辑是否删除关联
     * @param modelId
     * @param distributorIdList
     * @param currentAdmin
     * @param isAdd
     */
    private void dealWithByUpdate(Integer modelId, List<Integer> distributorIdList, AdminResponse currentAdmin, Boolean isAdd) {
        if(isAdd){
            return;
        }
        List<DistributorModelExcludeDO> excludeDOList = distributorModelExcludeQryExe.listByModelId(modelId);
        if(excludeDOList==null || excludeDOList.size()==0){
            return;
        }
        for(int x=0;x<excludeDOList.size();x++){
            if(distributorIdList !=null && distributorIdList.size()>0){
                for(int y=0;y<distributorIdList.size();y++){
                    if(excludeDOList.get(x).getDistributorId() - distributorIdList.get(y)==0){
                        //分销商仍在
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
            excludeDOList.stream().forEach(distributorModelExcludeDO -> {
                //删除
                distributorModelExcludeCmdExe.deleteById(distributorModelExcludeDO.getId());
            });
        }
    }
}
