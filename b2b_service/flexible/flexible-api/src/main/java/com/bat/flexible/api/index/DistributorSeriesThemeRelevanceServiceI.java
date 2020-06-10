package com.bat.flexible.api.index;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeRelevanceDO;

import java.util.List;

public interface DistributorSeriesThemeRelevanceServiceI {

    void add(Integer seriesThemeId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin);

    void save(Integer seriesThemeId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin);

    List<DistributorSimpleRelaQry> listDistributorSimpleMsgBySeriesThemeId(Integer seriesThemeId);

    void deleteBySeriesThemeId(Integer seriesThemeId);

    List<DistributorSeriesThemeRelevanceDO> findByDistributorIdList(List<Integer> distributorIdList, Integer seriesId);
}
