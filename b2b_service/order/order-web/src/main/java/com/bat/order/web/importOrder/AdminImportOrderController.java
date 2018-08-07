package com.bat.order.web.importOrder;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.importOrder.api.ImportOrderService;
import com.bat.order.api.importOrder.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/2 10:04
 */
@Api(tags = "订单导入后台接口")
@RequestMapping(value = "/order/v1/web/admin/importOrder")
@RestController
public class AdminImportOrderController {

    @Resource
    private ImportOrderService importOrderService;

    @GetMapping("/tempDownLoad")
    @ApiOperation(value = "获取下载模板URL")
    public Response getTempUrl(TempDownLoadQry tempDownLoadQry) {
        String url = importOrderService.getTempUrl(tempDownLoadQry.getFileType(), true);
        return Response.of(url);
    }

    @GetMapping()
    @ApiOperation(value = "导入订单详情")
    public Response<ImportOrderDetailDTO> importOrderDetail(ImportOrderDetailQry qry) throws IOException {
        ImportOrderDetailDTO detail = importOrderService.importOrderDetail(qry);
        return Response.of(detail);
    }

    @PutMapping()
    @ApiOperation(value = "导入订单修改")
    public Response importOrderUpdate(@RequestBody ImportOrderUpdateCmd cmd) throws IOException {
        importOrderService.importOrderUpdate(cmd);
        return Response.buildSuccess();
    }

    @PostMapping("/import")
    @ApiOperation(value = "订单导入接口")
    public Response importOrder(MultipartFile file) throws IOException {
        importOrderService.importOrder(file.getInputStream(), null);
        return Response.buildSuccess();
    }

    @PostMapping("/orderDownLoad")
    @ApiOperation(value = "订单导出")
    public Response downLoad(@RequestBody ImportOrderDownloadQry qry) {
        importOrderService.downLoad(qry);
        return Response.buildSuccess();
    }

    @GetMapping("/list")
    @ApiOperation(value = "导入订单列表")
    public Response<PageInfo<ImportOrderDTO>> importOrderList(ImportOrderListQry qry) throws IOException {
        PageInfo<ImportOrderDTO> page = importOrderService.importOrderList(qry);
        return Response.of(page);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除导入项")
    public Response deleteImportOrderInfo(@RequestBody ImportOrderIds request) throws IOException {
        importOrderService.deleteImportOrderInfo(request);
        return Response.buildSuccess();
    }

    @PutMapping("/orders")
    @ApiOperation(value = "导入订单批量下单")
    public Response importOrders(@RequestBody ImportOrderIds qry) {
        importOrderService.importOrders(qry, true, null, null, null);
        return Response.buildSuccess();
    }

}
