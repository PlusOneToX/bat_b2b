package com.bat.warehouse.manager.message;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.dubboapi.warehouse.stock.dto.ErpItemStockChangeCmd;
import com.bat.dubboapi.warehouse.stock.dto.GoodsMsgRpcDTO;
import com.bat.warehouse.api.inStock.dto.GoodsItemStockFlagCmd;
import com.bat.warehouse.api.message.MessageSendServiceI;
import com.bat.warehouse.mq.dto.CommonLogDTO;
import com.bat.warehouse.mq.dto.ErpItemStockChangeDTO;
import com.bat.warehouse.mq.dto.OrderErpNoLineDTO;
import com.bat.warehouse.mq.service.SenderService;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 9:07
 */
@Component
public class MessageSendService implements MessageSendServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendService.class);

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
     * 订单商品锁库记录列表解锁
     * 
     * @param orderGoodsStockUnLockList
     * @param erpBillNo
     */
    public void orderGoodsStockUnLock(List<OrderGoodsStockSimpleDTO> orderGoodsStockUnLockList, String erpBillNo) {
        LOGGER.info("发送消息、进行订单商品锁库解锁操作，解锁列表{},erp单号{}", JSON.toJSONString(orderGoodsStockUnLockList), erpBillNo);
        service.sendObject(orderGoodsStockUnLockList, "orderGoodsStockUnLock", "orderGoodsStockUnLock_" + erpBillNo);
    }

    /**
     * 处理货品是否缺货标记
     * 
     * @param goodsItemStockFlagCmd
     */
    public void dealwithGoodsUnderStockFlag(GoodsItemStockFlagCmd goodsItemStockFlagCmd) {
        long currentTimeMillis = System.currentTimeMillis();
        goodsItemStockFlagCmd.setTime(currentTimeMillis);
        service.sendObject(goodsItemStockFlagCmd, "dealwithGoodsUnderStockFlag",
            "dealwithGoodsUnderStockFlag_" + currentTimeMillis);
    }

    public void initGoodsStock(GoodsMsgRpcDTO goodsMsgRpcDTO) {
        LOGGER.info("发送消息、初始化库存{}", JSON.toJSONString(goodsMsgRpcDTO));
        service.sendObject(goodsMsgRpcDTO, "initGoodsStock", "initGoodsStock_" + goodsMsgRpcDTO.getGoodsId());
    }

    /**
     * 发送库存变更消息
     * 
     * @param stockBillDetailList
     */
    public void changeGoodsStockByErp(List<ErpItemStockChangeCmd> stockBillDetailList, String billNo,
        String stockBillType) {
        List<ErpItemStockChangeDTO> stockChangeDTOS =
            MessageConvertor.toErpItemStockChangeDTOList(stockBillDetailList, stockBillType);
        LOGGER.info("库存变更消息:{}", JSON.toJSONString(stockChangeDTOS));
        service.sendObject(stockChangeDTOS, "changeGoodsStock", "changeGoodsStock_" + billNo);
    }

    /**
     * 发送根据ERP订单号和行序号列表解锁B2B订单
     * 
     * @param orderErpNoLineDTOS
     */
    public void unLockByOrderErpNoLine(List<OrderErpNoLineDTO> orderErpNoLineDTOS, String billNo) {
        LOGGER.info("订单解锁:{}", JSON.toJSONString(orderErpNoLineDTOS));
        service.sendObject(orderErpNoLineDTOS, "unLockByOrderErpNoLine", "unLockByOrderErpNoLine_" + billNo);
    }

    /**
     * 根据货品Ids刷新货品是否有库存
     *
     * @param itemIds
     */
    public void freshItemUnderStockFlag(List<Integer> itemIds) {
        LOGGER.info("刷新货品是否有库存:{}", JSON.toJSONString(itemIds));
        service.sendObject(itemIds, "freshItemUnderStockFlag", "freshItemUnderStockFlag");
    }
}
