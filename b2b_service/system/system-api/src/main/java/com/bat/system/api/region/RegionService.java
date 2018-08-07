package com.bat.system.api.region;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.region.dto.RegionQry;
import com.bat.system.api.region.dto.data.RegionDTO;
import com.bat.system.api.region.dto.data.RegionInfoDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface RegionService {

    /**
     * 根据ID获取区域
     *
     * @param id
     * @return
     */
    RegionDTO getRegionById(Integer id);

    /**
     * 获取所有区域（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<RegionDTO> listRegion(RegionQry qry);

    /**
     * 递归获取所有信息（国家/省/市/区）
     * 
     * @return
     */
    List<RegionInfoDTO> listRegionTree();

    /**
     * 兼容旧版（中国（默认展开）/海外）
     * 
     * @return
     */
    List<RegionInfoDTO> listRegionTreeOld();

}
