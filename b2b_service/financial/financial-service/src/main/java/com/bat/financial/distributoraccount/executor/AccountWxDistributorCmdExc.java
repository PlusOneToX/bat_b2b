package com.bat.financial.distributoraccount.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.distributoraccount.convertor.AccountDistributorConvertor;
import com.bat.financial.distributoraccount.validator.DistributorAccountValidator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.distributoraccount.dto.AccountWxDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountWxDistributorUpdateCmd;
import com.bat.financial.api.distributoraccount.dto.DistributorInfo;
import com.bat.financial.dao.distributoraccount.AccountWxDistributorMapper;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;
import com.bat.financial.platformaccount.executor.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountWxDistributorCmdExc {

    @Resource
    private AccountWxDistributorMapper accountWxDistributorMapper;

    public boolean createAccount(AccountWxDistributorCreateCmd cmd) {
        // 校验参数
        DistributorAccountValidator.validatorWxPayAccountCreate(cmd);
        try {
            accountWxDistributorMapper.insertBatch(AccountDistributorConvertor.toAccountWxDistributorDOList(cmd));
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("distributor_id")) {
                throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccount(AccountWxDistributorUpdateCmd cmd) {
        // 校验参数
        DistributorAccountValidator.validatorWxPayAccountUpdate(cmd);
        AccountWxDistributorDO aDo = accountWxDistributorMapper.selectByPrimaryKey(cmd.getId());
        accountWxDistributorMapper.deleteByAppidAndSubMchid(aDo.getAppId(), cmd.getSubMchid());
        List<Integer> distributorId =
            cmd.getDistributorInfos().stream().map(DistributorInfo::getDistributorId).collect(Collectors.toList());
        accountWxDistributorMapper.deleteByDistributorIds(distributorId);
        accountWxDistributorMapper.insertBatch(AccountDistributorConvertor.toAccountWxDistributorDOList(cmd));
        return true;
    }

    public boolean deleteAccountById(Integer id) {
        AccountWxDistributorDO aDo = accountWxDistributorMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_ID_NOT_EXISTS);
        }
        accountWxDistributorMapper.deleteByPrimaryKey(id);
        return true;
    }
}
