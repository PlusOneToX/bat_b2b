package com.platform.modules.app.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lipengjun
 * @since 2020-08-13 02:59:41
 */
@Data
public class AppleUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 苹果用户唯一标识符
     */
    private String openId;
    /**
     * 用户共享的可选电子邮件
     */
    private String email;
    /**
     * 验证数据
     */
    private String authorizationCode;
    /**
     * Web令牌(JWT)
     */
    private String identityToken;
    /**
     * 标识用户是否为真实的人 0：当前平台不支持，忽略该值；1：无法确认；2：用户真实性非常高
     */
    private Integer realUserStatus;
    /**
     * 用户共享的可选全名
     */
    private FullName fullName;

    @Data
    public static class FullName implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 姓
         */
        private String familyName;
        /**
         * 名字
         */
        private String giveName;
        /**
         * 名字
         */
        private String givenName;
    }
}
