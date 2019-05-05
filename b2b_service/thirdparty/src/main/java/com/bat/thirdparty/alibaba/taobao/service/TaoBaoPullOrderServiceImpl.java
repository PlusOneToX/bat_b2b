package com.bat.thirdparty.alibaba.taobao.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.alibaba.taobao.api.dto.DataDecryptDTO;
import com.bat.thirdparty.alibaba.taobao.api.dto.ReceiverDataDTO;
import com.bat.thirdparty.alibaba.taobao.api.dto.TaoBaoHttpRequestDTO;
import com.bat.thirdparty.alibaba.taobao.service.executor.*;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.electricity.api.DistributorElectricityServiceRpc;
import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import com.bat.dubboapi.distributor.platform.api.DistributorSysPlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;
import com.bat.dubboapi.flexible.material.api.MaterialServiceRpc;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.taobao.TradeState;
import com.bat.thirdparty.common.util.Sha1Handler;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.AddressQry;
import com.bat.thirdparty.order.api.dto.common.UserInfoQry;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.alibaba.taobao.api.TaoBaoPullOrderServiceI;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoOrderDO;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeOrderRelevanceDO;
import com.bat.thirdparty.alibaba.taobao.service.executor.*;
import com.bat.thirdparty.utils.ConvertUtils;
import com.bat.thirdparty.utils.TaoBaoDecryptUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TopOaidDecryptResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

@Service
public class TaoBaoPullOrderServiceImpl implements TaoBaoPullOrderServiceI {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoPullOrderServiceImpl.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorElectricityServiceRpc distributorElectricityServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private MaterialServiceRpc materialServiceRpc;

    /** 线程内共享 店铺信息 */
    private static final ThreadLocal<DistributorElectricityRelationMappingRpcDTO> threadLocal = new ThreadLocal<>();

    @Resource
    private HttpServletRequest request;

    @Autowired
    private OrderBusinessLogServiceI orderReceiveLogServiceI;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private TaoBaoTradeQryExe taoBaoTradeQryExe;

    @Autowired
    private TaoBaoTradeCmdExe taoBaoTradeCmdExe;

    @Autowired
    private TaoBaoOrderCmdExe taoBaoOrderCmdExe;

    @Autowired
    private TaoBaoOrderQryExe taoBaoOrderQryExe;

    @Autowired
    private TaoBaoTradeOrderRelevanceCmdExe taoBaoTradeOrderRelevanceCmdExe;

    @Autowired
    private TaoBaoTradeOrderRelevanceQryExe taoBaoTradeOrderRelevanceQryExe;

    @Autowired
    private OrderOpenServiceI orderOpenServiceI;

    @Autowired
    private ConvertUtils convertUtils;

