package com.bat.thirdparty.order.api.dto.provisional;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @date 2019/06/9
 * @time 15:53
 */
@Data
public class ProvisionalOrderDetail implements Serializable {
    private static final long serialVersionUID = -6367389450166334426L;
    /**
     * 保存訂單詳情圖片id
     */
    @NotBlank(message = "T_ORDER_PREVIEW_IMAGE_NULL")
    private String image;


    private Integer materialId;
    private Integer modelId;
    private String modelName;
    private Integer brandId;
    private String brandName;

    @NotBlank(message = "T_ORDER_GENERATE_IMAGE_NULL")
    private String generateImage;


    private Integer pictureId;
    private BigDecimal price;
    private String skuNo;
    private String skuId;

    @NotNull(message = "T_ORDER_COUNT_NULL")
    @Min(value = 1L,message = "T_NUMBER_ILLEGAL")
    private Integer count;
    private BigDecimal totalPrice;


}
