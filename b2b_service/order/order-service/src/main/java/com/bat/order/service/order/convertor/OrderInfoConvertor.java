package com.bat.order.service.order.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.order.dao.order.dataobject.*;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;
import com.bat.dubboapi.promotion.dto.data.OrderPromotionRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.api.SysDepartmentServiceRpc;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.order.api.common.constant.PayWayEnum;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.order.OrderTypeServiceI;
import com.bat.order.api.order.dto.common.*;
import com.bat.order.api.order.dto.export.OrderInfoExportQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderInfoListDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;
import com.bat.order.api.order.dto.orderquery.user.UserPCDistributorOrderInfoListDTO;
import com.bat.order.dao.order.co.OrderExcelCO;
import com.bat.order.service.order.constans.OrderExcelFieldEnum;
import com.bat.order.service.order.constans.OrderExcelSqlQry;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 17:03
 */
@Component
public class OrderInfoConvertor {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemUserServiceRpc systemUserServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SysDepartmentServiceRpc sysDepartmentServiceRpc;

    @Autowired
    private OrderTypeServiceI orderTypeServiceI;

    /*    @DubboReference(check = false,timeout = 10000,retries = 0)
    private DistributorExtendDataServiceRpc distributorExtendDataServiceRpc;*/

    public static List<AdminOrderInfoListDTO> toOrderInfoListDTO(List<AdminOrderInfoListDO> list) {
        return list.stream().map(orderInfoListOrderDO -> {
            AdminOrderInfoListDTO dto = new AdminOrderInfoListDTO();
            BeanUtils.copyProperties(orderInfoListOrderDO, dto);
            dto.setUserSourceName(dto.getUserSource());
            // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
            Short aShort = CommonUtil.convertAdminOrderStatus(orderInfoListOrderDO.getOrderStatus(),
                orderInfoListOrderDO.getPayStatus(), orderInfoListOrderDO.getDeliverStatus(),
                orderInfoListOrderDO.getPayWay());
            dto.setFrontOrderStatus(aShort);
            return dto;
        }).collect(Collectors.toList());
    }

    public static OrderInfoDTO toOrderInfoDTO(OrderInfoDO orderInfoDO) {
        if (orderInfoDO != null) {
            OrderInfoDTO dto = new OrderInfoDTO();
            BeanUtils.copyProperties(orderInfoDO, dto);
            dto.setOrderSourceName(dto.getOrderSource());
            return dto;
        }
        return null;
    }

    public static OrderInfoRpcQryDTO toOrderInfoRpcQryDTO(OrderInfoDO orderInfoDO) {
        if (orderInfoDO == null) {
            return null;
        }
        OrderInfoRpcQryDTO qryDTO = new OrderInfoRpcQryDTO();
        BeanUtils.copyProperties(orderInfoDO, qryDTO);
        return qryDTO;
    }

    public static List<UserPCDistributorOrderInfoListDTO>
        toUserPCDistributorOrderInfoListDTOList(List<UserDistributorOrderInfoListDO> userDistributorOrderInfoListDOS) {
        if (!CollectionUtils.isEmpty(userDistributorOrderInfoListDOS)) {
            return userDistributorOrderInfoListDOS.stream().map(userDistributorOrderInfoListDO -> {
                UserPCDistributorOrderInfoListDTO dto = new UserPCDistributorOrderInfoListDTO();
                BeanUtils.copyProperties(userDistributorOrderInfoListDO, dto);
                // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
                Short aShort = CommonUtil.convertUserOrderStatus(dto.getOrderStatus(), dto.getPayStatus(),
                    dto.getDeliverStatus(), dto.getPayWay());
                dto.setFrontOrderStatus(aShort);
                return dto;
            }).collect(Collectors.toList());

        }
        return new ArrayList<>();
    }

    public static List<UserPCDistributorOrderInfoListDTO>
        toUserPCCustomerOrderInfoListDTOList(List<UserCustomerOrderInfoListDO> userDistributorOrderInfoListDOS) {
        if (!CollectionUtils.isEmpty(userDistributorOrderInfoListDOS)) {
            return userDistributorOrderInfoListDOS.stream().map(userDistributorOrderInfoListDO -> {
                UserPCDistributorOrderInfoListDTO dto = new UserPCDistributorOrderInfoListDTO();
                BeanUtils.copyProperties(userDistributorOrderInfoListDO, dto);
                // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
                Short aShort = CommonUtil.convertUserOrderStatus(dto.getOrderStatus(), dto.getPayStatus(),
                    dto.getDeliverStatus(), dto.getPayWay());
                dto.setFrontOrderStatus(aShort);
                return dto;
            }).collect(Collectors.toList());

        }
        return new ArrayList<>();
    }

