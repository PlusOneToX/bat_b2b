package com.bat.thirdparty.goods.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于接收推送订单信息(详情基于id)
 */
@Data
@ApiModel(value = "ItemCodeCmd", description = "货品编码")
public class ItemCodeCmd implements Serializable {

    @NotNull(message = "P_THIRDPARTY_ITEM_CODE_NULL")
    @ApiModelProperty(value = "货品编码列表", required = true)
    private List<String> itemCodes;

}
