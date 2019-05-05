package com.bat.thirdparty.quanyi.service;

import java.util.Date;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.quanyi.QuanyiErrorCode;
import com.bat.thirdparty.quanyi.common.QuanyiConstant;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiCmdExe;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiQryExe;
import com.bat.thirdparty.suning.api.dto.OrderDispatchCmd;
import com.bat.thirdparty.suning.common.SuNingConstant;
import com.bat.thirdparty.suning.request.CreateOrderRequest;
import com.bat.thirdparty.suning.request.ModifyOrderRequest;
import com.bat.thirdparty.suning.response.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.exchange.ExchangeCodeServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeStatusCmd;
import com.bat.dubboapi.flexible.third.api.ThirdSkuServiceRpc;
import com.bat.dubboapi.flexible.third.dto.ThirdSkuRelevanceRpcDTO;
import com.bat.thirdparty.quanyi.api.QuanyiServiceI;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiLogDO;
import com.bat.thirdparty.suning.response.*;

@Service
public class QuanyiServiceImpl implements QuanyiServiceI {

    @Autowired
    private ThirdQuanyiCmdExe thirdQuanyiCmdExe;

    @Value("${suning.distributor.id}")
    private Integer suNingDistributorId;

    @DubboReference(check = false, timeout = 50000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 50000)
    private ThirdSkuServiceRpc thirdSkuServiceRpc;

    @Autowired
    private ThirdQuanyiQryExe thirdQuanyiQryExe;

