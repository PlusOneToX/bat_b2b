package com.bat.system.api.check.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/15 17:05
 */
@Data
@ApiModel(value = "CheckConfigUpdateCmd", description = "审核配置更新")
public class CheckConfigUpdateCmd {

    @NotNull(message = "P_CHECK_CONFIG_EXT_NULL")
    @ApiModelProperty(
        value = "ext 1 商品管理上下架审批, 2 分销商编辑审批, 3 仓库库存调整审批,4 仓库库存预留审批, 5 订单价格审批,6 订单对账折扣审批,7 促销活动新增审批 8 促销活动编辑审批",
        required = true, example = "1")
    private Short ext;

    @ApiModelProperty(value = "checkUsers 审批人员多个以,分隔", example = "150161")
    private String checkUsers;

    @NotNull(message = "P_CHECK_CONFIG_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "openFlag 1.审批开启, 0. 审批关闭", required = true, example = "1")
    private Short openFlag;
}
