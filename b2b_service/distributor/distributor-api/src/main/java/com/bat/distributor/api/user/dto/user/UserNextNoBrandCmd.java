package com.bat.distributor.api.user.dto.user;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserNextNoBrandCmd", description = "下级分销商不可视品牌ids")
public class UserNextNoBrandCmd extends UserId {
    @NotNull(message = "P_DISTRIBUTOR_IDS_NULL")
    @ApiModelProperty(value = "不可视品牌ids(全量)", required = true)
    private List<Integer> brandIds;
}
