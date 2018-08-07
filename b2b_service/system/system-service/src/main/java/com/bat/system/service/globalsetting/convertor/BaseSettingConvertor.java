package com.bat.system.service.globalsetting.convertor;

import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.dao.globalsetting.dataobject.BaseSettingDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class BaseSettingConvertor {

    // public static BaseSettingDO toBaseSettingDO(BaseSettingCreateCmd cmd) {
    // BaseSettingDO regionDO = new BaseSettingDO();
    // BeanUtils.copyProperties(cmd, regionDO);
    // return regionDO;
    // }
    //
    // public static BaseSettingDO toBaseSettingDO(BaseSettingUpdateCmd cmd) {
    // BaseSettingDO regionDO = new BaseSettingDO();
    // BeanUtils.copyProperties(cmd, regionDO);
    // return regionDO;
    // }

    public static BaseSettingDTO toBaseSettingDTO(BaseSettingDO baseSettingDO) {
        BaseSettingDTO baseSettingDTO = new BaseSettingDTO();
        BeanUtils.copyProperties(baseSettingDO, baseSettingDTO);
        return baseSettingDTO;
    }

    // public static List<BaseSettingDTO> toBaseSettingDTOList(List<BaseSettingDO> regionDOList) {
    // return regionDOList.stream().map(regionDO -> {
    // BaseSettingDTO regionDTO = new BaseSettingDTO();
    // BeanUtils.copyProperties(regionDO, regionDTO);
    // return regionDTO;
    // }).collect(Collectors.toList());
    // }
}
