package com.bat.dubboapi.flexible.third.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MolejiCaseCmd implements Serializable {

    /**
     * 品牌名称
     */
    private String brand;

    /**
     * 机型名称
     */
    private String model;

    /**
     * 壳名称
     */
    private String material;

    /**
     * 壳颜色
     */
    private String color;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * sku编码
     */
    private String sku;

    /**
     * 图片URL
     */
    private String imgUrl;

    /**
     * 图片MD5值
     */
    private String imgMd5;

    /**
     * 打印效果图URL
     */
    private String prevImgUrl;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgMd5() {
        return imgMd5;
    }

    public void setImgMd5(String imgMd5) {
        this.imgMd5 = imgMd5;
    }

    public String getPrevImgUrl() {
        return prevImgUrl;
    }

    public void setPrevImgUrl(String prevImgUrl) {
        this.prevImgUrl = prevImgUrl;
    }
}
