package com.bat.financial.distributoraccount.executor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.distributoraccount.convertor.AccountDistributorConvertor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.distributoraccount.dto.AccountAppId;
import com.bat.financial.api.distributoraccount.dto.AccountQry;
import com.bat.financial.api.distributoraccount.dto.DistributorIds;
import com.bat.financial.api.distributoraccount.dto.DistributorInfo;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;
import com.bat.financial.common.constant.account.DistributorAccountConstant;
import com.bat.financial.dao.distributoraccount.AccountWxDistributorMapper;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountWxDistributorQryExc {

    @Resource
    private AccountWxDistributorMapper accountWxDistributorMapper;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    public PageInfo<AccountWxDistributorDTO> listAccount(AccountQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<AccountWxDistributorDO> accountOfflineDOS = accountWxDistributorMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(accountOfflineDOS);
        List<AccountWxDistributorDTO> toCurrencyRateDTOList =
            AccountDistributorConvertor.toAccountWxDistributorDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyRateDTOList);
        return pageInfo;
    }

    public AccountWxDistributorDTO getAccountByAppIdAndSubMchid(AccountAppId appId) {
        List<AccountWxDistributorDO> accountWxDistributorDOS =
            accountWxDistributorMapper.listByAppIdAndSubMchid(appId.getAppId(), appId.getSubMchid());
        if (CollectionUtils.isEmpty(accountWxDistributorDOS)) {
            throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NOT_EXISTS);
        }
        AccountWxDistributorDTO aDo = new AccountWxDistributorDTO();
        AccountWxDistributorDO aDo1 = accountWxDistributorDOS.get(0);
        BeanUtils.copyProperties(aDo1, aDo);
        List<DistributorInfo> distributorInfos = accountWxDistributorDOS.stream().map(accountAlipayDistributorDO -> {
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
        // 百分比
        if (aDo.getSubAccountRatio() != null) {
            aDo.setSubAccountRatio(
                aDo.getSubAccountRatio().multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return aDo;
    }

    public List<Integer> checkDistributor(DistributorIds ids) {
        return accountWxDistributorMapper.listByIdsAndApptype(ids.getIds(), ids.getAppType()).stream()
            .map(AccountWxDistributorDO::getDistributorId).collect(Collectors.toList());
    }

    public AccountWxDistributorDTO getAccountByDistributorId(Integer distributorId) {
        AccountWxDistributorDO aDo = accountWxDistributorMapper.getByDistributorId(distributorId);
        return AccountDistributorConvertor.toAccountWxDistributorDTO(aDo);
    }

    public AccountWxDistributorDTO getByDistributorIdAndAppType(Integer distributorId, Short appType) {
        AccountWxDistributorDO aDo = accountWxDistributorMapper.getByDistributorIdAndAppType(distributorId, appType);
        return AccountDistributorConvertor.toAccountWxDistributorDTO(aDo);
    }

    public AccountWxDistributorDTO getAccountById(Integer id) {
        AccountWxDistributorDO accountWxDistributorDO = accountWxDistributorMapper.selectByPrimaryKey(id);
        AccountAppId accountAppId = new AccountAppId();
        accountAppId.setAppId(accountWxDistributorDO.getAppId());
        return getAccountByAppIdAndSubMchid(accountAppId);
    }

    public List<AccountWxDistributorDO> listByCondition(Integer distributorId, Short appType, String appId,
        Short accountType) {
        return accountWxDistributorMapper.listByCondition(distributorId, appType, appId, accountType);
    }

    public BigDecimal getSubAccountRatioByDistributor(Integer distributorId) {
        List<AccountWxDistributorDO> accountWxDistributorDOList =
            accountWxDistributorMapper.listByCondition(distributorId, null, null,
                DistributorAccountConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE_PROVIDER_SECOND);
        if (accountWxDistributorDOList == null || accountWxDistributorDOList.size() == 0) {
            return null;
        }
        return accountWxDistributorDOList.get(0).getSubAccountRatio();
    }
}
