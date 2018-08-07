package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.dao.subaccount.DistributorSubAccountRatioDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountRatioDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorSubAccountRatioCmdExe {

    @Autowired
    private DistributorSubAccountRatioDOMapper distributorSubAccountRatioDOMapper;

    public void batchCreate(List<DistributorSubAccountRatioDO> ratioDOList) {
        if(ratioDOList ==null || ratioDOList.size()==0){
            return;
        }
        distributorSubAccountRatioDOMapper.batchCreate(ratioDOList);
    }

    public void batchUpdate(List<DistributorSubAccountRatioDO> accountRatioDOList) {
        if(accountRatioDOList ==null || accountRatioDOList.size()==0){
            return;
        }
        distributorSubAccountRatioDOMapper.batchUpdate(accountRatioDOList);
    }

    public void deleteByLevelId(Integer levelId, Integer userId, String userName) {
        distributorSubAccountRatioDOMapper.deleteByLevelId(levelId,userId,userName);
    }
}
