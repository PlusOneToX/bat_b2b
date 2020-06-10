
package com.bat.flexible.web.exchange;

import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.ExchangeShareCarryOutCmd;
import com.bat.flexible.api.exchange.dto.ExchangeShareCarryOutDTO;
import com.bat.flexible.api.exchange.dto.ExchangeShareDTO;
import com.bat.flexible.api.exchange.dto.ExchangeShareQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeSpecialServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.exchange.ExchangeShareServiceI;
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
@RequestMapping(value = "/flexible/v1/web/user/exchangeShare")
@Api(tags = "兑换卡转发前台接口")
public class UserExchangeShareController extends BaseController {

    @Autowired
    private ExchangeShareServiceI exchangeShareServiceI;

    @Autowired
    private ExchangeSpecialServiceI exchangeSpecialServiceI;


    @ApiOperation(value = "查看是否有转发活动")
    @PostMapping("/find")
    public Response<ExchangeShareDTO> find(@RequestBody @Valid ExchangeShareQry exchangeShareQry) {
        ExchangeShareDTO exchangeShareDTO = exchangeShareServiceI.find(exchangeShareQry);
        return Response.of(exchangeShareDTO);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeShareBe,
            value = CommonLogTypeConstantDTO.ExchangeShareBeOut)
    @ApiOperation(value = "进行转发")
    @PostMapping("/carryOut")
    public Response<ExchangeShareCarryOutDTO> carryOut(@RequestBody @Valid ExchangeShareCarryOutCmd exchangeShareCarryOutCmd) {
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO= exchangeShareServiceI.carryOut(exchangeShareCarryOutCmd,getUserId());
        return Response.of(exchangeShareCarryOutDTO);
    }

    @ApiOperation(value = "二次转发")
    @PostMapping("/second/carryOut")
    public Response<ExchangeShareCarryOutDTO> secondCarryOut(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO = exchangeShareServiceI.secondCarryOut(flexibleIdDTO.getId(),getUserId());
        return Response.of(exchangeShareCarryOutDTO);
    }

    @ApiOperation(value = "查看发布详情")
    @PostMapping("/releaseDetail")
    public Response<ExchangeShareCarryOutDTO> releaseDetail(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO = exchangeShareServiceI.releaseDetail(flexibleIdDTO.getId(),getUserId());
        return Response.of(exchangeShareCarryOutDTO);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeShareBe,
            value = CommonLogTypeConstantDTO.ExchangeShareBeReceive)
    @ApiOperation(value = "转发接收")
    @PostMapping("/receive")
    public Response receive(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        exchangeShareServiceI.receive(getUserId(), flexibleIdDTO.getId(), getCurrentAdmin());
        return Response.buildSuccess();
    }
}

