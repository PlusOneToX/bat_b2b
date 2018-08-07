package com.bat.financial.pay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.api.pay.dto.data.*;
import com.bat.financial.common.constant.FinancialConstant;
import com.bat.financial.common.constant.subaccount.OrderSubAccountConstant;
import com.bat.financial.common.error.DistributorAccountErrorCode;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import com.bat.financial.distributoraccount.executor.AccountWxDistributorQryExc;
import com.bat.financial.pay.config.WechatConfig;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.constant.TradeStatus;
import com.bat.financial.pay.data.WxConfig;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.pay.utils.WXPayUtil;
import com.bat.financial.pay.utils.WechatUtil;
import com.bat.financial.subaccount.executor.OrderSubAccountBillCmdExe;
import com.bat.financial.subaccount.executor.OrderSubAccountBillQryExe;
import com.bat.financial.subaccount.executor.OrderSubAccountCmdExe;
import com.bat.financial.subaccount.executor.OrderSubAccountQryExe;
import com.bat.financial.subaccount.validator.SubAccountValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.base.Response;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.dto.wechat.WxPayPartnerCreateOrderCmd;
import com.bat.financial.api.pay.dto.wechat.WxPaySettleInfo;
import com.bat.financial.api.pay.notify.CreateNotifyReq;
import com.bat.financial.api.pay.notify.CreateNotifyResp;
import com.bat.financial.api.pay.notify.RefundNotifyReq;
import com.bat.financial.api.pay.notify.RefundNotifyResp;
import com.bat.financial.api.subaccount.dto.OrderSubAccountIdCmd;
import com.bat.financial.api.subaccount.dto.WechatPaySubAccountCmd;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

@Service("WxPayPartnerServiceImpl")
public class WxPayPartnerServiceImpl implements PayService {

    @Value("${wechat.gateway}")
    private String gateway;

    private static final Logger log = LoggerFactory.getLogger(WxPayPartnerServiceImpl.class);

    @Autowired
    private com.bat.financial.pay.convertor.WxPayConvertor WxPayConvertor;

    @Autowired
    private PayCmdExc payCmdExc;

    @Autowired
    private OrderSubAccountQryExe orderSubAccountQryExe;

    @Autowired
    private OrderSubAccountCmdExe orderSubAccountCmdExe;

    @Autowired
    private OrderSubAccountBillQryExe orderSubAccountBillQryExe;

    @Autowired
    private OrderSubAccountBillCmdExe orderSubAccountBillCmdExe;

    @Autowired
    private SubAccountValidator subAccountValidator;

    @Autowired
    private AccountWxDistributorQryExc accountWxDistributorQryExc;

    @Resource
    private WechatConfig wechatConfig;

    /**
     * 锁分账流水
     */
    @CreateCache(name = FinancialConstant.WECHAT_PARTNER_SUB_ACCOUNT_BILL_LOCK_KEY)
    private Cache<String, Integer> wechatPartnewSubAccountRedisCache;

    @Override
    public CreateTradeRespDTO createTrade(CreateTradeCmd cmd) {
        Date date = new Date();
        WxConfig wxConfig = getWxConfig(cmd);
        confirmOldTrade(cmd);
        WxPayPartnerCreateOrderCmd createOrderCmd = WxPayConvertor.toWxPayPartnerCreateOrder(cmd, wxConfig, date);
        // 最终收款的子商户号
        cmd.setMchid(wxConfig.getSubMchid());
        // 服务商商户号
        cmd.setSpMchid(wxConfig.getAccountNo());
        WxPaySettleInfo wxPaySettleInfo = new WxPaySettleInfo();
        wxPaySettleInfo.setProfit_sharing(cmd.getSettleInfo().isProfitSharing());
        createOrderCmd.setSettle_info(wxPaySettleInfo);
        log.info("微信服务商下单{}", JSON.toJSONString(createOrderCmd));
        HttpClient httpClient = WechatUtil.getWechatHttpClientV3(wxConfig);
        HttpPost httpPost =
            WechatUtil.getHttpPost(wechatConfig.getWxPayPartnerCreateJsapiUrl(), JSON.toJSONString(createOrderCmd));
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity, Charset.defaultCharset());
                log.info("微信服务商下单返回{}", result);

