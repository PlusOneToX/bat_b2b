package com.bat.financial.pay.executor;

import static com.bat.financial.pay.executor.ErrorCode.B_ACCOUNT_NULL;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.dao.distributoraccount.AccountAlipayDistributorMapper;
import com.bat.financial.dao.distributoraccount.AccountWxDistributorMapper;
import com.bat.financial.dao.distributoraccount.dataobject.AccountAlipayDistributorDO;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;
import com.bat.financial.dao.pay.PayBillsCustomerMapper;
import com.bat.financial.dao.pay.PayBillsDistributorMapper;
import com.bat.financial.dao.pay.dataobject.ExpireTime;
import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDistributorDO;
import com.bat.financial.dao.platformaccount.AccountAlipayMapper;
import com.bat.financial.dao.platformaccount.AccountKuaiQianMapper;
import com.bat.financial.dao.platformaccount.AccountWxMapper;
import com.bat.financial.dao.platformaccount.dataobject.AccountAlipayDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountKuaiQianDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;
import com.bat.financial.message.MessageSendService;
import com.bat.financial.message.ReceiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.deposit.dto.ReceiptPayBillsQry;
import com.bat.financial.api.deposit.dto.data.PayCallBackDTO;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.pay.dto.CreateTradeCmd;
import com.bat.financial.common.constant.CounterpartyType;
import com.bat.financial.mq.dto.OrderPayDTO;
import com.bat.financial.pay.constant.BillsBusinessType;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.constant.PayWay;
import com.bat.financial.pay.data.AliConfig;
import com.bat.financial.pay.data.WxConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/21 20:53
 */
@Component
@Slf4j
public class PayCmdExc {

    @Resource
    private AccountWxMapper accountWxMapper;

    @Resource
    private AccountWxDistributorMapper accountWxDistributorMapper;

    @Resource
    private AccountAlipayMapper accountAlipayMapper;

    @Resource
    private AccountAlipayDistributorMapper accountAlipayDistributorMapper;

    @Resource
    private AccountKuaiQianMapper accountKuaiQianMapper;

    @Resource
    private PayBillsCustomerMapper payBillsCustomerMapper;

