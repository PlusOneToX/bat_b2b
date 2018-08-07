package com.bat.goods.service.message;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.manager.mq.dto.CommonLogDTO;
import com.bat.goods.manager.mq.service.SenderService;
import org.springframework.stereotype.Component;

import com.bat.goods.api.message.MessageSendServiceI;

/**
 * 沙漠
 */
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;

    @Override
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
            operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

    /**
     * 发送全量商品价格同步
     */
    @Override
    public void syncAllGoodsPrice() {
        service.sendObject("syncAllGoodsPrice", "syncAllGoodsPrice", "syncAllGoodsPrice");
    }

    /**
     * 发送全量商品信息同步
     */
    @Override
    public void syncAllGoodsItem() {
        service.sendObject("syncAllGoodsItem", "syncAllGoodsItem", "syncAllGoodsItem");
    }

    /**
     * 发送全量商品信息同步
     */
    @Override
    public void syncAllGoodsItemBox() {
        service.sendObject("syncAllGoodsItemBox", "syncAllGoodsItemBox", "syncAllGoodsItemBox");
    }

    /**
     * 发送全量商品可视范围刷新同步
     */
    @Override
    public void syncAllGoodsScope() {
        service.sendObject("syncAllGoodsScope", "syncAllGoodsScope", "syncAllGoodsScope");
    }

    @Override
    public void syncBrandGoodsScope(Integer brandId) {
        service.sendObject(brandId, "syncBrandGoodsScope", "syncBrandGoodsScope-" + String.valueOf(brandId));
    }

    @Override
    public void goodsItemSaleStatus(List<Integer> itemIds) {
        service.sendObject(itemIds, "goodsItemSaleStatus", "goodsItemSaleStatus");
    }

}
