package com.bat.financial.distributoraccount.executor;

import javax.annotation.Resource;

import com.bat.financial.distributoraccount.convertor.AccountDistributorConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorUpdateCmd;
import com.bat.financial.dao.distributoraccount.AccountAlipayDistributorMapper;
import com.bat.financial.dao.distributoraccount.dataobject.AccountAlipayDistributorDO;
import com.bat.financial.platformaccount.executor.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountAlipayDistributorCmdExc {

    @Resource
    private AccountAlipayDistributorMapper accountAlipayDistributorMapper;

    public boolean createAccount(AccountAlipayDistributorCreateCmd cmd) {
        accountAlipayDistributorMapper.insertBatch(AccountDistributorConvertor.toAccountAlipayDistributorDOList(cmd));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccount(AccountAlipayDistributorUpdateCmd cmd) {
        AccountAlipayDistributorDO aDo = accountAlipayDistributorMapper.selectByPrimaryKey(cmd.getId());
        accountAlipayDistributorMapper.deleteByAppid(aDo.getAppId());
        accountAlipayDistributorMapper.insertBatch(AccountDistributorConvertor.toAccountAlipayDistributorDOList(cmd));
        return true;
    }

    public boolean deleteAccountById(Integer id) {
        AccountAlipayDistributorDO aDo = accountAlipayDistributorMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        accountAlipayDistributorMapper.deleteByPrimaryKey(id);
        return true;
    }
}
