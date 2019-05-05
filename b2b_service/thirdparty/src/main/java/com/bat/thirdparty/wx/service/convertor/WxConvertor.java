package com.bat.thirdparty.wx.service.convertor;

import org.apache.commons.lang3.StringUtils;

import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;
import com.bat.thirdparty.wx.api.response.WxProgramAuthorizeResponse;
import com.bat.thirdparty.wx.api.response.dto.WxUserInfoDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 11:25
 */
public class WxConvertor {

    public static WxMiniProgramAuthorizeRpcDTO toWxProgramAuthorizeRpcDTO(WxProgramAuthorizeResponse response,
        WxUserInfoDTO wxUserInfoDTO) {
        WxMiniProgramAuthorizeRpcDTO rpcDTO = new WxMiniProgramAuthorizeRpcDTO();
        if (wxUserInfoDTO != null) {
            rpcDTO.setCountryCode(wxUserInfoDTO.getCountryCode());
            if (StringUtils.isNotBlank(wxUserInfoDTO.getPhoneNumber())) {
                rpcDTO.setPhone(wxUserInfoDTO.getPhoneNumber());
            } else {
                rpcDTO.setPhone(wxUserInfoDTO.getPurePhoneNumber());
            }
        }
        rpcDTO.setOpenid(response.getOpenid());
        return rpcDTO;
    }
}
