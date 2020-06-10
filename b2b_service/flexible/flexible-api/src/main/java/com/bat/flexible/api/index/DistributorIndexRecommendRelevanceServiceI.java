package com.bat.flexible.api.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.index.dto.page.DistributorIndexRecommendPageQry;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.index.co.IndexRecommendPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendRelevanceDO;

import java.util.List;

public interface DistributorIndexRecommendRelevanceServiceI {

    void add(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, Integer indexRecommendId, AdminResponse currentAdmin);

    void save(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, Integer indexRecommendId, AdminResponse currentAdmin);

    void deleteByIndexRecommendId(Integer indexRecommendId);

    /**
     * 根据分销商id列表查询
     * @param distributorIdList
     * @return
     */
    List<DistributorIndexRecommendRelevanceDO> listByDistributorIdList(List<Integer> distributorIdList);

    List<DistributorSimpleRelaQry> listDistributorMsg(Integer indexRecommendId);

    PageInfo<IndexRecommendPageCO> page(DistributorIndexRecommendPageQry distributorIndexRecommendPageQry);
}
