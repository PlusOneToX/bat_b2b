package com.bat.thirdparty.factory.haixing.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 海星提交订单对象 订单同步到海星（脚本类-放同一个包方便管理）
 * 
 * @author Administrator
 */
@NoArgsConstructor
@Data
public class HaiXingOrderResp implements Serializable {

    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private ResultDTO result;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("code")
    private String code;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("records")
        private Integer records;
        @JsonProperty("rows")
        private List<RowsDTO> rows;

        @NoArgsConstructor
        @Data
        public static class RowsDTO {
            @JsonProperty("orderId")
            private Integer orderId;
            @JsonProperty("sellerOrderNo")
            private String sellerOrderNo;
            @JsonProperty("orderNo")
            private String orderNo;
            @JsonProperty("orderStateId")
            private Integer orderStateId;
            @JsonProperty("memo")
            private Object memo;
            @JsonProperty("createTime")
            private String createTime;
            @JsonProperty("contacts")
            private Object contacts;
            @JsonProperty("mobileNumber")
            private Object mobileNumber;
            @JsonProperty("address")
            private Object address;
            @JsonProperty("provinceId")
            private Object provinceId;
            @JsonProperty("cityId")
            private Object cityId;
            @JsonProperty("countyId")
            private Object countyId;
            @JsonProperty("storeName")
            private String storeName;
            @JsonProperty("storeCode")
            private String storeCode;
            @JsonProperty("goods")
            private List<GoodsDTO> goods;

            @NoArgsConstructor
            @Data
            public static class GoodsDTO {
                @JsonProperty("orderId")
                private Integer orderId;
                @JsonProperty("order2goodsId")
                private Integer order2goodsId;
                @JsonProperty("order2goodsStatus")
                private Integer order2goodsStatus;
                @JsonProperty("goodsName")
                private String goodsName;
                @JsonProperty("goodsId")
                private Integer goodsId;
                @JsonProperty("brandName")
                private String brandName;
                @JsonProperty("goodsNum")
                private Integer goodsNum;
                @JsonProperty("specId")
                private Integer specId;
                @JsonProperty("goodsImg")
                private String goodsImg;
                @JsonProperty("specName")
                private String specName;
                @JsonProperty("skuCode")
                private String skuCode;
                @JsonProperty("lcOrderno")
                private Object lcOrderno;
                @JsonProperty("lcName")
                private Object lcName;
            }
        }
    }
}
