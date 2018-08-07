package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "UserQrCodeDTO", description = "分销商分销二维码")
public class UserQrCodeDTO {
    @ApiModelProperty(value = "分销商分销二维码图片链接", example = "http://bat")
    private String qrCodeUrl;
}
