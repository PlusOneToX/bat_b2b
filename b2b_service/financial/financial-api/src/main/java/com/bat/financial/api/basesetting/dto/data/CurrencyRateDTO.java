package com.bat.financial.api.basesetting.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/18 14:52
 */
@Data
public class CurrencyRateDTO {

    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "直接汇率", required = true, example = "6.5079")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "间接汇率", required = true, example = "0.1537")
    private BigDecimal reverseExRate;

    @ApiModelProperty(value = "原币代码", required = true, example = "USD")
    private String cyForid;

    @ApiModelProperty(value = "目标币代码", required = true, example = "CNY")
    private String cyToid;

    @ApiModelProperty(value = "开始时间", required = true, example = "2018-05-18 15:12:00")
    private Date begDate;

    @ApiModelProperty(value = "创建时间", required = true, example = "2018-05-18 15:12:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", required = true, example = "2018-05-18 15:12:00")
    private Date updateTime;

}
