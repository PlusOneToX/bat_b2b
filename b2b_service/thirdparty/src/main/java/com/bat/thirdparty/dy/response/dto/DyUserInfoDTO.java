package com.bat.thirdparty.dy.response.dto;

import lombok.Data;


@Data
public class DyUserInfoDTO {
    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
    private Watermark watermark;

    @Data
    public class Watermark {
        private String appid;
        private Long timestamp;
    }

}
