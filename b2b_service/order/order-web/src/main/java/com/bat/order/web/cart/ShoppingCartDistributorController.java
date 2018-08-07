package com.bat.order.web.cart;

import java.util.List;

import javax.validation.Valid;

import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.base.BaseController;
import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.ShoppingCartDistributorServiceI;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;
import com.bat.order.api.common.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "前台分销商购物车接口")
@RequestMapping("/order/v1/web/user/shoppingcart")
public class ShoppingCartDistributorController extends BaseController {

    @Autowired
    private ShoppingCartDistributorServiceI service;

    @ApiOperation(value = "获取购物车商品列表")
    @GetMapping(value = "/list")
    public Response<List<ShoppingCartDTO>> list() {
        String userId = getUserId();
        if (StringUtils.isBlank(userId)) {
            userId = "6209";
        }
        List<ShoppingCartDTO> dtos = service.list(getUserId(), getLanguage());
        return Response.of(dtos);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ShoppingCartDistributor,
        value = CommonLogTypeConstantDTO.ShoppingCartDistributorAdd)
    @ApiOperation(value = "新增购物车商品")
    @PostMapping()
    public Response<BaseId> create(@Valid @RequestBody ShoppingCartCmd cmd) {
        BaseId id = service.create(cmd, getUserId(), getLanguage());
        return Response.of(id);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ShoppingCartDistributor,
        value = CommonLogTypeConstantDTO.ShoppingCartDistributorAdds)
    @ApiOperation(value = "批量添加购物车商品")
    @PostMapping(value = "/list")
    public Response createList(@Valid @RequestBody List<ShoppingCartCmd> cmds) {
        service.createList(cmds, getUserId(), getLanguage());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ShoppingCartDistributor,
        value = CommonLogTypeConstantDTO.ShoppingCartDistributorUpdate)
    @ApiOperation(value = "修改购物车商品")
    @PutMapping()
    public Response update(@Valid @RequestBody ShoppingCartUpdateCmd cmd) {
        service.update(cmd, getUserId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ShoppingCartDistributor,
        value = CommonLogTypeConstantDTO.ShoppingCartDistributorDeletes)
    @ApiOperation(value = "批量删除购物车")
    @DeleteMapping(value = "/ids")
    public Response deleteByIds(@RequestBody @Valid BaseIds cmd) {
        service.deleteByIds(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ShoppingCartDistributor,
        value = CommonLogTypeConstantDTO.ShoppingCartDistributorDelete)
    @ApiOperation(value = "删除购物车")
    @DeleteMapping
    public Response deleteById(@RequestBody @Valid BaseId cmd) {
        service.deleteById(cmd);
        return Response.buildSuccess();
    }

}
