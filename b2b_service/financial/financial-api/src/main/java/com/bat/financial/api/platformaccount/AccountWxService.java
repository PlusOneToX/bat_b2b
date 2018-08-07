package com.bat.financial.api.platformaccount;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.AccountWxCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountWxUpdateCmd;
import com.bat.financial.api.platformaccount.dto.data.AccountWxDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 9:55
 */
public interface AccountWxService {
    PageInfo<AccountWxDTO> listAccount(AccountQry qry);

    boolean createAccount(AccountWxCreateCmd cmd);

    AccountWxDTO getAccount(Integer id);

    boolean updateAccount(AccountWxUpdateCmd cmd);

    boolean deleteAccountById(Integer id);
}
