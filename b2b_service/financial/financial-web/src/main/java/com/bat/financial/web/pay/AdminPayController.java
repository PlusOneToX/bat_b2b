package com.bat.financial.web.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.financial.api.common.CommonService;
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
import com.bat.financial.api.pay.utils.UrlUtils;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信环境<a herf="https://pay.weixin.qq.com/wiki/doc/apiv3/index.shtml"></a><br/>
 * 商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易会话标识后再按Native、JSAPI、APP等不同场景生成交易串调起支付。<br/>
 * Native、JSAPI、APP、（包含小程序） 返回预支付交易回话标志prepay_id（预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时）<br/>
 * 商户Native支付统一下单接口（B2B目前使用），微信后台系统返回链接参数code_url，商户后台系统将code_url值生成二维码图片，用户使用微信客户端扫码后发起支付。<br/>
 * H5 返回支付调转链接h5_url (h5_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付，h5_url的有效期为5分钟。)<br/>
 *
 * 支付宝环境<a href="https://opendocs.alipay.com/open/00a0ut"><a/><br/>
 * 当面付扫码支付(B2B目前使用)：调用统一收单线下交易预创建接口，返回qr_code（二维码码串），开发商可使用二维码生成工具根据该码串值生成对应的二维码。<br/>
 * APP支付<br/>
 * 手机网站支付<br/>
 * 电脑网站支付<br/>
 * 。。。<br/>
 * 
 * 快钱支付环境<a href="https://open.99bill.com/menu!access.do"><a/><br/>
 * 人民币网关支付<br/>
 * 快捷支付<br/>
 * 
 * @return
 */

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 14:11
 */
@Api(tags = "支付后台接口(暂时不直接支付)", value = "AdminPayController")
@Slf4j
@Controller
@RequestMapping("/financial/v1/web/admin/pay")
public class AdminPayController extends BaseController {

    @Resource
    private CommonService commonService;

