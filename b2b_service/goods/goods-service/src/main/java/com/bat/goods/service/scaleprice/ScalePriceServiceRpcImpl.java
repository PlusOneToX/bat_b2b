package com.bat.goods.service.scaleprice;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.scaleprice.api.ScalePriceServiceRpc;
import com.bat.dubboapi.goods.scaleprice.dto.ScalePriceRpcDTO;
import com.bat.goods.service.scaleprice.executor.ScalePriceQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 20:43
 */
@DubboService
public class ScalePriceServiceRpcImpl implements ScalePriceServiceRpc {

    @Resource
    private ScalePriceQryExe qryExe;

    @Override
    public Response<ScalePriceRpcDTO> getScalePriceByScalePriceId(Integer scalePriceId) {
        ScalePriceRpcDTO rpcDTO = qryExe.getScalePriceByScalePriceId(scalePriceId);
        return Response.of(rpcDTO);
    }
}
