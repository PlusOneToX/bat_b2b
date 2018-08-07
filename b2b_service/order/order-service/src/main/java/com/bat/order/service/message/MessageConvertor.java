package com.bat.order.service.message;

import static com.bat.order.service.common.Constant.ERP_CANCEL_TYPE_1;
import static com.bat.order.service.common.Constant.ERP_CANCEL_TYPE_2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.order.mq.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderDeliverBillLogDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCommissionDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.data.dao.OrderDistributorDO;
import com.bat.order.service.common.enumtype.OrderStatus;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/7/1 11:55
 */
public class MessageConvertor {

    public static List<OrderTreeNodeDataDTO> toOrderTreeNodeDataDTO(OrderPayDTO cmd) {
        List<OrderTreeNodeDataDTO> dataDTOS = new ArrayList<>();
        cmd.getOrderIds().forEach(orderId -> {
            OrderTreeNodeDataDTO dataDTO = new OrderTreeNodeDataDTO();
            BeanUtils.copyProperties(cmd, dataDTO);
            dataDTO.setOrderId(orderId);
            dataDTOS.add(dataDTO);
        });
        return dataDTOS;
    }

    public static OrderVoucherDTO toOrderVoucherDTO(OrderDistributorCostDO orderDistributorCostDO,
        Short counterpartyType) {
        OrderVoucherDTO voucherDTO = new OrderVoucherDTO();
        OrderDistributorDataDO distributorDataDO = orderDistributorCostDO.getDistributorDataDO();
        voucherDTO.setDistributorId(distributorDataDO.getDistributorId());
        voucherDTO.setDistributorName(distributorDataDO.getDistributorName());
        voucherDTO.setCompanyName(distributorDataDO.getCompanyName());
        voucherDTO.setCounterpartyType(counterpartyType);
        voucherDTO.setPayWay(distributorDataDO.getPayWay());
        voucherDTO.setOutTradeNo(orderDistributorCostDO.getOutTradeNo());
        if (StringUtils.isBlank(distributorDataDO.getCurrencyType())) {
            voucherDTO.setCurrencyType("CNY");
        } else {
            voucherDTO.setCurrencyType(distributorDataDO.getCurrencyType());
        }
        return voucherDTO;
    }

    public static OrderVoucherDTO toOrderVoucherDTO(OrderDistributorCostDO orderDistributorCostDO,
        OrderDistributorDataDO distributorDataDO, Short counterpartyType) {
        OrderVoucherDTO voucherDTO = new OrderVoucherDTO();
        voucherDTO.setDistributorId(distributorDataDO.getDistributorId());
        voucherDTO.setDistributorName(distributorDataDO.getDistributorName());
        voucherDTO.setCompanyName(distributorDataDO.getCompanyName());
        voucherDTO.setCounterpartyType(counterpartyType);
        voucherDTO.setPayWay(distributorDataDO.getPayWay());
        voucherDTO.setOutTradeNo(orderDistributorCostDO.getOutTradeNo());
        if (StringUtils.isBlank(distributorDataDO.getCurrencyType())) {
            voucherDTO.setCurrencyType("CNY");
        } else {
            voucherDTO.setCurrencyType(distributorDataDO.getCurrencyType());
        }
        return voucherDTO;
    }

    public static OrderDeliverBillLogDTO toOrderDeliverBillLogDTO(Integer orderDeliverBillId, Integer orderId,
        String operateSource, Integer operateId, String operator, String operateType, String operateDes,
        String operateData, Date time) {
        OrderDeliverBillLogDTO orderDeliverBillLogDTO = new OrderDeliverBillLogDTO();
        orderDeliverBillLogDTO.setOrderDeliverBillId(orderDeliverBillId);
        orderDeliverBillLogDTO.setOrderId(orderId);
        orderDeliverBillLogDTO.setOperateSource(operateSource);
        orderDeliverBillLogDTO.setOperateId(operateId);
        orderDeliverBillLogDTO.setOperator(operator);
        orderDeliverBillLogDTO.setOperateType(operateType);
        orderDeliverBillLogDTO.setOperateDes(operateDes);
        orderDeliverBillLogDTO.setOperateData(operateData);
        orderDeliverBillLogDTO.setOperateTime(time);
        return orderDeliverBillLogDTO;
    }

