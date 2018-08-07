package com.bat.order.web.order;


import com.github.pagehelper.PageInfo;
import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseSearchQry;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.deliver.OrderDeliveryStopPlaceServiceI;
import com.bat.order.api.deliver.dto.OrderDeliverStopPlaceCmd;
import com.bat.order.api.deliver.dto.data.OrderDeliverStopPlaceDTO;
import com.bat.order.api.exchange.dto.ExchangeCodePageQry;
import com.bat.order.api.exchange.dto.OrderGoodsExchangeCodeListDTO;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.dto.common.OrderTypeCmd;
import com.bat.order.api.order.dto.export.OrderInfoExportQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminJudgeOrderDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminJudgeOrderQry;
import com.bat.order.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order/v1/web/admin/orderDeliverStopPlace")
@Api(tags = "快递停发区域")
public class AdminOrderDeliverStopPlaceController extends BaseController {

    @Autowired
    private OrderDeliveryStopPlaceServiceI orderDeliveryStopPlaceServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value = "停发列表")
    public Response<PageInfo<OrderDeliverStopPlaceDTO>> list(BaseSearchQry qry) {
        PageInfo<OrderDeliverStopPlaceDTO> pageInfo = orderDeliveryStopPlaceServiceI.selectList(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminStopPlace, value = CommonLogTypeConstantDTO.AdminStopPlaceAdd)
    @PostMapping()
    @ApiOperation(value = "添加")
    public Response add(@Valid @RequestBody OrderDeliverStopPlaceCmd cmd) {
        orderDeliveryStopPlaceServiceI.create(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminStopPlace,value = CommonLogTypeConstantDTO.AdminStopPlaceUpdate)
    @PutMapping()
    @ApiOperation(value = "更新")
    public Response update(@Valid @RequestBody OrderDeliverStopPlaceCmd cmd) {
        orderDeliveryStopPlaceServiceI.update(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminStopPlace,value = CommonLogTypeConstantDTO.AdminStopPlaceDelete)
    @DeleteMapping
    @ApiOperation(value = "删除")
    public Response update(@RequestBody @Valid BaseId id) {
        orderDeliveryStopPlaceServiceI.delete(id.getId());
        return Response.buildSuccess();
    }
}
