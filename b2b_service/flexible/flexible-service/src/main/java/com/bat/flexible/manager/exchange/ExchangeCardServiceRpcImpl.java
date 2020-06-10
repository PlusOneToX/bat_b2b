package com.bat.flexible.manager.exchange;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.common.constant.exchange.*;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCardCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCardQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeShareQryExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeInvalidLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeOutboundRestoreLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeSyncBackLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeSyncBackLogQryExe;
import com.bat.flexible.manager.exchange.validator.ExchangeCodeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.common.BaseOpenApiException;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.common.ResponseBaseBean;
import com.bat.dubboapi.flexible.exchange.ExchangeCardServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.*;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.api.OrderDistributorDataServiceRpc;
import com.bat.dubboapi.order.order.api.OrderGoodsDubboServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsRpcDTO;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.*;
import com.bat.flexible.manager.common.constant.order.OrderServiceConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@DubboService
public class ExchangeCardServiceRpcImpl implements ExchangeCardServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCardServiceRpcImpl.class);

    @Autowired
    private ExchangeCardCmdExe exchangeCardCmdExe;

    @Autowired
    private ExchangeCardQryExe exchangeCardQryExe;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeCodeOutboundRestoreLogCmdExe exchangeCodeOutboundRestoreLogCmdExe;

    @Autowired
    private ExchangeMaterialRelevanceServiceI exchangeMaterialRelevanceServiceI;

    @Autowired
    private ExchangeModelRelevanceServiceI exchangeModelRelevanceServiceI;

    @Autowired
    private ExchangePictureRelevanceServiceI exchangePictureRelevanceServiceI;

    @Autowired
    private ExchangeRefundOrderServiceI exchangeRefundOrderServiceI;

    @Autowired
    private ExchangeCodeInvalidLogCmdExe exchangeCodeInvalidLogCmdExe;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @Autowired
    private ExchangeCodeSyncBackLogQryExe exchangeCodeSyncBackLogQryExe;

    @Autowired
    private ExchangeCodeSyncBackLogCmdExe exchangeCodeSyncBackLogCmdExe;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @DubboReference(check = false, timeout = 500000)
    private OrderDistributorDataServiceRpc orderDistributorDataServiceRpc;

    @DubboReference(check = false, timeout = 500000, retries = 0)
    private OrderGoodsDubboServiceRpc orderGoodsDubboServiceRpc;

    @Autowired
    private ExchangeCardServiceI exchangeCardServiceI;

    @Autowired
    private ExchangeShareQryExe exchangeShareQryExe;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;



    @Transactional
    @Override
    public ResponseBaseBean bindOrderAndBoxCode(OrderExchangeCardBindErpRequest orderExchangeCardBindErpRequest) {
        try {
            // 兑换卡数量、key兑换卡id、value数量
            Map<Integer, Integer> map = new HashMap<>();
            // 修改的兑换卡数量（删除还原）、 key兑换卡id value数量
            Map<Integer, Integer> updateMap = new HashMap<>();
            // 判断出库单是首次新增还是编辑
            // List<Order> outboundNoOrderList =
            // exchangeCodeDataManager.listByOutboundNoGroupByDistributorOrderId(orderExchangeCardBindErpRequest.getOutboundNo());
            List<OrderExtendDataSimpleRpcDTO> outboundNoOrderList = exchangeCodeQryExe
                .listByOutboundNoGroupByDistributorOrderId(orderExchangeCardBindErpRequest.getOutboundNo());
            if (outboundNoOrderList != null && outboundNoOrderList.size() > 0
                && !orderExchangeCardBindErpRequest.getIsUpdate()) {
                LOGGER.error("该出库单" + orderExchangeCardBindErpRequest.getOutboundNo() + "不属于首次出库盒码、原来已出库");
                throw new BaseOpenApiException(ExchangeErpErrorConstant.OutboundNotBelongFirst.getCode(),
                    ExchangeErpErrorConstant.OutboundNotBelongFirst.getMsg());
            }
            if ((outboundNoOrderList == null || outboundNoOrderList.size() == 0)
                && orderExchangeCardBindErpRequest.getIsUpdate()) {
                LOGGER.error("该出库单" + orderExchangeCardBindErpRequest.getOutboundNo() + "属于首次出库盒码、原来没有出库过");
                throw new BaseOpenApiException(ExchangeErpErrorConstant.OutboundBelongFirst.getCode(),
                    ExchangeErpErrorConstant.OutboundBelongFirst.getMsg());
            }
            // 判断盒码是否跨订单混用
            Map<String, List<String>> allBoxMap =
                checkBoxCodeMoreThenOneOrder(orderExchangeCardBindErpRequest.getItemCodeList());
            // 存储之前的所有订单盒码列表
            List<ItemBoxCodeRequest> allList = new ArrayList<>();
            for (int x = 0; x < orderExchangeCardBindErpRequest.getItemCodeList().size(); x++) {
                ItemBoxCodeRequest boxCodeRequest = new ItemBoxCodeRequest();
                ItemBoxCodeRequest request = orderExchangeCardBindErpRequest.getItemCodeList().get(x);
                BeanCopier beanCopier = BeanCopier.create(ItemBoxCodeRequest.class, ItemBoxCodeRequest.class, false);
                beanCopier.copy(request, boxCodeRequest, null);
                allList.add(boxCodeRequest);
            }

            // 判断修改时订单号是否一样
            if (orderExchangeCardBindErpRequest.getIsUpdate()) {

                List<ItemBoxCodeRequest> itemCodeList = orderExchangeCardBindErpRequest.getItemCodeList();
                // 判断有没有新增订单号（同一个发货单、不允许新增订单号）
                for (int x = 0; x < itemCodeList.size(); x++) {
                    Boolean isNew = true;
                    for (int y = 0; y < outboundNoOrderList.size(); y++) {
                        if (itemCodeList.get(x).getOrderNo().equals(outboundNoOrderList.get(y).getOrderErpNo())) {
                            // 订单号一样(ERP单号、不是B2B单号)
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        LOGGER.error("出库单：" + orderExchangeCardBindErpRequest.getOutboundNo() + "修改绑定盒码时、新加了订单号"
                            + itemCodeList.get(x).getOrderNo() + "，导致错误");
                        String msg = MessageUtils.get(ExchangeErpErrorConstant.OutboundNoNotHaveThisOrder.getMsg())
                            + "【" + itemCodeList.get(x).getOrderNo() + "】";
                        throw new BaseOpenApiException(ExchangeErpErrorConstant.OutboundNoNotHaveThisOrder.getCode(),
                            msg);
                    }
                }
                // 判断有没有少订单号（同一个发货单、不允许少订单号）
                for (int x = 0; x < outboundNoOrderList.size(); x++) {
                    Boolean isDelete = true;
                    for (int y = 0; y < itemCodeList.size(); y++) {
                        // ERP单号匹配
                        if (outboundNoOrderList.get(x).getOrderErpNo().equals(itemCodeList.get(y).getOrderNo())) {
                            // 订单号一样
                            isDelete = false;
                            break;
                        }
                    }
                    if (isDelete) {
                        LOGGER.error("出库单：" + orderExchangeCardBindErpRequest.getOutboundNo() + "修改绑定盒码时、少了订单号"
                            + outboundNoOrderList.get(x).getOrderErpNo() + "，导致错误");
                        String msg = ExchangeErpErrorConstant.OutboundNoHaveThisOrder.getMsg() + "【"
                            + outboundNoOrderList.get(x).getOrderErpNo() + "】";
                        throw new BaseOpenApiException(ExchangeErpErrorConstant.OutboundNoHaveThisOrder.getCode(), msg);
                    }
                }
                // 查询已发货的盒码、按照盒码分组
                List<ExchangeCodeDO> codeList = exchangeCodeServiceI
                    .listByOutboundNoGroupByBoxCode(orderExchangeCardBindErpRequest.getOutboundNo());
                // 剔除盒码不变的
                for (int x = 0; x < itemCodeList.size(); x++) {
                    List<String> boxCodeListList = itemCodeList.get(x).getBoxCodeList();
                    for (int y = 0; y < boxCodeListList.size(); y++) {
                        if (codeList != null && codeList.size() > 0) {
                            for (int z = 0; z < codeList.size(); z++) {
                                if (boxCodeListList.get(y).equals(codeList.get(z).getBoxCode())) {
                                    // 盒码一样、不做修改
                                    boxCodeListList.remove(y);
                                    codeList.remove(z);
                                    y--;
                                    z--;
                                    break;
                                }
                            }
                        }
                    }
                    // 判断是否发错了盒码、需要重置为空
                    if (boxCodeListList == null || boxCodeListList.size() == 0) {
                        // 该订单盒码没有变化、剔除
                        itemCodeList.remove(x);
                        x--;
                    }
                }
                // 判断之前有没有发多了、需要重置为null
                if (codeList != null && codeList.size() > 0) {
                    for (int x = 0; x < codeList.size(); x++) {
                        // 判断之前有没有发错盒码
                        Boolean isError = true;
                        if (itemCodeList != null && itemCodeList.size() > 0) {
                            for (int y = 0; y < itemCodeList.size(); y++) {
                                List<String> boxCodeList = itemCodeList.get(y).getBoxCodeList();
                                for (int z = 0; z < boxCodeList.size(); z++) {
                                    if (codeList.get(x).getBoxCode().equals(boxCodeList.get(z))) {
                                        // 没有发错
                                        isError = false;
                                    }
                                }
                            }
                        }
                        if (isError) {
                            // 重置为空
                            restoreOrderBoxCodeRela(codeList.get(x).getBoxCode(), updateMap,
                                orderExchangeCardBindErpRequest.getOutboundNo());
                        }
                    }
                }
                // 判断前后修改是否一样
                if ((itemCodeList == null || itemCodeList.size() == 0) && (codeList == null || codeList.size() == 0)) {
                    LOGGER.info("erp出库、绑定分销商订单和盒码关系：原因是前后盒码没有变化");
                    return ResponseBaseBean.responseBean();
                }
            }
            // 判断一盒一张卡片的是否连续(只判断一个出库单一个订单的)、暂时不做校验
            // checkBoxCodeIsContinuous(orderExchangeCardBindErpRequest.getItemCodeList(),orderExchangeCardBindErpRequest.getIsUpdate(),allBoxMap);
            LOGGER.info("开始处理出库单、绑定分销商订单和盒码关系、处理后参数为：" + JSON.toJSONString(orderExchangeCardBindErpRequest));
            // 需要对同一个销售单、同一个物料、进行排序、盒码长度短在前
            Collections.sort(orderExchangeCardBindErpRequest.getItemCodeList(), new Comparator<ItemBoxCodeRequest>() {
                @Override
                public int compare(ItemBoxCodeRequest o1, ItemBoxCodeRequest o2) {
                    String o1Str = o1.getOrderNo() + "_" + o1.getItemCode();
                    String o2Str = o2.getOrderNo() + "_" + o2.getItemCode();
                    if (o1Str.compareTo(o2Str) == 0) {
                        return o1.getBoxCodeList().size() - o2.getBoxCodeList().size();
                    }
                    return o1Str.compareTo(o2Str);
                }
            });
            // 定义一个map、订单可能会出现购买TPU兑换卡、同时赠送TPU的兑换卡、
            Map<Integer, Integer> orderGoodsMap = new HashMap<>();
            // 分销商和货品id是否是兑换卡 key为分销商id_货品id true是兑换卡、false不是兑换卡
            Map<String, Boolean> distributorExchangeMap = new HashMap<>();
            orderExchangeCardBindErpRequest.getItemCodeList().stream().forEach(itemBoxCodeRequest -> {
                LOGGER.info("查询分销商订单数据列表信息,{}", itemBoxCodeRequest.getOrderNo());
                com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> orderErpNoResp =
                    orderDistributorDataServiceRpc.getByOrderErpNoAndErpFlag(itemBoxCodeRequest.getOrderNo(),
                        OrderServiceConstant.ORDER_ERP_FLAG_YES);
                LOGGER.info("查询分销商订单数据列表信息,返回{}", JSON.toJSONString(orderErpNoResp));
                if (orderErpNoResp == null || !orderErpNoResp.isSuccess()) {
                    throw FlexibleDubboApiException
                        .buildException(FlexibleDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
                }
                if (!orderErpNoResp.isSuccess()) {
                    throw FlexibleDubboApiException.buildException(orderErpNoResp.getErrCode(),
                        orderErpNoResp.getErrMessage());
                }
                List<OrderDistributorDataRpcDTO> orderDistributorDataRpcDTOList = orderErpNoResp.getData();
                if (orderDistributorDataRpcDTOList == null || orderDistributorDataRpcDTOList.size() == 0) {
                    throw FlexibleDubboApiException.buildException("ERP订单号错误");
                }
                OrderDistributorDataRpcDTO orderDistributorDataRpcDTO = orderDistributorDataRpcDTOList.get(0);
                if (orderDistributorDataRpcDTO == null) {
                    throw FlexibleDubboApiException
                        .buildException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_ORDER)
                            + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
                }
                LOGGER.info("查询订单商品明细列表信息,{}", orderDistributorDataRpcDTO.getOrderId());
                com.bat.dubboapi.order.common.Response<List<OrderGoodsRpcDTO>> orderGoodsResponse =
                    orderGoodsDubboServiceRpc.listOrderGoodsByOrderId(orderDistributorDataRpcDTO.getOrderId());
                LOGGER.info("查询订单商品明细列表信息、返回{}", JSON.toJSONString(orderGoodsResponse));
                if (orderGoodsResponse == null || !orderGoodsResponse.isSuccess()) {
                    throw FlexibleDubboApiException.buildException("访问订单服务异常");
                }
                List<OrderGoodsRpcDTO> orderGoodsList = orderGoodsResponse.getData();
                if (orderGoodsList == null || orderGoodsList.size() == 0) {
                    throw new BaseOpenApiException(112, "找不到订单明细");
                }
                // 对同一个订单、同一个货品（多个明细）进行排序、按照下单数量递增
                Collections.sort(orderGoodsList, new Comparator<OrderGoodsRpcDTO>() {
                    @Override
                    public int compare(OrderGoodsRpcDTO o1, OrderGoodsRpcDTO o2) {
                        if (o1.getItemId() - o2.getItemId() == 0) {
                            return o1.getItemCount().intValue() - o2.getItemCount().intValue();
                        }
                        return o1.getItemId().intValue() - o2.getItemId().intValue();
                    }
                });
                LOGGER.info("订单兑换卡明细排序后:{}", JSON.toJSONString(orderGoodsList));
                // 判断物料编号
                /*GoodsItem goodsItem = goodsItemDBManager.findByItemCode(itemBoxCodeRequest.getItemCode());
                if(goodsItem==null){
                    throw new BaseOpenApiException(GoodsErrorConstant.ItemCodeErrorCode,itemBoxCodeRequest.getItemCode()+GoodsErrorConstant.ItemCodeErrorMsg);
                }*/
                // 移除已经判断的orderGoods
                for (int x = 0; x < orderGoodsList.size(); x++) {
                    if (orderGoodsMap.containsKey(orderGoodsList.get(x).getId())) {
                        LOGGER.info("已处理了兑换码绑定明细,{}",
                            JSON.toJSONString(orderGoodsMap) + "===" + orderGoodsList.get(x).getId());
                        // 已经处理了
                        orderGoodsList.remove(x);
                        x--;
                    }
                }
                LOGGER.info("订单兑换卡明细移除已绑定的:{}", JSON.toJSONString(orderGoodsList) + ",orderGoodsMap,{}",
                    JSON.toJSONString(orderGoodsMap));

                AtomicReference<Boolean> flag = new AtomicReference<>(false);
                if (orderGoodsList != null && orderGoodsList.size() > 0) {

                    for (int z = 0; z < orderGoodsList.size(); z++) {
                        // ERP出库按照传过去的明细进行出库
                        OrderGoodsRpcDTO orderGoodsRpcDTO = orderGoodsList.get(z);
                        // 需要判断货品id一致、且下单数量要大于等于盒码数量（同一个销售单可能有两个一样的80码）
                        if (orderGoodsRpcDTO.getItemCode().equals(itemBoxCodeRequest.getItemCode())) {
                            // 加到已处理集合、下一次会移除orderGoods
                            orderGoodsMap.put(orderGoodsRpcDTO.getId(), orderGoodsRpcDTO.getId());

                            // 是否修改操作、只还原之前订单绑定的盒码一次
                            AtomicReference<Boolean> isFirstUpdate = new AtomicReference<>(true);
                            // 判断物料是否是兑换卡
                            Boolean exchangeFlag = distributorExchangeMap.get(
                                orderDistributorDataRpcDTO.getDistributorId() + "_" + orderGoodsRpcDTO.getItemId());
                            if (exchangeFlag == null) {
                                ExchangeCardDO exchangeCardDO =
                                    exchangeCardServiceI.checkIsExchangeByItemIdAndDistributorId(
                                        orderGoodsRpcDTO.getItemId(), orderDistributorDataRpcDTO.getDistributorId());
                                exchangeFlag = exchangeCardDO != null ? true : false;
                                distributorExchangeMap.put(
                                    orderDistributorDataRpcDTO.getDistributorId() + "_" + orderGoodsRpcDTO.getItemId(),
                                    exchangeFlag);
                            }
                            if (!exchangeFlag) {
                                // 不属于兑换卡活动、或者分销商没有这个兑换卡活动权限
                                throw new BaseOpenApiException(
                                    ExchangeErrorConstant.ItemNotBelongExchangeError.getCode(),
                                    ExchangeErrorConstant.ItemNotBelongExchangeError.getMsg());
                            }
                            // 判断盒码状态是否是已经使用了
                            itemBoxCodeRequest.getBoxCodeList().stream().forEach(boxCode -> {
                                List<ExchangeCodeDO> codeList = exchangeCodeServiceI.listByBoxCode(boxCode);
                                if (codeList == null || codeList.size() == 0) {
                                    throw new BaseOpenApiException(ExchangeErrorConstant.BoxCodeError.getCode(),
                                        "【" + boxCode + "】" + ExchangeErrorConstant.BoxCodeError.getMsg());
                                }
                                // 判断对应的货品编码是与兑换卡的一致
                                ExchangeCardDO card = exchangeCardQryExe.getById(codeList.get(0).getExchangeId());
                                if (orderGoodsRpcDTO.getItemId() - card.getItemId() != 0) {
                                    String msg = "发货的盒码【" + boxCode + "】"
                                        + ExchangeErpErrorConstant.BoxCodeRelaItemCodeError.getMsg();
                                    throw new BaseOpenApiException(
                                        ExchangeErpErrorConstant.BoxCodeRelaItemCodeError.getCode(), msg);
                                }
                                // 定义变量
                                Boolean boxUpdateExist = true;
                                codeList.stream().forEach(exchangeCode -> {
                                    // 校验兑换码的状态是否属于未激活状态
                                    if (orderExchangeCardBindErpRequest.getIsUpdate()) {
                                        // 修改盒码
                                    } else {
                                        // 首次设置盒码和订单关系
                                        if (exchangeCode.getStatus() - ExchangeCodeConstant.StatusInit != 0) {
                                            String msg = "盒码【" + boxCode + "】中的" + exchangeCode.getPlainCode()
                                                + ExchangeErrorConstant.ExchangeCodeStatusNotInitError.getMsg();
                                            throw new BaseOpenApiException(
                                                ExchangeErrorConstant.ExchangeCodeStatusNotInitError.getCode(), msg);
                                        }
                                    }
                                });
                                codeList.stream().forEach(exchangeCode -> {
                                    if (exchangeCode.getDistributorOrderId() != null
                                        && orderDistributorDataRpcDTO.getOrderId()
                                            - exchangeCode.getDistributorOrderId() != 0) {
                                        LOGGER.error(boxCode + "关联订单失败、因为该盒码已分配");
                                        throw new BaseOpenApiException(
                                            ExchangeErpErrorConstant.BoxCodeRelaOrderFailByAlready.getCode(),
                                            boxCode + ExchangeErpErrorConstant.BoxCodeRelaOrderFailByAlready.getMsg());
                                    }
                                    if (exchangeCode.getStatus() - ExchangeCodeConstant.StatusUsed == 0) {
                                        LOGGER.error(boxCode + "激活盒码失败、因为该盒码已使用");
                                        throw new BaseOpenApiException(
                                            ExchangeErpErrorConstant.BoxCodeUpdateFailByUsed.getCode(),
                                            boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByUsed.getMsg());
                                    }
                                    if (exchangeCode.getStatus() - ExchangeCodeConstant.StatusEnd == 0) {
                                        LOGGER.error(boxCode + "激活盒码失败、因为该盒码已过期");
                                        throw new BaseOpenApiException(
                                            ExchangeErpErrorConstant.BoxCodeUpdateFailByEnd.getCode(),
                                            boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByEnd.getMsg());
                                    }
                                    if (exchangeCode.getStatus() - ExchangeCodeConstant.StatusInvalid == 0) {
                                        LOGGER.error(boxCode + "激活盒码失败、因为该盒码已作废");
                                        throw new BaseOpenApiException(
                                            ExchangeErpErrorConstant.BoxCodeUpdateFailByInvalid.getCode(),
                                            boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByInvalid.getMsg());
                                    }

                                    if (exchangeCode.getUserOrderId() != null) {
                                        LOGGER.error(boxCode + "激活盒码失败、因为该盒码已核销");
                                        throw new BaseOpenApiException(
                                            ExchangeErpErrorConstant.BoxCodeUpdateFailByUsed.getCode(),
                                            boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByUsed.getMsg());
                                    }
                                    exchangeCode.setStatus(ExchangeCodeConstant.StatusUnUse);
                                    exchangeCode.setDistributorOrderId(orderDistributorDataRpcDTO.getOrderId());
                                    exchangeCode.setDistributorId(orderDistributorDataRpcDTO.getDistributorId());
                                    exchangeCode.setDistributorName(orderDistributorDataRpcDTO.getDistributorName());
                                    exchangeCode.setDistributorCompanyName(orderDistributorDataRpcDTO.getCompanyName());
                                    exchangeCode.setDistributorOrderGoodsId(orderGoodsRpcDTO.getId());
                                    exchangeCode.setOutboundNo(orderExchangeCardBindErpRequest.getOutboundNo());
                                    exchangeCodeServiceI.update(exchangeCode);
                                    Integer count = map.get(exchangeCode.getExchangeId());
                                    if (count == null) {
                                        count = 0;
                                    }
                                    count++;
                                    // 设置数量
                                    map.put(exchangeCode.getExchangeId(), count);
                                });
                            });
                            flag.set(true);
                            // 跳出循环、ERP出库是明细对明细
                            break;
                        }
                    }
                    if (!flag.get()) {
                        throw new BaseOpenApiException(ExchangeErpErrorConstant.ItemNotBelongExchangeError.getCode(),
                            itemBoxCodeRequest.getItemCode()
                                + ExchangeErpErrorConstant.ItemNotBelongExchangeError.getMsg());
                    }

                }
            });
            if (map != null && map.size() > 0) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>)iterator.next();
                    ExchangeCardDO exchangeCard = exchangeCardQryExe.getById(entry.getKey());
                    exchangeCard.setSaleQuantity(exchangeCard.getSaleQuantity() == null ? entry.getValue()
                        : (exchangeCard.getSaleQuantity() + entry.getValue()));
                    if (orderExchangeCardBindErpRequest.getIsUpdate()) {
                        Integer restoreCount = updateMap.get(entry.getKey());
                        if (restoreCount != null && restoreCount > 0) {
                            // 减去还原的数量
                            exchangeCard.setSaleQuantity(exchangeCard.getSaleQuantity() - restoreCount);
                        }
                    }
                    exchangeCardCmdExe.update(exchangeCard);
                }
            }
            LOGGER.info("盒码绑定订单处理完毕、返回ERP：本次绑定参数{}", JSON.toJSONString(orderExchangeCardBindErpRequest));
            return ResponseBaseBean.responseBean();
        } catch (BaseOpenApiException e) {
            e.printStackTrace();
            LOGGER.error("ERP出库、绑定盒码和订单关系异常,{}", e.getMsg());
            String errMsg = e.getMsg();
            if (StringUtils.isNotBlank(MessageUtils.get(errMsg))) {
                errMsg = MessageUtils.get(errMsg);
            }
            return ResponseBaseBean.responseBean(e.getCode(), errMsg);
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            LOGGER.error("ERP出库、绑定盒码和订单关系异常,{}", e.getMsg());
            String errMsg = e.getMsg();
            if (StringUtils.isNotBlank(MessageUtils.get(errMsg))) {
                errMsg = MessageUtils.get(errMsg);
            }
            return ResponseBaseBean.responseBean(10002, errMsg);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ERP出库、绑定盒码和订单关系系统异常,{}", e.getMessage());
            return ResponseBaseBean.responseBean(10001, "ERP出库、绑定盒码和订单关系系统异常");
        }

    }

    /**
     * 退款作废
     * 
     * @param exchangeCodeRefundDTO
     * @return
     */
    @Override
    @Transactional
    public ResponseBaseBean refund(ExchangeCodeRefundDTO exchangeCodeRefundDTO) {

        try {
            ExchangeRefundOrderDO exchangeRefundOrder =
                exchangeRefundOrderServiceI.getByRefundNo(exchangeCodeRefundDTO.getRefundNo());
            if (exchangeRefundOrder != null) {
                throw new BaseOpenApiException(ExchangeErrorConstant.ExchangeRefundNoExist.getCode(),
                    ExchangeErrorConstant.ExchangeRefundNoExist.getMsg());
            } else {
                exchangeRefundOrder = new ExchangeRefundOrderDO();
            }
            exchangeRefundOrder.setReason(exchangeCodeRefundDTO.getRefundReason());
            exchangeRefundOrder.setCreateUserName(exchangeCodeRefundDTO.getOperatorName());
            exchangeRefundOrder.setRefundNo(exchangeCodeRefundDTO.getRefundNo());
            exchangeRefundOrder.setCreateTime(new Date());
            exchangeRefundOrder.setType(ExchangeConstant.RefundOrderTypeERP);
            exchangeRefundOrderServiceI.create(exchangeRefundOrder);
            Integer exchangeRefundOrderId = exchangeRefundOrder.getId();
            List<ExchangeBoxPlainCodeDTO> boxPlainCodeDTOList = exchangeCodeRefundDTO.getCodeDTOList();
            boxPlainCodeDTOList.stream().forEach(exchangeBoxPlainCodeDTO -> {
                List<String> plainCodeList = exchangeBoxPlainCodeDTO.getPlainCodeList();
                List<ExchangeCodeDO> codeList = exchangeCodeServiceI
                    .listByBoxCodeAndPlainCodeList(exchangeBoxPlainCodeDTO.getBoxCode(), plainCodeList);
                if (codeList == null || codeList.size() == 0) {
                    throw new BaseOpenApiException(ExchangeErrorConstant.ExchangeCodeError.getCode(),
                        ExchangeErrorConstant.ExchangeCodeError.getMsg());
                }
                // 校验明码、如果传进来的明码列表不为空
                checkPlainCode(plainCodeList, codeList, exchangeBoxPlainCodeDTO.getBoxCode());
                codeList.stream().forEach(exchangeCode -> {
                    if (exchangeCode.getStatus() - ExchangeCodeConstant.StatusUnUse == 0) {
                        exchangeCode.setStatus(ExchangeCodeConstant.StatusInvalid);
                        exchangeCodeServiceI.update(exchangeCode);
                        // 生成记录
                        saveInvalidLog(exchangeCodeRefundDTO, exchangeRefundOrderId, exchangeCode,
                            ExchangeConstant.ExchangeCodeInvalidTypeERP, null);
                    }
                });
            });
            return ResponseBaseBean.responseBean();
        } catch (BaseOpenApiException e) {
            e.printStackTrace();
            LOGGER.error("erp兑换码退款作废失败{}", e.getMessage());
            return ResponseBaseBean.responseBean(ExchangeErrorConstant.ExchangeCardRefundFailError.getCode(),
                e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("erp兑换码退款作废系统异常", e.getMessage());
            return ResponseBaseBean.responseBean(ExchangeErrorConstant.ExchangeCardRefundFailError.getCode(),
                ExchangeErrorConstant.ExchangeCardRefundFailError.getMsg());
        }
    }

    /**
     * 校验柔性下单的兑换码参数合法性
     * 
     * @param exchangeCodeOrderDTOList
     * @return
     */
    @Override
    public Response<List<ExchangeCodeSimpleDTO>> checkRxExchange(List<ExchangeCodeOrderDTO> exchangeCodeOrderDTOList,
        Short shareFlag) {
        List<ExchangeCodeSimpleDTO> list = new ArrayList<>();
        ExchangeShareDO exchangeShareDO = null;
        try {
            if (shareFlag != null && shareFlag == 1) {
                // 查看是否有合适的减免活动
                exchangeShareDO = exchangeShareQryExe.findSuitable(ExchangeConstant.EXCHANGE_ACTIVITY_PLATFORM_EX,
                    ExchangeConstant.EXCHANGE_SEAT_ORDER);
                LOGGER.info("查找减免活动返回:{}", JSONObject.toJSONString(exchangeShareDO));
            }
            // 可以减免的分销商id
            List<Integer> reductionDistributorIds = new ArrayList<>();
            for (int x = 0; x < exchangeCodeOrderDTOList.size(); x++) {
                ExchangeCodeOrderDTO exchangeCodeOrderDTO = exchangeCodeOrderDTOList.get(x);
                List<String> secortCodeList = exchangeCodeOrderDTO.getSecretCodeList();

                if (exchangeShareDO != null) {
                    reductionDistributorIds = getReductionDistributorIds(exchangeShareDO, secortCodeList);
                }

                for (int y = 0; y < secortCodeList.size(); y++) {
                    String secortCode = secortCodeList.get(y);
                    LOGGER.info("校验暗码{}", secortCode);
                    ExchangeCodeDO exchangeCodeDO = exchangeCodeServiceI.findByPlainCodeAndSecretCode(null, secortCode);
                    // 校验兑换码状态是否可用
                    ExchangeCodeValidator.validExchangeCodeStatusWhenUserUse(exchangeCodeDO, secortCode);
                    ExchangeCardDO exchangeCardDO = exchangeCardQryExe.getById(exchangeCodeDO.getExchangeId());
                    if (exchangeCardDO.getStatus() != ExchangeConstant.StatusStarting) {
                        return Response.buildFailure(
                            ExchangeRxErrorConstant.ExchangeFailByCardStatusNoStarting.getCode() + "",
                            ExchangeRxErrorConstant.ExchangeFailByCardStatusNoStarting.getMsg());
                    }
                    ExchangeMaterialRelevanceDO exchangeMaterialRelevanceDO = exchangeMaterialRelevanceServiceI
                        .findByExchangeIdAndMaterialId(exchangeCardDO.getId(), exchangeCodeOrderDTO.getMaterialId());
                    if (exchangeMaterialRelevanceDO == null) {
                        return Response.buildFailure(ExchangeRxErrorConstant.MaterialIdError.getCode() + "",
                            ExchangeRxErrorConstant.MaterialIdError.getMsg());
                    }
                    ExchangeModelRelevanceDO exchangeModelRela =
                        exchangeModelRelevanceServiceI.findOneByExchangeIdAndModelId(exchangeCardDO.getId(), null);
                    if (exchangeModelRela == null) {
                        if (exchangeCodeOrderDTO.getModelId() == null || exchangeCodeOrderDTO.getModelId() == 0) {
                            return Response.buildFailure(ExchangeRxErrorConstant.ModelRelaNullError.getCode() + "",
                                ExchangeRxErrorConstant.ModelRelaNullError.getMsg());
                        }
                    }
                    // 部分可用
                    if (exchangeModelRela.getType() - ExchangeConstant.ExchangeModelUserTypeSome == 0) {
                        exchangeModelRela = exchangeModelRelevanceServiceI
                            .findOneByExchangeIdAndModelId(exchangeCardDO.getId(), exchangeCodeOrderDTO.getModelId());
                        if (exchangeModelRela == null) {
                            return Response.buildFailure(ExchangeRxErrorConstant.ModelIdError.getCode() + "",
                                ExchangeRxErrorConstant.ModelIdError.getMsg());
                        }
                    }
                    if (exchangeCodeOrderDTO.getPictureId() != null && exchangeCodeOrderDTO.getPictureId() != 0) {
                        ExchangePictureRelevanceDO rela =
                            exchangePictureRelevanceServiceI.findOneByExchangeId(exchangeCardDO.getId());
                        if (rela == null) {
                            return Response.buildFailure(ExchangeRxErrorConstant.PictureIdError.getCode() + "",
                                ExchangeRxErrorConstant.PictureIdError.getMsg());
                        }
                        if (rela.getType() - ExchangeConstant.ExchangePictureUserTypeSome == 0) {
                            rela = exchangePictureRelevanceServiceI.findByExchangeIdAndPictureId(exchangeCardDO.getId(),
                                exchangeCodeOrderDTO.getPictureId());
                            if (rela == null) {
                                return Response.buildFailure(ExchangeRxErrorConstant.PictureIdError.getCode() + "",
                                    ExchangeRxErrorConstant.PictureIdError.getMsg());
                            }
                        }
                    }
                    ExchangeCodeSimpleDTO simpleDTO = new ExchangeCodeSimpleDTO();
                    simpleDTO.setExchangeId(exchangeCardDO.getId());
                    simpleDTO.setSecretCode(secortCode);
                    simpleDTO.setB2bDistributorId(exchangeCodeDO.getDistributorId());
                    if (exchangeCardDO.getMailSetting() - ExchangeConstant.EXCHANGE_MAIL_TYPE_COLLECT_FREIGHT == 0) {
                        // 设置为默认的2601
                        // simpleDTO.setB2bDistributorId(defaultDistributorId);
                    }

                    simpleDTO.setDistributorScope(exchangeCardDO.getDistributorScope());
                    simpleDTO.setMailSetting(exchangeCardDO.getMailSetting());
                    simpleDTO.setMailFee(exchangeCardDO.getMailFee());

                    if (exchangeCardDO.getMailSetting() - ExchangeConstant.EXCHANGE_MAIL_TYPE_COLLECT_FREIGHT == 0) {
                        if (reductionDistributorIds.size() > 0) {
                            // 如果减免的分销商包含当前分销商
                            if (reductionDistributorIds.contains(exchangeCodeDO.getDistributorId())) {
                                // 重新计算邮费
                                BigDecimal mailFee =
                                    exchangeCardDO.getMailFee().subtract(exchangeShareDO.getReduceAmount());
                                if (mailFee.compareTo(BigDecimal.ZERO) < 0) {
                                    mailFee = BigDecimal.ZERO;
                                }
                                simpleDTO.setMailFee(mailFee);
                            }
                        }
                    }
                    simpleDTO.setDistributorName(exchangeCodeDO.getDistributorName());
                    simpleDTO.setDistributorCompanyName(exchangeCodeDO.getDistributorCompanyName());
                    try {
                        DistributorRpcDTO distributorRpcDTO =
                            distributorServiceRpc.distributorById(simpleDTO.getB2bDistributorId()).getData();
                        simpleDTO.setSalesId(distributorRpcDTO.getSalesId());
                        simpleDTO.setSalesName(distributorRpcDTO.getSalesName());
                    } catch (Exception e) {
                        LOGGER.info("获取业务员出现异常：{}", e);
                    }
                    list.add(simpleDTO);
                }
            }
            LOGGER.info("校验兑换码、返回：{}", JSON.toJSONString(list));
            return Response.of(list);
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(exchangeCodeOrderDTOList) + "校验暗码系统异常{}", e.getMessage());
            e.printStackTrace();
            return Response.buildFailure(ExchangeCardErrorCode.EXCHANGE_SECRET_CODE_VALID_EXCEPTION,
                MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_SECRET_CODE_VALID_EXCEPTION));
        }
    }

    /**
     * 获取可以减免的分销商
     * 
     * @param exchangeShareDO
     * @param secretCodeList
     * @return
     */
    private List<Integer> getReductionDistributorIds(ExchangeShareDO exchangeShareDO, List<String> secretCodeList) {
        List<String> secretCodes = new ArrayList<>(secretCodeList);
        List<ExchangeCodeDO> exchangeCodeDO = exchangeCodeServiceI.listBySecretCodeList(secretCodes);
        // 兑换卡支持的发卡分销商
        List<Integer> distributorIds =
            exchangeCodeDO.stream().map(ExchangeCodeDO::getDistributorId).collect(Collectors.toList());
        List<ExchangeShareDistributorDO> exchangeShareDistributorDOS;
        // 指定全部
        if (exchangeShareDO.getDistributorVisualType() == FlexibleCommonConstant.ALL_TYPE) {
            //查找出不可视的分销商
            exchangeShareDistributorDOS = exchangeShareQryExe.listByExchangeShareIdAndType(exchangeShareDO.getId(), FlexibleCommonConstant.COMMON_OPEN_FLAG_NO);
            List<Integer> disableDistributorIds = exchangeShareDistributorDOS.stream().map(ExchangeShareDistributorDO::getDistributorId).collect(Collectors.toList());
            for (Integer disableDistributorId : disableDistributorIds) {
                List<Integer> distributorTreePaths = flexibleDistributorQryExe.getDistributorTreePaths(disableDistributorId);
                //过滤掉不合适的
                distributorIds.removeIf(distributorTreePaths::contains);
            }
        }
        // 指定部分
        if (exchangeShareDO.getDistributorVisualType() == FlexibleCommonConstant.APPOINT_TYPE) {
            //查找可视烦人分销商
            exchangeShareDistributorDOS = exchangeShareQryExe.listByExchangeShareIdAndType(exchangeShareDO.getId(), FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            List<Integer> ableDistributorIds = exchangeShareDistributorDOS.stream().map(ExchangeShareDistributorDO::getDistributorId).collect(Collectors.toList());
            for (Integer ableDistributorId : ableDistributorIds) {
                List<Integer> distributorTreePaths = flexibleDistributorQryExe.getDistributorTreePaths(ableDistributorId);
                //过滤掉不合适的
                distributorIds.removeIf(distributorId -> !distributorTreePaths.contains(distributorId));
            }
        }
        if (distributorIds.size() == 0) {
            return new ArrayList<>();
        }
        return distributorIds;
    }

    /**
     * 将盒码的B2B订单数据还原为空（ERP发货错误、修改）
     * 
     * @param boxCode
     * @param updateMap
     */
    private void restoreOrderBoxCodeRela(String boxCode, Map<Integer, Integer> updateMap, String outboundNo) {
        List<ExchangeCodeDO> exchangeCodeList = exchangeCodeServiceI.listByBoxCode(boxCode);
        /*erp可能发重复的盒码回来、不再判断
        exchangeCodeList.stream().forEach(exchangeCode -> {
            itemCodeList.stream().forEach(itemBoxCodeRequest -> {
                itemBoxCodeRequest.getBoxCodeList().stream().forEach(s -> {
                    if(exchangeCode.getBoxCode().equals(s)){
                        throw new BaseException(ExchangeErpErrorConstant.BoxCodeUpdateFailByNoChange.getCode(),"盒码【"+s+"】"+ExchangeErpErrorConstant.BoxCodeUpdateFailByNoChange.getMsg());
                    }
                });
            });
        });*/
        exchangeCodeList.stream().forEach(code -> {
            if (code.getStatus() - ExchangeCodeConstant.StatusUsed == 0) {
                LOGGER.error(boxCode + "修改盒码失败、因为该盒码已使用");
                throw new BaseOpenApiException(ExchangeErpErrorConstant.BoxCodeUpdateFailByUsed.getCode(),
                    boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByUsed.getMsg());
            }
            if (code.getStatus() - ExchangeCodeConstant.StatusEnd == 0) {
                LOGGER.error(boxCode + "修改盒码失败、因为该盒码已过期");
                throw new BaseOpenApiException(ExchangeErpErrorConstant.BoxCodeUpdateFailByEnd.getCode(),
                    boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByEnd.getMsg());
            }
            if (code.getStatus() - ExchangeCodeConstant.StatusInvalid == 0) {
                LOGGER.error(boxCode + "修改盒码失败、因为该盒码已作废");
                throw new BaseOpenApiException(ExchangeErpErrorConstant.BoxCodeUpdateFailByInvalid.getCode(),
                    boxCode + ExchangeErpErrorConstant.BoxCodeUpdateFailByInvalid.getMsg());
            }
            ExchangeCodeOutboundRestoreLogDO restoreLog = new ExchangeCodeOutboundRestoreLogDO();
            restoreLog.setBoxCode(boxCode);
            restoreLog.setCreateTime(new Date());
            restoreLog.setDistributorId(code.getDistributorId());
            restoreLog.setDistributorName(code.getDistributorName());
            restoreLog.setDistributorOrderGoodsId(code.getDistributorOrderGoodsId());
            restoreLog.setDistributorOrderId(code.getDistributorOrderId());
            restoreLog.setNewOutboundNo(outboundNo);
            restoreLog.setOldOutboundNo(code.getOutboundNo());
            exchangeCodeOutboundRestoreLogCmdExe.create(restoreLog);
            code.setStatus(ExchangeCodeConstant.StatusInit);
            code.setDistributorOrderGoodsId(null);
            code.setDistributorId(null);
            code.setDistributorName(null);
            code.setDistributorOrderId(null);
            code.setOutboundNo(null);
            exchangeCodeServiceI.update(code);
            Integer count = updateMap.get(code.getExchangeId());
            if (count == null) {
                count = 0;
            }
            count++;
            updateMap.put(code.getExchangeId(), count);
        });
    }

    private void checkPlainCode(List<String> plainCodeList, List<ExchangeCodeDO> codeList, String boxCode) {
        if (plainCodeList != null && plainCodeList.size() > 0) {
            // 明码传进来有误
            if (codeList.size() - plainCodeList.size() != 0) {
                plainCodeList.stream().forEach(plainCode -> {
                    Boolean flag = false;
                    for (int x = 0; x < codeList.size(); x++) {
                        if (plainCode.equals(codeList.get(x).getPlainCode())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        throw new BaseOpenApiException(ExchangeErrorConstant.PlainCodeError.getCode(),
                            "【" + plainCode + "】" + ExchangeErrorConstant.PlainCodeError.getMsg());
                    }
                });
            }
            /*codeList.stream().forEach(exchangeCode -> {
                if(exchangeCode.getStatus() - ExchangeCodeConstant.StatusUnUse !=0){
                    throw new BaseException(ExchangeErrorConstant.ExchangeCodeRefundFailByCodeStatusNotUnUse.getCode(),exchangeCode+ExchangeErrorConstant.ExchangeCodeRefundFailByCodeStatusNotUnUse.getMsg());
                }
            });*/
        } else {
            // 判断盒码是否都不是未使用
            AtomicReference<Boolean> isHaveUnUse = new AtomicReference<>(true);
            codeList.stream().forEach(exchangeCode -> {
                if (exchangeCode.getStatus() - ExchangeCodeConstant.StatusUnUse == 0) {
                    isHaveUnUse.set(false);
                }
            });
            if (isHaveUnUse.get()) {
                throw new BaseOpenApiException(
                    ExchangeErrorConstant.ExchangeCodeRefundFailByCodeStatusNoUnUse.getCode(),
                    boxCode + ExchangeErrorConstant.ExchangeCodeRefundFailByCodeStatusNoUnUse.getMsg());
            }
        }
    }

    /**
     * 保存兑换码作废日志
     * 
     * @param exchangeCodeRefundDTO
     * @param exchangeRefundOrderId
     * @param exchangeCode
     * @param type
     * @param userId
     */
    private void saveInvalidLog(ExchangeCodeRefundDTO exchangeCodeRefundDTO, Integer exchangeRefundOrderId,
        ExchangeCodeDO exchangeCode, Short type, Integer userId) {
        ExchangeCodeInvalidLogDO invalidLog = new ExchangeCodeInvalidLogDO();
        invalidLog.setCreateTime(new Date());
        invalidLog.setReason(exchangeCodeRefundDTO.getRefundReason());
        invalidLog.setCodeId(exchangeCode.getId());
        invalidLog.setCreateUserName(exchangeCodeRefundDTO.getOperatorName());
        invalidLog.setRefundOrderId(exchangeRefundOrderId);
        invalidLog.setType(type);
        if (userId != null) {
            invalidLog.setCodeId(userId);
        }
        invalidLog.setExchangeId(exchangeCode.getExchangeId());
        exchangeCodeInvalidLogCmdExe.create(invalidLog);
    }

    /**
     * 校验货品id是否为兑换卡活动
     * 
     * @param itemId
     * @param distributorId
     * @return
     */
    @Override
    public Response checkItemIsExchange(Integer itemId, Integer distributorId) {

        ExchangeCardDO exchangeCardDO =
            exchangeCardServiceI.checkIsExchangeByItemIdAndDistributorId(itemId, distributorId);
        if (exchangeCardDO == null) {
            List<Integer> itemList = new ArrayList<>();
            itemList.add(itemId);
            com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> listResponse =
                goodsServiceRpc.listGoodsItemByIds(itemList);
            GoodsItemRpcDTO goodsItemRpcDTO = listResponse.getData().get(0);
            throw new FlexibleCustomException(
                goodsItemRpcDTO.getItemName() + MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CANNOT_SALE_NOW));
        }

        ExchangeCardDTORpc exchangeCardDTORpc = BeanUtils.copy(exchangeCardDO, ExchangeCardDTORpc.class);
        return Response.of(exchangeCardDTORpc);
    }

    @Override
    public Response<ExchangeCardDTORpc> getById(Integer exchangeId) {
        try {
            ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeId);
            return Response.of(BeanUtils.copy(exchangeCardDO, ExchangeCardDTORpc.class));
        } catch (FlexibleCustomException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), e.getMessage());
        }
    }

    /**
     * 判断一个盒码是不是发了两个订单
     * 
     * @param itemCodeList
     */
    private Map<String, List<String>> checkBoxCodeMoreThenOneOrder(List<ItemBoxCodeRequest> itemCodeList) {
        Map<String, Integer> map = new HashMap<>();
        Map<String, List<String>> itemCodeListMap = new HashMap<>();
        itemCodeList.stream().forEach(itemBoxCodeRequest -> {
            List<String> list = itemBoxCodeRequest.getBoxCodeList();
            List<String> copyList = new ArrayList<>();
            list.stream().forEach(boxCode -> {
                if (map.containsKey(boxCode)) {
                    throw new BaseOpenApiException(ExchangeErpErrorConstant.BoxCodeBindOrderMoreThenOne.getCode(),
                        "盒码【" + boxCode + "】" + ExchangeErpErrorConstant.BoxCodeBindOrderMoreThenOne.getMsg());
                } else {
                    map.put(boxCode, 1);
                }
                copyList.add(boxCode);
            });
            // 按照itemCode存储
            if (itemCodeListMap.containsKey(itemBoxCodeRequest.getItemCode())) {
                List<String> stringList = itemCodeListMap.get(itemBoxCodeRequest.getItemCode());
                stringList.addAll(copyList);
                itemCodeListMap.put(itemBoxCodeRequest.getItemCode(), stringList);
            } else {
                itemCodeListMap.put(itemBoxCodeRequest.getItemCode(), copyList);
            }
        });
        return itemCodeListMap;
    }

    private void checkBoxCodeIsContinuous(List<ItemBoxCodeRequest> itemCodeList, Boolean isUpdate,
        Map<String, List<String>> allBoxCodeMap) {
        // 判断是否一个出库单多个订单、（多个订单不做校验）
        /*Map<String, String> orderMap = new HashMap<>();
        itemCodeList.stream().forEach(itemBoxCodeRequest -> {
            orderMap.put(itemBoxCodeRequest.getOrderNo(),itemBoxCodeRequest.getOrderNo());
        });
        if(orderMap.size()>1){
            return;
        }*/
        for (int z = 0; z < itemCodeList.size(); z++) {
            List<String> boxCodeList2 = itemCodeList.get(z).getBoxCodeList();
            List<String> boxCodeList = new ArrayList<>();
            boxCodeList.addAll(boxCodeList2);
            if (isUpdate) {
                // 判断是否是 之前发了12345、后续改为135这种
                /*if(boxCodeList ==null || boxCodeList.size()==0){
                    //判断中间是否存在初始化的盒码
                    List<String> al lBoxCodeList = allBoxCodeMap.get(itemCodeList.get(z).getItemCode());
                    String start = allBoxCodeList.get(0);
                    String end =allBoxCodeList.get(allBoxCodeList.size()-1);
                    ExchangeCodeSyncBackLog startLog = exchangeCodeSyncBackLogDataManager.findOneByBoxCodeAndStatus(start,null);
                    ExchangeCodeSyncBackLog endLog = exchangeCodeSyncBackLogDataManager.findOneByBoxCodeAndStatus(end,null);
                    //判断区间内是否还有未出库（初始化的盒码）
                    List<ExchangeCodeSynvBackLogQry> errorList = exchangeCodeSyncBackLogDataManager.getBoxCodeNumBetween(startLog.getAlreadySyncBoxCount(),endLog.getAlreadySyncBoxCount(),
                            startLog.getItemId());
                    if(errorList !=null && errorList.size()>0){
                        GoodsItem item = goodsItemDBManager.findById(startLog.getItemId());
                        String msg = item.getItemName()+ExchangeErpErrorConstant.BoxCodeOutbandUpdateFailByNoContinus.getMsg()+errorList.get(0).getBoxCode()+"】";
                        throw new BaseException(ExchangeErpErrorConstant.BoxCodeOutbandUpdateFailByNoContinus.getCode(),msg);
                    }
                    return;
                }*/
                /*List<ExchangeCode> outCodeList = exchangeCodeDataManager.listByOutboundNoGroupByBoxCode(outBoundNo);
                for(int x=0;x<boxCodeList.size();x++){
                    for(int y=0;y<outCodeList.size();y++){
                        if(boxCodeList.get(x).equals(outCodeList.get(y).getBoxCode())){
                            //去掉已存在的
                            boxCodeList.remove(x);
                            x--;
                            break;
                        }
                    }
                }*/
                if (boxCodeList == null || boxCodeList.size() == 0) {
                    // 编辑、之前发多了盒码、编辑修改
                    continue;
                }
            }
            // 第一个盒码
            String startBoxCode = boxCodeList.get(0);
            List<ExchangeCodeDO> codeList = exchangeCodeServiceI.listByBoxCode(startBoxCode);
            if (codeList.size() > 1) {
                continue;
            }
            // 最后一个盒码
            String endBoxCode = boxCodeList.get(boxCodeList.size() - 1);

            ExchangeCodeSyncBackLogDO startLog =
                exchangeCodeSyncBackLogQryExe.findOneByBoxCodeAndStatus(startBoxCode, ExchangeCodeConstant.StatusInit);
            ExchangeCodeSyncBackLogDO endLog =
                exchangeCodeSyncBackLogQryExe.findOneByBoxCodeAndStatus(endBoxCode, ExchangeCodeConstant.StatusInit);

            // 盒码数量
            Integer size = boxCodeList.size();
            // 区间未使用的数量
            List<ExchangeCodeSyncBackLogDO> unUseList = exchangeCodeSyncBackLogQryExe.listUnUseBoxCodeNumBetween(
                startLog.getAlreadySyncBoxCount(), endLog.getAlreadySyncBoxCount(), startLog.getItemId());
            Integer unUseCount = 0;
            if (unUseList != null && unUseList.size() > 0) {
                unUseCount = unUseList.size();
            }
            if (unUseCount > size) {
                throw new BaseOpenApiException(ExchangeErpErrorConstant.BoxCodeByOneMustBeContinuous.getCode(),
                    ExchangeErpErrorConstant.BoxCodeByOneMustBeContinuous.getMsg());
            }
        }
    }
}
