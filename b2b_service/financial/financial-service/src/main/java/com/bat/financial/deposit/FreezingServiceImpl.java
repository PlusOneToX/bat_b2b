package com.bat.financial.deposit;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.deposit.executor.DepositBalanceQryExc;
import com.bat.financial.deposit.executor.FreezingCmdExc;
import com.bat.financial.deposit.executor.FreezingQryExc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.FreezingService;
import com.bat.financial.api.deposit.dto.FreezableDistributorQry;
import com.bat.financial.api.deposit.dto.FreezableQry;
import com.bat.financial.api.deposit.dto.FreezingCreateCmd;
import com.bat.financial.api.deposit.dto.FreezingDeleteCmd;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorFreezingDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 12:21
 */
@Service
@Slf4j
public class FreezingServiceImpl implements FreezingService {

    @Resource
    private FreezingQryExc freezingQryExc;

    @Resource
    private FreezingCmdExc freezingCmdExc;

    @Resource
    private DepositBalanceQryExc depositBalanceQryExc;

    @Override
    public void listFreezableDistributor(FreezableDistributorQry qry) {
        // List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        // freezingQryExc.listFreezableDistributor(qry, distributorIds);
    }

    @Override
    public PageInfo<DepositDistributorFreezingDTO> listFreezing(FreezableQry qry) {
        return freezingQryExc.listFreezingByDistributorId(qry);
    }

    @Override
    public List<DepositDistributorDTO> listDepositAvailableByDistributorIds(List<Integer> id) {
        return depositBalanceQryExc.listDepositAvailableByDistributorIds(id);
    }

    @Override
    public void createFreezing(FreezingCreateCmd cmd) {
        freezingCmdExc.createFreezing(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFreezings(List<FreezingCreateCmd> cmds) {
        // TODO
        cmds.forEach(createCmd -> {
            freezingCmdExc.createFreezing(createCmd);
        });
    }

    @Override
    public void updateFreezing(FreezingDeleteCmd cmd) {
        freezingCmdExc.updateFreezing(cmd);
    }

}
