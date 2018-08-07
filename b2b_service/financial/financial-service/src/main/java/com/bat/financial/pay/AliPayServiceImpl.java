package com.bat.financial.pay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.pay.dataobject.PayTradeQrRelationDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;
import com.bat.financial.pay.config.AlipayConfig;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.constant.TradeStatus;
import com.bat.financial.pay.data.AliConfig;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.pay.executor.PayTradeQrRelationCmdExe;
import com.bat.financial.pay.executor.PayTradeQrRelationQryExe;
import com.bat.financial.pay.utils.TradeNoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.*;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 17:26
 */
@Service("AliPayServiceImpl")
@Slf4j
public class AliPayServiceImpl implements PayService {

    @Value("${alipay.gateway}")
    private String gateway;
    @Value("${alipay.notify_url_create}")
    private String notifyUrlCreate;
    @Resource
    private PayCmdExc payCmdExc;

    @Resource
    private PayTradeQrRelationQryExe relationQryExe;

    @Resource
    private PayTradeQrRelationCmdExe relationCmdExe;

    @Resource
    private CommonService commonService;

    @Resource
    private AlipayConfig alipayConfig;

    private Config getOptions(AliConfig config1) {
        Config config = new Config();
        String url = gateway;
        // 公司测试环境沙箱账号
        if (alipayConfig.getTestAppIds().contains(config1.getAppId())) {
            url = "https://openapi.alipaydev.com/gateway.do";
        }
        config.protocol = url.substring(0, url.indexOf("://"));
        config.gatewayHost = url.substring(url.indexOf("://") + 3, url.indexOf("/gateway.do"));
        config.signType = "RSA2";
        config.appId = config1.getAppId();
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = config1.getAppPrivateSecret();
        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2018051064521003.crt -->";
        // config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        // config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        // 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = config1.getAppPublicSecret();
        // 可设置异步通知接收服务地址（可选）
        // config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        // 可设置AES密钥，调用AES加解密相关接口时需要（可选）
        // config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";
        return config;
    }

