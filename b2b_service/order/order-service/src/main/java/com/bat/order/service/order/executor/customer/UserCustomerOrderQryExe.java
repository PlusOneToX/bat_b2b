package com.bat.order.service.order.executor.customer;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.order.dao.order.*;
import com.bat.order.dao.order.dataobject.*;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.constant.OrderDeliverConstant;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.OrderTimer;
import com.bat.order.service.common.error.OrderCommonErrorCode;
import com.bat.order.service.common.utils.CommonUtil;
import com.bat.order.service.deliver.convertor.OrderDeliverBillConvertor;
import com.bat.order.service.deliver.executor.OrderDeliveryTraceQry;
import com.bat.order.service.order.convertor.*;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliveryTraceDTO;
import com.bat.order.api.order.dto.common.OrderGoodsThirdCodeDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderCustomerDataDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDetailGoodsDTO;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListDTO;
import com.bat.order.api.order.dto.orderquery.user.UserCustomerOrderInfoListQry;
import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.OrderGoodsCustomerCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.deliver.OrderDeliverBillDOMapper;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.service.order.convertor.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/6 13:42
 */
@Component
public class UserCustomerOrderQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCustomerOrderQryExe.class);

    @Resource
    private OrderConfig orderConfig;

    @Resource
    private OrderCustomerDataMapper orderCustomerDataMapper;

    @Resource
    private OrderCustomerCostMapper orderCustomerCostMapper;

    @Resource
    private OrderDistributorDataMapper orderDistributorDataMapper;

    @Resource
    private OrderInfoDOMapper orderInfoDOMapper;

    @Resource
    private OrderGoodsDOMapper orderGoodsDOMapper;

    @Resource
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    @Resource
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostMapper;

    @Resource
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    @Resource
    private OrderDeliverBillDOMapper orderDeliverBillDOMapper;

    @Resource
    private OrderDeliveryTraceQry orderDeliveryTraceQry;

    @Resource
    private OrderGoodsExchangeCodeMapper orderGoodsExchangeCodeMapper;

    @Resource
    private OrderGoodsThirdCodeMapper orderGoodsThirdCodeMapper;

    @Resource
    private UserCustomerOrderCmdExe userCustomerOrderCmdExe;

    public PageInfo<UserCustomerOrderInfoListDTO> listCustomerOrderInfoByCustomerId(UserCustomerOrderInfoListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<OrderInfoDO> orderInfoListDOS = orderInfoDOMapper.listUserCustomerOrderByParams(map);
        PageInfo pageInfo = new PageInfo(orderInfoListDOS);
        List<UserCustomerOrderInfoListDTO> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderInfoListDOS)) {
            collect = orderInfoListDOS.stream()
                .map(orderInfoListDO -> getUserOrderInfoListDTO(qry.getCustomerId(), orderInfoListDO))
                .collect(Collectors.toList());
        }
        pageInfo.setList(collect);
        return pageInfo;
    }

    /**
     * 柔性订单基本详情（订单列表中的订单信息）
     * 
     * @param customerId
     * @param orderInfoListDO
     * @return
     */
    private UserCustomerOrderInfoListDTO getUserOrderInfoListDTO(Integer customerId, OrderInfoDO orderInfoListDO) {
        UserCustomerOrderInfoListDTO dto = new UserCustomerOrderInfoListDTO();

        //第一步  存orderInfo 订单基本信息
        OrderInfoDTO orderInfoDTO = OrderInfoConvertor.toOrderInfoDTO(orderInfoListDO);
        dto.setOrderInfo(orderInfoDTO);

        //第二步  存orderCustomerData 订单客户信息
        OrderCustomerDataDO orderCustomerDataDO = orderCustomerDataMapper.getByOrderIdAndCustomerId(orderInfoDTO.getId(), customerId);
        if (orderCustomerDataDO == null) {
            return dto;
        }
        OrderCustomerDataDTO dto1 = OrderCustomerDataConvertor.toOrderCustomerDataDTO(orderCustomerDataDO);
        dto.setOrderCustomerData(dto1);
        // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
        Short aShort = CommonUtil.convertAdminOrderStatus(dto.getOrderCustomerData().getOrderStatus(),
            dto.getOrderCustomerData().getPayStatus(), dto.getOrderInfo().getDeliverStatus(),
            dto.getOrderCustomerData().getPayWay());
        dto.getOrderCustomerData().setFrontOrderStatus(aShort);

        // 第三步  存orderCustomerCost 订单消费信息
        OrderCustomerCostDO orderCustomerCostDO = orderCustomerCostMapper.getByOrderIdAndCustomerId(dto.getOrderInfo().getId(), null);
        dto.setOrderCustomerCost(OrderCustomerCostConvertor.toOrderCustomerCostDTO(orderCustomerCostDO));

        // 第四步  存orderInfoDetailGoods 商品明细
        List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(orderInfoDTO.getId());

        AtomicReference<BigDecimal> orderCouponDistributionAmount = new AtomicReference<>(new BigDecimal(0)); //定义叠加运费和优惠

        List<OrderInfoDetailGoodsDTO> goodsDTOS = orderGoodsDOS.stream().map(orderGoodsDO -> {

            OrderInfoDetailGoodsDTO goodsDTO = new OrderInfoDetailGoodsDTO();

            //订单商品明细
            goodsDTO.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(orderGoodsDO));

            //订单定制明细
            OrderGoodsDiyDO goodsDiyDO = orderGoodsDiyDOMapper.getByOrderGoodsId(orderGoodsDO.getId());
            goodsDTO.setOrderGoodsDiy(OrderGoodsDiyConvertor.toOrderGoodsDiyDTO(goodsDiyDO));

            //订单费用明细
            OrderGoodsCustomerCostDO orderGoodsCustomerCostDO = orderGoodsCustomerCostMapper.getByOrderGoodsId(orderGoodsDO.getId());
            if (orderGoodsCustomerCostDO != null) {
                goodsDTO.setOrderGoodsCustomerCost(OrderGoodsCustomerCostConvertor.toOrderGoodsCustomerCostDTO(orderGoodsCustomerCostDO));

                if(goodsDTO.getOrderGoodsCustomerCost().getDeliveryFeeFlag() != null){
                    if(goodsDTO.getOrderGoodsCustomerCost().getDeliveryFeeFlag() == Constant.DELIVERY_FEE_FLAG_1){
                        // 商品实际单价叠加
                        orderCouponDistributionAmount.set(orderCouponDistributionAmount.get().add(goodsDTO.getOrderGoodsCustomerCost().getActualPrice()));
                    }
                }else{
                    LOGGER.info("商品未使用优惠不用设置运费");
                }
            }

            // 如果是兑换卡订单，则增加兑换卡信息
            if (orderConfig.getPlatformCards().contains(dto.getOrderInfo().getOrderSource())) {
                OrderGoodsExchangeCodeDO exchangeCodeDO = orderGoodsExchangeCodeMapper
                    .getByOrderIdAndOrderGoodsId(orderInfoDTO.getId(), orderGoodsDO.getId());
                goodsDTO.setOrderGoodsExchangeCode(
                    OrderGoodsExchangeCodeConvertor.toOrderGoodsExchangeCodeDTO(exchangeCodeDO));
            }
            return goodsDTO;
        }).collect(Collectors.toList());

        //取原来的运费
        BigDecimal distributionAmount = dto.getOrderCustomerCost().getDistributionAmount();
        if(distributionAmount != null){
            //运费 = 原来的运费 + 叠加后的商品实际单价
            dto.getOrderCustomerCost().setDistributionAmount(distributionAmount.add(orderCouponDistributionAmount.get()));
        }

        //取原来的优惠
        BigDecimal orderCouponAmount = dto.getOrderCustomerCost().getOrderCouponAmount();
        if(orderCouponAmount != null){
            //优惠 = 原来的优惠 + 叠加后的商品实际单价
            dto.getOrderCustomerCost().setOrderCouponAmount(orderCouponAmount.add(orderCouponDistributionAmount.get()));
        }

        //存订单明细
        dto.setOrderInfoDetailGoods(goodsDTOS);
        return dto;
    }

    public UserCustomerOrderInfoListDTO getCustomerOrderDetailInfoById(Integer id) {
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(id);
        UserCustomerOrderInfoListDTO dto = getUserOrderInfoListDTO(null, orderInfoDO);
        // 配送信息
        dto.setOrderDelivery(
            OrderDeliveryConvertor.toOrderDeliveryDTO(orderDeliveryDOMapper.getByOrderId(dto.getOrderInfo().getId())));
        // 组装发货参数
        List<OrderDeliverBillDO> orderDeliverBillDOS =
            orderDeliverBillDOMapper.listByOrderId(dto.getOrderInfo().getId());
        List<OrderDeliverDetailDTO> collect = orderDeliverBillDOS.stream().map(orderDeliverBillDO -> {
            OrderDeliverDetailDTO dto1 = new OrderDeliverDetailDTO();
            OrderDeliverBillDTO dto2 = OrderDeliverBillConvertor.toOrderDeliverBillDTO(orderDeliverBillDO);
            dto1.setOrderDeliverBill(dto2);
            if (dto2 != null) {
                dto1.setOrderDeliveryTrace(orderDeliveryTraceQry.getByOrderDeliverBillId(dto2.getId()));
                // 如果已经签收 并且状态还没有改为已完成 把订单状态改为已完成
                if (Short.valueOf(dto2.getLogisticsStatus())
                    .equals(OrderDeliverConstant.ORDER_DELIVER_LOGISTICS_STATUS_SIGN)
                    && dto.getOrderCustomerData().getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
                    OrderCustomerDataDO orderCustomerDataDO =
                        orderCustomerDataMapper.getByOrderIdAndCustomerId(orderInfoDO.getId(), null);
                    orderCustomerDataDO.setOrderStatus(OrderStatus.COMPLETED.getValue());
                    orderCustomerDataMapper.updateByPrimaryKey(orderCustomerDataDO);
                    dto.getOrderCustomerData().setOrderStatus(OrderStatus.COMPLETED.getValue());
                    // 更新B端订单状态
                    orderDistributorDataMapper.updateOrderPayStatusFormDeliver(orderInfoDO.getId(),
                        OrderStatus.COMPLETED.getValue());
                    LOGGER.error("该订单已修改为已完成状态:{}", JSONObject.toJSONString(orderCustomerDataDO));
                    userCustomerOrderCmdExe.dealFlagSendToSangxing(orderInfoDO.getId());
                }
            }
            return dto1;
        }).collect(Collectors.toList());
        // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
        Short aShort = CommonUtil.convertAdminOrderStatus(dto.getOrderCustomerData().getOrderStatus(),
            dto.getOrderCustomerData().getPayStatus(), dto.getOrderInfo().getDeliverStatus(),
            dto.getOrderCustomerData().getPayWay());
        dto.getOrderCustomerData().setFrontOrderStatus(aShort);
        dto.setOrderDeliverDetail(collect);
        return dto;
    }

    public OrderGoodsThirdCodeDTO findOrderByThirdCode(String code) {
        OrderGoodsThirdCodeDO orderGoodsThirdCodeDO = orderGoodsThirdCodeMapper.findByCode(code);
        if (orderGoodsThirdCodeDO != null) {
            OrderGoodsThirdCodeDTO dto = new OrderGoodsThirdCodeDTO();
            BeanUtils.copyProperties(orderGoodsThirdCodeDO, dto);
            return dto;
        }
        return null;
    }

    public List<OrderGoodsThirdCodeDO> listDeliverWriteOff() {
        // 找出已发货但是未核销的订单
        List<OrderGoodsThirdCodeDO> orderGoodsThirdCodeDOS = orderGoodsThirdCodeMapper.listDeliverByWriteOff((short)0);
        Iterator<OrderGoodsThirdCodeDO> iterator = orderGoodsThirdCodeDOS.iterator();
        // 遍历过滤掉未签收订单
        while (iterator.hasNext()) {
            boolean signFor = false;
            OrderGoodsThirdCodeDO orderGoodsThirdCodeDO = iterator.next();
            // 发货单信息
            List<OrderDeliverBillDO> orderDeliverBillDOS =
                orderDeliverBillDOMapper.listByOrderId(orderGoodsThirdCodeDO.getOrderId());
            LOGGER.info("荣耀核销发货单信息:{},订单id{}", JSONObject.toJSONString(orderDeliverBillDOS),
                orderGoodsThirdCodeDO.getOrderId());
            for (OrderDeliverBillDO orderDeliverBillDO : orderDeliverBillDOS) {
                // 追踪物流 会更新物流轨迹 与签收状态
                if (orderDeliverBillDO.getLogisticsStatus().equals("3")) {
                    signFor = true;
                    break;
                }

                boolean largeBreak = false;
                // 查找物流信息
                List<OrderDeliveryTraceDTO> orderDeliveryTraceDTOs =
                    orderDeliveryTraceQry.getByOrderDeliverBillId(orderDeliverBillDO.getId());
                LOGGER.info("荣耀核销返回的物流信息:{},发货单id{}", JSONObject.toJSONString(orderDeliveryTraceDTOs),
                    orderDeliverBillDO.getId());
                for (OrderDeliveryTraceDTO orderDeliveryTraceDTO : orderDeliveryTraceDTOs) {
                    if (StringUtils.isNotBlank(orderDeliveryTraceDTO.getLogisticsStatus())
                        && orderDeliveryTraceDTO.getLogisticsStatus().equals("3")) {
                        signFor = true;
                        largeBreak = true;
                        break;
                    }
                }
                if (largeBreak) {
                    break;
                }
            }
            // 未被签收则过滤
            if (!signFor) {
                LOGGER.info("当前订单未被签收:{}", JSONObject.toJSONString(orderGoodsThirdCodeDO));
                iterator.remove();
            }
        }
        LOGGER.info("当前已签收订单:{}", JSONObject.toJSONString(orderGoodsThirdCodeDOS));
        return orderGoodsThirdCodeDOS;
    }

    @Transactional(rollbackFor = Exception.class)
    public void writeOffThirdCodeOrders(List<Integer> ids) {
        if (ids == null && ids.size() == 0) {
            throw OrderException.buildException(OrderCommonErrorCode.COMMON_ID_ERROR);
        }
        List<OrderGoodsThirdCodeDO> list = new ArrayList<>();
        for (Integer id : ids) {
            OrderGoodsThirdCodeDO orderGoodsThirdCodeDO = new OrderGoodsThirdCodeDO();
            orderGoodsThirdCodeDO.setId(id);
            orderGoodsThirdCodeDO.setWriteOffFlag(Constant.WRITE_OFF_FLAG_1);
            list.add(orderGoodsThirdCodeDO);
        }
        orderGoodsThirdCodeMapper.listUpdateWriteOff(list);
    }

    /**
     * C端获取订单失效时间（单位分钟）
     */
    public Integer getLoseTime() {
        Integer time = 30;
        if (orderConfig.getOrderTimerPowerOffCLevel().equals(OrderTimer.Timer1.getLevel())) {
            time = OrderTimer.Timer1.getValue();
        } else if (orderConfig.getOrderTimerPowerOffCLevel().equals(OrderTimer.Timer2.getLevel())) {
            time = OrderTimer.Timer2.getValue();
        } else if (orderConfig.getOrderTimerPowerOffCLevel().equals(OrderTimer.Timer3.getLevel())) {
            time = OrderTimer.Timer3.getValue();
        } else if (orderConfig.getOrderTimerPowerOffCLevel().equals(OrderTimer.Timer4.getLevel())) {
            time = OrderTimer.Timer4.getValue();
        } else if (orderConfig.getOrderTimerPowerOffCLevel().equals(OrderTimer.Timer5.getLevel())) {
            time = OrderTimer.Timer5.getValue();
        }
        return time;
    }
}
