package com.bat.order.service.order.executor.distributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.dao.order.dataobject.*;
import com.bat.order.service.common.CommonRpcQryExe;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.enumtype.OrderStockType;
import com.bat.order.service.common.enumtype.OrderTimer;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.utils.CommonUtil;
import com.bat.order.service.deliver.convertor.OrderDeliverBillConvertor;
import com.bat.order.service.deliver.convertor.OrderDeliverBillDetailConvertor;
import com.bat.order.service.deliver.executor.OrderDeliveryTraceQry;
import com.bat.order.service.order.convertor.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGoodsControlRpcDTO;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsAreVisibleRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionRpcDTO;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.constant.PayStatusEnum;
import com.bat.order.api.common.constant.PayWayEnum;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDetailDTO;
import com.bat.order.api.order.constants.OrderFlag;
import com.bat.order.api.order.dto.common.OrderOneMoreDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderDistributorDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDetailGoodsDTO;
import com.bat.order.api.order.dto.orderquery.user.*;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.OrderGoodsDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.OrderDeliverBillDOMapper;
import com.bat.order.dao.deliver.OrderDeliverBillDetailDOMapper;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.order.OrderGoodsDOMapper;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.OrderTypeDOMapper;
import com.bat.order.service.order.constans.FrontOrderStatus;
import com.bat.order.service.order.constans.FrontOrderType;
import com.bat.order.service.order.constans.OrderErpType;
import com.bat.order.service.order.convertor.*;
import com.bat.order.service.order.executor.OrderRpcExe;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/2 20:07
 */
@Component
public class UserDistributorOrderQryExe {

    @Autowired
    private OrderInfoDOMapper orderInfoDOMapper;

    @Autowired
    private OrderGoodsDOMapper orderGoodsDOMapper;

    @Autowired
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    @Autowired
    private OrderDistributorDataMapper orderDistributorDataDOMapper;

    @Autowired
    private OrderDistributorCostMapper orderDistributorCostDOMapper;

    @Autowired
    private OrderTypeDOMapper orderTypeDOMapper;

    @Autowired
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    @Autowired
    private OrderGoodsDistributorCostMapper orderGoodsDistributorCostMapper;

    @Autowired
    private OrderDeliveryTraceQry orderDeliveryTraceQry;

    @Autowired
    private OrderDeliverBillDOMapper orderDeliverBillDOMapper;

    @Autowired
    private OrderDeliverBillDetailDOMapper orderDeliverBillDetailDOMapper;
    @Resource
    private CommonValidator commonValidator;
    @Resource
    private CommonRpcQryExe rpcQryExe;
    @Resource
    private OrderRpcExe orderRpcExe;
    @Resource
    private OrderConfig orderConfig;

