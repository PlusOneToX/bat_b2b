package com.bat.thirdparty.dy.service.convertor;

import com.bat.thirdparty.dy.response.DyCode2SessionResponse;
import com.bat.thirdparty.dy.response.dto.DyUserInfoDTO;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * 沙漠
 */
public class DyConvertor {

    public static DyMiniProgramAuthorizeRpcDTO toWxProgramAuthorizeRpcDTO(DyCode2SessionResponse response,
                                                                          DyUserInfoDTO dyUserInfoDTO) {
        DyMiniProgramAuthorizeRpcDTO rpcDTO = new DyMiniProgramAuthorizeRpcDTO();
        if (dyUserInfoDTO != null) {
            rpcDTO.setCountryCode(dyUserInfoDTO.getCountryCode());
            if (StringUtils.isNotBlank(dyUserInfoDTO.getPhoneNumber())) {
                rpcDTO.setPhone(dyUserInfoDTO.getPhoneNumber());
            } else {
                rpcDTO.setPhone(dyUserInfoDTO.getPurePhoneNumber());
            }
        }
        rpcDTO.setOpenid(response.getData().getOpenid());
        return rpcDTO;
    }
}
