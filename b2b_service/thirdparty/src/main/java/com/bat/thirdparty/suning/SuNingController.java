package com.bat.thirdparty.suning;

import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.suning.api.SuNingServiceI;
import com.bat.thirdparty.suning.api.dto.MiniLinkQry;
import com.bat.thirdparty.suning.common.SuNingConstant;
import com.bat.thirdparty.suning.request.CommonUrlRequest;
import com.bat.thirdparty.suning.response.ActiveBaseResponse;
import com.bat.thirdparty.suning.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.thirdparty.common.Response;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/thirdparty/v1/api/open/suning")
public class SuNingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuNingController.class);

    @Autowired
    private SuNingServiceI suNingServiceI;

    @ApiOperation(value = "苏宁下单，bat派工")
    @PostMapping
    @ResponseBody
    public ActiveBaseResponse request(CommonUrlRequest commonUrlRequest, HttpServletRequest request) {
        try {
            LOGGER.info("接收到的苏宁公共参数:{}", JSONObject.toJSONString(commonUrlRequest));
            JSONObject json = RequestUtil.getParameters(request);
            LOGGER.info("接收到的苏宁参数转化为标准json数据:{}", json);
            if (commonUrlRequest.getMethod().equals(SuNingConstant.createOrderSend)) {
                return suNingServiceI.createOrder(commonUrlRequest, json);
            } else if (commonUrlRequest.getMethod().equals(SuNingConstant.oredrModifySend)) {
                return suNingServiceI.modifyOrder(commonUrlRequest, json);
            } else {
                return suNingServiceI.errorMsg(null);
            }
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            return suNingServiceI.errorMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return suNingServiceI.errorMsg(null);
        }
    }

    @GetMapping("/link")
    @ApiOperation(value = "获取小程序scheme码")
    @ResponseBody
    public Response<String> link(MiniLinkQry qry) {
        String link = suNingServiceI.link(qry.getDistributorId(), qry.getPageUrl());
        return Response.of(link);
    }

    /*    @GetMapping("test1")
    @ResponseBody
    public void request(String orderId) {
        suNingServiceI.sign(orderId);
    }*/

    @GetMapping("test2")
    @ResponseBody
    public void request2(String orderId) {
        // suNingServiceI.destroy(orderId);
    }

}
