package com.bat.order.service.local;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.local.api.OrderDetailPictureLocalServiceRpc;
import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;
import com.bat.order.service.local.executor.OrderDetailPictureLocalCmdExe;

@DubboService
public class OrderDetailPictureLocalServiceRpcImpl implements OrderDetailPictureLocalServiceRpc {

    @Autowired
    private OrderDetailPictureLocalCmdExe orderDetailPictureLocalCmdExe;

    @Override
    public Response importData(String param) {
        JSONArray jsonArray = JSONArray.parseArray(param);
        List<OrderDetailPictureLocalDO> orderDetailPictureLocalDOList = new ArrayList<>();

        for (int x = 0; x < jsonArray.size(); x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            OrderDetailPictureLocalDO orderDetailPictureLocalDO =
                JSONObject.parseObject(jsonObject.toJSONString(), OrderDetailPictureLocalDO.class);
            orderDetailPictureLocalDO.setOrderGoodsDiyId(jsonObject.getInteger("orderDetailId"));
            orderDetailPictureLocalCmdExe.create(orderDetailPictureLocalDO);
        }
        // orderDetailPictureLocalCmdExe.batchCreate(orderDetailPictureLocalDOList);
        return Response.buildSuccess();
    }
}
