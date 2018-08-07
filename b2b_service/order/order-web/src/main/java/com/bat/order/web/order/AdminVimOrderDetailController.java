package com.bat.order.web.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.common.OrderGoodsVmiDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminDistributorOrderInfoDetailDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDetailQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderGoodsVmiListQry;
import com.bat.order.web.base.BaseController;
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
@Api(tags = "VMI订单明细")
@RequestMapping(value = "/order/v1/web/admin/vimOrderDetail")
@RestController
public class AdminVimOrderDetailController extends BaseController {

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value = "VMI订单明细列表")
    @SearchMethod
    public Response<PageInfo<OrderGoodsVmiDTO>> listVmiByParam(@Valid AdminOrderGoodsVmiListQry qry) {
        PageInfo<OrderGoodsVmiDTO> pageInfo = orderGoodsServiceI.listVmiByParam(qry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/detail")
    @ApiOperation(value = "订单详情")
    public Response<AdminDistributorOrderInfoDetailDTO> getOrderDetail(@Valid BaseId id) {
        AdminOrderDetailQry qry = new AdminOrderDetailQry();
        qry.setId(id.getId());
        qry.setSearchType(SearchType.ADMIN_DISTRIBUTOR_ORDER_LIST);
        return Response.of(orderInfoServiceI.getDistributorOrderDetail(qry));
    }

}
