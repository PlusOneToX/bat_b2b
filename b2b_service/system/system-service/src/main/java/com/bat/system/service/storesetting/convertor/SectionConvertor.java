package com.bat.system.service.storesetting.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.storesetting.dto.SectionCreateCmd;
import com.bat.system.api.storesetting.dto.SectionUpdateCmd;
import com.bat.system.api.storesetting.dto.data.SectionDTO;
import com.bat.system.dao.storesetting.dataobject.SectionDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class SectionConvertor {

    public static SectionDO toSectionDO(SectionCreateCmd cmd) {
        SectionDO sectionDO = new SectionDO();
        BeanUtils.copyProperties(cmd, sectionDO);
        Date date = new Date();
        sectionDO.setCreateTime(date);
        sectionDO.setUpdateTime(date);
        return sectionDO;
    }

    public static SectionDO toSectionDO(SectionUpdateCmd cmd) {
        SectionDO sectionDO = new SectionDO();
        BeanUtils.copyProperties(cmd, sectionDO);
        Date date = new Date();
        sectionDO.setUpdateTime(date);
        return sectionDO;
    }

    public static SectionDTO toSectionDTO(SectionDO regionDO) {
        SectionDTO sectionDTO = new SectionDTO();
        BeanUtils.copyProperties(regionDO, sectionDTO);
        return sectionDTO;
    }

    public static List<SectionDTO> toSectionDTOList(List<SectionDO> sectionDOList) {
        return sectionDOList.stream().map(sectionDO -> {
            SectionDTO sectionDTO = new SectionDTO();
            BeanUtils.copyProperties(sectionDO, sectionDTO);
            return sectionDTO;
        }).collect(Collectors.toList());
    }
}
