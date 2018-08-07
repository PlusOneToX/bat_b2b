package com.bat.distributor.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class DistributorNameChangeDTO implements Serializable {
    private Integer id;
    private String name;
    private String companyName;
}
