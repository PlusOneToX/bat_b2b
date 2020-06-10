package com.bat.flexible.api.index.dto.series;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DistributorSeriesQry {

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "展示主题序列个数")
    private Integer seriesNum=2;

    @ApiModelProperty(value = "每个系列展示图片个数")
    private Integer pictureNum=4;

}
