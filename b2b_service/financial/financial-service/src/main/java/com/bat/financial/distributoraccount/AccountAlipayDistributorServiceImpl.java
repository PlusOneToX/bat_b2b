package com.bat.financial.distributoraccount;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.distributoraccount.executor.AccountAlipayDistributorCmdExc;
import com.bat.financial.distributoraccount.executor.AccountAlipayDistributorQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.distributoraccount.AccountAlipayDistributorService;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorUpdateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountQry;
import com.bat.financial.api.distributoraccount.dto.data.AccountAlipayDistributorDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:59
 */
@Service
@Slf4j
public class AccountAlipayDistributorServiceImpl implements AccountAlipayDistributorService {
    @Resource
    private AccountAlipayDistributorQryExc qryExc;

    @Resource
    private AccountAlipayDistributorCmdExc cmdExc;

    @Override
    public PageInfo<AccountAlipayDistributorDTO> listAccount(AccountQry qry) {
        return qryExc.listAccount(qry);
    }

    @Override
    public AccountAlipayDistributorDTO getAccount(String appId) {
        return qryExc.getAccount(appId);
    }

    @Override
    public boolean createAccount(AccountAlipayDistributorCreateCmd cmd) {
        return cmdExc.createAccount(cmd);
    }

    @Override
    public boolean updateAccount(AccountAlipayDistributorUpdateCmd cmd) {
        return cmdExc.updateAccount(cmd);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return cmdExc.deleteAccountById(id);
    }

    @Override
    public List<Integer> checkDistributor(List<Integer> ids) {
        return qryExc.checkDistributor(ids);
    }
}
