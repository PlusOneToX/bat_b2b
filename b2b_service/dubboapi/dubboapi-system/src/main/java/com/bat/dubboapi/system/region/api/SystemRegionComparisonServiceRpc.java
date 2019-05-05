package com.bat.dubboapi.system.region.api;

import java.util.List;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.region.dto.data.RegionComparisonRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/19 19:47
 */
public interface SystemRegionComparisonServiceRpc {

    /**
     * 根据父id 以及名称进行模糊查询
     * 
     * @param parentId
     * @param regionName
     * @return
     */
    Response<List<RegionComparisonRpcDTO>> listRegionByParentIdAndAnotherName(Integer parentId, String regionName);
}
