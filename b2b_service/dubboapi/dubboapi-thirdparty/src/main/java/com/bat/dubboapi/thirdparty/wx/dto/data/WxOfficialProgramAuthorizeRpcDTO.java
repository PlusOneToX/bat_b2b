package com.bat.dubboapi.thirdparty.wx.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/9 10:19
 */
public class WxOfficialProgramAuthorizeRpcDTO implements Serializable {
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    
}
