package com.bat.order.web.order;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.orderquery.admin.AdminCustomerOrderInfoDetailDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminCustomerOrderInfoListQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDetailQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderInfoListDTO;
import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/23 14:47
 */
@Api(tags = "柔性定制订单列表后台接口")
@RequestMapping(value = "/order/v1/web/admin/customerDiyOrder")
@RestController
public class AdminCustomerDiyOrderController extends BaseController {
    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminCustomerDiyOrder,value =
    // CommonLogTypeConstantDTO.AdminCustomerDiyOrderList)
    @GetMapping(value = "/list")
    @ApiOperation(value = "订单列表")
    @SearchMethod
    public Response<PageInfo<AdminOrderInfoListDTO>>
        listCustomerDiyOrderInfo(@Valid AdminCustomerOrderInfoListQry qry) {
        PageInfo<AdminOrderInfoListDTO> pageInfo = orderInfoServiceI.listCustomerDiyOrderInfo(qry);
        return Response.of(pageInfo);
    }

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminCustomerDiyOrder,value =
    // CommonLogTypeConstantDTO.AdminCustomerDiyOrderDetail)
    @GetMapping(value = "/detail")
    @ApiOperation(value = "订单详情")
    public Response<AdminCustomerOrderInfoDetailDTO> getOrderDetail(@Valid BaseId id) {
        AdminOrderDetailQry qry = new AdminOrderDetailQry();
        qry.setId(id.getId());
        qry.setSearchType(SearchType.ADMIN_CUSTOMER_DIY_LIST);
        return Response.of(orderInfoServiceI.getCustomerOrderDetail(qry));
    }
}
