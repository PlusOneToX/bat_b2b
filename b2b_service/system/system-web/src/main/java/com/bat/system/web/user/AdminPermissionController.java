package com.bat.system.web.user;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.system.web.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.PermissionService;
import com.bat.system.api.user.dto.PermissionCreateCmd;
import com.bat.system.api.user.dto.PermissionId;
import com.bat.system.api.user.dto.PermissionQry;
import com.bat.system.api.user.dto.PermissionUpdateCmd;
import com.bat.system.api.user.dto.data.PermissionDTO;
import com.bat.system.api.user.dto.data.PermissionSyncDTO;
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
@Api(tags = "系统模块-权限后台接口", value = "AdminPermissionController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/permission")
public class AdminPermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

    @GetMapping("/list")
    @ApiOperation(value = "查询权限列表(分页)")
    public Response<PageInfo<PermissionDTO>> listPermission(@Valid PermissionQry qry) {
        PageInfo<PermissionDTO> pageInfo = permissionService.listPermission(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list/tree")
    @ApiOperation(value = "查询权限列表(不分页)")
    public Response<List<PermissionSyncDTO>> listPermissionTree() {
        List<PermissionSyncDTO> maps = permissionService.listPermissionTree();
        return Response.of(maps);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个权限")
    public Response<PermissionDTO> getPermission(@Valid PermissionId id) {
        return Response.of(permissionService.getPermissionById(id.getId()));
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPermission, value = CommonLogTypeConstantDTO.AdminPermissionAdd)
    @PostMapping()
    @ApiOperation(value = "添加权限")
    public Response createPermission(@RequestBody @Valid PermissionCreateCmd cmd) {
        permissionService.createPermission(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPermission, value = CommonLogTypeConstantDTO.AdminPermissionUpdate)
    @PutMapping()
    @ApiOperation(value = "更新权限")
    public Response updatePermission(@RequestBody @Valid PermissionUpdateCmd cmd) {
        permissionService.updatePermission(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPermission, value = CommonLogTypeConstantDTO.AdminPermissionDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除权限")
    public Response deletePermission(@RequestBody @Valid PermissionId id) {
        permissionService.deletePermissionById(id.getId());
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPermission, value = CommonLogTypeConstantDTO.AdminPermissionSync)
    @PutMapping("/po/sync")
    @ApiOperation(value = "根据接口文档 实现接口列表 内部测试调用")
    public Response syncPermission(String gateWaySwaggerUrl) {
        permissionService.syncPermission(gateWaySwaggerUrl);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPermission, value = CommonLogTypeConstantDTO.AdminPermissionSyncCache)
    @PutMapping("/po/syncCache")
    @ApiOperation(value = "根据现有关联关系 更新缓存 全量更新")
    public Response syncCache() {
        permissionService.syncCache();
        return Response.buildSuccess();
    }
}
