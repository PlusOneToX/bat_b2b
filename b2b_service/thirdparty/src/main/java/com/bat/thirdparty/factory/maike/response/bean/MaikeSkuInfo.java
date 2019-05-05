package com.bat.thirdparty.factory.maike.response.bean;

/**
 * 麦客产品
 */
public class MaikeSkuInfo {
    
    /**
     * 产品id
     */
    private Long id;

    /**
     * 麦客sku编码
     */
    private String sku_number;

    /**
     * 区域，1：成品，3：材料
     */
    private Integer area;

    /**
     * 类型
     */
    private String type;


    /**
     * 形态
     */
    private String shape;


    /**
     * 名称
     */
    private String name;

    /**
     * 规格
     */
    private String specification;


    /**
     * 特性
     */
    private String feature;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku_number() {
        return sku_number;
    }

    public void setSku_number(String sku_number) {
        this.sku_number = sku_number;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    
}