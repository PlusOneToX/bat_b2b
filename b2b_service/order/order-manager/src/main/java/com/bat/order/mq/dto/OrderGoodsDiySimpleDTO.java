package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderGoodsDiySimpleDTO implements Serializable {
    /**
     * order_goods_diy主键
     */
    private Integer id;

    private Integer categoryId;

    private Integer materialId;

    private Integer modelId;

    private String modelName;

    private Integer pictureId;

    private Integer labelId;

    private String labelUrl;

    private String manufactor;

}