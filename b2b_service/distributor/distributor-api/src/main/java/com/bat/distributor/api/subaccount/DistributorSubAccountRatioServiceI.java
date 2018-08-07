package com.bat.distributor.api.subaccount;

import com.bat.distributor.api.subaccount.dto.SubAccountLevelRatioDTO;

import java.util.List;

public interface DistributorSubAccountRatioServiceI {

    /**
     * 新增
     * @param subAccountConfigId
     * @param userId
     * @param userName
     * @param levelRatioList
     */
    void create(Integer subAccountConfigId, String userId, String userName, List<SubAccountLevelRatioDTO> levelRatioList);

    /**
     * 修改
     * @param subAccountConfigId
     * @param userId
     * @param userName
     * @param levelRatioList
     */
    void update(Integer subAccountConfigId, String userId, String userName, List<SubAccountLevelRatioDTO> levelRatioList);

    /**
     * 根据分销商分账配置id查询等级分账比例
     * @param subAccountConfigId
     * @return
     */
    List<SubAccountLevelRatioDTO> listLevelRatioBySubAccountConfigId(Integer subAccountConfigId);

    /**
     * 根据等级id删除
     * @param levelId
     * @param userId
     * @param userName
     */
    void deleteByLevelId(Integer levelId ,Integer userId,String userName);

    /**
     * 根据分销商分账配置id删除
     * @param subAccountConfigId
     * @param userId
     * @param userName
     */
    void deleteBySubAccountConfigId(Integer subAccountConfigId, Integer userId, String userName);
}
