package com.bat.thirdparty.order.controller;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.base.BaseController;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.dto.ThirdIdDTO;
import com.bat.thirdparty.log.api.dto.ThirdLogPageQry;
import com.bat.thirdparty.order.api.dto.log.LogAddressUpdateDTO;
import com.bat.thirdparty.order.api.dto.log.OrderBusinessPageQry;
import com.bat.thirdparty.order.api.dto.log.ThirdOrderSyncLogDTO;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/orderBusinessLog")
@Api(tags = "业务日志后台接口")
public class AdminOrderBusinessLogController extends BaseController {

    @Autowired
    private OrderBusinessLogServiceI orderBusinessLogServiceI;

    @GetMapping(value = "/pageSyncOrderLog")
    @ApiOperation(value = "分页查询第三方柔性订单日志")
    public Response<PageInfo<ThirdOrderSyncLogDTO>> pageSyncOrderLog(@Valid OrderBusinessPageQry  orderBusinessPageQry){
        PageInfo<ThirdOrderSyncLogDTO> pageInfo = orderBusinessLogServiceI.pageSyncOrderLog(orderBusinessPageQry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "业务日志列表")
    @GetMapping("/page")
    public Response<PageInfo<OrderBusinessLogDO>> page(@Valid ThirdLogPageQry thirdLogPageQry) {
        PageInfo<OrderBusinessLogDO> pageInfo = orderBusinessLogServiceI.page(thirdLogPageQry);
        return Response.of(pageInfo);
    }

    @PutMapping(value = "/updateAddress")
    @ApiOperation(value = "修改订单地址")
    public Response updateAddress(@Valid @RequestBody LogAddressUpdateDTO addressUpdateDTO){

        return orderBusinessLogServiceI.updateAddress(addressUpdateDTO);
    }

    @PutMapping(value = "/pushAgain")
    @ApiOperation(value = "重推")
    public Response pushAgain(@Valid @RequestBody ThirdIdDTO thirdIdDTO, HttpServletRequest request){

        return orderBusinessLogServiceI.pushAgian(thirdIdDTO.getId(),getUserName(),request);
    }

    @DeleteMapping
    @ApiOperation(value = "删除业务日志")
    public Response delete(@Valid @RequestBody ThirdIdDTO thirdIdDTO){

        return orderBusinessLogServiceI.deleteById(thirdIdDTO.getId());
    }

}
