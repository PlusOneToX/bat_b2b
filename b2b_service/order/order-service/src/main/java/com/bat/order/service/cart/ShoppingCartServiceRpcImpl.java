package com.bat.order.service.cart;

import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.cart.executor.ShoppingCartRpcExe;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.ShoppingCartServiceRpc;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/25 14:12
 */
@DubboService
public class ShoppingCartServiceRpcImpl implements ShoppingCartServiceRpc {

    @Resource
    private ShoppingCartRpcExe rpcExe;

    @Override
    public Response changeGoodsOpenFlag(List<Integer> goodsIds, Short openFlag) {
        return rpcExe.changeGoodsOpenFlag(goodsIds, openFlag);
    }

    @Override
    public Response changeGoodsOpenFlagByItemIds(List<Integer> itemIds, Short openFlag) {
        return rpcExe.changeGoodsOpenFlagByItemIds(itemIds, openFlag);
    }
}
