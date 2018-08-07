package com.bat.order.web.order;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.UserCustomerOrderServiceI;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderGoodsThirdCodeDTO;
import com.bat.order.api.order.dto.customer.OrderInfoCustomerCmd;
import com.bat.order.api.order.dto.customer.OrderInfoExchangeCmd;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListDTO;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListQry;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderThirdCodeQry;
import com.bat.order.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "C端客户订单前台接口")
@RequestMapping(value = "/order/v1/web/user/customer/order")
public class UserCustomerOrderController extends BaseController {

    @Resource
    private UserCustomerOrderServiceI service;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserCustomerOrder,
        value = CommonLogTypeConstantDTO.UserCustomerOrderCreateCustomer)
    @ApiOperation(value = "C端客户下单接口(非兑换卡下单)")
    @PostMapping()
    public Response<BaseIds> createCustomerOrder(@Valid @RequestBody OrderInfoCustomerCmd cmd) {
        System.out.print("开始下单啦");
        BaseIds ids =
            service.createOrder(cmd, getUserId(), getUserName(), getDistributorId(), getPlatform(), getLanguage());
        return Response.of(ids);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserCustomerOrder,
        value = CommonLogTypeConstantDTO.UserCustomerOrderCreateExchange)
    @ApiOperation(value = "C端客户兑换码下单接口(包括兑换卡和第三方兑换业务)(业务已下架)")
    @PostMapping(value = "/exchange")
    public Response<BaseIds> createExchangeOrder(@Valid @RequestBody OrderInfoExchangeCmd cmd) {
        BaseIds ids = service.createExchangeOrder(cmd, getUserId(), getUserName(), getDistributorId(), getPlatform(),
            getLanguage());
        return Response.of(ids);
    }

    @ApiOperation(value = "获取订单列表")
    @GetMapping("/list")
    public Response<PageInfo<UserCustomerOrderInfoListDTO>>
        listOrderInfoByCustomerId(@Valid UserCustomerOrderInfoListQry qry) {
        PageInfo<UserCustomerOrderInfoListDTO> pageInfo = service.listCustomerOrderInfoByCustomerId(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "获取订单详情")
    @GetMapping("/detail")
    public Response<UserCustomerOrderInfoListDTO> getCustomerOrderDetailInfoById(@Valid BaseId id) {
        System.out.println("获取订单详情-订单ID：" + id);
        UserCustomerOrderInfoListDTO dto = service.getCustomerOrderDetailInfoById(id.getId());
        return Response.of(dto);
    }

    @ApiOperation(value = "根据第三方兑换码获取订单信息")
    @GetMapping("/find/by/thirdCode")
    public Response<OrderGoodsThirdCodeDTO> findOrderByThirdCode(@Valid UserCustomerOrderThirdCodeQry qry) {
        OrderGoodsThirdCodeDTO dto = service.findOrderByThirdCode(qry.getCode());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserCustomerOrder,
        value = CommonLogTypeConstantDTO.UserCustomerOrderWriteOffThirdCodeOrder)
    @ApiOperation(value = "根据第三方兑换码核销订单")
    @PutMapping("/writeOff/by/thirdCode")
    public Response writeOffThirdCodeOrder(@Valid @RequestBody UserCustomerOrderThirdCodeQry qry) {
        service.writeOffThirdCodeOrder(qry.getCode());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserCustomerOrder,
        value = CommonLogTypeConstantDTO.UserCustomerOrderCancel)
    @ApiOperation(value = "根据订单id取消订单")
    @PutMapping("/cancel")
    public Response orderCancel(@Valid @RequestBody OrderCancelCmd cmd) {
        service.orderCancel(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "C端获取订单失效时间（单位分钟）")
    @GetMapping("/time")
    public Response<Integer> getLoseTime() {
        Integer time = service.getLoseTime();
        return Response.of(time);
    }

}
