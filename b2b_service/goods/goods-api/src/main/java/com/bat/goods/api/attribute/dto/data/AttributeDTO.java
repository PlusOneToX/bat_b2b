package com.bat.goods.api.attribute.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品属性信息封装")
public class AttributeDTO {

    private Integer id;
    @ApiModelProperty(value = "商品属性名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品属性英文名称", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "商品属性描述", required = false, example = "bat品牌")
    private String description;
    @ApiModelProperty(value = "属性类型 1.货品规格 2.货品颜色 3.商品参数", required = true, example = "0")
    private Short attributeType;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "商品属性值列表")
    private List<AttributeValueDTO> values;
}
