package com.bat.distributor.api.subaccount;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.subaccount.dto.SubAccountUserUpdateCmd;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigCmd;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigPageDTO;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigPageQry;

import java.util.List;

public interface DistributorSubAccountUserConfigServiceI {
    /**
     * 创建的
     * @param subAccountUserConfigCmd
     * @param userId
     * @param userName
     * @return
     */
    Response create(SubAccountUserConfigCmd subAccountUserConfigCmd, String userId, String userName);

    /**
     *
     * @param subAccountUserUpdateCmd
     * @param userId
     * @param userName
     * @return
     */
    Response update(SubAccountUserUpdateCmd subAccountUserUpdateCmd, String userId, String userName);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    SubAccountUserUpdateCmd detailById(Integer id);

    /**
     * 分页查询分销商侧配置
     * @param distributorId
     * @param page
     * @param size
     * @return
     */
    PageInfo<SubAccountUserConfigPageDTO> page(String distributorId, Integer page, Integer size);

    /**
     * 运营后台分页查询
     * @param subAccountUserConfigPageQry
     * @return
     */
    PageInfo<SubAccountUserConfigPageDTO> pageAdmin(SubAccountUserConfigPageQry subAccountUserConfigPageQry);

    /**
     * 条件查询
     * @param distributorId
     * @return
     */
    List<DistributorSubAccountUserConfigDO> listByCondition(Integer distributorId);

    /**
     * 删除
     * @param id
     * @param userId
     * @param userName
     */
    void delete(Integer id, Integer userId, String userName);
}
