package com.bat.order.web.order;


import com.github.pagehelper.PageInfo;
import com.bat.order.web.base.BaseController;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseSearchQry;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.deliver.OrderDeliveryStopPlaceServiceI;
import com.bat.order.api.deliver.dto.OrderDeliverStopPlaceCmd;
import com.bat.order.api.deliver.dto.data.OrderDeliverStopPlaceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order/v1/web/user/orderDeliverStopPlace")
@Api(tags = "快递停发区域")
public class UserOrderDeliverStopPlaceController extends BaseController {

    @Autowired
    private OrderDeliveryStopPlaceServiceI orderDeliveryStopPlaceServiceI;

    @GetMapping(value = "/findMatch")
    @ApiOperation(value = "查询该地址是否停发")
    public Response<Boolean> findMatch(String content) {
       boolean flag = orderDeliveryStopPlaceServiceI.findMatch(content);
        return Response.of(flag);
    }

}
