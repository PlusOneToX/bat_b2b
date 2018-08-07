package com.bat.order.service.deliver.executor;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderDeliverBillLogDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillDetailDTO;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillListDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDeliverBillListQry;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.OrderDeliverBillDOMapper;
import com.bat.order.dao.deliver.OrderDeliverBillDetailDOMapper;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillListDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.order.executor.OrderRpcExe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderDeliverBillQryExe {

    @Autowired
    private OrderDeliverBillDOMapper orderDeliverBillDOMapper;

    @Autowired
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    @Autowired
    private OrderInfoDOMapper orderInfoDOMapper;

    @Autowired
    private OrderGoodsDOMapper orderGoodsDOMapper;

    @Autowired
    private OrderDistributorDataMapper orderDistributorDataMapper;

    @Autowired
    private OrderDistributorCostMapper orderDistributorCostMapper;

    @Autowired
    private OrderDeliverBillDetailDOMapper orderDeliverBillDetailDOMapper;

    @Autowired
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    @Autowired
    private OrderRpcExe orderRpcExe;

    public List<OrderDeliverBillDO> listByOrderId(Integer orderId) {
        return orderDeliverBillDOMapper.listByOrderId(orderId);
    }

    public PageInfo<AdminOrderDeliverBillListDTO> listOrderDeliverBillByParam(AdminOrderDeliverBillListQry qry) {
        if (qry.getPage() != null && qry.getSize() != null) {
            PageHelper.startPage(qry.getPage(), qry.getSize());
        }
        BeanMap map = BeanMap.create(qry);
        List<OrderDeliverBillListDO> orderDeliverBillList = orderDeliverBillDOMapper.listByOrderDeliverByParam(map);
        List<AdminOrderDeliverBillListDTO> dtos =
            BeanUtils.copyList(orderDeliverBillList, AdminOrderDeliverBillListDTO.class);
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        List<Integer> ids = dtos.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<OrderDeliverBillLogDTO> orderDeliverBillLogDTOS = new ArrayList<>();
        try {
            if (ids.size() > 0) {
                // 获取同步erp相关日志
                Response<List<OrderDeliverBillLogDTO>> data =
                    orderRpcExe.findOrderDeliverBillLogByIdsAndOperateType(ids, "同步erp失败");
                orderDeliverBillLogDTOS = data.getData();
            }
        } catch (Exception e) {
            log.error("获取发货单日志出现异常:{}", e);
        }
        Map<Integer, List<OrderDeliverBillLogDTO>> orderDeliverBillLogGroup = orderDeliverBillLogDTOS.stream()
            .collect(Collectors.groupingBy(OrderDeliverBillLogDTO::getOrderDeliverBillId));
        for (AdminOrderDeliverBillListDTO adminOrderDeliverBillListDTO : dtos) {
            List<OrderDeliverBillLogDTO> list = orderDeliverBillLogGroup.get(adminOrderDeliverBillListDTO.getId());
            if (list != null && list.size() > 0) {
                // 获取最新同步erp失败的数据
                for (OrderDeliverBillLogDTO DTO : list) {
                    if (DTO.getOperateDes().contains("失败")) {
                        adminOrderDeliverBillListDTO.setErpErrorLog(DTO.getOperateDes());
                        break;
                    }
                }
            }
        }
        PageInfo pageInfo = new PageInfo(orderDeliverBillList);
        pageInfo.setList(dtos);
        return pageInfo;
    }

    public AdminOrderDeliverBillDetailDTO getDetail(Integer id) {
        AdminOrderDeliverBillDetailDTO orderDeliverBillDetail = new AdminOrderDeliverBillDetailDTO();

        AdminOrderDeliverBillDetailDTO.OrderDeliverBill orderDeliverBill =
            new AdminOrderDeliverBillDetailDTO.OrderDeliverBill();
        AdminOrderDeliverBillDetailDTO.OrderDelivery orderDelivery = new AdminOrderDeliverBillDetailDTO.OrderDelivery();
        List<AdminOrderDeliverBillDetailDTO.OrderDeliverBillDetail> orderDeliverBillDetails = new ArrayList<>();

        Map<Integer, OrderGoodsDO> orderGoodsDOMap = new HashMap<>();
        Map<Integer, OrderGoodsDiyDO> orderGoodsDiyDOMap = new HashMap<>();

        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillDOMapper.selectByPrimaryKey(id);
        if (orderDeliverBillDO != null) {
            org.springframework.beans.BeanUtils.copyProperties(orderDeliverBillDO, orderDeliverBill);
            OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(orderDeliverBillDO.getOrderId());
            if (orderInfoDO != null) {
                orderDeliverBill.setOrderCreateTime(orderInfoDO.getCreateTime());
            }
            OrderDistributorCostDO orderDistributorCostDO =
                orderDistributorCostMapper.getErpOrderByOrderId(orderDeliverBillDO.getOrderId());
            if (orderDistributorCostDO != null) {
                orderDeliverBill.setDistributionAmount(orderDistributorCostDO.getDistributionAmount());
            }
            String distributorName = "";
            List<OrderDistributorDataDO> orderDistributorDataDOs =
                orderDistributorDataMapper.listByOrderId(orderDeliverBillDO.getOrderId());
            for (OrderDistributorDataDO orderDistributorDataDO : orderDistributorDataDOs) {
                if (orderDistributorDataDO.getErpFlag() != null && orderDistributorDataDO.getErpFlag() == 1) {
                    distributorName = orderDistributorDataDO.getDistributorName();
                }
            }
            orderDeliverBill.setDistributorName(distributorName);

            OrderDeliveryDO orderDeliveryDO = orderDeliveryDOMapper.getByOrderId(orderDeliverBillDO.getOrderId());
            if (orderDeliveryDO != null) {
                org.springframework.beans.BeanUtils.copyProperties(orderDeliveryDO, orderDelivery);
            }

            List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(orderDeliverBillDO.getOrderId());
            orderGoodsDOMap =
                orderGoodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, a -> a, (k1, k2) -> k1));

            List<OrderGoodsDiyDO> orderGoodsDiyDOS =
                orderGoodsDiyDOMapper.listByOrderId(orderDeliverBillDO.getOrderId());
            orderGoodsDiyDOMap = orderGoodsDiyDOS.stream()
                .collect(Collectors.toMap(OrderGoodsDiyDO::getOrderGoodsId, a -> a, (k1, k2) -> k1));
        }
        List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOs =
            orderDeliverBillDetailDOMapper.listByOrderDeliverBillId(id);
        for (OrderDeliverBillDetailDO orderDeliverBillDetailDO : orderDeliverBillDetailDOs) {
            AdminOrderDeliverBillDetailDTO.OrderDeliverBillDetail obj =
                new AdminOrderDeliverBillDetailDTO.OrderDeliverBillDetail();
            org.springframework.beans.BeanUtils.copyProperties(orderDeliverBillDetailDO, obj);
            orderDeliverBillDetails.add(obj);
        }
        for (AdminOrderDeliverBillDetailDTO.OrderDeliverBillDetail deliverBillDetail : orderDeliverBillDetails) {
            OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(deliverBillDetail.getOrderGoodsId());
            if (orderGoodsDO != null) {
                deliverBillDetail.setGoodsType(orderGoodsDO.getGoodsType());
                deliverBillDetail.setDiyType(orderGoodsDO.getDiyType());
                deliverBillDetail.setSpecsName(orderGoodsDO.getSpecsName());
                deliverBillDetail.setColorName(orderGoodsDO.getColorName());
                deliverBillDetail.setItemCount(orderGoodsDO.getItemCount());
                deliverBillDetail.setItemInCount(orderGoodsDO.getItemInCount());
                deliverBillDetail.setItemVmiCount(orderGoodsDO.getItemVmiCount());
                deliverBillDetail.setItemOnWayCount(orderGoodsDO.getItemOnWayCount());
                deliverBillDetail.setDeliverCount(orderGoodsDO.getDeliverCount());
                deliverBillDetail.setUnDeliverCount(orderGoodsDO.getUnDeliverCount());
            }
            OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOMap.get(deliverBillDetail.getOrderGoodsId());
            if (orderGoodsDiyDO != null) {
                deliverBillDetail.setBrandName(orderGoodsDiyDO.getBrandName());
                deliverBillDetail.setModelName(orderGoodsDiyDO.getModelName());
            }
        }

        orderDeliverBillDetail.setOrderDeliverBill(orderDeliverBill);
        orderDeliverBillDetail.setOrderDeliverBillDetails(orderDeliverBillDetails);
        orderDeliverBillDetail.setOrderDelivery(orderDelivery);
        return orderDeliverBillDetail;
    }

    public List<OrderDeliverBillDO> listByDeliverErpNo(String deliverErpNo) {
        return orderDeliverBillDOMapper.listByDeliverErpNo(deliverErpNo);
    }

    public OrderDeliverBillDO getById(Integer id) {
        return orderDeliverBillDOMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<OrderDeliverBillDO> dos) {
        orderDeliverBillDOMapper.batchUpdate(dos);

    }

    public List<OrderDeliverBillRpcDTO> findNOLogisticsNoDeliverGoods(Date date) {
        List<OrderDeliverBillDO> orderDeliverBillDOS = orderDeliverBillDOMapper.findNOLogisticsNoDeliverGoods(date);
        List<OrderDeliverBillRpcDTO> orderDeliverBillRpcDTOS = new ArrayList<>();
        for (OrderDeliverBillDO orderDeliverBillDO : orderDeliverBillDOS) {
            OrderDeliverBillRpcDTO orderDeliverBillRpcDTO = new OrderDeliverBillRpcDTO();
            org.springframework.beans.BeanUtils.copyProperties(orderDeliverBillDO, orderDeliverBillRpcDTO);
            orderDeliverBillRpcDTOS.add(orderDeliverBillRpcDTO);
        }
        return orderDeliverBillRpcDTOS;
    }

    public List<OrderDeliverBillDO> listByOrderIdList(List<Integer> orderIdList) {
        return orderDeliverBillDOMapper.listByOrderIdList(orderIdList);
    }

    /**
     * 获取为同步erp订单发货单id列表
     * 
     * @return
     */
    public List<Integer> getNotErpOrderDeliverBillIds(Date startTime) {
        List<Integer> ids = orderDeliverBillDOMapper.getNotErpOrderDeliverBillIds(startTime);
        return ids;
    }
}
