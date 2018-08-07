package com.bat.order.web.order;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderTypeServiceI;
import com.bat.order.api.order.dto.common.OrderTypeListQry;
import com.bat.order.api.order.dto.common.OrderTypeId;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/24 9:59
 */
@Api(tags = "订单类型前台接口")
@RequestMapping(value = "/order/v1/web/user/orderType")
@RestController
public class UserOrderTypeController extends BaseController {

    @Autowired
    private OrderTypeServiceI orderTypeServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value = "订单类型列表")
    @SearchMethod
    public Response<PageInfo<OrderTypeDTO>> listOrderType(@Valid OrderTypeListQry qry) {
        PageInfo<OrderTypeDTO> pageInfo = orderTypeServiceI.listOrderType(qry);
        return Response.of(pageInfo);
    }

    @GetMapping()
    @ApiOperation(value = "查询订单类型")
    @SearchMethod
    public Response<OrderTypeDTO> getOrderType(@Valid OrderTypeId id) {
        return Response.of(orderTypeServiceI.getOrderType(id.getId()));
    }

}
