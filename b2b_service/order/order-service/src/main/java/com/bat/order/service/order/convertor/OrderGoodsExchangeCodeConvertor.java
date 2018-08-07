package com.bat.order.service.order.convertor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodePageMsgDTO;
import com.bat.order.api.exchange.dto.OrderGoodsExchangeCodeListDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderGoodsExchangeCodeDTO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/9 17:16
 */
public class OrderGoodsExchangeCodeConvertor {
    public static OrderGoodsExchangeCodeDTO toOrderGoodsExchangeCodeDTO(OrderGoodsExchangeCodeDO exchangeCodeDO) {
        if (exchangeCodeDO != null) {
            OrderGoodsExchangeCodeDTO dto = new OrderGoodsExchangeCodeDTO();
            BeanUtils.copyProperties(exchangeCodeDO, dto);
            return dto;
        }
        return null;
    }

    public static void setExchangeCodeMsg(List<ExchangeCodePageMsgDTO> codePageMsgDTOList,
        List<OrderGoodsExchangeCodeListDTO> listDTOS) {
        if (codePageMsgDTOList == null || codePageMsgDTOList.size() == 0) {
            return;
        }
        Map<String, ExchangeCodePageMsgDTO> codeB2BOrderDTORpcQryMap = codePageMsgDTOList.stream().collect(
            Collectors.toMap(ExchangeCodePageMsgDTO::getSecretCode, exchangeCodePageMsgDTO -> exchangeCodePageMsgDTO));
        listDTOS.stream().forEach(orderGoodsExchangeCodeListDTO -> {
            ExchangeCodePageMsgDTO exchangeCodePageMsgDTO =
                codeB2BOrderDTORpcQryMap.get(orderGoodsExchangeCodeListDTO.getSecretCode());
            orderGoodsExchangeCodeListDTO.setBoxCode(exchangeCodePageMsgDTO.getBoxCode());
            orderGoodsExchangeCodeListDTO.setCodeName(exchangeCodePageMsgDTO.getCodeName());
            orderGoodsExchangeCodeListDTO.setExchangeFactoryId(exchangeCodePageMsgDTO.getExchangeFactoryId());
            orderGoodsExchangeCodeListDTO.setPlainCode(exchangeCodePageMsgDTO.getPlainCode());
        });
    }
}
