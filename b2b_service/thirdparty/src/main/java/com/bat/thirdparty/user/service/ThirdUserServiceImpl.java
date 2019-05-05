package com.bat.thirdparty.user.service;

import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.user.api.ThirdUserServiceI;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.UserLoginRpcQry;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;

@Service
public class ThirdUserServiceImpl implements ThirdUserServiceI {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemUserServiceRpc systemUserServiceRpc;

    /**
     * 第三方系统用户登录
     * 
     * @param userLoginRpcQry
     * @return
     */
    @Override
    public Response login(UserLoginRpcQry userLoginRpcQry) {
        com.bat.dubboapi.system.common.Response<UserRpcDTO> userRpcDTOResponse =
            systemUserServiceRpc.userLogin(userLoginRpcQry);
        if (userRpcDTOResponse == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
        }
        if (!userRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(userRpcDTOResponse.getErrCode(),
                userRpcDTOResponse.getErrMessage());
        }
        // 返回gateway处理令牌
        userLoginRpcQry.setId(userRpcDTOResponse.getData().getId());
        return Response.of(userLoginRpcQry);
    }
}
