package com.bat.flexible.dao.exchange.co;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExchangeCodePageCO implements Serializable {

    private static final long serialVersionUID = 5615599225749351216L;

    private Integer id;
    //明码
    private String plainCode;

    //暗码
    private String secretCode;

    //盒码
    private String boxCode;

    //核销状态 0、初始化 1、未使用 2、已核销 3、已作废
    private Short status;

    //兑换卡活动名称
    private String cardName;

    //核销人
    private String userName;

    //核销订单id
    private Integer userOrderId;
   //核销时间
    private Date useTime;
    //最后操作时间
    private Date updateTime;

    //生成时间
    private Date createTime;

    private Short exchangeWay;

    //分销商名称
    private String distributorName;

    //批次
    private Integer exchangeFactoryId;

    /**
     * 分销商B2B订单号
     */
    private Integer distributorOrderId;

    /**
     * 核销第三方订单号
     */
    private String userThirdOrderNo;
}
