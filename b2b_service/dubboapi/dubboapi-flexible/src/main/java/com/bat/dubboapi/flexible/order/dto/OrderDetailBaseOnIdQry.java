package com.bat.dubboapi.flexible.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @date 2019/06/9
 * @time 17:09
 */
@Data
public class OrderDetailBaseOnIdQry implements Serializable {

    /**
     * 预览图
     */
    private String image;

    /**
     * 材质id
     */
    private Integer materialId;

    /**
     * 型号id
     */
    private Integer modelId;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 品牌id
     */
    private Integer brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 生产图
     */
    private String generateImage;

    /**
     * 图片id
     */
    private Integer pictureId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 80码
     */
    private String skuNo;

    /**
     * 下单数量
     */
    private Integer count;

    /**
     * 单品总价
     */
    private BigDecimal totalPrice;


    /**
     * 结算价格
     */
    private BigDecimal salePrice;



    /**
     * 拓展字段
     */
    private ExtendField extendField;


}
