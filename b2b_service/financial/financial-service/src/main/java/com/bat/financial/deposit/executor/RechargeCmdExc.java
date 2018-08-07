package com.bat.financial.deposit.executor;

import javax.annotation.Resource;

import com.bat.financial.deposit.constant.ChangeType;
import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.message.MessageSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.deposit.dto.data.PayCallBackDTO;
import com.bat.financial.api.voucher.VoucherService;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.OrderVoucherDTO;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
import com.bat.financial.common.constant.CounterpartyType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 15:01
 */
@Component
@Slf4j
public class RechargeCmdExc {

    @Resource
    private DepositBalanceService depositBalanceService;

    @Resource
    private DepositDetailService depositDetailService;

    @Resource
    private CommonService commonService;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Resource
    private VoucherService voucherService;

    /**
     * 更新账户余额 明细 生成收款单 同步收款单
     * 
     * @param payCallBackDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecharge(PayCallBackDTO payCallBackDTO) {
        log.info("充值更新开始：{}", JSON.toJSONString(payCallBackDTO));
        Integer distributorId = payCallBackDTO.getDistributorId();
        DepositDistributorDTO distributorDTO = depositBalanceService.getDepositBalanceByDistributorId(distributorId);
        if(distributorDTO != null) {
            DepositDistributorSubsidiaryBookDTO bookDO = new DepositDistributorSubsidiaryBookDTO();
            bookDO.setDistributorId(distributorDTO.getDistributorId());
            bookDO.setDistributorName(distributorDTO.getDistributorName());
            bookDO.setBusinessType(DepositBusinessType.RECHARGE);
            bookDO.setPayWay(payCallBackDTO.getPayWay());
            bookDO.setBusinessId(null);
            bookDO.setChangeType(ChangeType.INCREASE);
            bookDO.setAmount(payCallBackDTO.getAmount());
            bookDO.setRemark("充值");
            bookDO.setCreateTime(payCallBackDTO.getPayTime());
            commonService.calcBalanceAndBook(distributorDTO, bookDO);
            depositBalanceService.updateDepositBalance(distributorDTO);
            depositDetailService.createDepositDetail(bookDO);
            log.info("充值更新完成");
            createRechargeVoucher(payCallBackDTO);
        }
        return true;
    }

    /**
     * 生成收款单
     * 
     * @param payCallBackDTO
     */
    private void createRechargeVoucher(PayCallBackDTO payCallBackDTO) {
        log.info("生成充值收款单");
        OrderVoucherDTO dto = new OrderVoucherDTO();
        dto.setCounterpartyType(CounterpartyType.DISTRIBUTOR);
        dto.setDistributorId(payCallBackDTO.getDistributorId());
        dto.setDistributorName(payCallBackDTO.getDistributorName());
        if (StringUtils.isBlank(payCallBackDTO.getCompanyName())) {
            try {
                DistributorRpcDTO distributorInfo = commonService.getDistributorInfo(payCallBackDTO.getDistributorId());
                payCallBackDTO.setCompanyName(distributorInfo.getCompanyName());
            } catch (Exception e) {
                payCallBackDTO.setCompanyName("unknown");
                e.printStackTrace();
            }
        }
        dto.setCompanyName(payCallBackDTO.getCompanyName());
        dto.setPayWay(payCallBackDTO.getPayWay());
        if (StringUtils.isBlank(payCallBackDTO.getCurrencyType())) {
            payCallBackDTO.setCurrencyType("CNY");
        }
        dto.setCurrencyType(payCallBackDTO.getCurrencyType());
        dto.setOutTradeNo(payCallBackDTO.getOutTradeNo());
        voucherService.createVoucher(dto);
        VoucherQry qry = new VoucherQry();
        qry.setOutTradeNo(payCallBackDTO.getOutTradeNo());
        VoucherDistributorDTO voucher = voucherService.getVoucher(qry).get(0);
        // 开启子线程需指定租户编码
        String tenantNo = TenantContext.getTenantNo();
        threadPoolTaskScheduler.submit(() -> {
            log.info("异步同步收款单到ERP");
            TenantContext.setTenantNo(tenantNo);
            messageSendService.syncRechargeVouchersToERP(voucher.getId(), voucher.getOutTradeNo());
            TenantContext.removeTenantNo();
        });
    }

}
