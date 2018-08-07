package com.bat.system.service.globalsetting.convertor;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.globalsetting.dto.AgreementSettingCreateCmd;
import com.bat.system.api.globalsetting.dto.AgreementSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.AgreementSettingDTO;
import com.bat.system.dao.globalsetting.dataobject.AgreementSettingDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class AgreementSettingConvertor {

    public static AgreementSettingDO toAgreementSettingDO(AgreementSettingCreateCmd cmd) {
        if (cmd != null) {
            AgreementSettingDO regionDO = new AgreementSettingDO();
            BeanUtils.copyProperties(cmd, regionDO);
            return regionDO;
        }
        return null;
    }

    public static AgreementSettingDO toAgreementSettingDO(AgreementSettingUpdateCmd cmd) {
        if (cmd != null) {
            AgreementSettingDO regionDO = new AgreementSettingDO();
            BeanUtils.copyProperties(cmd, regionDO);
            return regionDO;
        }
        return null;
    }

    public static AgreementSettingDTO toAgreementSettingDTO(AgreementSettingDO agreementSettingDO) {
        if (agreementSettingDO != null) {
            AgreementSettingDTO agreementSettingDTO = new AgreementSettingDTO();
            BeanUtils.copyProperties(agreementSettingDO, agreementSettingDTO);
            return agreementSettingDTO;
        }
        return null;
    }

    public static List<AgreementSettingDTO> toAgreementSettingDTOList(List<AgreementSettingDO> regionDOList) {
        return regionDOList.stream().map(AgreementSettingConvertor::toAgreementSettingDTO).collect(Collectors.toList());
    }
}