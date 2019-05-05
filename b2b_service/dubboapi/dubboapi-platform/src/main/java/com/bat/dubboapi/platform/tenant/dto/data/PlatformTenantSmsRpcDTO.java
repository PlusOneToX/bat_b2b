package com.bat.dubboapi.platform.tenant.dto.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PlatformTenantSmsRpcDTO implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 平台租户id
     */
    private Integer tenantId;

    /**
     * 平台租户编码
     */
    private String tenantNo;

    /**
     * 短信平台类型：1 阿里云
     */
    private Short smsType;

    /**
     * 短信平台签名
     */
    private String sign;

    /**
     * 短信平台key
     */
    private String accessKeyId;

    /**
     * 短信平台secret
     */
    private String accessKeySecret;

    /**
     * 验证码长度
     */
    private Integer verifyCodeLength;

    /**
     * 验证码有效时间
     */
    private Integer codeVerifyTime;

    /**
     * 前端验证码倒计时
     */
    private Integer verifyCodeCountDown;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 短信模板id
     */
    private List<TenantSmsTemplate> tenantSmsTemplates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantNo() {
        return tenantNo;
    }

    public void setTenantNo(String tenantNo) {
        this.tenantNo = tenantNo;
    }

    public Short getSmsType() {
        return smsType;
    }

    public void setSmsType(Short smsType) {
        this.smsType = smsType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public Integer getVerifyCodeLength() {
        return verifyCodeLength;
    }

    public void setVerifyCodeLength(Integer verifyCodeLength) {
        this.verifyCodeLength = verifyCodeLength;
    }

    public Integer getCodeVerifyTime() {
        return codeVerifyTime;
    }

    public void setCodeVerifyTime(Integer codeVerifyTime) {
        this.codeVerifyTime = codeVerifyTime;
    }

    public Integer getVerifyCodeCountDown() {
        return verifyCodeCountDown;
    }

    public void setVerifyCodeCountDown(Integer verifyCodeCountDown) {
        this.verifyCodeCountDown = verifyCodeCountDown;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<TenantSmsTemplate> getTenantSmsTemplates() {
        return tenantSmsTemplates;
    }

    public void setTenantSmsTemplates(List<TenantSmsTemplate> tenantSmsTemplates) {
        this.tenantSmsTemplates = tenantSmsTemplates;
    }

    public static class TenantSmsTemplate implements Serializable {

        /**
         * id
         */
        private Integer id;

        /**
         * 平台租户短信配置id
         */
        private Integer platformTenantSmsId;

        /**
         * 短信模板类型：1 注册申请 2 B端验证码修改密码 3 C端修改手机号 4 C端客户验证码登录 5 C端验证码修改密码 6 分账业务员绑定 7 B端客户验证码登录
         */
        private Short templateType;

        /**
         * 短信平台模板码
         */
        private String templateCode;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPlatformTenantSmsId() {
            return platformTenantSmsId;
        }

        public void setPlatformTenantSmsId(Integer platformTenantSmsId) {
            this.platformTenantSmsId = platformTenantSmsId;
        }

        public Short getTemplateType() {
            return templateType;
        }

        public void setTemplateType(Short templateType) {
            this.templateType = templateType;
        }

        public String getTemplateCode() {
            return templateCode;
        }

        public void setTemplateCode(String templateCode) {
            this.templateCode = templateCode;
        }
    }
}