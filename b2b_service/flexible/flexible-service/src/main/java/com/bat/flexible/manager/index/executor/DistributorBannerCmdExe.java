package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorBannerDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorBannerCmdExe {

    @Autowired
    private DistributorBannerDOMapper distributorBannerDOMapper;

    public void create(DistributorBannerDO banner) {
        distributorBannerDOMapper.insert(banner);
    }

    public void createStartEvent(String time, Integer bannerId) {
        distributorBannerDOMapper.dropStartEvent(bannerId);
        distributorBannerDOMapper.createStartEvent(time,bannerId);
    }

    public void createEndEvent(String time, Integer bannerId) {
        distributorBannerDOMapper.dropEndEvent(bannerId);
        distributorBannerDOMapper.createEndEvent(time,bannerId);
    }

    public void delete(Integer id) {
        distributorBannerDOMapper.deleteByPrimaryKey(id);
    }

    public void update(DistributorBannerDO banner) {
        distributorBannerDOMapper.updateByPrimaryKey(banner);
    }
}