    public static OrderPromotionDTO toOrderPromotionDTO(OrderPromotionRpcDTO rpcDTO) {
        OrderPromotionDTO dto = new OrderPromotionDTO();
        List<PromotionDTO> promotions = new ArrayList<>();
        dto.setPromotions(promotions);
        List<GroupSeckillDTO> spellGroups = new ArrayList<>();
        dto.setSpellGroups(spellGroups);
        if (!CollectionUtils.isEmpty(rpcDTO.getPromotions())) {
            rpcDTO.getPromotions().forEach(promotionRpcDTO -> {
                PromotionDTO promotionDTO = new PromotionDTO();
                BeanUtils.copyProperties(promotionRpcDTO, promotionDTO);
                promotions.add(promotionDTO);
                List<PromotionRuleDTO> rules = new ArrayList<>();
                promotionDTO.setRules(rules);
                promotionRpcDTO.getRules().forEach(ruleRpcDTO -> {
                    PromotionRuleDTO ruleDTO = new PromotionRuleDTO();
                    BeanUtils.copyProperties(ruleRpcDTO, ruleDTO);
                    rules.add(ruleDTO);
                    List<PromotionRuleConditionDTO> conditions = new ArrayList<>();
                    ruleDTO.setConditions(conditions);
                    ruleRpcDTO.getConditions().forEach(conditionRpcDTO -> {
                        PromotionRuleConditionDTO conditionDTO = new PromotionRuleConditionDTO();
                        BeanUtils.copyProperties(conditionRpcDTO, conditionDTO);
                        conditions.add(conditionDTO);
                    });
                });
            });
        }
        if (!CollectionUtils.isEmpty(rpcDTO.getSpellGroups())) {
            rpcDTO.getSpellGroups().forEach(groupSeckillRpcDTO -> {
                GroupSeckillDTO groupSeckillDTO = new GroupSeckillDTO();
                BeanUtils.copyProperties(groupSeckillRpcDTO, groupSeckillDTO);
                spellGroups.add(groupSeckillDTO);
            });
        }
        return dto;
    }

