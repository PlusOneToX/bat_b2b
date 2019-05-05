package com.bat.thirdparty.factory.haixing.bak;

/**
 * 海星商品对象
 * 订单同步到海星（脚本类-放同一个包方便管理）
 */
public class HaiXingGood {
    private String skuCode;

    private Long goodsNum;

    private String goodsImg;

    private String diyFileUrl;

    /** 定制数据（JSON字符串）*/
    private String goodsCustomization;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getDiyFileUrl() {
        return diyFileUrl;
    }

    public void setDiyFileUrl(String diyFileUrl) {
        this.diyFileUrl = diyFileUrl;
    }

    public String getGoodsCustomization() {
        return goodsCustomization;
    }

    public void setGoodsCustomization(String goodsCustomization) {
        this.goodsCustomization = goodsCustomization;
    }
}
