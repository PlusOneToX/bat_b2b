package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorIndexRecommendRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorIndexRecommendRelevanceCmdExe {

    @Autowired
    private DistributorIndexRecommendRelevanceDOMapper distributorIndexRecommendRelevanceDOMapper;

    public void create(DistributorIndexRecommendRelevanceDO rela) {
        distributorIndexRecommendRelevanceDOMapper.insert(rela);
    }

    public void deleteById(Integer id) {
        distributorIndexRecommendRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void deleteByIndexRecommendId(Integer indexRecommendId) {
        distributorIndexRecommendRelevanceDOMapper.deleteByIndexRecommendId(indexRecommendId);
    }
}
