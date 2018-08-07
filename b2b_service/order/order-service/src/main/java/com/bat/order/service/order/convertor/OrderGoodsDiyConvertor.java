package com.bat.order.service.order.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.order.dto.OrderGoodsDiyLabelCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderGoodsDiyDTO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyLabelDO;

public class OrderGoodsDiyConvertor {

    /**
     * 给order_goods_diy表设置order_id和order_goods_id值
     * 
     * @param diyDOList
     * @param orderGoodsDOList
     */
    public static void setOrderIdAndOrderGoodsId(List<OrderGoodsDiyDO> diyDOList, List<OrderGoodsDO> orderGoodsDOList) {
        for (int x = 0; x < diyDOList.size(); x++) {
            diyDOList.get(x).setOrderGoodsId(orderGoodsDOList.get(x).getId());
        }
    }

    public static OrderGoodsDiyDTO toOrderGoodsDiyDTO(OrderGoodsDiyDO orderGoodsDiyDO) {
        if (orderGoodsDiyDO != null) {
            OrderGoodsDiyDTO dto = new OrderGoodsDiyDTO();
            BeanUtils.copyProperties(orderGoodsDiyDO, dto);
            return dto;
        }
        return null;
    }

    public static List<OrderGoodsDiyLabelDO> toOrderGoodsDiyLabelDOList(List<OrderGoodsDiyLabelCmd> cmds) {
        List<OrderGoodsDiyLabelDO> labelDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                OrderGoodsDiyLabelDO labelDO = new OrderGoodsDiyLabelDO();
                BeanUtils.copyProperties(cmd, labelDO);
                labelDOS.add(labelDO);
            });
        }
        return labelDOS;
    }
}
