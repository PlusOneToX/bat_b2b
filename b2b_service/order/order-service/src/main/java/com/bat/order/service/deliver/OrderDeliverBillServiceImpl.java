package com.bat.order.service.deliver;

import java.util.*;

import com.bat.order.service.deliver.convertor.OrderDeliverBillConvertor;
import com.bat.order.service.deliver.executor.OrderDeliverBillCmdExe;
import com.bat.order.service.deliver.executor.OrderDeliverBillQryExe;
import com.bat.order.service.deliver.validator.OrderDeliverValidator;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.common.constant.OrderDeliverConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;
import com.bat.order.api.common.response.ResponseBaseBean;
import com.bat.order.api.deliver.OrderDeliverBillServiceI;
import com.bat.order.api.deliver.OrderDeliveryServiceI;
import com.bat.order.api.deliver.dto.OrderDeliverBillResp;
import com.bat.order.api.deliver.dto.OrderGoodsDeliverBean;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillDetailDTO;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillListDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDTO;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDeliverBillListQry;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.dao.deliver.OrderDeliverBillDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.deliver.executor.OrderDeliverBillDetailCmdExe;
import com.bat.order.service.deliver.executor.OrderDeliveryTraceQry;

@Service
@Slf4j
public class OrderDeliverBillServiceImpl implements OrderDeliverBillServiceI {

    @Autowired
    private OrderDeliverBillCmdExe orderDeliverBillCmdExe;

    @Autowired
    private OrderDeliverBillQryExe orderDeliverBillQryExe;

    @Autowired
    private OrderDeliveryServiceI orderDeliveryServiceI;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WarehouseServiceRpc warehouseServiceRpc;

    @Autowired
    private OrderDeliverBillDetailCmdExe orderDeliverBillDetailCmdExe;

    @Autowired
    private OrderDeliverBillDOMapper orderDeliverBillDOMapper;

    @Autowired
    private OrderDeliveryTraceQry orderDeliveryTraceQry;

    @Autowired
    private MessageSendService messageSendService;

