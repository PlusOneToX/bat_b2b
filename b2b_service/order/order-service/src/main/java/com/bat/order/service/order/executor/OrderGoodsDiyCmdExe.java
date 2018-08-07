package com.bat.order.service.order.executor;

import java.util.List;

import com.bat.order.service.order.convertor.OrderGoodsDiyConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.order.dto.OrderGoodsDiyLabelCmd;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyLabelDO;

@Component
public class OrderGoodsDiyCmdExe {

    @Autowired
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    public void create(OrderGoodsDiyDO orderGoodsDiyDO) {
        orderGoodsDiyDOMapper.insert(orderGoodsDiyDO);
    }

    public void updateList(List<OrderGoodsDiyDO> updateList) {
        orderGoodsDiyDOMapper.updateList(updateList);
    }

    /**
     * 批量更新定制标签
     * 
     * @param cmds
     */
    public void orderGoodsDiyLabel(List<OrderGoodsDiyLabelCmd> cmds) {
        List<OrderGoodsDiyLabelDO> labelDOS = OrderGoodsDiyConvertor.toOrderGoodsDiyLabelDOList(cmds);
        if (!CollectionUtils.isEmpty(labelDOS)) {
            orderGoodsDiyDOMapper.updateListLabel(labelDOS);
        }
    }

    public void update(OrderGoodsDiyDO orderGoodsDiyDO) {
        orderGoodsDiyDOMapper.updateByPrimaryKey(orderGoodsDiyDO);
    }
}
