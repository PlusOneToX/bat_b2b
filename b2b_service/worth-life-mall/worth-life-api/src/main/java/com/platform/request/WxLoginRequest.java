package com.platform.request;

import com.platform.modules.app.entity.FullUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("微信登录模型")
public class WxLoginRequest implements Serializable {
    @ApiModelProperty("code  eg:oxaA11ulr9134oBL9Xscon5at_Gc")
    private String code;
    @ApiModelProperty("wx.login()返回的userInfo信息，JSON格式参数")
    private FullUserInfo userInfo;
    @ApiModelProperty("邀请码(非必传，仅在邀请注册的时候传递)")
    private String inviteCode;
}
