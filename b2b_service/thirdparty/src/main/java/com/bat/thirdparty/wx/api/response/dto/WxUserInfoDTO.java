package com.bat.thirdparty.wx.api.response.dto;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 11:20
 */
@Data
public class WxUserInfoDTO {
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
