package com.bat.distributor.web.role;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.api.role.RoleServiceI;
import com.bat.distributor.api.role.dto.RoleCmd;
import com.bat.distributor.api.role.dto.RoleId;
import com.bat.distributor.api.role.dto.RoleListQry;
import com.bat.distributor.api.role.dto.RoleOpenCmd;
import com.bat.distributor.api.role.dto.data.RoleDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商联系人角色后台接口", value = "AdminRoleController")
@RestController
@RequestMapping("/distributor/v1/web/admin/role")
public class AdminRoleController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminRoleController.class);

    @Resource
    private RoleServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商联系人角色列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<GroupDTO>> listRole(@Valid RoleListQry qry) {
        PageInfo<GroupDTO> pageInfo = service.listRole(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商联系人角色列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<GroupDTO>> listRolePo(@Valid RoleListQry qry) {
        PageInfo<GroupDTO> pageInfo = service.listRole(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商联系人角色id获取分销商联系人角色详情")
    @GetMapping()
    public Response<RoleDTO> getRole(@Valid RoleId qry) {
        RoleDTO dto = service.getRole(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole, value = CommonLogTypeConstantDTO.AdminRoleAdd)
    @ApiOperation(value = "添加分销商联系人角色")
    @PostMapping()
    public Response createRole(@RequestBody @Valid RoleCmd cmd) {
        service.createRole(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole, value = CommonLogTypeConstantDTO.AdminRoleUpdate)
    @ApiOperation(value = "更新分销商联系人角色")
    @PutMapping()
    public Response updateRole(@RequestBody @Valid RoleCmd cmd) {
        service.updateRole(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole, value = CommonLogTypeConstantDTO.AdminRoleUpdateStatus)
    @ApiOperation(value = "更新分销商联系人角色状态")
    @PutMapping(value = "/open")
    public Response openRole(@RequestBody @Valid RoleOpenCmd cmd) {
        service.openRole(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole, value = CommonLogTypeConstantDTO.AdminRoleDeleteById)
    @ApiOperation(value = "根据分销商联系人角色id删除分销商联系人角色")
    @DeleteMapping()
    public Response deleteRole(@RequestBody @Valid RoleId cmd) {
        service.deleteRole(cmd);
        return Response.buildSuccess();
    }

}
