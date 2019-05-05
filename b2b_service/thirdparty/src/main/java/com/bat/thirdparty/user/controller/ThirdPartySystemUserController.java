package com.bat.thirdparty.user.controller;

import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.user.api.ThirdUserServiceI;
import com.bat.dubboapi.system.user.dto.UserLoginRpcQry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/user")
@Api(tags = "第三方系统用户后台接口")
public class ThirdPartySystemUserController {

    @Autowired
    private ThirdUserServiceI thirdUserServiceI;

    @PostMapping(value ="/login")
    @ApiOperation(value = "第三方系统用户登录")
    public Response login(@RequestBody UserLoginRpcQry userLoginRpcQry){
        return thirdUserServiceI.login(userLoginRpcQry);
    }
}