    /**
     * 根据订单id查询订单配送流水列表
     * 
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDeliverBillDO> listByOrderId(Integer orderId) {
        return orderDeliverBillQryExe.listByOrderId(orderId);
    }

    @Override
    @Transactional
    public ResponseBaseBean createBill(List<OrderGoodsDeliverBean> deliverOrderDetailList, Short deliverStatus,
        Short logisticsStatus, String logisticsName, String expressNo, OrderInfoDO orderInfoDO, Integer distributionId,
        Short orderDeliveryType, OrderDeliveryDO orderDeliveryDO, String erpId, String deliveryErpNo, Date deliveryTime,
        Boolean diyFlag, Short push, String distributionCode) {
        ResponseBaseBean responseBaseBean = OrderDeliverValidator.validOrderDeliverBill(orderDeliveryType, orderInfoDO);
        if (responseBaseBean.getCode() != 0) {
            return responseBaseBean;
        }
        OrderDeliverBillDO orderDeliverBillDO = null;
        OrderDeliverBillResp response = new OrderDeliverBillResp();
        // 销售单出库
        if (orderDeliveryDO == null) {
            orderDeliveryDO = orderDeliveryServiceI.getByOrderId(orderInfoDO.getId());
        }
        orderDeliverBillDO = new OrderDeliverBillDO();

        if (distributionId != null) {
            // 柔性在前面已查询这两个参数
            orderDeliverBillDO.setDistributionId(distributionId);
            orderDeliverBillDO.setDistributionName(logisticsName);
        } else {
            // 标品订单
            LogisticsRpcQry qry = new LogisticsRpcQry();
            qry.setLogisticsErpId(erpId);
            com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsResponse =
                systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry);
            List<LogisticsRpcDTO> logisticsRpcDTOList;
            if (logisticsResponse == null || !logisticsResponse.isSuccess()) {
                return ResponseBaseBean.responseBean(10021, "访问系统服务异常");
            }
            distributionId = orderDeliveryDO.getDistributionId();
            logisticsName = orderDeliveryDO.getDistributionName();
            logisticsRpcDTOList = logisticsResponse.getData();
            if (logisticsRpcDTOList == null || logisticsRpcDTOList.size() == 0) {
                LogisticsRpcQry qry1 = new LogisticsRpcQry();
                qry1.setName(erpId);
                logisticsResponse = systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry1);
                if (logisticsResponse == null || !logisticsResponse.isSuccess()) {
                    return ResponseBaseBean.responseBean(10021, "访问系统服务异常");
                }
                logisticsRpcDTOList = logisticsResponse.getData();
            }

            if (logisticsRpcDTOList != null && logisticsRpcDTOList.size() > 0) {
                boolean b = false;
                for (LogisticsRpcDTO logisticsRpcDTO : logisticsRpcDTOList) {
                    if (logisticsRpcDTO.getId() == distributionId) {
                        orderDeliverBillDO.setDistributionName(logisticsRpcDTO.getName());
                        orderDeliverBillDO.setDistributionId(logisticsRpcDTO.getId());
                        distributionCode = logisticsRpcDTO.getLogisticsKdnCode();
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    orderDeliverBillDO.setDistributionName(logisticsRpcDTOList.get(0).getName());
                    orderDeliverBillDO.setDistributionId(logisticsRpcDTOList.get(0).getId());
                    distributionCode = logisticsRpcDTOList.get(0).getLogisticsKdnCode();
                }
            } else {
                // 找不到就取下单时的物流信息
                orderDeliverBillDO.setDistributionName(logisticsName);
                orderDeliverBillDO.setDistributionId(distributionId);
            }
        }
        // Logger.info("addOrderDeliverGoods: status----" +status);
        if ((deliverStatus != null)) {
            orderDeliverBillDO.setDeliverStatus(deliverStatus);
        }

        orderDeliverBillDO.setOrderId(orderInfoDO.getId());

        if (StringUtils.isNotBlank(deliveryErpNo)) {
            orderDeliverBillDO.setDeliverErpNo(deliveryErpNo);
        }
        if (StringUtils.isNotBlank(expressNo)) {
            orderDeliverBillDO.setLogisticsNo(expressNo);
        }
        orderDeliverBillDO.setDeliverTime(new Date());
        orderDeliverBillDO.setDeliverTime(deliveryTime == null ? new Date() : deliveryTime);
        orderDeliverBillDO.setCreateTime(new Date());
        orderDeliverBillDO.setUpdateTime(new Date());
        orderDeliverBillDO.setLogisticsStatus(String.valueOf(logisticsStatus));
        if (diyFlag) {
            // 柔性订单
            // orderDeliverBillDO.setPush(OrderDeliverConstant.);
        }
        orderDeliverBillDO.setDistributionCode(distributionCode);
        orderDeliverBillDO.setPush(push);
        orderDeliverBillCmdExe.create(orderDeliverBillDO);
        if (orderDeliverBillDO.getDeliverStatus() != null
                && orderDeliverBillDO.getDeliverStatus() == OrderDeliverConstant.ORDER_DELIVER_STATUS_CONFIRMED.shortValue()) {
            messageSendService.orderDeliverMsg(orderDeliverBillDO.getId());
        }
        List<OrderDeliverBillDetailDO> billDetailDOList = new ArrayList<>();
        // 定义仓库的map
        Map<String, WarehouseDTORpcQry> warehouseDTORpcQryMap = new HashMap<>();
        if (deliverOrderDetailList != null && deliverOrderDetailList.size() > 0) {
            for (OrderGoodsDeliverBean deliverBean : deliverOrderDetailList) {
                // 后面ERP需要回传这个行序号
                OrderGoodsDO orderGoodsDO = deliverBean.getOrderGoodsDO();
                if (orderGoodsDO == null) {
                    orderGoodsDO = orderGoodsServiceI.findByOrderIdAndItemIdAndSerialNumber(orderInfoDO.getId(),
                            deliverBean.getItemId(), 10);
                }
                OrderDeliverBillDetailDO billDetailDO = new OrderDeliverBillDetailDO();
                billDetailDO.setCount(deliverBean.getCount());
                billDetailDO.setOrderDeliverBillId(orderDeliverBillDO.getId());
                billDetailDO.setGoodsId(orderGoodsDO.getGoodsId());
                billDetailDO.setGoodsNo(orderGoodsDO.getGoodsNo());
                billDetailDO.setItemId(orderGoodsDO.getItemId());
                billDetailDO.setGoodsName(orderGoodsDO.getGoodsName());
                billDetailDO.setItemCode(orderGoodsDO.getItemCode());
                billDetailDO.setItemName(orderGoodsDO.getItemName());
                billDetailDO.setOrderId(orderGoodsDO.getOrderId());
                billDetailDO.setOrderGoodsId(orderGoodsDO.getId());
                billDetailDO.setSerialNumber(orderGoodsDO.getSerialNumber());
                billDetailDO.setCreateTime(new Date());
                billDetailDO.setUpdateTime(new Date());
                billDetailDO.setBarCode(orderGoodsDO.getBarCode());
                if (StringUtils.isNotBlank(deliverBean.getWarehouseNo())) {
                    WarehouseDTORpcQry warehouseDTORpcQry = warehouseDTORpcQryMap.get(deliverBean.getWarehouseNo());
                    if (warehouseDTORpcQry == null) {
                        Response<WarehouseDTORpcQry> warehouseDTORpcQryResponse = null;
                        try {
                            warehouseDTORpcQryResponse =
                                    warehouseServiceRpc.getByIdOrWarehouseNo(null, deliverBean.getWarehouseNo());
                            warehouseDTORpcQry = warehouseDTORpcQryResponse.getData();
                            if (warehouseDTORpcQry != null) {
                                warehouseDTORpcQryMap.put(deliverBean.getWarehouseNo(), warehouseDTORpcQry);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    if (warehouseDTORpcQry != null) {
                        billDetailDO.setWarehouseId(warehouseDTORpcQry.getId());
                        billDetailDO.setWarehouseName(warehouseDTORpcQry.getName());
                    } else {
                        // B2B找不到这个仓库
                        billDetailDO.setWarehouseName(deliverBean.getWarehouseNo());
                    }

                } else if (diyFlag) {
                    billDetailDO.setWarehouseName("工厂配送");
                }

                orderDeliverBillDetailCmdExe.create(billDetailDO);
                billDetailDOList.add(billDetailDO);
            }
        }
        response.setOrderDeliverBillDO(orderDeliverBillDO);
        response.setDeliverBillDetailDOList(billDetailDOList);
        responseBaseBean.setData(response);
        messageSendService.orderDeliverBillLogPackage(orderDeliverBillDO.getId(), orderDeliverBillDO.getOrderId(),
            "发货单创建", "创建成功", JSONObject.toJSONString(orderDeliverBillDO));
        return responseBaseBean;
    }

    @Override
    public void update(OrderDeliverBillDO orderDeliverBillDO) {
        orderDeliverBillCmdExe.update(orderDeliverBillDO);
        messageSendService.orderDeliverBillLogPackage(orderDeliverBillDO.getId(), orderDeliverBillDO.getOrderId(),
            "发货单更新", "更新成功", JSONObject.toJSONString(orderDeliverBillDO));
    }

    @Override
    public PageInfo<AdminOrderDeliverBillListDTO> listOrderDeliverBillByParam(AdminOrderDeliverBillListQry qry) {
        return orderDeliverBillQryExe.listOrderDeliverBillByParam(qry);
    }

    @Override
    public AdminOrderDeliverBillDetailDTO getDetail(Integer id) {
        return orderDeliverBillQryExe.getDetail(id);
    }

    @Override
    public OrderDeliverDetailDTO logisticsView(Integer orderDeliverBillId) {
        // 发货单信息
        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillDOMapper.selectByPrimaryKey(orderDeliverBillId);
        OrderDeliverDetailDTO orderDeliverDetailDTO = new OrderDeliverDetailDTO();
        OrderDeliverBillDTO orderDeliverBillDTO = OrderDeliverBillConvertor.toOrderDeliverBillDTO(orderDeliverBillDO);
        orderDeliverDetailDTO.setOrderDeliverBill(orderDeliverBillDTO);
        if (orderDeliverBillDTO != null) {
            orderDeliverDetailDTO
                .setOrderDeliveryTrace(orderDeliveryTraceQry.getByOrderDeliverBillId(orderDeliverBillDTO.getId()));
        }
        return orderDeliverDetailDTO;
    }

    @Override
    public List<OrderDeliverBillDO> listByDeliverErpNo(String deliverOrderNo) {
        return orderDeliverBillQryExe.listByDeliverErpNo(deliverOrderNo);
    }

    /**
     * 根据id列表批量删除
     * 
     * @param idList
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<Integer> idList) {
        orderDeliverBillCmdExe.batchDelete(idList);
    }

    @Override
    public OrderDeliverBillDO getById(Integer id) {
        return orderDeliverBillQryExe.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdate(List<OrderDeliverBillDO> orderDeliverBillDOList) {
        orderDeliverBillCmdExe.batchUpdate(orderDeliverBillDOList);
    }

    @Override
    public List<Integer> getNotErpOrderDeliverBillIds(Date startTime) {
        return orderDeliverBillQryExe.getNotErpOrderDeliverBillIds(startTime);
    }
}
