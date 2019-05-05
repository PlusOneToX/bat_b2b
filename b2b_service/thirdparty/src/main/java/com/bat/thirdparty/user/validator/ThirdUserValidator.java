package com.bat.thirdparty.user.validator;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.system.ThirdSystemErrorCode;
import com.bat.dubboapi.system.user.dto.UserLoginRpcQry;
import org.apache.commons.lang3.StringUtils;

public class ThirdUserValidator {

    /**
     * 校验登录用户名和密码
     * @param userLoginRpcQry
     */
    public static void validateLogin(UserLoginRpcQry userLoginRpcQry){
        if(StringUtils.isBlank(userLoginRpcQry.getUserName())){
            throw ThirdPartyException.buildException(ThirdSystemErrorCode.T_SYSTEM_USER_NAME_NULL);
        }
        if(StringUtils.isBlank(userLoginRpcQry.getPassword())){
            throw ThirdPartyException.buildException(ThirdSystemErrorCode.T_SYSTEM_PASSWORD_NULL);
        }
    }
}
