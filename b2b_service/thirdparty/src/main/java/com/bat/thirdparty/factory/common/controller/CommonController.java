package com.bat.thirdparty.factory.common.controller;

import static com.bat.thirdparty.factory.executor.ErrorCode.DELIVERORDERFAIL;

import javax.annotation.Resource;

import com.bat.thirdparty.factory.duohong.service.dto.DuoHongCommonResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.factory.api.FactoryServiceHolder;
import com.bat.thirdparty.factory.common.service.dto.OrderDeliveryCommonResp;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import com.bat.thirdparty.order.service.AdminOrderServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: 系统通用工厂控制器 为了实现手动发货的需求
 * @date 2019/11/30 22:00
 */
@RestController
@RequestMapping(value = "/thirdparty/v1/web/open/common")
@Slf4j
public class CommonController {

    @Resource
    private FactoryServiceHolder holder;

    @Resource
    private AdminOrderServiceImpl adminOrderService;

    /**
     * 普通发货回调接口
     *
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.KDS_FK,
        value = CommonLogTypeConstantDTO.KDSFKDeliverOrder)
    @PostMapping(value = "/deliverOrder")
    public Response deliverOrder(@RequestBody OrderDeliveryCommonResp resp) {
        // 查询物流编码与手动输入的是否匹配
//        Response response = adminOrderService.factoryTrackingNumber(resp.getOrderNo(), resp.getExpressNo());
//        if (!response.isSuccess()) {
//            return Response.buildFailure(response.getErrCode(), response.getErrMessage());
//        }
        resp.setManualFlag(true);
        log.info("通用发货物流回调 json：{}", JSON.toJSONString(resp));
        FactoryDeliverOrderReq orderResp = new FactoryDeliverOrderReq();
        orderResp.setFactoryEnum(FactoryEnum.COMMON);
        orderResp.setSystemResp(resp);
        DuoHongCommonResp commonResp = new DuoHongCommonResp();
        try {
            holder.getService(FactoryEnum.COMMON).deliverOrder(orderResp);
            commonResp.setRspType("msg");
            commonResp.setStatus("success");
            commonResp.setMsg("接收成功");
            return Response.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            commonResp.setRspType("msg");
            commonResp.setStatus("fail");
            commonResp.setMsg("物流处理回调处理失败");
            return Response.buildFailure(DELIVERORDERFAIL, "手动更新物流失败");
        }
    }
}
