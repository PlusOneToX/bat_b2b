package com.bat.financial.api.refund.dto.data;

import java.util.List;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:52
 */
@Data
public class RefundApplyDTO {
    List<RefundDistributorApplyDTO> refundDistributorApplyDTOS;
    List<RefundCustomerApplyDTO> refundCustomerApplyDTOS;
}