    public static OrderCommissionDTO toOrderCommissionDTO(OrderDistributorCommissionDO commissionDO) {
        OrderCommissionDTO dto = new OrderCommissionDTO();
        BeanUtils.copyProperties(commissionDO, dto);
        dto.setDistributorId(commissionDO.getDistributorAncestorId());
        return dto;
    }

    /**
     * 订单反锁库存消息（锁库之后、处理业务失败、解除库存锁定）
     * 
     * @param cmds
     * @return
     */
    public static List<OrderUnStockLockDTO> toOrderUnStockLockDTOList(List<ItemStockLockDTO> cmds) {
        List<OrderUnStockLockDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                OrderUnStockLockDTO dto = new OrderUnStockLockDTO();
                dtos.add(dto);
                dto.setItemId(cmd.getItemId());
                if (cmd.getVmiLock() != null) {
                    VminStockLockDTO vmiLock = new VminStockLockDTO();
                    BeanUtils.copyProperties(cmd.getVmiLock(), vmiLock);
                    dto.setVmiLock(vmiLock);
                }
                if (!CollectionUtils.isEmpty(cmd.getOnWayLockDTOList())) {
                    List<WarehouseInStockLockDTO> onWayLockDTOList = new ArrayList<>();
                    cmd.getOnWayLockDTOList().forEach(onWayStockLockDTO -> {
                        WarehouseInStockLockDTO lockDTO = new WarehouseInStockLockDTO();
                        BeanUtils.copyProperties(onWayStockLockDTO, lockDTO);
                        onWayLockDTOList.add(lockDTO);
                    });
                    dto.setOnWayLockDTOList(onWayLockDTOList);
                }
                if (!CollectionUtils.isEmpty(cmd.getInStockLockDTOList())) {
                    List<WarehouseInStockLockDTO> inStockLockDTOList = new ArrayList<>();
                    cmd.getInStockLockDTOList().forEach(inStockLockDTO -> {
                        WarehouseInStockLockDTO lockDTO = new WarehouseInStockLockDTO();
                        BeanUtils.copyProperties(inStockLockDTO, lockDTO);
                        inStockLockDTOList.add(lockDTO);
                    });
                    dto.setInStockLockDTOList(inStockLockDTOList);
                }
            });
        }
        return dtos;
    }

    public static CouponStatusDTO toCouponStatusDTO(List<String> couponNos, Short couponStatus) {
        CouponStatusDTO dto = new CouponStatusDTO();
        dto.setCouponNos(couponNos);
        dto.setCouponStatus(couponStatus);
        return dto;
    }

    public static List<OrderLogDTO> toOrderLogDTOList(List<Integer> orderIds, String operateSource, Integer operateId,
                                                      String operator, String operateType, String operateDes, String operateData, Date time) {
        List<OrderLogDTO> dtos = new ArrayList<>();
        orderIds.forEach(orderId -> {
            OrderLogDTO orderLogDTO = new OrderLogDTO();
            orderLogDTO.setOrderId(orderId);
            orderLogDTO.setOperateSource(operateSource);
            orderLogDTO.setOperateId(operateId);
            orderLogDTO.setOperator(operator);
            orderLogDTO.setOperateType(operateType);
            orderLogDTO.setOperateDes(operateDes);
            orderLogDTO.setOperateData(operateData);
            orderLogDTO.setOperateTime(time);
            dtos.add(orderLogDTO);
        });
        return dtos;
    }

    public static CommonLogDTO toCommonLogDTOList(String businessModule, String businessFunction, Integer businessId,
        String operateSource, Integer operateId, String operator, String operateType, String operateDes,
        String operateData, Date time) {
        if (StringUtils.isBlank(operator)) {
            operator = "系统";
        }
        CommonLogDTO dto = new CommonLogDTO();
        dto.setBusinessModule(businessModule);
        dto.setBusinessFunction(businessFunction);
        dto.setBusinessId(businessId);
        dto.setOperateSource(operateSource);
        dto.setOperateId(operateId);
        dto.setOperator(operator);
        dto.setOperateType(operateType);
        dto.setOperateDes(operateDes);
        dto.setOperateData(operateData);
        dto.setOperateTime(time);
        return dto;
    }

    public static OrderCancelDTO toOrderCancelDTO(OrderDistributorDataDO erpDistributorData,
                                                  OrderExtendDataDO orderExtendDataDO) {
        OrderCancelDTO dto = new OrderCancelDTO();
        dto.setOrderId(erpDistributorData.getOrderId());
        if (erpDistributorData.getOrderStatus().equals(OrderStatus.PENDING.getValue())) {
            dto.setErpCancelType(ERP_CANCEL_TYPE_1);
        } else {
            dto.setErpCancelType(ERP_CANCEL_TYPE_2);
        }
        if (orderExtendDataDO != null) {
            dto.setOrderErpNo(orderExtendDataDO.getOrderErpNo());
        }
        return dto;
    }

    public static OrderLableDTO toOrderLableDTO(Integer distributorId, Integer orderId, List<OrderGoodsDiyDO> diyDOS) {
        OrderLableDTO dto = new OrderLableDTO();
        dto.setOrderId(orderId);
        List<OrderGoodsDiySimpleDTO> diySimpleDTOList = new ArrayList<>();
        dto.setDiySimpleDTOList(diySimpleDTOList);
        dto.setDistributorId(distributorId);
        diyDOS.forEach(diyDO -> {
            OrderGoodsDiySimpleDTO simpleDTO = new OrderGoodsDiySimpleDTO();
            BeanUtils.copyProperties(diyDO, simpleDTO);
            diySimpleDTOList.add(simpleDTO);
        });
        return dto;
    }

    public static List<GoodsSaleDTO> toGoodsSaleDTOList(List<OrderGoodsDO> goodsDOS, Short changeType) {
        List<GoodsSaleDTO> saleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsDOS)) {
            goodsDOS.forEach(goodsDO -> {
                if (goodsDO.getUnDeliverCount() != null && goodsDO.getUnDeliverCount() > 0) {
                    GoodsSaleDTO saleDTO = new GoodsSaleDTO();
                    saleDTO.setSaleNum(goodsDO.getUnDeliverCount());
                    saleDTO.setOrderGoodsId(goodsDO.getId());
                    saleDTO.setChangeType(changeType);
                    saleDTO.setItemId(goodsDO.getItemId());
                    saleDTO.setGoodsId(goodsDO.getGoodsId());
                    OrderGoodsDistributorCostDO distributorCostDO = goodsDO.getOrderGoodsDistributorCost();
                    if (distributorCostDO != null) {
                        saleDTO.setGoodsPromotionId(distributorCostDO.getGoodsPromotionId());
                        saleDTO.setOrderPromotionId(distributorCostDO.getOrderPromotionId());
                        saleDTO.setSpellGroupId(distributorCostDO.getSpellGroupId());
                    }
                    saleDTOS.add(saleDTO);
                }
            });
        }
        return saleDTOS;
    }

    public static RebateVoucherDTO toRebateVoucherDTO(List<OrderDistributorDO> orderDistributorDOList,
        List<Integer> rebateVoucherIds) {
        RebateVoucherDTO dto = new RebateVoucherDTO();
        dto.setRebateVoucherIds(rebateVoucherIds);
        List<RebateVoucherDTO.RebateVoucherItemDTO> collect =
            orderDistributorDOList.stream().filter(orderDistributorDO -> {
                BigDecimal rebateVoucherAmount = orderDistributorDO.getOrderCost().getRebateVoucherAmount();
                return rebateVoucherAmount != null && rebateVoucherAmount.compareTo(BigDecimal.ZERO) > 0;
            }).map(orderDistributorDO -> {
                RebateVoucherDTO.RebateVoucherItemDTO voucherItemDTO = new RebateVoucherDTO.RebateVoucherItemDTO();
                OrderInfoDO order = orderDistributorDO.getOrder();
                voucherItemDTO.setOrderId(order.getId());
                voucherItemDTO.setOrderNo(order.getOrderNo());
                voucherItemDTO.setRebateVoucherAmount(orderDistributorDO.getOrderCost().getRebateVoucherAmount());
                voucherItemDTO.setCreateTime(order.getCreateTime());
                return voucherItemDTO;
            }).collect(Collectors.toList());
        dto.setVoucherItems(collect);
        return dto;
    }
}
