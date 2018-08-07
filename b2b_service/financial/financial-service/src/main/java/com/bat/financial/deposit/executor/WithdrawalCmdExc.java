package com.bat.financial.deposit.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.deposit.convertor.WithdrawConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.financial.api.deposit.dto.FreezingCreateCmd;
import com.bat.financial.api.deposit.dto.WithdrawalCreateCmd;
import com.bat.financial.dao.deposit.WithdrawDepositsDistributorApplyMapper;
import com.bat.financial.dao.deposit.dataobject.WithdrawDepositsDistributorApplyDO;
import com.bat.financial.deposit.constant.DepositBusinessType;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/9 21:52
 */
@Component
public class WithdrawalCmdExc {

    @Resource
    private FreezingCmdExc freezingCmdExc;

    @Resource
    private WithdrawDepositsDistributorApplyMapper withdrawMapper;

    /**
     * 新增提现 余额账户可用扣减，新增冻结，新增提现
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createWithdrawal(WithdrawalCreateCmd cmd) {
        WithdrawDepositsDistributorApplyDO aDo = WithdrawConvertor.toWithdrawDepositsDistributorApplyDO(cmd);
        withdrawMapper.insert(aDo);
        // 直接调用冻结逻辑
        FreezingCreateCmd cmd1 = new FreezingCreateCmd();
        cmd1.setDistributorId(cmd.getDistributorId());
        cmd1.setBusinessType(DepositBusinessType.WITHDRAWAL_FREEZING);
        cmd1.setBusinessId(aDo.getId());
        cmd1.setFreezingAmount(cmd.getWithdrawAmount());
        cmd1.setDistributorName(cmd.getDistributorName());
        cmd1.setRemark(cmd.getRemark());
        freezingCmdExc.createFreezing(cmd1);
        return true;
    }

    public void deleteWithdrawalByIds(List<Integer> ids) {
        withdrawMapper.deleteBatchByPrimaryKey(ids);
    }
}
