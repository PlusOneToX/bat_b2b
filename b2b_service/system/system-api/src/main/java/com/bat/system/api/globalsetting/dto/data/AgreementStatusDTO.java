package com.bat.system.api.globalsetting.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/25 11:20
 */
@Data
public class AgreementStatusDTO {
    @ApiModelProperty(value = "签署状态")
    private Boolean sign;
}
