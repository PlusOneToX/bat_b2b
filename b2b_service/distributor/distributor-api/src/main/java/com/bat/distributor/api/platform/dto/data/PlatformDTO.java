package com.bat.distributor.api.platform.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商平台信息")
public class PlatformDTO {
    @ApiModelProperty(value = "分销商平台id", example = "bat")
    private Integer id;
    @ApiModelProperty(value = "分销商平台名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "平台编码", example = "bat")
    private String platformNo;
    @ApiModelProperty(value = "状态, 1启用0停用", example = "1")
    private Short openFlag;
    @ApiModelProperty(value = "创建时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
