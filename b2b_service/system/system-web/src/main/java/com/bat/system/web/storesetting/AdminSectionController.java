package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.dto.*;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.system.web.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.SectionService;
import com.bat.system.api.storesetting.dto.*;
import com.bat.system.api.storesetting.dto.data.SectionDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页板块
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页板块后台接口", value = "AdminSectionController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/section")
public class AdminSectionController extends BaseController {

    @Resource
    private SectionService sectionService;

    @GetMapping("/list")
    @ApiOperation(value = "查询板块列表(分页)")
    public Response<PageInfo<SectionDTO>> listSection(@Valid SectionQry qry) {
        return Response.of(sectionService.listSection(qry));
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个板块")
    public Response<SectionDTO> getSection(@Valid SectionId id) {
        return Response.of(sectionService.getSectionById(id.getId()));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSection, value = CommonLogTypeConstantDTO.AdminSectionAdd)
    @PostMapping()
    @ApiOperation(value = "添加板块")
    public Response createSection(@RequestBody @Valid SectionCreateCmd cmd) {
        sectionService.createSection(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSection, value = CommonLogTypeConstantDTO.AdminSectionUpdate)
    @PutMapping()
    @ApiOperation(value = "更新板块")
    public Response updateSection(@RequestBody @Valid SectionUpdateCmd cmd) {
        sectionService.updateSection(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSection, value = CommonLogTypeConstantDTO.AdminSectionRelease)
    @PutMapping("/release")
    @ApiOperation(value = "发布板块")
    public Response releaseSection(@RequestBody @Valid SectionReleaseCmd cmd) {
        sectionService.releaseSection(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSection, value = CommonLogTypeConstantDTO.AdminSectionDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除板块")
    public Response deleteSection(@RequestBody @Valid SectionId id) {
        sectionService.deleteSectionById(id.getId());
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSection, value = CommonLogTypeConstantDTO.AdminSectionDeleteByIds)
    @DeleteMapping("/ids")
    @ApiOperation(value = "通过IDS删除板块")
    public Response deleteSectionByIds(@RequestBody @Valid SectionIds ids) {
        ids.getIds().forEach(integer -> sectionService.deleteSectionById(integer));
        return Response.buildSuccess();
    }
}
