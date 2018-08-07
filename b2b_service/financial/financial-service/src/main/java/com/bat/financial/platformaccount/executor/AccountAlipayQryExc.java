package com.bat.financial.platformaccount.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.api.SystemOrganizationServiceRpc;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountAlipayDTO;
import com.bat.financial.dao.platformaccount.AccountAlipayMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountAlipayDO;
import com.bat.financial.platformaccount.convertor.AccountConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class AccountAlipayQryExc {

    @Resource
    private AccountAlipayMapper accountAlipayMapper;

    @DubboReference(check = false, timeout = 30000)
    private SystemOrganizationServiceRpc organizationServiceRpc;

    public PageInfo<AccountAlipayDTO> listAccount(AccountQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<AccountAlipayDO> accountOfflineDOS = accountAlipayMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(accountOfflineDOS);
        List<AccountAlipayDTO> toCurrencyRateDTOList = AccountConvertor.toAccountAlipayDTOList(pageInfo.getList());
        List<AccountAlipayDTO> accountWxDTOS = fillOrgName(toCurrencyRateDTOList);
        pageInfo.setList(accountWxDTOS);
        return pageInfo;
    }

    /**
     * 根据组织id 填充组织名称
     * 
     * @param toCurrencyRateDTOList
     * @return
     */
    private List<AccountAlipayDTO> fillOrgName(List<AccountAlipayDTO> toCurrencyRateDTOList) {
        List<Integer> organizationIds =
            toCurrencyRateDTOList.stream().map(AccountAlipayDTO::getOrganizationId).collect(Collectors.toList());
        Response<List<OrganizationRpcDTO>> listResponse = null;
        try {
            listResponse = organizationServiceRpc.listOrganizationInfoByOrganizationIds(organizationIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listResponse != null && listResponse.isSuccess()) {
            List<OrganizationRpcDTO> data = listResponse.getData();
            OrganizationRpcDTO organizationRpcDTO1 = new OrganizationRpcDTO();
            organizationRpcDTO1.setName("unknown organization");
            return toCurrencyRateDTOList.stream().peek(accountAlipayDTO -> {
                OrganizationRpcDTO rpcDTO = data.stream()
                    .filter(
                        organizationRpcDTO -> organizationRpcDTO.getId().equals(accountAlipayDTO.getOrganizationId()))
                    .findFirst().orElse(organizationRpcDTO1);
                accountAlipayDTO.setOrganizationName(rpcDTO.getName());
            }).collect(Collectors.toList());
        } else {
            return toCurrencyRateDTOList;
        }
    }

    public AccountAlipayDTO getAccount(Integer id) {
        AccountAlipayDO aDo = accountAlipayMapper.selectByPrimaryKey(id);
        return aDo == null ? null : AccountConvertor.toAccountAlipayDTO(aDo);
    }

    public AccountAlipayDTO getAccountByOrganization(Integer id) {
        AccountAlipayDO aDo = accountAlipayMapper.getByOrganizationId(id);
        return aDo == null ? null : AccountConvertor.toAccountAlipayDTO(aDo);
    }
}
