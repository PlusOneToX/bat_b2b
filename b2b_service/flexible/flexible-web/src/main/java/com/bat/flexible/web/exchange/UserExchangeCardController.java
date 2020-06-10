package com.bat.flexible.web.exchange;

import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.ExchangeCardServiceI;
import com.bat.flexible.api.exchange.ExchangeCodeUserServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCardParamDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 兑换卡对接柔性
 */
@RestController
@RequestMapping(value = "/flexible/v1/web/user/exchangeCard")
@Api(tags = "兑换卡前台接口")
public class UserExchangeCardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserExchangeCardController.class);

    @Autowired
    private ExchangeCardServiceI exchangeCardServiceI;

    @Autowired
    private ExchangeCodeUserServiceI exchangeCodeUserServiceI;



    /**
     * 获取随机的兑换卡id
     * @return
     */
    @GetMapping("/getDefaultExchange")
    @ApiOperation(value = "获取默认的兑换卡")
    public Response getDefaultExchangeIdNew(@Valid FlexibleIdDTO flexibleIdDTO){
        return exchangeCardServiceI.getDefaultExchangeIdNew(flexibleIdDTO.getId());
    }

    @GetMapping(value = "/listModelAndMaterial")
    @ApiOperation(value = "根据兑换卡id查询其材质和型号树结构")
    public Response listModelAndMaterial (@Valid FlexibleIdDTO flexibleIdDTO){
        LOGGER.info("==================传进来的兑换卡id是："+flexibleIdDTO.getId()+"==============");

        ExchangeCardParamDTO exchangeCardParamDTO = exchangeCardServiceI.listModelAndMaterial(flexibleIdDTO.getId());

        return Response.of(exchangeCardParamDTO);
    }


}
