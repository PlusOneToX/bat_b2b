package com.bat.financial.api.platformaccount.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 13:46
 */
@Data
@ApiModel(value = "AccountAlipayCreateCmd", description = "平台收款账户(支付宝)新增")
public class AccountAlipayCreateCmd {

    @NotNull(message = "P_ACCOUNT_ALIPAY_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "销售组织id", required = true, example = "1")
    private Integer organizationId;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "收款账户名称", required = true, example = "")
    private String accountName;

    @ApiModelProperty(value = "erp账户编码", required = false, example = "00001")
    private String erpAccountNo;

    @NotBlank(message = "P_ACCOUNT_WXPAY_BANK_NO_NULL")
    @ApiModelProperty(value = "erp银行账号", required = true, example = "10863000000410707")
    private String bankNo;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_APP_ID_NULL")
    @ApiModelProperty(value = "支付宝应用appid", required = true, example = "2018022162645163")
    private String appId;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_APP_KEY_NULL")
    @ApiModelProperty(value = "支付宝公钥", required = true,
        example = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArUl6zkBFBJ2o78ZmOdimORJV2uvtCYB2/xDIr1DXCxalsOCGzBQQM7stIv8JTHWHJNvlBl1PZ7khASS20CUIGLZVa0JVHTGWbV7AKCe1WAFUIJnpzN5ZSx+G1nYt/yCsOBqpGbdQkCzsOvWlJcbNIu1uneybY2wxcqHgUQSoG2Du1UBelABSG/ESvpEDA6co9Y80FvApGB0fHXUire6SY1Wq5jE3z0+nlw+Zpsv9Ivr3bOplHu6eLJFYw29Oxc6k0+yfcVK9xJ9krXjTE6V7Qi41zOCWTwapXDVGSNLFsQruf76H3GW4PoI2SBf4pRMJ47oSSqOrVRxz5kbzoGH6pwIDAQAB")
    private String appPublicSecret;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_APP_KEY_NULL")
    @ApiModelProperty(value = "支付宝私钥", required = true,
        example = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLvHFmgV5xoOK3m0sK0DxnDlF4OwOEwJa9/5v8MRha8b9eHylqAJea2NXGeyodHITMc8jOLmIFyomfdg1vwxLhqF2YUc+IsJj4WsyZ69LdjkUbqYQNi7/IQK5kpSC/hJernPfbdQdXETEGXtRh3cskDGlzC0iiN4FsfD6NX51hDemFgMx+fG4Wsv8/xdle1dCjbfNwXGZDsa78TVZ2SG3lDecTMBoZHOxFpi34f45816GYk/t2UgyFs/euuagUxsaS9YLsoLsQM+DVxOBqSaorbpfO/PlEB4BIFKcfmltyx9wcH2kyN7J1WGprCE8utmXFDlqKD6B3RGLk3LuU3JqzAgMBAAECggEAWFMt5n6XbzOMloTgl6OHTDZM447SC69iqqQx/7dZE7RSfn+L+MSg9SSkKRQkQdKnT+uyVpCNNm2EFHmdgaJUgTf4u5xcGKpW5vSWVvSBykNvenqXIThAwJQqBr//3oehKWk3mxC1Se3ODzD1aig5dTtcS9/McWcW2O1UQP3+wJ/8wv+hZX6W92V5IsysWeB9gmrMdEq+FIC3Q4R2dp47DecZr/6+EROMMdRVuAg1pNuJp/1YfN52gzNH8wqST6gRDXLe/hlAxONIA5+BXtO+qW9t+mowGRzSMrJHcRUCcKeNJybpv7HmJ+vxO5w/0O+2ud3cqAFFGpEjcS4y6j3JQQKBgQD3WZ8YY9ck2QQMGGFsTAeiGCEn/cQySJrCzbVyc8xyW/tUF+w43ibhij30VK1u8rZyF+SAOaKW2z8E2mrXP1x43vyDnNnRWpLsJHUQ32Wiblr/T8qYLdugpKwl8o3zFFy1RGpz3EBNdhc6SOCPPLzfMnS/+oaTgxGa2HSPKdJsCwKBgQDS3F8WRRopy5LCyMSgoBQ3GIGa6JB4CIH3fy8He4EMniMe5rfE+cjDSUKaMNUQP588FdUj2nuaH9zTogH//cvAlqLIcsVNIKudqciTVTcp5Rlwi/QviVB/YHm/v8SLyEDb9fsWEtxrSbJYwXrLLepRwhjOXvh6zwUQLo8iEgMM+QKBgB8vro59sBeWQRthS49C+sdmMXAAX+dTHignlZQLo6BblpGgvojhAZf4PR18O628bmJuPsIoAibxF7395/ChrYAT5VSWxyPNPq8FXbk14XOcZF7CabHMPl+/w3C5Z0pZq+Ky8JURFQ5vMDiKDb4hk1K04uF9rdAVe3fBiNiwFjbHAoGBAMqBUeC1ZLcXB0+COTR6CHARXQdEqtpccVUcFDJ/biSMCva2ZW8K5MCILyqeE0GTmH3ACf5w6ZvPVej6AimzuGaxRIx1jP7RjX/RewvkHyJhH9lyz4SHv4NVSBfbOAt2urOEvSsp2f4/vMR4rFFNO2hhnCk2FDT9dMNhs5zo530hAoGBAPHzr/RcTTCjH8kGz1xd6ETGE1sF+VXfk87WP1y0hRRf6p1wVT1VFqWtXSa0zq9Ua+zrbeSpgAG0TnSIC0O73h26+JcSSQJpKc5YiRLtkkEr7wmiLiyXgAuPtkLlMPnS89AmB5kmUKmgLY9YGAkYblPSgpXxWLD+Vv+PzSmnpOhf")
    private String appPrivateSecret;

    @NotNull(message = "P_ACCOUNT_ALIPAY_BACK_TYPE_NULL")
    @ApiModelProperty(value = "取消订单是否原路返回 1、是 2、否", required = true, example = "1")
    private Short backType;

    @NotNull(message = "P_ACCOUNT_ALIPAY_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用,0停用", required = true, example = "1")
    private Short openFlag;

}
