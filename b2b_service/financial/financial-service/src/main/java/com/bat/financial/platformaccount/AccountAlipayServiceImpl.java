package com.bat.financial.platformaccount;

import javax.annotation.Resource;

import com.bat.financial.platformaccount.executor.AccountAlipayCmdExc;
import com.bat.financial.platformaccount.executor.AccountAlipayQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.AccountAlipayService;
import com.bat.financial.api.platformaccount.dto.AccountAlipayCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountAlipayUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountAlipayDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:03
 */
@Service
@Slf4j
public class AccountAlipayServiceImpl implements AccountAlipayService {

    @Resource
    private AccountAlipayQryExc qryExc;

    @Resource
    private AccountAlipayCmdExc cmdExc;

    @Override
    public PageInfo<AccountAlipayDTO> listAccount(AccountQry qry) {
        return qryExc.listAccount(qry);
    }

    @Override
    public AccountAlipayDTO getAccount(Integer id) {
        return qryExc.getAccount(id);
    }

    @Override
    public boolean createAccount(AccountAlipayCreateCmd cmd) {
        return cmdExc.createAccount(cmd);
    }

    @Override
    public boolean updateAccount(AccountAlipayUpdateCmd cmd) {
        return cmdExc.updateAccount(cmd);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return cmdExc.deleteAccountById(id);
    }
}
