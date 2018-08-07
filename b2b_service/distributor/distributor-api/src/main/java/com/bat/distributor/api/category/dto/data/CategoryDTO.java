package com.bat.distributor.api.category.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商类别信息")
public class CategoryDTO {

    private Integer id;
    @ApiModelProperty(value = "分销商类别名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商类别描述", required = false, example = "bat")
    private String description;
    @ApiModelProperty(value = "ERP分销商类别编号", required = false, example = "bat")
    private String erpCategoryNo;
    @ApiModelProperty(value = "订单类型", required = true, example = "14565")
    private Integer orderTypeId;
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