                CreateTradeRespDTO.WXCreateTradeRespDTO respDTO = new CreateTradeRespDTO.WXCreateTradeRespDTO();
                JSONObject jsonObject = JSONObject.parseObject(result);
                respDTO.setPrepayId(jsonObject.getString("prepay_id"));

                CreateTradeRespDTO dto;
                try {
                    dto = getResp(cmd.getPayMethod().toUpperCase(), wxConfig, createOrderCmd, respDTO,
                        cmd.getRedirectUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, "下单失败");
                }
                log.info("微信拼装返回参数 json:{}", JSON.toJSONString(dto));
                // 存储支付凭证 待支付
                payCmdExc.createPayBills(cmd, createOrderCmd.getOut_trade_no(), date);
                return dto;
            } else {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity, Charset.defaultCharset());
                log.info("微信服务商下单、失败返回{}", result);
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, "下单失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, "下单失败");
        }
    }

    private WechatPayHttpClientBuilder getBuild(WxConfig config) {
        PrivateKey merchantPrivateKey;
        try {
            merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(config.getApiclientKey().getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw FinancialException.buildException(ErrorCode.B_INVALID_KEY_FORMAT);
        }
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
            new WechatPay2Credentials(config.getAccountNo(),
                new PrivateKeySigner(config.getSerialNumber(), merchantPrivateKey)),
            config.getAppKey().getBytes(StandardCharsets.UTF_8));
        return WechatPayHttpClientBuilder.create()
            .withMerchant(config.getAccountNo(), config.getSerialNumber(), merchantPrivateKey)
            .withValidator(new WechatPay2Validator(verifier));
    }

    @Override
    public QueryTradeRespDTO queryTrade(QueryTradeQry qry) {
        WxConfig config = payCmdExc.getWxConfig(qry.getTradeMode(), qry.getAppType(), qry.getPayeeId(),
            qry.getOrganizationId(), qry.getOutTradeNo());
        WechatPayHttpClientBuilder builder = getBuild(config);
        HttpClient httpClient = builder.build();
        HttpGet httpGet = new HttpGet(
            gateway + "/pay/transactions/out-trade-no/" + qry.getOutTradeNo() + "?mchid=" + config.getAccountNo());
        httpGet.addHeader("Accept", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse response;
        String bodyAsString = null;
        QueryTradeRespDTO dto = new QueryTradeRespDTO();
        try {
            response = httpClient.execute(httpGet);
            bodyAsString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
                QueryTradeRespDTO.WXQueryTradeRespDTO wxQueryTradeRespDTO =
                    objectMapper.readValue(bodyAsString, QueryTradeRespDTO.WXQueryTradeRespDTO.class);
                dto.setTradeState(wxQueryTradeRespDTO.getTradeState());
                return dto;
            } else {
                TradeErrorRespDTO errorRespDTO = objectMapper.readValue(bodyAsString, TradeErrorRespDTO.class);
                log.error(errorRespDTO.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        dto.setTradeState(TradeStatus.ERROR);
        return dto;
    }

    @Override
    public boolean closeTrade(CloseTradeCmd cmd) {
        return false;
    }

    @Override
    public RefundTradeRespDTO refundTrade(RefundTradeCmd cmd) {
        return null;
    }

    @Override
    public QueryRefundDTO queryRefund(QueryRefundCmd cmd) {
        return null;
    }

    @Override
    public CreateNotifyResp createTradeNotify(CreateNotifyReq notify) {
        return null;
    }

    @Override
    public RefundNotifyResp refundTradeNotify(RefundNotifyReq req) {
        return null;
    }

    /**
     * 逻辑是 公众号或小程序支付 以前端传的值为主
     *
     * 但是如果是native h5支付 前端没有传值的情况下，从数据库中取值，并把改值存到cmd,以此存在支付凭证表中
     *
     * @param cmd
     * @return
     */
    private WxConfig getWxConfig(BaseTrade cmd) {
        WxConfig wxConfig =
            payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(), cmd.getOrganizationId());
        if (StringUtils.isNotBlank(cmd.getAppId())) {
            wxConfig.setAppId(cmd.getAppId());
        } else {
            cmd.setAppId(wxConfig.getAppId());
        }
        log.info("微信服务商 wxConfig：{}", wxConfig);
        return wxConfig;
    }

    /**
     * 封装统一下单 结果
     *
     * @param payMethod
     * @param config
     * @param tradeDTO
     * @param respDTO
     * @param redirectUrl
     * @return
     * @throws Exception
     */
    private CreateTradeRespDTO getResp(String payMethod, WxConfig config, WxPayPartnerCreateOrderCmd tradeDTO,
        CreateTradeRespDTO.WXCreateTradeRespDTO respDTO, String redirectUrl) throws Exception {
        CreateTradeRespDTO dto = new CreateTradeRespDTO();
        if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_PARTNER_JSAPI) {
            respDTO.setAppId(config.getAppId());
            respDTO.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
            respDTO.setPrepayId("prepay_id=" + respDTO.getPrepayId());
            respDTO.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
            respDTO.setSignType("RSA");
            Map<String, String> mapData = new HashMap<>(16);
            mapData.put("appId", respDTO.getAppId());
            mapData.put("timeStamp", respDTO.getTimeStamp());
            mapData.put("nonceStr", respDTO.getNonceStr());
            mapData.put("package", respDTO.getPrepayId());
            mapData.put("signType", respDTO.getSignType());
            respDTO.setPaySign(WXPayUtil.generateSignature(mapData, config.getApiclientKey()));
        } else if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_PARTNER_NATIVE) {
            if (StringUtils.isBlank(redirectUrl)) {
                throw FinancialException.buildException(ErrorCode.B_REDIRECT_URL_NULL);
            }
            if (StringUtils.isNotBlank(redirectUrl)) {
                respDTO.setH5Url(respDTO.getH5Url() + "&redirect_url=" + URLEncoder.encode(redirectUrl, "UTF-8"));
            }
        }
        respDTO.setOutTradeNo(tradeDTO.getOut_trade_no());
        dto.setWxResp(respDTO);
        return dto;
    }

    private WxConfig getWxConfig(Integer distributorId, String appId, String subMchid) {
        List<AccountWxDistributorDO> wxDistributorDOList =
            accountWxDistributorQryExc.listByCondition(distributorId, null, appId, null);
        if (wxDistributorDOList == null || wxDistributorDOList.size() == 0) {
            throw FinancialException.buildException(
                DistributorAccountErrorCode.P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL,
                MessageUtils.get(DistributorAccountErrorCode.P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL));
        }
        AccountWxDistributorDO accountWxDistributorDO = wxDistributorDOList.get(0);
        WxConfig wxConfig = new WxConfig();
        wxConfig.setAppId(accountWxDistributorDO.getAppId());
        wxConfig.setAppKey(accountWxDistributorDO.getAppKey());
        wxConfig.setAccountNo(accountWxDistributorDO.getAccountNo());
        wxConfig.setApiclientKey(accountWxDistributorDO.getApiclientKey());
        wxConfig.setSerialNumber(accountWxDistributorDO.getSerialNumber());
        // 如果子商户号带过来则取
        wxConfig.setSubMchid(StringUtils.isBlank(subMchid) ? accountWxDistributorDO.getSubMchid() : subMchid);
        return wxConfig;
    }

    /**
     * 微信分账
     * 
     * @param orderSubAccountIdCmd
     * @param orderSubAccountDO
     *            不为空表示人工手动分账
     * @param billDOList
     *            不为空表示人工手动
     */
    public Response subAccountToWxPartner(OrderSubAccountIdCmd orderSubAccountIdCmd,
                                          OrderSubAccountDO orderSubAccountDO, List<OrderSubAccountBillDO> billDOList) {
        log.info("orderSubAccountIdCmd:{}", JSON.toJSONString(orderSubAccountIdCmd));
        log.info("orderSubAccountDO:{}", JSON.toJSONString(orderSubAccountDO));
        log.info("billDOList:{}", JSON.toJSONString(billDOList));
        List<Integer> billIdList = orderSubAccountIdCmd.getBillIdList();
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        billIdList.forEach(id -> {
            AutoReleaseLock autoReleaseLock =
                wechatPartnewSubAccountRedisCache.tryLock(TenantContext.getTenantNo() + ":" + id, 5, TimeUnit.MINUTES);
            autoReleaseLockList.add(autoReleaseLock);
        });
        if (billDOList == null || billDOList.size() == 0) {
            // 消息消费（不为空是手动触发、参数会带过来）
            try {
                // 判断订单是否分账完毕
                orderSubAccountDO = subAccountValidator.validOrderSubStatus(orderSubAccountIdCmd.getId());
                // 判断流水是否已经分账成功
                billDOList = subAccountValidator.validBillStatus(billIdList);
                log.info("billDOList:{}", JSON.toJSONString(billDOList));
            } catch (FinancialException e) {
                e.printStackTrace();
                autoReleaseLockList.forEach(AutoReleaseLock::close);
                return Response.buildFailure(e.getCode(), e.getMsg());
            }
        }
        Response response = new Response();
        response.setSuccess(true);
        String lastTransactionId = orderSubAccountDO.getLastTransactionId();
        try {
            // 组装参数
            WxConfig wxConfig = getWxConfig(orderSubAccountDO.getDistributorId(), orderSubAccountIdCmd.getAppId(),
                orderSubAccountDO.getSubMchid());
            WechatPaySubAccountCmd cmd =
                WxPayConvertor.toWechatPaySubAccountCmd(orderSubAccountDO, billDOList, orderSubAccountIdCmd.getAppId());
            log.info("微信服务商分账{}", JSON.toJSONString(cmd));
            HttpClient httpClient = WechatUtil.getWechatHttpClientV3(wxConfig);
            HttpPost httpPost =
                WechatUtil.getHttpPost(wechatConfig.getWxPayPartnerSubAccountCreateUrl(), JSON.toJSONString(cmd));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity, Charset.defaultCharset());
                log.info("微信服务商分账返回{}", result);
                // 一定要设置微信返回来的流水号
                JSONObject jsonObject = JSON.parseObject(result);
                String transaction_id = jsonObject.getString("transaction_id");
                billDOList.forEach(orderSubAccountBillDO -> {
                    orderSubAccountBillDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL);
                    orderSubAccountBillDO
                        .setSuccessFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_SUCCESS);
                    orderSubAccountBillDO.setFailReason(null);
                    orderSubAccountBillDO.setSubTransactionId(transaction_id);
                    orderSubAccountBillDO.setSuccessTime(new Date());
                    orderSubAccountBillDO.setOutTradeNo(lastTransactionId);
                });
                orderSubAccountDO.setSubAccountFailFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_NO);
                orderSubAccountDO.setLastTransactionId(transaction_id);
            } else {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity, Charset.defaultCharset());
                log.info("微信服务商分账、失败返回{}", result);
                if (result.length() > 255) {
                    // 截取字符串
                    result = result.substring(0, 255);
                }
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, result);
            }
        } catch (FinancialException e) {
            e.printStackTrace();
            log.error("分账失败{}", e.getMsg());
            // 不能再抛异常、要不然记录不了失败原因
            billDOList.stream().forEach(orderSubAccountBillDO -> {
                orderSubAccountBillDO.setSuccessFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_FAIL);
                orderSubAccountBillDO.setFailReason(e.getMsg());
                orderSubAccountBillDO.setOutTradeNo(lastTransactionId);
            });
            orderSubAccountDO.setSubAccountFailFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_YES);
            response.setSuccess(false);
            response.setErrCode(e.getCode());
            response.setErrMessage(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分账系统异常{}", e.getMessage());
            // 系统级别异常
            billDOList.stream().forEach(orderSubAccountBillDO -> {
                orderSubAccountBillDO.setSuccessFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_FAIL);
                orderSubAccountBillDO
                    .setFailReason(MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
            });
            orderSubAccountDO.setSubAccountFailFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_YES);
            response.setErrCode(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION);
            response.setErrMessage(MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        } finally {
            orderSubAccountBillCmdExe.batchUpdate(billDOList);
            orderSubAccountDO.setUpdateTime(new Date());
            // 判断是否全都分账成功
            if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL.equals(billDOList.get(0).getStatus())) {
                List<OrderSubAccountBillDO> accountBillDOList = orderSubAccountBillQryExe
                    .listByCondition(orderSubAccountDO.getId(), OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN);
                if (accountBillDOList == null || accountBillDOList.size() == 0) {
                    // 全都分账了
                    orderSubAccountDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL);
                }
            }
            orderSubAccountCmdExe.update(orderSubAccountDO);
            autoReleaseLockList.forEach(AutoReleaseLock::close);
            return response;
        }

    }

    /**
     * 确认旧订单 并处理
     *
     * @return
     */
    private void confirmOldTrade(CreateTradeCmd cmd) {
        if (StringUtils.isNotBlank(cmd.getOrderId())) {
            log.info("cmd json:{}", JSON.toJSONString(cmd));
            PayBillsDO payBillByOrderId =
                payCmdExc.getPayBillByOrderId(cmd.getCustomerFlag(), cmd.getOrderId().split(",")[0], cmd.getPayerId());
            // 存在旧的交易
            if (payBillByOrderId != null) {
                log.info("存在旧的交易，payBillByOrderId：{}", JSON.toJSONString(payBillByOrderId));
                if (payBillByOrderId.getPayStatus().equals(PayStatus.COMPLETE_PAYMENT)) {
                    throw FinancialException.buildException(ErrorCode.B_TRADE_HAS_BEEN_PAID);
                } else if (payBillByOrderId.getPayStatus().equals(PayStatus.WAIT_PAYMENT)) {
                    confirmWaitTradeTrade(cmd, payBillByOrderId);
                }
            }
        }
    }

    private void confirmWaitTradeTrade(CreateTradeCmd cmd, PayBillsDO payBillByOrderId) {
        // 这个时候要向付款机构求证 不能直接关闭 客户往往第一次支付 第二次不会支付
        QueryTradeQry qry = new QueryTradeQry();
        BeanUtils.copyProperties(cmd, qry);
        qry.setOutTradeNo(payBillByOrderId.getOutTradeNo());
        QueryTradeRespDTO queryTradeRespDTO = queryTrade(qry);
        Date expireTime = payBillByOrderId.getExpireTime();
        if (queryTradeRespDTO.getTradeState().equals(TradeStatus.SUCCESS)) {
            log.info("旧的交易已经支付");
            throw FinancialException.buildException(ErrorCode.B_TRADE_HAS_BEEN_PAID);
        } else {
            Date now = new Date();
            if (queryTradeRespDTO.getTradeState().equals(TradeStatus.NOTPAY) && now.before(expireTime)) {
                // 待支付状态 并且在自定义的失效时间之内（2分钟 正常情况下已经失效）
                log.info("旧的交易待支付");
                long timeSlot = (expireTime.getTime() - now.getTime()) / 1000;
                throw FinancialException.buildException(ErrorCode.B_TRADE_WAIT_TRADE,
                    MessageFormat.format(MessageUtils.get(ErrorCode.B_TRADE_WAIT_TRADE), timeSlot));
            } else {
                log.info("旧的交易未支付,又调起新的交易，原订单应该关闭");
                CloseTradeCmd closeTradeCmd = new CloseTradeCmd();
                BeanUtils.copyProperties(cmd, closeTradeCmd);
                closeTradeCmd.setOutTradeNo(payBillByOrderId.getOutTradeNo());
                // 开启子线程需指定租户编码 关闭失败不影响第二次下单
                try {
                    closeTrade(closeTradeCmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
