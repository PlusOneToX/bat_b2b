package com.bat.flexible.manager.index.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.index.DistributorBannerDOMapper;
import com.bat.flexible.dao.index.co.DistributorBannerListCO;
import com.bat.flexible.dao.index.co.DistributorBannerPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorBannerQryExe {

    @Autowired
    private DistributorBannerDOMapper distributorBannerDOMapper;

    public DistributorBannerDO findMaxSortNo() {
        return distributorBannerDOMapper.findMaxSortNo();
    }

    public List<DistributorBannerDO> listBySeriesId(Integer seriesId) {
        return distributorBannerDOMapper.listBySeriesId(seriesId);
    }

    public DistributorBannerDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        DistributorBannerDO distributorBannerDO = distributorBannerDOMapper.selectByPrimaryKey(id);
        if(distributorBannerDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return distributorBannerDO;
    }

    public List<DistributorBannerListCO> listCOByDistributorIdsAndStatus(List<Integer> distributorIds, Short status) {
        return distributorBannerDOMapper.listCOByDistributorIdsAndStatus(distributorIds,status);
    }

    public List<DistributorBannerPageCO> listCOByCondition(Short showLocation, Short status, String content) {
        return distributorBannerDOMapper.listCOByCondition(showLocation,status,content);
    }
}