    /**
     * 分销获取再来一单数据
     * 
     * @param qry
     * @param userId
     * @return
     */
    public List<OrderOneMoreDTO> oneMoreOrder(BaseId qry, String userId) {
        commonValidator.checkUserId(userId);
        List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(qry.getId());

        List<GoodsItemPromotionRpcDTO> rpcDTOS = new ArrayList<>();
        List<GoodsItemPriceRpcDTO> priceRpcDTOS = new ArrayList<>();
        List<GoodsItemBoxRpcDTO> boxRpcDTOS = new ArrayList<>();
        List<GoodsItemRpcDTO> itemRpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
            List<Integer> itemIds = new ArrayList<>();
            List<Integer> cartIds = new ArrayList<>();
            List<GoodsItemRpc> goodsItems = new ArrayList<>();
            orderGoodsDOS.forEach(shoppingCartDistributorDO -> {
                cartIds.add(shoppingCartDistributorDO.getId());
                if (!itemIds.contains(shoppingCartDistributorDO.getItemId())) {
                    itemIds.add(shoppingCartDistributorDO.getItemId());
                    GoodsItemRpc goodsItemRpc = new GoodsItemRpc();
                    goodsItemRpc.setItemId(shoppingCartDistributorDO.getItemId());
                    goodsItemRpc.setGoodsId(shoppingCartDistributorDO.getGoodsId());
                    goodsItems.add(goodsItemRpc);
                }
            });
            // 获取商品活动数据
            rpcDTOS = rpcQryExe.goodsItemPromotionDistributorByOrderGoodsDOList(orderGoodsDOS, Integer.valueOf(userId));
            // 获取价格
            priceRpcDTOS = rpcQryExe.listDistributorGoodsItemPrice(goodsItems, Integer.valueOf(userId));
            // 装箱规格
            boxRpcDTOS = rpcQryExe.listGoodsItemBoxRpcDTOList(itemIds);
            // 货品数据
            itemRpcDTOS = orderRpcExe.listGoodsItems(itemIds);
        }
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = orderGoodsDiyDOMapper.listByOrderId(qry.getId());
        List<OrderGoodsDistributorCostDO> distributorCostDOS =
            orderGoodsDistributorCostMapper.listByOrderIdAndDistributorId(qry.getId(), Integer.valueOf(userId));
        List<OrderOneMoreDTO> orderOneMoreDTOS = OrderConvertor.toOrderOneMoreDTOList(orderGoodsDOS, orderGoodsDiyDOS,
            distributorCostDOS, rpcDTOS, priceRpcDTOS, boxRpcDTOS, itemRpcDTOS);

        // 过滤出订单的商品id
        List<Integer> orderGoodsIdList =
            orderOneMoreDTOS.stream().map(x -> x.getGoodsId()).collect(Collectors.toList());

        // 根据用户id和商品id列表，去计算列表中的不可视商品
        List<Map<Integer, Boolean>> invisibleProductListMap =
            userProductVisibility(Integer.parseInt(userId), orderGoodsIdList);

