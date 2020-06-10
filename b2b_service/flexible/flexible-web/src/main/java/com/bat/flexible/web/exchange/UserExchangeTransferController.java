
package com.bat.flexible.web.exchange;

import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeTransferServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeTransferRecordDTO;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 兑换卡转赠
 */

@RestController
@RequestMapping(value = "/flexible/v1/web/user/exchangeTransfer")
@Api(tags = "兑换卡转赠前台接口")
public class UserExchangeTransferController extends BaseController {

    @Autowired
    private ExchangeTransferServiceI exchangeTransferServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeTransferBe,
            value = CommonLogTypeConstantDTO.ExchangeTransferSendOut)
    @ApiOperation(value = "兑换码发起转赠")
    @PostMapping("/sendOut")
    public Response sendOut(@RequestBody  @Valid FlexibleIdDTO flexibleIdDTO) {
        ExchangeCodeTransferRecordDTO exchangeCodeTransferRecordDTO = exchangeTransferServiceI.sendOut(getUserId(), getUserName(), flexibleIdDTO.getId());
        return Response.of(exchangeCodeTransferRecordDTO);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeTransferBe,
            value = CommonLogTypeConstantDTO.ExchangeTransferReceive)
    @ApiOperation(value = "兑换码接收")
    @PostMapping("/receive")
    public Response receive(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        exchangeTransferServiceI.receive(getUserId(), getUserName(), flexibleIdDTO.getId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "转赠详情")
    @GetMapping("/detail")
    public Response detail(@Valid FlexibleIdDTO flexibleIdDTO) {
        ExchangeCodeTransferRecordDTO exchangeCodeTransferRecordDTO = exchangeTransferServiceI.detail(flexibleIdDTO.getId());
        return Response.of(exchangeCodeTransferRecordDTO);
    }


}

