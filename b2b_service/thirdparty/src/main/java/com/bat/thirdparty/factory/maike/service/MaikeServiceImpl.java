package com.bat.thirdparty.factory.maike.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.factory.maike.common.MaikeFactoryConfig;
import com.bat.thirdparty.factory.maike.convertor.MaikeConvertor;
import com.bat.thirdparty.factory.maike.request.order.ApiMaikeOrderCancelModelRequest;
import com.bat.thirdparty.factory.maike.response.MaikeResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryCmd;
import com.bat.dubboapi.order.order.api.OrderGoodsDiyServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeDeliveryInfo;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeOrderAddCmd;
import com.bat.dubboapi.order.order.dto.info.OrderIdListQry;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.CommonUtils;
import com.bat.thirdparty.factory.maike.api.MaikeServiceI;
import com.bat.thirdparty.factory.maike.common.MaikeBaseParamCmd;
import com.bat.thirdparty.factory.maike.common.MaikeConfigQry;
import com.bat.thirdparty.factory.maike.common.MaikeErrorConstant;
import com.bat.thirdparty.factory.maike.request.devlivery.ApiOrderDeliveryCallbackModelRequest;
import com.bat.thirdparty.order.api.AdminOrderServiceI;
import com.bat.thirdparty.order.service.executor.OrderQryExe;
import com.bat.thirdparty.tenant.TenantContext;

