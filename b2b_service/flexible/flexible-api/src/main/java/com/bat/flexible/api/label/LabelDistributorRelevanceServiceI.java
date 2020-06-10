package com.bat.flexible.api.label;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;

import java.util.List;

public interface LabelDistributorRelevanceServiceI {
    /**
     * 保存分销商和标签的关联关系
     * @param distributorIdList
     * @param labelId
     * @param currentAdmin
     * @param isAdd
     */
    void saveRela(List<Integer> distributorIdList, Integer labelId, AdminResponse currentAdmin, boolean isAdd);

    /**
     * 根据标签id和分销商id查询列表
     * @param labelId
     * @param distributorId
     * @return
     */
    List<DistributorSimpleRelaQry> listDTOByCondition(Integer labelId, Integer distributorId);
}
