package com.bat.financial.message;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.RefundLogDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.VoucherLogDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.WithdrawApplyLogDTO;
import com.bat.financial.api.deposit.dto.data.PayCallBackDTO;
import com.bat.financial.api.message.MessageSendServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountIdCmd;
import com.bat.financial.mq.dto.CommonLogDTO;
import com.bat.financial.mq.dto.OrderPayDTO;
import com.bat.financial.mq.service.SenderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 9:07
 */
@Slf4j
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;

    @Resource
    private HttpServletRequest request;

    /**
     * 充值成功回调
     * 
     * @param payCallBackDTO
     * @return
     */
    public boolean rechargeSuccessCallback(PayCallBackDTO payCallBackDTO, String key) {
        return service.sendObject(payCallBackDTO, "Recharge", key);
    }

    /**
     * 充值生成收款单
     *
     * @param id
     * @return
     */
    public boolean syncRechargeVouchersToERP(Integer id, String key) {
        return service.sendObject(id, "rechargeVoucherSyncErp", key);
    }

    /**
     * 交易成功回调
     * 
     * @param dto
     * @return
     */
    public boolean transactionSuccessCallback(OrderPayDTO dto, String key) {
        return service.sendObject(dto, "orderPay", key);
    }

    /**
     * 同步退款单
     *
     * @param dto
     * @return
     */
    public boolean syncRefundBillToErp(RefundBillSyncDTO dto, String key) {
        return service.sendObject(dto, "syncRefundBill", key);
    }

    /**
     * 发送收款单日志
     */
    private void voucherLog(Integer voucherId, Integer orderId, String operateSource, Integer operateId,
        String operator, String operateType, String operateDes, String operateData, Date time) {
        VoucherLogDTO dto = MessageConvertor.toVoucherLogDTO(voucherId, orderId, operateSource, operateId, operator,
            operateType, operateDes, operateData, time);
        service.sendObject(dto, "voucherLog", "voucherLog-" + dto.getVoucherId());
    }

    /**
     * 发送收款单日志
     * 
     * @param voucherId
     * @param orderId
     * @param operateType
     * @param operateDes
     * @param operateData
     */
    public void voucherLogPackage(Integer voucherId, Integer orderId, String operateType, String operateDes,
        String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName = "系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            voucherLog(voucherId, orderId, platform, userId, userName, operateType, operateDes, operateData,
                new Date());
        } catch (Exception e) {
            log.error("记录收款单日志出现异常:{}", e);
        }
    }

    /**
     * 发送退款单日志
     */
    private void refundLog(Integer refundId, Integer orderId, String operateSource, Integer operateId, String operator,
        String operateType, String operateDes, String operateData, Date time) {
        RefundLogDTO dto = MessageConvertor.toRefundLogDTO(refundId, orderId, operateSource, operateId, operator,
            operateType, operateDes, operateData, time);
        service.sendObject(dto, "refundLog", "refundLog-" + dto.getRefundId());
    }

    /**
     * 发送退款单日志
     * 
     * @param refundId
     * @param orderId
     * @param operateType
     * @param operateDes
     * @param operateData
     */
    public void refundLogPackage(Integer refundId, Integer orderId, String operateType, String operateDes,
        String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName = "系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            refundLog(refundId, orderId, platform, userId, userName, operateType, operateDes, operateData, new Date());
        } catch (Exception e) {
            log.error("记录退款单日志出现异常:{}", e);
        }
    }

    /**
     * 发送通用日志消息
     */
    @Override
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
            operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

    /**
     * 发送分销客户提现申请日志
     * 
     * @param withdrawApplyId
     * @param distributorId
     * @param operateSource
     * @param operateId
     * @param operator
     * @param operateType
     * @param operateDes
     * @param operateData
     * @param time
     */
    private void withdrawApplyLog(Integer withdrawApplyId, Integer distributorId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        WithdrawApplyLogDTO dto = MessageConvertor.toWithdrawApplyLogDTO(withdrawApplyId, distributorId, operateSource,
            operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "withdrawApplyLog", "withdrawApplyLog-" + dto.getWithdrawApplyId());
    }

    /**
     * 发送分销客户提现申请日志
     * 
     * @param withdrawApplyId
     * @param distributorId
     * @param operateType
     * @param operateDes
     * @param operateData
     */
    public void withdrawApplyLogPackage(Integer withdrawApplyId, Integer distributorId, String operateType,
        String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName = "系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            withdrawApplyLog(withdrawApplyId, distributorId, platform, userId, userName, operateType, operateDes,
                operateData, new Date());
        } catch (Exception e) {
            log.error("记录分销客户提现申请日志出现异常:{}", e);
        }
    }

    /**
     * 测试
     *
     * @param request
     * @param accessor
     * @return
     */
    @Override
    public boolean test(String request, MessageHeaderAccessor accessor) {
        return service.sendObject(request, accessor);
    }

    private String getUserName() {
        String userName = request.getHeader("userName");
        if (StringUtils.isNotBlank(userName)) {
            try {
                return URLDecoder.decode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param orderSubAccountIdCmd
     */
    public void subAccountToWxPartner(OrderSubAccountIdCmd orderSubAccountIdCmd) {
        service.sendObject(orderSubAccountIdCmd, "subAccountToWxPartner",
            "subAccountToWxPartner-" + System.currentTimeMillis());
    }
}
