package com.bat.financial.message;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.bat.financial.subaccount.executor.OrderSubAccountCmdExe;
import com.bat.financial.voucher.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.financial.subaccount.dto.OrderSubAccountCmd;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.Response;
import com.bat.financial.api.deposit.RechargeService;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.deposit.dto.data.PayCallBackDTO;
import com.bat.financial.api.refund.RefundService;
import com.bat.financial.api.refund.dto.data.OrderRefundDTO;
import com.bat.financial.api.subaccount.OrderSubAccountBillServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountIdCmd;
import com.bat.financial.api.voucher.VoucherService;
import com.bat.financial.api.voucher.dto.data.OrderVoucherDTO;
import com.bat.financial.common.FinancialConfig;
import com.bat.financial.common.constant.FinancialConstant;
import com.bat.financial.deposit.DepositServiceImpl;
import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.mq.api.Sink;
import com.bat.financial.mq.dto.OrderCommissionDTO;
import com.bat.financial.pay.WxPayPartnerServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 13:54
 */
@Service
@Slf4j
public class ReceiveService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Resource
    private RechargeService reChargeService;

    @Resource
    private VoucherService voucherService;

    @Resource
    private DepositServiceImpl depositService;

    @Resource
    private RefundService refundService;

    @Resource
    private FinancialConfig config;

    @Resource
    private OrderSubAccountCmdExe orderSubAccountCmdExe;

    @Autowired
    private WxPayPartnerServiceImpl wxPayPartnerServiceImpl;

    @Autowired
    private OrderSubAccountBillServiceI orderSubAccountBillServiceI;

    /**
     * 测试监听者
     * 
     * @param message
     */
    @StreamListener(value = Sink.FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'Test'")
    public void test(@Headers Map<String, Object> headers, String message) {
        System.out.println(headers);
        for (Map.Entry<String, Object> stringStringEntry : headers.entrySet()) {
            String key = stringStringEntry.getKey();
            Object value = stringStringEntry.getValue();
            System.out.println(key);
            System.out.println(value);
        }
        log.info("{}, tags: test receive: {}", Sink.FINANCIAL_INPUT, message);
    }

    /**
     * 充值回调监听者
     * 
     * @param payCallBackDTO
     */
    @StreamListener(value = Sink.FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'Recharge'")
    public void rechargeSuccessCallback(@Headers Map headers, @Payload PayCallBackDTO payCallBackDTO) {
        log.info("{} receive: {}", Sink.FINANCIAL_INPUT, JSON.toJSONString(payCallBackDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        reChargeService.updateRecharge(payCallBackDTO);
        TenantContext.removeTenantNo();
    }

    /**
     * 生成收款单监听订单
     * 
     * @param orderVoucherDTO
     */
    @StreamListener(value = Sink.FINANCIAL_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderVoucher'")
    public void createOrderVoucher(@Headers Map headers, @Payload OrderVoucherDTO orderVoucherDTO) {
        log.info("{} receive: {}", Sink.FINANCIAL_DISTRIBUTOR_INPUT, JSON.toJSONString(orderVoucherDTO));
        try {
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            voucherService.createVoucher(orderVoucherDTO);
            TenantContext.removeTenantNo();
        } catch (FinancialException e) {
            TenantContext.removeTenantNo();
            if (e.getCode().equals(ErrorCode.B_VOUCHER_IS_EXISTS)) {
                log.info("之前已经生成过收款单，消息静默处理成功");
            } else {
                throw e;
            }
        } catch (Exception e) {
            TenantContext.removeTenantNo();
            throw e;
        }
    }

    /**
     * 余额变更监听
     * 
     * @param bookDTO
     */
    @StreamListener(value = Sink.FINANCIAL_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'depositDetailChange'")
    public void depositDetailChange(@Headers Map headers, @Payload DepositDistributorSubsidiaryBookDTO bookDTO) {
        log.info("{} receive: {}", Sink.FINANCIAL_ORDER_INPUT, JSON.toJSONString(bookDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        depositService.accountBalanceChange(bookDTO);
        TenantContext.removeTenantNo();
    }

    /**
     * 佣金监听
     *
     * @param commissionDTO
     */
    @StreamListener(value = Sink.FINANCIAL_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderCommission'")
    public void orderCommission(@Headers Map headers, @Payload OrderCommissionDTO commissionDTO) {
        log.info("{} receive: {}", Sink.FINANCIAL_ORDER_INPUT, JSON.toJSONString(commissionDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        DepositDistributorSubsidiaryBookDTO bookDTO = new DepositDistributorSubsidiaryBookDTO();
        bookDTO.setDepositDistributorId(null);
        bookDTO.setBusinessId(commissionDTO.getOrderId() + "");
        bookDTO.setDistributorId(commissionDTO.getDistributorId());
        bookDTO.setDistributorName(null);
        bookDTO.setAmount(commissionDTO.getAmount());
        bookDTO.setBusinessType(DepositBusinessType.DISTRIBUTION_COMMISSION);
        bookDTO.setChangeType(commissionDTO.getCommissionType());
        bookDTO.setCreateTime(new Date());
        depositService.accountBalanceChange(bookDTO);
        TenantContext.removeTenantNo();
    }

    @CreateCache(name = FinancialConstant.REFUND_TRY_LOCK, cacheType = CacheType.BOTH)
    private Cache<String, Integer> financialRefundCache;

    /**
     * 退款监听 (这个分布式锁 貌似没有用，在下次消息推送时，锁已经释放，达不到去重的目的)
     *
     * @param orderRefundDTO
     */
    @StreamListener(value = Sink.FINANCIAL_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderRefund'")
    public void orderRefund(@Headers Map headers, @Payload OrderRefundDTO orderRefundDTO) {
        log.info("{} receive: {}", Sink.FINANCIAL_ORDER_INPUT, JSON.toJSONString(orderRefundDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        String messageId = (String)headers.get("rocketmq_MESSAGE_ID");
        log.info("退款监听 消息id：{}", messageId);
        // 消息id 做分布式锁，没拿到锁，直接返回成功，避免重复消费消息。（其实这样做已经失去了消息队列的意义，但又不能不去重消息）
        String finalTenantNo = tenantNo;
        boolean b = financialRefundCache.tryLockAndRun(TenantContext.getTenantNo() + ":" + messageId, 3,
            TimeUnit.MINUTES, () -> {
                TenantContext.setTenantNo(finalTenantNo);
                refundService.createRefund(orderRefundDTO);
                TenantContext.removeTenantNo();
            });
        if (b) {
            log.info("获取锁并，执行退款逻辑");
        } else {
            log.info("已经有相同id 消息正在处理。静默返回成功");
        }
    }

    /**
     * 分销商用户名修改监听
     * 
     * @param nameChange
     */
    @StreamListener(value = Sink.FINANCIAL_DISTRIBUTOR_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'distributorNameChange'")
    public void distributorInfoChangeEvent(@Headers Map headers, String nameChange) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        System.out.println(nameChange);
        // log.info("{} receive: {}", Sink.INPUT1, JSON.toJSONString(nameChange));
        // DistributorNameChangeEvent event = new DistributorNameChangeEvent(this);
        // BeanUtils.copyProperties(nameChange, event);
        // log.info("发布事件 distributorNameChange:{}", event);
        // publisher.publishEvent(event);
        TenantContext.removeTenantNo();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    /**
     * 订单分账记录生成
     *
     * @param orderSubAccountCmd
     */
    @StreamListener(value = Sink.FINANCIAL_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderSubAccount'")
    public void orderSubAccount(@Headers Map<String, Object> headers, @Payload OrderSubAccountCmd orderSubAccountCmd) {
        log.info("订单分账、消息处理 receive: {}", JSON.toJSONString(orderSubAccountCmd));
        String messageId = (String)headers.get("rocketmq_MESSAGE_ID");
        log.info("订单分账 消息id：{}", messageId);
        try {
            String tenantNo = (String)headers.get("tenantNo");
            log.info("orderSubAccount tenantNo:{}", tenantNo);
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
                log.info("orderSubAccount tenantNo:{}", tenantNo);
            }
            TenantContext.setTenantNo(tenantNo);
            orderSubAccountCmdExe.dealwithOrderSubAccount(orderSubAccountCmd);
            TenantContext.removeTenantNo();
        } catch (Exception e) {
            TenantContext.removeTenantNo();
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 订单分账
     *
     * @param orderSubAccountIdCmd
     */
    @StreamListener(value = Sink.FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'subAccountToWxPartner'")
    public void subAccountToWxPartner(@Headers Map<String, Object> headers,
        @Payload OrderSubAccountIdCmd orderSubAccountIdCmd) {
        log.info("订单通过微信服务商分账、消息处理 receive: {}", JSON.toJSONString(orderSubAccountIdCmd));
        String messageId = (String)headers.get("rocketmq_MESSAGE_ID");
        log.info("订单通过微信服务商分账 消息id：{}", messageId);

        try {
            String tenantNo = (String)headers.get("tenantNo");
            log.info("subAccountToWxPartner tenantNo:{}", tenantNo);
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
                log.info("subAccountToWxPartner tenantNo:{}", tenantNo);
            }
            TenantContext.setTenantNo(tenantNo);
            Response response = wxPayPartnerServiceImpl.subAccountToWxPartner(orderSubAccountIdCmd, null, null);
            TenantContext.removeTenantNo();
            if (!response.isSuccess()) {
                // 找不到该分销商的订单数据 这个错误需要消息重试
                if ("O_ORDER_DISTRIBUTOR_DATA_NULL".equals(response.getErrCode())) {
                    throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
                }
            }
        } catch (FinancialException e) {
            log.error("微信服务商分账异常{}、参数是{}", e.getMsg(), JSON.toJSONString(orderSubAccountIdCmd));
            TenantContext.removeTenantNo();
            throw e;
        }
    }

    /**
     * 订单分账定时器执行
     *
     * @param
     */
    @StreamListener(value = Sink.FINANCIAL_THIRDPARTY_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'wxSubAccountTimer'")
    public void wxSubAccountTimer(@Headers Map<String, Object> headers, @Payload Long time) {
        log.info("订单分账定时器消息消费、消息处理: {}", time);
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        String finalTenantNo = tenantNo;
        new Thread(() -> {
            // 线程执行
            TenantContext.setTenantNo(finalTenantNo);
            orderSubAccountBillServiceI.wxSubAccountTimer();
            TenantContext.removeTenantNo();
        }).start();
        log.info("执行订单分账定时器、处理完毕");
    }
}
