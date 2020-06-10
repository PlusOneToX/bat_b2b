package com.bat.flexible.manager.label.executor;

import com.bat.flexible.dao.label.LabelDistributorRelevanceDOMapper;
import com.bat.flexible.dao.label.dataobject.LabelDistributorRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabelDistributorRelevanceCmdExe {

    @Autowired
    private LabelDistributorRelevanceDOMapper labelDistributorRelevanceDOMapper;


    public void update(LabelDistributorRelevanceDO labelDistributorRelevanceDO) {
        labelDistributorRelevanceDOMapper.updateByPrimaryKey(labelDistributorRelevanceDO);
    }

    public void create(LabelDistributorRelevanceDO relevanceDO) {
        labelDistributorRelevanceDOMapper.insert(relevanceDO);
    }
}
