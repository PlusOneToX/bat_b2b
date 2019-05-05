
package com.bat.thirdparty.vmall.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 沙漠
 */
@Data
public class BopOrderFulfillInfoResponse {

    private OrderData data;
    private boolean success;
    private String resultCode;
    private String info;

    @Data
    public static class OrderData {
        private String nextPosition;
        private List<OrderFulfillInfoList> orderFulfillInfoList;
    }

    @Data
    public static class OrderFulfillInfoList {
        private BigDecimal cashPay;
        private BigDecimal couponDeduct;
        private BigDecimal deliveryFee;
        private BigDecimal discount;
        private OrderBaseExtFulfillInfo orderBaseExtFulfillInfo;
        private String orderCode;
        private OrderDeliveryAddressFulfillInfo orderDeliveryAddressFulfillInfo;
        private List<OrderExtendFulfillInfoList> orderExtendFulfillInfoList;
        private long orderId;
        private List<OrderProductFulfillInfoList> orderProductFulfillInfoList;
        private BigDecimal orderSource;
        private Integer orderStatus;
        private Date orderTime;
        private Integer orderType;
        private List<PaymentFulfillInfoList> paymentFulfillInfoList;
        private Integer paymentStatus;
        private Date paymentTime;
        private BigDecimal petalPay;
        private BigDecimal poBigDecimalPay;
        private BigDecimal taxAmount;
        private String userId;
        /**
         * 收货人信息id。收货人返回的信息经过匿名化处理，商家需要获取明文信息时，经授权后，使用此参数调用ocidDecrypt接口获取收货人的信息
         */
        private String ocid;
    }

    @Data
    public static class OrderBaseExtFulfillInfo {
        private Integer crossBorderFlag;
        private String deliveryPattern;
        private String paymentPattern;
        private String storeType;
    }

    @Data
    public static class OrderDeliveryAddressFulfillInfo {
        private String address;
        private String city;
        private String cityId;
        private String consignee;
        private String district;
        private String districtId;
        private String email;
        private String firstName;
        private String lastName;
        private String middleName;
        private String mobile;
        private String phone;
        private String province;
        private String provinceId;
        private String street;
        private String streetId;
        private String zipCode;
    }

    @Data
    public static class OrderExtendFulfillInfoList {
        private String paramCode;
        private String paramValue;

    }

    @Data
    public static class OrderProductFulfillInfoList {
        private String name;
        private BigDecimal orderPrice;
        private List<OrderProductAttrFulfillInfoList> orderProductAttrFulfillInfoList;
        private String orderProductCode;
        private Integer quantity;
        private BigDecimal salePrice;
        private String skuCode;
    }

    @Data
    public static class PaymentFulfillInfoList {
        private String bankTradeNo;
        private Integer creditNum;
        private BigDecimal creditRate;
        private BigDecimal creditSellerPercent;
        private BigDecimal paymentAmount;
        private Integer paymentChannel;
        private BigDecimal paymentFee;
        private String paymentMethod;
        private String paymentNo;
        private Date paymentTime;
        private Integer paymentType;
        private BigDecimal serviceFee;
        private BigDecimal settlementAmount;
    }

    @Data
    public static class OrderProductAttrFulfillInfoList {
        private String paramCode;
        private String paramValue;

    }

}