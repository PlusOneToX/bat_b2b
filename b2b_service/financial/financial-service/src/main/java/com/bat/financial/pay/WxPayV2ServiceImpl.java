package com.bat.financial.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;
import com.bat.financial.pay.config.MyConfig;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.constant.TradeStatus;
import com.bat.financial.pay.constant.WxPayReturnCode;
import com.bat.financial.pay.data.WxConfig;
import com.bat.financial.pay.data.wxv2.WxPayBaseResponse;
import com.bat.financial.pay.data.wxv3.WxRefundTradeReqDTO;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.pay.utils.DateUtils;
import com.bat.financial.pay.utils.TradeNoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.api.pay.dto.data.QueryRefundDTO;
import com.bat.financial.api.pay.dto.data.QueryTradeRespDTO;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.api.pay.notify.CreateNotifyReq;
import com.bat.financial.api.pay.notify.CreateNotifyResp;
import com.bat.financial.api.pay.notify.RefundNotifyReq;
import com.bat.financial.api.pay.notify.RefundNotifyResp;
import com.bat.financial.api.pay.utils.ObjectUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: v2版api xml风格
 * 
 *               旧版 api 暂时只实现支付
 * @date: 2018/5/20 17:26
 */
@Service("WxPayV2ServiceImpl")
@Slf4j
public class WxPayV2ServiceImpl implements PayService {

    @Value("${wechat.notify_url_create_v2}")
    private String notifyUrlCreateV2;

    @Resource
    private PayCmdExc payCmdExc;

    @Resource
    private CommonService commonService;

    @SneakyThrows
    private MyConfig getConfig(WxConfig config) {
        MyConfig myConfig = new MyConfig();
        myConfig.setAppID(config.getAppId());
        myConfig.setMchID(config.getAccountNo());
        myConfig.setKey(config.getAppKey());
        return myConfig;
    }

