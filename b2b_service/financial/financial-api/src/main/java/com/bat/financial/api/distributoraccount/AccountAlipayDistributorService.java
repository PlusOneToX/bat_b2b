package com.bat.financial.api.distributoraccount;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorUpdateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountQry;
import com.bat.financial.api.distributoraccount.dto.data.AccountAlipayDistributorDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:54
 */
public interface AccountAlipayDistributorService {
    PageInfo<AccountAlipayDistributorDTO> listAccount(AccountQry qry);

    boolean createAccount(AccountAlipayDistributorCreateCmd cmd);

    AccountAlipayDistributorDTO getAccount(String appId);

    boolean updateAccount(AccountAlipayDistributorUpdateCmd cmd);

    boolean deleteAccountById(Integer id);

    List<Integer> checkDistributor(List<Integer> ids);
}
