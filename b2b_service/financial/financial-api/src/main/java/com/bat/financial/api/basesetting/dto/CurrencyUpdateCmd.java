package com.bat.financial.api.basesetting.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/18 11:22
 */
@Data
@ApiModel(value = "CurrencyUpdateCmd", description = "币别更新")
public class CurrencyUpdateCmd {

    @NotNull(message = "P_CURRENCY_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotBlank(message = "P_CURRENCY_NAME_NULL")
    @ApiModelProperty(value = "货币名称", required = true, example = "人民币")
    private String name;

    @NotBlank(message = "P_CURRENCY_CODE_NULL")
    @ApiModelProperty(value = "货币代码", required = true, example = "CNY")
    private String currencyCode;

    @NotNull(message = "P_CURRENCY_MONEY_ACCURACY_NULL")
    @ApiModelProperty(value = "金额精度", required = true, example = "2")
    private Short moneyAccuracy;

    @ApiModelProperty(value = "erp编码", example = "")
    private String erpNo;

    @NotNull(message = "P_CURRENCY_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "数据状态", required = true, example = "1")
    private Short openFlag;

}
