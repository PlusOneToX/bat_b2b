package com.bat.dubboapi.system.globalsetting.dto;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/8 20:10
 */
public class FactorySettingOrderInvalidRpcDTO implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 渠道名称
     */
    private String name;
    /**
     * 渠道来源
     */
    private Integer orderSource;
    /**
     * 订单时效
     */
    private Integer invalid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    @Override
    public String toString() {
        return "FactorySettingOrderInvalidRpcDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderSource=" + orderSource +
                ", invalid=" + invalid +
                '}';
    }
}
