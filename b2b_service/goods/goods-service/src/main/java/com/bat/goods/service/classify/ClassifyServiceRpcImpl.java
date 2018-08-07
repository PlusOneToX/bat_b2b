package com.bat.goods.service.classify;

// package by domain, not by duty

import com.bat.goods.service.classify.executor.ClassifyRpcQryExe;
import com.bat.dubboapi.goods.classify.api.GoodsClassifyServiceRpc;
import com.bat.dubboapi.goods.classify.dto.data.ClassifyRpcDTO;
import com.bat.dubboapi.goods.common.Response;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService
public class ClassifyServiceRpcImpl implements GoodsClassifyServiceRpc {

    @Resource
    private ClassifyRpcQryExe classifyRpcQryExe;

    @Override
    public Response<List<ClassifyRpcDTO>> listByIds(List<Integer> ids) {
        List<ClassifyRpcDTO> classifyRpcDTOS = classifyRpcQryExe.listByIds(ids);
        return Response.of(classifyRpcDTOS);
    }
}