    @Resource
    private PayBillsDistributorMapper payBillsDistributorMapper;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    // 小程序 公众号支付 必须依赖前端传值。因为平台收款条件下，我们平台商户号对应多个分销商公众号或小程序id,这些id我们数据库无法获取
    // 数据库存储的平台appid，仅限我们公司内部的appid,无法直接给分销商使用。会报appid与openid不一致
    public WxConfig getWxConfig(Short tradeMode, Short appType, Integer payeeId, Integer organizationId) {
        log.info("getWxConfig:tradeMode:{},appType:{},payeeId:{},organizationId:{}", tradeMode, appType, payeeId,
            organizationId);
        WxConfig wxConfig = new WxConfig();
        if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            AccountWxDistributorDO accountWxDistributorDO =
                accountWxDistributorMapper.getByDistributorIdAndAppType(payeeId, appType);
            if (accountWxDistributorDO == null) {
                accountWxDistributorDO = accountWxDistributorMapper.getByDistributorId(payeeId);
            }
            if (accountWxDistributorDO == null) {
                throw FinancialException.buildException(B_ACCOUNT_NULL);
            }
            wxConfig.setAppId(accountWxDistributorDO.getAppId());
            wxConfig.setAccountNo(accountWxDistributorDO.getAccountNo());
            wxConfig.setAppKey(accountWxDistributorDO.getAppKey());
            wxConfig.setApiclientKey(accountWxDistributorDO.getApiclientKey());
            wxConfig.setSerialNumber(accountWxDistributorDO.getSerialNumber());
            wxConfig.setSubMchid(accountWxDistributorDO.getSubMchid());
        } else if (tradeMode.equals(TradeMode.PLATFORM)) {
            AccountWxDO accountWxDO = accountWxMapper.getByDistributorIdAndAppType(organizationId, appType);
            if (accountWxDO == null) {
                accountWxDO = accountWxMapper.getAccountByOrganizationAndAppType(organizationId, appType);
            }
            if (accountWxDO == null) {
                throw FinancialException.buildException(B_ACCOUNT_NULL);
            }
            wxConfig.setAppId(accountWxDO.getAppId());
            wxConfig.setAccountNo(accountWxDO.getAccountNo());
            wxConfig.setAppKey(accountWxDO.getAppKey());
            wxConfig.setApiclientKey(accountWxDO.getApiclientKey());
            wxConfig.setSerialNumber(accountWxDO.getSerialNumber());
            wxConfig.setBackType(accountWxDO.getBackType());
        }
        return wxConfig;
    }

    public WxConfig getWxConfig(Short tradeMode, Short appType, Integer payeeId, Integer organizationId,
        String outTradeNo) {
        WxConfig wxConfig = getWxConfig(tradeMode, appType, payeeId, organizationId);
        // 如果 outTradeNo 不为空 从支付凭证表中获取appid,支付时存储的appid,比任何来源更可信
        if (StringUtils.isNotBlank(outTradeNo)) {
            PayBillsDO payBillByOutTradeNo = getPayBillByOutTradeNo(null, outTradeNo);
            if (payBillByOutTradeNo != null) {
                wxConfig.setAppId(payBillByOutTradeNo.getAppId());
            } else {
                log.info("待操作的支付凭证不存在");
            }
        }
        return wxConfig;
    }

    public AliConfig getAlipayConfig(Short tradeMode, Integer payeeId, Integer organizationId) {
        AliConfig config = new AliConfig();
        if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            AccountAlipayDistributorDO aDo = accountAlipayDistributorMapper.getByDistributorId(payeeId);
            if (aDo == null) {
                log.info("支付宝分销商自己收款:{},支付未设置", payeeId);
                throw FinancialException.buildException(B_ACCOUNT_NULL);
            }
            config.setAppId(aDo.getAppId());
            config.setAppPrivateSecret(aDo.getAppPrivateSecret());
            config.setAppPublicSecret(aDo.getAppPublicSecret());
        } else if (tradeMode.equals(TradeMode.PLATFORM)) {
            AccountAlipayDO aDo = accountAlipayMapper.getByOrganizationId(organizationId);
            if (aDo == null) {
                log.info("支付宝平台收款，机构id:{},支付未设置", organizationId);
                throw FinancialException.buildException(B_ACCOUNT_NULL);
            }
            config.setAppId(aDo.getAppId());
            config.setAppPrivateSecret(aDo.getAppPrivateSecret());
            config.setAppPublicSecret(aDo.getAppPublicSecret());
            config.setBackType(aDo.getBackType());
        }
        return config;
    }

    public AccountKuaiQianDO getKuaiQianConfig(Integer organizationId) {
        AccountKuaiQianDO aDo = accountKuaiQianMapper.getByOrganizationId(organizationId);
        if (aDo == null) {
            throw FinancialException.buildException(B_ACCOUNT_NULL);
        }
        return aDo;
    }

    public void createPayBills(CreateTradeCmd cmd, String tradeNo) {
        createPayBills(cmd, tradeNo, new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    public void createPayBills(CreateTradeCmd cmd, String tradeNo, Date date) {
        PayBillsDO baseDo = new PayBillsDO();
        baseDo.setOutTradeNo(tradeNo);
        baseDo.setBusinessType(cmd.getBusinessType());
        baseDo.setBusinessId(cmd.getOrderId());
        baseDo.setPayStatus(PayStatus.WAIT_PAYMENT);
        baseDo.setTotalFee(cmd.getAmount());
        baseDo.setOrderTitle(cmd.getTitle());
        baseDo.setOrderDescribe(cmd.getDescription());
        baseDo.setProductId(null);
        baseDo.setOnlineTradeNo(null);
        // 根据平台渠道 设置过期时间
        if (cmd.getPayChannel().toUpperCase().equals(PayChannel.WXPAY_V3.name())
            || cmd.getPayChannel().toUpperCase().equals(PayChannel.WXPAY_V2.name())
            || cmd.getPayChannel().toUpperCase().equals(PayChannel.WXPAY_PARTNER_JSAPI.name())
            || cmd.getPayChannel().toUpperCase().equals(PayChannel.WXPAY_PARTNER_NATIVE.name())) {
            baseDo.setPayType(PayWay.WXPAY);
        } else if (cmd.getPayChannel().toUpperCase().equals(PayChannel.ALIPAY.name())) {
            baseDo.setPayType(PayWay.ALIPAY);
        } else if (cmd.getPayChannel().toUpperCase().equals(PayChannel.KUAIQIAN.name())) {
            baseDo.setPayType(PayWay.KUAIQIAN);
        } else if (cmd.getPayChannel().toUpperCase().equals(PayChannel.BALANCE.name())) {
            // 余额支付直接完成
            baseDo.setPayType(PayWay.BALANCE);
            baseDo.setPayStatus(PayStatus.COMPLETE_PAYMENT);
        }
        // 默认过期时间5分钟 只针对重复交易时 校验时使用，不在调起第三方你使用
        baseDo.setExpireTime(caleExTime(ExpireTime.DEFAULT_EXPIRE_TIME));
        baseDo.setPayTime(null);
        baseDo.setCreateTime(date);
        baseDo.setUpdateTime(date);
        // 收款账户定位参数
        baseDo.setPayMethod(cmd.getPayMethod().toUpperCase());
        baseDo.setTradeMode(cmd.getTradeMode());
        baseDo.setPayeeId(cmd.getPayeeId());
        baseDo.setOrganizationId(cmd.getOrganizationId());
        baseDo.setAppId(cmd.getAppId());
        // 先删除凭证 再插入
        deleteByOrderId(cmd.getOrderId(), cmd.getPayerId());
        if (cmd.getCustomerFlag() == CustomerFlag.IS_CUSTOMER) {
            PayBillsCustomerDO aDo = new PayBillsCustomerDO();
            BeanUtils.copyProperties(baseDo, aDo);
            aDo.setCustomerId(cmd.getPayerId());
            aDo.setCustomerName(cmd.getPayerName());
            aDo.setMchid(cmd.getMchid());
            aDo.setSpMchid(cmd.getSpMchid());
            // 新增订单C端客户支付凭证表
            payBillsCustomerMapper.insert(aDo);
        } else if (cmd.getCustomerFlag() == CustomerFlag.IS_NOT_CUSTOMER) {
            PayBillsDistributorDO aDo = new PayBillsDistributorDO();
            BeanUtils.copyProperties(baseDo, aDo);
            aDo.setDistributorId(cmd.getPayerId());
            aDo.setDistributorName(cmd.getPayerName());
            // 新增订单分销客户支付凭证表
            payBillsDistributorMapper.insert(aDo);
        }
    }

    private Date caleExTime(long time) {
        Date date = new Date();
        date.setTime(date.getTime() + time * 1000 - 1);
        return date;
    }

    public PayBillsDO getPayBillByOrderId(Short customerFlag, String orderId, Integer customerId) {
        PayBillsDO payBillsDO = null;
        if (customerFlag != null) {
            if (customerFlag == CustomerFlag.IS_CUSTOMER) {
                payBillsDO = payBillsCustomerMapper.getByOrderIdAndCustomerId(orderId, customerId);
            } else if (customerFlag == CustomerFlag.IS_NOT_CUSTOMER) {
                payBillsDO = payBillsDistributorMapper.getByOrderIdAndDistributorId(orderId, customerId);
            }
        } else {
            // 不传平台 查到为止
            payBillsDO = payBillsCustomerMapper.getByOrderIdAndCustomerId(orderId, customerId);
            if (payBillsDO == null) {
                payBillsDO = payBillsDistributorMapper.getByOrderIdAndDistributorId(orderId, customerId);
            }
        }
        return payBillsDO;
    }

    public PayBillsDO getPayBillByOutTradeNo(Short customerFlag, String outTradeNo) {
        PayBillsDO payBillsDO = null;
        if (customerFlag != null) {
            if (customerFlag == CustomerFlag.IS_CUSTOMER) {
                payBillsDO = payBillsCustomerMapper.getByOutTradeNo(outTradeNo);
            } else if (customerFlag == CustomerFlag.IS_NOT_CUSTOMER) {
                payBillsDO = payBillsDistributorMapper.getByOutTradeNo(outTradeNo);
            }
        } else {
            // 不传平台 查到为止
            payBillsDO = payBillsCustomerMapper.getByOutTradeNo(outTradeNo);
            if (payBillsDO == null) {
                payBillsDO = payBillsDistributorMapper.getByOutTradeNo(outTradeNo);
            }
        }
        return payBillsDO;
    }

    public List<PayBillsDistributorDO> listPayBillsDistributorByParams(ReceiptPayBillsQry qry) {
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("payTime", qry.getPayTime());
        return payBillsDistributorMapper.selectByParams(map);
    }

    /**
     * 根据商户订单号删除支付凭证
     *
     * @param outTradeNo
     */
    public void deleteByOutTradeNo(String outTradeNo) {
        payBillsCustomerMapper.deleteByOutTradeNo(outTradeNo);
        payBillsDistributorMapper.deleteByOutTradeNo(outTradeNo);
    }

    public void deleteByOrderId(String orderId, Integer payerId) {
        if (StringUtils.isNotBlank(orderId)) {
            orderId = orderId.split(",")[0];
            payBillsCustomerMapper.deleteByOrderId(orderId, payerId);
            payBillsDistributorMapper.deleteByOrderId(orderId, payerId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePayBills(Short customerFlag, PayBillsDO aDo) {
        try {
            if (customerFlag == CustomerFlag.IS_CUSTOMER) {
                log.info("更新支付凭证C端1：{}", aDo);
                PayBillsCustomerDO payBillsCustomerDO =
                    payBillsCustomerMapper.getByOutTradeNoAndTotalFee(aDo.getOutTradeNo(), aDo.getTotalFee());
                log.info("更新支付凭证C端2:{}", payBillsCustomerDO);
                if (payBillsCustomerDO == null) {
                    throw FinancialException.buildException(ErrorCode.B_PAY_BILL_NOT_EXISTS);
                }
                if (payBillsCustomerDO.getPayStatus() == PayStatus.WAIT_PAYMENT) {
                    payBillsCustomerDO.setPayStatus(aDo.getPayStatus());
                    payBillsCustomerDO.setPayTime(aDo.getPayTime());
                    payBillsCustomerDO.setOnlineTradeNo(aDo.getOnlineTradeNo());
                    payBillsCustomerDO.setReceiptAmount(aDo.getReceiptAmount());
                    payBillsCustomerDO.setRemark(aDo.getRemark());
                    log.info("wait update:{}", payBillsCustomerDO);
                    payBillsCustomerMapper.updateByPrimaryKeySelective(payBillsCustomerDO);
                    // C 端只有下订单
                    if (payBillsCustomerDO.getPayStatus() == PayStatus.COMPLETE_PAYMENT
                        && payBillsCustomerDO.getBusinessType() == BillsBusinessType.ORDER) {
                        log.info("C端订单异步通知");
                        // 开启子线程需指定租户编码
                        String tenantNo = TenantContext.getTenantNo();
                        threadPoolTaskScheduler.submit(() -> {
                            TenantContext.setTenantNo(tenantNo);
                            transactionSuccessCallback(payBillsCustomerDO);
                            TenantContext.removeTenantNo();
                        });
                    }
                } else if (payBillsCustomerDO.getPayStatus() == PayStatus.COMPLETE_PAYMENT) {
                    log.info("C端支付凭证号：{}，所对应的支付，已经完成支付", aDo.getOutTradeNo());
                } else if (payBillsCustomerDO.getPayStatus() == PayStatus.CANCEL_PAYMENT) {
                    log.info("C端支付凭证号：{}，所对应的支付，已经被取消", aDo.getOutTradeNo());
                } else {
                    log.info("C端支付凭证号：{}，所对应的支付 不属于待支付 已支付 已取消的任意一种", aDo.getOutTradeNo());
                }
            } else if (customerFlag == CustomerFlag.IS_NOT_CUSTOMER) {
                log.info("更新支付凭证B端1:{}", aDo);
                PayBillsDistributorDO payBillsDistributorDO =
                    payBillsDistributorMapper.getByOutTradeNoAndTotalFee(aDo.getOutTradeNo(), aDo.getTotalFee());
                log.info("更新支付凭证B端2:{}", payBillsDistributorDO);
                if (payBillsDistributorDO == null) {
                    throw FinancialException.buildException(ErrorCode.B_PAY_BILL_NOT_EXISTS);
                }
                if (payBillsDistributorDO.getPayStatus() == PayStatus.WAIT_PAYMENT) {
                    payBillsDistributorDO.setPayStatus(aDo.getPayStatus());
                    payBillsDistributorDO.setPayTime(aDo.getPayTime());
                    payBillsDistributorDO.setOnlineTradeNo(aDo.getOnlineTradeNo());
                    // B端目前没有值
                    payBillsDistributorDO.setReceiptAmount(aDo.getReceiptAmount());
                    payBillsDistributorDO.setRemark(aDo.getRemark());
                    log.info("wait update:{}", payBillsDistributorDO);
                    payBillsDistributorMapper.updateByPrimaryKeySelective(payBillsDistributorDO);
                    // 目前B端有充值 C端没有
                    if (payBillsDistributorDO.getPayStatus() == PayStatus.COMPLETE_PAYMENT) {
                        if (payBillsDistributorDO.getBusinessType().equals(BillsBusinessType.RECHARGE)) {
                            log.info("充值异步通知");
                            rechargeSuccessCallback(payBillsDistributorDO);
                        } else if (payBillsDistributorDO.getBusinessType().equals(BillsBusinessType.ORDER)) {
                            log.info("B端订单异步通知");
                            // 开启子线程需指定租户编码
                            String tenantNo = TenantContext.getTenantNo();
                            threadPoolTaskScheduler.submit(() -> {
                                TenantContext.setTenantNo(tenantNo);
                                transactionSuccessCallback(payBillsDistributorDO);
                                TenantContext.removeTenantNo();
                            });
                        }
                    }
                } else if (payBillsDistributorDO.getPayStatus() == PayStatus.COMPLETE_PAYMENT) {
                    log.info("B端支付凭证号：{}，所对应的支付，已经完成支付", aDo.getOutTradeNo());
                } else if (payBillsDistributorDO.getPayStatus() == PayStatus.CANCEL_PAYMENT) {
                    log.info("B端支付凭证号：{}，所对应的支付，已经被取消", aDo.getOutTradeNo());
                } else {
                    log.info("B端支付凭证号：{}，所对应的支付 不属于待支付 已支付 已取消的任意一种", aDo.getOutTradeNo());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * B端充值回调
     * 
     * @param payBillsDistributorDO
     */
    public void rechargeSuccessCallback(PayBillsDistributorDO payBillsDistributorDO) {
        PayCallBackDTO payCallBackDTO = new PayCallBackDTO();
        payCallBackDTO.setOutTradeNo(payBillsDistributorDO.getOutTradeNo());
        payCallBackDTO.setDistributorId(payBillsDistributorDO.getDistributorId());
        payCallBackDTO.setDistributorName(payBillsDistributorDO.getDistributorName());
        payCallBackDTO.setAmount(payBillsDistributorDO.getTotalFee());
        /**
         * 支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
         */
        payCallBackDTO.setPayWay(payBillsDistributorDO.getPayType());
        payCallBackDTO.setPayBillsId(payBillsDistributorDO.getId());
        payCallBackDTO.setCurrencyType(payBillsDistributorDO.getCurrencyCode());
        payCallBackDTO.setBusinessType(BillsBusinessType.RECHARGE);
        payCallBackDTO.setRemark("充值");
        payCallBackDTO.setPayTime(payBillsDistributorDO.getPayTime());
        /**
         * @see ReceiveService#rechargeSuccessCallback(PayCallBackDTO)
         */
        log.info("消息体：{}", payCallBackDTO);
        boolean b = messageSendService.rechargeSuccessCallback(payCallBackDTO, payBillsDistributorDO.getOutTradeNo());
        if (b) {
            log.info("消息发送成功");
        }
    }

    /**
     * B端下单回调
     * 
     * @param payBillsDistributorDO
     */
    public void transactionSuccessCallback(PayBillsDistributorDO payBillsDistributorDO) {
        if (payBillsDistributorDO.getBusinessId() != null) {
            OrderPayDTO dto = new OrderPayDTO();
            String[] split = payBillsDistributorDO.getBusinessId().split(",");
            List<Integer> orderIds = Arrays.stream(split).map(Integer::valueOf).collect(Collectors.toList());
            dto.setOrderIds(orderIds);
            dto.setCounterpartyType(CounterpartyType.DISTRIBUTOR);
            dto.setDistributorId(payBillsDistributorDO.getDistributorId());
            // TODO 待完善 拆分支付的情况
            // 余额支付没有支付凭证号，所以为空
            String key = null;
            if (payBillsDistributorDO.getOutTradeNo() != null) {
                dto.setOutTradeNo(payBillsDistributorDO.getOutTradeNo());
                key = payBillsDistributorDO.getOutTradeNo();
                dto.setDepositAmount(BigDecimal.ZERO);
                dto.setPaidAmount(payBillsDistributorDO.getTotalFee());
                // B端目前没有值
                dto.setReceiptAmount(payBillsDistributorDO.getReceiptAmount());
            } else {
                dto.setDepositAmount(payBillsDistributorDO.getTotalFee());
                dto.setPaidAmount(BigDecimal.ZERO);
            }
            dto.setPadTime(payBillsDistributorDO.getPayTime());
            log.info("消息体：{}", dto);
            boolean b = messageSendService.transactionSuccessCallback(dto, key);
            if (b) {
                log.info("消息发送成功");
            }
        } else {
            log.error("订单不存在，无法发送消息");
        }
    }

    /**
     * C端下单回调
     * 
     * @param payBillsCustomerDO
     */
    public void transactionSuccessCallback(PayBillsCustomerDO payBillsCustomerDO) {
        if (payBillsCustomerDO.getBusinessId() != null) {
            OrderPayDTO dto = new OrderPayDTO();
            String[] split = payBillsCustomerDO.getBusinessId().split(",");
            List<Integer> orderIds = Arrays.stream(split).map(Integer::valueOf).collect(Collectors.toList());
            dto.setOrderIds(orderIds);
            dto.setCounterpartyType(CounterpartyType.CUSTOMER);
            dto.setCustomerId(payBillsCustomerDO.getCustomerId());
            // TODO 待完善 拆分支付的情况
            dto.setDepositAmount(BigDecimal.ZERO);
            dto.setPaidAmount(payBillsCustomerDO.getTotalFee());
            dto.setPadTime(payBillsCustomerDO.getPayTime());
            dto.setOutTradeNo(payBillsCustomerDO.getOutTradeNo());
            dto.setPaidAmount(payBillsCustomerDO.getTotalFee());
            dto.setReceiptAmount(payBillsCustomerDO.getReceiptAmount());
            // 目前只处理 全场券
            if (StringUtils.isNotBlank(payBillsCustomerDO.getRemark())) {
                if (payBillsCustomerDO.getRemark().contains(OrderPayDTO.ALIPAY_VOUCHER_TYPE_ALIPAY_BIZ_VOUCHER)) {
                    dto.setAlipayVoucherType(OrderPayDTO.ALIPAY_VOUCHER_TYPE_ALIPAY_BIZ_VOUCHER);
                }
            }
            log.info("消息体：{}", dto);
            boolean b = messageSendService.transactionSuccessCallback(dto, payBillsCustomerDO.getOutTradeNo());
            if (b) {
                log.info("消息发送成功");
            }
        } else {
            log.error("订单不存在，无法发送消息");
        }
    }
}
