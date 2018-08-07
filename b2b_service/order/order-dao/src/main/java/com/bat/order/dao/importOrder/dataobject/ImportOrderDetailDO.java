package com.bat.order.dao.importOrder.dataobject;

public class ImportOrderDetailDO {
    private Long id;

    private Integer importOrderGoodsId;

    private Integer importOrderId;

    private Integer materialId;

    private String materialName;

    private Integer modelId;

    private String modelName;

    private Integer brandId;

    private String brandName;

    private Integer pictureId;

    private Integer categoryId;

    private String generateImage;

    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getImportOrderGoodsId() {
        return importOrderGoodsId;
    }

    public void setImportOrderGoodsId(Integer importOrderGoodsId) {
        this.importOrderGoodsId = importOrderGoodsId;
    }

    public Integer getImportOrderId() {
        return importOrderId;
    }

    public void setImportOrderId(Integer importOrderId) {
        this.importOrderId = importOrderId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getGenerateImage() {
        return generateImage;
    }

    public void setGenerateImage(String generateImage) {
        this.generateImage = generateImage == null ? null : generateImage.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }
}