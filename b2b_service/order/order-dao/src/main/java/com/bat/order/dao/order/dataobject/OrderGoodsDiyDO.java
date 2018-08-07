package com.bat.order.dao.order.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderGoodsDiyDO implements Serializable {
    private Integer id;

    private Integer orderId;

    private Integer orderGoodsId;

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

    private Integer labelId;

    private String labelUrl;

    private String generateImage;

    private String previewImage;

    private Date createTime;

    private Date updateTime;

}