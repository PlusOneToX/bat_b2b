package com.bat.financial.api.distributoraccount.dto.data;

import com.bat.financial.api.distributoraccount.dto.DistributorInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:55
 */
@Data
public class AccountWxDistributorDTO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String distributorCompanyName;

    private List<DistributorInfo> distributorInfos;

    private String accountName;

    private String appId;

    private String apiclientKey;

    private String serialNumber;

    private Date certificateInvalidTime;

    private String appKey;

    private Short appType;

    private String accountNo;

    private String version;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "账户类型 1、自有账户 2、服务商二级商户")
    private Short accountType;

    @ApiModelProperty(value = "最大分账比例、账户类型选择服务商二级商户才有值")
    private BigDecimal subAccountRatio;

    @ApiModelProperty(value = "子商户号、账户类型选择服务商二级商户必填")
    private String subMchid;

}
