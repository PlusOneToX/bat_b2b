package com.bat.system.web.organization;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.organization.dto.*;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.system.web.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.organization.OrganizationService;
import com.bat.system.api.organization.dto.*;
import com.bat.system.api.organization.dto.data.OrganizationDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 19:10
 */
@Api(tags = "系统模块-组织后台接口", value = "AdminOrganizationController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/organization")
public class AdminOrganizationController extends BaseController {

    @Resource
    private OrganizationService organizationService;

    @GetMapping("/list")
    @ApiOperation(value = "查询组织列表(分页)")
    public Response<PageInfo<OrganizationDTO>> listOrganization(@Valid OrganizationQry qry) {
        PageInfo pageInfo = organizationService.listOrganization(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list")
    @ApiOperation(value = "查询组织列表(分页)(公共数据)")
    public Response<PageInfo<OrganizationDTO>> listOrganizationPo(@Valid OrganizationQry qry) {
        PageInfo pageInfo = organizationService.listOrganization(qry);
        return Response.of(pageInfo);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个组织")
    public Response<OrganizationDTO> getOrganization(@Valid OrganizationId id) {
        return Response.of(organizationService.getOrganizationById(id.getId()));
    }

    @GetMapping("/ids")
    @ApiOperation(value = "通过ID集合查询多个多个组织")
    public Response<List<OrganizationDTO>> getOrganizationByIds(@Valid OrganizationIds ids) {
        return Response.of(organizationService.getOrganizationByIds(ids));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminOrganization, value = CommonLogTypeConstantDTO.AdminOrganizationAdd)
    @PostMapping()
    @ApiOperation(value = "添加组织")
    public Response createOrganization(@RequestBody @Valid OrganizationCreateCmd cmd) {
        organizationService.createOrganization(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminOrganization, value = CommonLogTypeConstantDTO.AdminOrganizationUpdate)
    @PutMapping()
    @ApiOperation(value = "更新组织")
    public Response updateOrganization(@RequestBody @Valid OrganizationUpdateCmd cmd) {
        organizationService.updateOrganization(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminOrganization, value = CommonLogTypeConstantDTO.AdminOrganizationDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除组织")
    public Response deleteOrganization(@RequestBody @Valid OrganizationId id) {
        organizationService.deleteOrganizationById(id.getId());
        return Response.buildSuccess();
    }
}
