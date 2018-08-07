package com.bat.order.api.deliver.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class OrderDeliverStopPlaceDTO {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("地点名称")
    private String placeName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
