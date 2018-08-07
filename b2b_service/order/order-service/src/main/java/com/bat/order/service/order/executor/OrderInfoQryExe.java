package com.bat.order.service.order.executor;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.order.dao.cost.*;
import com.bat.order.dao.order.dataobject.*;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.utils.CommonUtil;
import com.bat.order.service.order.constans.DirectFlag;
import com.bat.order.service.order.convertor.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.platform.api.PlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.globalsetting.api.BaseSettingServiceRpc;
import com.bat.dubboapi.system.globalsetting.dto.BaseSettingRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.cost.dto.data.OrderGoodsDistributorCostDTO;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.orderquery.admin.*;
import com.bat.order.api.order.dto.orderquery.common.OrderCustomerDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderDistributorDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDetailGoodsDTO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.OrderGoodsExchangeCodeMapper;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.co.OrderExcelCO;
import com.bat.order.service.cost.convertor.OrderCostConvertor;
import com.bat.order.service.data.convertor.OrderDataConvertor;
import com.bat.order.service.order.constans.ErpFlag;
import com.bat.order.service.order.convertor.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderInfoQryExe {

    @Autowired
    private OrderInfoDOMapper orderInfoDOMapper;

    @Autowired
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    @Autowired
    private OrderExtendDataMapper orderExtendDataMapper;

    @Autowired
    private OrderDistributorDataMapper orderDistributorDataMapper;

    @Autowired
    private OrderDistributorCostMapper orderDistributorCostMapper;

    @Autowired
    private OrderGoodsDOMapper orderGoodsDOMapper;

    @Autowired
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    @Autowired
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostDOMapper;

    @Autowired
    private OrderInvoiceDOMapper orderInvoiceDOMapper;

    @Autowired
    private OrderGoodsExchangeCodeMapper orderGoodsExchangeCodeMapper;

    @Autowired
    private OrderCustomerDataMapper orderCustomerDataMapper;

    @Autowired
    private OrderCustomerCostMapper orderCustomerCostMapper;

    @Autowired
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostMapper;

    @DubboReference(check = false, timeout = 5000)
    private BaseSettingServiceRpc baseSettingServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private PlatformServiceRpc platformServiceRpc;

    public OrderInfoDO getById(Integer orderId) {
        return orderInfoDOMapper.selectByPrimaryKey(orderId);
    }

    /**
     * B2B订单列表
     *
     * @param qry
     * @return
     */
    public PageInfo<AdminOrderInfoListDTO> listDistributorOrderInfo(AdminDistributorOrderInfoListQry qry) {
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        Date startTime = qry.getStartTime();
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -6);
            startTime = calendar.getTime();
        }
        map.put("startTime", startTime);
        map.put("endTime", qry.getEndTime() == null ? new Date() : qry.getEndTime());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<AdminOrderInfoListDO> order = orderInfoDOMapper.listAdminDistributorOrderByParams(map);
        PageInfo pageInfo = new PageInfo(order);
        List<AdminOrderInfoListDTO> list = OrderInfoConvertor.toOrderInfoListDTO(pageInfo.getList());
        // 分销订单情况获取bat结算价（及一级分销商价）
        if (!CollectionUtils.isEmpty(list) && qry.getSearchType() == 2) {
            List<Integer> orderIds = list.stream().map(AdminOrderInfoListDTO::getId).collect(Collectors.toList());
            List<OrderDistributorCostDO> distributorCostDOS =
                orderDistributorCostMapper.listByOrderIdsAndTreeNode(orderIds, 1);
            if (!CollectionUtils.isEmpty(distributorCostDOS)) {
                Map<Integer, List<OrderDistributorCostDO>> orderDistributorCostDOSMap =
                    distributorCostDOS.stream().collect(Collectors.groupingBy(OrderDistributorCostDO::getOrderId));
                list.forEach(adminOrderInfoListDTO -> {
                    List<OrderDistributorCostDO> costDOS =
                        orderDistributorCostDOSMap.get(adminOrderInfoListDTO.getId());
                    if (!CollectionUtils.isEmpty(costDOS)) {
                        adminOrderInfoListDTO.setSettlementAmount(costDOS.get(0).getPayAmount());
                    }
                });
            }
        }
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 柔性订单列表
     *
     * @param qry
     * @return
     */
    public PageInfo<AdminOrderInfoListDTO> listCustomerDiyOrderInfo(AdminCustomerOrderInfoListQry qry) {
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        Date startTime = qry.getStartTime();
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -6);
            startTime = calendar.getTime();
        }
        map.put("startTime", startTime);
        map.put("endTime", qry.getEndTime() == null ? new Date() : qry.getEndTime());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<AdminOrderInfoListDO> order = orderInfoDOMapper.listAdminCustomerOrderByParams(map);
        PageInfo pageInfo = new PageInfo(order);
        List<AdminOrderInfoListDTO> list = OrderInfoConvertor.toOrderInfoListDTO(pageInfo.getList());
        // TODO 填充店铺 以及平台类型
        pageInfo.setList(fillOrderSourceName2(list));
        return pageInfo;
    }

    /**
     * 异常订单列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<AdminOrderInfoListDTO> listExceptionOrderInfo(AdminExceptionOrderInfoListQry qry) {
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        Date startTime = qry.getStartTime();
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -6);
            startTime = calendar.getTime();
        }
        map.put("startTime", startTime);
        map.put("endTime", qry.getEndTime() == null ? new Date() : qry.getEndTime());
        if (qry.getPage() != null && qry.getSize() != null) {
            PageHelper.startPage(qry.getPage(), qry.getSize());
        }
        // 判断是否查找长时间未发货的订单
        if (qry.getErrorType() != null && qry.getErrorType() == 3) {
            // 获取配置数据
            Response<BaseSettingRpcDTO> response = baseSettingServiceRpc.getBaseSettingByKey("not_delivery_unit");
            BaseSettingRpcDTO baseSettingRpcDTO = response.getData();
            // 默认取两天后
            int changeDayNum = 2;
            if (baseSettingRpcDTO != null && StringUtils.isNotBlank(baseSettingRpcDTO.getValue())) {
                changeDayNum = Integer.valueOf(baseSettingRpcDTO.getValue());
            }
            Date changeDate = getChangeDate(new Date(), -changeDayNum);
            map.put("noDeliverTime", changeDate);
        }
        List<AdminOrderInfoListDO> order = orderInfoDOMapper.listAdminExceptionOrderByParams(map);
        PageInfo pageInfo = new PageInfo(order);
        List<AdminOrderInfoListDTO> list = OrderInfoConvertor.toOrderInfoListDTO(pageInfo.getList());
        // TODO 填充店铺 以及平台类型
        pageInfo.setList(list);
        return pageInfo;
    }

    private Date getChangeDate(Date date, int changeDay) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 1表示明天,-1表示昨天
        calendar.add(Calendar.DATE, changeDay);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取分销订单详情
     *
     * http://localhost:8069/order/v1/web/admin/erpOrder/detail?id=17
     *
     * @param qry
     * @return
     */
    public AdminDistributorOrderInfoDetailDTO getDistributorOrderDetail(AdminOrderDetailQry qry) {
        AdminDistributorOrderInfoDetailDTO aDo = new AdminDistributorOrderInfoDetailDTO();
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(qry.getId());
        if (orderInfoDO != null) {
            aDo.setOrderInfo(OrderInfoConvertor.toOrderInfoDTO(orderInfoDO));
            // 增加订单来源
            fillOrderSourceName(aDo.getOrderInfo());
            aDo.setOrderDelivery(
                OrderDeliveryConvertor.toOrderDeliveryDTO(orderDeliveryDOMapper.getByOrderId(orderInfoDO.getId())));
            aDo.setOrderExtendData(
                OrderDataConvertor.toOrderExtendDataDTO(orderExtendDataMapper.getByOrderId(orderInfoDO.getId())));
            aDo.setOrderInvoice(
                OrderCostConvertor.toOrderInvoiceDTO(orderInvoiceDOMapper.getByOrderId(orderInfoDO.getId())));
            aDo.setOrderDistributors(getOrderDistributorDTOS(qry, aDo, orderInfoDO));
            if (aDo.getOrderDistributor() != null && aDo.getOrderDistributor().getOrderDistributorDataDTO() != null) {
                Integer distributorId = aDo.getOrderDistributor().getOrderDistributorDataDTO().getDistributorId();
                List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(orderInfoDO.getId());
                if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
                    // 商品所有等级分销商信息
                    List<OrderInfoDetailGoodsDTO> collect1 = getAllOrderInfoDetailGoodsDTOS(orderGoodsDOS);
                    aDo.setOrderInfoDetailGoodsList(collect1);
                    // 商品默认分销商信息
                    List<OrderInfoDetailGoodsDTO> collect2 = getOrderInfoDetailGoodsDTOS(distributorId, collect1);
                    aDo.setOrderInfoDetailGoods(collect2);
                }
            }
            // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
            Short aShort = CommonUtil.convertUserOrderStatus(
                aDo.getOrderDistributor().getOrderDistributorDataDTO().getOrderStatus(),
                aDo.getOrderDistributor().getOrderDistributorDataDTO().getPayStatus(),
                aDo.getOrderInfo().getDeliverStatus(),
                aDo.getOrderDistributor().getOrderDistributorDataDTO().getPayWay());
            aDo.setFrontOrderStatus(aShort);
        }
        return aDo;
    }

    /**
     * 填充订单来源名称
     * 
     * @param aDo
     */
    private void fillOrderSourceName(OrderInfoDTO aDo) {
        try {
            com.bat.dubboapi.distributor.common.Response<List<PlatformRpcDTO>> listResponse =
                platformServiceRpc.listPlatformByPlatformNos(Collections.singletonList(aDo.getOrderSource()));
            if (listResponse.isSuccess()) {
                if (!CollectionUtils.isEmpty(listResponse.getData())) {
                    aDo.setOrderSourceName(listResponse.getData().get(0).getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AdminOrderInfoListDTO> fillOrderSourceName2(List<AdminOrderInfoListDTO> list) {
        try {
            List<String> collect = list.stream().map(AdminOrderInfoListDTO::getUserSource).collect(Collectors.toList());
            com.bat.dubboapi.distributor.common.Response<List<PlatformRpcDTO>> listResponse =
                platformServiceRpc.listPlatformByPlatformNos(collect);
            if (listResponse.isSuccess()) {
                if (!CollectionUtils.isEmpty(listResponse.getData())) {
                    return list.stream().peek(dto -> {
                        Optional<PlatformRpcDTO> first = listResponse.getData().stream()
                            .filter(platformRpcDTO -> platformRpcDTO.getPlatformNo().equals(dto.getUserSource()))
                            .findFirst();
                        first.ifPresent(platformRpcDTO -> dto.setUserSourceName(platformRpcDTO.getName()));
                    }).collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取柔性定制订单详情
     * 
     * @param qry
     * @return
     */
    public AdminCustomerOrderInfoDetailDTO getCustomerOrderDetail(AdminOrderDetailQry qry) {
        AdminCustomerOrderInfoDetailDTO dto = new AdminCustomerOrderInfoDetailDTO();
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(qry.getId());
        if (orderInfoDO != null) {
            dto.setOrderInfo(OrderInfoConvertor.toOrderInfoDTO(orderInfoDO));
            fillOrderSourceName(dto.getOrderInfo());
            dto.setOrderDelivery(
                OrderDeliveryConvertor.toOrderDeliveryDTO(orderDeliveryDOMapper.getByOrderId(orderInfoDO.getId())));
            dto.setOrderExtendData(
                OrderDataConvertor.toOrderExtendDataDTO(orderExtendDataMapper.getByOrderId(orderInfoDO.getId())));
            dto.setOrderInvoice(
                OrderCostConvertor.toOrderInvoiceDTO(orderInvoiceDOMapper.getByOrderId(orderInfoDO.getId())));
            // 柔性客户 消费信息
            OrderCustomerDTO orderCustomerDTO = new OrderCustomerDTO();
            orderCustomerDTO.setOrderDistributorDataDTO(OrderCustomerDataConvertor
                .toOrderCustomerDataDTO(orderCustomerDataMapper.getByOrderIdAndCustomerId(orderInfoDO.getId(), null)));
            orderCustomerDTO.setOrderDistributorCostDTO(OrderCustomerCostConvertor
                .toOrderCustomerCostDTO(orderCustomerCostMapper.getByOrderIdAndCustomerId(orderInfoDO.getId(), null)));
            dto.setOrderDistributor(orderCustomerDTO);
            List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(orderInfoDO.getId());
            if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
                List<OrderInfoDetailGoodsDTO> collect = orderGoodsDOS.stream().map(orderGoodsDO -> {
                    OrderInfoDetailGoodsDTO dto1 = new OrderInfoDetailGoodsDTO();
                    dto1.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(orderGoodsDO));
                    dto1.setOrderGoodsDiy(OrderGoodsDiyConvertor
                        .toOrderGoodsDiyDTO(orderGoodsDiyDOMapper.getByOrderGoodsId(orderGoodsDO.getId())));
                    dto1.setOrderGoodsCustomerCost(OrderGoodsCustomerCostConvertor.toOrderGoodsCustomerCostDTO(
                        orderGoodsCustomerCostMapper.getByOrderGoodsId(orderGoodsDO.getId())));
                    return dto1;
                }).collect(Collectors.toList());
                dto.setOrderInfoDetailGoods(collect);
            }
        }
        // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
        Short aShort = CommonUtil.convertUserOrderStatus(
            dto.getOrderDistributor().getOrderDistributorDataDTO().getOrderStatus(),
            dto.getOrderDistributor().getOrderDistributorDataDTO().getPayStatus(),
            dto.getOrderInfo().getDeliverStatus(), dto.getOrderDistributor().getOrderDistributorDataDTO().getPayWay());
        dto.setFrontOrderStatus(aShort);
        return dto;
    }

    /**
     * 获取该商品的所有信息 包含多级分销信息
     * 
     * @description: 根据商品信息 补齐商品的定制信息（如果是定制商品）并查询 该商品在不同分销级数下的价格数据 并放入集合
     * 
     * @param orderGoodsDOS
     * @return
     */
    public List<OrderInfoDetailGoodsDTO> getAllOrderInfoDetailGoodsDTOS(List<OrderGoodsDO> orderGoodsDOS) {
        List<OrderGoodsExchangeCodeDO> exchangeCodes = new ArrayList<>();
        List<OrderDistributorDataDO> orderDistributorDataDOs = new ArrayList<>();
        if (orderGoodsDOS.size() > 0) {
            exchangeCodes = orderGoodsExchangeCodeMapper.listByOrderId(orderGoodsDOS.get(0).getOrderId());
            orderDistributorDataDOs = orderDistributorDataMapper.listByOrderId(orderGoodsDOS.get(0).getOrderId());
        }
        Map<Integer, OrderDistributorDataDO> orderDistributorDataMap = orderDistributorDataDOs.stream()
            .collect(Collectors.toMap(OrderDistributorDataDO::getDistributorId, a -> a, (k1, k2) -> k1));

        List<OrderGoodsExchangeCodeDO> finalExchangeCodes = exchangeCodes;
        List<OrderInfoDetailGoodsDTO> collect = orderGoodsDOS.stream().map(orderGoodsDO -> {
            OrderInfoDetailGoodsDTO dto = new OrderInfoDetailGoodsDTO();
            dto.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(orderGoodsDO));
            OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOMapper.getByOrderGoodsId(orderGoodsDO.getId());
            if (orderGoodsDiyDO != null) {
                dto.setOrderGoodsDiy(OrderGoodsDiyConvertor.toOrderGoodsDiyDTO(orderGoodsDiyDO));

                Iterator<OrderGoodsExchangeCodeDO> exchangeCodeIterator = finalExchangeCodes.iterator();
                while (exchangeCodeIterator.hasNext()) {
                    OrderGoodsExchangeCodeDO exchangeCodeBean = exchangeCodeIterator.next();
                    // 移除已核销的兑换码
                    if (exchangeCodeBean.getOrderGoodsId() == dto.getOrderGoodsDiy().getOrderGoodsId().intValue()) {
                        dto.getOrderGoodsDiy().setExchangeOrderId(exchangeCodeBean.getExchangeOrderId());
                        dto.getOrderGoodsDiy().setDistributorCompanyName(exchangeCodeBean.getDistributorCompanyName());
                        dto.getOrderGoodsDiy().setSalesName(exchangeCodeBean.getSalesName());
                        if (dto.getOrderGoodsDiy().getExchangeOrderId() == null) {
                            dto.getOrderGoodsDiy().setExchangeOrderId(0);
                        }
                        // 移除
                        exchangeCodeIterator.remove();
                        break;
                    }
                }
            }
            List<OrderGoodsDistributorCostDTO> list =
                OrderGoodsDistributorCostConvertor.toOrderGoodsDistributorCostDTOList(
                    orderGoodsDistributorCostDOMapper.listByOrderGoodsId(orderGoodsDO.getId()));
            for (OrderGoodsDistributorCostDTO orderGoodsDistributorCostDTO : list) {
                OrderDistributorDataDO dos =
                    orderDistributorDataMap.get(orderGoodsDistributorCostDTO.getDistributorId());
                if (dos != null) {
                    orderGoodsDistributorCostDTO.setDistributorName(dos.getDistributorName());
                }
            }
            dto.setOrderGoodsDistributorCosts(list);
            return dto;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 从
     * 
     * @see #getAllOrderInfoDetailGoodsDTOS(List<OrderGoodsDO>)
     * 
     *      过滤出特定分销级数数据
     *
     * 
     * @param distributorId
     * @param collect
     * @return
     */
    public List<OrderInfoDetailGoodsDTO> getOrderInfoDetailGoodsDTOS(Integer distributorId,
        List<OrderInfoDetailGoodsDTO> collect) {
        return collect.stream().map(orderInfoDetailGoodsDTO -> {
            OrderInfoDetailGoodsDTO dto = new OrderInfoDetailGoodsDTO();
            dto.setOrderGoods(orderInfoDetailGoodsDTO.getOrderGoods().clone());
            if (orderInfoDetailGoodsDTO.getOrderGoodsDiy() != null) {
                dto.setOrderGoodsDiy(orderInfoDetailGoodsDTO.getOrderGoodsDiy().clone());
            }
            OrderGoodsDistributorCostDTO costDTO = orderInfoDetailGoodsDTO.getOrderGoodsDistributorCosts().stream()
                .filter(orderGoodsDistributorCostDTO -> orderGoodsDistributorCostDTO.getDistributorId()
                    .equals(distributorId))
                .findFirst().orElse(new OrderGoodsDistributorCostDTO());
            dto.setOrderGoodsDistributorCost(costDTO.clone());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 封装 订单所对应的 多级分销数据
     * 
     * 并筛选分销数据 选出合适的分销级数 做默认级数
     * 
     * @param qry
     * @param aDo
     * @param orderInfoDO
     * @return
     */
    private List<OrderDistributorDTO> getOrderDistributorDTOS(AdminOrderDetailQry qry,
        AdminDistributorOrderInfoDetailDTO aDo, OrderInfoDO orderInfoDO) {
        List<OrderDistributorDataDO> orderDistributorDataDOS =
            orderDistributorDataMapper.listByOrderId(orderInfoDO.getId());
        return orderDistributorDataDOS.stream().map(orderDistributorDataDO -> {
            OrderDistributorDTO dto = new OrderDistributorDTO();
            dto.setOrderDistributorDataDTO(OrderDistributorConvertor.toOrderDistributorDataDTO(orderDistributorDataDO));
            dto.setOrderDistributorCostDTO(OrderDistributorConvertor.toOrderDistributorCostDTO(
                orderDistributorCostMapper.getByOrderIdAndDistributorId(orderDistributorDataDO.getOrderId(),
                    orderDistributorDataDO.getDistributorId())));
            // 并筛选分销数据 选出合适的分销级数 做默认级数
            boolean erpFlag = qry.getSearchType().equals(SearchType.ADMIN_ERP_ORDER_LIST)
                && dto.getOrderDistributorDataDTO().getErpFlag().equals(ErpFlag.YES);
            log.info("erpFlag:{}", erpFlag);
            boolean directFlag = qry.getSearchType().equals(SearchType.ADMIN_DISTRIBUTOR_ORDER_LIST)
                && (dto.getOrderDistributorDataDTO().getDirectFlag().equals(DirectFlag.YES)
                    || dto.getOrderDistributorDataDTO().getDistributorAncestorId() == null);
            log.info("directFlag:{}", directFlag);
            if (erpFlag || directFlag) {
                log.info("默认分销商数据：{}", dto.clone());
                aDo.setOrderDistributor(dto.clone());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderInfoDO> listByIds(List<Integer> orderIds) {
        return orderInfoDOMapper.listByIds(orderIds);
    }

    public String getOrderIdByOrderNo(Integer id) {
        return orderInfoDOMapper.selectByPrimaryKey(id).getOrderNo();
    }

    public OrderInfoDO getByOrderNo(String orderNo) {
        return orderInfoDOMapper.getByOrderNo(orderNo);
    }

    public List<OrderExcelCO> listCOByCondition(String sql, Boolean activityFlag, Integer distributorId,
        List<String> orderSourceList, List<Short> orderStatusList, Date startTime, Date endTime, String itemCode,
        List<Integer> orderTypeIdList, List<Short> payStatusList, List<Short> deliverStatusList) {
        return orderInfoDOMapper.listCOByCondition(sql, activityFlag, distributorId, orderSourceList, orderStatusList,
            startTime, endTime, itemCode, orderTypeIdList, payStatusList, deliverStatusList);
    }

    public AdminJudgeOrderDTO judgeDistributorOrderStatus(AdminJudgeOrderQry qry) {
        AdminJudgeOrderDTO dto = new AdminJudgeOrderDTO();
        // 校验参数 判断订单是否存在
        if (qry.getId() == null && qry.getOrderNo() == null) {
            throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_ID_ORDER_NO_NULL);
        } else {
            OrderInfoDO orderInfoDO = null;
            if (qry.getId() != null) {
                orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(qry.getId());
            }
            if (orderInfoDO == null && qry.getOrderNo() != null) {
                orderInfoDO = orderInfoDOMapper.getByOrderNo(qry.getOrderNo());
                qry.setId(orderInfoDO.getId());
            }
            if (orderInfoDO == null) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_NOT_EXIST);
            }
        }
        // 判断订单类型
        List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(qry.getId());
        if (orderGoodsDOS.get(0).getDiyType().equals(Constant.COMMON_CUSTOM_TYPE_STANDARD)) {
            // 标准
            dto.setCustomerOrderFlag(false);
        } else if (orderGoodsDOS.get(0).getDiyType().equals(Constant.COMMON_CUSTOM_TYPE_DIY)) {
            // DIY定制
            OrderCustomerDataDO byOrderId = orderCustomerDataMapper.getByOrderId(qry.getId());
            if (byOrderId != null) {
                // 未付款 没有确认的柔性订单
                dto.setCustomerOrderFlag(true);
            } else {
                log.info("订单为C端 定制订单 但是没有C端订单数据，数据不完整");
                // 但是不排除PC下柔性订单
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_INCOMPLETE_DATA);
            }
        }
        List<OrderDistributorDataDO> orderDistributorDataDOS = orderDistributorDataMapper.listByOrderId(qry.getId());
        if (CollectionUtils.isEmpty(orderDistributorDataDOS)) {
            // 没有分销层数据 可能是柔性订单
            dto.setDistributorOrderFlag(false);
            dto.setErpOrderFlag(false);
        } else {
            // ERP 层数据
            orderDistributorDataDOS.stream()
                .filter(orderDistributorDataDO -> orderDistributorDataDO.getErpFlag().equals((short)1)).findFirst()
                .ifPresent(orderDistributorDataDO1 -> dto.setErpOrderFlag(true));
            // 除了ERP层数据之外 还没有分销层数据
            List<OrderDistributorDataDO> orders = orderDistributorDataDOS.stream()
                .filter(orderDistributorDataDO -> orderDistributorDataDO.getErpFlag().equals((short)0))
                .collect(Collectors.toList());
            if (orders.size() >= 1) {
                dto.setDistributorOrderFlag(true);
            }
        }
        return dto;
    }
}