    @DubboReference(check = false, timeout = 50000)
    private ExchangeCodeServiceRpc exchangeCodeServiceRpc;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuanyiServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSuningOrder(String url, JSONObject json, CreateOrderRequest createOrderRequest,
                               OrderCreateResponse response, OrderDispatchCmd orderDispatchCmd) {
        Response<DistributorRpcDTO> distributorResponse = distributorServiceRpc.distributorById(suNingDistributorId);
        if (!distributorResponse.isSuccess() || distributorResponse.getData() == null) {
            LOGGER.info("找不到分销商信息!分销商id:{}", suNingDistributorId);
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        com.bat.dubboapi.flexible.common.Response<ThirdSkuRelevanceRpcDTO> thirdSkuResponse =
            thirdSkuServiceRpc.findSkuRelevance(suNingDistributorId, createOrderRequest.getSrvcmmdtyCode());
        if (!thirdSkuResponse.isSuccess() || thirdSkuResponse.getData() == null) {
            LOGGER.info("找不到sku相关信息!分销商id:{},第三方sku:{}", suNingDistributorId, createOrderRequest.getSrvcmmdtyCode());
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        ThirdQuanyiDO thirdQuanyiDO = new ThirdQuanyiDO();
        thirdQuanyiDO.setDistributorId(suNingDistributorId);
        thirdQuanyiDO.setDistributorName(distributorResponse.getData().getName());
        String phone = createOrderRequest.getMobPhone();
        if (StringUtils.isBlank(phone)) {
            phone = createOrderRequest.getPhoneNum();
        }
        thirdQuanyiDO.setThirdPhone(phone);
        thirdQuanyiDO.setThirdCode(createOrderRequest.getOrderId());
        thirdQuanyiDO.setThirdSkuNo(createOrderRequest.getSrvcmmdtyCode());
        thirdQuanyiDO.setThirdUserRemark(createOrderRequest.getSrvMemo());
        thirdQuanyiDO.setThirdOrderDetail(json.toJSONString());
        thirdQuanyiDO.setCreateTime(new Date());
        thirdQuanyiDO.setMaterialId(thirdSkuResponse.getData().getMaterialId());
        thirdQuanyiDO.setMaterialName(thirdSkuResponse.getData().getMaterialName());
        try {
            // 添加对应订单信息
            thirdQuanyiCmdExe.insertSelective(thirdQuanyiDO);
            // 增加对应操作日志
            ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
            thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiDO.getId());
            thirdQuanyiLogDO.setDirection(QuanyiConstant.direction1);
            thirdQuanyiLogDO.setParam(json.toJSONString());
            thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType1);
            thirdQuanyiLogDO.setResponse(JSONObject.toJSONString(response));
            thirdQuanyiLogDO.setCreateTime(new Date());
            thirdQuanyiLogDO.setUrl(url);
            thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType1);
            thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
            orderDispatchCmd.setOrderId(createOrderRequest.getOrderId());
            orderDispatchCmd.setThirdQuanyiId(thirdQuanyiDO.getId());
        } catch (DuplicateKeyException e) {
            LOGGER.error("订单号重复:{}", createOrderRequest.getOrderId());
            return;
            // throw ThirdPartyException.buildException(SuningErrorCode.SUNING_ORDER_NO_REPEAT,
            // MessageUtils.get(SuningErrorCode.SUNING_ORDER_NO_REPEAT));
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderModifyResponse modifyOrder(String url, JSONObject json, ModifyOrderRequest modifyOrderRequest) {
        OrderModifyResponse response = new OrderModifyResponse();
        response.setServiceNum(modifyOrderRequest.getServiceNum());
        try {
            Response<DistributorRpcDTO> distributorResponse =
                distributorServiceRpc.distributorById(suNingDistributorId);
            if (!distributorResponse.isSuccess() || distributorResponse.getData() == null) {
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
            }
            ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findByDistributorIdAndThirdCode(suNingDistributorId,
                modifyOrderRequest.getServiceNum());
            if (thirdQuanyiDO == null) {
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.orderCantFind);
                return response;
            }
            thirdQuanyiDO.setLastModifyRequest(json.toJSONString());
            // 若是订单取消
            if (modifyOrderRequest.getOperationType().equals(SuNingConstant.modifyOperationType2)) {
                if (thirdQuanyiDO.getDestroyFlag() != null
                    && thirdQuanyiDO.getDestroyFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                    response.setReturnCode(SuNingConstant.returnCodeF);
                    response.setReturnMessage(SuNingConstant.orderHAsDestroyCantCancel);
                    return response;
                }
                thirdQuanyiDO.setCancelFlag(ThirdCommonConstant.COMMON_FLAG_YES);
                thirdQuanyiDO.setCancelTime(new Date());
                // 同时作废兑换卡
                if (thirdQuanyiDO.getExchangeCodeId() != null) {
                    ExchangeCodeStatusCmd cmd = new ExchangeCodeStatusCmd();
                    cmd.setId(thirdQuanyiDO.getExchangeCodeId());
                    cmd.setReason(QuanyiConstant.invalidReason);
                    cmd.setDistributorId(suNingDistributorId);
                    cmd.setDistributorName(distributorResponse.getData().getName());
                    com.bat.dubboapi.flexible.common.Response response2 = exchangeCodeServiceRpc.invalid(cmd);
                    if (!response2.isSuccess()) {
                        response.setReturnCode(SuNingConstant.returnCodeF);
                        response.setReturnMessage(SuNingConstant.cancelFailMayBeHAsDestroy);
                        return response;
                    }
                }
            }
            thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
            response.setReturnCode(SuNingConstant.returnCodeS);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            // 手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setReturnCode(SuNingConstant.returnCodeF);
            response.setReturnMessage(SuNingConstant.returnUnknownError);
            return response;
        }
    }

