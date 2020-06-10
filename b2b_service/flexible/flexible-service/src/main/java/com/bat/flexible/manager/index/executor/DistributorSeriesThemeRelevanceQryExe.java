package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorSeriesThemeRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorSeriesThemeRelevanceQryExe {

    @Autowired
    private DistributorSeriesThemeRelevanceDOMapper distributorSeriesThemeRelevanceDOMapper;

    public List<DistributorSeriesThemeRelevanceDO> listBySeriesThemeId(Integer seriesThemeId) {
        return distributorSeriesThemeRelevanceDOMapper.listBySeriesThemeId(seriesThemeId);
    }

    public List<DistributorSeriesThemeRelevanceDO> listByDistributorIdList(List<Integer> distributorIdList, Integer seriesId) {
        return distributorSeriesThemeRelevanceDOMapper.listByDistributorIdList(distributorIdList,seriesId);
    }
}
