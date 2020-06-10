package com.bat.flexible.manager.label.executor;

import com.bat.flexible.dao.label.LabelDistributorRelevanceDOMapper;
import com.bat.flexible.dao.label.dataobject.LabelDistributorRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelDistributorRelevanceQryExe {

    @Autowired
    private LabelDistributorRelevanceDOMapper labelDistributorRelevanceDOMapper;


    public List<LabelDistributorRelevanceDO> listByLabelIdAndDistributorId(Integer labelId,Integer distributorId) {
        return labelDistributorRelevanceDOMapper.listByLabelIdAndDistributorId(labelId,distributorId);
    }
}
