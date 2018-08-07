package com.bat.financial.deposit.convertor;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.deposit.dto.WithdrawalCreateCmd;
import com.bat.financial.dao.deposit.dataobject.WithdrawDepositsDistributorApplyDO;
import com.bat.financial.deposit.constant.DepositApplyStatus;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/31 14:15
 */
public class WithdrawConvertor {
    public static WithdrawDepositsDistributorApplyDO toWithdrawDepositsDistributorApplyDO(WithdrawalCreateCmd cmd) {
        WithdrawDepositsDistributorApplyDO aDo = new WithdrawDepositsDistributorApplyDO();
        BeanUtils.copyProperties(cmd, aDo);
        aDo.setApplyStatus(DepositApplyStatus.UNCONFIRMED);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }
}
