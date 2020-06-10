package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorBannerRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorBannerRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorBannerRelevanceQryExe {

    @Autowired
    private DistributorBannerRelevanceDOMapper distributorBannerRelevanceDOMapper;

    public List<DistributorBannerRelevanceDO> listByBannerId(Integer bannerId) {
        return distributorBannerRelevanceDOMapper.listByBannerId(bannerId);
    }
}
