package com.bat.order.web.order;

import javax.validation.Valid;

import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.base.BaseController;
import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderTypeServiceI;
import com.bat.order.api.order.dto.common.OrderTypeListQry;
import com.bat.order.api.order.dto.common.OrderTypeCmd;
import com.bat.order.api.order.dto.common.OrderTypeId;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/24 9:59
 */
@Api(tags = "订单类型后台接口")
@RequestMapping(value = "/order/v1/web/admin/orderType")
@RestController
public class AdminOrderTypeController extends BaseController {

    @Autowired
    private OrderTypeServiceI orderTypeServiceI;

    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderType,value = CommonLogTypeConstantDTO.AdminOrderTypeList)
    @GetMapping(value = "/list")
    @ApiOperation(value = "订单类型列表")
    @SearchMethod
    public Response<PageInfo<OrderTypeDTO>> listOrderType(@Valid OrderTypeListQry qry) {
        PageInfo<OrderTypeDTO> pageInfo = orderTypeServiceI.listOrderType(qry);
        return Response.of(pageInfo);
    }

    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderType,value = CommonLogTypeConstantDTO.AdminOrderTypeFind)
    @GetMapping()
    @ApiOperation(value = "查询订单类型")
    @SearchMethod
    public Response<OrderTypeDTO> getOrderType(@Valid OrderTypeId id) {
        return Response.of(orderTypeServiceI.getOrderType(id.getId()));
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderType,value = CommonLogTypeConstantDTO.AdminOrderTypeAdd)
    @PostMapping()
    @ApiOperation(value = "订单类型添加")
    public Response createOrderType(@Valid @RequestBody OrderTypeCmd qry) {
        orderTypeServiceI.createOrderType(qry);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderType,value = CommonLogTypeConstantDTO.AdminOrderTypeUpdate)
    @PutMapping()
    @ApiOperation(value = "订单类型更新")
    public Response updateOrderType(@Valid @RequestBody OrderTypeCmd qry) {
        orderTypeServiceI.updateOrderType(qry);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderType,value = CommonLogTypeConstantDTO.AdminOrderTypeDelete)
    @DeleteMapping()
    @ApiOperation(value = "订单类型删除")
    public Response deleteOrderType(@Valid @RequestBody OrderTypeId id) {
        orderTypeServiceI.deleteOrderType(id.getId());
        return Response.buildSuccess();
    }
}
