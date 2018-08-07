package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.SectionService;
import com.bat.system.api.storesetting.dto.SectionId;
import com.bat.system.api.storesetting.dto.SectionQry;
import com.bat.system.api.storesetting.dto.data.SectionDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页板块
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页板块前台接口", value = "UserSectionController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/section")
public class UserSectionController extends BaseController {

    @Resource
    private SectionService sectionService;

    @GetMapping("/list")
    @ApiOperation(value = "查询板块列表(不分页)")
    public Response<PageInfo<SectionDTO>> listSection(@Valid SectionQry qry) {
        return Response.of(sectionService.listSection(qry));
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个板块")
    public Response<SectionDTO> getSection(@Valid SectionId id) {
        return Response.of(sectionService.getSectionById(id.getId()));
    }
}
