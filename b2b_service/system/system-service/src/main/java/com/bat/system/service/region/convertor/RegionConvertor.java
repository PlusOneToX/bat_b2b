package com.bat.system.service.region.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.region.dto.RegionCreateCmd;
import com.bat.system.api.region.dto.RegionUpdateCmd;
import com.bat.system.api.region.dto.data.RegionComparisonDTO;
import com.bat.system.api.region.dto.data.RegionDTO;
import com.bat.system.dao.region.dataobject.RegionComparisonDO;
import com.bat.system.dao.region.dataobject.RegionDO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class RegionConvertor {

    public static RegionDO toRegionDO(RegionCreateCmd cmd) {
        if (cmd != null) {
            RegionDO regionDO = new RegionDO();
            BeanUtils.copyProperties(cmd, regionDO);
            return regionDO;
        }
        return null;
    }

    public static RegionDO toRegionDO(RegionUpdateCmd cmd) {
        if (cmd != null) {
            RegionDO regionDO = new RegionDO();
            BeanUtils.copyProperties(cmd, regionDO);
            return regionDO;
        }
        return null;
    }

    public static RegionDTO toRegionDTO(RegionDO regionDO) {
        if (regionDO != null) {
            RegionDTO regionDTO = new RegionDTO();
            BeanUtils.copyProperties(regionDO, regionDTO);
            return regionDTO;
        }
        return null;
    }

    public static RegionComparisonDTO toRegionComparisonDTO(RegionComparisonDO regionDO) {
        if (regionDO != null) {
            RegionComparisonDTO regionDTO = new RegionComparisonDTO();
            BeanUtils.copyProperties(regionDO, regionDTO);
            return regionDTO;
        }
        return null;
    }

    public static List<RegionComparisonDTO> toRegionComparisonDTOList(List<RegionComparisonDO> comparisonDOList) {
        if (!CollectionUtils.isEmpty(comparisonDOList)) {
            return comparisonDOList.stream().map(RegionConvertor::toRegionComparisonDTO).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static List<RegionDTO> toRegionDTOList(List<RegionDO> regionDOList) {
        if (!CollectionUtils.isEmpty(regionDOList)) {
            return regionDOList.stream().map(RegionConvertor::toRegionDTO).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
