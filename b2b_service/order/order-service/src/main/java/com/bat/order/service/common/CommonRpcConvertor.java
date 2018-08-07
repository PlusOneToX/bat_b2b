package com.bat.order.service.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;

/**
 * @date: 2018/6/28 12:12
 */
public class CommonRpcConvertor {

    public static List<GoodsItemRpcQry> toGoodsItemRpcQryList(List<OrderGoodsDO> orderGoodsDOS) {
        List<GoodsItemRpcQry> rpcQryList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
            orderGoodsDOS.forEach(orderGoodsDO -> {
                GoodsItemRpcQry rpcQry = new GoodsItemRpcQry();
                rpcQry.setGoodsId(orderGoodsDO.getGoodsId());
                rpcQry.setItemId(orderGoodsDO.getItemId());
                rpcQryList.add(rpcQry);
            });
        }
        return rpcQryList.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsItemRpcQry::getItemId))),
                ArrayList::new));
    }
}
