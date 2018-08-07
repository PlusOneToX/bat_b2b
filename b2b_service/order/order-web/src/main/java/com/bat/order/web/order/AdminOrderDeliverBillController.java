package com.bat.order.web.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.web.base.BaseController;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.deliver.OrderDeliverBillServiceI;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillDetailDTO;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillListDTO;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDeliverBillListQry;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 沙漠
 */
@Api(tags = "发货单列表订单后台接口")
@RequestMapping(value = "/order/v1/web/admin/orderDeliverBill")
@RestController
public class AdminOrderDeliverBillController extends BaseController {

    @Autowired
    private OrderDeliverBillServiceI userDeliverOrderBillServiceI;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderDeliverBill,value = CommonLogTypeConstantDTO.AdminOrderDeliverBillList)
    @GetMapping(value = "/list")
    @ApiOperation(value = "发货单列表")
    @SearchMethod
    public Response<PageInfo<AdminOrderDeliverBillListDTO>>
        listOrderDeliverBillByParam(@Valid AdminOrderDeliverBillListQry qry) {
        PageInfo<AdminOrderDeliverBillListDTO> pageInfo = userDeliverOrderBillServiceI.listOrderDeliverBillByParam(qry);
        return Response.of(pageInfo);
    }


    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminOrderDeliverBill,value = CommonLogTypeConstantDTO.AdminOrderDeliverBillDetail)
    @GetMapping(value = "/detail")
    @ApiOperation(value = "发货单详情")
    public Response<AdminOrderDeliverBillDetailDTO> getOrderDetail(@Valid BaseId id) {
        return Response.of(userDeliverOrderBillServiceI.getDetail(id.getId()));
    }

   // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminOrderDeliverBill, value = CommonLogTypeConstantDTO.AdminOrderDeliverBillLogisticsView)
    @GetMapping(value = "/logistics/view")
    @ApiOperation(value = "订单物流查看")
    public Response<OrderDeliverDetailDTO> logisticsView(@Valid BaseId id) {
        return Response.of(userDeliverOrderBillServiceI.logisticsView(id.getId()));
    }

    @GetMapping(value = "/resetDeliverCount")
    @ApiOperation(value = "重置发货数量脚本")
    public Response resetDeliverCount(){
        orderGoodsServiceI.resetDeliverCount();
        return Response.buildSuccess();
    }
}
