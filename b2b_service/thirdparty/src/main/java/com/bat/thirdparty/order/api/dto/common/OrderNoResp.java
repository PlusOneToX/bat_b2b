package com.bat.thirdparty.order.api.dto.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderNoResp implements Serializable {

    private static final long serialVersionUID = -1203130654472739189L;

    private Integer orderId;


     private String orderNo;


    /**
     * 历史原因、基于编码回传第三方bat单号是这个
     */
    private String orderNo;

     private String thirdOrderNo;

    /**
     * 历史数据结构
     */
    private OrderData data;

     private Integer code=200;

     private String userMsg="订单推送成功";

     private String msg="SUCCESS";


     public static class OrderData{

         private Integer orderId;


         private String orderNo;

         /**
          * 历史原因、基于编码回传第三方bat单号是这个
          */
         private String orderNo;

         /**
          * 第三方传过来的订单编码
          */
         private String thirdOrderNo;

         public Integer getOrderId() {
             return orderId;
         }

         public void setOrderId(Integer orderId) {
             this.orderId = orderId;
         }

         public String getOrderNo() {
             return orderNo;
         }

         public void setOrderNo(String orderNo) {
             this.orderNo = orderNo;
         }

         public String getorderNo() {
             return orderNo;
         }

         public void setorderNo(String orderNo) {
             this.orderNo = orderNo;
         }

         public String getThirdOrderNo() {
             return thirdOrderNo;
         }

         public void setThirdOrderNo(String thirdOrderNo) {
             this.thirdOrderNo = thirdOrderNo;
         }
     }
}
