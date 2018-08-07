package com.bat.financial.api.platformaccount;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountKuaiQianDTO;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianCreateCmd;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 9:55
 */
public interface AccountKuaiQianService {
    PageInfo<AccountKuaiQianDTO> listAccount(AccountQry qry);

    boolean createAccount(AccountKuaiQianCreateCmd cmd);

    AccountKuaiQianDTO getAccount(Integer id);

    boolean updateAccount(AccountKuaiQianUpdateCmd cmd);

    boolean deleteAccountById(Integer id);
}
