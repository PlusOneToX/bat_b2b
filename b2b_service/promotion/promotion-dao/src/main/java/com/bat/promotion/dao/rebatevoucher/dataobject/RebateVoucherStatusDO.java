package com.bat.promotion.dao.rebatevoucher.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class RebateVoucherStatusDO {
    private Integer id;
    private Short rebateVoucherStatus;
    private Date updateTime;

}