package com.bat.system.web.organization;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.system.web.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.organization.DepartmentService;
import com.bat.system.api.organization.dto.DepartmentCreateCmd;
import com.bat.system.api.organization.dto.DepartmentId;
import com.bat.system.api.organization.dto.DepartmentQry;
import com.bat.system.api.organization.dto.DepartmentUpdateCmd;
import com.bat.system.api.organization.dto.data.DepartmentDTO;
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
@Api(tags = "系统模块-销售部门后台接口", value = "AdminDepartmentController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/department")
public class AdminDepartmentController extends BaseController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/list")
    @ApiOperation(value = "查询销售部门列表(分页)")
    public Response<PageInfo<DepartmentDTO>> listDepartment(@Valid DepartmentQry qry) {
        PageInfo pageInfo = departmentService.listDepartment(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list")
    @ApiOperation(value = "查询销售部门列表(分页)(公共数据)")
    public Response<PageInfo<DepartmentDTO>> listDepartmentPo(@Valid DepartmentQry qry) {
        PageInfo pageInfo = departmentService.listDepartment(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list/id")
    @ApiOperation(value = "组织下的部门列表(分页)(公共数据)")
    public Response<PageInfo<DepartmentDTO>> listDepartmentPoByOrganization(@Valid DepartmentQry qry) {
        PageInfo pageInfo = departmentService.listDepartmentByOrganization(qry);
        return Response.of(pageInfo);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个销售部门")
    public Response<DepartmentDTO> getDepartment(@Valid DepartmentId id) {
        return Response.of(departmentService.getDepartmentById(id.getId()));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDepartment, value = CommonLogTypeConstantDTO.AdminDepartmentAdd)
    @PostMapping()
    @ApiOperation(value = "添加销售部门")
    public Response createDepartment(@RequestBody @Valid DepartmentCreateCmd cmd) {
        departmentService.createDepartment(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDepartment, value = CommonLogTypeConstantDTO.AdminDepartmentUpdate)
    @PutMapping()
    @ApiOperation(value = "更新销售部门")
    public Response updateDepartment(@RequestBody @Valid DepartmentUpdateCmd cmd) {
        departmentService.updateDepartment(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDepartment, value = CommonLogTypeConstantDTO.AdminDepartmentDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除销售部门")
    public Response deleteDepartment(@RequestBody @Valid DepartmentId id) {
        departmentService.deleteDepartmentById(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDepartment, value = CommonLogTypeConstantDTO.AdminDepartmentSync)
    @PutMapping("/po/sync")
    @ApiOperation(value = "与ERP进行同步")
    public Response syncDepartment() {
        departmentService.syncDepartment();
        return Response.buildSuccess();
    }
}