    public static String querSql(OrderInfoExportQry orderInfoExportQry) {
        List<String> fieldList = orderInfoExportQry.getFieldList();
        if (fieldList == null || fieldList.size() == 0) {
            OrderExcelFieldEnum[] fieldEnums = OrderExcelFieldEnum.values();
            if (fieldList == null) {
                fieldList = new ArrayList<>();
            }
            for (int x = 0; x < fieldEnums.length; x++) {
                fieldList.add(String.valueOf(fieldEnums[x]));
            }
            orderInfoExportQry.setFieldList(fieldList);
        }
        OrderExcelSqlQry orderExcelSqlQry = new OrderExcelSqlQry();
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_info", "", false);
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_extend_data",
            "order_extend_data.order_id = order_info.id", false);
        // 这个表影响到order_goods_distributor_cost的查询
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_distributor_data",
            "order_distributor_data.order_id = order_info.id and order_distributor_data.erp_flag=1",
            ((orderInfoExportQry.getOrderStatusList() != null && orderInfoExportQry.getOrderStatusList().size() > 0)
                || orderInfoExportQry.getDistributorId() != null
                || OrderExcelFieldEnum.existByTable(fieldList, "order_goods_distributor_cost")
                || orderInfoExportQry.getActivityFlag() != null
                || (orderInfoExportQry.getPayStatusList() != null && orderInfoExportQry.getPayStatusList().size() > 0))
                    ? true : false);

        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_goods",
            "order_goods.order_id = order_info.id", true);
        // OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_goods",
        // "order_goods.order_id = order_info.id",
        // StringUtils.isNotBlank(orderInfoExportQry.getItemCode()) ? true : false);

        // 需要判断order_goods是否进行查询
        // 需要关联分销商id（所以必须要查询order_distributor_data）
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_goods_distributor_cost",
            orderExcelSqlQry.getEndSql().contains("order_goods")
                ? "order_goods_distributor_cost.order_goods_id = order_goods.id and "
                    + " order_distributor_data.`distributor_id` = order_goods_distributor_cost.`distributor_id`"
                : "order_goods_distributor_cost.order_id = order_info.id "
                    + " and order_distributor_data.`distributor_id` = order_goods_distributor_cost.`distributor_id` ",
            orderInfoExportQry.getActivityFlag() != null ? true : false);

        OrderExcelFieldEnum.queryTableSql(
            orderExcelSqlQry, fieldList, "order_goods_diy", orderExcelSqlQry.getEndSql().contains("order_goods ")
                ? "order_goods_diy.order_goods_id = order_goods.id" : "order_goods_diy.order_id = order_info.id",
            false);
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_delivery",
            "order_delivery.order_id = order_info.id", false);

        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_deliver_bill",
            "order_deliver_bill.order_id = order_info.id", false);
        // 处理order_goods_customer_cost
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_goods_customer_cost",
            orderExcelSqlQry.getEndSql().contains("order_goods ")
                ? "order_goods_customer_cost.order_goods_id = order_goods.id " :
                // 需要判断sql是否查询了order_goods_diy、如果查询了order_goods_diy而没有查询order_goods、直接去order_goods_id来匹配（只查order_id会出现重复明细）
                (orderExcelSqlQry.getEndSql().contains("order_goods_diy ")
                    ? "order_goods_customer_cost.`order_goods_id`  = order_goods_diy.`order_goods_id`" :
                    // 没有查询order_goods等订单明细才能用这个（否则会出现重复明细）
                    "order_goods_customer_cost.order_id = order_info.id "),
            false);
        OrderExcelFieldEnum.queryTableSql(orderExcelSqlQry, fieldList, "order_customer_data",
            "order_customer_data.order_id = order_info.id", false);
        return orderExcelSqlQry.getFullSql();
    }

    public void setDepartmentName(List<String> fieldList, List<OrderExcelCO> orderExcelCOList) {

        if (!fieldList.contains(String.valueOf(OrderExcelFieldEnum.DEPARTMENT_NAME))) {
            return;
        }
        // 查询所有的业务员
        Response<List<DepartmentRpcDTO>> departmentResponse = sysDepartmentServiceRpc.listByCondition(null);
        if (!departmentResponse.isSuccess()) {
            throw OrderException.buildException(departmentResponse.getErrCode(), departmentResponse.getErrMessage());
        }
        List<DepartmentRpcDTO> departmentRpcDTOList = departmentResponse.getData();
        Map<Integer, DepartmentRpcDTO> departmentRpcDTOMap = departmentRpcDTOList.stream()
            .collect(Collectors.toMap(DepartmentRpcDTO::getId, departmentRpcDTO -> departmentRpcDTO));
        Response<List<UserRpcDTO>> userResp = systemUserServiceRpc.listByCondition(null);
        if (!userResp.isSuccess()) {
            throw OrderException.buildException(userResp.getErrCode(), userResp.getErrMessage());
        }
        List<UserRpcDTO> userRpcDTOList = userResp.getData();
        Map<Integer, UserRpcDTO> userRpcDTOMap =
            userRpcDTOList.stream().collect(Collectors.toMap(UserRpcDTO::getId, userRpcDTO -> userRpcDTO));
        // 业务员id列表
        List<String> list = orderExcelCOList.stream().map(OrderExcelCO::getDepartmentName).collect(Collectors.toList());
        Map<Integer, String> map = new HashMap<>();
        list.stream().forEach(userId -> {
            Integer u = Integer.parseInt(userId);
            UserRpcDTO userRpcDTO = userRpcDTOMap.get(u);
            if (userRpcDTO != null) {
                DepartmentRpcDTO departmentRpcDTO = departmentRpcDTOMap.get(userRpcDTO.getDepartmentId());
                if (departmentRpcDTO != null) {
                    map.put(u, departmentRpcDTO.getDepartmentName());
                }
            }

        });
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            // 查询是将销售员id作为部门名称
            if (StringUtils.isNotBlank(orderExcelCO.getDepartmentName())
                && map.containsKey(Integer.parseInt(orderExcelCO.getDepartmentName()))) {
                orderExcelCO.setDepartmentName(map.get(Integer.parseInt(orderExcelCO.getDepartmentName())));
            }
        });
    }

    public void setOrderStatusName(List<String> fieldList, List<OrderExcelCO> orderExcelCOList) {
        if (!fieldList.contains(String.valueOf(OrderExcelFieldEnum.ORDER_STATUS))) {
            return;
        }
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            if (StringUtils.isNotBlank(orderExcelCO.getOrderStatus())) {
                Short orderSource = Short.parseShort(orderExcelCO.getOrderStatus());
                if (orderSource - OrderInfoConstant.ORDER_STATUS_UN_CONFIRM == 0) {
                    orderExcelCO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_UN_CONFIRM_NAME);
                }
                if (orderSource - OrderInfoConstant.ORDER_STATUS_CONFIRMED == 0) {
                    orderExcelCO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_CONFIRMED_NAME);
                }
                if (orderSource - OrderInfoConstant.ORDER_STATUS_REJECTED == 0) {
                    orderExcelCO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_REJECTED_NAME);
                }
                if (orderSource - OrderInfoConstant.ORDER_STATUS_CANCEL == 0) {
                    orderExcelCO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_CANCEL_NAME);
                }
                if (orderSource - OrderInfoConstant.ORDER_STATUS_COMPLETED == 0) {
                    orderExcelCO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_COMPLETED_NAME);
                }
            }
        });
    }

    public void setPayStatusName(List<String> fieldList, List<OrderExcelCO> orderExcelCOList) {
        if (!fieldList.contains(String.valueOf(OrderExcelFieldEnum.PAY_STATUS))) {
            return;
        }
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            if (StringUtils.isNotBlank(orderExcelCO.getPayStatus())) {
                Short payStatus = Short.parseShort(orderExcelCO.getPayStatus());
                if (payStatus - OrderInfoConstant.ORDER_PAY_STATUS_UNPAY == 0) {
                    orderExcelCO.setPayStatus(OrderInfoConstant.ORDER_PAY_STATUS_UNPAY_NAME);
                }
                if (payStatus - OrderInfoConstant.ORDER_PAY_STATUS_PARTIAL_PAYMENT == 0) {
                    orderExcelCO.setPayStatus(OrderInfoConstant.ORDER_PAY_STATUS_PARTIAL_PAYMENT_NAME);
                }
                if (payStatus - OrderInfoConstant.ORDER_PAY_STATUS_PAID == 0) {
                    orderExcelCO.setPayStatus(OrderInfoConstant.ORDER_PAY_STATUS_PAID_NAME);
                }
                if (payStatus - OrderInfoConstant.ORDER_PAY_STATUS_REBATES == 0) {
                    orderExcelCO.setPayStatus(OrderInfoConstant.ORDER_PAY_STATUS_REBATES_NAME);
                }
                if (payStatus - OrderInfoConstant.ORDER_PAY_STATUS_REFUND_APPLY == 0) {
                    orderExcelCO.setPayStatus(OrderInfoConstant.ORDER_PAY_STATUS_REFUND_APPLY_NAME);
                }
                if (payStatus - OrderInfoConstant.ORDER_PAY_STATUS_REFUNDED == 0) {
                    orderExcelCO.setPayStatus(OrderInfoConstant.ORDER_PAY_STATUS_REFUNDED_NAME);
                }
            }
        });
    }

    public void setPayWayName(List<String> fieldList, List<OrderExcelCO> orderExcelCOList) {
        if (!fieldList.contains(String.valueOf(OrderExcelFieldEnum.PAY_WAY))) {
            return;
        }
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            if (StringUtils.isNotBlank(orderExcelCO.getPayWay())) {
                Short payWay = Short.parseShort(orderExcelCO.getPayWay());
                PayWayEnum[] values = PayWayEnum.values();
                for (int x = 0; x < values.length; x++) {
                    PayWayEnum payWayEnum = values[x];
                    if (payWayEnum.getPayStatus() - payWay == 0) {
                        orderExcelCO.setPayWay(payWayEnum.getPayStatusStr());
                    }
                }
            }
        });
    }

    public void setDeliverStatusName(List<String> fieldList, List<OrderExcelCO> orderExcelCOList) {
        if (!fieldList.contains(String.valueOf(OrderExcelFieldEnum.DELIVER_STATUS))) {
            return;
        }
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            if (StringUtils.isNotBlank(orderExcelCO.getDeliverStatus())) {
                Short deliverStatus = Short.parseShort(orderExcelCO.getDeliverStatus());
                if (deliverStatus - OrderInfoConstant.ORDER_DELIVER_STATUS_UN_SHIPPED == 0) {
                    orderExcelCO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_UN_SHIPPED_NAME);
                }
                if (deliverStatus - OrderInfoConstant.ORDER_DELIVER_STATUS_OUTBOUNDING == 0) {
                    orderExcelCO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_OUTBOUNDING_NAME);
                }
                if (deliverStatus - OrderInfoConstant.ORDER_DELIVER_STATUS_SOME_SHIPPED == 0) {
                    orderExcelCO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_SOME_SHIPPED_NAME);
                }
                if (deliverStatus - OrderInfoConstant.ORDER_DELIVER_STATUS_SHIPPED == 0) {
                    orderExcelCO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_SHIPPED_NAME);
                }
            }
        });
    }

    public void setOrderTypeName(List<String> fieldList, List<OrderExcelCO> orderExcelCOList) {
        if (!fieldList.contains(String.valueOf(OrderExcelFieldEnum.ORDER_TYPE_ID))) {
            return;
        }
        List<OrderTypeDO> orderTypeDOList = orderTypeServiceI.listByCondition(null);
        if (orderTypeDOList == null || orderTypeDOList.size() == 0) {
            return;
        }
        Map<Integer, OrderTypeDO> orderTypeDOMap =
            orderTypeDOList.stream().collect(Collectors.toMap(OrderTypeDO::getId, orderTypeDO -> orderTypeDO));
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            if (StringUtils.isNotBlank(orderExcelCO.getOrderTypeId())) {
                Integer orderTypeId = Integer.parseInt(orderExcelCO.getOrderTypeId());
                OrderTypeDO orderTypeDO = orderTypeDOMap.get(orderTypeId);
                if (orderTypeDO != null) {
                    orderExcelCO.setOrderTypeId(orderTypeDO.getName());
                }
            }
        });
    }

    /**
     * 设置分销商ERPid
     * 
     * @param fieldList
     * @param orderExcelCOList
     */
    /*  public void setDistributorErpId(List<String> fieldList, List<OrderExcelCO> orderExcelCOList,Integer distributorId) {
        if(!fieldList.contains(String.valueOf(OrderExcelFieldEnum.DISTRIBUTOR_ERP_NO))){
            return;
        }
        List<DistributorExtendDataRpcDTO> distributorExtendDataRpcDTOList = new ArrayList<>();
        if(distributorId !=null){
            com.bat.dubboapi.distributor.common.Response<DistributorExtendDataRpcDTO> response = distributorExtendDataServiceRpc.getByDistributorId(distributorId);
            if(!response.isSuccess()){
                throw OrderException.buildException(response.getErrCode(),response.getErrMessage());
            }
            distributorExtendDataRpcDTOList.add(response.getData());
        }else {
            com.bat.dubboapi.distributor.common.Response<List<DistributorExtendDataRpcDTO>> response = distributorExtendDataServiceRpc.listByCondition();
            if(!response.isSuccess()){
                throw OrderException.buildException(response.getErrCode(),response.getErrMessage());
            }
            distributorExtendDataRpcDTOList.addAll(response.getData());
        }
        Map<Integer, DistributorExtendDataRpcDTO> dataRpcDTOMap = distributorExtendDataRpcDTOList.stream().collect(Collectors.toMap(DistributorExtendDataRpcDTO::getDistributorId, distributorExtendDataRpcDTO -> distributorExtendDataRpcDTO));
    
        orderExcelCOList.stream().forEach(orderExcelCO -> {
            if(StringUtils.isNotBlank(orderExcelCO.getDistributorErpId())){
                //查询回来的是分销商id
                Integer distrbutorId = Integer.parseInt(orderExcelCO.getDistributorErpId());
                DistributorExtendDataRpcDTO distributorExtendDataRpcDTO = dataRpcDTOMap.get(distrbutorId);
                if(distributorExtendDataRpcDTO !=null){
                    //存ERP编码
                    orderExcelCO.setDistributorErpId(distributorExtendDataRpcDTO.getErpNo());
                }
            }
        });
    }*/
}
