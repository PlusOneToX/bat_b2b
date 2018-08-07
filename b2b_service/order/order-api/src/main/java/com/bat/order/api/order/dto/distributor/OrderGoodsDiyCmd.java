package com.bat.order.api.order.dto.distributor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品定制信息")
public class OrderGoodsDiyCmd {
    @ApiModelProperty(value = "产品类型id", required = true, example = "1223234")
    @NotNull(message = "P_ORDER_CATEGORY_ID_NULL")
    private Integer categoryId;
    @ApiModelProperty(value = "产品类型名称", required = true, example = "产品类型名称")
    @NotBlank(message = "P_ORDER_CATEGORY_NAME_NULL")
    private String categoryName;
    @ApiModelProperty(value = "材质id", required = true, example = "1223234")
    @NotNull(message = "P_ORDER_MATERIAL_ID_NULL")
    private Integer materialId;
    @ApiModelProperty(value = "材质名称", required = true, example = "材质名称")
    @NotBlank(message = "P_ORDER_MATERIAL_NAME_NULL")
    private String materialName;
    @ApiModelProperty(value = "生产商 YC云创 MK麦客 LHW联辉王 ", required = true, example = "MK")
    @NotBlank(message = "P_ORDER_MANUFACTOR_NULL")
    private String manufactors;
    @ApiModelProperty(value = "品牌id", required = true, example = "1223234")
    @NotNull(message = "P_ORDER_BRAND_ID_NULL")
    private Integer brandId;
    @ApiModelProperty(value = "品牌名称", required = true, example = "品牌名称")
    @NotBlank(message = "P_ORDER_BRAND_NAME_NULL")
    private String brandName;
    @ApiModelProperty(value = "型号id", required = true, example = "1223234")
    @NotNull(message = "P_ORDER_MODEL_NAME_NULL")
    private Integer modelId;
    @ApiModelProperty(value = "型号名称", required = true, example = "型号名称")
    @NotBlank(message = "P_ORDER_MODEL_NAME_NULL")
    private String modelName;
    @ApiModelProperty(value = "图片id（网络图传0）", required = true, example = "1223234")
    @NotNull(message = "P_ORDER_PICTURE_ID_NULL")
    private Integer pictureId;
    @ApiModelProperty(value = "图片名称", required = true, example = "图片名称")
    private String pictureName;
    @ApiModelProperty(value = "生产图URL地址", required = true, example = "http://bat")
    @NotBlank(message = "P_ORDER_PICTURE_GENERATE_IMAGE_NULL")
    private String generateImage;
    @ApiModelProperty(value = "预览图URL地址", required = true, example = "http://bat")
    @NotBlank(message = "P_ORDER_PICTURE_PREVIEW_IMAGE_NULL")
    private String previewImage;
}
