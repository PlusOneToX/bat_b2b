package com.bat.dubboapi.financial.refund.dto.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/28 9:59
 */
@Data
public class RefundBillSyncDTO implements Serializable {
    // 退款单id
    private Integer id;
    // 有方法体直接同步 没有从财务反查
    private CreateRefundBillRequest refundBillReq;
}
