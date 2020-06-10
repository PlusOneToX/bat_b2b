
package com.bat.flexible.web.exchange;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.ExchangeQuanyiCmd;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 兑换卡转赠
 */

@RestController
@RequestMapping(value = "/flexible/v1/web/user/exchangeQuanyi")
@Api(tags = "兑换卡权益前台接口")
public class UserExchangeQuanyiController extends BaseController {

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeQuanyiBe,
            value = CommonLogTypeConstantDTO.ExchangeQuanyiExchange)
    @ApiOperation(value = "兑换码权益兑换")
    @PostMapping("/exchange")
    public Response<Integer> exchange(@RequestBody @Valid ExchangeQuanyiCmd cmd) {
        return Response.of(exchangeCodeServiceI.quanyiExchange(cmd));
    }


}

