package com.bat.financial.deposit.executor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.financial.api.deposit.dto.DepositReChargeQry;
import com.bat.financial.dao.deposit.DepositDistributorMapper;
import com.bat.financial.dao.deposit.DepositDistributorSubsidiaryBookMapper;
import com.bat.financial.dao.deposit.DepositMapper;
import com.bat.financial.dao.pay.PayBillsCustomerMapper;
import com.bat.financial.dao.pay.PayBillsDistributorMapper;
import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDistributorDO;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.pay.constant.PayStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 15:01
 */
@Component
@Slf4j
public class RechargeQryExc {

    @Resource
    private DepositMapper depositMapper;

    @Resource
    private DepositDistributorMapper depositDistributorMapper;

    @Resource
    private DepositDistributorSubsidiaryBookMapper depositDistributorSubsidiaryBookMapper;

    @Resource
    private PayBillsCustomerMapper payBillsCustomerMapper;

    @Resource
    private PayBillsDistributorMapper payBillsDistributorMapper;

    public boolean queryRechargeStatus(DepositReChargeQry qry) {
        if (qry.getCustomerFlag() == CustomerFlag.IS_CUSTOMER) {
            PayBillsCustomerDO payBillsCustomerDO = payBillsCustomerMapper.getByOutTradeNo(qry.getOutTradeNo());
            return payBillsCustomerDO.getPayStatus() == PayStatus.COMPLETE_PAYMENT;
        } else if (qry.getCustomerFlag() == CustomerFlag.IS_NOT_CUSTOMER) {
            PayBillsDistributorDO payBillsDistributorDO =
                payBillsDistributorMapper.getByOutTradeNo(qry.getOutTradeNo());
            return payBillsDistributorDO.getPayStatus() == PayStatus.COMPLETE_PAYMENT;
        }
        return false;
    }
}
