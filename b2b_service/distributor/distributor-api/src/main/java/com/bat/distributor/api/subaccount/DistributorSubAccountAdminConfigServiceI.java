package com.bat.distributor.api.subaccount;

import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import com.bat.distributor.api.base.Response;

public interface DistributorSubAccountAdminConfigServiceI {

    /**
     * 创建
     * @param subAccountAdminConfigCmd
     * @param userId
     * @param userName
     * @return
     */
    Response create(SubAccountAdminConfigCmd subAccountAdminConfigCmd, String userId, String userName);

    /**
     * 修改
     * @param subAccountAdminConfigCmd
     * @param userId
     * @param userName
     * @return
     */
    Response update(SubAccountAdminConfigCmd subAccountAdminConfigCmd, String userId, String userName);

    /**
     * 根据分销商id查询详情
     * @param distributorId
     * @return
     */
    SubAccountAdminConfigCmd detailByDistributorId(Integer distributorId);
}
