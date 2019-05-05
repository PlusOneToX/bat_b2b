package com.bat.promotion.api.rebatevoucher.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 16:15
 */
@Data
@ApiModel(value = "RebateVoucherCmds", description = "返利代金券信息")
public class RebateVoucherCmds {

    @ApiModelProperty(value = "返利代金券集合", required = false, example = "1")
    private List<RebateVoucherCmd> rebateVoucherCmdList;
}
