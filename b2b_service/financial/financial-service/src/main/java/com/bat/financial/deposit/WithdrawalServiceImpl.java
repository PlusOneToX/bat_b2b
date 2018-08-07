package com.bat.financial.deposit;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.deposit.executor.WithdrawalCmdExc;
import com.bat.financial.deposit.executor.WithdrawalQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.WithdrawalService;
import com.bat.financial.api.deposit.dto.WithdrawalCreateCmd;
import com.bat.financial.api.deposit.dto.WithdrawalQry;
import com.bat.financial.api.deposit.dto.data.WithdrawDepositsDistributorApplyDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/9 21:43
 */
@Service
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {

    @Resource
    private WithdrawalCmdExc withdrawalCmdExc;

    @Resource
    private WithdrawalQryExc withdrawalQryExc;

    @Resource
    private DepositDetailServiceImpl depositDetailService;

    @Override
    public boolean createWithdrawal(WithdrawalCreateCmd cmd) {
        return withdrawalCmdExc.createWithdrawal(cmd);
    }

    @Override
    public PageInfo<WithdrawDepositsDistributorApplyDTO> listWithdrawal(WithdrawalQry qry) {
        return withdrawalQryExc.listWithdrawal(qry);
    }

    @Override
    public void deleteWithdrawalByIds(List<Integer> ids) {
        withdrawalCmdExc.deleteWithdrawalByIds(ids);
    }
}
