package com.bat.thirdparty.distributor.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 13:41
 */
@Data
@ApiModel(description = "相关信息")
public class SamsungCheckTokenQry {
    private String authorization;
    private String xOspAppId;
    private String xOspClientosversion;
    private String xOspClientmodel;
    private String xOspPackagename;
    private String xOspPackageversion;
    private String authToken;
}
