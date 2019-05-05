package com.bat.thirdparty.erp.controller;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeRefundDTO;
import com.bat.dubboapi.flexible.exchange.dto.OrderExchangeCardBindErpRequest;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.erp.api.ErpExchangeCardServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping(value = "/thirdparty/v1/web/erp/exchangeCard")
@RestController
@Api(tags = "兑换卡ERP接口")
public class ErpExchangeCardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpExchangeCardController.class);


    @Autowired
    private ErpExchangeCardServiceI erpExchangeCardServiceI;

    /**
     * erp出库、绑定订单和盒码关系
     * @return
     */
    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ErpExchangeCard,value = CommonLogTypeConstantDTO.ErpExchangeCardBindOrderAndBoxCode)
    @PostMapping(value = "/bindOrderAndBoxCode")
    @ApiOperation(value = "erp出库、绑定订单和盒码关系")
    public ResponseBaseBean bindOrderAndBoxCode(@Valid @RequestBody OrderExchangeCardBindErpRequest orderExchangeCardBindErpRequest){
        LOGGER.info("erp出库、绑定分销商订单和盒码关系，参数为："+ JSON.toJSONString(orderExchangeCardBindErpRequest));


        return erpExchangeCardServiceI.bindOrderAndBoxCode(orderExchangeCardBindErpRequest);
    }

    /**
     * 兑换卡erp退货到b2b失效
     * @param exchangeCodeRefundDTO
     * @return
     */
    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ErpExchangeCard,value = CommonLogTypeConstantDTO.ErpExchangeCardRefund)
    @PostMapping(value = "/refund")
    @ApiOperation(value = "兑换卡erp退货到b2b失效")
    public ResponseBaseBean refund(@Valid @RequestBody ExchangeCodeRefundDTO exchangeCodeRefundDTO){



        return erpExchangeCardServiceI.refund(exchangeCodeRefundDTO);
    }


}
