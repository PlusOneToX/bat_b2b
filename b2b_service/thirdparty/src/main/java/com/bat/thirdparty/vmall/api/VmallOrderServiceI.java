package com.bat.thirdparty.vmall.api;

import com.bat.thirdparty.vmall.dto.TimeDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;

import java.util.List;

public interface VmallOrderServiceI {


    List<OrderBaseOnIdCmd> vmallOrderPull(TimeDTO time);

    ResponseBaseBean sendGoodsCallBack(OrderLogistics logistics, String otherOrderNo);
}
