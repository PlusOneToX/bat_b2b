package com.bat.thirdparty.mongodb.convertor;

import java.util.ArrayList;
import java.util.List;

import com.bat.thirdparty.mongodb.dao.dataobject.OrderLogDO;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderLogDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/9 10:47
 */
public class LogConvertor {

    public static List<OrderLogDTO> toOrderLogDTOList(List<OrderLogDO> orderLogDOS) {
        List<OrderLogDTO> dtos = new ArrayList<>();
        orderLogDOS.forEach(orderLogDO -> {
            OrderLogDTO dto = new OrderLogDTO();
            BeanUtils.copyProperties(orderLogDO, dto);
            dtos.add(dto);
        });
        return dtos;
    }

}
