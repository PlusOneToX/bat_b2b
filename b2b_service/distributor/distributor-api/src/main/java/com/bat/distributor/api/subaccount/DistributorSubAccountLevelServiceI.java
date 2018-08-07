package com.bat.distributor.api.subaccount;

import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;

import java.util.List;

public interface DistributorSubAccountLevelServiceI {
    /**
     * 保存分销商分账等级
     * @param isAdd 是否新增 True 新增 false 修改
     * @param distributorId
     * @param levelNameList
     * @param userId
     * @param userName
     */
    void save(boolean isAdd, Integer distributorId, List<String> levelNameList, String userId, String userName);

    /**
     * 根据分销商id查询等级列表
     * @param distributorId
     * @return
     */
    List<DistributorSubAccountLevelDO> listByDistributorId(Integer distributorId);
}
