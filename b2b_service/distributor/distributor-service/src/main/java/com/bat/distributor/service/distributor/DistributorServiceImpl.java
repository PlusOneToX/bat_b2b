package com.bat.distributor.service.distributor;

// package by domain, not by duty

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.BaseIds;
import com.bat.distributor.api.distributor.DistributorServiceI;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.dao.distributor.dataobject.DistributorBusinessDO;
import com.bat.distributor.service.distributor.executor.DistributorCmdExe;
import com.bat.distributor.service.distributor.executor.DistributorQryExe;
import com.bat.dubboapi.goods.brand.api.GoodsBrandServiceRpc;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;

@Service
public class DistributorServiceImpl implements DistributorServiceI {

    @Resource
    private DistributorCmdExe cmdExe;

    @Resource
    private DistributorQryExe qryExe;

    @Resource
    private DistributorQryExe distributorQryExe;

    @DubboReference(check = false, timeout = 30000)
    private GoodsBrandServiceRpc goodsBrandServiceRpc;

    @Override
    public void createDistributor(DistributorCmd cmd, String userId, String userName) {
        cmdExe.createDistributor(cmd, userId, userName);
        if(cmd.getBusiness()!=null&& StringUtils.isNotBlank(cmd.getBusiness().getDistributorGroupIds())) {
            goodsBrandServiceRpc.reSetBrandByDistributorGroupIds(cmd.getBusiness().getDistributorGroupIds());
        }
    }

    @Override
    public void updateDistributor(DistributorUpdateCmd cmd, String userId, String userName) throws Exception {
        cmdExe.updateDistributor(cmd, userId, userName);
        if(cmd.getBusiness()!=null&& StringUtils.isNotBlank(cmd.getBusiness().getDistributorGroupIds())) {
            goodsBrandServiceRpc.reSetBrandByDistributorGroupIds(cmd.getBusiness().getDistributorGroupIds());
        }
    }

    @Override
    public void refuse(DistributorId cmd) {
        cmdExe.refuse(cmd);
    }

    @Override
    public void freezeDistributor(DistributorFreezeStatusCmd cmd) {
        cmdExe.freezeDistributor(cmd);
    }

    @Override
    public PageInfo<DistributorOneListDTO> listOneDistributor(DistributorOneListQry qry) {
        return qryExe.executeOneList(qry);
    }

    @Override
    public PageInfo<DistributorListDTO> listDistributor(DistributorListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public PageInfo<DistributorNextListDTO> listNextDistributor(DistributorNextListQry qry) {
        return qryExe.executeNextList(qry);
    }

    @Override
    public void deleteDistributor(DistributorId cmd) {
        cmdExe.deleteDistributor(cmd);
    }

    @Override
    public DistributorDTO getDistributor(DistributorId qry) {
        return qryExe.execute(qry);
    }

    @Override
    public List<DistributorIdsDTO> distributorIds(BaseIds qry) {
        return qryExe.distributorIds(qry);
    }

    @Override
    public void checkNextDistributor(DistributorApplyStatusCmd cmd) {
        cmdExe.checkNextDistributor(cmd);
    }

    @Override
    public DistributorBusinessDO getDistributorBusinessDOByDistributorId(Integer distributorId) {
        return qryExe.getDistributorBusinessDOByDistributorId(distributorId);
    }

    @Override
    public PageInfo<DistributorCheckListDTO> listDistributorCheck(DistributorCheckListQry qry, String userId) {
        return qryExe.listDistributorCheck(qry, userId);
    }

    @Override
    public DistributorCheckDTO getDistributorCheck(BaseId qry) {
        return qryExe.getDistributorCheck(qry);
    }

    @Override
    public void checkDistributor(DistributorCheckCmd cmd, String userId, String userName) {
        cmdExe.checkDistributor(cmd, userId, userName);
        DistributorBusinessDO distributorBusinessDO = distributorQryExe.findBusinessByCheck(cmd.getId());
        if (distributorBusinessDO != null && StringUtils.isNotBlank(distributorBusinessDO.getDistributorGroupIds())) {
            goodsBrandServiceRpc.reSetBrandByDistributorGroupIds(distributorBusinessDO.getDistributorGroupIds());
        }
    }

}