package com.bat.flexible.manager.third.executor;

import com.bat.flexible.dao.third.ThirdSkuRelevanceDOMapper;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThirdSkuRelevanceCmdExe {

    @Autowired
    private ThirdSkuRelevanceDOMapper thirdSkuRelevanceDOMapper;

    public void update(ThirdSkuRelevanceDO relevanceDO) {
        thirdSkuRelevanceDOMapper.updateByPrimaryKey(relevanceDO);
    }

    public void deleteById(Integer id) {
        thirdSkuRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void deleteByDistributorId(Integer distributorId) {
        thirdSkuRelevanceDOMapper.deleteByDistributorId(distributorId);
    }

    public void create(ThirdSkuRelevanceDO thirdSkuRelevanceDO) {
        thirdSkuRelevanceDOMapper.insert(thirdSkuRelevanceDO);
    }
}
