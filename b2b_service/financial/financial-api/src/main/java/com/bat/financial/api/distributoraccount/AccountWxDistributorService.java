package com.bat.financial.api.distributoraccount;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.distributoraccount.dto.*;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;
import com.bat.financial.api.distributoraccount.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:54
 */
public interface AccountWxDistributorService {
    PageInfo<AccountWxDistributorDTO> listAccount(AccountQry qry);

    boolean createAccount(AccountWxDistributorCreateCmd cmd);

    AccountWxDistributorDTO getAccountByAppIdAndSubMchid(AccountAppId appId);

    boolean updateAccount(AccountWxDistributorUpdateCmd cmd);

    boolean deleteAccountById(Integer id);

    List<Integer> checkDistributor(DistributorIds ids);

    AccountWxDistributorDTO getAccountByDistributorId(String appId);

    AccountWxDistributorDTO getAccountById(Integer id);

    /**
     * 根据分销商ID获取分销商服务商分账比例
     * 
     * @param distributorId
     * @return
     */
    BigDecimal getSubAccountRatioByDistributor(Integer distributorId);
}
