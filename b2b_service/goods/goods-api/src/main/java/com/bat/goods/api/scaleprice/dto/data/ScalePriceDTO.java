package com.bat.goods.api.scaleprice.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "价格等级信息封装")
public class ScalePriceDTO {

    private Integer id;
    @ApiModelProperty(value = "价格等级名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "对应erp价目表价格字段", required = false, example = "F_PAEZ_Price2")
    private String erpField;
    @ApiModelProperty(value = "价格等级描述", required = false, example = "保护类")
    private String description;
    @ApiModelProperty(value = "是否建议零售价:0 否 1 是", required = true, example = "0")
    private Short retailFlag;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
