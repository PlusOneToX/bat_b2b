package com.bat.financial.platformaccount.executor;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.platformaccount.dto.AccountWxCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountWxUpdateCmd;
import com.bat.financial.dao.platformaccount.AccountWxMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;
import com.bat.financial.platformaccount.convertor.AccountConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountWxCmdExc {

    @Resource
    private AccountWxMapper accountWxMapper;

    public boolean createAccount(AccountWxCreateCmd cmd) {
        AccountWxDO aDo = AccountConvertor.toAccountWxDO(cmd);
        try {
            accountWxMapper.insert(aDo);
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_organization_id")) {
                throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
            }
        }
        return true;
    }

    public boolean updateAccount(AccountWxUpdateCmd cmd) {
        AccountWxDO aDo = accountWxMapper.selectByPrimaryKey(cmd.getId());
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        AccountWxDO aDo1 = AccountConvertor.toAccountWxDO(cmd);
        aDo1.setCreateTime(aDo.getCreateTime());
        accountWxMapper.updateByPrimaryKeyWithBLOBs(aDo1);
        return true;
    }

    public boolean deleteAccountById(Integer id) {
        AccountWxDO aDo = accountWxMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        accountWxMapper.deleteByPrimaryKey(id);
        return true;
    }
}
