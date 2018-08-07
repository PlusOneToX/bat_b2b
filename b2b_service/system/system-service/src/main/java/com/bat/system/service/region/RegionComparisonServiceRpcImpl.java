package com.bat.system.service.region;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.region.dto.data.RegionComparisonDTO;
import com.bat.system.service.region.executor.RegionComparisonQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.region.api.SystemRegionComparisonServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionComparisonRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/15 22:32
 */
@DubboService
@Slf4j
public class RegionComparisonServiceRpcImpl implements SystemRegionComparisonServiceRpc {

    @Resource
    private RegionComparisonQryExc regionComparisonQryExc;

    @Override
    public Response<List<RegionComparisonRpcDTO>> listRegionByParentIdAndAnotherName(Integer parentId,
        String regionName) {
        List<RegionComparisonDTO> regionComparisonDTOS =
            regionComparisonQryExc.listRegionByParentIdAndAnotherName(parentId, regionName);
        List<RegionComparisonRpcDTO> collect = regionComparisonDTOS.stream().map(regionComparisonDTO -> {
            RegionComparisonRpcDTO dto = new RegionComparisonRpcDTO();
            BeanUtils.copyProperties(regionComparisonDTO, dto);
            return dto;
        }).collect(Collectors.toList());
        return Response.of(collect);
    }
}
