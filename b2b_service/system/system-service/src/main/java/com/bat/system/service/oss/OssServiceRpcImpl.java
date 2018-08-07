package com.bat.system.service.oss;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.oss.OssService;
import com.bat.system.api.oss.dto.data.AssumeRoleDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.dubboapi.thirdparty.oss.dto.AssumeRoleRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/11 21:31
 */
@DubboService
public class OssServiceRpcImpl implements OssService {

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartySystemOssServiceRpc rpc;

    @Override
    public AssumeRoleDTO getSts(String userId) {
        AssumeRoleDTO role = new AssumeRoleDTO();
        Response<AssumeRoleRpcDTO> assumeRole = rpc.getAssumeRole(userId);
        if (assumeRole.isSuccess()) {
            AssumeRoleRpcDTO data = assumeRole.getData();
            BeanUtils.copyProperties(data, role);
            return role;
        } else {
            throw SystemException.buildException(assumeRole.getErrCode(), assumeRole.getErrMessage());
        }
    }
}
