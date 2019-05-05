package com.bat.thirdparty.vmall.response;

import lombok.Data;

import java.util.List;

@Data
public class BopShipmentLisResponse {

    private ShipmentData data;
    private boolean success;
    private String resultCode;
    private String info;

    @Data
    public static class ShipmentData {
        List<ShipmentInfo> shipmentInfoList;
    }

    @Data
    public static class ShipmentInfo {
        private BasicInfo basicInfo;
    }

    @Data
    public static class BasicInfo {

        private String shipmentId;
        private String shipmentNo;
        private String orderNo;

        private String status;

        private String sendTime;
    }

}
