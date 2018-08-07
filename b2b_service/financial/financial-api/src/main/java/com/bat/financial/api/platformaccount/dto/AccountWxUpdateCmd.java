package com.bat.financial.api.platformaccount.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 13:46
 */
@Data
@ApiModel(value = "AccountWxUpdateCmd", description = "平台收款账户(微信)更新")
public class AccountWxUpdateCmd {

    @NotNull(message = "P_ACCOUNT_WXPAY_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_ACCOUNT_WXPAY_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "销售组织id", required = true, example = "1")
    private Integer organizationId;

    @NotBlank(message = "P_ACCOUNT_WXPAY_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "收款账户名称", required = true, example = "")
    private String accountName;

    @ApiModelProperty(value = "erp账户编码", required = false, example = "00001")
    private String erpAccountNo;

    @NotBlank(message = "P_ACCOUNT_WXPAY_BANK_NO_NULL")
    @ApiModelProperty(value = "erp银行账号", required = true, example = "1341583701@1341583701")
    private String bankNo;

    @NotBlank(message = "P_ACCOUNT_WXPAY_APP_ID_NULL")
    @ApiModelProperty(value = "微信应用appid", required = true, example = "wx63c410d5de384240")
    private String appId;

    // @NotBlank(message = "P_ACCOUNT_WXPAY_APP_ID_NULL")
    @ApiModelProperty(value = "商户API证书私钥",
        example = "-----BEGIN PRIVATE KEY-----\n" + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDIpB+Gr0B4LtT4\n"
            + "I1dzD67lUhhUlSD9or1rmJ4VsdsuLn21JjelPVoYcNthkjGnvRfUq5GU1rB4+3Cs\n"
            + "VlRFloIEO2FKMA3VFkr3rlZ2f7ldyNM2CyU2Znphb9EPnz9RJyqqKfkipvYZZkVK\n"
            + "h+ZojtxrmotZn8zs6xG67aOCbCQKJg0KizISWpjsKPeKqW1D9UmjENwSPev6iIcs\n"
            + "AGRlSCs0kVJkhNoyOw70YwfZHX21MSOGbqtxclKXFEFvI7ZP8WAxZf2+PFtWASSH\n"
            + "ZZ1bix5voF40CQCzSEHp+PIOD0XwBulRnxYU+zj/FnFOE2g5Gc9QhMpbgSXmAb5Y\n"
            + "Rt/2GixjAgMBAAECggEAZpKBC3nMm6RVfAWVT11VLCcsCRG0K/Zmn/sF1lF2m8Vo\n"
            + "vpMrH26eAKCxhtAWUop5BGAxrMWUskTY9UGtE2Y+UauJsUOfzq/242hdC3eAbFGe\n"
            + "qKJKA+Wf6vAfEL14wSQky5lppC124sVF5zRStmFSjjATzV3Ehdom1klmreZbBlRF\n"
            + "u3+AqqSnT6421vqL4YhTn97IGnLhZu4Z9D5p1nQYuxkVEf6OZCXoZVPZozTpYLq3\n"
            + "R6D3fq6qfueUHU/MZIWxBaqP1L50Uv+hYY5mkpyH1CHaus67xZxh+rI4Vxp3Nfkx\n"
            + "+UuB9tRqeMeCW41IKMH+Ulby9lkutTDQgzPes/uoAQKBgQDqTxMkl4mhBcvN41tQ\n"
            + "Qlmpq4IFobv1hRYsx5Ex/QSlPOGTCQmLa5Lw6aKvRBdXhvY7pDhfmcpMvieVs4na\n"
            + "eFV9fMIsYWtP8DLMnh71cSlyeoneRbPe+hn6CPi0fyXUgL3rEpqzOKV5pNN+L9a9\n"
            + "ikw1vaROxSVm7CTWPxVWNw71QwKBgQDbNyZqj1B244KFyx1vMONZPdXtuC/W9urj\n"
            + "ix/9rbhIPEHW/XDKefh29QTQ8s3Q7SjfjW1ROWmaZ5RCEWS58eCsF/Gm0s4Kh+J7\n"
            + "WxigU/w+Sljkcwc0o7lZEAli5ZGOKTr+1qGkIGG36qDgwbGDZKO8I3aR+wDAFrfx\n"
            + "PJ0bacDqYQKBgQCwr+hzk4JJVqGVJHlbUG1ksNxfetLOFgnewq8OzwmNwRcpVYtJ\n"
            + "A1nPKZ9wIxym+DL+TpvkG4idzlbzYGv3vv5Ynped9+iAY1UrT6Fw3GCw3w7P+ifl\n"
            + "YRvtnI5PLgJM+Lg+Z80YkQLXdB+37EM2su89J0Cb1baaokA0/2ULmoFBGQKBgCiY\n"
            + "Bd0lvNjHOJxeCI1WmEMmWD81JGbCQqzwTnRDfyShQ+/z7rcP7xrYNJQc4lAWGsOL\n"
            + "BPtNjmixVZsGbYihaeF0baNe1hjNIPuG2wCVjNhcHCnxwxvUVbZQaeyOZm3U5L/c\n"
            + "LwKdnHraUABO3fhe4pMsXgCaYoEvs9BQUSeKZdcBAoGBAI8LVD8PhD2Kgvgom4yu\n"
            + "ug27fs/ShmIUJOws2uRNem10gPQi+d382H7byKkqurUVJtFemh6iKjUNRU2J00uJ\n"
            + "YrLmyHlap3ewTLdsPXxCfdBtxAj/4MtZ/DcXX9Kdi9rySPlIRjdm5CbT9lXhrWX+\n" + "D4T5YZqHj2wLXwLQtmhzXxgE\n"
            + "-----END PRIVATE KEY-----\n" + "-----BEGIN PRIVATE KEY-----\n"
            + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDIpB+Gr0B4LtT4\n"
            + "I1dzD67lUhhUlSD9or1rmJ4VsdsuLn21JjelPVoYcNthkjGnvRfUq5GU1rB4+3Cs\n"
            + "VlRFloIEO2FKMA3VFkr3rlZ2f7ldyNM2CyU2Znphb9EPnz9RJyqqKfkipvYZZkVK\n"
            + "h+ZojtxrmotZn8zs6xG67aOCbCQKJg0KizISWpjsKPeKqW1D9UmjENwSPev6iIcs\n"
            + "AGRlSCs0kVJkhNoyOw70YwfZHX21MSOGbqtxclKXFEFvI7ZP8WAxZf2+PFtWASSH\n"
            + "ZZ1bix5voF40CQCzSEHp+PIOD0XwBulRnxYU+zj/FnFOE2g5Gc9QhMpbgSXmAb5Y\n"
            + "Rt/2GixjAgMBAAECggEAZpKBC3nMm6RVfAWVT11VLCcsCRG0K/Zmn/sF1lF2m8Vo\n"
            + "vpMrH26eAKCxhtAWUop5BGAxrMWUskTY9UGtE2Y+UauJsUOfzq/242hdC3eAbFGe\n"
            + "qKJKA+Wf6vAfEL14wSQky5lppC124sVF5zRStmFSjjATzV3Ehdom1klmreZbBlRF\n"
            + "u3+AqqSnT6421vqL4YhTn97IGnLhZu4Z9D5p1nQYuxkVEf6OZCXoZVPZozTpYLq3\n"
            + "R6D3fq6qfueUHU/MZIWxBaqP1L50Uv+hYY5mkpyH1CHaus67xZxh+rI4Vxp3Nfkx\n"
            + "+UuB9tRqeMeCW41IKMH+Ulby9lkutTDQgzPes/uoAQKBgQDqTxMkl4mhBcvN41tQ\n"
            + "Qlmpq4IFobv1hRYsx5Ex/QSlPOGTCQmLa5Lw6aKvRBdXhvY7pDhfmcpMvieVs4na\n"
            + "eFV9fMIsYWtP8DLMnh71cSlyeoneRbPe+hn6CPi0fyXUgL3rEpqzOKV5pNN+L9a9\n"
            + "ikw1vaROxSVm7CTWPxVWNw71QwKBgQDbNyZqj1B244KFyx1vMONZPdXtuC/W9urj\n"
            + "ix/9rbhIPEHW/XDKefh29QTQ8s3Q7SjfjW1ROWmaZ5RCEWS58eCsF/Gm0s4Kh+J7\n"
            + "WxigU/w+Sljkcwc0o7lZEAli5ZGOKTr+1qGkIGG36qDgwbGDZKO8I3aR+wDAFrfx\n"
            + "PJ0bacDqYQKBgQCwr+hzk4JJVqGVJHlbUG1ksNxfetLOFgnewq8OzwmNwRcpVYtJ\n"
            + "A1nPKZ9wIxym+DL+TpvkG4idzlbzYGv3vv5Ynped9+iAY1UrT6Fw3GCw3w7P+ifl\n"
            + "YRvtnI5PLgJM+Lg+Z80YkQLXdB+37EM2su89J0Cb1baaokA0/2ULmoFBGQKBgCiY\n"
            + "Bd0lvNjHOJxeCI1WmEMmWD81JGbCQqzwTnRDfyShQ+/z7rcP7xrYNJQc4lAWGsOL\n"
            + "BPtNjmixVZsGbYihaeF0baNe1hjNIPuG2wCVjNhcHCnxwxvUVbZQaeyOZm3U5L/c\n"
            + "LwKdnHraUABO3fhe4pMsXgCaYoEvs9BQUSeKZdcBAoGBAI8LVD8PhD2Kgvgom4yu\n"
            + "ug27fs/ShmIUJOws2uRNem10gPQi+d382H7byKkqurUVJtFemh6iKjUNRU2J00uJ\n"
            + "YrLmyHlap3ewTLdsPXxCfdBtxAj/4MtZ/DcXX9Kdi9rySPlIRjdm5CbT9lXhrWX+\n" + "D4T5YZqHj2wLXwLQtmhzXxgE\n"
            + "-----END PRIVATE KEY-----\n")
    private String apiclientKey;

    // @NotBlank(message = "P_ACCOUNT_WXPAY_APP_ID_NULL")
    @ApiModelProperty(value = "商户证书序列号", example = "3539E6018A925D3B79A2720F12222CC437B4315C")
    private String serialNumber;

    @ApiModelProperty(value = "商户证书失效时间", example = "2026-05-20 16:38:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date certificateInvalidTime;

    @NotBlank(message = "P_ACCOUNT_WXPAY_ACCOUNT_NO_NULL")
    @ApiModelProperty(value = "微信商户号", required = true, example = "1341583701")
    private String accountNo;

    @NotBlank(message = "P_ACCOUNT_WXPAY_APP_KEY_NULL")
    @ApiModelProperty(value = "微信appKey", required = true, example = "BkPKm7DhDrz2wsfWVBkcDcCbXA8SMte2")
    private String appKey;

    @NotNull(message = "P_ACCOUNT_WXPAY_APP_TYPE_NULL")
    @ApiModelProperty(value = "应用类型 1、微信公众号 2、微信小程序", required = true, example = "1")
    private Short appType;

    @NotNull(message = "P_ACCOUNT_WXPAY_BACK_TYPE_NULL")
    @ApiModelProperty(value = "取消订单是否原路返回 1、是 2、否", required = true, example = "1")
    private Short backType;

    @NotNull(message = "P_ACCOUNT_WXPAY_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用,0停用", required = true, example = "1")
    private Short openFlag;

    @NotBlank(message = "P_ACCOUNT_WXPAY_API_VERSION_NULL")
    @ApiModelProperty(value = "api接口版本", required = true, example = "V2")
    private String version;

}
