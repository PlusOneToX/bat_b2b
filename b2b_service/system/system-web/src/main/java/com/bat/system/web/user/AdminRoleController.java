package com.bat.system.web.user;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.user.dto.*;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.MenuService;
import com.bat.system.api.user.RoleService;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.RoleDTO;
import com.bat.system.api.user.dto.data.UserInfoDTO;
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
@Api(tags = "系统模块-角色后台接口", value = "AdminRoleController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/role")
public class AdminRoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    @ApiOperation(value = "查询角色列表(分页)")
    public Response<PageInfo<RoleDTO>> listRole(@Valid RoleQry qry) {
        PageInfo<RoleDTO> pageInfo = roleService.listRole(qry);
        return Response.of(pageInfo);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个角色")
    public Response<RoleDTO> getRole(@Valid RoleId id) {
        return Response.of(roleService.getRoleById(id.getId()));
    }

    @GetMapping("/ids")
    @ApiOperation(value = "通过IDS查询多个角色")
    public Response<UserInfoDTO> getRoleByIds(@Valid RoleIds ids) {
        return Response.of(roleService.getRoleByIds(ids.getIds()));
    }

    // @GetMapping("/po/list/menuTree")
    // @ApiOperation(value = "通过id查询菜单列表(不分页)")
    // public Response<List<Map.Entry<String, List<MenuShowSyncDTO>>>> listMenuTree() {
    // return Response.of(menuService.listMenuTree());
    // }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole, value = CommonLogTypeConstantDTO.AdminRoleAdd)
    @PostMapping()
    @ApiOperation(value = "添加角色")
    public Response createRole(@RequestBody @Valid RoleCreateCmd cmd) {
        roleService.createRole(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole,
        value = CommonLogTypeConstantDTO.AdminRoleUpdate)
    @PutMapping()
    @ApiOperation(value = "更新角色")
    public Response updateRole(@RequestBody @Valid RoleUpdateCmd cmd) {
        roleService.updateRole(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRole,
        value = CommonLogTypeConstantDTO.AdminRoleDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除角色")
    public Response deleteRole(@RequestBody @Valid RoleId id) {
        roleService.deleteRoleById(id.getId());
        return Response.buildSuccess();
    }

}
