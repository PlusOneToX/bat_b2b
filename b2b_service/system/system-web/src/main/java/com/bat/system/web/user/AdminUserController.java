package com.bat.system.web.user;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.user.dto.*;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.system.web.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.UserService;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.RockAccountInfoDTO;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.web.annotation.SearchMethod;
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
@Api(tags = "系统模块-后台用户后台接口", value = "AdminUserController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/user")
public class AdminUserController extends BaseController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    @ApiOperation(value = "查询后台用户列表(分页)")
    @SearchMethod
    public Response<PageInfo<UserDTO>> listUser(@Valid UserQry qry) {
        PageInfo pageInfo = userService.listUser(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list/salesman")
    @ApiOperation(value = "查询后台用户列表(分页)(没有角色组织信息)")
    @SearchMethod
    public Response<PageInfo<UserDTO>> listSalesMan(@Valid UserQry qry) {
        PageInfo pageInfo = userService.listSalesMan(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list")
    @ApiOperation(value = "查询后台用户列表(分页)")
    @SearchMethod
    public Response<PageInfo<UserDTO>> listUserPo(@Valid UserQry qry) {
        PageInfo pageInfo = userService.listUser(qry);
        return Response.of(pageInfo);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个后台用户")
    public Response<UserDTO> getUser(@Valid UserId id) {
        return Response.of(userService.getUserById(id.getId()));
    }

    @GetMapping("/ids")
    @ApiOperation(value = "通过ID集合查询多个后台用户")
    public Response<List<UserDTO>> getUserByIds(@Valid UserIds ids) {
        return Response.of(userService.getUserByIds(ids));
    }

    @GetMapping("/po/ids")
    @ApiOperation(value = "通过ID集合查询多个后台用户(公共查询)")
    public Response<List<UserDTO>> getUserPoByIds(@Valid UserIds ids) {
        return Response.of(userService.getUserByIds(ids));
    }

    @GetMapping("/po/rockAccountInfoList")
    @ApiOperation(value = "获取账户中心员工信息(公共查询)")
    public Response<List<RockAccountInfoDTO>> getRockAccountInfoList() {
        return Response.of(userService.findRockAccountInfoList());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserAdd)
    @PostMapping()
    @ApiOperation(value = "添加后台用户")
    public Response createUser(@RequestBody @Valid UserCreateCmd cmd) {
        userService.createUser(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserLogin)
    @PostMapping("/login")
    @ApiOperation(value = "后台用户登录")
    public Response<UserDTO> userLogin(@RequestBody @Valid UserLoginQry qry) {
        return Response.of(userService.userLogin(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserLogout)
    @PostMapping("/logout")
    @ApiOperation(value = "后台用户登出")
    public Response userLogout() {
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserUpdate)
    @PutMapping()
    @ApiOperation(value = "更新后台用户")
    public Response updateUser(@RequestBody @Valid UserUpdateCmd cmd) {
        userService.updateUser(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserUpdateStatus)
    @PutMapping("/status")
    @ApiOperation(value = "更新后台用户")
    public Response updateUserStatus(@RequestBody @Valid UserStatusUpdateCmd cmd) {
        userService.updateUserStatus(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserUpdatePassword)
    @PutMapping("/password")
    @ApiOperation(value = "更新后台用户")
    public Response updateUserPassword(@RequestBody @Valid UserStatusPasswordCmd cmd) {
        userService.updateUserPassword(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除后台用户")
    public Response deleteUser(@RequestBody @Valid UserId id) {
        userService.deleteUserById(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminUser, value = CommonLogTypeConstantDTO.AdminUserSync)
    @PutMapping("/po/sync")
    @ApiOperation(value = "通过用户id同步ERP的部门组织关系")
    public Response syncUser(@RequestBody @Valid UserId id) {
        userService.syncUser(id.getId());
        return Response.buildSuccess();
    }
}
