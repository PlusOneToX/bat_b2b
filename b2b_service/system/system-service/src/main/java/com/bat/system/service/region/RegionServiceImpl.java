package com.bat.system.service.region;

import static com.bat.system.service.common.CommonConstant.OVERSEAS_NAME;
import static com.bat.system.service.common.CommonConstant.OVERSEAS_NAME_EN;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.region.RegionService;
import com.bat.system.api.region.dto.RegionQry;
import com.bat.system.api.region.dto.data.RegionDTO;
import com.bat.system.api.region.dto.data.RegionInfoDTO;
import com.bat.system.service.region.executor.RegionQryExc;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionQryExc regionQryExc;

    @Override
    public RegionDTO getRegionById(Integer id) {
        return regionQryExc.getRegionById(id);
    }

    @Override
    public PageInfo<RegionDTO> listRegion(RegionQry qry) {
        return regionQryExc.listRegion(qry);
    }

    @Override
    @Cached(name = "system:region:RegionServiceImpl.listRegionTree", key = "1", cacheType = CacheType.REMOTE)
    public List<RegionInfoDTO> listRegionTree() {
        long start = System.currentTimeMillis();
        log.info("RegionServiceImpl.listRegionTree start");
        List<RegionInfoDTO> regionInfoDTOS = regionQryExc.listRegionTree(0);
        long end = System.currentTimeMillis();
        log.info("RegionServiceImpl.listRegionTree end,cost time:{}", end - start);
        return regionInfoDTOS;
    }

    @Override
    @Cached(name = "system:region:RegionServiceImpl.listRegionTreeOld", key = "1", cacheType = CacheType.REMOTE)
    public List<RegionInfoDTO> listRegionTreeOld() {
        long start = System.currentTimeMillis();
        log.info("RegionServiceImpl.listRegionTreeOld start");
        List<RegionInfoDTO> regionInfoDTOS = regionQryExc.listRegionTree(0, (short)2);
        RegionInfoDTO dto =
            regionInfoDTOS.stream().filter(regionInfoDTO -> regionInfoDTO.getId().equals(37)).findFirst().get();
        List<RegionInfoDTO> sonRegions = dto.getSonRegions();
        regionInfoDTOS.removeIf(regionInfoDTO -> regionInfoDTO.getId().equals(37));
        RegionInfoDTO dto1 = new RegionInfoDTO();
        dto1.setId(0);
        dto1.setRegionName(OVERSEAS_NAME);
        dto1.setRegionNameEn(OVERSEAS_NAME_EN);
        dto1.setSonRegions(regionInfoDTOS);
        sonRegions.add(dto1);
        long end = System.currentTimeMillis();
        log.info("RegionServiceImpl.listRegionTreeOld end,cost time:{}", end - start);
        return sonRegions;
    }
}