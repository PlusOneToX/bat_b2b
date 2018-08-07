package com.bat.financial.platformaccount;

import javax.annotation.Resource;

import com.bat.financial.platformaccount.executor.AccountOfflineCmdExc;
import com.bat.financial.platformaccount.executor.AccountOfflineQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.AccountOfflineService;
import com.bat.financial.api.platformaccount.dto.AccountOfflineCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountOfflineUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountOfflineDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:03
 */
@Service
@Slf4j
public class AccountOfflineServiceImpl implements AccountOfflineService {

    @Resource
    private AccountOfflineQryExc qryExc;

    @Resource
    private AccountOfflineCmdExc cmdExc;

    @Override
    public PageInfo<AccountOfflineDTO> listAccount(AccountQry qry) {
        return qryExc.listAccount(qry);
    }

    @Override
    public boolean createAccount(AccountOfflineCreateCmd cmd) {
        return cmdExc.createAccount(cmd);
    }

    @Override
    public AccountOfflineDTO getAccount(Integer id) {
        return qryExc.getAccount(id);
    }

    @Override
    public boolean updateAccount(AccountOfflineUpdateCmd cmd) {
        return cmdExc.updateAccount(cmd);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return cmdExc.deleteAccountById(id);
    }

}
