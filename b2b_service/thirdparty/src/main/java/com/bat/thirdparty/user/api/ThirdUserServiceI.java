package com.bat.thirdparty.user.api;

import com.bat.thirdparty.common.base.Response;
import com.bat.dubboapi.system.user.dto.UserLoginRpcQry;

public interface ThirdUserServiceI {

    /**
     * 第三方系统用户登录
     * @param userLoginRpcQry
     * @return
     */
    Response login(UserLoginRpcQry userLoginRpcQry);
}
