package com.bat.thirdparty.factory.common.service;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderGoodsDiyServiceRpc;
import com.bat.dubboapi.order.order.dto.info.OrderIdListQry;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.factory.common.service.dto.OrderDeliveryCommonResp;
import com.bat.thirdparty.factory.maike.service.MaikeServiceImpl;
import com.bat.thirdparty.order.api.AdminOrderServiceI;
import com.bat.thirdparty.utils.CommonUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryCmd;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.factory.api.FactoryService;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.factory.maike.common.MaikeErrorConstant;
import com.bat.thirdparty.tenant.TenantContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/26 9:27
 */
@Service("CommonFactoryServiceImpl")
@Slf4j
public class CommonFactoryServiceImpl implements FactoryService {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsDiyServiceRpc orderGoodsDiyServiceRpc;

    @Autowired
    private AdminOrderServiceI adminOrderServiceI;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFactoryServiceImpl.class);

    @Override
    public String syncOrder(FactoryOrderAddCmd addCmd) {
        throw ThirdPartyException.buildException("手动不支持推单");
    }

    @Override
    public void deliverOrder(FactoryDeliverOrderReq factoryDeliverOrderReq) {
        OrderDeliveryCommonResp systemResp = factoryDeliverOrderReq.getSystemResp();
        OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
        BeanUtils.copyProperties(systemResp, orderDeliveryCmd);
        com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
            orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
        log.info("手动发货、返回{}", JSON.toJSONString(responseBean));
        if (responseBean.getCode() != 0) {
            // 需要判断是否bat
            if (700012 - responseBean.getCode() == 0) {
                log.info("手动发货转发到bat租户执行");
                ResponseBaseBean responseBaseBean = DeliverOrder(orderDeliveryCmd);
                if (responseBaseBean.getCode() != 0) {
                    throw ThirdPartyException.buildException(responseBaseBean.getCode() + "",
                        responseBaseBean.getMsg());
                }
            } else {
                throw ThirdPartyException.buildException(MaikeErrorConstant.DeliveryErrorCode + "",
                    responseBean.getMsg());
            }
        }
    }

    @Override
    public void diyOrderSyncToFactory(String startTime) {
        Response<OrderIdListQry> orderResp = orderGoodsDiyServiceRpc.listUnSyncFactoryByStartTime(CommonUtil.stringToDateTime(startTime));
        if (orderResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderResp.getErrCode(), orderResp.getErrMessage());
        }
        OrderIdListQry orderIdListQry = orderResp.getData();
        if (orderIdListQry == null || CollectionUtils.isEmpty(orderIdListQry.getIdList())) {
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

    private ResponseBaseBean DeliverOrder(OrderDeliveryCmd orderDeliveryCmd) {
        log.info("租户切换");
        TenantContext.removeTenantNo();
        TenantContext.setTenantNo("101");
        log.info("切换后租户编码:{}", TenantContext.getTenantNo());
        try {
            com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
                orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
            log.info("bat租户-手动发货、返回{}", JSON.toJSONString(responseBean));
            if (responseBean.getCode() == 0) {
                return ResponseBaseBean.responseBean();
            }
            log.info("bat租户-手动发货失败、返回{}", JSON.toJSONString(responseBean));
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, responseBean.getMsg());
        } catch (Exception e) {
            log.error("bat租户-手动发货失败", e);
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, e.getMessage());
        } finally {
            TenantContext.removeTenantNo();
        }
    }
}
