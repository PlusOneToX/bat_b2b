package com.bat.system.web.region;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.region.RegionService;
import com.bat.system.api.region.dto.RegionId;
import com.bat.system.api.region.dto.RegionIds;
import com.bat.system.api.region.dto.RegionQry;
import com.bat.system.api.region.dto.data.RegionDTO;
import com.bat.system.api.region.dto.data.RegionInfoDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/12 12:15
 */
@Api(tags = "系统模块-区域后台接口", value = "AdminRegionController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/region")
public class AdminRegionController extends BaseController {

    @Resource
    private RegionService regionService;

    @GetMapping("/list")
    @ApiOperation(value = "查询区域列表(分页)")
    public Response<PageInfo<RegionDTO>> listRegion(@Valid RegionQry qry) {
        PageInfo<RegionDTO> pageInfo = regionService.listRegion(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/list/tree")
    @ApiOperation(value = "查询区域递归树形列表(不分页)(国家/省/市/区)")
    public Response<List<RegionInfoDTO>> listRegionTree() {
        return Response.of(regionService.listRegionTree());
    }

    @GetMapping("/list/tree/old")
    @ApiOperation(value = "查询区域递归树形列表(不分页)(中国/海外)")
    public Response<List<RegionInfoDTO>> listRegionTreeOld() {
        List<RegionInfoDTO> regionInfoDTOS;
        synchronized (AdminRegionController.class) {
            regionInfoDTOS = regionService.listRegionTreeOld();
        }
        return Response.of(regionInfoDTOS);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个区域")
    public Response<RegionDTO> getRegion(@Valid RegionId id) {
        return Response.of(regionService.getRegionById(id.getId()));
    }

    @GetMapping("/ids")
    @ApiOperation(value = "通过IDS查询多个区域(不分页)")
    public Response<List<RegionDTO>> getRegionByIds(@Valid RegionIds ids) {
        List<RegionDTO> collect =
            ids.getIds().stream().map(integer -> regionService.getRegionById(integer)).collect(Collectors.toList());
        return Response.of(collect);
    }
}