    /**
     * 预创建订单（阿里）统一下单（微信）人民币网关支付（快钱）<br/>
     * 说法不一样，意思差不多
     * 
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay, value = CommonLogTypeConstantDTO.AdminPayCreate)
    @PostMapping("/createTrade")
    @ApiOperation(value = "创建交易")
    @ResponseBody
    public Response<CreateTradeRespDTO> createTrade(@Valid @RequestBody CreateTradeCmd cmd) {
        CreateTradeRespDTO trade =
            commonService.getMap().get(PayChannel.valueOf(cmd.getPayChannel().toUpperCase())).createTrade(cmd);
        return Response.of(trade);
    }

    /**
     * 微信返回xml数据(取的有问题 死也取不出来，换为从request 读取)
     * 
     * @param tradeMode 交易模式 1平台收款 2分销商收款
     * @param organizationOrPayeeId 如果是平台收款 则为组织id 如果是分销商收款 则为收款人id（分销商id）
     * @param customerFlag C端业务标志 0 B端业务 1 C端业务
     * @param appType 微信收款有这参数 区分公众号与小程序 相同商户号 appId不一样
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayWxpayNotifyV2)
    @PostMapping("/wxpay/v2/pay/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}/{tenantNo}")
    @ApiOperation(value = "微信支付回调V2")
    public void createTradeWxNotifyV2(HttpServletRequest request, HttpServletResponse response,
        @PathVariable Short tradeMode, @PathVariable Integer organizationOrPayeeId, @PathVariable Short customerFlag,
        @PathVariable Short appType) throws IOException {
        String inputLine = null;
        // 接收到的数据
        StringBuilder receiveData = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"))) {
            while ((inputLine = in.readLine()) != null) {
                receiveData.append(inputLine);
            }
        }
        log.info(" Wxpay v2 notify request xml:{}", receiveData);
        CreateNotifyReq req = new CreateNotifyReq();
        req.setCustomerFlag(customerFlag);
        req.setTradeMode(tradeMode);
        req.setAppType(appType);
        if (tradeMode.equals(TradeMode.PLATFORM)) {
            req.setOrganizationId(organizationOrPayeeId);
        } else if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            req.setPayeeId(organizationOrPayeeId);
        }
        req.setWxReqV2(receiveData.toString());
        CreateNotifyResp resp = commonService.getMap().get(PayChannel.WXPAY_V2).createTradeNotify(req);

        response.getWriter().println(resp.getWxResp().getXml());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayWxpayNotifyV3)
    @PostMapping("/wxpay/v3/pay/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}/{tenantNo}")
    @ApiOperation(value = "微信支付回调V3")
    @ResponseBody
    public CreateNotifyResp.WXCreateNotifyResp createTradeWxNotifyV3(
        @RequestBody CreateNotifyReq.WXCreateNotifyV3Req notify, @PathVariable Short tradeMode,
        @PathVariable Integer organizationOrPayeeId, @PathVariable Short customerFlag, @PathVariable Short appType) {
        log.info(" Wxpay v3 pay notify request json:{}", JSON.toJSONString(notify));
        CreateNotifyReq req = new CreateNotifyReq();
        req.setCustomerFlag(customerFlag);
        req.setTradeMode(tradeMode);
        req.setAppType(appType);
        if (tradeMode.equals(TradeMode.PLATFORM)) {
            req.setOrganizationId(organizationOrPayeeId);
        } else if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            req.setPayeeId(organizationOrPayeeId);
        }
        req.setWxReqV3(notify);
        CreateNotifyResp resp = commonService.getMap().get(PayChannel.WXPAY_V3).createTradeNotify(req);
        return resp.getWxResp();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayWxpayNotifyV3)
    @PostMapping("/wxpay/v3/refund/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}/{tenantNo}")
    @ApiOperation(value = "微信退款回调V3")
    @ResponseBody
    public RefundNotifyResp.WXRefundNotifyResp refundTradeWxNotifyV3(
        @RequestBody RefundNotifyReq.WXRefundNotifyV3Req notify, @PathVariable Short tradeMode,
        @PathVariable Integer organizationOrPayeeId, @PathVariable Short customerFlag, @PathVariable Short appType) {
        log.info(" Wxpay v3 refund notify request json:{}", JSON.toJSONString(notify));
        RefundNotifyReq req = new RefundNotifyReq();
        req.setCustomerFlag(customerFlag);
        req.setTradeMode(tradeMode);
        req.setAppType(appType);
        if (tradeMode.equals(TradeMode.PLATFORM)) {
            req.setOrganizationId(organizationOrPayeeId);
        } else if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            req.setPayeeId(organizationOrPayeeId);
        }
        req.setWxReqV3(notify);
        RefundNotifyResp resp = commonService.getMap().get(PayChannel.WXPAY_V3).refundTradeNotify(req);
        return resp.getWxResp();
    }

    /**
     * 因为支付宝返回的参数中 有下划线，无法直接用对象接收
     * 
     * @param request
     * @param organizationOrPayeeId
     * @param customerFlag
     * @return
     * @throws JsonProcessingException
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayAlipayNotify)
    @PostMapping("/alipay/pay/notify/{tradeMode}/{customerFlag}/{organizationOrPayeeId}/{tenantNo}")
    @ApiOperation(value = "支付宝支付回调")
    @ResponseBody
    public void createTradeAlipayNotify(HttpServletRequest request, HttpServletResponse response,
        @PathVariable Short tradeMode, @PathVariable Integer organizationOrPayeeId, @PathVariable Short customerFlag)
        throws IOException {
        // 成功才返回
        log.info(" Alipay notify request json:{}", JSON.toJSONString(request.getParameterMap()));
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info(" Alipay notify request map json:{}", JSON.toJSONString(params));
        log.info(" Alipay notify request url params:{}", UrlUtils.mapToUrl(params));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CreateNotifyReq.AlipayCreateNotifyReq alipayCreateNotifyReq =
            objectMapper.readValue(JSON.toJSONString(params), CreateNotifyReq.AlipayCreateNotifyReq.class);
        CreateNotifyReq req = new CreateNotifyReq();
        alipayCreateNotifyReq.setSignMap(params);
        if (tradeMode.equals(TradeMode.PLATFORM)) {
            req.setOrganizationId(organizationOrPayeeId);
        } else if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            req.setPayeeId(organizationOrPayeeId);
        }
        req.setTradeMode(tradeMode);
        req.setCustomerFlag(customerFlag);
        req.setAlipayReq(alipayCreateNotifyReq);
        CreateNotifyResp resp = commonService.getMap().get(PayChannel.ALIPAY).createTradeNotify(req);
        response.getWriter().println(resp.getAlipayResp().getMessage());
    }

    /**
     * 块钱理论是可以对象接收的，没有处理
     * 
     * @param request
     * @param organizationId
     * @param customerFlag
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayKuaiqianpayNotify)
    @SneakyThrows
    @GetMapping("/kuaiqianpay/pay/notify/{organizationId}/{customerFlag}/{tenantNo}")
    @ResponseBody
    public void createTradeKuaiQianNotify(HttpServletRequest request, HttpServletResponse response,
        @PathVariable Integer organizationId, @PathVariable Short customerFlag) {
        // 成功才返回
        log.info(" KuaiQianpay notify request json:{}", JSON.toJSONString(request.getParameterMap()));
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<>(16);
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info(" KuaiQianpay notify request map json:{}", JSON.toJSONString(params));
        log.info(" KuaiQianpay notify request url params:{}", UrlUtils.mapToUrl(params));
        ObjectMapper objectMapper = new ObjectMapper();
        CreateNotifyReq.KuaiQianCreateNotifyReq kuaiQianCreateNotifyReq =
            objectMapper.readValue(JSON.toJSONString(params), CreateNotifyReq.KuaiQianCreateNotifyReq.class);
        CreateNotifyReq req = new CreateNotifyReq();
        req.setOrganizationId(organizationId);
        req.setCustomerFlag(customerFlag);
        req.setKuaiQianReq(kuaiQianCreateNotifyReq);
        CreateNotifyResp resp = commonService.getMap().get(PayChannel.KUAIQIAN).createTradeNotify(req);
        response.getWriter().println(resp.getKuaiQianResp().getMessage());
    }

    /**
     * 查询交易（阿里）查询订单（微信）人民币网关支付查询（快钱）
     * 
     * @return
     */
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay, value =
    // CommonLogTypeConstantDTO.AdminPayQueryTrade)
    @GetMapping("/queryTrade")
    @ApiOperation(value = "查询交易")
    @ResponseBody
    public Response<QueryTradeRespDTO> queryTrade(@Valid QueryTradeQry qry) {
        QueryTradeRespDTO queryTradeRespDTO =
            commonService.getMap().get(PayChannel.valueOf(qry.getPayChannel().toUpperCase())).queryTrade(qry);
        return Response.of(queryTradeRespDTO);
    }

