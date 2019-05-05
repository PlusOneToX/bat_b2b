package com.bat.dubboapi.financial.voucher.dto.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/17 23:05
 */
@Data
public class ErpVoucherDetailsDTO implements Serializable {
    private String orderNo;
    private String voucherNo;
    private Long voucherTime;
    private Double amount;
}