    @SneakyThrows
    @Override
    public CreateTradeRespDTO createTrade(CreateTradeCmd cmd) {
        Date date = new Date();
        MyConfig config = getConfig(getWxConfig(cmd));
        confirmOldTrade(cmd);
        CreateTradeRespDTO dto = new CreateTradeRespDTO();
        // 坑爹的3.0.9 api 正式环境是HMACSHA256 测试环境是MD5 加密,微信文档说的默认MD5，在官方demo中 并不是
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = createTradeDTO(cmd, date);
        log.info("微信支付V2 请求map：{}", data);
        Map<String, String> resp = wxpay.unifiedOrder(data);
        log.info("微信支付V2 返回map：{}", resp);
        if (resp.get("return_code") != null) {
            if (resp.get("return_code").equals(WxPayReturnCode.SUCCESS)) {
                if (resp.get("result_code").equals(WxPayReturnCode.SUCCESS)) {
                    // 存储支付凭证 待支付
                    CreateTradeRespDTO.WXCreateTradeRespDTO respDTO =
                        getResp(cmd.getPayMethod().toUpperCase(), config.getKey(), resp, cmd.getRedirectUrl());
                    String outTradeNo = data.get("out_trade_no");
                    respDTO.setOutTradeNo(outTradeNo);
                    payCmdExc.createPayBills(cmd, outTradeNo, date);
                    dto.setWxResp(respDTO);
                    return dto;
                } else {
                    log.error("err_code:{},err_code_des:{}", resp.get("err_code"), resp.get("err_code_des"));
                    throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, resp.get("err_code_des"));
                }
            } else {
                log.error("return_msg:{}", resp.get("return_msg"));
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, resp.get("return_msg"));
            }
        } else {
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR);
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
     * 封装统一下单参数
     *
     * @param cmd
     * @param date
     * @return
     */
    @SneakyThrows
    private Map<String, String> createTradeDTO(CreateTradeCmd cmd, Date date) {
        InetAddress inetAddress = InetAddress.getLocalHost();
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", cmd.getDescription());
        data.put("out_trade_no", TradeNoUtils.getCreateTradeNo("wx", cmd.getCustomerFlag()));
        data.put("fee_type", cmd.getCurrencyCode());
        data.put("total_fee", String.valueOf((int)(cmd.getAmount().multiply(BigDecimal.valueOf(100L)).doubleValue())));
        log.info("WX V2 H5 IP :{}", inetAddress.getHostAddress());
        data.put("spbill_create_ip", inetAddress.getHostAddress());
        data.put("sign_type", "MD5");
        // 根据不同场景拼接不同参数 为了回调时能获取正确的配置
        // 根据租户获取租户域名
        String host = commonService.getHostByTenantNo(TenantContext.getTenantNo());
        String notifyUrl = "https://" + host + notifyUrlCreateV2 + "/" + cmd.getTradeMode() + "/" + cmd.getAppType()
            + "/" + cmd.getCustomerFlag() + "/";
        if (cmd.getTradeMode().equals(TradeMode.PLATFORM)) {
            notifyUrl += cmd.getOrganizationId();
        } else if (cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
            || cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            notifyUrl += cmd.getPayeeId();
        }
        // 拼接租户编码
        notifyUrl = notifyUrl + "/" + TenantContext.getTenantNo();
        data.put("notify_url", notifyUrl);
        if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_NATIVE) {
            data.put("trade_type", "NATIVE");
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_JSAPI
            || PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_MINI_PROGRAM) {
            if (cmd.getPlatformUserId() == null) {
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_PAYER_NULL);
            }
            data.put("trade_type", "JSAPI");
            data.put("openid", cmd.getPlatformUserId());
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_APP) {
            data.put("trade_type", "APP");
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.WXPAY_H5) {
            if (cmd.getSceneInfo() == null) {
                data.put("scene_info",
                    "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://www.bat.com\",\"wap_name\": \"bat商城\"}}");
            }
            data.put("trade_type", "MWEB");
        } else {
            // 合单支付暂时不支持（目前没必要）
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_NOT_SUPPORT);
        }
        data.put("time_expire", DateUtils.getTimeExpire(date, PayChannel.WXPAY_V2.name()));
        return data;
    }

    /**
     * 下单返回参数封装过程
     * 
     * @param payMethod
     * @param key
     * @param resp
     * @return
     * @throws Exception
     */
    private CreateTradeRespDTO.WXCreateTradeRespDTO getResp(String payMethod, String key, Map<String, String> resp,
        String redirectUrl) throws Exception {
        CreateTradeRespDTO.WXCreateTradeRespDTO respDTO = new CreateTradeRespDTO.WXCreateTradeRespDTO();
        if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_NATIVE) {
            // native 返回
            respDTO.setCodeUrl(resp.get("code_url"));
        } else if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_JSAPI
            || PayChannel.valueOf(payMethod) == PayChannel.WXPAY_MINI_PROGRAM) {
            respDTO.setAppId(resp.get("appid"));
            respDTO.setNonceStr(resp.get("nonce_str"));
            respDTO.setPrepayId("prepay_id=" + resp.get("prepay_id"));
            respDTO.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
            respDTO.setSignType("MD5");
            Map<String, String> mapData = new HashMap<>(16);
            mapData.put("appId", respDTO.getAppId());
            mapData.put("timeStamp", respDTO.getTimeStamp());
            mapData.put("nonceStr", respDTO.getNonceStr());
            mapData.put("package", respDTO.getPrepayId());
            mapData.put("signType", respDTO.getSignType());
            respDTO.setPaySign(WXPayUtil.generateSignature(mapData, key));
        } else if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_APP) {
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_NOT_SUPPORT);
        } else if (PayChannel.valueOf(payMethod) == PayChannel.WXPAY_H5) {
            if (StringUtils.isBlank(redirectUrl)) {
                throw FinancialException.buildException(ErrorCode.B_REDIRECT_URL_NULL);
            }
            if (StringUtils.isNotBlank(redirectUrl)) {
                respDTO.setH5Url(resp.get("mweb_url"));
                respDTO.setH5Url(respDTO.getH5Url() + "&redirect_url=" + URLEncoder.encode(redirectUrl, "UTF-8"));
            }
        } else {
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_NOT_SUPPORT);
        }
        return respDTO;
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
        CreateNotifyResp resp = new CreateNotifyResp();
        CreateNotifyResp.WXCreateNotifyResp notifyResp = new CreateNotifyResp.WXCreateNotifyResp();
        try {
            Map<String, String> reqMap = WXPayUtil.xmlToMap(cmd.getWxReqV2());
            log.info("解析xml 后的结果map：{}", reqMap);
            String outTradeNo = reqMap.get("out_trade_no");
            WxConfig wxConfig = payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(),
                cmd.getOrganizationId(), outTradeNo);
            log.info("微信支付V2 wxConfig：{}", wxConfig);
            WXPay wxpay = new WXPay(getConfig(wxConfig));
            boolean isAccord = wxpay.isPayResultNotifySignatureValid(reqMap);
            String transactionId = reqMap.get("transaction_id");
            String totalFee = reqMap.get("total_fee");
            if (isAccord) {
                PayBillsDO aDo = new PayBillsDO();
                aDo.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(reqMap.get("time_end")));
                // 没有返回 支付状态
                aDo.setPayStatus(PayStatus.COMPLETE_PAYMENT);
                aDo.setOutTradeNo(outTradeNo);
                aDo.setOnlineTradeNo(transactionId);
                // 没有返回货币
                aDo.setCurrencyCode("CNY");
                aDo.setTotalFee(new BigDecimal(totalFee).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
                payCmdExc.updatePayBills(cmd.getCustomerFlag(), aDo);
                notifyResp.setXml(notifyResponse(WxPayReturnCode.SUCCESS));
                resp.setWxResp(notifyResp);
                return resp;
            }
            log.error("微信V2回调通知验签失败：{}", isAccord);
            log.error("微信V2回调通知支付失败 out_trade_no:{},transaction_id:{},total_fee:{}", outTradeNo, transactionId,
                totalFee);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信V2回调通知支付失败：error:{}", e.getMessage());
        }
        notifyResp.setXml(notifyResponse(WxPayReturnCode.FAIL));
        resp.setWxResp(notifyResp);
        return resp;
    }

    @SneakyThrows
    @Override
    public QueryTradeRespDTO queryTrade(QueryTradeQry qry) {
        WxConfig wxConfig = payCmdExc.getWxConfig(qry.getTradeMode(), qry.getAppType(), qry.getPayeeId(),
            qry.getOrganizationId(), qry.getOutTradeNo());
        WXPay wxpay = new WXPay(getConfig(wxConfig));
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", qry.getOutTradeNo());
        QueryTradeRespDTO dto = new QueryTradeRespDTO();
        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            if (resp != null) {
                if (WxPayReturnCode.SUCCESS.equals(resp.get("return_code"))
                    && WxPayReturnCode.SUCCESS.equals(resp.get("result_code"))) {
                    dto.setTradeState(resp.get("trade_state"));
                    return dto;
                } else {
                    // 当return_code为FAIL时返回信息为错误原因 ，例如 签名失败 参数格式校验错误
                    log.error("return_msg:{}", resp.get("return_msg"));
                    // 当result_code为FAIL时返回错误代码，详细参见下文错误列表
                    log.error("err_code:{},err_code_des:{}", resp.get("err_code"), resp.get("err_code_des"));
                    // 对当前查询订单状态的描述和下一步操作的指引
                    log.error("trade_state_desc:{}", resp.get("trade_state_desc"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dto.setTradeState(TradeStatus.ERROR);
        return dto;
    }

    @SneakyThrows
    @Override
    public boolean closeTrade(CloseTradeCmd cmd) {
        WxConfig wxConfig = payCmdExc.getWxConfig(cmd.getTradeMode(), cmd.getAppType(), cmd.getPayeeId(),
            cmd.getOrganizationId(), cmd.getOutTradeNo());
        WXPay wxpay = new WXPay(getConfig(wxConfig));
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", cmd.getOutTradeNo());
        try {
            Map<String, String> resp = wxpay.closeOrder(data);
            if (resp != null) {
                if (WxPayReturnCode.SUCCESS.equals(resp.get("return_code"))
                    && WxPayReturnCode.SUCCESS.equals(resp.get("result_code"))) {
                    return true;
                } else {
                    // 当return_code为FAIL时返回信息为错误原因 ，例如 签名失败 参数格式校验错误
                    log.error("return_msg:{}", resp.get("return_msg"));
                    // 对业务结果的补充说明
                    log.error("result_msg:{}", resp.get("result_msg"));
                    // 当result_code为FAIL时返回错误代码，详细参见下文错误列表
                    log.error("err_code:{},err_code_des:{}", resp.get("err_code"), resp.get("err_code_des"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SneakyThrows
    @Override
    public RefundTradeRespDTO refundTrade(RefundTradeCmd cmd) {
        // V2 退款需要证书，但是99%分销商 没有收集证书。所以不支持
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    private WxRefundTradeReqDTO refundTradeDTO(AccountWxDO config, RefundTradeCmd cmd) {
        return null;
    }

    @Override
    public QueryRefundDTO queryRefund(QueryRefundCmd cmd) {
        // 虽然查询不需要证书，但是都不能退，查就没有必要了
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public RefundNotifyResp refundTradeNotify(RefundNotifyReq req) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    public String notifyResponse(String statusCode) {
        Map<String, String> response = ObjectUtils.objToStrMap(WxPayBaseResponse.toResponse(statusCode));
        try {
            if (response != null) {
                return WXPayUtil.mapToXml(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String notifyResponse(String statusCode, String msg) {
        Map<String, String> response = ObjectUtils.objToStrMap(WxPayBaseResponse.toResponse(statusCode, msg));
        try {
            if (response != null) {
                return WXPayUtil.mapToXml(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
