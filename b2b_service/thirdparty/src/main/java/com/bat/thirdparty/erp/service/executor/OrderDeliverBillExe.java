package com.bat.thirdparty.erp.service.executor;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.util.CommonUtils;
import com.bat.thirdparty.erp.k3cloudwebapiclient.K3CloudApiClient;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.delivery.api.OrderDeliverBillServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillCmd;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillRpcDTO;
import com.bat.dubboapi.order.order.api.OrderInfoServiceRpc;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.thirdparty.erp.api.request.BillQueryRequest;
import com.bat.thirdparty.erp.manager.ErpDataManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderDeliverBillExe {

    @CreateCache(name = ThirdKeyConstant.EXPRESS_NO_SYNC_ERP_PRE)
    private Cache<String, Integer> expressNoSyncErpCache;

    @Value("${china.time.url}")
    private String timeUrl;

    @DubboReference(check = false, timeout = 600000, retries = 0)
    private OrderDeliverBillServiceRpc orderDeliverBillServiceRpc;

    @Resource
    private ErpDataManager erpDataManager;

    @DubboReference(check = false, timeout = 10000)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private OrderInfoServiceRpc orderInfoServiceRpc;

    @Resource
    private MessageSendService messageSendService;

    @Autowired
    private OrderBusinessLogServiceI orderReceiveLogServiceI;

    @Resource
    private HttpServletRequest request;

    /**
     * 同步ERP快递单号
     */
    public void syncErpExpressNo() {
        // 分布式锁
        AutoReleaseLock autoReleaseLock =
            expressNoSyncErpCache.tryLock(TenantContext.getTenantNo() + ":" + "10000", 10, TimeUnit.MINUTES);
        if (autoReleaseLock == null) {
            // 加锁
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        try {
            Long earlistTime = CommonUtils.getWebsiteDatetime(timeUrl);
            earlistTime = earlistTime - 7 * 24 * 60 * 60 * 1000;
            Response<List<OrderDeliverBillRpcDTO>> response =
                orderDeliverBillServiceRpc.findNOLogisticsNoDeliverGoods(earlistTime);
            List<OrderDeliverBillRpcDTO> orderDeliverBills = response.getData();
            if (orderDeliverBills != null && orderDeliverBills.size() > 0) {
                log.info("开始同步 物流单号，orderDeliverBills：{}", orderDeliverBills);
                findErpExpressNo(orderDeliverBills);
            }
        } finally {
            // 释放锁
            expressNoSyncErpCache.remove(TenantContext.getTenantNo() + ":" + "10000");
            autoReleaseLock.close();
        }
    }

    private void findErpExpressNo(List<OrderDeliverBillRpcDTO> orderDeliverBills) {
        String errorMsg = "";
        boolean isSuccess = false;
        try {
            StringBuilder deliverErpNos = new StringBuilder();
            Map<String, OrderDeliverBillRpcDTO> orderDeliverBillMap = new HashMap<>();
            for (OrderDeliverBillRpcDTO deliverGoods : orderDeliverBills) {
                deliverErpNos.append("'" + deliverGoods.getDeliverErpNo() + "',");
                // 根据erp出库单号放入map
                orderDeliverBillMap.put(deliverGoods.getDeliverErpNo(), deliverGoods);
            }
            // 组装erp出库单号
            deliverErpNos.deleteCharAt(deliverErpNos.length() - 1);
            K3CloudApiClient client = erpDataManager.login();
            BillQueryRequest billQueryVo = new BillQueryRequest();
            billQueryVo.setFormId("SAL_OUTSTOCK");
            billQueryVo.setFieldKeys("FBillNo,F_PAEZ_Text4,F_PAEZ_Text3");
            billQueryVo.setFilterString("FBillNo in (" + deliverErpNos + ") ");
            List<List<Object>> result = client.executeBillQuery(JSONObject.toJSONString(billQueryVo));
            log.info("得到erp物流信息:{}", JSONObject.toJSONString(result));
            if (result != null && !result.isEmpty()) {
                sendLoger(JSONObject.toJSONString(billQueryVo), ThirdCommonConstant.API_REQUEST_SUCCESS, "",
                    JSONObject.toJSONString(result));
                for (List<Object> obj : result) {
                    // FBillNo,F_PAEZ_Text4,F_PAEZ_Text3
                    // 销售出库单号，物流公司，快递单号
                    String deliverOrderNo = obj.get(0).toString();
                    String logisticsNo = obj.get(2).toString();// 快递单号
                    if (!StringUtils.isEmpty(deliverOrderNo) && !StringUtils.isEmpty(logisticsNo)) {
                        OrderDeliverBillRpcDTO orderDeliverBill = orderDeliverBillMap.get(deliverOrderNo);
                        String distributionName = obj.get(1).toString();// 物流公司

                        LogisticsRpcQry qry = new LogisticsRpcQry();
                        qry.setName(distributionName);
                        com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsRpcDTOResponse =
                            systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry);
                        List<LogisticsRpcDTO> logisticss = logisticsRpcDTOResponse.getData();
                        log.info("根据物流名称获取到配置的物流信息:{}", JSONObject.toJSONString(logisticss));
                        if (logisticss != null && logisticss.size() > 0) {
                            boolean flag = false;
                            for (LogisticsRpcDTO logistics : logisticss) {
                                if (logistics.getId().longValue() == orderDeliverBill.getDistributionId().longValue()) {
                                    orderDeliverBill.setDistributionId(logistics.getId());
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                orderDeliverBill.setDistributionName(logisticss.get(0).getName());
                                orderDeliverBill.setDistributionId(logisticss.get(0).getId());
                            }
                            orderDeliverBill.setLogisticsNo(logisticsNo);
                            // 下面还有短信发送。。。。待实现
                        }
                    }
                }
            } else {
                sendLoger(JSONObject.toJSONString(billQueryVo), ThirdCommonConstant.API_REQUEST_FAIL, "",
                    JSONObject.toJSONString(result));
            }

            List<OrderDeliverBillCmd> orderDeliverBillCmds = new ArrayList<>();
            for (OrderDeliverBillRpcDTO orderDeliverBillRpcDTO : orderDeliverBills) {
                OrderDeliverBillCmd orderDeliverBillCmd = new OrderDeliverBillCmd();
                BeanUtils.copyProperties(orderDeliverBillRpcDTO, orderDeliverBillCmd);
                orderDeliverBillCmds.add(orderDeliverBillCmd);

            }
            log.info("物流信息变更为:{}", JSONObject.toJSONString(orderDeliverBillCmds));
            // 批量更新物流信息
            Response response = orderDeliverBillServiceRpc.batchUpdate(orderDeliverBillCmds);
            if (response.isSuccess()) {
                isSuccess = true;
                log.info("物流更新成功");
            }
        } catch (Exception e) {
            errorMsg = ";本次同步出现异常信息:" + e.getMessage();
            log.error("同步ERP快递单号同步出现异常:{}", e);
        }
        for (OrderDeliverBillRpcDTO orderDeliverBillRpcDTO : orderDeliverBills) {
            String operateDes = "同步失败" + errorMsg;
            if (isSuccess) {
                operateDes = "同步成功！同步后的快递单号为:" + orderDeliverBillRpcDTO.getLogisticsNo();
            }
            messageSendService.orderDeliverBillLogPackage(orderDeliverBillRpcDTO.getId(),
                orderDeliverBillRpcDTO.getOrderId(), "同步erp的快递单号", operateDes,
                JSONObject.toJSONString(orderDeliverBillRpcDTO));
        }
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
            pushLogDO.setLogType(OrderBusinessLogEnum.SYNC_EXPRESSNO_FROM_ERP.getLogType());
            pushLogDO.setTowardType(OrderBusinessLogEnum.SYNC_EXPRESSNO_FROM_ERP.getTowardType());
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
