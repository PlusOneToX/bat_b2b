package com.bat.financial.platformaccount;

import javax.annotation.Resource;

import com.bat.financial.platformaccount.executor.AccountWxCmdExc;
import com.bat.financial.platformaccount.executor.AccountWxQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.AccountWxService;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.AccountWxCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountWxUpdateCmd;
import com.bat.financial.api.platformaccount.dto.data.AccountWxDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:03
 */
@Service
@Slf4j
public class AccountWxServiceImpl implements AccountWxService {

    @Resource
    private AccountWxQryExc qryExc;

    @Resource
    private AccountWxCmdExc cmdExc;

    @Override
    public PageInfo<AccountWxDTO> listAccount(AccountQry qry) {
        return qryExc.listAccount(qry);
    }

    @Override
    public AccountWxDTO getAccount(Integer id) {
        return qryExc.getAccount(id);
    }

    @Override
    public boolean createAccount(AccountWxCreateCmd cmd) {
        return cmdExc.createAccount(cmd);
    }

    @Override
    public boolean updateAccount(AccountWxUpdateCmd cmd) {
        return cmdExc.updateAccount(cmd);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return cmdExc.deleteAccountById(id);
    }
}
