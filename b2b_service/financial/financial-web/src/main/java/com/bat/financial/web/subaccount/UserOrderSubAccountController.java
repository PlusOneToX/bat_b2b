package com.bat.financial.web.subaccount;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.subaccount.OrderSubAccountServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountPageDTO;
import com.bat.financial.api.subaccount.dto.OrderSubAccountPageQry;
import com.bat.financial.api.subaccount.dto.WechatPaySubAccountCmd;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "订单分账前台接口")
@RestController
@RequestMapping("/financial/v1/web/user/orderSubAccount")
public class UserOrderSubAccountController extends BaseController {

    @Autowired
    private OrderSubAccountServiceI orderSubAccountServiceI;

    @PostMapping(value = "/test")
    @ApiOperation(value = "测试")
    public Response test(@RequestBody WechatPaySubAccountCmd cmd) {
        orderSubAccountServiceI.test(cmd);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页")
    public Response<PageInfo<OrderSubAccountPageDTO>> page(@Valid OrderSubAccountPageQry orderSubAccountPageQry) {
        orderSubAccountPageQry.setDistributorId(Integer.parseInt(getUserId()));
        PageInfo<OrderSubAccountPageDTO> pageInfo = orderSubAccountServiceI.page(orderSubAccountPageQry);
        return Response.of(pageInfo);
    }

}
