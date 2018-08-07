package com.bat.order.service.order.executor.distributor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.order.convertor.OrderInfoConvertor;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.promotion.dto.data.OrderPromotionRpcDTO;
import com.bat.order.api.order.dto.common.OrderPromotionDTO;
import com.bat.order.api.order.dto.distributor.OrderPromotionQry;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.order.executor.OrderRpcExe;

@Component
public class OrderInfoCmdExe {

    @Resource
    private OrderInfoDOMapper orderInfoDOMapper;
    @Resource
    private OrderRpcExe orderRpcExe;

    public void create(OrderInfoDO orderInfoDO) {
        orderInfoDOMapper.insert(orderInfoDO);
    }

    public void update(OrderInfoDO orderInfoDO) {
        orderInfoDOMapper.updateByPrimaryKey(orderInfoDO);
    }

    public void batchUpdate(List<OrderInfoDO> orderInfoDOList) {
        orderInfoDOMapper.batchUpdate(orderInfoDOList);
    }

    /**
     * 
     * @param qry
     * @return
     */
    public OrderPromotionDTO orderPromotion(OrderPromotionQry qry) {
        OrderPromotionRpcDTO rpcDTO = orderRpcExe.promotionByOrderPromotionIds(qry);
        return OrderInfoConvertor.toOrderPromotionDTO(rpcDTO);
    }
}
