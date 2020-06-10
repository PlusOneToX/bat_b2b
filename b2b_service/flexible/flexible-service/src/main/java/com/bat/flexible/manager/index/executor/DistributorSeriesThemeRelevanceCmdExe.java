package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorSeriesThemeRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorSeriesThemeRelevanceCmdExe {

    @Autowired
    private DistributorSeriesThemeRelevanceDOMapper distributorSeriesThemeRelevanceDOMapper;

    public void create(DistributorSeriesThemeRelevanceDO rela) {
        distributorSeriesThemeRelevanceDOMapper.insert(rela);
    }

    public void deleteById(Integer id) {
        distributorSeriesThemeRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void deleteBySeriesThemeId(Integer seriesThemeId) {
        distributorSeriesThemeRelevanceDOMapper.deleteBySeriesThemeId(seriesThemeId);
    }
}
