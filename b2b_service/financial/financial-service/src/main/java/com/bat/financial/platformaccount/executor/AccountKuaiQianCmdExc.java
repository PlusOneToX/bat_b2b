package com.bat.financial.platformaccount.executor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianUpdateCmd;
import com.bat.financial.dao.platformaccount.AccountKuaiQianMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountKuaiQianDO;
import com.bat.financial.platformaccount.convertor.AccountConvertor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountKuaiQianCmdExc {

    @Resource
    private AccountKuaiQianMapper accountKuaiQianMapper;

    @SneakyThrows
    public boolean createAccount(AccountKuaiQianCreateCmd cmd) {
        AccountKuaiQianDO aDo = AccountConvertor.toAccountKuaiQianDO(cmd);
        accountKuaiQianMapper.insert(aDo);
        return true;
    }

    public boolean updateAccount(AccountKuaiQianUpdateCmd cmd) {
        AccountKuaiQianDO aDo = accountKuaiQianMapper.selectByPrimaryKey(cmd.getId());
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        AccountKuaiQianDO aDo1 = AccountConvertor.toAccountKuaiQianDO(cmd);
        accountKuaiQianMapper.updateByPrimaryKeySelective(aDo1);
        return true;
    }

    public boolean deleteAccountById(Integer id) {
        AccountKuaiQianDO aDo = accountKuaiQianMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        accountKuaiQianMapper.deleteByPrimaryKey(id);
        return true;
    }
}
