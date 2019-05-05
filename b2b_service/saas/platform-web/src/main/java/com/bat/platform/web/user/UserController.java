package com.bat.platform.web.user;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.platform.api.base.common.IdDTO;
import com.bat.platform.api.user.dto.UserCmd;
import com.bat.platform.api.user.dto.UserLoginQry;
import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.platform.api.user.UserServiceI;
import com.bat.platform.api.user.dto.UserListQry;
import com.bat.platform.api.user.dto.data.UserDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "平台用户接口", value = "UserController")
@RestController
@RequestMapping("/platform/v1/web/user")
public class UserController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserServiceI service;

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public Response<UserDTO> login(@Valid @RequestBody UserLoginQry qry) {
        UserDTO userDTO = service.login(qry);
        return Response.of(userDTO);
    }

    @ApiOperation(value = "用户详情")
    @GetMapping(value = "/detail")
    public Response<UserDTO> detail(@Valid IdDTO idDTO) {
       UserDTO userDTO= service.detail(idDTO.getId());
        return Response.of(userDTO);
    }

    @ApiOperation(value = "查询平台用户列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<UserDTO>> listUser(@Valid UserListQry qry) {
        PageInfo<UserDTO> pageInfo = service.listUser(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody UserCmd cmd) {
        service.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody UserCmd cmd) {
        service.update(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/delete")
    public Response delete(@Valid @RequestBody IdDTO idDTO) {
        service.deleteById(idDTO.getId());
        return Response.buildSuccess();
    }
}
