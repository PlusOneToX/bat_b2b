package com.bat.system.service.globalsetting.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.globalsetting.dto.FactorySettingDelayPushesUpdateCmd;
import com.bat.system.api.globalsetting.dto.FactorySettingOrderInvalidUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.FactorySettingDelayPushesDTO;
import com.bat.system.api.globalsetting.dto.data.FactorySettingOrderInvalidDTO;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingDelayPushDO;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingOrderInvalidDO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class FactorySettingConvertor {

    public static List<FactorySettingDelayPushesDTO>
        toFactorySettingDelayPushesDTOList(List<FactorySettingDelayPushDO> factorySettingDelayPushDOS) {
        List<FactorySettingDelayPushesDTO> dtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(factorySettingDelayPushDOS)){
            dtos = factorySettingDelayPushDOS.stream().map(aDo -> {
                FactorySettingDelayPushesDTO dto = new FactorySettingDelayPushesDTO();
                BeanUtils.copyProperties(aDo, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return dtos;
    }

    public static FactorySettingDelayPushDO toFactorySettingDelayPushesDO(FactorySettingDelayPushesUpdateCmd cmd) {
        FactorySettingDelayPushDO factorySettingDelayPushDO = new FactorySettingDelayPushDO();
        BeanUtils.copyProperties(cmd, factorySettingDelayPushDO);
        return factorySettingDelayPushDO;
    }

    public static List<FactorySettingOrderInvalidDTO>
        toFactorySettingOrderInvalidDTOList(List<FactorySettingOrderInvalidDO> factorySettingOrderInvalidDOS) {
        List<FactorySettingOrderInvalidDTO> dtos =  new ArrayList<>();
        if(!CollectionUtils.isEmpty(factorySettingOrderInvalidDOS)){
            dtos = factorySettingOrderInvalidDOS.stream().map(aDo -> {
                FactorySettingOrderInvalidDTO dto = new FactorySettingOrderInvalidDTO();
                BeanUtils.copyProperties(aDo, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return dtos;
    }

    public static FactorySettingOrderInvalidDTO
        toFactorySettingOrderInvalidDTO(FactorySettingOrderInvalidDO factorySettingOrderInvalidDO) {
        FactorySettingOrderInvalidDTO factorySettingOrderInvalidDTO = new FactorySettingOrderInvalidDTO();
        if(factorySettingOrderInvalidDO != null) {
            BeanUtils.copyProperties(factorySettingOrderInvalidDO, factorySettingOrderInvalidDTO);
        }
        return factorySettingOrderInvalidDTO;
    }

    public static FactorySettingOrderInvalidDO toFactorySettingOrderInvalidDO(FactorySettingOrderInvalidUpdateCmd cmd) {
        FactorySettingOrderInvalidDO factorySettingOrderInvalidDO = new FactorySettingOrderInvalidDO();
        BeanUtils.copyProperties(cmd, factorySettingOrderInvalidDO);
        return factorySettingOrderInvalidDO;
    }

}
