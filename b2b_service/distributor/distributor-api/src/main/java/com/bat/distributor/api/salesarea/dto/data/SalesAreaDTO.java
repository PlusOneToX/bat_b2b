package com.bat.distributor.api.salesarea.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "销售区域信息")
public class SalesAreaDTO {

    private Integer id;
    @ApiModelProperty(value = "销售区域名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "销售区域描述", required = false, example = "bat")
    private String description;
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
