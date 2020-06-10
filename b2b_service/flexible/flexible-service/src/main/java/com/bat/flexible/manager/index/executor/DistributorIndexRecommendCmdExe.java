package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorIndexRecommendDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorIndexRecommendCmdExe {

    @Autowired
    private DistributorIndexRecommendDOMapper distributorIndexRecommendDOMapper;

    public void update(DistributorIndexRecommendDO distributorIndexRecommend) {
        distributorIndexRecommendDOMapper.updateByPrimaryKey(distributorIndexRecommend);
    }

    public void delete(Integer id) {
        distributorIndexRecommendDOMapper.deleteByPrimaryKey(id);
    }

    public void create(DistributorIndexRecommendDO distributorIndexRecommend) {
        distributorIndexRecommendDOMapper.insert(distributorIndexRecommend);
    }
}
