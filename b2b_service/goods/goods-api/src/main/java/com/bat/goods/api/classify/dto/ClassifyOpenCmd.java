package com.bat.goods.api.classify.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassifyOpenCmd", description = "商品分类停用启用")
public class ClassifyOpenCmd extends ClassifyId implements Serializable {
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = true, example = "78445")
    private String cacheKey = "userClassifyList";
}
