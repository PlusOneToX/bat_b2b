package com.bat.order.web.order;

import javax.validation.Valid;

import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderPromotionDTO;
import com.bat.order.api.order.dto.distributor.OrderPromotionQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminDistributorOrderInfoDetailDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminDistributorOrderInfoListQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDetailQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderInfoListDTO;
import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.base.BaseController;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 15:03
 */
@Api(tags = "分销订单列表后台接口")
@RequestMapping(value = "/order/v1/web/admin/distributionOrder")
@RestController
public class AdminDistributionOrderController extends BaseController {

    @Autowired
    private OrderInfoServiceI service;

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminDistributionOrder,value =
    // CommonLogTypeConstantDTO.AdminDistributionOrderList)
    @GetMapping(value = "/list")
    @ApiOperation(value = "订单列表")
    @SearchMethod
    public Response<PageInfo<AdminOrderInfoListDTO>>
        listCheckingOrderInfo(@Valid AdminDistributorOrderInfoListQry qry) {
        PageInfo<AdminOrderInfoListDTO> pageInfo = service.listDistributorOrderInfo(qry);
        return Response.of(pageInfo);
    }

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminDistributionOrder,value =
    // CommonLogTypeConstantDTO.AdminDistributionOrderDetail)
    @GetMapping(value = "/detail")
    @ApiOperation(value = "订单详情")
    public Response<AdminDistributorOrderInfoDetailDTO> getOrderDetail(@Valid BaseId id) {
        AdminOrderDetailQry qry = new AdminOrderDetailQry();
        qry.setId(id.getId());
        qry.setSearchType(SearchType.ADMIN_DISTRIBUTOR_ORDER_LIST);
        return Response.of(service.getDistributorOrderDetail(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDistributionOrder,
        value = CommonLogTypeConstantDTO.AdminOrderCancel)
    @ApiOperation(value = "根据订单id取消订单")
    @PutMapping("/cancel")
    public Response orderCancel(@Valid @RequestBody OrderCancelCmd cmd) {
        service.orderCancel(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据订单活动条件ids获取活动信息")
    @GetMapping("/promotion")
    public Response<OrderPromotionDTO> orderPromotion(@Valid OrderPromotionQry qry) {
        OrderPromotionDTO dto = service.orderPromotion(qry);
        return Response.of(dto);
    }

}