    @Override
    public void modifyOrderLog(String url, JSONObject json, ModifyOrderRequest createOrderRequest,
        OrderModifyResponse response) {
        ThirdQuanyiDO thirdQuanyiDO =
            thirdQuanyiQryExe.findByDistributorIdAndThirdCode(suNingDistributorId, createOrderRequest.getServiceNum());
        if (thirdQuanyiDO == null) {
            return;
        }
        // 增加对应操作日志
        ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
        thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiDO.getId());
        thirdQuanyiLogDO.setDirection(QuanyiConstant.direction1);
        thirdQuanyiLogDO.setUrl(url);
        thirdQuanyiLogDO.setParam(json.toJSONString());
        thirdQuanyiLogDO.setResponse(JSONObject.toJSONString(response));
        thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType1);
        thirdQuanyiLogDO.setCreateTime(new Date());
        thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType6);
        thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSuningOredrDispatchLog(String url, String jsonStr, OrderDispatchResponse response,
        OrderDispatchCmd orderDispatchCmd) {
        ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(orderDispatchCmd.getThirdQuanyiId());
        if (thirdQuanyiDO == null) {
            LOGGER.error("派工时,找不到对应权益;权益id:{}", orderDispatchCmd.getThirdQuanyiId());
            return;
        }
        // 判断是否派工成功
        if (response.getSuccess() && response.getData().getReturnCode().equals(SuNingConstant.returnCodeY)) {
            thirdQuanyiDO.setDispatchFlag(ThirdCommonConstant.COMMON_FLAG_YES);
            thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
        }
        // 增加对应操作日志
        ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
        thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiDO.getId());
        thirdQuanyiLogDO.setDirection(QuanyiConstant.direction2);
        thirdQuanyiLogDO.setUrl(url);
        thirdQuanyiLogDO.setParam(jsonStr);
        thirdQuanyiLogDO.setResponse(JSONObject.toJSONString(response));
        thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType2);
        thirdQuanyiLogDO.setCreateTime(new Date());
        thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType2);
        thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void signInLog(String url, String jsonStr, OrderSignInResponse response, Integer thirdQuanyiId) {
        ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(thirdQuanyiId);
        if (thirdQuanyiDO == null) {
            throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_SIGN_ERROR);
        }
        // 增加对应操作日志
        ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
        thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiId);
        thirdQuanyiLogDO.setDirection(QuanyiConstant.direction2);
        thirdQuanyiLogDO.setUrl(url);
        thirdQuanyiLogDO.setParam(jsonStr);
        thirdQuanyiLogDO.setResponse(JSONObject.toJSONString(response));
        thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType2);
        thirdQuanyiLogDO.setCreateTime(new Date());
        thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType4);
        thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
    }

    @Override
    public void destroyAndLog(String url, String jsonStr, OrderDestroryResponse response, Integer thirdQuanyiId,
                              String thirdUserRemark, Integer orderId) {
        ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(thirdQuanyiId);
        if (thirdQuanyiDO == null) {
            throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_DESTROY_ERROR);
        }
        Date date = new Date();
        // 订单核销成功；则更新核销状态
        if (response.getSuccess() && response.getData().getReturnCode().equals(SuNingConstant.returnCodeY)) {
            thirdQuanyiDO.setDestroyFlag(ThirdCommonConstant.COMMON_FLAG_YES);
            thirdQuanyiDO.setDestroyTime(date);
            thirdQuanyiDO.setOrderId(orderId);
            thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
        }
        // 增加对应操作日志
        ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
        thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiId);
        thirdQuanyiLogDO.setDirection(QuanyiConstant.direction2);
        thirdQuanyiLogDO.setUrl(url);
        thirdQuanyiLogDO.setParam(jsonStr);
        thirdQuanyiLogDO.setResponse(JSONObject.toJSONString(response));
        thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType2);
        thirdQuanyiLogDO.setCreateTime(date);
        thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType5);
        thirdQuanyiLogDO.setRemark("当前订单id:{" + orderId + "},第三方客户备注:" + thirdUserRemark);
        thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void destroyAndLog(Integer thirdQuanyiId, String thirdUserRemark, Integer orderId) {
        ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(thirdQuanyiId);
        if (thirdQuanyiDO == null) {
            throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_DESTROY_ERROR);
        }
        Date date = new Date();
        thirdQuanyiDO.setOrderId(orderId);
        thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
        // 增加对应操作日志
        ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
        thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiId);
        thirdQuanyiLogDO.setDirection(QuanyiConstant.direction2);
        thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType2);
        thirdQuanyiLogDO.setCreateTime(date);
        thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType9);
        thirdQuanyiLogDO.setRemark("当前订单id:{" + orderId + "},第三方客户备注:" + thirdUserRemark);
        thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
    }

    @Override
    public ThirdQuanyiDO findById(Integer thirdQuanyiId) {
        return thirdQuanyiQryExe.findById(thirdQuanyiId);
    }

}
