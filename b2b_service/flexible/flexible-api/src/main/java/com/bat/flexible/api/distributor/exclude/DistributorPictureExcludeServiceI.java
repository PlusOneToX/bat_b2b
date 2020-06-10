package com.bat.flexible.api.distributor.exclude;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;

import java.util.List;

public interface DistributorPictureExcludeServiceI {
    void save(Integer pictureId, List<Integer> distributorIdRemoveList, Boolean isAdd, AdminResponse adminResponse);

    List<DistributorSimpleRelaQry> listByPictureIdAndDelFlag(Integer id, Short delFlag);

    List<DistributorPictureExcludeDO> listByDistributorId(List<Integer> distributorIds);
}