@Service
public class MaikeServiceImpl implements MaikeServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaikeServiceImpl.class);

    @Value("${maike.key}")
    private String maikeKey;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @Autowired
    private OrderQryExe orderQryExe;

    @Autowired
    private MaikeConvertor maikeConvertor;

    @Autowired
    private HttpUtil httpUtil;

    @DubboReference(check = false, timeout = 6000, retries = 0)
    private OrderGoodsDiyServiceRpc orderGoodsDiyServiceRpc;

    @Autowired
    private AdminOrderServiceI adminOrderServiceI;

    @Value("${bat.space.host}")
    private String spaceHostName;

    @Resource
    MaikeConfigQry maikeConfigQry;

    @Override
    public ResponseBaseBean deliverOrder(ApiOrderDeliveryCallbackModelRequest request, String signature,
        String timestamp) {

        if (StringUtils.isBlank(signature)) {
            return ResponseBaseBean.responseBean(10000, "签名不能为空");
        }
        if (StringUtils.isBlank(timestamp)) {
            return ResponseBaseBean.responseBean(2000, "时间戳不能为空");
        }
        // 针对麦客PHP专门做的
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("ExpressCode", request.getExpressCode());
        jsonObject.put("ExpressName", request.getExpressName());
        jsonObject.put("ExpressNo", request.getExpressNo());
        jsonObject.put("ExpressTime", request.getExpressTime());
        jsonObject.put("OrderNo", request.getOrderNo());
        String data = JSON.toJSONString(jsonObject);
        String param = maikeKey + data + timestamp;

        try {
            LOGGER.info("签名数据：" + param);
            String checkSign = CommonUtils.sha1(param);
            if (!checkSign.equals(signature)) {
                LOGGER.info("签名错误：" + checkSign);
                LOGGER.info("签名前数据：" + param);
                return new ResponseBaseBean(MaikeErrorConstant.SignCheckoutError,
                    MaikeErrorConstant.SignCheckoutErrorMsg);
            }
            OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
            orderDeliveryCmd.setExpressCode(request.getExpressCode());
            orderDeliveryCmd.setExpressName(request.getExpressName());
            orderDeliveryCmd.setExpressNo(request.getExpressNo());
            orderDeliveryCmd.setOrderNo(request.getOrderNo());
            orderDeliveryCmd.setExpressTime(request.getExpressTime());
            com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
                orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
            LOGGER.info("麦客发货、返回{}", JSON.toJSONString(responseBean));
            if (responseBean.getCode() == 0) {
                return ResponseBaseBean.responseBean();
            }
            // 需要判断是否bat
            if (700012 - responseBean.getCode() == 0) {
                LOGGER.info("麦客发货转发到bat租户执行");
                ResponseBaseBean responseBaseBean = DeliverOrder(request, signature, timestamp);
                return responseBaseBean;
            }
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, responseBean.getMsg());
        } catch (Exception e) {
            LOGGER.error("麦客发货失败", e);
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, e.getMessage());
        }
    }

    /**
     * 发货
     * 
     * @param request
     * @param signature
     * @param timestamp
     * @return
     */
    private ResponseBaseBean DeliverOrder(ApiOrderDeliveryCallbackModelRequest request, String signature,
        String timestamp) {
        LOGGER.info("租户切换");
        TenantContext.removeTenantNo();
        TenantContext.setTenantNo("101");
        LOGGER.info("切换后租户编码:{}", TenantContext.getTenantNo());
        try {
            if (StringUtils.isBlank(signature)) {
                return ResponseBaseBean.responseBean(10000, "签名不能为空");
            }
            if (StringUtils.isBlank(timestamp)) {
                return ResponseBaseBean.responseBean(2000, "时间戳不能为空");
            }
            // 针对麦客PHP专门做的
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("ExpressCode", request.getExpressCode());
            jsonObject.put("ExpressName", request.getExpressName());
            jsonObject.put("ExpressNo", request.getExpressNo());
            jsonObject.put("ExpressTime", request.getExpressTime());
            jsonObject.put("OrderNo", request.getOrderNo());
            String data = JSON.toJSONString(jsonObject);
            String param = maikeKey + data + timestamp;

            try {
                LOGGER.info("bat租户-签名数据：" + param);
                String checkSign = CommonUtils.sha1(param);
                if (!checkSign.equals(signature)) {
                    LOGGER.info("bat租户-签名错误：" + checkSign);
                    LOGGER.info("bat租户-签名前数据：" + param);
                    return new ResponseBaseBean(MaikeErrorConstant.SignCheckoutError,
                        MaikeErrorConstant.SignCheckoutErrorMsg);
                }
                OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
                orderDeliveryCmd.setExpressCode(request.getExpressCode());
                orderDeliveryCmd.setExpressName(request.getExpressName());
                orderDeliveryCmd.setExpressNo(request.getExpressNo());
                orderDeliveryCmd.setOrderNo(request.getOrderNo());
                orderDeliveryCmd.setExpressTime(request.getExpressTime());
                com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
                    orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
                LOGGER.info("bat租户-麦客发货、返回{}", JSON.toJSONString(responseBean));
                if (responseBean.getCode() == 0) {
                    return ResponseBaseBean.responseBean();
                }
                LOGGER.info("bat租户-麦客发货失败、返回{}", JSON.toJSONString(responseBean));
                return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, responseBean.getMsg());
            } catch (Exception e) {
                LOGGER.error("bat租户-麦客发货失败", e);
                return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, e.getMessage());
            }
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    @Override
    public ResponseBaseBean cancelOrderFromFactory(ApiMaikeOrderCancelModelRequest request, String signature,
                                                   String timestamp) {
        if (StringUtils.isBlank(signature)) {
            return ResponseBaseBean.responseBean(10000, "签名不能为空");
        }
        if (StringUtils.isBlank(timestamp)) {
            return ResponseBaseBean.responseBean(2000, "时间戳不能为空");
        }
        String param = JSON.toJSONString(request);
        LOGGER.info("签名数据：" + param);
        try {
            OrderExtendDataSimpleRpcDTO extendDataRpc =
                orderQryExe.getOrderExtendDataRpcByOrderFactoryNo(request.getMaikeOrderNo());
            if (extendDataRpc == null) {
                // 调用bat的请求

            }
            String checkSign = CommonUtils.sha1(param);
            if (!checkSign.equals(signature)) {
                LOGGER.info("签名错误：" + checkSign);
                LOGGER.info("签名前数据：" + param);
                return new ResponseBaseBean(MaikeErrorConstant.SignCheckoutError,
                    MaikeErrorConstant.SignCheckoutErrorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String syncOrder(MaikeOrderAddCmd maikeOrderAddCmd) {
        MaikeFactoryConfig maikeFactoryConfig= maikeConfigQry.getTenantFactoryConfig(maikeOrderAddCmd.getManufactors());
        MaikeDeliveryInfo deliveryInfo =
            maikeConvertor.getDeliveryInfo(maikeOrderAddCmd.getShipping_address_info().getProvince(),
                maikeOrderAddCmd.getOverSeasFlag(), maikeOrderAddCmd.getLogisticsId(), maikeFactoryConfig);
        maikeOrderAddCmd.setDelivery_info(deliveryInfo);
        // 设置密钥
        maikeOrderAddCmd.setCompany_id(MaikeBaseParamCmd.company_id);
        maikeOrderAddCmd.setSecret(MaikeBaseParamCmd.secret);
        LOGGER.info("订单推送麦客、参数：" + JSON.toJSONString(maikeOrderAddCmd));
        MaikeResponse maikeResponse =
            httpUtil.requestFor(maikeFactoryConfig.getMaikeDomain() + maikeFactoryConfig.getAddOrderUrl(),
                HttpMethod.POST, maikeOrderAddCmd, MaikeResponse.class);
        LOGGER.info("订单推送麦客、返回：" + JSON.toJSONString(maikeResponse));

        if (maikeResponse != null && maikeResponse.getStatus() != null && maikeResponse.getStatus() == 1) {
            String resultData = String.valueOf(maikeResponse.getData());
            LOGGER.info("返回data:{}", resultData);
            resultData = resultData.replace("order_number=", "order_number:");
            JSONObject jsonObject = JSON.parseObject(resultData);
            LOGGER.info("jsonObject", JSON.toJSONString(jsonObject));
            String productOrderNo = jsonObject.getString("order_number");
            if (StringUtils.isBlank(productOrderNo)) {
                throw ThirdPartyException.buildException("麦客下单、没有返回工厂单号：" + maikeOrderAddCmd.getOrder_number());
            }
            // 对订单信息进行更新
            LOGGER.info("productOrderNo:" + productOrderNo);
            /* outOrderAddModel.setCode(1);
            OutDataOrderAddModel outDataOrderAddModel = new OutDataOrderAddModel();
            outDataOrderAddModel.setOrderNo(productOrderNo);
            outOrderAddModel.setData(outDataOrderAddModel);
            if(!isAdminOperate){
                //order不为空是后台操作、那个接口口会修改
                LOGGER.info("设置工厂单号："+ JSON.toJSONString(order)+"=="+productOrderNo);
                orderDBManager.updateOrder(order.getId(), productOrderNo);
            }*/
            return productOrderNo;
            // logger.info("保存成功:"+orderNo);
        } else {
            /*  OrderLog log = new OrderLog();
            log.setOperateType(OrderConstant.OperateTypeYCSyncOrder);
            log.setOperateUid(0L);
            log.setOperateName(order.getDistributorName());
            log.setOrderId(order.getId());
            log.setCreateTime(System.currentTimeMillis());
            String remark = String.format("同步订单信息至麦客操作执行失败,订单编号: %s , 分销商编号: %s，具体原因: %s", order.getId(),
                    order.getDistributorId(), maikeResponse.getMsg());
            log.setOperateRemark(remark);
            orderAdminDataManager.saveLog(log);
            outOrderAddModel.setCode(0);
            outOrderAddModel.setMsg(remark);*/

        }
        throw ThirdPartyException.buildException("推送麦客工厂失败");
    }

    /**
     * 触发定制订单到工厂
     * 
     * @param manufactor
     *            工厂编码
     */
    @Override
    public void diyOrderSyncToFactory(String manufactor) {
        Response<OrderIdListQry> orderResp = orderGoodsDiyServiceRpc.listUnSyncFactoryByManufactor(manufactor);
        if (orderResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderResp.getErrCode(), orderResp.getErrMessage());
        }
        OrderIdListQry orderIdListQry = orderResp.getData();
        LOGGER.info("同步麦客订单列表{}", JSON.toJSONString(orderIdListQry));
        if (orderIdListQry == null || orderIdListQry.getIdList() == null || orderIdListQry.getIdList().size() == 0) {
            LOGGER.info("定时器触发订单同步工厂{},暂无符合条件订单", manufactor);
            return;
        }
        List<Integer> orderIdList = orderIdListQry.getIdList();
        for (int x = 0; x < orderIdList.size(); x++) {
            try {
                adminOrderServiceI.syncOrderToFactory(orderIdList.get(x));
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("同步订单到工厂异常、忽略不处理,{},异常信息{}" + orderIdList.get(x), e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String s =
            "{\"data\":{\"order_number\":\"21072243200335\"},\"msg\":\"第三方订单数据有误，已转入订单审核，错误原因：\\\\n产品截稿时间错误\",\"status\":1}";
        MaikeResponse maikeResponse = new MaikeResponse();
        maikeResponse.setData("{\"order_number\":\"21072243200335\"}");
        String res = String.valueOf(maikeResponse.getData());
        System.out.println(res);
        JSONObject jsonObject = JSON.parseObject(res);
        String order_number = jsonObject.getString("order_number");
        System.out.println(order_number);
    }
}