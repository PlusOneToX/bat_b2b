package com.bat.financial.common;

import static com.bat.financial.common.constant.FinancialConstant.URL_TYPE_6;
import static com.bat.financial.common.error.FinancialCommonErrorCode.COMMON_TENANT_URL_ERROR;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.bat.financial.common.constant.WxApiVersion;
import com.bat.financial.common.constant.account.DistributorAccountConstant;
import com.bat.financial.common.constant.third.DistributorConstant;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.deposit.constant.ChangeType;
import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.distributoraccount.executor.AccountWxDistributorQryExc;
import com.bat.financial.platformaccount.executor.AccountAlipayQryExc;
import com.bat.financial.platformaccount.executor.AccountKuaiQianQryExc;
import com.bat.financial.platformaccount.executor.AccountOfflineQryExc;
import com.bat.financial.platformaccount.executor.AccountWxQryExc;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.OrderTradeRpcQry;
import com.bat.dubboapi.order.order.dto.data.OrderCostRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderTradeRpcDTO;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.common.dto.BaseBillEntity;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.pay.dto.CreateTradeCmd;
import com.bat.financial.api.pay.dto.QueryTradeQry;
import com.bat.financial.api.pay.dto.RefundTradeCmd;
import com.bat.financial.api.platformaccount.dto.data.AccountAlipayDTO;
import com.bat.financial.api.platformaccount.dto.data.AccountKuaiQianDTO;
import com.bat.financial.api.platformaccount.dto.data.AccountOfflineDTO;
import com.bat.financial.api.platformaccount.dto.data.AccountWxDTO;
import com.bat.financial.common.constant.CounterpartyType;
import com.bat.financial.pay.constant.BillsBusinessType;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.pay.constant.PayWay;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 通用的一些工具
 * @date: 2018/6/9 23:18
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService, ApplicationContextAware {

    @DubboReference(check = false, timeout = 30000)
    private SystemUserServiceRpc systemUserServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private OrderServiceRpc orderServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformServiceRpc;

    private ApplicationContext applicationContext;

    private final Map<PayChannel, PayService> map = new HashMap<>();

    @Override
    public Map<PayChannel, PayService> getMap() {
        return map;
    }

    @PostConstruct
    public void init() {
        map.put(PayChannel.WXPAY_V2, applicationContext.getBean("WxPayV2ServiceImpl", PayService.class));
        map.put(PayChannel.WXPAY_V3, applicationContext.getBean("WxPayV3ServiceImpl", PayService.class));
        map.put(PayChannel.ALIPAY, applicationContext.getBean("AliPayServiceImpl", PayService.class));
        map.put(PayChannel.KUAIQIAN, applicationContext.getBean("KuaiQianPayServiceImpl", PayService.class));
        map.put(PayChannel.BALANCE, applicationContext.getBean("BalancePayServiceImpl", PayService.class));
        // 微信服务商支付
        map.put(PayChannel.WXPAY_PARTNER_JSAPI,
            applicationContext.getBean("WxPayPartnerServiceImpl", PayService.class));
        map.put(PayChannel.WXPAY_PARTNER_NATIVE,
            applicationContext.getBean("WxPayPartnerServiceImpl", PayService.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Resource
    private AccountWxQryExc accountWxQryExc;

    @Resource
    private AccountWxDistributorQryExc accountWxDistributorQryExc;

    /**
     * 获取当前用户下的用户列表
     *
     * @param userId
     * @return
     */
    public List<Integer> getOwnSaleIds(Integer userId) {
        List<Integer> ownSaleIds = null;
        Response<List<Integer>> response = systemUserServiceRpc.findOwnSaleIds(userId);
        if (response.isSuccess()) {
            ownSaleIds = response.getData();
        } else {
            throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return ownSaleIds;
    }

    /**
     * 获取业务员对应的分销商id集合
     *
     * @param userId
     * @return
     */
    public List<Integer> getDistributorIds(Integer userId) {
        List<Integer> ownSaleIds = getOwnSaleIds(userId);
        List<Integer> distributorId = null;
        if (CollectionUtils.isNotEmpty(ownSaleIds)) {
            distributorId = distributorServiceRpc.getDistributorIdsBySalesIds(ownSaleIds);
        }
        return distributorId;
    }

    /**
     * 获取分销商信息
     * 
     * @param distributorId
     * @return
     */
    @Override
    public DistributorRpcDTO getDistributorInfo(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> response =
            distributorServiceRpc.distributorById(distributorId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取组织信息
     * 
     * @param distributorId
     * @return
     */
    @Override
    public OrganizationRpcDTO getOrganizationInfo(Integer distributorId) {
        DistributorRpcDTO data = getDistributorInfo(distributorId);
        return getOrganizationInfoBySaleId(data.getSalesId());
    }

    /**
     * 通过业务员id获取业务员所属组织id
     * 
     * @param sealId
     * @return
     */
    @Override
    public OrganizationRpcDTO getOrganizationInfoBySaleId(Integer sealId) {
        Response<UserRpcDTO> response = systemUserServiceRpc.getUserById(sealId);
        if (response.isSuccess()) {
            return response.getData().getOrganizationDTO();
        } else {
            throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取组织id
     * 
     * @param distributorId
     * @return
     */
    public Integer getOrganizationId(Integer distributorId) {
        return getOrganizationInfo(distributorId).getId();
    }

    /**
     * 预创建订单 信息处理
     * 
     * 根据前端传递的订单号，去订单服务查询金额等信息
     */
    @Override
    public void preCreateTrade(CreateTradeCmd cmd) {
        // 如果有订单号直接默认是订单类型
        if (StringUtils.isNotBlank(cmd.getOrderId())) {
            cmd.setBusinessType(BillsBusinessType.ORDER);
            List<Integer> orderIds =
                Arrays.stream(cmd.getOrderId().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            OrderTradeRpcQry qry = new OrderTradeRpcQry();
            qry.setOrderIds(orderIds);
            // C端 B端 标志
            if (cmd.getCustomerFlag().equals(CustomerFlag.IS_NOT_CUSTOMER)) {
                qry.setCounterpartyType(CounterpartyType.DISTRIBUTOR);
                qry.setDistributorId(cmd.getPayerId());
            } else if (cmd.getCustomerFlag().equals(CustomerFlag.IS_CUSTOMER)) {
                qry.setCounterpartyType(CounterpartyType.CUSTOMER);
                qry.setCustomerId(cmd.getPayerId());
            }
            // 获取订单交易信息
            com.bat.dubboapi.order.common.Response<OrderTradeRpcDTO> orderTrade = orderServiceRpc.orderTrade(qry);
            if (orderTrade.isSuccess()) {
                OrderTradeRpcDTO data = orderTrade.getData();
                log.info("preCreateTrade data:{}", JSON.toJSONString(data));
                // 支付渠道
                fillPayChannel(cmd, data);
                // 组织
                cmd.setOrganizationId(data.getOrganizationId());
                // 货币单位
                cmd.setCurrencyCode(data.getCurrencyType());
                // 收款分销商
                cmd.setPayeeId(data.getDistributorId());
                // 付款金额
                AtomicReference<BigDecimal> payAmount = new AtomicReference<>(new BigDecimal(0));
                data.getOrderCosts().forEach(orderCostRpcDTO -> {
                    if (orderCostRpcDTO.getPaidAmount() == null) {
                        orderCostRpcDTO.setPaidAmount(BigDecimal.ZERO);
                    }
                    if (orderCostRpcDTO.getDepositAmount() == null) {
                        orderCostRpcDTO.setDepositAmount(BigDecimal.ZERO);
                    }
                    payAmount
                        .set(
                            payAmount.get()
                                .add(orderCostRpcDTO.getPayAmount().subtract(orderCostRpcDTO.getPaidAmount())
                                    .subtract(orderCostRpcDTO.getDepositAmount()))
                                .setScale(2, BigDecimal.ROUND_HALF_UP));
                });
                String orderids = data.getOrderCosts().stream().map(OrderCostRpcDTO::getOrderId).map(String::valueOf)
                    .collect(Collectors.joining(","));
                cmd.setOrderId(orderids);
                if (cmd.getPayMethod().toUpperCase().equals(PayChannel.BALANCE.name())) {
                    cmd.setAmount(payAmount.get());
                } else {
                    cmd.setAmount(payAmount.get());
                    // cmd.setAmount(BigDecimal.valueOf(0.01));
                }
                // 收款模式 1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)
                cmd.setTradeMode(data.getTradeMode());
            } else {
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ORDER_INFO_ERROR);
            }
        } else {
            cmd.setBusinessType(BillsBusinessType.RECHARGE);
        }
    }

    @Resource
    private PayCmdExc payCmdExc;

    /**
     * 预查询订单 信息处理
     * 
     * @param qry
     */
    @Override
    public void preQueryTrade(QueryTradeQry qry) {
        PayBillsDO billsDO = null;
        if (StringUtils.isNotBlank(qry.getOutTradeNo())) {
            billsDO = payCmdExc.getPayBillByOutTradeNo(null, qry.getOutTradeNo());
        } else if (StringUtils.isNotBlank(qry.getOrderId())) {
            billsDO = payCmdExc.getPayBillByOrderId(null, qry.getOrderId().split(",")[0], qry.getCustomerId());
        }
        if (billsDO == null) {
            throw FinancialException.buildException(ErrorCode.B_PAY_BILL_NOT_EXISTS);
        }
        log.info("preQueryTrade billsDO:{}", JSON.toJSONString(billsDO));
        qry.setOutTradeNo(billsDO.getOutTradeNo());
        qry.setTradeMode(billsDO.getTradeMode());
        qry.setPayeeId(billsDO.getPayeeId());
        qry.setOrganizationId(billsDO.getOrganizationId());
        fillPayChannel(qry, billsDO);
    }

    @Override
    public void preRefundTrade(RefundTradeCmd cmd) {
        PayBillsDO billsDO = null;
        if (StringUtils.isNotBlank(cmd.getOutTradeNo())) {
            billsDO = payCmdExc.getPayBillByOutTradeNo(null, cmd.getOutTradeNo());
        } else if (StringUtils.isNotBlank(cmd.getOrderId())) {
            billsDO = payCmdExc.getPayBillByOrderId(null, cmd.getOrderId().split(",")[0], cmd.getCustomerId());
        }
        if (billsDO == null) {
            throw FinancialException.buildException(ErrorCode.B_PAY_BILL_NOT_EXISTS);
        }
        log.info("preRefundTrade billsDO:{}", JSON.toJSONString(billsDO));
        cmd.setOutTradeNo(billsDO.getOutTradeNo());
        cmd.setTradeMode(billsDO.getTradeMode());
        cmd.setPayeeId(billsDO.getPayeeId());
        cmd.setOrganizationId(billsDO.getOrganizationId());
        cmd.setTotalAmount(billsDO.getTotalFee().doubleValue());
        cmd.setRefundAmount(billsDO.getTotalFee().doubleValue());
        cmd.setReason("测试退款");
        fillPayChannel(cmd, billsDO);
    }

    /**
     * 填充支付渠道相关(创建)
     * 
     * @param cmd
     *            前端调接口数据
     * @param data
     *            订单交易信息
     */
    private void fillPayChannel(CreateTradeCmd cmd, OrderTradeRpcDTO data) {
        cmd.setTitle("销售订单支付");
        if (data.getPayWay().equals(PayWay.ALIPAY)) {
            cmd.setPayChannel(PayChannel.ALIPAY.name());
            cmd.setDescription("销售订单支付宝支付");
        }
        if (data.getPayWay().equals(PayWay.WXPAY)) {
            short appType =
                cmd.getPayMethod().toUpperCase().equals(PayChannel.WXPAY_MINI_PROGRAM.name()) ? (short)2 : (short)1;
            // 判断微信支付版本 V2 V3版本
            if (data.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
                || data.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
                Integer distributorId = data.getDistributorId();

                List<AccountWxDistributorDO> accountWxDistributorDOList =
                    accountWxDistributorQryExc.listByCondition(distributorId, appType, cmd.getAppId(), null);
                if (accountWxDistributorDOList == null || accountWxDistributorDOList.size() == 0) {
                    throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NULL);
                }
                AccountWxDistributorDO accountWxDistributorDO = accountWxDistributorDOList.get(0);
                // 判断是否分账模式、分账走服务商
                Boolean serviceProviderChannelFlag =
                    checkIsServiceProviderChannel(data, accountWxDistributorDO.getAccountType());
                if (!serviceProviderChannelFlag) {
                    if (accountWxDistributorDO.getVersion().endsWith(WxApiVersion.V3)) {
                        cmd.setPayChannel(PayChannel.WXPAY_V3.name());
                    } else {
                        cmd.setPayChannel(PayChannel.WXPAY_V2.name());
                    }
                } else {
                    // 走服务商
                    cmd.setPayChannel(PayChannel.WXPAY_PARTNER_JSAPI.name());
                    if (StringUtils.isBlank(cmd.getPlatformUserId())) {
                        cmd.setPayChannel(PayChannel.WXPAY_PARTNER_NATIVE.name());
                    }
                    // 分销商关闭了分账
                    if (data.getSubAccountFlag().equals(DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_NO)) {
                        cmd.getSettleInfo().setProfitSharing(false);
                    }
                    cmd.setPayMethod(cmd.getPayChannel());
                }
            } else if (data.getTradeMode().equals(TradeMode.PLATFORM)) {
                AccountWxDTO account = accountWxQryExc.getByDistributorIdAndAppType(data.getOrganizationId(), appType);
                if (account == null) {
                    throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NULL);
                }
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    cmd.setPayChannel(PayChannel.WXPAY_V3.name());
                } else {
                    cmd.setPayChannel(PayChannel.WXPAY_V2.name());
                }
            }
            cmd.setDescription("销售订单微信支付");
        }
        if (data.getPayWay().equals(PayWay.KUAIQIAN)) {
            cmd.setPayChannel(PayChannel.KUAIQIAN.name());
            cmd.setDescription("销售订单快钱支付");
        }
        if (data.getPayWay().equals(PayWay.BALANCE)) {
            cmd.setPayChannel(PayChannel.BALANCE.name());
        }
        if (data.getPayWay().equals(PayWay.BALANCE_ALIPAY)) {
            // cmd.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        }
        if (data.getPayWay().equals(PayWay.BALANCE_WX)) {
            // cmd.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        }
        if (data.getPayWay().equals(PayWay.BALANCE_KUAIQIAN)) {
            // cmd.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        }
        // 校验支付渠道 与支付方式知否匹配
        String channelName = cmd.getPayChannel().toUpperCase();
        if (channelName.endsWith(WxApiVersion.V2) || channelName.endsWith(WxApiVersion.V3)) {
            channelName = cmd.getPayChannel().substring(0, cmd.getPayChannel().length() - 3);
        }
        if (!cmd.getPayMethod().toUpperCase().startsWith(channelName)) {
            throw FinancialException.buildException(ErrorCode.B_PAY_METHOD_ERROR);
        }
    }

    /**
     * 判断是否要走服务商支付渠道
     *
     * @param orderTradeRpcDTO
     * @param accountType
     * @return
     */
    private Boolean checkIsServiceProviderChannel(OrderTradeRpcDTO orderTradeRpcDTO, Short accountType) {
        if (accountType == null
            || DistributorAccountConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SELF.equals(accountType)) {
            // 自己收款
            return false;
        }
        if (orderTradeRpcDTO.getSubAccountFlag() == null || orderTradeRpcDTO.getSubAccountFlag() == 0) {
            return false;
        }
        if (!orderTradeRpcDTO.getShopOrderFlag()) {
            // 非门店订单、直接返回false
            return false;
        }
        if (DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_NO.equals(orderTradeRpcDTO.getCustomerFlag())) {
            // 不开C端模式、
            return false;
        }
        if (!DistributorConstant.DISTRIBUTOR_CUSTOMER_MODE_SELF.equals(orderTradeRpcDTO.getCustomerMode())) {
            // 非分销商自己收款
            return false;
        }
        // if(DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_NO.equals(orderTradeRpcDTO.getSubAccountFlag())){
        // return false;
        // }
        return true;
    }

    /**
     * 填充支付渠道相关 (查询)
     *
     * @param qry
     * @param data
     */
    private void fillPayChannel(QueryTradeQry qry, PayBillsDO data) {
        if (data.getPayType().equals(PayWay.ALIPAY)) {
            qry.setPayChannel(PayChannel.ALIPAY.name());
        } else if (data.getPayType().equals(PayWay.WXPAY)) {
            short appType =
                qry.getPayMethod().toUpperCase().equals(PayChannel.WXPAY_MINI_PROGRAM.name()) ? (short)2 : (short)1;
            // 判断微信支付版本 V2 V3版本
            if (data.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
                || data.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
                Integer distributorId = data.getPayeeId();
                AccountWxDistributorDTO account =
                    accountWxDistributorQryExc.getByDistributorIdAndAppType(distributorId, appType);
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    qry.setPayChannel(PayChannel.WXPAY_V3.name());
                } else {
                    qry.setPayChannel(PayChannel.WXPAY_V2.name());
                }
            } else if (data.getTradeMode().equals(TradeMode.PLATFORM)) {
                AccountWxDTO account = accountWxQryExc.getByDistributorIdAndAppType(data.getOrganizationId(), appType);
                if (account == null) {
                    throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NULL);
                }
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    qry.setPayChannel(PayChannel.WXPAY_V3.name());
                } else {
                    qry.setPayChannel(PayChannel.WXPAY_V2.name());
                }
            }
        } else if (data.getPayType().equals(PayWay.KUAIQIAN)) {
            qry.setPayChannel(PayChannel.KUAIQIAN.name());
        } else if (data.getPayType().equals(PayWay.BALANCE)) {
            qry.setPayChannel(PayChannel.BALANCE.name());
        } else if (data.getPayType().equals(PayWay.BALANCE_ALIPAY)) {
            // qry.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        } else if (data.getPayType().equals(PayWay.BALANCE_WX)) {
            // qry.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        } else if (data.getPayType().equals(PayWay.BALANCE_KUAIQIAN)) {
            // qry.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        }
        // 校验支付渠道 与支付方式知否匹配
        String channelName = qry.getPayChannel().toUpperCase();
        if (channelName.endsWith(WxApiVersion.V2) || channelName.endsWith(WxApiVersion.V3)) {
            channelName = qry.getPayChannel().substring(0, qry.getPayChannel().length() - 3);
        }
        if (!qry.getPayMethod().toUpperCase().startsWith(channelName)) {
            throw FinancialException.buildException(ErrorCode.B_PAY_METHOD_ERROR);
        }
    }

    /**
     * 填充支付渠道相关 (退款)
     *
     * @param qry
     * @param data
     */
    private void fillPayChannel(RefundTradeCmd qry, PayBillsDO data) {
        if (data.getPayType().equals(PayWay.ALIPAY)) {
            qry.setPayChannel(PayChannel.ALIPAY.name());
        } else if (data.getPayType().equals(PayWay.WXPAY)) {
            short appType =
                qry.getPayMethod().toUpperCase().equals(PayChannel.WXPAY_MINI_PROGRAM.name()) ? (short)2 : (short)1;
            // 判断微信支付版本 V2 V3版本
            if (data.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
                || data.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
                Integer distributorId = data.getPayeeId();
                AccountWxDistributorDTO account =
                    accountWxDistributorQryExc.getByDistributorIdAndAppType(distributorId, appType);
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    qry.setPayChannel(PayChannel.WXPAY_V3.name());
                } else {
                    qry.setPayChannel(PayChannel.WXPAY_V2.name());
                }
            } else if (data.getTradeMode().equals(TradeMode.PLATFORM)) {
                AccountWxDTO account = accountWxQryExc.getByDistributorIdAndAppType(data.getOrganizationId(), appType);
                if (account == null) {
                    throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NULL);
                }
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    qry.setPayChannel(PayChannel.WXPAY_V3.name());
                } else {
                    qry.setPayChannel(PayChannel.WXPAY_V2.name());
                }
            }
        } else if (data.getPayType().equals(PayWay.KUAIQIAN)) {
            qry.setPayChannel(PayChannel.KUAIQIAN.name());
        } else if (data.getPayType().equals(PayWay.BALANCE)) {
            qry.setPayChannel(PayChannel.BALANCE.name());
        } else if (data.getPayType().equals(PayWay.BALANCE_ALIPAY)) {
            // qry.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        } else if (data.getPayType().equals(PayWay.BALANCE_WX)) {
            // qry.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        } else if (data.getPayType().equals(PayWay.BALANCE_KUAIQIAN)) {
            // qry.setPayChannel(PayChannel.BALANCE.name());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
        }
        // 校验支付渠道 与支付方式知否匹配
        String channelName = qry.getPayChannel().toUpperCase();
        if (channelName.endsWith(WxApiVersion.V2) || channelName.endsWith(WxApiVersion.V3)) {
            channelName = qry.getPayChannel().substring(0, qry.getPayChannel().length() - 3);
        }
        if (!qry.getPayMethod().toUpperCase().startsWith(channelName)) {
            throw FinancialException.buildException(ErrorCode.B_PAY_METHOD_ERROR);
        }
    }

    /**
     * 根据payMethod 获取 payChannel
     *
     * @param payMethod
     * @param tradeMode
     * @param distributorId
     * @param organizationId
     * @return
     */
    @Override
    public String getPayChannel(String payMethod, Short tradeMode, Integer distributorId, Integer organizationId) {
        String payChannelName = payMethod.split("_")[0];
        if (payChannelName.equals(PayChannel.ALIPAY.name())) {
            return PayChannel.ALIPAY.name();
        }
        if ("WXPAY".equals(payChannelName)) {
            short appType = payMethod.toUpperCase().equals(PayChannel.WXPAY_MINI_PROGRAM.name()) ? (short)2 : (short)1;
            // 判断微信支付版本 V2 V3版本
            if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
                AccountWxDistributorDTO account =
                    accountWxDistributorQryExc.getByDistributorIdAndAppType(distributorId, appType);
                if (account == null) {
                    throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NULL);
                }
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    return PayChannel.WXPAY_V3.name();
                } else {
                    return PayChannel.WXPAY_V2.name();
                }
            } else if (tradeMode.equals(TradeMode.PLATFORM)) {
                AccountWxDTO account = accountWxQryExc.getByDistributorIdAndAppType(organizationId, appType);
                if (account == null) {
                    throw FinancialException.buildException(ErrorCode.B_ACCOUNT_NULL);
                }
                if (account.getVersion().endsWith(WxApiVersion.V3)) {
                    return PayChannel.WXPAY_V3.name();
                } else {
                    return PayChannel.WXPAY_V2.name();
                }
            }
        }
        if (payChannelName.equals(PayChannel.KUAIQIAN.name())) {
            return PayChannel.KUAIQIAN.name();
        }
        if (payChannelName.equals(PayChannel.BALANCE.name())) {
            return PayChannel.BALANCE.name();
        }
        throw FinancialException.buildException(ErrorCode.B_GET_PAY_CHANNEL_PAY_METHOD_ERROR);
    }

    /**
     * 明细与 余额 数据互相补充
     * 
     * @param distributorDTO
     * @param bookDTO
     */
    @Override
    public void calcBalanceAndBook(DepositDistributorDTO distributorDTO, DepositDistributorSubsidiaryBookDTO bookDTO) {
        // distributorDTO->bookDTO
        if (bookDTO.getBeforeDepositAmount() == null) {
            // 最重要的数值
            if (distributorDTO.getAccountBalance() == null || distributorDTO.getAccountAvailable() == null) {
                throw FinancialException
                    .buildException(com.bat.financial.api.base.ErrorCode.B_ACCOUNT_AMOUNT_EXCEPTION);
            }
            bookDTO.setDepositDistributorId(distributorDTO.getId());
            bookDTO.setBeforeDepositAmount(distributorDTO.getAccountBalance());
            bookDTO.setCreateTime(new Date());
            // 补全分销商名称
            if (StringUtils.isBlank(bookDTO.getDistributorName())) {
                if (StringUtils.isNotBlank(distributorDTO.getDistributorName())) {
                    bookDTO.setDistributorName(distributorDTO.getDistributorName());
                } else {
                    DistributorRpcDTO distributorInfo = null;
                    try {
                        distributorInfo = getDistributorInfo(bookDTO.getDistributorId());
                        bookDTO.setDistributorName(distributorInfo.getName());
                    } catch (Exception e) {
                        bookDTO.setDistributorName("unknown");
                        e.printStackTrace();
                    }
                }
            }
            if (bookDTO.getChangeType() == null) {
                throw FinancialException
                    .buildException(com.bat.financial.api.base.ErrorCode.B_ACCOUNT_CHANGE_TYPE_NULL);
            } else {
                // bookDTO->distributorDTO
                if (bookDTO.getChangeType().equals(ChangeType.INCREASE)) {
                    BigDecimal add = distributorDTO.getAccountBalance().add(bookDTO.getAmount());
                    distributorDTO.setAccountBalance(add);
                    distributorDTO.setAccountAvailable(distributorDTO.getAccountAvailable().add(bookDTO.getAmount()));
                    bookDTO.setAfterDepositAmount(add);
                    switch (bookDTO.getBusinessType()) {
                        case DepositBusinessType.RECHARGE:
                            distributorDTO
                                .setRechargeAmount(distributorDTO.getRechargeAmount().add(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.WITHDRAWAL:
                            distributorDTO
                                .setWithdrawAmount(distributorDTO.getWithdrawAmount().add(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.ORDER_CREATE_SUB:
                            distributorDTO
                                .setConsumerAmount(distributorDTO.getConsumerAmount().add(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.ORDER_CANCEL_ADD:
                            distributorDTO.setRefundAmount(distributorDTO.getRefundAmount().add(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.ORDER_ADJUSTMENT:
                            break;
                        case DepositBusinessType.ERP_INCREMENT:
                            break;
                        case DepositBusinessType.ERP_TOTAL:
                            break;
                        case DepositBusinessType.DISTRIBUTION_COMMISSION:
                            distributorDTO
                                .setCommissionAmount(distributorDTO.getCommissionAmount().add(bookDTO.getAmount()));
                            break;
                        default:
                            break;
                    }
                } else if (bookDTO.getChangeType().equals(ChangeType.DECREASE)) {
                    BigDecimal subtract = distributorDTO.getAccountBalance().subtract(bookDTO.getAmount());
                    distributorDTO.setAccountBalance(subtract);
                    distributorDTO
                        .setAccountAvailable(distributorDTO.getAccountAvailable().subtract(bookDTO.getAmount()));
                    bookDTO.setAfterDepositAmount(subtract);
                    switch (bookDTO.getBusinessType()) {
                        case DepositBusinessType.RECHARGE:
                            distributorDTO
                                .setRechargeAmount(distributorDTO.getRechargeAmount().subtract(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.WITHDRAWAL:
                            distributorDTO
                                .setWithdrawAmount(distributorDTO.getWithdrawAmount().subtract(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.ORDER_CREATE_SUB:
                            distributorDTO
                                .setConsumerAmount(distributorDTO.getConsumerAmount().subtract(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.ORDER_CANCEL_ADD:
                            distributorDTO
                                .setRefundAmount(distributorDTO.getRefundAmount().subtract(bookDTO.getAmount()));
                            break;
                        case DepositBusinessType.ORDER_ADJUSTMENT:
                            break;
                        case DepositBusinessType.ERP_INCREMENT:
                            break;
                        case DepositBusinessType.ERP_TOTAL:
                            break;
                        case DepositBusinessType.DISTRIBUTION_COMMISSION:
                            distributorDTO.setCommissionAmount(
                                distributorDTO.getCommissionAmount().subtract(bookDTO.getAmount()));
                            break;
                        default:
                            break;
                    }
                } else {
                    throw FinancialException
                        .buildException(com.bat.financial.api.base.ErrorCode.B_ACCOUNT_CHANGE_TYPE_EXCEPTION);
                }
            }

        }

    }

    @Value("${erp.settle.account.alipay}")
    private String erpSettleAccountAlipay;

    @Value("${erp.settle.account.wxpay}")
    private String erpSettleAccountWxpay;

    @Value("${erp.settle.account.kuaiqian}")
    private String erpSettleAccountKuaiqian;

    @Value("${erp.settle.account.company-transfer}")
    private String erpSettleAccountCompanyTransfer;

    @Resource
    private AccountAlipayQryExc accountAlipayQryExc;

    @Resource
    private AccountKuaiQianQryExc accountKuaiQianQryExc;

    @Resource
    private AccountOfflineQryExc accountOfflineQryExc;

    @Override
    public BaseBillEntity getEntity(Integer organizationId, Short payWay, String outTradeNo) {
        BaseBillEntity entry = new BaseBillEntity();
        switch (payWay) {
            case PayWay.ALIPAY:
                AccountAlipayDTO accountByOrganization = accountAlipayQryExc.getAccountByOrganization(organizationId);
                entry.setFACCOUNTID(accountByOrganization.getBankNo());
                entry.setFSETTLETYPEID(accountByOrganization.getErpAccountNo());
                break;
            case PayWay.WXPAY:

                short appType = 1;
                if (StringUtils.isNotBlank(outTradeNo)) {
                    PayBillsDO payBillByOutTradeNo = payCmdExc.getPayBillByOutTradeNo(null, outTradeNo);
                    if (payBillByOutTradeNo != null) {
                        if (payBillByOutTradeNo.getPayMethod().toUpperCase()
                            .equals(PayChannel.WXPAY_MINI_PROGRAM.name())) {
                            appType = 2;
                        }
                    }
                }
                AccountWxDTO accountByOrganization1 =
                    accountWxQryExc.getAccountByOrganizationAndAppType(organizationId, appType);
                entry.setFACCOUNTID(accountByOrganization1.getBankNo());
                entry.setFSETTLETYPEID(accountByOrganization1.getErpAccountNo());
                break;
            case PayWay.KUAIQIAN:
                AccountKuaiQianDTO accountByOrganization2 =
                    accountKuaiQianQryExc.getAccountByOrganization(organizationId);
                entry.setFACCOUNTID(accountByOrganization2.getBankNo());
                entry.setFSETTLETYPEID(accountByOrganization2.getErpAccountNo());
                break;
            case PayWay.OFFLINE:
                AccountOfflineDTO accountByOrganization3 =
                    accountOfflineQryExc.getAccountByOrganization(organizationId);
                entry.setFACCOUNTID(accountByOrganization3.getBankNo() + "");
                entry.setFSETTLETYPEID(accountByOrganization3.getErpAccountNo());
                break;
            default:
                break;
        }
        return entry;
    }

    /**
     * 根据租户编码获取后端接口主机域名
     * 
     * @param tenantNo
     * @return
     */
    @Override
    public String getHostByTenantNo(String tenantNo) {
        com.bat.dubboapi.platform.common.Response<PlatformTenantUrlRpcDTO> response =
            platformServiceRpc.urlConfig(tenantNo, URL_TYPE_6);
        if (response.isSuccess() && response.getData() != null) {
            return response.getData().getHost();
        } else {
            throw FinancialException.buildException(COMMON_TENANT_URL_ERROR);
        }
    }
}
