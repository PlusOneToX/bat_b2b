package com.bat.order.web.order;


import com.bat.order.web.base.BaseController;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.dto.export.OrderInfoExportQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminJudgeOrderDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminJudgeOrderQry;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "/order/v1/web/admin/orderInfo")
@Api(tags = "运营订单后台接口")
public class AdminOrderInfoController extends BaseController {

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @PostMapping("/export")
    @ApiOperation(value = "订单导出")
    public Response export(@RequestBody OrderInfoExportQry orderInfoExportQry, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HSSFWorkbook wb = orderInfoServiceI.createExcelByCondition(orderInfoExportQry,getLanguage());
        if (wb == null) {
            return Response.buildFailure("11", "excel导出失败");
        }
        exportExcel("订单导出.xls",wb,request,response);
        return null;
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "测试")
    public void test(){
        orderInfoServiceI.test();
    }

    @GetMapping(value = "/judgeDistributorOrderStatus")
    @ApiOperation(value = "判断订单有没有生成分销层数据")
    public Response<AdminJudgeOrderDTO> judgeDistributorOrderStatus(@Valid AdminJudgeOrderQry qry){
        AdminJudgeOrderDTO dto =orderInfoServiceI.judgeDistributorOrderStatus(qry);
        return Response.of(dto);
    }
}
