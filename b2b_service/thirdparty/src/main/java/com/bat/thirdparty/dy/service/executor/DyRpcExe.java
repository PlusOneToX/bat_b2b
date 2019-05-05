package com.bat.thirdparty.dy.service.executor;

import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.dy.response.DyCode2SessionResponse;
import com.bat.thirdparty.dy.response.dto.DyUserInfoDTO;
import com.bat.thirdparty.dy.service.convertor.DyConvertor;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.dy.dto.DyMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;
import com.bat.thirdparty.dy.config.DyConfig;
import com.bat.thirdparty.dy.request.DyCode2SessionRequest;
import com.bat.thirdparty.dy.utils.DyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 沙漠
 */
@Component
public class DyRpcExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(DyRpcExe.class);

    @Resource
    private HttpUtil httpUtil;
    @Resource
    private DyConfig programConfig;

    public Response<DyMiniProgramAuthorizeRpcDTO> dyMiniProgramAuthorize(DyMiniProgramAuthorizeRpcCmd cmd) {
        String url = programConfig.getDyCode2SessionUrl();
        DyCode2SessionRequest request = new DyCode2SessionRequest();
        BeanUtils.copyProperties(cmd, request);
        DyCode2SessionResponse response = httpUtil.requestFor(url, HttpMethod.POST, request, DyCode2SessionResponse.class);
        if (response.getErr_no() != 0) {
            return Response.buildFailure(ErrorCode.B_DY_CODE_USED_ERROR, response.getErr_tips());
        }
        DyUserInfoDTO dyUserInfoDTO = null;
        if (StringUtils.isNotBlank(cmd.getEncryptedData())) {
            dyUserInfoDTO = DyUtil.getUserInfo(cmd.getEncryptedData(), response.getData().getSession_key(), cmd.getIv());
            if (dyUserInfoDTO == null) {
                return Response.buildFailure(ErrorCode.B_DY_CODE_USED_ERROR, MessageUtils.get("B_DY_CODE_USED_ERROR"));
            }
        }
        return Response.of(DyConvertor.toWxProgramAuthorizeRpcDTO(response, dyUserInfoDTO));
    }



}
