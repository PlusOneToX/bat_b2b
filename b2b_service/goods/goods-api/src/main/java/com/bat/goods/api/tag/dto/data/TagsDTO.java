package com.bat.goods.api.tag.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "商品标签信息封装")
public class TagsDTO {

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "标签列表")
    private List<Tag> tags;

    @Data
    public static  class Tag {
        private Integer id;
        @ApiModelProperty(value = "商品标签名称", required = true, example = "bat")
        private String name;
        @ApiModelProperty(value = "商品标签英文名称", required = false, example = "bat")
        private String nameEn;
        @ApiModelProperty(value = "商品id")
        private Integer goodsId;
    }

}
