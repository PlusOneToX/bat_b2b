package com.bat.thirdparty.factory.maike.request.order;

import java.math.BigDecimal;


public class MaikeDetailInfo {
    
    /**
     * 
     */
    private String area="";


    private String sku_number;


    /**
     * 支付宝物料号
     */
    private String alipay_sku_number="";


    /**
     * 物流公司
     */
    private String logistic_company="";



    private Integer is_exist_avatar;

    /**
     * 文件路径
     */
    private String file_path;

    /**
     * 效果文件路径
     */
    private String effect_file_path;

    /**
     * 标签文件路径
     */
    private String label_file_path;

    /**
     * 文件路径本地
     */
    private String file_local_path;

    /**
     * 效果文件路径本地
     */
    private String effect_file_local_path;

    /**
     * 标签文件路径本地
     */
    private String label_file_local_path;


    /**
     * 产品名称
     */
    private String product_name;


    /**
     * 规格
     */
    private String specification;

    /**
     * 单位
     */
    private String unit="个";

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 描述
     */
    private String description="";

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSku_number() {
        return sku_number;
    }

    public void setSku_number(String sku_number) {
        this.sku_number = sku_number;
    }

    public String getAlipay_sku_number() {
        return alipay_sku_number;
    }

    public void setAlipay_sku_number(String alipay_sku_number) {
        this.alipay_sku_number = alipay_sku_number;
    }

    public String getLogistic_company() {
        return logistic_company;
    }

    public void setLogistic_company(String logistic_company) {
        this.logistic_company = logistic_company;
    }

    public Integer getIs_exist_avatar() {
        return is_exist_avatar;
    }

    public void setIs_exist_avatar(Integer is_exist_avatar) {
        this.is_exist_avatar = is_exist_avatar;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEffect_file_path() {
        return effect_file_path;
    }

    public void setEffect_file_path(String effect_file_path) {
        this.effect_file_path = effect_file_path;
    }

    public String getLabel_file_path() {
        return label_file_path;
    }

    public void setLabel_file_path(String label_file_path) {
        this.label_file_path = label_file_path;
    }

    public String getFile_local_path() {
        return file_local_path;
    }

    public void setFile_local_path(String file_local_path) {
        this.file_local_path = file_local_path;
    }

    public String getEffect_file_local_path() {
        return effect_file_local_path;
    }

    public void setEffect_file_local_path(String effect_file_local_path) {
        this.effect_file_local_path = effect_file_local_path;
    }

    public String getLabel_file_local_path() {
        return label_file_local_path;
    }

    public void setLabel_file_local_path(String label_file_local_path) {
        this.label_file_local_path = label_file_local_path;
    }



    
}