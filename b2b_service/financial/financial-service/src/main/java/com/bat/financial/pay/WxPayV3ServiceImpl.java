package com.bat.financial.pay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.api.pay.dto.data.*;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.constant.TradeStatus;
import com.bat.financial.pay.constant.WXRefundStatus;
import com.bat.financial.pay.constant.WxPayReturnCode;
import com.bat.financial.pay.data.WxConfig;
import com.bat.financial.pay.data.wxv3.WxCreateTradeReqDTO;
import com.bat.financial.pay.data.wxv3.WxPayNotifyCipherRespDTO;
import com.bat.financial.pay.data.wxv3.WxRefundNotifyCipherRespDTO;
import com.bat.financial.pay.data.wxv3.WxRefundTradeReqDTO;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.pay.utils.DateUtils;
import com.bat.financial.pay.utils.TradeNoUtils;
import com.bat.financial.pay.utils.WXPayUtil;
import com.bat.financial.refund.constant.RefundBillsStatus;
import com.bat.financial.refund.executor.RefundBillsCmdExc;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.pay.notify.CreateNotifyReq;
import com.bat.financial.api.pay.notify.CreateNotifyResp;
import com.bat.financial.api.pay.notify.RefundNotifyReq;
import com.bat.financial.api.pay.notify.RefundNotifyResp;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: v3版api restful风格
 * @date: 2018/5/20 17:26
 */
@Service("WxPayV3ServiceImpl")
@Slf4j
public class WxPayV3ServiceImpl implements PayService {

    @Value("${wechat.gateway}")
    private String gateway;

    @Value("${wechat.notify_url_create_v3}")
    private String notifyUrlCreateV3;

    @Value("${wechat.notify_url_refund_v3}")
    private String notifyUrlRefundV3;

    @Resource
    private PayCmdExc payCmdExc;

    @Resource
    private CommonService commonService;

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

