package com.bat.order.web.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.exchange.api.OrderGoodsExchangeCodeServiceI;
import com.bat.order.api.exchange.dto.ExchangeCodePageQry;
import com.bat.order.api.exchange.dto.OrderGoodsExchangeCodeListDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order/v1/web/admin/exchangeCard")
@Api(tags = "兑换卡订单后台接口")
public class AdminOrderExchangeController {

    @Autowired
    private OrderGoodsExchangeCodeServiceI orderGoodsExchangeCodeServiceI;

    @GetMapping(value = "/pageExchange")
    @ApiOperation(value = "分页查询兑换码兑换情况")
    public Response<PageInfo<OrderGoodsExchangeCodeListDTO>> pageExchange(@Valid ExchangeCodePageQry exchangeCodePageQry){
        PageInfo<OrderGoodsExchangeCodeListDTO> pageInfo = orderGoodsExchangeCodeServiceI.page(exchangeCodePageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "测试")
    public void test(){
        orderGoodsExchangeCodeServiceI.test();
    }
}
