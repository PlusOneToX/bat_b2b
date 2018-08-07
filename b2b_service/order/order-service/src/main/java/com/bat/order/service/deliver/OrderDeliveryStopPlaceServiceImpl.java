package com.bat.order.service.deliver;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseSearchQry;
import com.bat.order.api.deliver.OrderDeliveryStopPlaceServiceI;
import com.bat.order.api.deliver.dto.OrderDeliverStopPlaceCmd;
import com.bat.order.api.deliver.dto.data.OrderDeliverStopPlaceDTO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverStopPlaceDO;
import com.bat.order.service.deliver.executor.OrderDeliverStopPlaceCmdExe;
import com.bat.order.service.deliver.executor.OrderDeliverStopPlaceQryExe;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDeliveryStopPlaceServiceImpl implements OrderDeliveryStopPlaceServiceI {

    @Autowired
    private OrderDeliverStopPlaceCmdExe orderDeliverStopPlaceCmdExe;

    @Autowired
    private OrderDeliverStopPlaceQryExe orderDeliverStopPlaceQryExe;

    @Override
    public void create(OrderDeliverStopPlaceCmd cmd) {
        OrderDeliverStopPlaceDO orderDeliverStopPlaceDO=new OrderDeliverStopPlaceDO();
        orderDeliverStopPlaceDO.setPlaceName(cmd.getPlaceName());
        orderDeliverStopPlaceDO.setCreateTime(new Date());
        orderDeliverStopPlaceCmdExe.create(orderDeliverStopPlaceDO);
    }

    @Override
    public void update(OrderDeliverStopPlaceCmd cmd) {
        OrderDeliverStopPlaceDO orderDeliverStopPlaceDO=new OrderDeliverStopPlaceDO();
        orderDeliverStopPlaceDO.setId(cmd.getId());
        orderDeliverStopPlaceDO.setPlaceName(cmd.getPlaceName());
        orderDeliverStopPlaceDO.setUpdateTime(new Date());
        orderDeliverStopPlaceCmdExe.updateByPrimaryKeySelective(orderDeliverStopPlaceDO);
    }

    @Override
    public void delete(Integer id) {
        orderDeliverStopPlaceCmdExe.delete(id);
    }

    @Override
    public boolean findMatch(String placeName) {
        int count = orderDeliverStopPlaceQryExe.findMatch(placeName);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<OrderDeliverStopPlaceDTO> selectList(BaseSearchQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<OrderDeliverStopPlaceDO> list = orderDeliverStopPlaceQryExe.selectList(qry);
        PageInfo pageInfo = new PageInfo(list);
        List<OrderDeliverStopPlaceDTO> orderDeliverStopPlaceDTOS = new ArrayList<>();
        for (OrderDeliverStopPlaceDO orderDeliverStopPlaceDO : list) {
            OrderDeliverStopPlaceDTO orderDeliverStopPlaceDTO = new OrderDeliverStopPlaceDTO();
            BeanUtils.copyProperties(orderDeliverStopPlaceDO, orderDeliverStopPlaceDTO);
            orderDeliverStopPlaceDTOS.add(orderDeliverStopPlaceDTO);
        }
        pageInfo.setList(orderDeliverStopPlaceDTOS);
        return pageInfo;
    }
}
