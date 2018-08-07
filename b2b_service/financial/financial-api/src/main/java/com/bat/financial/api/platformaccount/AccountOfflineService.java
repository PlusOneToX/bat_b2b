package com.bat.financial.api.platformaccount;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.dto.AccountOfflineCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountOfflineUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountOfflineDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 9:55
 */
public interface AccountOfflineService {
    PageInfo<AccountOfflineDTO> listAccount(AccountQry qry);

    boolean createAccount(AccountOfflineCreateCmd cmd);

    AccountOfflineDTO getAccount(Integer id);

    boolean updateAccount(AccountOfflineUpdateCmd cmd);

    boolean deleteAccountById(Integer id);
}
