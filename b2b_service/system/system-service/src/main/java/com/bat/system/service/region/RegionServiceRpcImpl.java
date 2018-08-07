package com.bat.system.service.region;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.MessageUtils;
import com.bat.system.api.region.dto.data.RegionDTO;
import com.bat.system.service.region.executor.ErrorCode;
import com.bat.system.service.region.executor.RegionQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@DubboService
@Slf4j
public class RegionServiceRpcImpl implements SystemRegionServiceRpc {

    @Resource
    private RegionQryExc regionQryExc;

    @Override
    public Response<RegionRpcDTO> getRegionById(Integer id) {
        RegionDTO regionById = regionQryExc.getRegionById(id);
        if (regionById != null) {
            RegionRpcDTO dto = new RegionRpcDTO();
            BeanUtils.copyProperties(regionById, dto);
            return Response.of(dto);
        }
        return Response.buildFailure(ErrorCode.B_REGION_NOT_EXISTS, MessageUtils.get(ErrorCode.B_REGION_NOT_EXISTS));
    }

    @Override
    public Response<List<RegionRpcDTO>> listRegionByParentIdAndRegionName(Integer parentId, String regionName) {
        List<RegionDTO> regionDTOS = regionQryExc.listRegionByParentIdAndRegionName(parentId, regionName);
        List<RegionRpcDTO> collect = regionDTOS.stream().map(regionDTO -> {
            RegionRpcDTO dto = new RegionRpcDTO();
            BeanUtils.copyProperties(regionDTO, dto);
            return dto;
        }).collect(Collectors.toList());
        return Response.of(collect);
    }

    @Override
    public Response<List<RegionRpcDTO>> listRegionByLevelAndRegionName(Short level, String regionName) {
        List<RegionDTO> regionDTOS = regionQryExc.listRegionByLevelAndRegionName(level, regionName);
        List<RegionRpcDTO> collect = regionDTOS.stream().map(regionDTO -> {
            RegionRpcDTO dto = new RegionRpcDTO();
            BeanUtils.copyProperties(regionDTO, dto);
            return dto;
        }).collect(Collectors.toList());
        return Response.of(collect);
    }

}