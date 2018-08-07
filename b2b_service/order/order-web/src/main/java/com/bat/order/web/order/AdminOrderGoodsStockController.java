package com.bat.order.web.order;

import com.bat.order.api.common.response.Response;
import com.bat.order.api.stock.api.OrderGoodsStockServiceI;
import com.bat.order.api.stock.dto.OrderGoodsStockQry;
import com.bat.order.api.stock.dto.OrderGoodsStockQryDTO;
import com.bat.order.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "订单库存后台接口")
@RequestMapping(value = "/order/v1/web/admin/orderGoodsStock")
@RestController
public class AdminOrderGoodsStockController extends BaseController {

    @Autowired
    private OrderGoodsStockServiceI orderGoodsStockServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value="条件查询库存锁库订单列表")
    public Response<List<OrderGoodsStockQryDTO>> list(@Valid OrderGoodsStockQry orderGoodsStockPageQry){
        List<OrderGoodsStockQryDTO> list = orderGoodsStockServiceI.listDTOByCondition(orderGoodsStockPageQry);
        return Response.of(list);
    }


}
