package com.bat.thirdparty.msgcenter.service.executor;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.distributor.DistributorConstant;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.config.WechatProgramMsgConfig;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.wx.service.executor.WxRpcExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorExtendDataServiceRpc;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorMsgNeedDataDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.UpperDistributorRpcDTO;
import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.VoucherDistributorRpcDTO;
import com.bat.dubboapi.order.cost.api.OrderDistributorCostServiceRpc;
import com.bat.dubboapi.order.cost.dto.OrderDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.delivery.api.OrderDeliverBillServiceRpc;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillRpcDTO;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryRpcQryDTO;
import com.bat.dubboapi.order.order.api.OrderDistributorDataServiceRpc;
import com.bat.dubboapi.order.order.api.OrderExtendDataServiceRpc;
import com.bat.dubboapi.order.order.api.OrderGoodsDubboServiceRpc;
import com.bat.dubboapi.order.order.api.OrderInfoServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsRpcDTO;
import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantCommonRpcDTO;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import com.bat.thirdparty.msgcenter.common.MsgCenterWechatConstant;
import com.bat.thirdparty.msgcenter.dao.MsgCenterWechatTemplateMapper;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterWechatTemplateDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WechatProgramMsgExe {

    private static final Logger log = LoggerFactory.getLogger(WechatProgramMsgExe.class);

    @Resource
    private HttpUtil httpUtil;

    @Resource
    private WxRpcExe wxRpcExe;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @Resource
    private MsgCenterWechatTemplateMapper msgCenterWechatTemplateMapper;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private OrderDistributorDataServiceRpc orderDistributorDataServiceRpc;

    @Resource
    private TemplateExe templateExe;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderInfoServiceRpc orderInfoServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliverBillServiceRpc orderDeliverBillServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderExtendDataServiceRpc orderExtendDataServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDistributorCostServiceRpc orderDistributorCostServiceRpc;

    @Resource
    private MsgCenterCmdExe msgCenterCmdExe;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsDubboServiceRpc orderGoodsDubboServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private FinancialVoucherServiceRpc financialVoucherServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorExtendDataServiceRpc distributorExtendDataServiceRpc;


    /**
     * 订单状态流转相关通知
     * @param orderId
     * @param orderDeliverBillId
     * @param msgType
     */
    public void sendFromOrder(Integer orderId, Integer orderDeliverBillId, short msgType) {
        OrderDeliverBillRpcDTO orderDeliverBillRpcDTO = null;
        OrderDeliveryRpcQryDTO orderDeliveryRpcQryDTO=null;
        List<OrderGoodsRpcDTO> orderGoodsRpcDTOS=null;
        VoucherDistributorRpcDTO voucherDistributorRpcDTO=null;
        List<String> openIds = new ArrayList<>();
        //发货通知
        if (msgType == MsgCenterConstant.MSG_TYPE_DELIVERY) {
            //判断发货单id是否为空
            if (orderDeliverBillId == null) {
                return;
            }
            com.bat.dubboapi.order.common.Response<OrderDeliverBillRpcDTO> orderDeliverBillResponse = orderDeliverBillServiceRpc.findById(orderDeliverBillId);
            log.info("获取得到的发货单信息:{}",JSONObject.toJSONString(orderDeliverBillResponse));
            if (!orderDeliverBillResponse.isSuccess() || orderDeliverBillResponse.getData() == null) {
                return;
            }
            orderDeliverBillRpcDTO = orderDeliverBillResponse.getData();
            orderId = orderDeliverBillRpcDTO.getOrderId();
            com.bat.dubboapi.order.common.Response<OrderDeliveryRpcQryDTO> orderDeliveryResponse=orderDeliveryServiceRpc.getByOrderId(orderId);
            if (!orderDeliveryResponse.isSuccess() || orderDeliveryResponse.getData() == null) {
               // return;
            }
            orderDeliveryRpcQryDTO=orderDeliveryResponse.getData();
            //获取商品信息
            com.bat.dubboapi.order.common.Response<List<OrderGoodsRpcDTO>> orderGoodsResponse=orderGoodsDubboServiceRpc.listOrderGoodsByOrderId(orderId);
            if (!orderGoodsResponse.isSuccess() || orderGoodsResponse.getData() == null) {
              //  return;
            }
            orderGoodsRpcDTOS=orderGoodsResponse.getData();
            //收款单
            com.bat.dubboapi.financial.common.Response<VoucherDistributorRpcDTO> voucherDistributorResponse=financialVoucherServiceRpc.listVouchersByBusinessId(orderId);
            if (!voucherDistributorResponse.isSuccess() || voucherDistributorResponse.getData() == null) {
             //   return;
            }
            voucherDistributorRpcDTO=voucherDistributorResponse.getData();
            com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> orderDistributorDataResponse = orderDistributorDataServiceRpc.listByOrderIdAndErpFlag(orderDeliverBillRpcDTO.getOrderId(), null);
            if (!orderDistributorDataResponse.isSuccess() || orderDistributorDataResponse.getData() == null) {
                return;
            }
            List<OrderDistributorDataRpcDTO> orderDistributorDataList=orderDistributorDataResponse.getData();
            List<Integer> distributorIds = orderDistributorDataList.stream().map(OrderDistributorDataRpcDTO::getDistributorId).collect(Collectors.toList());
            List<DistributorMsgNeedDataDTO> distributorMsgNeedDataDTOS=distributorExtendDataServiceRpc.getByDistributorIds(distributorIds).getData();
            openIds = distributorMsgNeedDataDTOS.stream().map(DistributorMsgNeedDataDTO::getOpenId).collect(Collectors.toList());
            log.info("根据分销商id获取到的openId:{}",openIds);
        }
        OrderDistributorCostRpcQryDTO orderDistributorCost = null;
        //订单未支付提醒
        if (msgType == MsgCenterConstant.MSG_TYPE_UNPAID) {
            com.bat.dubboapi.order.common.Response<List<OrderDistributorCostRpcQryDTO>> orderGoodsDistributorCostResponse = orderDistributorCostServiceRpc.getDirectPayInfoByOrderId(orderId);
            log.info("得到的费用信息:{}",JSONObject.toJSONString(orderGoodsDistributorCostResponse));
            if (!orderGoodsDistributorCostResponse.isSuccess() || orderGoodsDistributorCostResponse.getData() == null
                    || orderGoodsDistributorCostResponse.getData().size() != 1) {
                return;
            }
            orderDistributorCost = orderGoodsDistributorCostResponse.getData().get(0);
        }

        //获取订单信息
        com.bat.dubboapi.order.common.Response<OrderInfoRpcQryDTO> orderResponse = orderInfoServiceRpc.getById(orderId);
        log.info("得到的订单信息:{}",JSONObject.toJSONString(orderResponse));
        if (!orderResponse.isSuccess() || orderResponse.getData() == null) {
            return;
        }
        OrderInfoRpcQryDTO orderInfo = orderResponse.getData();
        //获取订单扩展信息;主要获取订单openId
        com.bat.dubboapi.order.common.Response<OrderExtendDataSimpleRpcDTO> orderExtendDataResponse = orderExtendDataServiceRpc.getExtendDataSimpleByOrderId(orderId);
        log.info("得到的订单扩展信息:{}",JSONObject.toJSONString(orderExtendDataResponse));
        if (!orderExtendDataResponse.isSuccess() || orderExtendDataResponse.getData() == null || StringUtils.isBlank(orderExtendDataResponse.getData().getOpenId())) {
            com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> orderDistributorDataResponse = orderDistributorDataServiceRpc.listByOrderIdAndErpFlag(orderId, null);
            log.info("根据订单得到的分销商信息:{}",JSONObject.toJSONString(orderDistributorDataResponse));
            if (!orderDistributorDataResponse.isSuccess() || orderDistributorDataResponse.getData() == null) {
                return;
            }
            List<OrderDistributorDataRpcDTO> orderDistributorDataList=orderDistributorDataResponse.getData();
            List<Integer> distributorIds = orderDistributorDataList.stream().map(OrderDistributorDataRpcDTO::getDistributorId).collect(Collectors.toList());
            List<DistributorMsgNeedDataDTO> distributorMsgNeedDataDTOS=distributorExtendDataServiceRpc.getByDistributorIds(distributorIds).getData();
            log.info("根据分销商ids得到的分销商信息:{}",JSONObject.toJSONString(distributorMsgNeedDataDTOS));
            openIds = distributorMsgNeedDataDTOS.stream().map(DistributorMsgNeedDataDTO::getOpenId).collect(Collectors.toList());
        }
        Integer toUserId = null;
        String tuUserName = null;
        if(orderId!=null) {
            com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> orderDistributorDataResponse = orderDistributorDataServiceRpc.listByOrderIdAndErpFlag(orderId, null);
            if (!orderDistributorDataResponse.isSuccess() || orderDistributorDataResponse.getData() == null) {
                return;
            }
            for (OrderDistributorDataRpcDTO orderDistributorDataRpcDTO : orderDistributorDataResponse.getData()) {
                if (orderDistributorDataRpcDTO.getDirectFlag() == 1) {
                    toUserId = orderDistributorDataRpcDTO.getDistributorId();
                    tuUserName = orderDistributorDataRpcDTO.getDistributorName();
                }
            }
        }
       if(orderExtendDataResponse.getData()!=null) {
           //获取openid
           String openId = orderExtendDataResponse.getData().getOpenId();
           if (StringUtils.isNotBlank(openId) && !openIds.contains(openId)) {
               openIds.add(openId);
           }
       }
        //获取跳转页面以及模板id
        WechatProgramMsgDTO wechatProgramMsgDTO = getPageUrlAndTemplateId(msgType, orderId);
        if (wechatProgramMsgDTO == null) {
            return;
        }
        Object data = null;
        //订单状态通知
        if (msgType == MsgCenterConstant.MSG_TYPE_STATUS) {
            data = templateExe.getTemplateOrderStatus(orderInfo.getOrderNo());
        }

        //订单发货通知
        if (msgType == MsgCenterConstant.MSG_TYPE_DELIVERY) {
            followWayOrder(orderDeliverBillRpcDTO,orderGoodsRpcDTOS,orderDeliveryRpcQryDTO,voucherDistributorRpcDTO,wechatProgramMsgDTO.getPageUrl(),null);
            data = templateExe.getTemplateOrderDelivery(orderInfo.getOrderNo(), orderDeliverBillRpcDTO.getDistributionName(), orderDeliverBillRpcDTO.getLogisticsNo(), orderDeliverBillRpcDTO.getDeliverTime());
        }
        //订单未支付提醒
        if (msgType == MsgCenterConstant.MSG_TYPE_UNPAID) {
            if (orderDistributorCost != null) {
                data = templateExe.getTemplateOrderUnpaid(orderInfo.getOrderNo(), orderDistributorCost.getPayAmount().toPlainString(), orderInfo.getCreateTime());
            }
        }
        log.info("最终的参数数据:{}",JSONObject.toJSONString(data));
        if (data == null) {
            return;
        }
        //去重处理
        openIds = openIds.stream().distinct().collect(Collectors.toList());
        for (String openIdc : openIds) {
            if(StringUtils.isBlank(openIdc)){
                continue;
            }
            SubscribeSend subscribeSend = getSubscribeSend(openIdc, data, wechatProgramMsgDTO.getPageUrl(), wechatProgramMsgDTO.getTemplateId());
            MsgCenterLogCmd cmd = getLogCmd(data, wechatProgramMsgDTO.getPageUrl(), msgType, subscribeSend, openIdc,orderId);
            cmd.setToUserId(toUserId);
            cmd.setToUsername(tuUserName);
            msgCenterCmdExe.addLog(cmd);
            //进行消息推送
            String result = programMessageSend(subscribeSend);
            SendResult sendResult = JSONObject.parseObject(result, SendResult.class);
            if (sendResult.getErrCode() == 0) {
                msgCenterCmdExe.updatePush(cmd.getId(), ThirdCommonConstant.COMMON_FLAG_YES, null);
            } else {
                msgCenterCmdExe.updatePush(cmd.getId(), ThirdCommonConstant.COMMON_FLAG_NO, sendResult.getErrMsg());
            }
        }
    }

    private void followWayOrder(OrderDeliverBillRpcDTO orderDeliverBillRpcDTO, List<OrderGoodsRpcDTO> orderGoodsRpcDTOS, OrderDeliveryRpcQryDTO orderDeliveryRpcQryDTO, VoucherDistributorRpcDTO voucherDistributorRpcDTO, String pageUrl, String openId) {
        try {
            com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> orderDistributorDataResponse = orderDistributorDataServiceRpc.listByOrderIdAndErpFlag(orderDeliverBillRpcDTO.getOrderId(), null);
            if (!orderDistributorDataResponse.isSuccess() || orderDistributorDataResponse.getData() == null) {
                return;
            }
            List<OrderDistributorDataRpcDTO> orderDistributorDataList=orderDistributorDataResponse.getData();
            List<Integer> distributorIds = orderDistributorDataList.stream().map(OrderDistributorDataRpcDTO::getDistributorId).collect(Collectors.toList());
            List<DistributorMsgNeedDataDTO> distributorMsgNeedDataDTOS=distributorExtendDataServiceRpc.getByDistributorIds(distributorIds).getData();
            FollowWay followWay = new FollowWay();
            followWay.setOpenid(openId);
            String phone = orderDeliveryRpcQryDTO.getPhone();
            if (StringUtils.isBlank(phone)) {
                phone = orderDeliveryRpcQryDTO.getMobile();
            }
            followWay.setReceiverPhone(phone);
            followWay.setWaybillId(orderDeliverBillRpcDTO.getLogisticsNo());
            if(voucherDistributorRpcDTO!=null) {
                followWay.setTransId(voucherDistributorRpcDTO.getOutTradeNo());
            }else{
                followWay.setTransId("");
            }
            followWay.setOrderDetailPath(pageUrl);
            List<FollowWay.GoodsInfo> goodsInfos = new ArrayList<>();
            for (OrderGoodsRpcDTO orderGoodsRpcDTO : orderGoodsRpcDTOS) {
                FollowWay.GoodsInfo goodsInfo = new FollowWay.GoodsInfo();
                goodsInfo.setGoodsImgUrl(orderGoodsRpcDTO.getImageUrl());
                goodsInfo.setGoodsName(orderGoodsRpcDTO.getItemName());
                goodsInfos.add(goodsInfo);
            }
            FollowWay.DetailList detail = new FollowWay.DetailList();
            detail.setDetailList(goodsInfos);
            followWay.setGoodsInfo(detail);
            //获取token
            String token = getToken();
            String url = MsgCenterWechatConstant.ProgramFollowWaybillMessageUrl.replace("ACCESS_TOKEN", token);
            log.info("小程序传运单请求地址:" + url);
            for(DistributorMsgNeedDataDTO distributorMsgNeedDataDTO:distributorMsgNeedDataDTOS){
                followWay.setOpenid(distributorMsgNeedDataDTO.getOpenId());
                log.info("最终的请求数据传参{}", JSONObject.toJSONString(followWay));
                //进行接口调用
                String result = httpUtil.requestFor(url, HttpMethod.POST, followWay, String.class);
                log.info("小程序传运单返回:" + result);
            }


        } catch (Exception e) {
            log.info("小程序传运单出现异常:{}",e);
        }
    }


    /**
     * 分销商状态流转通知
     *
     * @param orderId
     * @param distributorId
     * @param msgType
     */
    public void sendFromDistributor(Integer orderId, Integer distributorId, short msgType) {
        com.bat.dubboapi.distributor.common.Response<UpperDistributorRpcDTO> response = distributorServiceRpc.getUpperDistributorId(distributorId);
        log.info("查询到的上级分销商信息:{}",JSONObject.toJSONString(response));
        if (!response.isSuccess() || response.getData() == null) {
            return;
        }

        UpperDistributorRpcDTO upperDistributor = response.getData();
        OrderInfoRpcQryDTO orderInfo = null;

        OrderDistributorCostRpcQryDTO orderDistributorCost = null;
        String name="";
        if (msgType == MsgCenterConstant.MSG_TYPE_NEW) {
            com.bat.dubboapi.order.common.Response<OrderDistributorCostRpcQryDTO> orderGoodsDistributorCostResponse = orderDistributorCostServiceRpc.getByOrderIdAndDistributorId(orderId, distributorId);
            log.info("查询到的费用信息:{}",JSONObject.toJSONString(orderGoodsDistributorCostResponse));
            if (!orderGoodsDistributorCostResponse.isSuccess() || orderGoodsDistributorCostResponse.getData() == null
            ) {
                return;
            }
           com.bat.dubboapi.order.common.Response<OrderDeliveryRpcQryDTO> orderDeliveryResponse= orderDeliveryServiceRpc.getByOrderId(orderId);
            log.info("查询到的物流信息:{}",JSONObject.toJSONString(orderDeliveryResponse));
            if(!orderDeliveryResponse.isSuccess()||orderDeliveryResponse.getData()==null){
                return;
            }
            name=orderDeliveryResponse.getData().getUserName();
            orderDistributorCost = orderGoodsDistributorCostResponse.getData();
            com.bat.dubboapi.order.common.Response<OrderInfoRpcQryDTO> orderResponse = orderInfoServiceRpc.getById(orderId);
            log.info("查询到的订单信息:{}",JSONObject.toJSONString(orderResponse));
            if (!orderResponse.isSuccess() || orderResponse.getData() == null) {
                return;
            }
            orderInfo = orderResponse.getData();
        }


        //获取跳转页面以及模板id
        WechatProgramMsgDTO wechatProgramMsgDTO = getPageUrlAndTemplateId(msgType, orderId);
        if (wechatProgramMsgDTO == null) {
            return;
        }
        Object data = null;

        //分销商审核通知
        if (msgType == MsgCenterConstant.MSG_TYPE_EXAMINE) {
            data = templateExe.getTemplateDistributorExamine(upperDistributor.getApplyTime(), upperDistributor.getDistributorName());

        }
        //新订单通知
        if (msgType == MsgCenterConstant.MSG_TYPE_NEW) {
            data = templateExe.getTemplateNewOrder(orderInfo.getOrderNo(), name, orderDistributorCost.getPayAmount().toPlainString());
        }
        if (data == null) {
            return;
        }
        SubscribeSend subscribeSend = getSubscribeSend(upperDistributor.getOpenId(), data, wechatProgramMsgDTO.getPageUrl(), wechatProgramMsgDTO.getTemplateId());
        MsgCenterLogCmd cmd = getLogCmd(data, wechatProgramMsgDTO.getPageUrl(), msgType, subscribeSend, upperDistributor.getOpenId(),orderId);

        cmd.setToUserId(upperDistributor.getUpperDistributorId());
        cmd.setToUsername(upperDistributor.getUpperDistributorName());

        msgCenterCmdExe.addLog(cmd);
        //进行消息推送
        String result = programMessageSend(subscribeSend);
        SendResult sendResult = JSONObject.parseObject(result, SendResult.class);
        if (sendResult.getErrCode() == 0) {
            msgCenterCmdExe.updatePush(cmd.getId(), ThirdCommonConstant.COMMON_FLAG_YES, null);
        } else {
            msgCenterCmdExe.updatePush(cmd.getId(), ThirdCommonConstant.COMMON_FLAG_NO, sendResult.getErrMsg());
        }

    }

    /**
     * 获取跳转页面以及模板id
     * @param msgType
     * @param orderId
     * @return
     */
    private WechatProgramMsgDTO getPageUrlAndTemplateId(short msgType, Integer orderId) {
        MsgCenterWechatTemplateDO msgCenterWechatTemplateDO = msgCenterWechatTemplateMapper.selectByType(msgType);
        if (msgCenterWechatTemplateDO == null) {
            return null;
        }
        WechatProgramMsgDTO wechatProgramMsgDTO = new WechatProgramMsgDTO();
        wechatProgramMsgDTO.setTemplateId(msgCenterWechatTemplateDO.getTemplateId());
        wechatProgramMsgDTO.setPageUrl(WechatProgramMsgConfig.orderDetailUrl + orderId);
        if (msgType == MsgCenterConstant.MSG_TYPE_EXAMINE) {
            wechatProgramMsgDTO.setPageUrl(WechatProgramMsgConfig.distributorListUrl);
        }
        if (msgType == MsgCenterConstant.MSG_TYPE_NEW) {
            wechatProgramMsgDTO.setPageUrl(WechatProgramMsgConfig.orderListUrl);
        }
        return wechatProgramMsgDTO;
    }

    /**
     * 获取即将发送的封装数据
     * @return
     */
    private SubscribeSend getSubscribeSend(String openId, Object data, String pageUrl, String templateId) {
        SubscribeSend subscribeSend = new SubscribeSend();
        //设置openid
        subscribeSend.setTouser(openId);
        //设置模板
        subscribeSend.setTemplate_id(templateId);
        if (StringUtils.isNotBlank(WechatProgramMsgConfig.miniprogramState)) {
            //设置跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
            subscribeSend.setMiniprogram_state(WechatProgramMsgConfig.miniprogramState);
        }
        //设置模板内容
        subscribeSend.setData(data);
        //设置跳转页面
        subscribeSend.setPage(pageUrl);
        return subscribeSend;
    }

    /**
     * 发送小程序订阅消息
     */
    public String programMessageSend(SubscribeSend subscribeSend) {
        String token=getToken();
        String url = MsgCenterWechatConstant.ProgramSubscribeMessageUrl.replace("ACCESS_TOKEN", token);
        log.info("小程序订阅发送请求地址:" + url);
        log.info("即将发送的json数据:" + JSONObject.toJSONString(subscribeSend));
        //进行接口调用
        String result = httpUtil.requestFor(url, HttpMethod.POST, subscribeSend, String.class);
        log.info("小程序请求发送消息返回:" + result);
        return result;
    }

    /**
     * 获取token
     * @return
     */
    private String getToken() {
        Response<PlatformTenantCommonRpcDTO> response = platformTenantServiceRpc.commonConfig(TenantContext.getTenantNo());
        if (!response.isSuccess()) {
            return null;
        }
        PlatformTenantCommonRpcDTO platformTenantCommonRpcDTO = response.getData();
        String token = wxRpcExe.getAccessToken(platformTenantCommonRpcDTO.getWxProgramAppId(), platformTenantCommonRpcDTO.getWxProgramAppSecret(), DistributorConstant.WX_PLATFORM_TYPE_PROGRAM);
        return token;
    }

    /**
     * 获取操作日志
     * @return
     */
    private MsgCenterLogCmd getLogCmd(Object data,String pageUrl,Short msgType,SubscribeSend subscribeSend,String openId,Integer orderId){
        MsgCenterLogCmd msgCenterLogCmd=new MsgCenterLogCmd();
        msgCenterLogCmd.setCenterId(0);
        msgCenterLogCmd.setChannel(MsgCenterConstant.CHANNEL_1);
        msgCenterLogCmd.setTitle(MsgCenterConstant.MSG_TITLE);
        msgCenterLogCmd.setContent(JSONObject.toJSONString(data));
        msgCenterLogCmd.setMiniLink(pageUrl);
        msgCenterLogCmd.setPushSwitch(ThirdCommonConstant.COMMON_FLAG_YES);
        msgCenterLogCmd.setPushFlag(ThirdCommonConstant.COMMON_FLAG_NO);
        msgCenterLogCmd.setReadFlag(ThirdCommonConstant.COMMON_FLAG_NO);
        msgCenterLogCmd.setPushTerminal(MsgCenterConstant.PUSH_TERMINAL_2);
        msgCenterLogCmd.setMiniBody(JSONObject.toJSONString(subscribeSend));
        msgCenterLogCmd.setMsgType(msgType);
        msgCenterLogCmd.setOpenId(openId);
        msgCenterLogCmd.setUserType(MsgCenterConstant.USER_TYPE_1);
        msgCenterLogCmd.setPcLink(WechatProgramMsgConfig.orderDetailUrl + orderId);
        if (msgType == MsgCenterConstant.MSG_TYPE_EXAMINE) {
            msgCenterLogCmd.setPcLink(WechatProgramMsgConfig.pcDistributorListUrl);
        }
        if (msgType == MsgCenterConstant.MSG_TYPE_NEW) {
            msgCenterLogCmd.setPcLink(WechatProgramMsgConfig.pcOrderListUrl);
        }
        return msgCenterLogCmd;
    }

}