    @SneakyThrows
    @Override
    public CreateTradeRespDTO createTrade(CreateTradeCmd cmd) {
        Date date = new Date();
        CreateTradeRespDTO dto = confirmOldTrade(cmd);
        if (dto == null) {
            dto = new CreateTradeRespDTO();
        } else {
            return dto;
        }
        CreateTradeRespDTO.AlipayCreateTradeRespDTO respDTO = new CreateTradeRespDTO.AlipayCreateTradeRespDTO();
        AliConfig alipayConfig =
            payCmdExc.getAlipayConfig(cmd.getTradeMode(), cmd.getPayeeId(), cmd.getOrganizationId());
        Factory.setOptions(getOptions(alipayConfig));
        String tradeNo = TradeNoUtils.getCreateTradeNo("al", cmd.getCustomerFlag());
        // 获取租户主机域名
        String host = commonService.getHostByTenantNo(TenantContext.getTenantNo());
        String notifyUrl =
            "https://" + host + notifyUrlCreate + "/" + cmd.getTradeMode() + "/" + cmd.getCustomerFlag() + "/";
        if (cmd.getTradeMode().equals(TradeMode.PLATFORM)) {
            notifyUrl += cmd.getOrganizationId();
        } else if (cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
            || cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            notifyUrl += cmd.getPayeeId();
        }
        // 拼接租户编码
        notifyUrl = notifyUrl + "/" + TenantContext.getTenantNo();
        String timeoutExpress = "1m";
        log.info("支付宝支付 请求参数：notifyUrl：{}，description：{}，tradeNo：{}，amount：{}，timeoutExpress：{}", notifyUrl,
            cmd.getDescription(), tradeNo, cmd.getAmount(), timeoutExpress);
        if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.ALIPAY_COMMON) {
            // 通用支付（小程序）
            AlipayTradeCreateResponse response =
                Factory.Payment.Common().optional("timeout_express", timeoutExpress).asyncNotify(notifyUrl)
                    .create(cmd.getDescription(), tradeNo, cmd.getAmount() + "", cmd.getPlatformUserId());
            if (ResponseChecker.success(response)) {
                respDTO.setTradeNo(response.getTradeNo());
                respDTO.setOutTradeNo(response.getOutTradeNo());
            } else {
                log.error(response.msg + "，" + response.subMsg);
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR,
                    response.msg + "，" + response.subMsg);
            }
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.ALIPAY_FACE_TO_FACE) {
            // 面对面
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().asyncNotify(notifyUrl)
                .preCreate(cmd.getDescription(), tradeNo, cmd.getAmount() + "");
            if (ResponseChecker.success(response)) {
                respDTO.setCodeUrl(response.getQrCode());
                respDTO.setOutTradeNo(tradeNo);
                // 面对面支付 二维码保存
                relationCmdExe.createRelation(cmd, respDTO);
            } else {
                log.error(response.msg + "，" + response.subMsg);
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR,
                    response.msg + "，" + response.subMsg);
            }
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.ALIPAY_WAP) {
            // 手机网页支付
            AlipayTradeWapPayResponse response =
                Factory.Payment.Wap().optional("timeout_express", timeoutExpress).asyncNotify(notifyUrl)
                    .pay(cmd.getDescription(), tradeNo, cmd.getAmount() + "", getQuitUrl(cmd), getRedirectUrl(cmd));
            if (ResponseChecker.success(response)) {
                respDTO.setFrom(response.getBody());
            }
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.ALIPAY_PAGE) {
            AlipayTradePagePayResponse response = Factory.Payment.Page().optional("timeout_express", timeoutExpress)
                .asyncNotify(notifyUrl).pay(cmd.getDescription(), tradeNo, cmd.getAmount() + "", getRedirectUrl(cmd));
            if (ResponseChecker.success(response)) {
                respDTO.setFrom(response.getBody());
            }
        } else if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.ALIPAY_APP) {
            AlipayTradeAppPayResponse response = Factory.Payment.App().optional("timeout_express", timeoutExpress)
                .asyncNotify(notifyUrl).pay(cmd.getDescription(), tradeNo, cmd.getAmount() + "");
            if (ResponseChecker.success(response)) {
                respDTO.setAlipaySdk(response.getBody());
            }
        } else {
            // 暂不支持花呗支付
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_NOT_SUPPORT);
        }
        respDTO.setOutTradeNo(tradeNo);
        log.info("支付宝支付 返回结果：{}", respDTO);
        dto.setAlipayResp(respDTO);
        // 存储支付凭证 待支付
        payCmdExc.createPayBills(cmd, tradeNo, date);
        return dto;
    }

    @SneakyThrows
    private String getQuitUrl(CreateTradeCmd cmd) {
        if (StringUtils.isBlank(cmd.getQuitUrl())) {
            String redirectUrl = cmd.getRedirectUrl();
            cmd.setQuitUrl(redirectUrl);
            return redirectUrl;
        } else {
//            return URLEncoder.encode(cmd.getQuitUrl(), "UTF-8");
            return cmd.getQuitUrl();
        }
    }

    @SneakyThrows
    private String getRedirectUrl(CreateTradeCmd cmd) {
        if (StringUtils.isBlank(cmd.getRedirectUrl())) {
            throw FinancialException.buildException(ErrorCode.B_REDIRECT_URL_NULL);
        } else {
//            return URLEncoder.encode(cmd.getRedirectUrl(), "UTF-8");
            return cmd.getRedirectUrl();
        }
    }

    /**
     * 确认旧订单 并处理
     *
     * @return
     */
    private CreateTradeRespDTO confirmOldTrade(CreateTradeCmd cmd) {
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
                    return confirmWaitTradeTrade(cmd, payBillByOrderId);
                }
            }
        }
        return null;
    }

    /**
     * 抽出来本来想做递归的
     * 
     * 这一块要求特别多
     * 
     * @param cmd
     * @param payBillByOrderId
     */
    private CreateTradeRespDTO confirmWaitTradeTrade(CreateTradeCmd cmd, PayBillsDO payBillByOrderId) {
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
            // 针对于 面对面支付（面对面支付没有交易过期时间）
            if (payBillByOrderId.getPayMethod().equalsIgnoreCase(PayChannel.ALIPAY_FACE_TO_FACE.name())) {
                // 如果在有效期之类 直接提示
                // 在有效期之外 如果用户已经唤起收银台 待支付，提示待支付
                if (queryTradeRespDTO.getTradeState().equals(TradeStatus.NOTPAY)) {
                    // 待支付状态 并且在自定义的失效时间之内（2分钟 正常情况下已经失效）
                    log.info("旧的交易待支付");
                    throw FinancialException.buildException("该订单已经存在待支付的付款，请完成待支付的订单");
                } else {
                    // 如果没有唤起过收银台 该订单在支付宝不存在 （交易不存在）
                    // 支付宝旧码长期有效 如果有旧码 使用旧码
                    PayTradeQrRelationDO byOutTradeNo =
                        relationQryExe.getByOutTradeNo(payBillByOrderId.getOutTradeNo());
                    if (byOutTradeNo != null) {
                        log.info("旧的交易未支付,又调起新的交易，使用旧码");
                        CreateTradeRespDTO createTradeRespDTO = new CreateTradeRespDTO();
                        CreateTradeRespDTO.AlipayCreateTradeRespDTO alipayCreateTradeRespDTO =
                            new CreateTradeRespDTO.AlipayCreateTradeRespDTO();
                        alipayCreateTradeRespDTO.setTradeNo(byOutTradeNo.getOutTradeNo());
                        alipayCreateTradeRespDTO.setCodeUrl(byOutTradeNo.getQrCode());
                        createTradeRespDTO.setAlipayResp(alipayCreateTradeRespDTO);
                        return createTradeRespDTO;
                    } else {
                        // 如果没有旧码 开启新的交易
                        closeOldTrade(cmd, payBillByOrderId);
                    }
                }
            } else {
                // 除了面对面交易之外的其他交易
                Date now = new Date();
                if (queryTradeRespDTO.getTradeState().equals(TradeStatus.NOTPAY) && new Date().before(expireTime)) {
                    // 待支付状态 并且在自定义的失效时间之内（2分钟 正常情况下已经失效）
                    log.info("旧的交易待支付");
                    long timeSlot = (expireTime.getTime() - now.getTime()) / 1000;
                    throw FinancialException.buildException(ErrorCode.B_TRADE_WAIT_TRADE,
                        MessageFormat.format(MessageUtils.get(ErrorCode.B_TRADE_WAIT_TRADE), timeSlot));
                } else {
                    closeOldTrade(cmd, payBillByOrderId);
                }
            }
        }
        return null;
    }

    private void closeOldTrade(CreateTradeCmd cmd, PayBillsDO payBillByOrderId) {
        log.info("旧的交易未支付,又调起新的交易，生产新码");
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

    @SneakyThrows
    @Override
    public CreateNotifyResp createTradeNotify(CreateNotifyReq cmd) {
        AliConfig alipayConfig =
            payCmdExc.getAlipayConfig(cmd.getTradeMode(), cmd.getPayeeId(), cmd.getOrganizationId());
        Factory.setOptions(getOptions(alipayConfig));
        CreateNotifyReq.AlipayCreateNotifyReq alipayReq = cmd.getAlipayReq();
        Boolean aBoolean = null;
        CreateNotifyResp resp = new CreateNotifyResp();
        CreateNotifyResp.AlipayCreateNotifyResp notifyResp = new CreateNotifyResp.AlipayCreateNotifyResp();
        try {
            aBoolean = Factory.Payment.Common().verifyNotify(alipayReq.getSignMap());
        } catch (Exception e) {
            log.info("支付宝回调通知验签失败");
            notifyResp.setMessage("error");
        }
        if (aBoolean != null && aBoolean) {
            log.info("支付宝回调通知验签成功");
            log.info("alipayReq:{}", JSON.toJSONString(alipayReq));
            // 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
            // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款） TRADE_FINISHED（交易结束，不可退款）都视为取消支付
            Short payStatus = TradeStatus.AliPayTradeState.WAIT_BUYER_PAY.equals(alipayReq.getTradeStatus())
                ? PayStatus.WAIT_PAYMENT : TradeStatus.AliPayTradeState.TRADE_SUCCESS.equals(alipayReq.getTradeStatus())
                    ? PayStatus.COMPLETE_PAYMENT : PayStatus.CANCEL_PAYMENT;
            if (payStatus.equals(PayStatus.COMPLETE_PAYMENT)) {
                // 存储支付凭证 已支付或取消支付（支付失败）
                PayBillsDO aDo = new PayBillsDO();
                String time = null;
                if (alipayReq.getGmtPayment() != null) {
                    time = alipayReq.getGmtPayment();
                }
                if (alipayReq.getGmtCreate() != null) {
                    time = alipayReq.getGmtCreate();
                }
                if (time != null) {
                    aDo.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
                } else {
                    aDo.setPayTime(new Date());
                }
                aDo.setPayStatus(payStatus);
                aDo.setOutTradeNo(alipayReq.getOutTradeNo());
                aDo.setOnlineTradeNo(alipayReq.getTradeNo());
                // 支付宝默认没有货币返回 需要加查询参数 这里预先写为人民币
                aDo.setCurrencyCode("CNY");
                aDo.setTotalFee(new BigDecimal(alipayReq.getTotalAmount()));
                // 无资金商户优惠券 目前只有小程序 只对接C端的 B端分销的不管
                if (cmd.getCustomerFlag() == CustomerFlag.IS_CUSTOMER) {
                    if (StringUtils.isNotBlank(alipayReq.getVoucherDetailList())
                        && (!alipayReq.getVoucherDetailList().contains("ALIPAY_CASH_VOUCHER"))) {
                        aDo.setReceiptAmount(new BigDecimal(alipayReq.getReceiptAmount()));
                        aDo.setRemark(alipayReq.getVoucherDetailList());
                    }
                }
                payCmdExc.updatePayBills(cmd.getCustomerFlag(), aDo);
            } else {
                log.info("AliPayTradeState:{}", alipayReq.getTradeStatus());
                log.info("payStatus:{}", payStatus);
            }
            notifyResp.setMessage("success");
        } else {
            log.info("支付宝回调通知验签失败");
            notifyResp.setMessage("error");
        }
        resp.setAlipayResp(notifyResp);
        return resp;
    }

    @SneakyThrows
    @Override
    public QueryTradeRespDTO queryTrade(QueryTradeQry cmd) {
        AliConfig alipayConfig =
            payCmdExc.getAlipayConfig(cmd.getTradeMode(), cmd.getPayeeId(), cmd.getOrganizationId());
        Factory.setOptions(getOptions(alipayConfig));
        AlipayTradeQueryResponse response = Factory.Payment.Common().query(cmd.getOutTradeNo());
        QueryTradeRespDTO dto = new QueryTradeRespDTO();
        if (ResponseChecker.success(response)) {
            ObjectMapper objectMapper = new ObjectMapper();
            QueryTradeRespDTO.AlipayQueryTradeRespDTO respDTO =
                objectMapper.readValue(response.getHttpBody(), QueryTradeRespDTO.AlipayQueryTradeRespDTO.class);
            log.info("alipay queryTrade json :{}", JSON.toJSONString(respDTO));
            switch (respDTO.getAlipayTradeQueryResponse().getTradeStatus()) {
                case TradeStatus.AliPayTradeState.TRADE_SUCCESS:
                    dto.setTradeState(TradeStatus.SUCCESS);
                    break;
                case TradeStatus.AliPayTradeState.WAIT_BUYER_PAY:
                    dto.setTradeState(TradeStatus.NOTPAY);
                    break;
                case TradeStatus.AliPayTradeState.TRADE_CLOSED:
                case TradeStatus.AliPayTradeState.TRADE_FINISHED:
                    dto.setTradeState(TradeStatus.CLOSED);
                    break;
                default:
                    dto.setTradeState(TradeStatus.ERROR);
                    break;
            }
            return dto;
        } else {
            log.error(response.msg + "，" + response.subMsg);
        }
        dto.setTradeState(TradeStatus.ERROR);
        return dto;
    }

    @SneakyThrows
    @Override
    public boolean closeTrade(CloseTradeCmd cmd) {
        AliConfig alipayConfig =
            payCmdExc.getAlipayConfig(cmd.getTradeMode(), cmd.getPayeeId(), cmd.getOrganizationId());
        Factory.setOptions(getOptions(alipayConfig));
        AlipayTradeCloseResponse response = Factory.Payment.Common().close(cmd.getOutTradeNo());
        if (ResponseChecker.success(response)) {
            return true;
        } else {
            log.error(response.msg + "，" + response.subMsg);
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR,
                response.msg + "，" + response.subMsg);
        }
    }

    /**
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，支付宝将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款
     * 支付宝退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额
     * <p>
     * <p>
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.refund
     *
     * @param cmd
     * @return
     */
    @SneakyThrows
    @Override
    public RefundTradeRespDTO refundTrade(RefundTradeCmd cmd) {
        AliConfig alipayConfig =
            payCmdExc.getAlipayConfig(cmd.getTradeMode(), cmd.getPayeeId(), cmd.getOrganizationId());
        if (alipayConfig.getBackType() == null || alipayConfig.getBackType() == AccountWxDO.BACK_TYPE_YES) {
            Factory.setOptions(getOptions(alipayConfig));
            String al = TradeNoUtils.getRefundTradeNo("al", cmd.getCustomerFlag());
            AlipayTradeRefundResponse response = Factory.Payment.Common().optional("refund_reason", cmd.getReason())
                .optional("refund_currency", cmd.getCurrencyCode()).optional("out_request_no", al)
                .refund(cmd.getOutTradeNo(), cmd.getRefundAmount() + "");
            log.info("退款返回信息：" + response.getHttpBody());
            RefundTradeRespDTO queryTradeRespDTO = new RefundTradeRespDTO();
            queryTradeRespDTO.setOutRefundNo(al);
            queryTradeRespDTO.setRefundTime(new Date());
            if (ResponseChecker.success(response)) {
                log.info("调用成功");
                ObjectMapper objectMapper = new ObjectMapper();
                RefundTradeRespDTO.AlipayRefundTradeRespDTO respDTO =
                    objectMapper.readValue(response.getHttpBody(), RefundTradeRespDTO.AlipayRefundTradeRespDTO.class);
                respDTO.setOutRequestNo(al);
                queryTradeRespDTO.setAlipayResp(respDTO);
                if ("Y".equals(respDTO.getAlipayTradeRefundResponse().getFundChange())) {
                    log.info("退款成功");
                    queryTradeRespDTO.setRefundStatus(true);
                }
                queryTradeRespDTO.setOnlineTradeNo(respDTO.getAlipayTradeRefundResponse().getTradeNo());
            } else {
                // 有一种情况 交易已关闭是 已经退款的意思 40004，ACQ.TRADE_HAS_CLOSE Business Failed，交易已经关闭
                log.info("调用失败");
                log.error(response.code + "，" + response.subCode);
                log.error(response.msg + "，" + response.subMsg);
                if ("40004".equals(response.code) && "ACQ.TRADE_HAS_CLOSE".equals(response.subCode)) {
                    queryTradeRespDTO.setRefundStatus(true);
                }
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR,
                    response.msg + "，" + response.subMsg);
            }
            return queryTradeRespDTO;
        } else {
            log.info("该分销商：{},或者该机构：{} 不支持原路返还", cmd.getPayeeId(), cmd.getOrganizationId());
            throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT_BACK_TYPE);
        }
    }

    /**
     * 商户可使用该接口查询自已通过alipay.trade.refund或alipay.trade.refund.apply提交的退款请求是否执行成功。
     * 该接口的返回码10000，仅代表本次查询操作成功，不代表退款成功。异步退款场景返回了查询数据，且refund_status为空或为REFUND_SUCCESS，则代表退款成功，若没有查询到则代表未退款成功，可以调用退款接口进行重试。重试时请务必保证退款请求号一致。同步退款场景需响应返回
     * trade_no 等具体退款信息才表示退款成功。
     * <p>
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.fastpay.refund.query
     *
     * @param cmd
     * @return
     */
    @SneakyThrows
    @Override
    public QueryRefundDTO queryRefund(QueryRefundCmd cmd) {
        AliConfig alipayConfig =
            payCmdExc.getAlipayConfig(cmd.getTradeMode(), cmd.getPayeeId(), cmd.getOrganizationId());
        Factory.setOptions(getOptions(alipayConfig));
        AlipayTradeFastpayRefundQueryResponse response =
            Factory.Payment.Common().queryRefund(cmd.getOutTradeNo(), cmd.getOutRefundNo());
        if (ResponseChecker.success(response)) {
            QueryRefundDTO queryRefundDTO = new QueryRefundDTO();
            ObjectMapper objectMapper = new ObjectMapper();
            QueryRefundDTO.AlipayQueryRefundDTO respDTO =
                objectMapper.readValue(response.getHttpBody(), QueryRefundDTO.AlipayQueryRefundDTO.class);
            queryRefundDTO.setAlipayResp(respDTO);
            return queryRefundDTO;
        } else {
            log.error(response.msg + "，" + response.subMsg);
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR,
                response.msg + "，" + response.subMsg);
        }
    }

    @Override
    public RefundNotifyResp refundTradeNotify(RefundNotifyReq req) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

}
