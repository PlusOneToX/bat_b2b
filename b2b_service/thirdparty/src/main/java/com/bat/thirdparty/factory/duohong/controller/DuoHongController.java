package com.bat.thirdparty.factory.duohong.controller;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.thirdparty.factory.api.FactoryServiceHolder;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.factory.duohong.service.dto.DuoHongCommonResp;
import com.bat.thirdparty.factory.duohong.service.dto.DuoHongDeliverOrderResp;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:45
 */
@RestController
@RequestMapping(value = "/thirdparty/v1/web/open/duohong")
@Slf4j
public class DuoHongController {

    @Resource
    private FactoryServiceHolder holder;

    /**
     * 多鸿发货回调接口
     *
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Duohong,
        value = CommonLogTypeConstantDTO.DuohongDeliverOrder)
    @PostMapping(value = "/deliverOrder")
    public DuoHongCommonResp deliverOrder(@RequestBody DuoHongDeliverOrderResp resp) {
        log.info("多鸿发货物流回调 json：{}", JSON.toJSONString(resp));
        FactoryDeliverOrderReq orderResp = new FactoryDeliverOrderReq();
        orderResp.setFactoryEnum(FactoryEnum.DH_OLK);
        orderResp.setDuoHongResp(resp);
        DuoHongCommonResp commonResp = new DuoHongCommonResp();
        try {
            holder.getService(FactoryEnum.DH_OLK).deliverOrder(orderResp);
            commonResp.setRspType("msg");
            commonResp.setStatus("success");
            commonResp.setMsg("接收成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResp.setRspType("msg");
            commonResp.setStatus("fail");
            commonResp.setMsg("物流处理回调处理失败");
        }
        return commonResp;
    }
}
