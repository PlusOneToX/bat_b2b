package com.bat.order.api.order.dto.diy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderDetailDiyDTO {

    @ApiModelProperty(value = "型号id")
    @NotNull(message = "COMMON_MODEL_ID_NULL")
    private Integer modelId;

    @ApiModelProperty(value = "材质id")
    @NotNull(message = "COMMON_MATERIAL_ID_NULL")
    private Integer materialId;

    @ApiModelProperty(value = "图片id")
    @NotNull(message = "COMMON_PICTURE_ID_NULL")
    private Integer pictureId;

    @ApiModelProperty(value = "生产图")
    @NotBlank(message = "P_PICTURE_GENERATE_IMAGE_NULL")
    private String generateImage;

    @ApiModelProperty(value = "预览图")
    @NotBlank(message = "P_PICTURE_PREVIEW_IMAGE_NULL")
    private String previewImage;

    @ApiModelProperty(value = "下单数量")
    @NotNull(message = "ORDER_ITEM_COUNT_NULL")
    @Min(value = 1L,message = "ORDER_ITEM_COUNT_MUST_GREATER_THAN_ZERO")
    private Integer itemCount;

    @ApiModelProperty(value = "是否赠品 1 非赠品 2 赠品")
    @NotNull(message = "ORDER_ITEM_TYPE_NULL")
    private Short itemType;

    @ApiModelProperty(value = "销售单价、两位小数")
    @NotNull(message = "ORDER_ITEM_SALE_PRICE_NULL")
    @Min(value = 0L,message = "ORDER_ITEM_SALE_PRICE_CANNOT_LESS_THAN_ZERO")
    private BigDecimal salePrice;


    @ApiModelProperty(value = "实际单价、实际价格、两位小数")
    @NotNull(message = "ORDER_ITEM_ACTUAL_PRICE_NULL")
    @Min(value = 0L,message = "ORDER_ITEM_ACTUAL_PRICE_CANNOT_LESS_THAN_ZERO")
    private BigDecimal actualPrice;

    @ApiModelProperty(value = "定制类型 0、标准定制 1、DIY定制")
    @NotNull(message = "COMMON_DIY_TYPE_NULL")
    private Short diyType;
}
