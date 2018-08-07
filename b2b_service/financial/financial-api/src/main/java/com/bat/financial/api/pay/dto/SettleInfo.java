package com.bat.financial.api.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 16:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleInfo {

    @JsonProperty("profit_sharing")
    @ApiModelProperty(value = "是否指定分账，枚举值", example = "true")
    private boolean profitSharing = true;
}
