package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.dao.subaccount.DistributorSubAccountSalemanDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorSubAccountSalemanCmdExe {

    @Autowired
    DistributorSubAccountSalemanDOMapper distributorSubAccountSalemanDOMapper;

    public void updateLevelIdNull(Integer levelId, Integer userId, String userName) {
        distributorSubAccountSalemanDOMapper.updateLevelIdNull(levelId,userId,userName);
    }

    public void create(DistributorSubAccountSalemanDO distributorSubAccountSalemanDO) {
        distributorSubAccountSalemanDOMapper.insert(distributorSubAccountSalemanDO);
    }

    public void update(DistributorSubAccountSalemanDO distributorSubAccountSalemanDO) {
        distributorSubAccountSalemanDOMapper.updateByPrimaryKey(distributorSubAccountSalemanDO);
    }

    public void batchCreate(List<DistributorSubAccountSalemanDO> list) {
        distributorSubAccountSalemanDOMapper.batchCreate(list);
    }
}
