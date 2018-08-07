package com.bat.goods.api.tag.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TagCmd", description = "商品标签信息")
public class TagCmd {

    @ApiModelProperty(value = "商品标签ID", required = false)
    private Integer id;
    @NotBlank(message = "P_GOODS_TAG_NAME_NULL")
    @ApiModelProperty(value = "商品标签名称,最长20个字符", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品标签英文名称，最长100个字符", required = false, example = "bat")
    private String nameEn;
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @NotNull(message = "P_GOODS_SORT_NULL")
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;

}
