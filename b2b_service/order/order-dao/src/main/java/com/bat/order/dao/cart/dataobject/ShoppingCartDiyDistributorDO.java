package com.bat.order.dao.cart.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class ShoppingCartDiyDistributorDO {
    private Integer id;

    private Integer shoppingCartDistributorId;

    private Integer categoryId;

    private String categoryName;

    private Integer materialId;

    private String materialName;

    private String manufactors;

    private Integer modelId;

    private String modelName;

    private Integer brandId;

    private String brandName;

    private Integer pictureId;

    private String pictureName;

    private String generateImage;

    private String previewImage;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

}