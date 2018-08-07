package com.bat.distributor.api.distributor;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.dao.distributor.dataobject.DistributorBusinessDO;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.BaseIds;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;

public interface DistributorServiceI {
    /**
     * 后台添加分销商
     * 
     * @param cmd
     * @return
     */
    public void createDistributor(DistributorCmd cmd, String userId, String userName);

    /**
     * 后台更新分销商
     * 
     * @param cmd
     * @return
     */
    public void updateDistributor(DistributorUpdateCmd cmd, String userId, String userName) throws Exception;

    /**
     * 根据分销商id拒绝申请中的分销商
     * 
     * @param cmd
     */
    void refuse(DistributorId cmd);

    /**
     * 冻结解冻分销商
     * 
     * @param cmd
     * @return
     */
    public void freezeDistributor(DistributorFreezeStatusCmd cmd);

    /**
     * 获取一级分销商列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<DistributorOneListDTO> listOneDistributor(DistributorOneListQry qry);

    /**
     * 分销商列表（分页,包含所有分销商）
     *
     * @param qry
     * @return
     */
    public PageInfo<DistributorListDTO> listDistributor(DistributorListQry qry);

    /**
     * 根据搜索条件查找多级分销商列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<DistributorNextListDTO> listNextDistributor(DistributorNextListQry qry);

    /**
     * 根据ID删除分销商
     * 
     * @param cmd
     * @return
     */
    public void deleteDistributor(DistributorId cmd);

    /**
     * 根据分销商id获取详情
     * 
     * @param qry
     * @return
     */
    public DistributorDTO getDistributor(DistributorId qry);

    /**
     * 根据ids查找分销商基本数据
     * 
     * @param qry
     * @return
     */
    public List<DistributorIdsDTO> distributorIds(BaseIds qry);

    /**
     * 下级分销商审核
     * 
     * @param cmd
     */
    public void checkNextDistributor(DistributorApplyStatusCmd cmd);

    /**
     * 根据分销商id查询分销商业务数据
     *
     * @param distributorId
     * @return
     */
    public DistributorBusinessDO getDistributorBusinessDOByDistributorId(Integer distributorId);

    /**
     * 分销商资料审批列表
     *
     * @param qry
     * @return
     */
    public PageInfo<DistributorCheckListDTO> listDistributorCheck(DistributorCheckListQry qry, String userId);

    /**
     * 分销商资料审批详情
     *
     * @param qry
     * @return
     */
    public DistributorCheckDTO getDistributorCheck(BaseId qry);

    /**
     * 分销商资料审批
     *
     * @param cmd
     * @param userId
     * @param userName
     */
    public void checkDistributor(DistributorCheckCmd cmd, String userId, String userName);
}
