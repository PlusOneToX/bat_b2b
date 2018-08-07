package com.bat.financial.platformaccount.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.financial.api.platformaccount.dto.*;
import org.springframework.beans.BeanUtils;

import com.bat.financial.api.platformaccount.dto.data.AccountAlipayDTO;
import com.bat.financial.api.platformaccount.dto.data.AccountKuaiQianDTO;
import com.bat.financial.api.platformaccount.dto.data.AccountOfflineDTO;
import com.bat.financial.api.platformaccount.dto.data.AccountWxDTO;
import com.bat.financial.dao.platformaccount.dataobject.AccountAlipayDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountKuaiQianDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountOfflineDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 11:06
 */
public class AccountConvertor {
    public static List<AccountWxDTO> toAccountWxDTOList(List<AccountWxDO> list) {
        return list.stream().map(accountWxDO -> {
            AccountWxDTO dto = new AccountWxDTO();
            BeanUtils.copyProperties(accountWxDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static AccountWxDTO toAccountWxDTO(AccountWxDO aDo) {
        AccountWxDTO dto = new AccountWxDTO();
        BeanUtils.copyProperties(aDo, dto);
        return dto;
    }

    public static AccountWxDO toAccountWxDO(AccountWxCreateCmd cmd) {
        AccountWxDO aDo = new AccountWxDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static AccountWxDO toAccountWxDO(AccountWxUpdateCmd cmd) {
        AccountWxDO aDo = new AccountWxDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static List<AccountAlipayDTO> toAccountAlipayDTOList(List<AccountAlipayDO> list) {
        return list.stream().map(accountAlipayDO -> {
            AccountAlipayDTO dto = new AccountAlipayDTO();
            BeanUtils.copyProperties(accountAlipayDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static AccountAlipayDTO toAccountAlipayDTO(AccountAlipayDO aDo) {
        AccountAlipayDTO dto = new AccountAlipayDTO();
        BeanUtils.copyProperties(aDo, dto);
        return dto;
    }

    public static AccountAlipayDO toAccountAlipayDO(AccountAlipayCreateCmd cmd) {
        AccountAlipayDO aDo = new AccountAlipayDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static AccountAlipayDO toAccountAlipayDO(AccountAlipayUpdateCmd cmd) {
        AccountAlipayDO aDo = new AccountAlipayDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static List<AccountKuaiQianDTO> toAccountKuaiQianDTOList(List<AccountKuaiQianDO> list) {
        return list.stream().map(accountKuaiQianDO -> {
            AccountKuaiQianDTO dto = new AccountKuaiQianDTO();
            BeanUtils.copyProperties(accountKuaiQianDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static AccountKuaiQianDTO toAccountKuaiQianDTO(AccountKuaiQianDO aDo) {
        AccountKuaiQianDTO dto = new AccountKuaiQianDTO();
        BeanUtils.copyProperties(aDo, dto);
        return dto;
    }

    public static AccountKuaiQianDO toAccountKuaiQianDO(AccountKuaiQianCreateCmd cmd) {
        AccountKuaiQianDO aDo = new AccountKuaiQianDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static AccountKuaiQianDO toAccountKuaiQianDO(AccountKuaiQianUpdateCmd cmd) {
        AccountKuaiQianDO aDo = new AccountKuaiQianDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static AccountOfflineDO toAccountOfflineDO(AccountOfflineCreateCmd cmd) {
        AccountOfflineDO aDo = new AccountOfflineDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static AccountOfflineDO toAccountOfflineDO(AccountOfflineUpdateCmd cmd) {
        AccountOfflineDO aDo = new AccountOfflineDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static List<AccountOfflineDTO> toAccountOfflineDTOList(List<AccountOfflineDO> list) {
        return list.stream().map(accountOfflineDO -> {
            AccountOfflineDTO aDo = new AccountOfflineDTO();
            BeanUtils.copyProperties(accountOfflineDO, aDo);
            return aDo;
        }).collect(Collectors.toList());
    }

    public static AccountOfflineDTO toAccountOfflineDTO(AccountOfflineDO aDo) {
        AccountOfflineDTO offlineDTO = new AccountOfflineDTO();
        BeanUtils.copyProperties(aDo, offlineDTO);
        return offlineDTO;
    }
}
