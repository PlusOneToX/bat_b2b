package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/19 14:00
 */
public class OrderGoodsDiyLabelCmd implements Serializable {
    /**
     * 订单定制信息明细id
     */
    private Integer id;


    private Integer labelId;

    /**
     * 标签存放路径
     */
    private String labelUrl;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabelUrl() {
        return labelUrl;
    }

    public void setLabelUrl(String labelUrl) {
        this.labelUrl = labelUrl;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }
}
