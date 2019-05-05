package com.bat.thirdparty.vmall.response;

import lombok.Data;

/**
 * 沙漠
 */
@Data
public class BopAddressDecryptResponse {

    private OrderConsignee data;
    private String info;
    private String resultCode;
    private boolean success;

    @Data
    public static class OrderConsignee {
        private String address;
        private String consignee;
        private String email;
        /**
         * 收货人手机
         */
        private String mobile;
        private String ocid;
        private String orderCode;
        /**
         * 收货人电话
         */
        private String phone;
        /**
         * 返回的是否是收货人虚拟手机
         */
        private boolean useVirtualMobile;
        /**
         * 收货人虚拟手机
         */
        private String visualMobile;
        /**
         * 本条收货人信息是否有效。ocid是否和orderCode当前的ocid匹配。true：匹配，false：不匹配。当不匹配时，建议通过订单查询接口获取最新的ocid。
         */
        private boolean valid;

        private String country;

        private String province;

        private String city;

        private String district;

        private String street;

    }

}