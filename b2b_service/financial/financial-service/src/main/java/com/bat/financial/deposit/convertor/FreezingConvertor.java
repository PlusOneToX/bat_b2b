package com.bat.financial.deposit.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.deposit.dto.FreezingCreateCmd;
import com.bat.financial.api.deposit.dto.data.DepositDistributorFreezingDTO;
import com.bat.financial.api.deposit.dto.data.WithdrawDepositsDistributorApplyDTO;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorFreezingDO;
import com.bat.financial.dao.deposit.dataobject.WithdrawDepositsDistributorApplyDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 14:38
 */
public class FreezingConvertor {
    public static DepositDistributorFreezingDO toDepositDistributorFreezingDO(FreezingCreateCmd cmd) {
        DepositDistributorFreezingDO aDo = new DepositDistributorFreezingDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static List<DepositDistributorFreezingDTO>
        toDepositDistributorFreezingDOList(List<DepositDistributorFreezingDO> dos) {
        return dos.stream().map(depositDistributorFreezingDO -> {
            DepositDistributorFreezingDTO aDo = new DepositDistributorFreezingDTO();
            BeanUtils.copyProperties(depositDistributorFreezingDO, aDo);
            return aDo;
        }).collect(Collectors.toList());
    }

    public static List<WithdrawDepositsDistributorApplyDTO>
        toWithdrawDepositsDistributorApplyDTOList(List<WithdrawDepositsDistributorApplyDO> dos) {
        return dos.stream().map(applyDO -> {
            WithdrawDepositsDistributorApplyDTO aDo = new WithdrawDepositsDistributorApplyDTO();
            BeanUtils.copyProperties(applyDO, aDo);
            return aDo;
        }).collect(Collectors.toList());
    }
}