    @Autowired
    private TaoBaoDecryptUtils taoBaoDecryptUtils;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorSysPlatformServiceRpc distributorSysPlatformServiceRpc;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doPullTaoBaoOrder(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            log.info("params:{},{}", startTime, endTime);
            List<DistributorElectricityRelationMappingRpcDTO> relationMappings = getAllMapping();
            log.info("得到店铺关系信息{}:", JSONObject.toJSONString(relationMappings));
            String tenantNo=TenantContext.getTenantNo();
            log.info("线程外租户编号{}:", tenantNo);
            // 根据淘系店铺数量创建线程 并发拉取
            relationMappings.forEach(relationMapping -> executorService.execute(() -> {
                log.info("子线程线程租户编号{}:", tenantNo);
                TenantContext.setTenantNo(tenantNo);
                log.info("==========================淘宝商家后台管理系统开始处理增量订单信息========================");
                // 初始化店铺配置值
                log.info("threadLocal set {}", relationMapping);
                threadLocal.set(relationMapping);
                // 拉取淘宝订单信息
                log.info("开始拉取订单");
                pullOrder(startTime, endTime);
                log.info("订单拉取结束");
                // 扫描所有已付款被拦截的订单和未付款的订单 查询有没有审核通过以及有没有付款
                // 扫描已付款待发货的订单，判断从上次扫描结束后 用户有没有取消订单
                log.info("开始状态检查。。。。。。。。。。。。。。。。。。。。。。。");
                checkStatus();
                log.info("状态检查结束。。。。。。。。。。。。。。。。。。。。。。。");
                // 扫描所有可生产（待发货）订单信息 发货
                log.info("开始发货。。。。。。。。。。。。。。。。。。。。。。。");
                sendGoods();
                log.info("发货结束。。。。。。。。。。。。。。。。。。。。。。。");
                threadLocal.remove();
            }));
        } catch (Exception e) {
            log.error("出现异常:{}", e);
            throw e;
        }
    }

    /**
     * 第一版 不考虑拆单 与定制与普通单混合的情况，后续完善 taobao.trades.sold.increment.get( 查询卖家已卖出的增量交易数据（根据修改时间） )
     *
     * @param startTime
     * @param endTime
     */
    public void pullOrder(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("拉取区间增量订单数据");
        long time1 = System.currentTimeMillis();
        TaobaoClient client = new DefaultTaobaoClient(threadLocal.get().getUrl(), threadLocal.get().getAppKey(),
            threadLocal.get().getSecret());
        TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
        req.setFields(
            "tid,type,status,payment,orders,rx_audit_status,receiver_name,receiver_mobile,receiver_phone,receiver_address,created");
        req.setStartModified(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
        req.setEndModified(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
        req.setPageNo(1L);
        req.setPageSize(100L);
        req.setUseHasNext(true);
        List<Trade> trades = new ArrayList<>();
        // 递归分页过滤 定制订单信息
        recursionSearchOrder2(client, req, trades);
        log.info("最终得到的订单数据:{}", JSONObject.toJSONString(trades));
        long time2 = System.currentTimeMillis();
        log.info("区间增量查询订单耗时：{}", time2 - time1);
        log.info("区间增量订单拉取结束，开始过滤订单并获取订单详情");
        // 获取订单详情
        List<Trade> collect = trades.stream().map(trade -> {
            // 审核中订单
            if (trade.getStatus().equals(TradeState.PAID_FORBID_CONSIGN.name())) {
                return trade;
            } else {
                return getTradeFullinfo(trade.getTid());
            }
        }).collect(Collectors.toList());
        log.info("获取订单详情结束，开始保存订单");
        // 保存淘系订单信息
        save(collect);
    }

    /**
     * 递归分页查询 定制订单收集 trades
     *
     * @param client
     * @param req
     * @param trades
     */
    private void recursionSearchOrder2(TaobaoClient client, TradesSoldIncrementGetRequest req, List<Trade> trades) {
        TradesSoldIncrementGetResponse rsp = null;
        try {
            rsp = client.execute(req, threadLocal.get().getSessionKey());
            if (rsp != null && rsp.getTrades() != null) {

                List<ReceiverDataDTO> receiverDataDTOS = new ArrayList<>();
                for (Trade trade : rsp.getTrades()) {
                    ReceiverDataDTO receiverDataDTO = new ReceiverDataDTO();
                    receiverDataDTO.setOaid(trade.getOaid());
                    receiverDataDTO.setScene("1007");
                    receiverDataDTO.setTid(trade.getTid().toString());
                    receiverDataDTOS.add(receiverDataDTO);
                }
                DataDecryptDTO dataDecryptDTO = new DataDecryptDTO();
                dataDecryptDTO.setAppkey(threadLocal.get().getAppKey());
                dataDecryptDTO.setSecret(threadLocal.get().getSecret());
                dataDecryptDTO.setSessionKey(threadLocal.get().getSessionKey());
                dataDecryptDTO.setList(receiverDataDTOS);
                // 进行解密
                Map<String, TopOaidDecryptResponse.Receiver> map = taoBaoDecryptUtils.dataDecrypt(dataDecryptDTO);

                rsp.getTrades().forEach(trade -> {

                    TopOaidDecryptResponse.Receiver receiver = map.get(trade.getTid().toString());
                    if (receiver == null) {
                        throw ThirdPartyException.buildException("获取不到解密信息，tid:" + trade.getTid().toString());
                    }
                    log.info("改造前trade数据:{}", JSONObject.toJSONString(trade));
                    // 解密数据赋值
                    trade.setReceiverName(receiver.getName());
                    trade.setReceiverMobile(receiver.getMobile());
                    trade.setReceiverPhone(receiver.getPhone());
                    trade.setReceiverAddress(receiver.getAddressDetail());
                    log.info("改造后trade数据:{}", JSONObject.toJSONString(trade));
                    if (trade.getOrders() != null) {
                        List<Order> collect = trade.getOrders().stream()
                            .filter(order -> isCustomize(order.getOuterSkuId())).collect(Collectors.toList());
                        if (collect.size() != 0) {
                            trades.add(trade);
                        }
                    }
                });
                if (rsp.getHasNext()) {
                    req.setPageNo(req.getPageNo() + 1);
                    recursionSearchOrder2(client, req, trades);
                }
            }
        } catch (ApiException e) {
            log.error("分页出错:{}", e);
            sendLoger(JSONObject.toJSONString(req), (short)1, e.getErrMsg(), JSONObject.toJSONString(rsp));
        }
    }

    private boolean isCustomize(String skuId) {
        if (StringUtils.isBlank(skuId)) {
            return false;
        }
        return getMaterial().stream().anyMatch(material -> skuId.equals(material.getId() + ""));
    }

    public Trade getTradeFullinfo(Long tid) {
        StringBuilder url = new StringBuilder();
        url.append("http://jst.bat.com/order/customize?");
        url.append("tid=" + tid);
        url.append("&appkey=" + threadLocal.get().getAppKey());
        url.append("&secret=" + threadLocal.get().getSecret());
        url.append("&sessionKey=" + threadLocal.get().getSessionKey());
        log.info("最终请求url:{}", url.toString());
        String tid1 = httpUtil.requestFor(url.toString(), HttpMethod.GET, String.class);
        Gson gson = new Gson();
        TradeFullinfoGetResponse tradeFullinfoGetResponse = null;
        try {
            tradeFullinfoGetResponse = gson.fromJson(tid1, TradeFullinfoGetResponse.class);
        } catch (JsonSyntaxException e) {
            sendLoger(tid.toString(), (short)1, e.getMessage(), JSONObject.toJSONString(tradeFullinfoGetResponse));
            e.printStackTrace();
        }
        if (tradeFullinfoGetResponse != null) {
            if (tradeFullinfoGetResponse.getTrade() != null) {
                return tradeFullinfoGetResponse.getTrade();
            } else {
                sendLoger(tid.toString(), (short)1, "获取不到信息", JSONObject.toJSONString(tradeFullinfoGetResponse));
            }
        }
        return null;
    }

    public void save(List<Trade> trades) {
        trades.forEach(trade -> {
            TaobaoTradeDO taoBaoTrade = new TaobaoTradeDO();
            BeanUtils.copyProperties(trade, taoBaoTrade);
            // 先查询待保存的订单存不存在 避免重复录入
            TaobaoTradeDO one = taoBaoTradeQryExe.getById(taoBaoTrade.getTid());
            log.info("查询订单是否存在:{}", one == null);
            if (one == null) {
                log.info("即将保存的交易信息{}", JSONObject.toJSONString(taoBaoTrade));
                taoBaoTradeCmdExe.save(taoBaoTrade);
                trade.getOrders().forEach(order -> {
                    TaobaoOrderDO taoBaoOrder = new TaobaoOrderDO();
                    BeanUtils.copyProperties(order, taoBaoOrder);
                    log.info("即将保存的淘宝订单信息{}", JSONObject.toJSONString(taoBaoOrder));
                    taoBaoOrderCmdExe.save(taoBaoOrder);
                    TaobaoTradeOrderRelevanceDO taoBaoTradeTaoBaoOrder = new TaobaoTradeOrderRelevanceDO();
                    taoBaoTradeTaoBaoOrder.setTid(taoBaoTrade.getTid());
                    taoBaoTradeTaoBaoOrder.setOid(order.getOid());
                    log.info("即将保存的淘宝交易订单信息{}", JSONObject.toJSONString(taoBaoTradeTaoBaoOrder));
                    taoBaoTradeOrderRelevanceCmdExe.save(taoBaoTradeTaoBaoOrder);
                });
            }
        });
    }

    public void checkStatus() {
        DistributorElectricityRelationMappingRpcDTO relationMapping = threadLocal.get();
        List<TaobaoTradeDO> trades = taoBaoTradeQryExe.findBySellerNickAndStatus(relationMapping.getSellerNick(),
            TradeState.WAIT_BUYER_PAY.name());
        List<TaobaoTradeDO> trades1 = taoBaoTradeQryExe.findBySellerNickAndStatus(relationMapping.getSellerNick(),
            TradeState.TRADE_NO_CREATE_PAY.name());
        List<TaobaoTradeDO> trades2 = taoBaoTradeQryExe.findBySellerNickAndStatus(relationMapping.getSellerNick(),
            TradeState.PAID_FORBID_CONSIGN.name());
        List<TaobaoTradeDO> trades3 = taoBaoTradeQryExe.findBySellerNickAndStatus(relationMapping.getSellerNick(),
            TradeState.WAIT_SELLER_SEND_GOODS.name());
        trades.addAll(trades1);
        trades.addAll(trades2);
        trades.addAll(trades3);
        // 便利修改订单状态 其他暂时不改
        trades.forEach(taoBaoTrade -> {
            Trade tradeFullinfo = getTradeFullinfo(taoBaoTrade.getTid());
            if (tradeFullinfo != null) {
                taoBaoTrade.setStatus(tradeFullinfo.getStatus());
                taoBaoTradeCmdExe.update(taoBaoTrade);
            }
        });
    }

    public void sendGoods() {
        List<TaobaoTradeDO> trades = taoBaoTradeQryExe.findBySellerNickAndStatus(threadLocal.get().getSellerNick(),
            TradeState.WAIT_SELLER_SEND_GOODS.name());
        trades.forEach(taoBaoTrade -> {
            log.info("组装1 SupplenssOrderAddModel");
            OrderBaseOnIdCmd addModel = new OrderBaseOnIdCmd();
            String postFee = taoBaoTrade.getPostFee();
            // addModel.setDistributionMoney(Double.valueOf(postFee));
            // addModel.setPaymentType((short)2);
            log.info("组装2 OrderAddModel");
            addModel.setRemark("店铺：" + taoBaoTrade.getSellerNick() + ",买家：" + taoBaoTrade.getBuyerNick());
            addModel.setOrderNo(taoBaoTrade.getTid() + "");
            UserInfoQry userInfo = new UserInfoQry("", "", "", "");
            userInfo.setPhoneNumber(taoBaoTrade.getReceiverMobile());
            userInfo.setUserName(taoBaoTrade.getReceiverName());
            userInfo.setUserNo(taoBaoTrade.getBuyerOpenUid());
            addModel.setUserInfo(userInfo);
            log.info("组装2.1 OrderAddModel->userInfo组装结果:{}", JSONObject.toJSONString(userInfo));
            AddressQry address = createAddress(taoBaoTrade);
            log.info("组装2.2 OrderAddModel->Address组装结果:{}", JSONObject.toJSONString(address));
            addModel.setAddress(address);
            List<OrderDetailBaseOnIdQry> customInfos = createThirdPartyCustomInfos(taoBaoTrade);
            log.info("组装2.3 OrderAddModel->ThirdPartyCustomInfo组装结果:{}", JSONObject.toJSONString(customInfos));
            addModel.setOrderDetails(customInfos);
            log.info("组装完成:{}", JSONObject.toJSONString(addModel));
            TaoBaoHttpRequestDTO taoBaoHttpRequestDTO = createHeader(taoBaoTrade, addModel);
            log.info("创建请求头参数组装结果:{}", JSONObject.toJSONString(taoBaoHttpRequestDTO));
            if (taoBaoHttpRequestDTO != null) {
                log.info("开始发送请求");
                orderOpenServiceI.createOrderBaseOnId(null, addModel, taoBaoHttpRequestDTO, null);
            } else {
                log.info("参数不足，未发送请求");
            }
        });
    }

    /**
     * 组装地址
     */
    private AddressQry createAddress(TaobaoTradeDO taoBaoTrade) {
        log.info("组装地址前taoBaoTrade：{}", JSONObject.toJSONString(taoBaoTrade));
        AddressQry address = new AddressQry("", "", "", "");
        // 广东省-深圳市-龙岗区(不确定会不会缺)
        address.setProvince(taoBaoTrade.getReceiverState());
        address.setCity(taoBaoTrade.getReceiverCity());
        String area = taoBaoTrade.getReceiverDistrict();
        String receiverTown = taoBaoTrade.getReceiverTown();
        if (StringUtils.isBlank(area)) {
            if (StringUtils.isNotBlank(receiverTown)) {
                area = "省直辖县级行政区划";
            } else {
                area = address.getCity();
            }
        }
        address.setArea(area);
        address.setDetail(taoBaoTrade.getReceiverAddress());
        return address;
    }

    /**
     * 创建定制信息 不考虑拼单
     *
     * @return
     */
    private List<OrderDetailBaseOnIdQry> createThirdPartyCustomInfos(TaobaoTradeDO taoBaoTrade) {
        List<TaobaoTradeOrderRelevanceDO> taoBaoTradeTaoBaoOrders =
            taoBaoTradeOrderRelevanceQryExe.findByTid(taoBaoTrade.getTid());
        List<OrderDetailBaseOnIdQry> customInfos = new ArrayList<>();
        List<TaobaoOrderDO> orders = new ArrayList<>();
        // 一个柔性交易可能有多个订单 有的订单可能与定制无关
        // 搜集该交易下的所有 柔性定制订单
        taoBaoTradeTaoBaoOrders.forEach(taoBaoTradeTaoBaoOrder -> {
            TaobaoOrderDO one = taoBaoOrderQryExe.findOne(taoBaoTradeTaoBaoOrder.getOid());
            if (isCustomize(one.getOuterSkuId())) {
                orders.add(one);
            }
        });
        // 取第一个不考虑拼单
        if (orders.size() != 0) {
            TaobaoOrderDO taoBaoOrder = orders.get(0);
            OrderDetailBaseOnIdQry info = new OrderDetailBaseOnIdQry();
            try {
                String customization = taoBaoOrder.getCustomization();
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(customization).getAsJsonObject();
                jsonObject = jsonObject.getAsJsonObject("info");
                info.setImage(jsonObject.get("image").getAsString());
                info.setMaterialId(jsonObject.get("materialId").getAsInt());
                info.setModelId(jsonObject.get("modelId").getAsInt());
                JsonElement modelName = jsonObject.get("modelName");
                if (modelName == null) {
                    modelName = jsonObject.get("model");
                }
                String modelNameStr = modelName.getAsString();
                info.setModelName(modelNameStr);
                info.setBrandId(jsonObject.get("brandId").getAsInt());
                info.setBrandName(jsonObject.get("brandName").getAsString());
                JsonElement phoneW1 = jsonObject.get("phoneW");
                JsonElement phoneH1 = jsonObject.get("phoneH");
                String phoneW = "76.14";
                String phoneH = "151.29";
                if (phoneH1 != null && phoneW1 != null) {
                    phoneH = phoneH1.getAsString();
                    phoneW = phoneW1.getAsString();
                }
                info.setGenerateImage(convertUtils.convertPng2Pdf(jsonObject.get("pdf").getAsString(),
                    jsonObject.get("materialId").getAsInt(), phoneW, phoneH,
                    threadLocal.get().getDistributorId().longValue()));
                info.setPictureId(jsonObject.get("pictureId").getAsInt());
                info.setPrice(new BigDecimal(taoBaoOrder.getPrice()));
                JsonElement skuNo = jsonObject.get("commodityCode");
                String skuNoStr = "810071000309";
                if (skuNo != null) {
                    skuNoStr = skuNo.getAsString();
                }
                info.setSkuNo(skuNoStr);
                info.setCount(1);
                info.setTotalPrice(info.getPrice().multiply(new BigDecimal(info.getCount())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            customInfos.add(info);
        }
        return customInfos;
    }

    /**
     * 组装请求头
     */
    private TaoBaoHttpRequestDTO createHeader(TaobaoTradeDO taoBaoTrade, OrderBaseOnIdCmd addModel) {
        DistributorElectricityRelationMappingRpcDTO relationMapping = threadLocal.get();
        String distributorId = relationMapping.getDistributorId().toString();
        String orderSource = relationMapping.getOrderSourceId().toString();
        String timestamp = System.currentTimeMillis() + "";
        String orderString = JSON.toJSONString(addModel);
        log.info("开始获取平台信息,orderSource:{},distributorId:{}", orderSource, distributorId);
        SysPlatformRpcDTO sysPlatformRpcDTO = distributorSysPlatformServiceRpc
            .getByPlatformAndDistributorId(orderSource, Integer.valueOf(distributorId)).getData();
        log.info("平台信息返回:{}", JSONObject.toJSONString(sysPlatformRpcDTO));
        String signStr = distributorId + sysPlatformRpcDTO.getAppKey() + orderSource + orderString + timestamp;
        log.info("淘宝调取下单 签名串：{}", signStr);
        try {
            TaoBaoHttpRequestDTO HttpRequest = new TaoBaoHttpRequestDTO();
            String orderSign = Sha1Handler.encryption(signStr);
            HttpRequest.setDistributorId(distributorId);
            HttpRequest.setOrderSource(orderSource);
            HttpRequest.setTimestamp(timestamp);
            HttpRequest.setOrderSign(orderSign);
            return HttpRequest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DistributorElectricityRelationMappingRpcDTO> getAllMapping() {
        log.info("开始获取店铺关系信息");
        Response<List<DistributorElectricityRelationMappingRpcDTO>> response =
            distributorElectricityServiceRpc.getAll();
        log.info("获取店铺关系信息返回:{}", JSONObject.toJSONString(response));
        List<DistributorElectricityRelationMappingRpcDTO> list = response.getData();
        return list.stream().filter(relationMapping -> "10000".equals(relationMapping.getePlatfrom()))
            .collect(Collectors.toList());
    }

    private List<MaterialDTORpcQry> getMaterial() {
        com.bat.dubboapi.flexible.common.Response<List<MaterialDTORpcQry>> listResponse =
            materialServiceRpc.listAll();
        List<MaterialDTORpcQry> materialDTORpcQrys = listResponse.getData();
        return materialDTORpcQrys;
    }

    private void sendLoger(String requestParamJson, short status, String errorMsg, String businessData) {
        try {
            Integer userId = null;
            String platform = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
            } catch (Exception e) {
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            OrderBusinessLogDO pushLogDO = new OrderBusinessLogDO();
            pushLogDO.setLogType(OrderBusinessLogEnum.GET_TAOBAO_ORDER.getLogType());
            pushLogDO.setTowardType(OrderBusinessLogEnum.GET_TAOBAO_ORDER.getTowardType());
            pushLogDO.setDistributorId(userId);
            pushLogDO.setPlatform(platform);
            pushLogDO.setRequestParamJson(requestParamJson);
            pushLogDO.setCreateTime(new Date());
            pushLogDO.setStatus(status);
            pushLogDO.setErrorMsg(errorMsg);
            pushLogDO.setBusinessData(businessData);
            orderReceiveLogServiceI.create(pushLogDO);
        } catch (Exception e) {
            log.error("插入第三方日志出现异常:{}", e);
        }
    }
}
