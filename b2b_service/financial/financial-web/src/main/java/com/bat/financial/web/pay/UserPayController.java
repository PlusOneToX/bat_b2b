package com.bat.financial.web.pay;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.api.pay.dto.data.QueryRefundDTO;
import com.bat.financial.api.pay.dto.data.QueryTradeRespDTO;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/28 11:29
 */
@Api(tags = "支付前台接口", value = "UserPayOrderController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/user/pay")
public class UserPayController extends BaseController {

    @Resource
    private CommonService commonService;

    /**
     * 预创建订单（阿里）统一下单（微信）人民币网关支付（快钱）<br/>
     * 说法不一样，意思差不多
     *
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPay,
        value = CommonLogTypeConstantDTO.UserPayCreateTrade)
    @PostMapping("/createTrade")
    @ApiOperation(value = "创建交易")
    public Response<CreateTradeRespDTO> createTrade(@Valid @RequestBody CreateTradeCmd cmd) {
        log.info("createTrade:{}", JSON.toJSONString(cmd));
        commonService.preCreateTrade(cmd);
        log.info("CreateTradeCmd:{}", JSON.toJSONString(cmd));
        CreateTradeRespDTO trade =
            commonService.getMap().get(PayChannel.valueOf(cmd.getPayChannel().toUpperCase())).createTrade(cmd);
        return Response.of(trade);
    }

    /**
     * 查询交易（阿里）查询订单（微信）人民币网关支付查询（快钱）
     *
     * @return
     */
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPay, value =
    // CommonLogTypeConstantDTO.UserPayQueryTrade)
    @GetMapping("/queryTrade")
    @ApiOperation(value = "查询交易")
    public Response<QueryTradeRespDTO> queryTrade(@Valid QueryTradeQry qry) {
        commonService.preQueryTrade(qry);
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
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPay,
        value = CommonLogTypeConstantDTO.UserPayCloseTrade)
    @PostMapping("/closeTrade")
    @ApiOperation(value = "关闭交易")
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
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPay,
        value = CommonLogTypeConstantDTO.UserPayRefundTrade)
    @PostMapping("/refundTrade")
    @ApiOperation(value = "退款")
    public Response<RefundTradeRespDTO> refundTrade(RefundTradeCmd cmd) {
        commonService.preRefundTrade(cmd);
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
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPay, value =
    // CommonLogTypeConstantDTO.UserPayQueryRefund)
    @GetMapping("queryRefund")
    @ApiOperation(value = "查询退款")
    public Response<QueryRefundDTO> queryRefund(QueryRefundCmd cmd) {
        QueryRefundDTO queryRefundDTO =
            commonService.getMap().get(PayChannel.valueOf(cmd.getPayChannel().toUpperCase())).queryRefund(cmd);
        return Response.of(queryRefundDTO);
    }

}
