package com.bat.flexible.api.distributor.exclude;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;

import java.util.List;

public interface DistributorModelExcludeServiceI {
    /**
     *
     * @param modelId
     * @param distributorIdList
     * @param currentAdmin
     * @param isAdd
     */
    void saveModelRela(Integer modelId, List<Integer> distributorIdList, AdminResponse currentAdmin, Boolean isAdd);

    /**
     * 根据型号id查询列表
     * @param modelId
     * @return
     */
    List<DistributorModelExcludeDO> listByModelId(Integer modelId);

    /**
     * 根据分销商id查询不可用型号列表
     * @param distributorIds
     * @return
     */
    List<DistributorModelExcludeDO> listByDistributorIds(List<Integer> distributorIds);
}
