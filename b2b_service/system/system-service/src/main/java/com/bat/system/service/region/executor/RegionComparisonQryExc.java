package com.bat.system.service.region.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.region.dto.data.RegionComparisonDTO;
import com.bat.system.dao.region.RegionComparisonMapper;
import com.bat.system.dao.region.dataobject.RegionComparisonDO;
import com.bat.system.service.region.convertor.RegionConvertor;
import org.springframework.stereotype.Component;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/15 22:34
 */
@Component
public class RegionComparisonQryExc {

    @Resource
    private RegionComparisonMapper regionComparisonMapper;

    public List<RegionComparisonDTO> listRegionByParentIdAndAnotherName(Integer parentId, String anotherName) {
        List<RegionComparisonDO> regionComparisonDOS =
            regionComparisonMapper.listRegionByParentIdAndAnotherName(parentId, anotherName);
        return RegionConvertor.toRegionComparisonDTOList(regionComparisonDOS);
    }
}
