package com.bat.thirdparty.order.controller;

import java.io.IOException;

import javax.validation.Valid;

import com.bat.thirdparty.common.base.BaseController;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.dto.ThirdIdDTO;
import com.bat.thirdparty.message.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.erp.manager.ErpDataManager;
import com.bat.thirdparty.order.api.AdminOrderServiceI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/order")
@Api(tags = "订单触发第三方调用后台接口")
public class AdminOrderController extends BaseController {

    @Autowired
    private AdminOrderServiceI adminOrderServiceI;

    @PutMapping(value = "/syncOrderToERP")
    @ApiOperation(value = "同步销售单给ERP")
    public Response syncOrderToERP(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        return adminOrderServiceI.syncOrderToERP(thirdIdDTO.getId());
    }

    @PutMapping(value = "/syncOrderToFactory")
    @ApiOperation(value = "同步订单到工厂")
    public Response syncOrderToFactory(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        return adminOrderServiceI.syncOrderToFactory(thirdIdDTO.getId());
    }

    @PutMapping(value = "/synchronizedLogisticsByOrderId")
    @ApiOperation(value = "根据订单Id同步物流信息")
    public Response synchronizedLogisticsByOrderId(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        return adminOrderServiceI.synchronizedLogisticsByOrderID(thirdIdDTO.getId());
    }

    @PutMapping(value = "/syncLogisticsToThird")
    @ApiOperation(value = "同步订单物流到第三方")
    public Response syncLogisticsToThird(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        return adminOrderServiceI.syncLogisticsToThird(thirdIdDTO.getId());
    }

    @PutMapping(value = "/syncDiyDeliveryOrderToERP")
    @ApiOperation(value = "同步柔性发货单到ERP")
    public Response syncDiyDeliveryOrderToERP(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        return adminOrderServiceI.syncDiyDeliveryOrderToERP(thirdIdDTO.getId());
    }

    @PutMapping(value = "/createLable")
    @ApiOperation(value = "手动生成标签")
    public com.bat.dubboapi.order.common.Response createLable(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        return adminOrderServiceI.createLable(thirdIdDTO.getId());
    }

    /**
     * 同步ERP快递单号
     * 
     * @return
     */
    @PostMapping(value = "/sync/expressNo")
    @ApiOperation(value = "同步ERP快递单号")
    public Response syncErpExpressNo() {
        return adminOrderServiceI.syncErpExpressNo();
    }

    /**
     * 添加任务
     * 
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/add")
    @ApiOperation(value = "测试定时器")
    public Response add() throws IOException {
        JSONObject requestInfo = new JSONObject();
        /* requestInfo.put("jobGroup",2);
        requestInfo.put("id",1640);
        requestInfo.put("jobDesc","同步ERP库存到B2B");
        requestInfo.put("executorRouteStrategy","FIRST");
        requestInfo.put("scheduleType","CRON");
        requestInfo.put("cronGen_display","0 0 0/1 * * ? ");
        requestInfo.put("jobCron","0 0 0/1 * * ? ");
        requestInfo.put("executorHandler","warehouseErpSyncB2BJobHandler");
        requestInfo.put("executorBlockStrategy","SERIAL_EXECUTION");
        requestInfo.put("executorTimeout",0);
        requestInfo.put("executorFailRetryCount",3);
        requestInfo.put("author","warehouse-service");*/
        requestInfo.put("jobGroup", 2);
        requestInfo.put("jobDesc", "同步ERP库存到B2B");
        requestInfo.put("author", "warehouse-service");
        requestInfo.put("scheduleType", "CRON");
        requestInfo.put("scheduleConf", "0 0 0/2 * * ? ");
        requestInfo.put("cronGen_display", "0 0 0/2 * * ? ");
        requestInfo.put("schedule_conf_CRON", "0 0 0/2 * * ? ");
        requestInfo.put("executorHandler", "warehouseErpSyncB2BJobHandler");
        requestInfo.put("executorRouteStrategy", "FIRST");
        requestInfo.put("misfireStrategy", "DO_NOTHING");
        requestInfo.put("executorBlockStrategy", "SERIAL_EXECUTION");
        requestInfo.put("executorTimeout", 0);
        requestInfo.put("executorFailRetryCount", 3);
        // requestInfo.put("id",1640);
        // JSONObject response=promotionExe.addJob("http://172.16.14.4:8082/xxl-job-admin",requestInfo);
        return Response.buildSuccess();

    }

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private ErpDataManager erpDataManager;

    @GetMapping(value = "/test3")
    @ApiOperation(value = "测试队列")
    public void test3() {
        messageSendService.syncErpStockToB2B();
        String token = erpDataManager.getToken("AXI-token");
        System.out.println("第一次：" + token);
        /*  erpDataManager.deleteCachedToken("AXI-token");
        token = erpDataManager.getToken("AXI-token");
        System.out.println("第二次："+token);*/
    }

}
