package com.bat.dubboapi.order.order.dto.factory.haixing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 海星提交订单对象 订单同步到海星（脚本类-放同一个包方便管理）
 * 
 * @author Administrator
 */
@Data
public class HaiXingOrderAddCmd implements Serializable {
    /**
     * 是 商家单号（唯一）
     */
    private String sellerOrderNo;

    /**
     * 店铺编号 00683
     */
    private String storeCode;
    /**
     * 否 商家备注
     */
    private String memo;
    /**
     * 否 收件人
     */
    private String contacts;
    /**
     * 否 联系电话
     */
    private String mobileNumber;
    /**
     * 否 省份
     */
    private String province;
    /**
     * 否 地市
     */
    private String city;
    /**
     * 否 区县 （不是国家）
     */
    private String county;
    /**
     * 否 详细地址
     */
    private String address;
    /**
     * 否 定制数据（JSON字符串） {"pic":"http://domain.com/test.jpg"}
     */
    private String orderCustomization;

    /**
     * 否 商品数据数组
     */
    private String goods;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Goods implements Serializable {
        /**
         * 是 SKU编码
         */
        private String skuCode;
        /**
         * 是 商品数量
         */
        private Integer goodsNum;
        /**
         * 否 效果图（成品缩略图）
         */
        private String goodsImg;
        /**
         * 是 定制文件URL
         */
        private String diyFileUrl;
        /**
         * 否 定制数据（JSON字符串）
         */
        private String goodsCustomization;
    }
}
