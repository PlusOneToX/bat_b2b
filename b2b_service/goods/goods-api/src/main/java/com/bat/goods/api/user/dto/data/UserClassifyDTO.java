package com.bat.goods.api.user.dto.data;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品分类信息")
public class UserClassifyDTO implements Serializable {

    private Integer id;
    @ApiModelProperty(value = "商品分类名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "商品分类英文名称", example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "排序,数据越小排在越前面", example = "1")
    private Integer sort;
    @ApiModelProperty(value = "父级ID", example = "1001")
    private Integer parentId;
    @ApiModelProperty(value = "分类图片地址", example = "http://bat")
    private String imageUrl;
    @ApiModelProperty(value = "分类英文图片地址", example = "http://bat")
    private String imageUrlEn;

    @ApiModelProperty(value = "小程序分类名称")
    private String appletName;

    @ApiModelProperty(value = "子分类")
    private List<UserClassifyDTO> subClassifies;

    @ApiModelProperty(value = "banner图")
    private List<UserClassifyBannerDTO> userClassifyBannerDTOS;
}
