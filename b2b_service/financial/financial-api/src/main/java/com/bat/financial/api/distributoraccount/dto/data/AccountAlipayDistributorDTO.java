package com.bat.financial.api.distributoraccount.dto.data;

import java.util.Date;
import java.util.List;

import com.bat.financial.api.distributoraccount.dto.DistributorInfo;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:55
 */
@Data
public class AccountAlipayDistributorDTO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String distributorCompanyName;

    private List<DistributorInfo> distributorInfos;

    private String accountName;

    private String appId;

    private Date createTime;

    private Date updateTime;

    private String appPublicSecret;

    private String appPrivateSecret;

}
