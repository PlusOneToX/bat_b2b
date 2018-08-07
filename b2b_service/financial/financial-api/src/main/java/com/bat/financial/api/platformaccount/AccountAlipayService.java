package com.bat.financial.api.platformaccount;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.dto.AccountAlipayCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountAlipayUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountAlipayDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 9:55
 */
public interface AccountAlipayService {
    PageInfo<AccountAlipayDTO> listAccount(AccountQry qry);

    boolean createAccount(AccountAlipayCreateCmd cmd);

    AccountAlipayDTO getAccount(Integer id);

    boolean updateAccount(AccountAlipayUpdateCmd cmd);

    boolean deleteAccountById(Integer id);
}