    @SneakyThrows
    @Override
    public CreateTradeRespDTO createTrade(CreateTradeCmd cmd) {
        Date date = new Date();
        WxConfig config = getWxConfig(cmd);
        confirmOldTrade(cmd);
        ObjectMapper objectMapper = new ObjectMapper();
        CreateTradeRespDTO.WXCreateTradeRespDTO respDTO;
        HttpClient httpClient = getBuild(config).build();
        HttpPost httpPost = getHttpPost(cmd);
        WxCreateTradeReqDTO tradeDTO = createTradeDTO(config, cmd, date);
        String paramsJson = objectMapper.writeValueAsString(tradeDTO);
        log.info("微信支付V3 请求 url:{}, json：{}", httpPost.getURI(), paramsJson);
        httpPost.setEntity(new StringEntity(paramsJson, "UTF-8"));;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String bodyAsString = EntityUtils.toString(response.getEntity());
            log.info("微信支付V3 返回  json：{}", bodyAsString);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respDTO = objectMapper.readValue(bodyAsString, CreateTradeRespDTO.WXCreateTradeRespDTO.class);
                CreateTradeRespDTO dto =
                    getResp(cmd.getPayMethod().toUpperCase(), config, tradeDTO, respDTO, cmd.getRedirectUrl());
                log.info("微信拼装返回参数 json:{}", JSON.toJSONString(dto));
                // 存储支付凭证 待支付
                payCmdExc.createPayBills(cmd, tradeDTO.getOutTradeNo(), date);
                return dto;
            } else {
                TradeErrorRespDTO errorRespDTO = objectMapper.readValue(bodyAsString, TradeErrorRespDTO.class);
                log.error(errorRespDTO.toString());
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, errorRespDTO.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, e.getMessage());
        }
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
        log.info("微信支付V2 wxConfig：{}", wxConfig);
        return wxConfig;
    }

    /**
     * 创建 HTTP POST 请求
     * 
     * @param cmd
     * @return
     */
    private HttpPost getHttpPost(CreateTradeCmd cmd) {
        HttpPost httpPost = new HttpPost();
        if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_NATIVE) {
            httpPost.setURI(URI.create(gateway + "/pay/transactions/native"));
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_JSAPI
            || PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_MINI_PROGRAM) {
            httpPost.setURI(URI.create(gateway + "/pay/transactions/jsapi"));
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_APP) {
            httpPost.setURI(URI.create(gateway + "/pay/transactions/app"));
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_H5) {
            httpPost.setURI(URI.create(gateway + "/pay/transactions/h5"));
        } else {
            // 合单支付暂时不支持（目前没必要）
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_NOT_SUPPORT);
        }
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        return httpPost;
    }

    /**
     * 封装统一下单参数
     *
     * @param config
     * @param cmd
     * @param date
     * @return
     */
    @SneakyThrows
    private WxCreateTradeReqDTO createTradeDTO(WxConfig config, CreateTradeCmd cmd, Date date) {
        WxCreateTradeReqDTO dto = new WxCreateTradeReqDTO();
        dto.setAppid(config.getAppId());
        dto.setDescription(cmd.getDescription());
        dto.setMchid(config.getAccountNo());
        // 加上组织id 是否C端回调时选择正确配置
        // 获取租户主机域名
        String host = commonService.getHostByTenantNo(TenantContext.getTenantNo());
        String notifyUrl = "https://" + host + notifyUrlCreateV3 + "/" + cmd.getTradeMode() + "/" + cmd.getAppType()
            + "/" + cmd.getCustomerFlag() + "/";
        if (cmd.getTradeMode().equals(TradeMode.PLATFORM)) {
            notifyUrl += cmd.getOrganizationId();
        } else if (cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
            || cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            notifyUrl += cmd.getPayeeId();
        }
        // 拼接租户编码
        notifyUrl = notifyUrl + "/" + TenantContext.getTenantNo();
        dto.setNotifyUrl(notifyUrl);
        dto.setOutTradeNo(TradeNoUtils.getCreateTradeNo("wx", cmd.getCustomerFlag()));
        WxCreateTradeReqDTO.AmountDTO amountDTO = new WxCreateTradeReqDTO.AmountDTO();
        amountDTO.setCurrency("CNY");
        amountDTO.setTotal((int)(cmd.getAmount().multiply(BigDecimal.valueOf(100L)).doubleValue()));
        dto.setAmount(amountDTO);
        if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_JSAPI
            || PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_MINI_PROGRAM) {
            if (cmd.getPlatformUserId() == null) {
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_PAYER_NULL);
            }
            WxCreateTradeReqDTO.PayerDTO payerDTO = new WxCreateTradeReqDTO.PayerDTO();
            payerDTO.setOpenid(cmd.getPlatformUserId());
            dto.setPayer(payerDTO);
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_H5) {
            WxCreateTradeReqDTO.SceneInfoDTO sceneInfoDTO = new WxCreateTradeReqDTO.SceneInfoDTO();
            WxCreateTradeReqDTO.SceneInfoDTO.H5InfoDTO h5InfoDTO = new WxCreateTradeReqDTO.SceneInfoDTO.H5InfoDTO();
            if (cmd.getSceneInfo() == null) {
                // 手动拼装 +场景信息
                h5InfoDTO.setType("Wap");
                InetAddress inetAddress = InetAddress.getLocalHost();
                log.info("WX V3 H5 IP :{}", inetAddress.getHostAddress());
                sceneInfoDTO.setPayerClientIp(inetAddress.getHostAddress());
            } else {
                sceneInfoDTO.setPayerClientIp(cmd.getSceneInfo().getPayerClientIp());
                h5InfoDTO.setType(cmd.getSceneInfo().getH5Info().getType());
            }
            sceneInfoDTO.setH5Info(h5InfoDTO);
            dto.setSceneInfo(sceneInfoDTO);
        }
        dto.setTimeExpire(DateUtils.getTimeExpire(date, PayChannel.WXPAY_V3.name()));
        return dto;
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
    private CreateTradeRespDTO getResp(String payMethod, WxConfig config, WxCreateTradeReqDTO tradeDTO,
        CreateTradeRespDTO.WXCreateTradeRespDTO respDTO, String redirectUrl) throws Exception {
        CreateTradeRespDTO dto = new CreateTradeRespDTO();
        if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_JSAPI
            || PayChannel.valueOf(payMethod) == PayChannel.WXPAY_MINI_PROGRAM) {
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
        } else if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_H5) {
            if (StringUtils.isBlank(redirectUrl)) {
                throw FinancialException.buildException(ErrorCode.B_REDIRECT_URL_NULL);
            }
            if (StringUtils.isNotBlank(redirectUrl)) {
                respDTO.setH5Url(respDTO.getH5Url() + "&redirect_url=" + URLEncoder.encode(redirectUrl, "UTF-8"));
            }
        }
        respDTO.setOutTradeNo(tradeDTO.getOutTradeNo());
        dto.setWxResp(respDTO);
        return dto;
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

    /**
     * 抽出来本来想做递归的
     * 
     * @param cmd
     * @param payBillByOrderId
     */
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
            if (queryTradeRespDTO.getTradeState().equals(TradeStatus.NOTPAY) && new Date().before(expireTime)) {
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

    @SneakyThrows
    @Override
    public CreateNotifyResp createTradeNotify(CreateNotifyReq cmd) {
        WxConfig config =
            payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(), cmd.getOrganizationId());
        CreateNotifyReq.WXCreateNotifyV3Req wxReq = cmd.getWxReqV3();
        CreateNotifyResp resp = new CreateNotifyResp();
        CreateNotifyResp.WXCreateNotifyResp notifyResp = new CreateNotifyResp.WXCreateNotifyResp();
        try {
            AesUtil aesUtil = new AesUtil(config.getAppKey().getBytes());
            String ciphertext = aesUtil.decryptToString(wxReq.getResource().getAssociatedData().getBytes(),
                wxReq.getResource().getNonce().getBytes(), wxReq.getResource().getCiphertext());
            WxPayNotifyCipherRespDTO wxNotifyCipherDTO = JSON.parseObject(ciphertext, WxPayNotifyCipherRespDTO.class);
            log.info("解密之后的密文：{}", wxNotifyCipherDTO);
            // appid 有可能不准 好像后面也没有用上
            config.setAppId(wxNotifyCipherDTO.getAppid());
            // 存储支付凭证 已支付或取消支付（支付失败）
            PayBillsDO aDo = new PayBillsDO();
            aDo.setPayTime(getWxTime(wxNotifyCipherDTO.getSuccessTime()));
            aDo.setPayStatus(WxPayReturnCode.SUCCESS.equals(wxNotifyCipherDTO.getTradeState())
                ? PayStatus.COMPLETE_PAYMENT : PayStatus.CANCEL_PAYMENT);
            aDo.setOutTradeNo(wxNotifyCipherDTO.getOutTradeNo());
            aDo.setOnlineTradeNo(wxNotifyCipherDTO.getTransactionId());
            aDo.setCurrencyCode(wxNotifyCipherDTO.getAmount().getCurrency());
            // 更新支付凭证
            aDo.setTotalFee(new BigDecimal(wxNotifyCipherDTO.getAmount().getTotal()).divide(new BigDecimal(100), 2,
                RoundingMode.HALF_UP));
            payCmdExc.updatePayBills(cmd.getCustomerFlag(), aDo);
            notifyResp.setCode(WxPayReturnCode.SUCCESS);
            notifyResp.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            notifyResp.setCode(WxPayReturnCode.FAIL);
            notifyResp.setMessage("失败");
        }
        resp.setWxResp(notifyResp);
        return resp;
    }

    public Date getWxTime(String time) throws ParseException {
        String replace = time.replace("T", " ").substring(0, time.indexOf("+"));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replace);
    }

    @SneakyThrows
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
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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

    @SneakyThrows
    @Override
    public boolean closeTrade(CloseTradeCmd cmd) {
        WxConfig config = payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(),
            cmd.getOrganizationId(), cmd.getOutTradeNo());
        WechatPayHttpClientBuilder builder = getBuild(config);
        HttpClient httpClient = builder.build();
        HttpPost httpPost = new HttpPost(gateway + "/pay/transactions/out-trade-no/" + cmd.getOutTradeNo() + "/close");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", config.getAccountNo());
        objectMapper.writeValue(bos, rootNode);
        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            // 正常 接口响应204，无内容
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                log.info("商户订单：{}，取消成功", cmd.getOutTradeNo());
                return true;
            } else {
                log.error("商户订单：{}，取消失败", cmd.getOutTradeNo());
                String bodyAsString = EntityUtils.toString(response.getEntity());
                TradeErrorRespDTO errorRespDTO = objectMapper.readValue(bodyAsString, TradeErrorRespDTO.class);
                log.error(errorRespDTO.toString());
                throw FinancialException.buildException(ErrorCode.B_QUERY_TRADE_ERROR, errorRespDTO.getMessage());
            }
        } catch (IOException e) {
            log.error("商户订单：{}，取消失败", cmd.getOutTradeNo());
            e.printStackTrace();
            log.error(e.getMessage());
            throw FinancialException.buildException(ErrorCode.B_CLOSE_TRADE_ERROR, e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public RefundTradeRespDTO refundTrade(RefundTradeCmd cmd) {
        WxConfig config = payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(),
            cmd.getOrganizationId(), cmd.getOutTradeNo());
        // 分销商自己的账户没有是否原路返回的标志
        if (config.getBackType() == null || config.getBackType() == AccountWxDO.BACK_TYPE_YES) {
            // 判断当前账户是原路返回还是返回余额
            WechatPayHttpClientBuilder builder = getBuild(config);
            HttpClient httpClient = builder.build();
            HttpPost httpPost = new HttpPost(gateway + "/refund/domestic/refunds");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            WxRefundTradeReqDTO dto = refundTradeDTO(cmd);
            ObjectMapper objectMapper = new ObjectMapper();
            log.info("微信V3 退款申请参数 json:{}", JSON.toJSONString(dto));
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(dto), "UTF-8"));
            HttpResponse response;
            String bodyAsString = null;
            try {
                response = httpClient.execute(httpPost);
                bodyAsString = EntityUtils.toString(response.getEntity());
                log.info("微信V3 退款返回参数 json:{}", bodyAsString);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    RefundTradeRespDTO refundTradeRespDTO = new RefundTradeRespDTO();
                    RefundTradeRespDTO.WXRefundTradeRespDTO wxRefundTradeRespDTO =
                        objectMapper.readValue(bodyAsString, RefundTradeRespDTO.WXRefundTradeRespDTO.class);
                    wxRefundTradeRespDTO.setOutTradeNo(dto.getOutTradeNo());
                    refundTradeRespDTO.setWxResp(wxRefundTradeRespDTO);
                    refundTradeRespDTO.setOutRefundNo(dto.getOutRefundNo());
                    refundTradeRespDTO.setOnlineTradeNo(wxRefundTradeRespDTO.getRefundId());
                    if (wxRefundTradeRespDTO.getSuccessTime() != null) {
                        refundTradeRespDTO.setRefundTime(getWxTime(wxRefundTradeRespDTO.getSuccessTime()));
                    } else {
                        refundTradeRespDTO.setRefundTime(new Date());
                    }
                    return refundTradeRespDTO;
                } else {
                    TradeErrorRespDTO errorRespDTO = objectMapper.readValue(bodyAsString, TradeErrorRespDTO.class);
                    log.error(errorRespDTO.toString());
                    throw FinancialException.buildException(ErrorCode.B_REFUND_TRADE_ERROR, errorRespDTO.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw FinancialException.buildException(ErrorCode.B_REFUND_TRADE_ERROR, e.getMessage());
            }
        } else {
            log.info("该分销商：{},或者该机构：{} 不支持原路返还", cmd.getPayeeId(), cmd.getOrganizationId());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT_BACK_TYPE);
        }
    }

    private WxRefundTradeReqDTO refundTradeDTO(RefundTradeCmd cmd) {
        WxRefundTradeReqDTO dto = new WxRefundTradeReqDTO();
        dto.setOutTradeNo(cmd.getOutTradeNo());
        dto.setOutRefundNo(TradeNoUtils.getRefundTradeNo("wx", cmd.getCustomerFlag()));
        WxRefundTradeReqDTO.AmountDTO amountDTO = new WxRefundTradeReqDTO.AmountDTO();
        amountDTO.setTotal((int)(cmd.getTotalAmount() * 100));
        amountDTO.setRefund((int)(cmd.getRefundAmount() * 100));
        amountDTO.setCurrency(cmd.getCurrencyCode());
        dto.setAmount(amountDTO);
        dto.setReason(cmd.getReason());
        // 加上组织id 是否C端回调时选择正确配置(主要是为了验签)
        // 获取租户主机域名
        String host = commonService.getHostByTenantNo(TenantContext.getTenantNo());
        String notifyUrl = "https://" + host + notifyUrlRefundV3 + "/" + cmd.getTradeMode() + "/" + cmd.getAppType()
            + "/" + cmd.getCustomerFlag() + "/";
        if (cmd.getTradeMode().equals(TradeMode.PLATFORM)) {
            notifyUrl += cmd.getOrganizationId();
        } else if (cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
            || cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            notifyUrl += cmd.getPayeeId();
        }
        // 拼接租户编码
        notifyUrl = notifyUrl + "/" + TenantContext.getTenantNo();
        log.info("微信退款 回调通知URL：{}", notifyUrl);
        dto.setNotifyUrl(notifyUrl);
        return dto;
    }

    @Override
    public QueryRefundDTO queryRefund(QueryRefundCmd cmd) {
        // AccountWxDO config = payCmdExc.getWxConfig(cmd.getOrganizationId());
        // WechatPayHttpClientBuilder builder = getBuild(cmd.getOrganizationId(), config);
        // HttpClient httpClient = builder.build();
        // HttpGet httpGet = new HttpGet(gateway + "/refund/domestic/refunds/" + cmd.getOutRefundNo());
        // httpGet.addHeader("Accept", "application/json");
        // ObjectMapper objectMapper = new ObjectMapper();
        // HttpResponse response;
        // String bodyAsString = null;
        // try {
        // response = httpClient.execute(httpGet);
        // bodyAsString = EntityUtils.toString(response.getEntity());
        // if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // QueryRefundDTO queryTradeRespDTO = new QueryRefundDTO();
        // QueryRefundDTO.WXQueryRefundDTO wxQueryTradeRespDTO =
        // objectMapper.readValue(bodyAsString, QueryRefundDTO.WXQueryRefundDTO.class);
        // queryTradeRespDTO.setWxResp(wxQueryTradeRespDTO);
        // return queryTradeRespDTO;
        // } else {
        // TradeErrorRespDTO errorRespDTO = objectMapper.readValue(bodyAsString, TradeErrorRespDTO.class);
        // log.error(errorRespDTO.toString());
        // throw SystemException.buildException(ErrorCode.B_CLOSE_TRADE_ERROR, errorRespDTO.getMessage());
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // log.error(e.getMessage());
        // throw SystemException.buildException(ErrorCode.B_CLOSE_TRADE_ERROR, e.getMessage());
        // }
        return null;
    }

    @Resource
    private RefundBillsCmdExc refundBillsCmdExc;

    @Override
    public RefundNotifyResp refundTradeNotify(RefundNotifyReq cmd) {
        WxConfig config =
            payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(), cmd.getOrganizationId());
        RefundNotifyReq.WXRefundNotifyV3Req wxReq = cmd.getWxReqV3();
        RefundNotifyResp resp = new RefundNotifyResp();
        RefundNotifyResp.WXRefundNotifyResp notifyResp = new RefundNotifyResp.WXRefundNotifyResp();
        try {
            AesUtil aesUtil = new AesUtil(config.getAppKey().getBytes());
            String ciphertext = aesUtil.decryptToString(wxReq.getResource().getAssociatedData().getBytes(),
                wxReq.getResource().getNonce().getBytes(), wxReq.getResource().getCiphertext());
            WxRefundNotifyCipherRespDTO wxNotifyCipherDTO =
                JSON.parseObject(ciphertext, WxRefundNotifyCipherRespDTO.class);
            log.info("解密之后的密文：{}", wxNotifyCipherDTO);
            log.info("退款凭证号：{}", wxNotifyCipherDTO.getOutRefundNo());
            String wxRefundStatus = wxNotifyCipherDTO.getRefundStatus();
            // 更新退款凭证
            short refundStatus;
            if (wxRefundStatus.equals(WXRefundStatus.SUCCESS)) {
                refundStatus = RefundBillsStatus.SUCCESS;
            } else {
                refundStatus = RefundBillsStatus.FAIL;
            }
            boolean b = refundBillsCmdExc.updateRefundBills(wxNotifyCipherDTO.getOutRefundNo(), refundStatus);
            if (b) {
                notifyResp.setCode(WxPayReturnCode.SUCCESS);
                notifyResp.setMessage("成功");
            } else {
                notifyResp.setCode(WxPayReturnCode.FAIL);
                notifyResp.setMessage("失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            notifyResp.setCode(WxPayReturnCode.FAIL);
            notifyResp.setMessage("失败");
        }
        resp.setWxResp(notifyResp);
        return resp;
    }

}
