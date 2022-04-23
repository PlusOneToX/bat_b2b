package com.platform.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("合伙人奖励")
public class RewardAmountVo {
    @ApiModelProperty("邀请人数量")
    private Integer inviteCount;
    @ApiModelProperty("合伙人奖励总数量")
    private BigDecimal rewardAmount;
}
