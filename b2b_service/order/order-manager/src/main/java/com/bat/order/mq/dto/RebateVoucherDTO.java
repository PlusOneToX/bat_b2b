package com.bat.order.mq.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/2/17 10:57
 */
@Data
public class RebateVoucherDTO implements Serializable {
    private List<Integer> rebateVoucherIds;
    private List<RebateVoucherItemDTO> voucherItems;

    @Data
    public static class RebateVoucherItemDTO implements Serializable{
        private Integer orderId;
        private String orderNo;
        private BigDecimal rebateVoucherAmount;
        private Date createTime;
    }
}


