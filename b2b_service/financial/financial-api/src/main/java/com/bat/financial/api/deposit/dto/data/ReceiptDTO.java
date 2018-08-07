package com.bat.financial.api.deposit.dto.data;

import com.bat.financial.api.pay.dto.data.PayBillsDTO;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/31 22:40
 */
@Data
public class ReceiptDTO {
    private DepositDistributorSubsidiaryBookDTO bookDTO;
    private PayBillsDTO payBillsDTO;
}
