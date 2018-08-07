package com.bat.financial.platformaccount.executor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.platformaccount.dto.AccountAlipayCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountAlipayUpdateCmd;
import com.bat.financial.dao.platformaccount.AccountAlipayMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountAlipayDO;
import com.bat.financial.platformaccount.convertor.AccountConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountAlipayCmdExc {

    @Resource
    private AccountAlipayMapper accountAlipayMapper;

    public boolean createAccount(AccountAlipayCreateCmd cmd) {
        AccountAlipayDO aDo = AccountConvertor.toAccountAlipayDO(cmd);
        AccountAlipayDO accountWxDO = accountAlipayMapper.getByOrganizationId(cmd.getOrganizationId());
        if (accountWxDO != null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ORGANIZATION_ID_EXISTS);
        }
        accountAlipayMapper.insert(aDo);
        return true;
    }

    public boolean updateAccount(AccountAlipayUpdateCmd cmd) {
        AccountAlipayDO aDo = accountAlipayMapper.selectByPrimaryKey(cmd.getId());
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        AccountAlipayDO aDo1 = AccountConvertor.toAccountAlipayDO(cmd);
        aDo1.setCreateTime(aDo.getCreateTime());
        accountAlipayMapper.updateByPrimaryKeySelective(aDo1);
        return true;
    }

    public boolean deleteAccountById(Integer id) {
        AccountAlipayDO aDo = accountAlipayMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        accountAlipayMapper.deleteByPrimaryKey(id);
        return true;
    }
}
