package com.bat.thirdparty.message.service;

import com.bat.dubboapi.thirdparty.mongodb.dto.data.*;
import com.bat.thirdparty.log.api.dto.CommonLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 沙漠
 */
@Component
@Slf4j
public class MessageSendService {

    @Resource
    private SenderService service;

    @Resource
    private HttpServletRequest request;

    /**
     * 发送发货单日志
     */
    private void orderDeliverBillLog(Integer orderDeliverBillId, Integer orderId, String operateSource, Integer operateId, String operator,
                           String operateType, String operateDes, String operateData, Date time) {
        OrderDeliverBillLogDTO dto = MessageConvertor.toOrderDeliverBillLogDTO(orderDeliverBillId, orderId, operateSource, operateId, operator,
                operateType, operateDes, operateData, time);

        service.sendObject(dto, "orderDeliverBillLog", "orderDeliverBillLog-" + dto.getOrderDeliverBillId());
    }

    public void orderDeliverBillLogPackage(Integer orderDeliverBillId, Integer orderId,String operateType, String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName="系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            orderDeliverBillLog(orderDeliverBillId,orderId, platform, userId
                    , userName, operateType, operateDes, operateData, new Date());
        } catch (Exception e) {
            log.error("记录发货单日志出现异常:{}", e);
        }
    }

    /**
     * 发送收款单日志
     */
    public void voucherLog(Integer voucherId, Integer orderId, String operateSource, Integer operateId, String operator,
                           String operateType, String operateDes, String operateData, Date time) {
        VoucherLogDTO dto = MessageConvertor.toVoucherLogDTO(voucherId, orderId, operateSource, operateId, operator,
                operateType, operateDes, operateData, time);
        service.sendObject(dto, "voucherLog", "voucherLog-" + dto.getVoucherId());
    }

    /**
     * 发送收款单日志
     * @param voucherId
     * @param orderId
     * @param operateType
     * @param operateDes
     * @param operateData
     * @param date
     */
    public void voucherLogPackage(Integer voucherId, Integer orderId,String operateType, String operateDes, String operateData,Date date) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName="系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            voucherLog(voucherId,orderId, platform, userId
                    , userName, operateType, operateDes, operateData, date);
        } catch (Exception e) {
            log.error("记录收款单日志出现异常:{}", e);
        }
    }

    /**
     * 发送退款单日志
     */
    private void refundLog(Integer refundId, Integer orderId, String operateSource, Integer operateId, String operator,
                          String operateType, String operateDes, String operateData, Date time) {
        RefundLogDTO dto = MessageConvertor.toRefundLogDTO(refundId, orderId, operateSource, operateId, operator,
                operateType, operateDes, operateData, time);
        service.sendObject(dto, "refundLog", "refundLog-" + dto.getRefundId());
    }

    /**
     * 发送退款单日志
     * @param refundId
     * @param orderId
     * @param operateType
     * @param operateDes
     * @param operateData
     */
    public void refundLogPackage(Integer refundId, Integer orderId, String operateType, String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName="系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            refundLog(refundId, orderId, platform,
                    userId, userName, operateType, operateDes, operateData, new Date());
        } catch (Exception e) {
            log.error("记录退款单日志出现异常:{}", e);
        }
    }

    /**
     * 发送通用日志消息
     */
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
            operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

    /**
     * 发送订单日志消息
     */
    private void orderLog(List<Integer> orderIds, String operateSource, Integer operateId, String operator,
                         String operateType, String operateDes, String operateData, Date time) {
        List<OrderLogDTO> dtos = MessageConvertor.toOrderLogDTOList(orderIds, operateSource, operateId, operator,
                operateType, operateDes, operateData, time);
        dtos.forEach(dto -> {
            service.sendObject(dto, "orderLog", "orderLog-" + dto.getOrderId());
        });
    }

    /**
     * 发送订单日志消息
     */
    public void oredrLogPackage(Integer orderId, String operateType, String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName="系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            List<Integer> orderIds = new ArrayList<>();
            orderIds.add(orderId);
            orderLog(orderIds, platform, userId
                    , userName, operateType, operateDes, operateData, new Date());

        } catch (Exception e) {
            log.error("记录订单日志出现异常:{}", e);
        }
    }

    /**
     * 发送订单日志消息(批量)
     */
    public void oredrsLogPackage(List<Integer> orderIds, String operateType, String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName="系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            orderLog(orderIds, platform, userId
                    , userName, operateType, operateDes, operateData, new Date());
        } catch (Exception e) {
            log.error("记录订单日志出现异常:{}", e);
        }
    }

    /**
     * 发送分销商日志
     */
    private void distributorLog(Integer distributorId, String operateSource, Integer operateId, String operator,
                               String operateType, String operateDes, String operateData, Date time) {
        DistributorLogDTO dto = MessageConvertor.toDistributorLogDTO(distributorId, operateSource, operateId, operator,
                operateType, operateDes, operateData, time);
        service.sendObject(dto, "distributorLog", "distributorLog-" + dto.getDistributorId());
    }

    /**
     * 发送分销商日志
     */
    public void distributorLogPackage(Integer distributorId,String operateType, String operateDes, String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName="系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            distributorLog(distributorId, platform, userId
                    , userName, operateType, operateDes, operateData, new Date());
        } catch (Exception e) {
            log.error("记录分销商日志出现异常:{}", e);
        }
    }

    private String getUserName() {
        String userName = request.getHeader("userName");
        if (StringUtils.isNotBlank(userName)) {
            try {
                return URLDecoder.decode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * ERP同步库存到B2B
     */
    public void syncErpStockToB2B() {
        log.info("ERP同步库存到B2B、发送消息");
        long time = System.currentTimeMillis();
        service.sendObject(time,"syncErpStockToB2B","syncErpStockToB2B-"+time);
    }

    /**
     * 微信分账定时任务
     */
    public void wxSubAccount() {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("定时任务发送消息、执行微信分账");
        service.sendObject(currentTimeMillis,"wxSubAccountTimer","wxSubAccountTimer-"+currentTimeMillis);
    }

    /**
     * 订单发货通知消息发送
     */
    public void orderDeliverMsg(Integer orderDeliverBillId) {
        log.info("订单发货通知消息发送:{}",orderDeliverBillId);
        service.sendObject(orderDeliverBillId,"orderDeliverMsg","orderDeliverMsg-"+orderDeliverBillId);
    }
}
