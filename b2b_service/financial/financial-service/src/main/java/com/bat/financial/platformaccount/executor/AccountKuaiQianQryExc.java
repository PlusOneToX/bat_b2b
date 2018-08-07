package com.bat.financial.platformaccount.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.platformaccount.convertor.AccountConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountKuaiQianDTO;
import com.bat.financial.dao.platformaccount.AccountKuaiQianMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountKuaiQianDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountKuaiQianQryExc {

    @Resource
    private AccountKuaiQianMapper accountKuaiQianMapper;

    public PageInfo<AccountKuaiQianDTO> listAccount(AccountQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<AccountKuaiQianDO> accountOfflineDOS = accountKuaiQianMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(accountOfflineDOS);
        List<AccountKuaiQianDTO> toCurrencyRateDTOList = AccountConvertor.toAccountKuaiQianDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyRateDTOList);
        return pageInfo;
    }

    public AccountKuaiQianDTO getAccount(Integer id) {
        AccountKuaiQianDO aDo = accountKuaiQianMapper.selectByPrimaryKey(id);
        return aDo == null ? null : AccountConvertor.toAccountKuaiQianDTO(aDo);
    }

    public AccountKuaiQianDTO getAccountByOrganization(Integer id) {
        AccountKuaiQianDO aDo = accountKuaiQianMapper.getByOrganizationId(id);
        return aDo == null ? null : AccountConvertor.toAccountKuaiQianDTO(aDo);
    }
}
