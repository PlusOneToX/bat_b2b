package com.bat.flexible.api.index;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;

import java.util.List;

public interface DistributorBannerRelevanceServiceI {


    void add(Integer bannerId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin);

    void save(Integer bannerId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin);

    List<DistributorSimpleRelaQry> listDistributorMsgByBannerId(Integer bannerId);

    void deleteByBannerIdList(List<Integer> bannerIdList);
}
