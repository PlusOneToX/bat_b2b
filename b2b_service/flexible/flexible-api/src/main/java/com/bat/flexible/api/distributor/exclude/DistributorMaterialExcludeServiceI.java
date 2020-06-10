package com.bat.flexible.api.distributor.exclude;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;

import java.util.List;

public interface DistributorMaterialExcludeServiceI {
    /**
     *
     * @param materialId
     * @param distributorIdList
     * @param currentAdmin
     * @param isAdd
     */
    void save(Integer materialId, List<Integer> distributorIdList, AdminResponse currentAdmin, Boolean isAdd);

    List<DistributorMaterialExcludeDO> listByMaterialIdAndDelFlag(Integer materialId, Short delFlag);

    List<DistributorMaterialExcludeDO> listByDistributorIds(List<Integer> distributorIds);
}
