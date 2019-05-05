package com.bat.dubboapi.flexible.order.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsDiyParamDTO implements Serializable {
    private static final long serialVersionUID = 4209162406583423442L;
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

    /**
     * 物料编码
     */
    private String itemCode;

    /**
     * 数量
     */
    private Integer itemCount;
}
