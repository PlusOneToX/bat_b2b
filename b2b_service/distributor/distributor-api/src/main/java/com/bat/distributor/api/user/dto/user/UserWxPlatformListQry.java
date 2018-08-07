package com.bat.distributor.api.user.dto.user;

import com.bat.distributor.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserWxPlatformListQry", description = "查询微信平台")
public class UserWxPlatformListQry extends BasePage {
    @ApiModelProperty(value = "平台类型：1 公众号 2 小程序,不传查询全部", required = false, example = "1")
    private Short type;
}
