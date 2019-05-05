package com.bat.thirdparty.factory.haixing.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:28
 */
@Data
public class HaixingAddOrderResp {

    /**
     * 接口执行状态：成功：true，失败：false
     */
    private Boolean success;
    /**
     * 执行失败的异常消息
     */
    private String msg;
    /**
     * 异常代码
     */
    private String code;
    /**
     * 执行成功返回的数据
     */
    private Result result;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Result {

        /**
         * 商家单号（唯一）
         */
        private String sellerOrderNo;
        /**
         * 海星订单号
         */
        private String orderNo;
        /**
         * 订单ID
         */
        private Integer orderId;
        /**
         * 商品数据数组
         */
        private List<Goods> goods;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Goods {
            /**
             * 是 海星订单明细ID
             */
            private Integer order2goodsId;
            /**
             * 是 SKU编码
             */
            private String skuCode;
            /**
             * 是 商品数量
             */
            private Integer goodsNum;
        }
    }
}