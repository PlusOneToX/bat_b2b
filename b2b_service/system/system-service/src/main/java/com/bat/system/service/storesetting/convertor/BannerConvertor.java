package com.bat.system.service.storesetting.convertor;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.storesetting.dto.BannerCreateCmd;
import com.bat.system.api.storesetting.dto.BannerUpdateCmd;
import com.bat.system.api.storesetting.dto.data.BannerDTO;
import com.bat.system.dao.storesetting.dataobject.BannerDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class BannerConvertor {

    public static BannerDO toBannerDO(BannerCreateCmd cmd) {
        BannerDO regionDO = new BannerDO();
        BeanUtils.copyProperties(cmd, regionDO);
        return regionDO;
    }

    public static BannerDO toBannerDO(BannerUpdateCmd cmd) {
        BannerDO regionDO = new BannerDO();
        BeanUtils.copyProperties(cmd, regionDO);
        return regionDO;
    }

    public static BannerDTO toBannerDTO(BannerDO regionDO) {
        BannerDTO regionDTO = new BannerDTO();
        BeanUtils.copyProperties(regionDO, regionDTO);
        return regionDTO;
    }

    public static List<BannerDTO> toBannerDTOList(List<BannerDO> regionDOList) {
        return regionDOList.stream().map(regionDO -> {
            BannerDTO regionDTO = new BannerDTO();
            BeanUtils.copyProperties(regionDO, regionDTO);
            return regionDTO;
        }).collect(Collectors.toList());
    }
}
