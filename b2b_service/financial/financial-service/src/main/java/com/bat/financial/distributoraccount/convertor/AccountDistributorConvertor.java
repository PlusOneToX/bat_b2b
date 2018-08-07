package com.bat.financial.distributoraccount.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountAlipayDistributorUpdateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountWxDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountWxDistributorUpdateCmd;
import com.bat.financial.api.distributoraccount.dto.data.AccountAlipayDistributorDTO;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;
import com.bat.financial.dao.distributoraccount.dataobject.AccountAlipayDistributorDO;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 11:06
 */
public class AccountDistributorConvertor {
    public static List<AccountWxDistributorDTO> toAccountWxDistributorDTOList(List<AccountWxDistributorDO> list) {
        return list.stream().map(accountWxDistributorDO -> {
            AccountWxDistributorDTO dto = new AccountWxDistributorDTO();
            BeanUtils.copyProperties(accountWxDistributorDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static AccountWxDistributorDTO toAccountWxDistributorDTO(AccountWxDistributorDO aDo) {
        if (aDo != null) {
            AccountWxDistributorDTO dto = new AccountWxDistributorDTO();
            BeanUtils.copyProperties(aDo, dto);
            return dto;
        }
        return null;
    }

    public static List<AccountWxDistributorDO> toAccountWxDistributorDOList(AccountWxDistributorCreateCmd cmd) {
        return cmd.getDistributorInfos().stream().map(distributorInfo -> {
            AccountWxDistributorDO aDo = new AccountWxDistributorDO();
            BeanUtils.copyProperties(cmd, aDo);
            aDo.setDistributorId(distributorInfo.getDistributorId());
            aDo.setDistributorName(distributorInfo.getDistributorName());
            aDo.setDistributorCompanyName(distributorInfo.getDistributorCompanyName());
            Date date = new Date();
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            return aDo;
        }).collect(Collectors.toList());
    }

    public static List<AccountWxDistributorDO> toAccountWxDistributorDOList(AccountWxDistributorUpdateCmd cmd) {
        return cmd.getDistributorInfos().stream().map(distributorInfo -> {
            AccountWxDistributorDO aDo = new AccountWxDistributorDO();
            BeanUtils.copyProperties(cmd, aDo);
            aDo.setDistributorId(distributorInfo.getDistributorId());
            aDo.setDistributorName(distributorInfo.getDistributorName());
            aDo.setDistributorCompanyName(distributorInfo.getDistributorCompanyName());
            Date date = new Date();
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            return aDo;
        }).collect(Collectors.toList());

    }

    public static List<AccountAlipayDistributorDTO>
        toAccountAlipayDistributorDTOList(List<AccountAlipayDistributorDO> list) {
        return list.stream().map(accountAlipayDistributorDO -> {
            AccountAlipayDistributorDTO dto = new AccountAlipayDistributorDTO();
            BeanUtils.copyProperties(accountAlipayDistributorDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static AccountAlipayDistributorDTO toAccountAlipayDistributorDTO(AccountAlipayDistributorDO aDo) {
        AccountAlipayDistributorDTO dto = new AccountAlipayDistributorDTO();
        BeanUtils.copyProperties(aDo, dto);
        return dto;
    }

    public static List<AccountAlipayDistributorDO>
        toAccountAlipayDistributorDOList(AccountAlipayDistributorCreateCmd cmd) {
        return cmd.getDistributorInfos().stream().map(distributorInfo -> {
            AccountAlipayDistributorDO aDo = new AccountAlipayDistributorDO();
            BeanUtils.copyProperties(cmd, aDo);
            aDo.setDistributorId(distributorInfo.getDistributorId());
            aDo.setDistributorName(distributorInfo.getDistributorName());
            aDo.setDistributorCompanyName(distributorInfo.getDistributorCompanyName());
            Date date = new Date();
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            return aDo;
        }).collect(Collectors.toList());
    }

    public static List<AccountAlipayDistributorDO>
        toAccountAlipayDistributorDOList(AccountAlipayDistributorUpdateCmd cmd) {
        return cmd.getDistributorInfos().stream().map(distributorInfo -> {
            AccountAlipayDistributorDO aDo = new AccountAlipayDistributorDO();
            BeanUtils.copyProperties(cmd, aDo);
            aDo.setDistributorId(distributorInfo.getDistributorId());
            aDo.setDistributorName(distributorInfo.getDistributorName());
            aDo.setDistributorCompanyName(distributorInfo.getDistributorCompanyName());
            Date date = new Date();
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            return aDo;
        }).collect(Collectors.toList());
    }
}
