package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorIndexRecommendRelevanceDOMapper;
import com.bat.flexible.dao.index.co.IndexRecommendPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorIndexRecommendRelevanceQryExe {

    @Autowired
    private DistributorIndexRecommendRelevanceDOMapper distributorIndexRecommendRelevanceDOMapper;

    public List<DistributorIndexRecommendRelevanceDO> listByIndexRecommendId(Integer indexRecommendId) {
        return distributorIndexRecommendRelevanceDOMapper.listByIndexRecommendId(indexRecommendId);
    }

    public List<DistributorIndexRecommendRelevanceDO> listByDistributorIdList(List<Integer> distributorIdList) {
        return distributorIndexRecommendRelevanceDOMapper.listByDistributorIdList(distributorIdList);
    }

    public List<IndexRecommendPageCO> listCOByContent(String content) {
        return distributorIndexRecommendRelevanceDOMapper.listCOByContent(content);
    }
}
