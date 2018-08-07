package com.bat.system.service.storesetting.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.storesetting.dto.MobileCreateCmd;
import com.bat.system.api.storesetting.dto.MobileUpdateCmd;
import com.bat.system.api.storesetting.dto.data.MobileDTO;
import com.bat.system.api.storesetting.dto.data.MobileItemDTO;
import com.bat.system.dao.storesetting.dataobject.MobileDO;
import com.bat.system.dao.storesetting.dataobject.MobileItemDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class MobileConvertor {

    public static MobileDO toMobileDO(MobileCreateCmd cmd) {
        MobileDO mobileDO = new MobileDO();
        BeanUtils.copyProperties(cmd, mobileDO);
        Date date = new Date();
        mobileDO.setCreateTime(date);
        mobileDO.setUpdateTime(date);
        return mobileDO;
    }

    public static MobileDO toMobileDO(MobileUpdateCmd cmd) {
        MobileDO mobileDO = new MobileDO();
        BeanUtils.copyProperties(cmd, mobileDO);
        Date date = new Date();
        mobileDO.setUpdateTime(date);
        return mobileDO;
    }

    public static MobileDTO toMobileDTO(MobileDO regionDO) {
        MobileDTO mobileDTO = new MobileDTO();
        BeanUtils.copyProperties(regionDO, mobileDTO);
        return mobileDTO;
    }

    public static List<MobileDTO> toMobileDTOList(List<MobileDO> mobileDOList) {
        return mobileDOList.stream().map(mobileDO -> {
            MobileDTO mobileDTO = new MobileDTO();
            BeanUtils.copyProperties(mobileDO, mobileDTO);
            return mobileDTO;
        }).collect(Collectors.toList());
    }

    public static List<MobileItemDTO> toMobileItemDTOList(List<MobileItemDO> mobileItemDOList) {
        return mobileItemDOList.stream().map(mobileItemDO -> {
            MobileItemDTO mobileItemDTO = new MobileItemDTO();
            BeanUtils.copyProperties(mobileItemDO, mobileItemDTO);
            return mobileItemDTO;
        }).collect(Collectors.toList());
    }
}
