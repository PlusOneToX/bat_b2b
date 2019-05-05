package com.bat.thirdparty.factory.haixing.controller;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.factory.haixing.service.dto.HaixingDeliverOrderResp;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.factory.api.FactoryServiceHolder;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/open/haixing")
@Slf4j
public class HaixingController {

    @Resource
    private FactoryServiceHolder holder;

    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.Haixing,value = CommonLogTypeConstantDTO.HaixingDeliverOrder)
	@PostMapping(value = "/deliverOrder")
    public String deliverOrder(@RequestBody HaixingDeliverOrderResp resp){
        log.info("海星发货物流回调 json：{}", JSON.toJSONString(resp));
        FactoryDeliverOrderReq orderResp = new FactoryDeliverOrderReq();
        orderResp.setFactoryEnum(FactoryEnum.LHW);
        orderResp.setHaixingResp(resp);
        try {
            holder.getService(FactoryEnum.LHW).deliverOrder(orderResp);
            return "SUCCESS";
        } catch (ThirdPartyException e) {
            log.error(e.getCode()+","+e.getMsg());
            return "FAIL";
        } catch (Exception e){
            log.error(e.getLocalizedMessage());
            return "FAIL";
        }
    }
}