package com.bat.financial.api.deposit.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:45
 */
@Data
@ApiModel(value = "DepositDetailSummaryUserQry", description = "余额明细汇总")
public class DepositDetailSummaryUserQry {
    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", example = "1")
    private Integer distributorId;

    @ApiModelProperty(value = "开始时间", example = "2018-06-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-07-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
