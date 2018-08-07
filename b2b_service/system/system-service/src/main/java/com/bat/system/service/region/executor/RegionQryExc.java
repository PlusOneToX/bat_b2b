package com.bat.system.service.region.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.region.dto.RegionQry;
import com.bat.system.api.region.dto.data.RegionDTO;
import com.bat.system.api.region.dto.data.RegionInfoDTO;
import com.bat.system.dao.region.RegionMapper;
import com.bat.system.dao.region.dataobject.RegionDO;
import com.bat.system.service.region.convertor.RegionConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class RegionQryExc {
    @Resource
    private RegionMapper regionMapper;

    public PageInfo<RegionDTO> listRegion(RegionQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<RegionDO> regionDOList = regionMapper.listByParentId(qry.getParentId());
        PageInfo pageInfo = new PageInfo(regionDOList);
        List<RegionDTO> toRegionDTOList = RegionConvertor.toRegionDTOList(pageInfo.getList());
        if (qry.getParentId() == 0) {
            // 查询国家 时中国排第一个（特殊）
            // 中国
            RegionDO regionDO = regionMapper.selectByPrimaryKey(37);
            boolean b = toRegionDTOList.removeIf(regionDTO -> regionDTO.getId() == 37);
            if (!b) {
                toRegionDTOList.remove(0);
            }
            toRegionDTOList.add(0, RegionConvertor.toRegionDTO(regionDO));

        }
        pageInfo.setList(toRegionDTOList);
        return pageInfo;
    }

    public RegionDTO getRegionById(Integer id) {
        RegionDO regionDO = regionMapper.selectByPrimaryKey(id);
        return regionDO == null ? null : RegionConvertor.toRegionDTO(regionDO);
    }

    public List<RegionInfoDTO> listRegionTree(Integer parentId) {
        List<RegionInfoDTO> regionInfoDTO = new ArrayList<>();
        List<RegionDO> regionDOS = regionMapper.listByParentId(parentId);
        if (regionDOS != null && regionDOS.size() != 0) {
            regionDOS.forEach(regionDO -> {
                RegionInfoDTO dto = new RegionInfoDTO();
                dto.setId(regionDO.getId());
                dto.setRegionName(regionDO.getRegionName());
                dto.setRegionNameEn(regionDO.getRegionNameEn());
                List<RegionInfoDTO> regionInfoDTOS = listRegionTree(dto.getId());
                dto.setSonRegions(regionInfoDTOS);
                regionInfoDTO.add(dto);
            });
        }
        return regionInfoDTO;
    }

    /**
     * 从父节点 到几级 level
     * 
     * @param parentId
     * @param level
     * @return
     */
    public List<RegionInfoDTO> listRegionTree(Integer parentId, Short level) {
        List<RegionInfoDTO> regionInfoDTO = new ArrayList<>();
        List<RegionDO> regionDOS = regionMapper.listByParentId(parentId);
        if (regionDOS != null && regionDOS.size() != 0) {
            regionDOS.forEach(regionDO -> {
                RegionInfoDTO dto = new RegionInfoDTO();
                dto.setId(regionDO.getId());
                dto.setRegionName(regionDO.getRegionName());
                dto.setRegionNameEn(regionDO.getRegionNameEn());
                if (regionDO.getLevel() <= level) {
                    List<RegionInfoDTO> regionInfoDTOS = listRegionTree(dto.getId(), level);
                    dto.setSonRegions(regionInfoDTOS);
                }
                regionInfoDTO.add(dto);
            });
        }
        return regionInfoDTO;
    }

    public List<RegionDTO> listRegionByParentIdAndRegionName(Integer parentId, String regionName) {
        List<RegionDO> regionDOS = regionMapper.listByParentIdAndRegionName(parentId, regionName);
        return RegionConvertor.toRegionDTOList(regionDOS);
    }

    public List<RegionDTO> listRegionByLevelAndRegionName(Short level, String regionName) {
        List<RegionDO> regionDOS = regionMapper.listRegionByLevelAndRegionName(level, regionName);
        return RegionConvertor.toRegionDTOList(regionDOS);
    }
}
