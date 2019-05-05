package com.bat.dubboapi.flexible.label.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsDiySimpleDTO implements Serializable {
    private static final long serialVersionUID = 3161243096207028901L;

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