package com.bat.thirdparty.factory.maike.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.factory.api.FactoryServiceHolder;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.factory.maike.service.dto.MaikeDeliverOrderResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.factory.maike.api.MaikeServiceI;
import com.bat.thirdparty.factory.maike.request.devlivery.ApiOrderDeliveryCallbackModelRequest;
import com.bat.thirdparty.factory.maike.request.order.ApiMaikeOrderCancelModelRequest;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;

import static com.bat.thirdparty.factory.maike.common.MaikeErrorConstant.DeliveryErrorCode;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/open/maike")
public class MaikeController {

    private static final Logger Logger = LoggerFactory.getLogger(MaikeController.class);

    @Autowired
    private MaikeServiceI maikeServiceI;

    @Resource
    private FactoryServiceHolder holder;

    /**
     * 麦客发货接口
     * 
     * @param signature
     *            签名
     * @param timestamp
     *            时间戳
     * @param request
     * @param result
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Maike, value = CommonLogTypeConstantDTO.MaikeDeliverOrder)
    @RequestMapping(value = "/deliverOrder")
    public ResponseBaseBean deliverOrder(@RequestParam(name = "signature", required = false) String signature,
        @RequestParam(name = "timestamp", required = false) String timestamp,
        @Valid @RequestBody ApiOrderDeliveryCallbackModelRequest request, BindingResult result) {
        Logger.info("麦客发货：" + signature + "、Timestamp:" + timestamp + "、参数：" + JSON.toJSONString(request));
        FactoryDeliverOrderReq req = new FactoryDeliverOrderReq();
        MaikeDeliverOrderResp orderResp = new MaikeDeliverOrderResp();
        orderResp.setSignature(signature);
        orderResp.setTimestamp(timestamp);
        orderResp.setRequest(request);
        req.setFactoryEnum(FactoryEnum.MK);
        req.setMaikeResp(orderResp);
        ResponseBaseBean baseBean = new ResponseBaseBean();
        try {
            holder.getService(FactoryEnum.MK).deliverOrder(req);
            return baseBean;
        } catch (ThirdPartyException e) {
            Logger.error(e.getCode()+","+e.getMsg());
            baseBean.setCode(Integer.valueOf(e.getCode()));
            baseBean.setMsg(e.getMsg());
            return baseBean;
        } catch (Exception e){
            Logger.error(e.getLocalizedMessage());
            baseBean.setCode(DeliveryErrorCode);
            baseBean.setMsg(e.getLocalizedMessage());
            return baseBean;
        }
    }

    @PostMapping(value = "/cancelOrder")
    public ResponseBaseBean cancelOrder(@RequestParam(name = "signature", required = false) String signature,
        @RequestParam(name = "timestamp", required = false) String timestamp,
        @Valid @RequestBody ApiMaikeOrderCancelModelRequest request) {
        Logger.info("麦客取消：" + signature + "、Timestamp:" + timestamp + "、参数：" + JSON.toJSONString(request));

        return maikeServiceI.cancelOrderFromFactory(request, signature, timestamp);
    }

}