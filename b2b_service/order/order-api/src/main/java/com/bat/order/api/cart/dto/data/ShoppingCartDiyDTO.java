package com.bat.order.api.cart.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "购物车商品定制信息")
public class ShoppingCartDiyDTO {

    @ApiModelProperty(value = "定制信息id", example = "135445")
    private Integer id;
    @ApiModelProperty(value = "产品类型id", example = "135445")
    private Integer categoryId;
    @ApiModelProperty(value = "产品类型名称", example = "产品类型名称")
    private String categoryName;
    @ApiModelProperty(value = "材质id", example = "135445")
    private Integer materialId;
    @ApiModelProperty(value = "材质名称", example = "材质名称")
    private String materialName;
    @ApiModelProperty(value = "生产商 YC云创 MK麦客 LHW联辉王", example = "MK")
    private String manufactors;
    @ApiModelProperty(value = "品牌id", example = "135445")
    private Integer brandId;
    @ApiModelProperty(value = "品牌名称", example = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "型号id", example = "135445")
    private Integer modelId;
    @ApiModelProperty(value = "型号名称", example = "型号名称")
    private String modelName;
    @ApiModelProperty(value = "图片id（网络图传0）", example = "135445")
    private Integer pictureId;
    @ApiModelProperty(value = "图片名称", example = "图片名称")
    private String pictureName;
    @ApiModelProperty(value = "生产图URL地址", example = "http://bat")
    private String generateImage;
    @ApiModelProperty(value = "预览图URL地址", example = "http://bat")
    private String previewImage;
    @ApiModelProperty(value = "商品是否有效 1、有效 0、无效", example = "1")
    private Short openFlag;
}
