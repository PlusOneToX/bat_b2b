package com.bat.dubboapi.system.region.api;

import java.util.List;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/19 19:47
 */
public interface SystemRegionServiceRpc {
    /**
     * 根据区域id 获取区域信息
     * 
     * @param id
     * @return
     */
    Response<RegionRpcDTO> getRegionById(Integer id);

    /**
     * 根据父id 以及名称进行模糊查询
     * 
     * @param parentId
     * @param regionName
     * @return
     */
    Response<List<RegionRpcDTO>> listRegionByParentIdAndRegionName(Integer parentId, String regionName);

    /**
     * 根据区域级数 以及名称进行模糊查询
     * @param level
     * @param regionName
     * @return
     */
    Response<List<RegionRpcDTO>> listRegionByLevelAndRegionName(Short level, String regionName);
}
