package com.bat.goods.api.tag.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品标签信息封装")
public class TagDTO {

    private Integer id;
    @ApiModelProperty(value = "商品标签名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品标签英文名称", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
