package com.bat.thirdparty.log.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.log.api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.log.api.LogServiceI;
import com.bat.thirdparty.log.api.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/log")
@Api(tags = "日志接口")
public class LogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Resource
    private LogServiceI logServiceI;

    @ApiOperation(value = "操作日志列表")
    @GetMapping("/list")
    public Response<PageInfo> listCommonLog(@Valid LogQry qry) {
        PageInfo pageInfo = logServiceI.findPageCommonLogByParams(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "分销商日志列表")
    @GetMapping("/distributor/list")
    public Response<PageInfo> listDistributorLog(@Valid DistributorLogQry qry) {
        PageInfo pageInfo = logServiceI.findPageDistributorLogByParams(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "订单日志列表")
    @GetMapping("/order/list")
    public Response<PageInfo> listOrderLog(@Valid OrderLogQry qry) {
        PageInfo pageInfo = logServiceI.findPageOrderLogByParams(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "收款单日志列表")
    @GetMapping("/voucher/list")
    public Response<PageInfo> listVoucherLog(@Valid VoucherLogQry qry) {
        PageInfo pageInfo = logServiceI.findPageVoucherLogByParams(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "退款单日志列表")
    @GetMapping("/refund/list")
    public Response<PageInfo> listRefundLog(@Valid RefundLogQry qry) {
        PageInfo pageInfo = logServiceI.findPageRefundLogByParams(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "发货单日志列表")
    @GetMapping("/orderDeliverBill/list")
    public Response<PageInfo> listOrderDeliverBillLog(@Valid OrderDeliverBillLogQry qry) {
        PageInfo pageInfo = logServiceI.findPageOrderDeliverBillLogByParams(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "分销客户提现申请日志列表")
    @GetMapping("/withdrawApply/list")
    public Response<PageInfo> listWithdrawApplyLog(@Valid WithdrawApplyLogQry qry) {
        PageInfo pageInfo = logServiceI.findPageWithdrawApplyLogByParams(qry);
        return Response.of(pageInfo);
    }
}
