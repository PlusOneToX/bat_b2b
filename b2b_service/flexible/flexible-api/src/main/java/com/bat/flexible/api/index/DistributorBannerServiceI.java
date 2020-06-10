package com.bat.flexible.api.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.index.dto.banner.DistributorBannerDTO;
import com.bat.flexible.api.index.dto.page.DistributorBannerPageQry;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.index.co.DistributorBannerPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;

import java.util.List;

public interface DistributorBannerServiceI {


    PageInfo<DistributorBannerPageCO> page(DistributorBannerPageQry distributorBannerPageRrequest);

    Response detail(Integer id);

    Response create(DistributorBannerDTO distributorBannerDTO, AdminResponse currentAdmin);

    Response update(DistributorBannerDTO distributorBannerDTO, AdminResponse currentAdmin);

    Response delete(List<Integer> list);

    Response listByDistributorIdAndStatus(Integer distributorId, Short status);


    List<DistributorBannerDO> listBySeriesId(Integer seriesId);
}
