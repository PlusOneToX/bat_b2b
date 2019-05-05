package com.bat.thirdparty.erp.api.request.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ERP订单变更明细")
public class ErpOrderChangeDetailEntry {
    @ApiModelProperty(value = "物料编号", required = true, example = "80462336544")
    @NotBlank(message = "P_THIRDPARTY_ITEM_NO_NULL")
    private String itemNo;
    @ApiModelProperty(value = "含税价", required = true, example = "12.68")
    @NotNull(message = "P_THIRDPARTY_ITEM_TAX_PRICE_NULL")
    private BigDecimal itemTaxPrice;
    @ApiModelProperty(value = "物料名称", required = false, example = "物料名称")
    private String itemName;
    @ApiModelProperty(value = "数量", required = true, example = "12")
    @NotNull(message = "P_THIRDPARTY_ITEM_NUM_NULL")
    private Integer num;
    @ApiModelProperty(value = "是否赠品", required = true, example = "12")
    @NotNull(message = "P_THIRDPARTY_ITEM_FREE_NULL")
    private Boolean isFree;
    @ApiModelProperty(value = "变更单明细内码(erp变更单明细表主键)", required = true, example = "12344")
    @NotNull(message = "P_THIRDPARTY_ORDER_CHANGE_ENTRY_ID_NULL")
    @JsonProperty("EntryId")
    private Integer EntryId;
    @ApiModelProperty(value = "销售订单明细内码(erp销售订单明细表主键)", required = true, example = "124544")
    @NotNull(message = "P_THIRDPARTY_ORDER_ITEN_ID_NULL")
    private Integer itemOrderId;
    @ApiModelProperty(value = "销售订单明细变更类型：A 增加 B 修改 D 删除", required = true, example = "A")
    @NotNull(message = "P_THIRDPARTY_ORDER_ITEN_CHANGE_TYPE_NULL")
    private String itemFChangeType;
}
