package com.bat.thirdparty.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bat.thirdparty.alibaba.taobao.api.TaoBaoOrderServiceI;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.distributor.ThirdDistributorPlatformApiConstant;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.order.OrderDeliveryConstant;
import com.bat.thirdparty.common.util.MD5Utils;
import com.bat.thirdparty.common.util.SHA256Utils;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.config.NineMachineConfig;
import com.bat.thirdparty.order.config.ZhaoLiangJiConfig;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.executor.OrderLogisticPushQryExe;
import com.bat.thirdparty.vmall.api.VmallOrderServiceI;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bat.dubboapi.distributor.electricity.api.DistributorElectricityServiceRpc;
import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import com.bat.dubboapi.distributor.platform.api.DistributorPlatformApiServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.flexible.third.api.ThirdCourierContrastServiceRpc;
import com.bat.dubboapi.flexible.third.api.ThirdSkuServiceRpc;
import com.bat.dubboapi.flexible.third.dto.MolejiLogisticsCmd;
import com.bat.dubboapi.flexible.third.dto.ThirdCourierContrastRpcDTO;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.order.ThirdPartyOrderDeliverServiceRpc;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;

@DubboService
public class ThirdPartyOrderDeliverServiceRpcImpl implements ThirdPartyOrderDeliverServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyOrderDeliverServiceRpcImpl.class);

    @DubboReference(check = false, timeout = 10000)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @Value("${moleji.distributor.id:997}")
    private Long molejiDistributorId;

    @Autowired
    private NineMachineConfig nineMachineConfig;
    @Autowired
    private ZhaoLiangJiConfig zhaoLiangJiConfig;

    @Value("${moleji.factoryId:ce9dec5776f33265e1390578bfa32401}")
    private String molejiFactoryId;

    @Value("${moleji.factorySecret:16f96790f9a92e5f33ea379b160a3fac}")
    private String MolejiFactorySecret;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorPlatformApiServiceRpc distributorPlatformApiServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private ThirdSkuServiceRpc thirdSkuServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorElectricityServiceRpc distributorElectricityServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private ThirdCourierContrastServiceRpc thirdCourierContrastServiceRpc;

    @Autowired
    private OrderLogisticPushQryExe orderLogisticPushQryExe;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private TaoBaoOrderServiceI taoBaoOrderServiceI;

    @Autowired
    private OrderBusinessLogServiceI orderBusinessLogServiceI;

    @Autowired
    private VmallOrderServiceI vmallOrderServiceI;

    @Value("${huawei.distributor.id}")
    private Integer huaweiDistributorId;

    @Autowired
    private MessageSendService messageSendService;// todo messageSendService.orderDeliverBillLog

    @Override
    public Response<Short> syncLogisticsToThirdParty(Integer orderId, String otherOrderNo, String distributionName,
        OrderLogistics logistics, Integer distributionId, String platform) {
        Integer distributorId = logistics.getDistributorId();
        LOGGER.info(
            "开始同步物流信息给第三方,orderId{},otherOrderNo{},distributionName{},logistics{},distributionId{},platform{},distributorId{}",
            orderId, otherOrderNo, distributionName, JSON.toJSONString(logistics), distributionId, platform,
            distributorId);

        List<OrderBusinessLogDO> orderBusinessLogDOList = orderBusinessLogServiceI.listReceiveOrderByCondition(null,
            null, ThirdCommonConstant.API_REQUEST_SUCCESS, null, otherOrderNo, null, null, distributorId);
        // 设置第三方用户id
        if (orderBusinessLogDOList != null && orderBusinessLogDOList.size() > 0) {
            logistics.setUserId(orderBusinessLogDOList.get(0).getBusinessData());
        }
        // DistributorUrl distributorUrl =
        // distributorUrlDBManager.findByDistributorAndOrderSourceAndType(distributorId,order.getOrderSource(),PushSyncConstant.LOGISTICS_PUSH_ID);
        com.bat.dubboapi.distributor.common.Response<DistributorPlatformApiRpcDTO> distributorApiResponse =
            distributorPlatformApiServiceRpc.getByDistributorIdAndApiTypeAndPlatform(distributorId,
                ThirdDistributorPlatformApiConstant.Distributor_SYS_PLATFORM_API_TYPE_LOGISTICS_PUSH, platform);
        if (!distributorApiResponse.isSuccess()) {
            return Response.buildFailure("11", "获取分销商url配置失败");
        }
        DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO = distributorApiResponse.getData();
        if (distributorPlatformApiRpcDTO == null) {
            return Response.buildFailure("22", "该分销商尚未配置物流信息推送、请与客服沟通处理");
        }
        // 传送更多数据信息、类似九级LogisticsSendType判断即可、
        LOGGER.info("同步物流信息：" + orderId);
        // 增加推送业务逻辑
        /*
        logistics.setUserId(order.getUserId());
        logistics.setExpressTime(orderDeliveryCmd.getExpressTime());
        logistics.setOtherOrderNo(String.valueOf(orderId));
        logistics.setDistributorId(distributorId);
        logistics.setExpressNo(orderDeliveryCmd.getExpressNo());
        logistics.setExpressName(orderDeliveryCmd.getExpressName());*/
        Map<String, String> configLogistics = new HashMap<>();
        if (distributorId == 752 || distributorId == 1762) {
            configLogistics = nineMachineConfig.getLogistics();
        } else if (distributorId == 1783 || distributorId == 2560) {
            configLogistics = zhaoLiangJiConfig.getLogistics();
        }
        if (configLogistics != null && configLogistics.size() > 0) {
            for (Map.Entry<String, String> entry : configLogistics.entrySet()) {
                String value = entry.getValue();
                if (value.contains(distributionName)) {
                    logistics.setExpressName(value);
                    logistics.setExpressCode(entry.getKey());
                    break;
                }
            }
            if (StringUtils.isBlank(logistics.getExpressName())) {
                logistics.setExpressName("其他");
                if (distributorId == 752 || distributorId == 1762) {
                    logistics.setExpressCode("NULL");
                } else if (distributorId == 1783 || distributorId == 2560) {
                    logistics.setExpressCode("1000");
                }
            }

        }
        ResponseBaseBean responseBaseBean = null;

        OrderBusinessLogDO logDO = new OrderBusinessLogDO();
        logDO.setLogType(OrderBusinessLogEnum.PUSH_LOGISTICS_THIRD.getLogType());
        logDO.setTowardType(OrderBusinessLogEnum.PUSH_LOGISTICS_THIRD.getTowardType());
        logDO.setDistributorId(distributorId);
        logDO.setPlatform(platform);
        logDO.setOtherOrderNo(logistics.getOtherOrderNo());

        if (molejiDistributorId - distributorId == 0) {
            // 摩乐吉
            com.bat.dubboapi.flexible.common.Response<MolejiLogisticsCmd> molejiLogisticsCmdResponse =
                thirdSkuServiceRpc.trandformLogistics(logistics.getDistributorId(), logistics.getExpressCode(),
                    logistics.getExpressTime(), logistics.getExpressNo(), otherOrderNo);
            if (!molejiLogisticsCmdResponse.isSuccess()) {
                return Response.buildFailure(molejiLogisticsCmdResponse.getErrCode(),
                    molejiLogisticsCmdResponse.getErrMessage());
            }
            MolejiLogisticsCmd molejiLogisticsCmd = molejiLogisticsCmdResponse.getData();
            molejiLogisticsCmd.setFactoryId(molejiFactoryId);
            String json = JSON.toJSONString(molejiLogisticsCmd);
            JSONObject jsonObject = JSON.parseObject(json, Feature.OrderedField);
            String param = "";
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                param = param + entry.getKey() + "=" + entry.getValue() + "&";
            }
            param = param.substring(0, param.length() - 1);
            LOGGER.info(param);
            String checkSignature = MD5Utils.digest32(
                SHA256Utils.getSHA256(MolejiFactorySecret + molejiLogisticsCmd.getTimestamp()).toLowerCase() + param)
                .toLowerCase();
            molejiLogisticsCmd.setSignature(checkSignature);
            LOGGER.info("摩乐吉物流参数：" + JSON.toJSONString(molejiLogisticsCmd));
            logDO.setRequestParamJson(JSON.toJSONString(molejiLogisticsCmd));
            // responseBaseBean =logisticsPushService.pushMolejiLogistics(distributorUrl.getUrl(),
            // JSON.toJSONString(molejiLogisticsCmd));
            responseBaseBean =
                httpUtil.requestFor(distributorPlatformApiRpcDTO.getHostName() + distributorPlatformApiRpcDTO.getUri(),
                    HttpMethod.POST, molejiLogisticsCmd, ResponseBaseBean.class);
        } else {
            com.bat.dubboapi.distributor.common.Response<
                DistributorElectricityRelationMappingRpcDTO> mappingRpcDTOResponse =
                    distributorElectricityServiceRpc.getByDistributorIdAndSellerNick(distributorId, null);
            LOGGER.info("查询淘宝配置返回{}", JSON.toJSONString(mappingRpcDTOResponse));
            if (!mappingRpcDTOResponse.isSuccess()) {
                return Response.buildFailure(mappingRpcDTOResponse.getErrCode(), mappingRpcDTOResponse.getErrMessage());
            }
            DistributorElectricityRelationMappingRpcDTO mappingRpcDTO = mappingRpcDTOResponse.getData();

            if (mappingRpcDTO != null && "10000".equals(mappingRpcDTO.getePlatfrom())) {
                // 淘系平台订单
                responseBaseBean = taoBaoOrderServiceI.sendGoodsCallBack(logistics, mappingRpcDTO, otherOrderNo);
            } else if (distributorId == huaweiDistributorId.intValue()) {
                responseBaseBean = vmallOrderServiceI.sendGoodsCallBack(logistics, otherOrderNo);
            } else {
                // 判断是不是系统
                if (distributorPlatformApiRpcDTO.getDevelopSource()
                    - ThirdDistributorPlatformApiConstant.DevelopSourceJiuJi == 0) {
                    LogisticsRpcQry qry = new LogisticsRpcQry();
                    qry.setLogisticsId(distributionId);
                    com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsRpcDTOResponse =
                        systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry);
                    LogisticsRpcDTO logisticsRpcDTO = logisticsRpcDTOResponse.getData().get(0);
                    // ThirdCourierContrast thirdCourierContrast =
                    // thirdCourierContrastDataManager.findByDistributorIdAndDistributionKdnCode(752L,logisticsRpcDTO.getLogisticsKdnCode());

                    com.bat.dubboapi.flexible.common.Response<ThirdCourierContrastRpcDTO> logisticsResponse =
                        thirdCourierContrastServiceRpc.getByDistributorIdAndDistributionKdnCode(752,
                            logisticsRpcDTO.getLogisticsKdnCode());
                    if (!logisticsResponse.isSuccess()) {
                        return Response.buildFailure(logisticsResponse.getErrCode(), logisticsResponse.getErrMessage());
                    }
                    ThirdCourierContrastRpcDTO thirdCourierContrastRpcDTO = logisticsResponse.getData();
                    // 转义快递公司和编码给系统
                    if (thirdCourierContrastRpcDTO == null) {
                        LOGGER.info("尚未配置系统快递公司转义,{}", logistics.getExpressCode());
                        return Response.buildFailure("222", "尚未在第三方快递公司对照表配置快递公司转义关系");
                    }
                    logistics.setExpressCode(thirdCourierContrastRpcDTO.getDistributorShipperCode());
                    logistics.setExpressName(thirdCourierContrastRpcDTO.getDistributorShipperName());
                }

                responseBaseBean = orderLogisticPushQryExe.push(distributorPlatformApiRpcDTO, logistics);

            }
        }
        // 推送状态
        Short pushStatus = null;

        // 若推送成功，需要对发货单数据状态进行更新
        if (responseBaseBean.getCode() == 0 || responseBaseBean.getCode() == 200
            || ((distributorId == 1783 || distributorId == 2560) && responseBaseBean.getCode() == 1)) {

            pushStatus = OrderDeliveryConstant.ORDER_DELIVERY_STATUS_SUCCESS;
            // orderLogisticsPushLogServiceI.create(orderId, logistics.getExpressNo(),
            // ThirdCommonConstant.API_REQUEST_SUCCESS, null, JSON.toJSONString(responseBaseBean),
            // JSON.toJSONString(logistics));
            return Response.of(pushStatus);
        } else {
            pushStatus = OrderDeliveryConstant.ORDER_DELIVERY_STATUS_FAIL;
            if (responseBaseBean != null && StringUtils.isNotBlank(responseBaseBean.getMsg())
                && responseBaseBean.getMsg().contains("订单状态已不支持物流同步")) {
                // 不支持物流同步(返回信息判断)
                return Response.of(OrderDeliveryConstant.ORDER_DELIVERY_STATUS_REJECT);
            }
            // orderLogisticsPushLogServiceI.create(orderId, logistics.getExpressNo(),
            // ThirdCommonConstant.API_REQUEST_FAIL, null, JSON.toJSONString(responseBaseBean),
            // JSON.toJSONString(logistics));
            if (responseBaseBean != null && responseBaseBean.getCode() != null && responseBaseBean.getCode() == 1000) {
                // 1000表示该订单不需要再同步物流单号过去
                pushStatus = OrderDeliveryConstant.ORDER_DELIVERY_STATUS_REJECT;
            } else {
                return Response.buildFailure(responseBaseBean.getMsg(), responseBaseBean.getMsg());
            }
        }
        return Response.of(pushStatus);
    }

}
