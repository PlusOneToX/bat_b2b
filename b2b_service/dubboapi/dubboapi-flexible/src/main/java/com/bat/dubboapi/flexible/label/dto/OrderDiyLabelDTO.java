package com.bat.dubboapi.flexible.label.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDiyLabelDTO implements Serializable {

    private static final long serialVersionUID = 8909816386783137698L;
    /**
     * order_goods_diy主键
     */
    private Integer id;


    private Integer orderId;

    /**
     * 标签id
     */
    private Integer labelId;

    /**
     * 生成标签的url
     */
    private String labelUrl;
}