    /**
     * 
     * 关闭交易（阿里）关闭订单（微信）
     * 
     * 阿里：用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭。
     * 
     * 微信：以下情况需要调用关单接口：<br/>
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；<br/>
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。<br/>
     * 
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayCloseTrade)
    @PostMapping("/closeTrade")
    @ApiOperation(value = "关闭交易")
    @ResponseBody
    public Response closeTrade(@Valid @RequestBody CloseTradeCmd cmd) {
        commonService.getMap().get(PayChannel.valueOf(cmd.getPayChannel().toUpperCase())).closeTrade(cmd);
        return Response.buildSuccess();
    }

    /**
     *
     * 退款（阿里）申请退款（微信）人民币网关退款（快钱）
     *
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay,
        value = CommonLogTypeConstantDTO.AdminPayRefundTrade)
    @PostMapping("/refundTrade")
    @ApiOperation(value = "退款")
    @ResponseBody
    public Response<RefundTradeRespDTO> refundTrade(RefundTradeCmd cmd) {
        RefundTradeRespDTO refundTradeRespDTO =
            commonService.getMap().get(PayChannel.valueOf(cmd.getPayChannel().toUpperCase())).refundTrade(cmd);
        return Response.of(refundTradeRespDTO);
    }

    /**
     *
     * 无（阿里）查询单笔退款（微信）人民币网关退款查询（快钱）
     *
     * @return
     */
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPay, value =
    // CommonLogTypeConstantDTO.AdminPayQueryRefund)
    @GetMapping("queryRefund")
    @ApiOperation(value = "查询退款")
    @ResponseBody
    public Response<QueryRefundDTO> queryRefund(QueryRefundCmd cmd) {
        QueryRefundDTO queryRefundDTO =
            commonService.getMap().get(PayChannel.valueOf(cmd.getPayChannel().toUpperCase())).queryRefund(cmd);
        return Response.of(queryRefundDTO);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPay,
        value = CommonLogTypeConstantDTO.UserPayWxpayPartnerNotify)
    @PostMapping("/wxpay/partner/pay/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}/{tenantNo}")
    @ApiOperation(value = "微信服务商支付回调")
    @ResponseBody
    public CreateNotifyResp.WXCreateNotifyResp createTradeWxPartnerNotify(
        @RequestBody CreateNotifyReq.WXCreateNotifyV3Req notify, @PathVariable Short tradeMode,
        @PathVariable Integer organizationOrPayeeId, @PathVariable Short customerFlag, @PathVariable Short appType) {
        log.info(" Wxpay partner pay notify request json:{}", JSON.toJSONString(notify));
        CreateNotifyReq req = new CreateNotifyReq();
        req.setCustomerFlag(customerFlag);
        req.setTradeMode(tradeMode);
        req.setAppType(appType);
        if (tradeMode.equals(TradeMode.PLATFORM)) {
            req.setOrganizationId(organizationOrPayeeId);
        } else if (tradeMode.equals(TradeMode.DISTRIBUTOR_SELF) || tradeMode.equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            req.setPayeeId(organizationOrPayeeId);
        }
        req.setWxReqV3(notify);
        CreateNotifyResp resp = commonService.getMap().get(PayChannel.WXPAY_V3).createTradeNotify(req);
        return resp.getWxResp();
    }

}
