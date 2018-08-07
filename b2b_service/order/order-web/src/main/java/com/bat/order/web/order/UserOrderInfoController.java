package com.bat.order.web.order;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bat.order.web.base.BaseController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.dto.export.OrderInfoExportQry;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/order/v1/web/user/orderInfo")
@Api(tags = "运营前台订单接口")
public class UserOrderInfoController extends BaseController {

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
}
