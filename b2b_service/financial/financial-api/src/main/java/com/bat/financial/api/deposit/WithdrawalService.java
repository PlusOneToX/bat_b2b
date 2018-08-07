package com.bat.financial.api.deposit;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.WithdrawalQry;
import com.bat.financial.api.deposit.dto.data.WithdrawDepositsDistributorApplyDTO;
import com.bat.financial.api.deposit.dto.WithdrawalCreateCmd;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 11:54
 */
public interface WithdrawalService {

    boolean createWithdrawal(WithdrawalCreateCmd cmd);

    PageInfo<WithdrawDepositsDistributorApplyDTO> listWithdrawal(WithdrawalQry qry);

    void deleteWithdrawalByIds(List<Integer> ids);
}
