package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.DistributorBannerRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorBannerRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorBannerRelevanceCmdExe {

    @Autowired
    private DistributorBannerRelevanceDOMapper distributorBannerRelevanceDOMapper;

    public void create(DistributorBannerRelevanceDO rela) {
        distributorBannerRelevanceDOMapper.insert(rela);
    }

    public void deleteById(Integer id) {
        distributorBannerRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void deleteByBannerIdList(List<Integer> bannerIdList) {
        distributorBannerRelevanceDOMapper.deleteByBannerIdList(bannerIdList);
    }
}
