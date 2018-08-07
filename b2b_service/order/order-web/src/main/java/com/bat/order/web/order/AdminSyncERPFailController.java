package com.bat.order.web.order;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bat.order.web.annotation.SearchMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.orderquery.admin.*;
import com.bat.order.api.utils.excel.ExcelUtils;
import com.bat.order.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/23 14:49
 */
@Api(tags = "同步erp失败订单后台接口")
@RequestMapping(value = "/order/v1/web/admin/syncERPFail")
@RestController
public class AdminSyncERPFailController extends BaseController {
    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @Resource
    ExcelUtils excelUtils;

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.SynErpOrderFail,value =
    // CommonLogTypeConstantDTO.SynErpOrderFailList)
    @GetMapping(value = "/list")
    @ApiOperation(value = "订单列表")
    @SearchMethod
    public Response<PageInfo<AdminOrderInfoListDTO>> listExceptionOrderInfo(@Valid AdminExceptionOrderInfoListQry qry) {
        PageInfo<AdminOrderInfoListDTO> pageInfo = orderInfoServiceI.listExceptionOrderInfo(qry);
        return Response.of(pageInfo);
    }

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.SynErpOrderFail,value =
    // CommonLogTypeConstantDTO.SynErpOrderFailDetail)
    @GetMapping(value = "/detail")
    @ApiOperation(value = "订单详情")
    public Response<AdminDistributorOrderInfoDetailDTO> getOrderDetail(@Valid BaseId id) {
        AdminOrderDetailQry qry = new AdminOrderDetailQry();
        qry.setId(id.getId());
        qry.setSearchType(SearchType.ADMIN_ERP_ORDER_LIST);
        return Response.of(orderInfoServiceI.getDistributorOrderDetail(qry));
    }

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.SynErpOrderFail,value =
    // CommonLogTypeConstantDTO.SynErpOrderFailExport)
    @GetMapping("/export")
    @ApiOperation(value = "报表导出")
    public void orderExport(@Valid AdminExceptionOrderInfoListQry qry, HttpServletResponse response)
        throws IOException {
        qry.setPage(null);
        qry.setSize(null);
        PageInfo<AdminOrderInfoListDTO> pageInfo = orderInfoServiceI.listExceptionOrderInfo(qry);
        List<AdminOrderInfoListExcelDTO> dtos =
            BeanUtils.copyList(pageInfo.getList(), AdminOrderInfoListExcelDTO.class);
        excelUtils.getExporter().exportExcel(AdminOrderInfoListExcelDTO.class, dtos, response);
    }
}
