package com.bat.financial.platformaccount.executor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.platformaccount.dto.AccountOfflineCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountOfflineUpdateCmd;
import com.bat.financial.dao.platformaccount.AccountOfflineMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountOfflineDO;
import com.bat.financial.platformaccount.convertor.AccountConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountOfflineCmdExc {

    @Resource
    private AccountOfflineMapper accountOfflineMapper;

    public boolean createAccount(AccountOfflineCreateCmd cmd) {
        AccountOfflineDO aDo = AccountConvertor.toAccountOfflineDO(cmd);
        accountOfflineMapper.insert(aDo);
        return true;
    }

    public boolean deleteAccountById(Integer id) {
        AccountOfflineDO aDo = accountOfflineMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        accountOfflineMapper.deleteByPrimaryKey(id);
        return true;
    }

    public boolean updateAccount(AccountOfflineUpdateCmd cmd) {
        AccountOfflineDO aDo = accountOfflineMapper.selectByPrimaryKey(cmd.getId());
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        AccountOfflineDO aDo1 = AccountConvertor.toAccountOfflineDO(cmd);
        accountOfflineMapper.updateByPrimaryKeySelective(aDo1);
        return true;
    }

}