        // 如果不可视商品不为空，就需要判断
        if (ObjectUtils.isNotEmpty(invisibleProductListMap)) {
            // 循环再来一单商品，如果商品id能从不可视的map集合中取到内容，则说明购物车中有不可视商品
            for (OrderOneMoreDTO orderOneMoreDTO : orderOneMoreDTOS) {
                long count = invisibleProductListMap.stream()
                    .filter(x -> ObjectUtils.isNotEmpty(x.get(orderOneMoreDTO.getGoodsId()))).count();
                if (count > 0) {
                    // 不可视
                    orderOneMoreDTO.setVisible(0);
                }
            }
        }
        return orderOneMoreDTOS;
    }

    public UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO getOrderInfoById(Integer id) {
        UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO resultDTO =
            new UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO();
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(id);
        if (orderInfoDO != null) {
            resultDTO.setOrderInfo(OrderInfoConvertor.toOrderInfoDTO(orderInfoDO));
            // 获取订单类型
            resultDTO.setOrderType(
                OrderTypeConvertor.toOrderTypeDTO(orderTypeDOMapper.selectByPrimaryKey(orderInfoDO.getOrderTypeId())));
            getFrontOrderType(resultDTO);
            List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(orderInfoDO.getId());
            if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
                List<OrderInfoDetailGoodsDTO> collect = orderGoodsDOS.stream().map(orderGoodsDO -> {
                    OrderInfoDetailGoodsDTO dto = new OrderInfoDetailGoodsDTO();
                    dto.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(orderGoodsDO));
                    OrderGoodsDiyDO byOrderGoodsId = orderGoodsDiyDOMapper.getByOrderGoodsId(orderGoodsDO.getId());
                    dto.setOrderGoodsDiy(OrderGoodsDiyConvertor.toOrderGoodsDiyDTO(byOrderGoodsId));
                    return dto;
                }).collect(Collectors.toList());
                resultDTO.setOrderInfoDetailGoods(collect);
            }
            OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataDOMapper
                .getDistributorDataByOrderIdAndFlag(orderInfoDO.getId(), OrderFlag.DIRECT_FLAG);
            if (orderDistributorDataDO != null) {
                OrderDistributorDTO dto = new OrderDistributorDTO();
                dto.setOrderDistributorDataDTO(
                    OrderDistributorConvertor.toOrderDistributorDataDTO(orderDistributorDataDO));
                OrderDistributorCostDO orderDistributorCostDO = orderDistributorCostDOMapper
                    .getByOrderIdAndDistributorId(orderInfoDO.getId(), orderDistributorDataDO.getDistributorId());
                dto.setOrderDistributorCostDTO(
                    OrderDistributorConvertor.toOrderDistributorCostDTO(orderDistributorCostDO));
                resultDTO.setOrderDistributor(dto);
            }
        }
        return resultDTO;
    }

    /**
     * 获取前端 订单类型
     * 
     * @param resultDTO
     */
    private void getFrontOrderType(UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO resultDTO) {
        if (OrderErpType.DIY_SYS.equals(resultDTO.getOrderType().getErpType())) {
            resultDTO.setType(FrontOrderType.DIY);
        } else {
            if (resultDTO.getOrderInfo().getStockType().equals(OrderStockType.IN.getValue())) {
                resultDTO.setType(FrontOrderType.ON_STOCK);
            } else if (resultDTO.getOrderInfo().getStockType().equals(OrderStockType.ON_WAY.getValue())) {
                resultDTO.setType(FrontOrderType.ON_WAY);
            }
        }
    }

    /**
     * PC 端订单列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserPCDistributorOrderInfoListDTO>
        listOrderInfoByDistributorId(UserPCDistributorOrderInfoListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<UserDistributorOrderInfoListDO> orderInfoListDOS =
            orderInfoDOMapper.listUserPCDistributorOrderByParams(map);
        PageInfo pageInfo = new PageInfo(orderInfoListDOS);
        List<UserPCDistributorOrderInfoListDTO> collect =
            OrderInfoConvertor.toUserPCDistributorOrderInfoListDTOList(orderInfoListDOS);
        pageInfo.setList(collect);
        return pageInfo;
    }

    /**
     * PC端柔性店铺订单列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserPCDistributorOrderInfoListDTO>
        listShopOrderInfoByDistributorId(UserPCDistributorOrderInfoListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<UserCustomerOrderInfoListDO> orderInfoListDOS = orderInfoDOMapper.listUserPCShopOrderInfoByParams(map);
        PageInfo pageInfo = new PageInfo(orderInfoListDOS);
        List<UserPCDistributorOrderInfoListDTO> collect =
            OrderInfoConvertor.toUserPCCustomerOrderInfoListDTOList(orderInfoListDOS);
        pageInfo.setList(collect);
        return pageInfo;
    }

    public List<UserPCDistributorOrderInfoExportDTO>
        shopOrderInfoByDistributorIdExportData(UserPCDistributorOrderInfoListQry qry) {
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<UserCustomerOrderInfoListDO> orderInfoListDOS = orderInfoDOMapper.listUserPCShopOrderInfoByParams(map);
        List<UserPCDistributorOrderInfoListDTO> collect =
            OrderInfoConvertor.toUserPCCustomerOrderInfoListDTOList(orderInfoListDOS);
        List<UserPCDistributorOrderInfoExportDTO> list = new ArrayList<>();
        if (collect != null) {
            for (UserPCDistributorOrderInfoListDTO userPCDistributorOrderInfoListDTO : collect) {
                UserPCDistributorOrderInfoExportDTO userPCDistributorOrderInfoExportDTO =
                    new UserPCDistributorOrderInfoExportDTO();
                BeanUtils.copyProperties(userPCDistributorOrderInfoListDTO, userPCDistributorOrderInfoExportDTO);
                // 订单状态转换
                userPCDistributorOrderInfoExportDTO.setFrontOrderStatus(
                    FrontOrderStatus.getStatusStr(userPCDistributorOrderInfoListDTO.getFrontOrderStatus()));
                userPCDistributorOrderInfoExportDTO
                    .setPayWay(PayWayEnum.statusStr(userPCDistributorOrderInfoListDTO.getPayWay()));
                userPCDistributorOrderInfoExportDTO
                    .setPayStatus(PayStatusEnum.statusStr(userPCDistributorOrderInfoListDTO.getPayStatus()));
                list.add(userPCDistributorOrderInfoExportDTO);
            }
        }
        return list;
    }

    public PageInfo<UserMBDistributorOrderInfoListDTO>
        listMBOrderInfoByDistributorId(UserMBDistributorOrderInfoListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<OrderInfoDO> orderInfoDOS = orderInfoDOMapper.listUserMBDistributorOrderByParams(map);
        PageInfo pageInfo = new PageInfo(orderInfoDOS);
        pageInfo.setList(getUserMBDistributorOrderInfoListDTOS(false, qry, orderInfoDOS));
        return pageInfo;
    }

    /**
     * 移动端下级分销商订单列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserMBDistributorOrderInfoListDTO>
        listMBNextDistributorOrderInfoByDistributorId(UserMBDistributorOrderInfoListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<OrderInfoDO> orderInfoDOS = orderInfoDOMapper.listUserMBDistributorOrderByParams(map);
        PageInfo pageInfo = new PageInfo(orderInfoDOS);
        pageInfo.setList(getUserMBDistributorOrderInfoListDTOS(true, qry, orderInfoDOS));
        return pageInfo;
    }

    /**
     * 移动端下级分销商订单总数
     * 
     * @param qry
     * @return
     */
    public Integer countMBNextDistributorOrderInfoByDistributorId(UserMBDistributorOrderInfoCountQry qry) {
        Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(qry));
        Integer count = orderInfoDOMapper.countUserMBDistributorOrderByParams(map);
        return count;
    }

    /**
     * 移动端 公共组装代码
     * 
     * @param searchNextFlag
     *            是否查询下级分销订单标志
     * @param qry
     * @param orderInfoDOS
     * @return
     */
    private List<UserMBDistributorOrderInfoListDTO> getUserMBDistributorOrderInfoListDTOS(boolean searchNextFlag,
        UserMBDistributorOrderInfoListQry qry, List<OrderInfoDO> orderInfoDOS) {
        List<UserMBDistributorOrderInfoListDTO> collect = orderInfoDOS.stream().map(orderInfoDO -> {
            UserMBDistributorOrderInfoListDTO dto = new UserMBDistributorOrderInfoListDTO();
            dto.setOrderInfo(OrderInfoConvertor.toOrderInfoDTO(orderInfoDO));
            if (searchNextFlag) {
                dto.setOrderDistributorData(
                    OrderDistributorConvertor.toOrderDistributorDataDTO(orderDistributorDataDOMapper
                        .getByOrderIdAndDistributorAncestorId(orderInfoDO.getId(), qry.getDistributorId())));
                // 下级分销商订单查询 新加发货信息
                dto.setOrderDelivery(
                    OrderDeliveryConvertor.toOrderDeliveryDTO(orderDeliveryDOMapper.getByOrderId(orderInfoDO.getId())));
            } else {
                dto.setOrderDistributorData(
                    OrderDistributorConvertor.toOrderDistributorDataDTO(orderDistributorDataDOMapper
                        .getByOrderIdAndDistributorId(orderInfoDO.getId(), qry.getDistributorId())));
            }
            dto.setOrderDistributorCost(OrderDistributorConvertor.toOrderDistributorCostDTO(orderDistributorCostDOMapper
                .getByOrderIdAndDistributorId(orderInfoDO.getId(), dto.getOrderDistributorData().getDistributorId())));
            List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(orderInfoDO.getId());
            List<OrderInfoDetailGoodsDTO> collect1 = orderGoodsDOS.stream().map(orderGoodsDO -> {
                OrderInfoDetailGoodsDTO dto1 = new OrderInfoDetailGoodsDTO();
                dto1.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(orderGoodsDO));
                OrderGoodsDistributorCostDO goodsDistributorCostDO =
                    orderGoodsDistributorCostMapper.getByOrderGoodsIdAndDistributorId(orderGoodsDO.getId(),
                        dto.getOrderDistributorData().getDistributorId());
                dto1.setOrderGoodsDistributorCost(
                    OrderGoodsDistributorCostConvertor.toOrderGoodsDistributorCostDTO(goodsDistributorCostDO));
                return dto1;
            }).collect(Collectors.toList());
            dto.setOrderInfoDetailGoods(collect1);
            // 状态转换 后端订单状态 收款状态 发货状态 换算成前端订单状态
            Short aShort = CommonUtil.convertUserOrderStatus(dto.getOrderDistributorData().getOrderStatus(),
                dto.getOrderDistributorData().getPayStatus(), dto.getOrderInfo().getDeliverStatus(),
                dto.getOrderDistributorData().getPayWay());
            dto.setFrontOrderStatus(aShort);
            return dto;
        }).collect(Collectors.toList());
        return collect;

    }

    public UserPCDistributorOrderDetailDTO getOrderDetailInfoById(UserPCDistributorOrderDetailQry qry) {
        UserPCDistributorOrderDetailDTO dto = new UserPCDistributorOrderDetailDTO();
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(qry.getOrderId());
        if (orderInfoDO == null) {
            throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_NOT_EXIST);
        }
        // 订单基本信息
        dto.setOrderInfo(OrderInfoConvertor.toOrderInfoDTO(orderInfoDO));
        OrderDistributorDataDO orderDistributorDataDO =
            orderDistributorDataDOMapper.getByOrderIdAndDistributorId(qry.getOrderId(), qry.getDistributorId());
        OrderDistributorCostDO orderDistributorCostDO =
            orderDistributorCostDOMapper.getByOrderIdAndDistributorId(qry.getOrderId(), qry.getDistributorId());
        // 订单数据
        dto.setOrderDistributorData(OrderDistributorConvertor.toOrderDistributorDataDTO(orderDistributorDataDO));
        dto.setOrderDistributorCost(OrderDistributorConvertor.toOrderDistributorCostDTO(orderDistributorCostDO));
        // 前台订单状态
        Short aShort = CommonUtil.convertUserOrderStatus(dto.getOrderDistributorData().getOrderStatus(),
            dto.getOrderDistributorData().getPayStatus(), dto.getOrderInfo().getDeliverStatus(),
            dto.getOrderDistributorData().getPayWay());
        dto.setFrontOrderStatus(aShort);
        // 收货信息
        dto.setOrderDelivery(
            OrderDeliveryConvertor.toOrderDeliveryDTO(orderDeliveryDOMapper.getByOrderId(qry.getOrderId())));
        List<OrderGoodsDO> orderGoodsDOS = orderGoodsDOMapper.listByOrderId(qry.getOrderId());
        // 商品详情
        List<OrderInfoDetailGoodsDTO> collect = orderGoodsDOS.stream().map(orderGoodsDO -> {
            OrderInfoDetailGoodsDTO dto1 = new OrderInfoDetailGoodsDTO();
            // 商品
            dto1.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(orderGoodsDO));
            OrderGoodsDiyDO goodsDiyDO = orderGoodsDiyDOMapper.getByOrderGoodsId(orderGoodsDO.getId());
            // 定制信息
            dto1.setOrderGoodsDiy(OrderGoodsDiyConvertor.toOrderGoodsDiyDTO(goodsDiyDO));
            OrderGoodsDistributorCostDO costDO = orderGoodsDistributorCostMapper
                .getByOrderGoodsIdAndDistributorId(orderGoodsDO.getId(), qry.getDistributorId());
            // 商品维度 分销商费用
            dto1.setOrderGoodsDistributorCost(
                OrderGoodsDistributorCostConvertor.toOrderGoodsDistributorCostDTO(costDO));
            return dto1;
        }).collect(Collectors.toList());
        dto.setOrderInfoDetailGoods(collect);
        // 发货单信息
        List<OrderDeliverBillDO> orderDeliverBillDOS =
            orderDeliverBillDOMapper.listByOrderId(dto.getOrderInfo().getId());
        List<OrderDeliverDetailDTO> collect1 = orderDeliverBillDOS.stream().map(orderDeliverBillDO -> {
            OrderDeliverDetailDTO dto1 = new OrderDeliverDetailDTO();
            OrderDeliverBillDTO dto2 = OrderDeliverBillConvertor.toOrderDeliverBillDTO(orderDeliverBillDO);
            dto1.setOrderDeliverBill(dto2);
            if (dto2 != null) {
                dto1.setOrderDeliveryTrace(orderDeliveryTraceQry.getByOrderDeliverBillId(dto2.getId()));
            }
            return dto1;
        }).collect(Collectors.toList());
        dto.setOrderDeliverDetail(collect1);
        return dto;
    }

    public List<OrderDeliverBillDetailDTO>
        getPCOrderDeliverBillDetailById(UserPCDistributorOrderDeliverBillDetailQry qry) {
        List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOS =
            orderDeliverBillDetailDOMapper.listByOrderDeliverBillId(qry.getOrderDeliverBillId());
        return OrderDeliverBillDetailConvertor.toOrderDeliverBillDetailDTOList(orderDeliverBillDetailDOS);
    }

    public List<OrderDeliverDetailDTO> getMBOrderDeliverBillDetailByOrderId(UserPCDistributorOrderDetailQry qry) {
        List<OrderDeliverBillDO> orderDeliverBillDOS = orderDeliverBillDOMapper.listByOrderId(qry.getOrderId());
        if (!CollectionUtils.isEmpty(orderDeliverBillDOS)) {
            return orderDeliverBillDOS.stream().map(orderDeliverBillDO -> {
                OrderDeliverDetailDTO dto = new OrderDeliverDetailDTO();
                // 发货单
                dto.setOrderDeliverBill(OrderDeliverBillConvertor.toOrderDeliverBillDTO(orderDeliverBillDO));
                // 物流追踪
                dto.setOrderDeliveryTrace(orderDeliveryTraceQry.getByOrderDeliverBillId(orderDeliverBillDO.getId()));
                List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOS =
                    orderDeliverBillDetailDOMapper.listByOrderDeliverBillId(orderDeliverBillDO.getId());
                if (!CollectionUtils.isEmpty(orderDeliverBillDetailDOS)) {
                    List<OrderDeliverDetailDTO.OrderDeliverBillDetail> collect =
                        orderDeliverBillDetailDOS.stream().map(orderDeliverBillDetailDO -> {
                            OrderDeliverDetailDTO.OrderDeliverBillDetail detail =
                                new OrderDeliverDetailDTO.OrderDeliverBillDetail();
                            // 发货单商品详情
                            detail.setOrderDeliverBillDetail(
                                OrderDeliverBillDetailConvertor.toOrderDeliverBillDetailDTO(orderDeliverBillDetailDO));
                            // 下单商品 信息 补充规格等值
                            detail.setOrderGoods(OrderGoodsConvertor.toOrderGoodsDTO(
                                orderGoodsDOMapper.selectByPrimaryKey(orderDeliverBillDetailDO.getOrderGoodsId())));
                            // TODO 下单商品 分销商付款信息（柔性的暂时不考虑）
                            detail.setOrderGoodsDistributorCost(
                                OrderGoodsDistributorCostConvertor.toOrderGoodsDistributorCostDTO(
                                    orderGoodsDistributorCostMapper.getByOrderGoodsIdAndDistributorId(
                                        orderDeliverBillDetailDO.getOrderGoodsId(), qry.getDistributorId())));
                            return detail;
                        }).collect(Collectors.toList());
                    // 商品
                    dto.setOrderDeliverBillDetails(collect);
                }
                // 发货单商品详情
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     *
     * 前台订单转换
     *
     * 计算 并转换状态
     *
     * 0全部 订单全部 发货全部
     *
     * 1待确认 orderStatus1 payStatus3 订单待确认 已付款 （区间结算 线下付款可以未付款）
     *
     * 2待发货 orderStatus2 deliverStatus1 deliverStatus2 订单已确认 发货待发货 发货出库中
     *
     * 3部分发货 orderStatus2 deliverStatus3 订单已确认 发货部分发货
     *
     * 4待收货 orderStatus2 deliverStatus4 订单已确认 发货已发货
     *
     * 5已关闭 orderStatus4 订单已取消
     *
     * 6已完成 orderStatus5 订单已完成
     *
     * 7待付款 orderStatus1 payStatus3 订单待确认 待付款 （区间结算 线下付款不算）
     * 
     * @return
     */
    public List<UserMBOrderInfoCountDTO> listMBOrderInfoCountByDistributorId(UserMBOrderInfoCountQry qry) {
        List<UserMBOrderInfoCountDTO> userMBOrderInfoCountDTOS = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        // 待付款
        map.put("frontOrderStatus", FrontOrderStatus.WAIT_PAYMENT);
        Integer waitPayment = orderInfoDOMapper.listUserMBDistributorOrderByParams(map).size();
        userMBOrderInfoCountDTOS
            .add(new UserMBOrderInfoCountDTO("waitPayment", FrontOrderStatus.WAIT_PAYMENT, waitPayment));
        // 待确认
        map.put("frontOrderStatus", FrontOrderStatus.WAIT_CONFIRM);
        Integer waitConfirm = orderInfoDOMapper.listUserMBDistributorOrderByParams(map).size();
        userMBOrderInfoCountDTOS
            .add(new UserMBOrderInfoCountDTO("waitSendGoods", FrontOrderStatus.WAIT_CONFIRM, waitConfirm));
        // 待发货
        map.put("frontOrderStatus", FrontOrderStatus.WAIT_SEND_GOODS);
        Integer waitSendGoods = orderInfoDOMapper.listUserMBDistributorOrderByParams(map).size();
        userMBOrderInfoCountDTOS
            .add(new UserMBOrderInfoCountDTO("waitSendGoods", FrontOrderStatus.WAIT_SEND_GOODS, waitSendGoods));
        // 部分发货
        map.put("frontOrderStatus", FrontOrderStatus.WAIT_PART_SEND_GOODS);
        Integer waitPartSendGoods = orderInfoDOMapper.listUserMBDistributorOrderByParams(map).size();
        userMBOrderInfoCountDTOS.add(
            new UserMBOrderInfoCountDTO("waitPartSendGoods", FrontOrderStatus.WAIT_PART_SEND_GOODS, waitPartSendGoods));
        // 已发货
        map.put("frontOrderStatus", FrontOrderStatus.WAIT_RECEIVE_GOODS);
        Integer waitReceiveGoods = orderInfoDOMapper.listUserMBDistributorOrderByParams(map).size();
        userMBOrderInfoCountDTOS.add(
            new UserMBOrderInfoCountDTO("waitReceiveGoods", FrontOrderStatus.WAIT_RECEIVE_GOODS, waitReceiveGoods));
        return userMBOrderInfoCountDTOS;
    }

    /**
     * C端获取订单失效时间（单位分钟）
     */
    public Integer getLoseTime() {
        Integer time = 30;
        if (orderConfig.getOrderTimerPowerOffDLevel().equals(OrderTimer.Timer1.getLevel())) {
            time = OrderTimer.Timer1.getValue();
        } else if (orderConfig.getOrderTimerPowerOffDLevel().equals(OrderTimer.Timer2.getLevel())) {
            time = OrderTimer.Timer2.getValue();
        } else if (orderConfig.getOrderTimerPowerOffDLevel().equals(OrderTimer.Timer3.getLevel())) {
            time = OrderTimer.Timer3.getValue();
        } else if (orderConfig.getOrderTimerPowerOffDLevel().equals(OrderTimer.Timer4.getLevel())) {
            time = OrderTimer.Timer4.getValue();
        } else if (orderConfig.getOrderTimerPowerOffDLevel().equals(OrderTimer.Timer5.getLevel())) {
            time = OrderTimer.Timer5.getValue();
        }
        return time;
    }

    /**
     * <h2>根据商品id计算出里面不可视的商品</h2>
     * 
     * @param userId
     *            用户id
     * @param orderGoodsList
     *            用户下单的商品id
     * @return
     */
    public List<Map<Integer, Boolean>> userProductVisibility(Integer userId, List<Integer> orderGoodsList) {
        // 查询分销商的所有可视与不可视商品、品牌
        DistributorGoodsControlRpcDTO goodsControlRpcDTO = orderRpcExe.distributorGoodsControl(userId);

        // 根据订单商品id查询出商品的可视范围和品牌
        List<GoodsAreVisibleRpcDTO> goodsAreVisibleRpcDTOS = orderRpcExe.goodsAreVisible(orderGoodsList);

        // 默认可视的商品，可视范围跟品牌可视走
        List<GoodsAreVisibleRpcDTO> brandVisibility =
            goodsAreVisibleRpcDTOS.stream().filter(x -> x.getDistributorScope() == 0).collect(Collectors.toList());

        // 非默认可视的商品，就是商品的可视
        List<GoodsAreVisibleRpcDTO> goodsAreVisible =
            goodsAreVisibleRpcDTOS.stream().filter(x -> x.getDistributorScope() != 0).collect(Collectors.toList());

        List<Map<Integer, Boolean>> visibleResult = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(goodsAreVisible)) {
            // 以商品可视为准则的 用户可视商品=全部可视商品+可视商品-不可视商品
            List<Integer> goodsIdList = new ArrayList<>();
            // 相加
            goodsIdList.addAll(goodsControlRpcDTO.getAllVisibleProducts());
            goodsIdList.addAll(goodsControlRpcDTO.getGoodsIds());
            // 相减
            goodsIdList.removeAll(goodsControlRpcDTO.getNoGoodsIds());

            checkList(goodsAreVisible, goodsIdList, 1, visibleResult);
        }

        if (ObjectUtils.isNotEmpty(brandVisibility)) {
            // 以品牌准则的可视 用户可视品牌=全部可视品牌+可视品牌-不可视品牌
            List<Integer> brandIdList = new ArrayList<>();
            // 相加
            brandIdList.addAll(goodsControlRpcDTO.getAllVisibleBrands());
            brandIdList.addAll(goodsControlRpcDTO.getBrandIds());
            // 相减
            brandIdList.removeAll(goodsControlRpcDTO.getNoBrandIds());

            checkList(brandVisibility, brandIdList, 2, visibleResult);
        }
        return visibleResult;
    }

    /**
     * <h2>判断是否有商品不可视</h2>
     * 
     * @param orderGoodsList
     *            订单商品信息
     * @param userVisibleList
     *            用户可视列表
     * @param type
     *            1商品，2品牌
     * @return
     */
    private void checkList(List<GoodsAreVisibleRpcDTO> orderGoodsList, List<Integer> userVisibleList, Integer type,
        List<Map<Integer, Boolean>> visibleResult) {

        // 商品
        if (type == 1) {
            for (int i = 0; i < orderGoodsList.size(); i++) {
                if (!userVisibleList.contains(orderGoodsList.get(i).getGoodsId())) {
                    Map<Integer, Boolean> map = new HashMap<>();
                    // false不可视
                    map.put(orderGoodsList.get(i).getGoodsId(), false);
                    visibleResult.add(map);
                }
            }
        }

        // 品牌
        if (type == 2) {
            for (int i = 0; i < orderGoodsList.size(); i++) {
                if (!userVisibleList.contains(orderGoodsList.get(i).getBrandId())) {
                    Map<Integer, Boolean> map = new HashMap<>();
                    // false不可视
                    map.put(orderGoodsList.get(i).getGoodsId(), false);
                    visibleResult.add(map);
                }
            }
        }
    }
}
