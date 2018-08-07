package com.bat.order.web.cart;

import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.base.BaseController;
import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.ShoppingCartCustomerServiceI;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.ShoppingCustomerCartCmd;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;
import com.bat.order.api.common.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "前台C端客户购物车接口")
@RequestMapping("/order/v1/web/user/customer/shoppingcart")
public class ShoppingCartCustomerController extends BaseController {

    @Autowired
    private ShoppingCartCustomerServiceI service;

    @ApiOperation(value = "获取购物车商品列表")
    @GetMapping(value = "/list")
    public Response<List<ShoppingCartDTO>> list() {
        List<ShoppingCartDTO> dtos = service.list(getUserId(), getDistributorId(), getLanguage());
        return Response.of(dtos);
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerAddDiy)
    @ApiOperation(value = "新增购物车商品(支持C端柔性定制)")
    @PostMapping(value = "/diy")
    public Response<BaseId> createDiy(@Valid @RequestBody ShoppingCustomerCartCmd cmd) {
        BaseId id = service.createDiy(cmd, getUserId(), getDistributorId(), getLanguage());
        return Response.of(id);
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerAddsDiy)
    @ApiOperation(value = "新增购物车商品(支持C端柔性定制)")
    @PostMapping(value = "/diy/list")
    public Response createDiyList(@Valid @RequestBody List<ShoppingCustomerCartCmd> cmds) {
        service.createDiyList(cmds, getUserId(), getDistributorId(), getLanguage());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerAdd)
    @ApiOperation(value = "新增购物车商品(支持非柔性定制)")
    @PostMapping()
    public Response<BaseId> create(@Valid @RequestBody ShoppingCartCmd cmd) {
        BaseId id = service.create(cmd, getUserId(), getDistributorId(), getLanguage());
        return Response.of(id);
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerAdds)
    @ApiOperation(value = "新增购物车商品(非柔性定制)")
    @PostMapping(value = "/list")
    public Response createList(@Valid @RequestBody List<ShoppingCartCmd> cmds) {
        service.createList(cmds, getUserId(), getDistributorId(), getLanguage());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerUpdate)
    @ApiOperation(value = "修改购物车商品")
    @PutMapping()
    public Response update(@Valid @RequestBody ShoppingCartUpdateCmd cmd) {
        service.update(cmd, getUserId(), getDistributorId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerDeleteByIds)
    @ApiOperation(value = "批量删除购物车")
    @DeleteMapping(value = "/ids")
    public Response deleteByIds(@RequestBody @Valid BaseIds cmd) {
        service.deleteByIds(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerDeleteByIds)
    @ApiOperation(value = "批量删除购物车(支付宝小程序)")
    @PutMapping(value = "/ids")
    public Response deleteByIds2(@RequestBody @Valid BaseIds cmd) {
        service.deleteByIds(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.ShoppingCartCustomer,value = CommonLogTypeConstantDTO.ShoppingCartCustomerDelete)
    @ApiOperation(value = "删除购物车")
    @DeleteMapping
    public Response deleteById(@RequestBody @Valid BaseId cmd) {
        service.deleteById(cmd);
        return Response.buildSuccess();
    }


}
