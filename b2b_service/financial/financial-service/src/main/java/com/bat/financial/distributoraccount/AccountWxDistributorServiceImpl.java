package com.bat.financial.distributoraccount;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.api.distributoraccount.dto.*;
import com.bat.financial.distributoraccount.executor.AccountWxDistributorCmdExc;
import com.bat.financial.distributoraccount.executor.AccountWxDistributorQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.distributoraccount.AccountWxDistributorService;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:59
 */
@Service
@Slf4j
public class AccountWxDistributorServiceImpl implements AccountWxDistributorService {
    @Resource
    private AccountWxDistributorQryExc qryExc;

    @Resource
    private AccountWxDistributorCmdExc cmdExc;

    @Override
    public PageInfo<AccountWxDistributorDTO> listAccount(AccountQry qry) {
        return qryExc.listAccount(qry);
    }

    @Override
    public AccountWxDistributorDTO getAccountById(Integer id) {
        return qryExc.getAccountById(id);
    }

    /**
     * 根据分销商ID获取分销商服务商分账比例
     *
     * @param distributorId
     * @return
     */
    @Override
    public BigDecimal getSubAccountRatioByDistributor(Integer distributorId) {
        return qryExc.getSubAccountRatioByDistributor(distributorId);
    }

    @Override
    public AccountWxDistributorDTO getAccountByAppIdAndSubMchid(AccountAppId appId) {
        return qryExc.getAccountByAppIdAndSubMchid(appId);
    }

    @Override
    public boolean createAccount(AccountWxDistributorCreateCmd cmd) {
        return cmdExc.createAccount(cmd);
    }

    @Override
    public boolean updateAccount(AccountWxDistributorUpdateCmd cmd) {
        return cmdExc.updateAccount(cmd);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return cmdExc.deleteAccountById(id);
    }

    @Override
    public List<Integer> checkDistributor(DistributorIds ids) {
        return qryExc.checkDistributor(ids);
    }

    @Override
    public AccountWxDistributorDTO getAccountByDistributorId(String distributorId) {
        return qryExc.getAccountByDistributorId(Integer.valueOf(distributorId));
    }

}
