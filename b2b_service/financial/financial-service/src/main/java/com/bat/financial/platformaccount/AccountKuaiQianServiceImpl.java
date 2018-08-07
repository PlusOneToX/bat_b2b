package com.bat.financial.platformaccount;

import javax.annotation.Resource;

import com.bat.financial.platformaccount.executor.AccountKuaiQianCmdExc;
import com.bat.financial.platformaccount.executor.AccountKuaiQianQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.AccountKuaiQianService;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountKuaiQianDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:03
 */
@Service
@Slf4j
public class AccountKuaiQianServiceImpl implements AccountKuaiQianService {

    @Resource
    private AccountKuaiQianQryExc qryExc;

    @Resource
    private AccountKuaiQianCmdExc cmdExc;

    @Override
    public PageInfo<AccountKuaiQianDTO> listAccount(AccountQry qry) {
        return qryExc.listAccount(qry);
    }

    @Override
    public AccountKuaiQianDTO getAccount(Integer id) {
        return qryExc.getAccount(id);
    }

    @Override
    public boolean createAccount(AccountKuaiQianCreateCmd cmd) {
        return cmdExc.createAccount(cmd);
    }

    @Override
    public boolean updateAccount(AccountKuaiQianUpdateCmd cmd) {
        return cmdExc.updateAccount(cmd);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return cmdExc.deleteAccountById(id);
    }
}
