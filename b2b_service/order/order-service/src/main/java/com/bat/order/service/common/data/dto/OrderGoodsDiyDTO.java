package com.bat.order.service.common.data.dto;

import lombok.Data;

@Data
public class OrderGoodsDiyDTO {
    /**
     * 产品类型id
     */
    private Integer categoryId;
    /**
     * 产品类型名称
     */
    private String categoryName;
    /**
     * 材质id
     */
    private Integer materialId;
    /**
     * 材质名称
     */
    private String materialName;
    /**
     * 生产商 YC云创 MK麦客 LHW联辉王
     */
    private String manufactors;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 型号id
     */
    private Integer modelId;
    /**
     * 型号名称
     */
    private String modelName;
    /**
     * 图片id（网络图传0）
     */
    private Integer pictureId;
    /**
     * 图片名称
     */
    private String pictureName;
    /**
     * 生产图URL地址
     */
    private String generateImage;
    /**
     * 预览图URL地址
     */
    private String previewImage;
}
