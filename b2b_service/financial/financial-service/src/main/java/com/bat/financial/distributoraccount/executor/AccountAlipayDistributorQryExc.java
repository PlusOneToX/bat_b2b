package com.bat.financial.distributoraccount.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.distributoraccount.convertor.AccountDistributorConvertor;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.distributoraccount.dto.AccountQry;
import com.bat.financial.api.distributoraccount.dto.DistributorInfo;
import com.bat.financial.api.distributoraccount.dto.data.AccountAlipayDistributorDTO;
import com.bat.financial.dao.distributoraccount.AccountAlipayDistributorMapper;
import com.bat.financial.dao.distributoraccount.dataobject.AccountAlipayDistributorDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountAlipayDistributorQryExc {

    @Resource
    private AccountAlipayDistributorMapper accountAlipayDistributorMapper;

    public PageInfo<AccountAlipayDistributorDTO> listAccount(AccountQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<AccountAlipayDistributorDO> accountOfflineDOS = accountAlipayDistributorMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(accountOfflineDOS);
        List<AccountAlipayDistributorDTO> toCurrencyRateDTOList =
            AccountDistributorConvertor.toAccountAlipayDistributorDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyRateDTOList);
        return pageInfo;
    }

    public AccountAlipayDistributorDTO getAccount(String id) {
        List<AccountAlipayDistributorDO> accountAlipayDistributorDOS = accountAlipayDistributorMapper.listByAppId(id);
        if (CollectionUtils.isEmpty(accountAlipayDistributorDOS)) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NOT_EXISTS);
        }
        AccountAlipayDistributorDTO aDo = new AccountAlipayDistributorDTO();
        AccountAlipayDistributorDO aDo1 = accountAlipayDistributorDOS.get(0);
        BeanUtils.copyProperties(aDo1, aDo);
        List<DistributorInfo> distributorInfos =
            accountAlipayDistributorDOS.stream().map(accountAlipayDistributorDO -> {
                DistributorInfo distributorInfo = new DistributorInfo();
                distributorInfo.setDistributorId(accountAlipayDistributorDO.getDistributorId());
                distributorInfo.setDistributorName(accountAlipayDistributorDO.getDistributorName());
                distributorInfo.setDistributorCompanyName(accountAlipayDistributorDO.getDistributorCompanyName());
                return distributorInfo;
            }).collect(Collectors.toList());
        aDo.setDistributorInfos(distributorInfos);
        aDo.setDistributorId(null);
        aDo.setDistributorName(null);
        aDo.setDistributorCompanyName(null);
        return aDo;
    }

    public List<Integer> checkDistributor(List<Integer> ids) {
        return accountAlipayDistributorMapper.listByIds(ids).stream().map(AccountAlipayDistributorDO::getDistributorId)
            .collect(Collectors.toList());
    }
}
