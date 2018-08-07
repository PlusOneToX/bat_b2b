package com.bat.financial.platformaccount.executor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountOfflineDTO;
import com.bat.financial.dao.platformaccount.AccountOfflineMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountOfflineDO;
import com.bat.financial.platformaccount.convertor.AccountConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountOfflineQryExc {

    @Resource
    private AccountOfflineMapper accountOfflineMapper;

    public PageInfo<AccountOfflineDTO> listAccount(AccountQry qry) {
        BeanMap map = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<AccountOfflineDO> accountOfflineDOS = accountOfflineMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(accountOfflineDOS);
        List<AccountOfflineDTO> toCurrencyRateDTOList = AccountConvertor.toAccountOfflineDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyRateDTOList);
        return pageInfo;
    }

    public AccountOfflineDTO getAccount(Integer id) {
        AccountOfflineDO aDo = accountOfflineMapper.selectByPrimaryKey(id);
        return aDo == null ? null : AccountConvertor.toAccountOfflineDTO(aDo);
    }

    public AccountOfflineDTO getAccountByOrganization(Integer id) {
        AccountOfflineDO aDo = accountOfflineMapper.getAccountByOrganization(id);
        return aDo == null ? null : AccountConvertor.toAccountOfflineDTO(aDo);
    }
}
