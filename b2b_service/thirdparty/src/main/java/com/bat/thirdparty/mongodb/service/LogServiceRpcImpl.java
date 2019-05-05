package com.bat.thirdparty.mongodb.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.log.api.dto.OrderDeliverBillLogQry;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.mongodb.api.LogServiceRpc;
import com.bat.dubboapi.thirdparty.mongodb.dto.OrderLogCmd;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderDeliverBillLogDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderLogDTO;
import com.bat.thirdparty.mongodb.dao.dataobject.OrderDeliverBillLogDO;
import com.bat.thirdparty.mongodb.executor.LogCmdExe;
import com.bat.thirdparty.mongodb.executor.LogQryExe;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/9 8:51
 */
@DubboService
public class LogServiceRpcImpl implements LogServiceRpc {

    @Resource
    private LogCmdExe logCmdExe;

    @Resource
    private LogQryExe logQryExe;

    @Override
    public Response createOrderLog(OrderLogCmd cmd) {
        logCmdExe.createOrderLog(cmd);
        return Response.buildSuccess();
    }

    @Override
    public Response<List<OrderLogDTO>> findOrderLogByOrderId(Integer orderId) {
        List<OrderLogDTO> dtos = logQryExe.findOrderLogByOrderId(orderId);
        return Response.of(dtos);
    }

    @Override
    public Response<List<OrderDeliverBillLogDTO>> findOrderDeliverBillLogByIdsAndOperateType(List<Integer> ids,
        String operateType) {
        OrderDeliverBillLogQry qry = new OrderDeliverBillLogQry();
        qry.setOperateType(operateType);
        qry.setOrderDeliverBillIds(ids);
        List<OrderDeliverBillLogDO> dos = logQryExe.findOrderDeliverBillLogByParams(qry);
        List<OrderDeliverBillLogDTO> dtos = BeanUtils.copyList(dos, OrderDeliverBillLogDTO.class);
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        return Response.of(dtos);
    }
}
