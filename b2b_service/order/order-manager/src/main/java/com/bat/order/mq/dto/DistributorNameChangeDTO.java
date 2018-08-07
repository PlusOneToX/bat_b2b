package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class DistributorNameChangeDTO implements Serializable {
    private Integer id;
    private String name;
    private String companyName;
}
