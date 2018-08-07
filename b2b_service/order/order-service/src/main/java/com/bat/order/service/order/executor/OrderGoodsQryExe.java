package com.bat.order.service.order.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.dto.common.OrderGoodsVmiDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderGoodsVmiListQry;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDetailDTO;
import com.bat.order.dao.order.dataobject.OrderGoodsVmiDO;

@Component
public class OrderGoodsQryExe {

    @Autowired
    private OrderGoodsDOMapper orderGoodsDOMapper;

    public List<OrderGoodsDO> listByOrderId(Integer orderId) {
        return orderGoodsDOMapper.listByOrderId(orderId);
    }

    public OrderGoodsDO findByOrderIdAndItemIdAndSerialNumber(Integer orderId, Integer itemId, Integer serialNumber) {
        return orderGoodsDOMapper.findByOrderIdAndItemIdAndSerialNumber(orderId, itemId, serialNumber);
    }

    public PageInfo<OrderGoodsVmiDTO> listVmiByParam(AdminOrderGoodsVmiListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<OrderGoodsVmiDO> orderGoodsVmiDOs = orderGoodsDOMapper.listVmiByParam(map);
        PageInfo pageInfo = new PageInfo(orderGoodsVmiDOs);
        List<OrderGoodsVmiDTO> list = new ArrayList<>();
        for (OrderGoodsVmiDO orderGoodsVmiDO : orderGoodsVmiDOs) {
            OrderGoodsVmiDTO dto = new OrderGoodsVmiDTO();
            BeanUtils.copyProperties(orderGoodsVmiDO, dto);
            list.add(dto);
        }
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<OrderGoodsDetailDTO> listOrderGoodsDetailByOrderId(Integer orderId) {
        return orderGoodsDOMapper.listOrderGoodsDetailByOrderId(orderId);

    }

    public OrderGoodsDO getById(Integer orderGoodsId) {
        return orderGoodsDOMapper.selectByPrimaryKey(orderGoodsId);
    }

    public List<OrderGoodsDO> listByOrderIdList(List<Integer> orderIdList) {
        return orderGoodsDOMapper.listByOrderIdList(orderIdList);
    }
}
