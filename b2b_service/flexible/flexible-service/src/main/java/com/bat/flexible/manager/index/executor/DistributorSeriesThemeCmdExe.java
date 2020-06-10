package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorSeriesThemeDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorSeriesThemeCmdExe {

    @Autowired
    private DistributorSeriesThemeDOMapper distributorSeriesThemeDOMapper;

    public void update(DistributorSeriesThemeDO seriesTheme) {
        distributorSeriesThemeDOMapper.updateByPrimaryKey(seriesTheme);
    }

    public void create(DistributorSeriesThemeDO seriesTheme) {
        distributorSeriesThemeDOMapper.insert(seriesTheme);
    }

    public void deleteById(Integer id) {
        distributorSeriesThemeDOMapper.deleteByPrimaryKey(id);
    }
}
