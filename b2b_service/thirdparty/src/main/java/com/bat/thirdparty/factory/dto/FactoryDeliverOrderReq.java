package com.bat.thirdparty.factory.dto;

import com.bat.thirdparty.factory.common.service.dto.OrderDeliveryCommonResp;
import com.bat.thirdparty.factory.duohong.service.dto.DuoHongDeliverOrderResp;
import com.bat.thirdparty.factory.haixing.service.dto.HaixingDeliverOrderResp;
import com.bat.thirdparty.factory.maike.service.dto.MaikeDeliverOrderResp;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 16:18
 */
@Data
public class FactoryDeliverOrderReq {
    private FactoryEnum factoryEnum;
    private DuoHongDeliverOrderResp duoHongResp;
    private MaikeDeliverOrderResp maikeResp;
    private HaixingDeliverOrderResp haixingResp;
    private OrderDeliveryCommonResp systemResp;
}
