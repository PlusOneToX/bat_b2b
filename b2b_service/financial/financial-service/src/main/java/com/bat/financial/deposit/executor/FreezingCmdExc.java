package com.bat.financial.deposit.executor;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.deposit.constant.FreezingStatus;
import com.bat.financial.deposit.convertor.FreezingConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.deposit.dto.FreezingCreateCmd;
import com.bat.financial.api.deposit.dto.FreezingDeleteCmd;
import com.bat.financial.dao.deposit.DepositDistributorFreezingMapper;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorDO;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorFreezingDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 14:01
 */
@Component
@Slf4j
public class FreezingCmdExc {

    @Resource
    private DepositDistributorFreezingMapper freezingMapper;

    @Resource
    private DepositBalanceCmdExc depositBalanceCmdExc;

    @Resource
    private DepositBalanceQryExc depositBalanceQryExc;

    @Transactional(rollbackFor = Exception.class)
    public void createFreezing(FreezingCreateCmd cmd) {
        DepositDistributorDO aDo = depositBalanceQryExc.getDepositBalanceByDistributorId(cmd.getDistributorId());
        // 没有金额可以冻结
        if (aDo.getAccountAvailable().compareTo(BigDecimal.ZERO) == 0) {
            throw FinancialException.buildException(ErrorCode.B_FREEZING_AMOUNT_ZERO);
        }
        aDo.setAccountAvailable(aDo.getAccountAvailable().subtract(cmd.getFreezingAmount()));
        aDo.setFreezingAmount(aDo.getFreezingAmount().add(cmd.getFreezingAmount()));
        DepositDistributorFreezingDO depositDistributorFreezingDO =
            FreezingConvertor.toDepositDistributorFreezingDO(cmd);
        depositDistributorFreezingDO.setDepositDistributorId(aDo.getId());
        depositDistributorFreezingDO.setStatus(FreezingStatus.IS_FREEZING);
        depositDistributorFreezingDO.setBusinessType(DepositBusinessType.OTHER_FREEZING);
        depositBalanceCmdExc.updateDepositDistributor(aDo);
        freezingMapper.insert(depositDistributorFreezingDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFreezing(FreezingDeleteCmd cmd) {
        DepositDistributorFreezingDO depositDistributorFreezingDO = freezingMapper.getByPrimaryKeyFreezing(cmd.getId());
        if (depositDistributorFreezingDO == null) {
            throw FinancialException.buildException(ErrorCode.B_DEPOSIT_FREEZING_NOT_EXISTS);
        }
        DepositDistributorDO aDo =
            depositBalanceQryExc.getDepositBalanceByDistributorId(depositDistributorFreezingDO.getDistributorId());
        aDo.setAccountAvailable(aDo.getAccountAvailable().add(depositDistributorFreezingDO.getFreezingAmount()));
        aDo.setFreezingAmount(aDo.getFreezingAmount().subtract(depositDistributorFreezingDO.getFreezingAmount()));
        depositDistributorFreezingDO.setStatus(FreezingStatus.NOT_FREEZING);
        depositBalanceCmdExc.updateDepositDistributor(aDo);
        freezingMapper.updateByPrimaryKey(depositDistributorFreezingDO);
    }
}
