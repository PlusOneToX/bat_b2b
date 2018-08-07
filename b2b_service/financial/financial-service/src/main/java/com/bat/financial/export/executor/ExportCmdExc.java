package com.bat.financial.export.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.dao.pay.PayBillsDistributorMapper;
import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDistributorDO;
import com.bat.financial.dao.refund.RefundBillsCustomerMapper;
import com.bat.financial.dao.refund.RefundBillsDistributorMapper;
import com.bat.financial.dao.refund.dataobject.RefundBillsCustomerDO;
import com.bat.financial.dao.refund.dataobject.RefundBillsDistributorDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderExtendDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.financial.api.export.dto.ReconciliationExportDTO;
import com.bat.financial.dao.pay.PayBillsCustomerMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/4 15:28
 */
@Component
@Slf4j
public class ExportCmdExc {

    @Resource
    private PayBillsCustomerMapper payBillsCustomerMapper;

    @Resource
    private PayBillsDistributorMapper payBillsDistributorMapper;

    @Resource
    private RefundBillsCustomerMapper refundBillsCustomerMapper;

    @Resource
    private RefundBillsDistributorMapper refundBillsDistributorMapper;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderExtendDataServiceRpc orderExtendDataServiceRpc;

    public void reconciliationExport(List<ReconciliationExportDTO> dtoList) {
        getBusinessId(dtoList);
        getOrderInfo(dtoList);
    }

    private void getOrderInfo(List<ReconciliationExportDTO> dtoList) {
        for (ReconciliationExportDTO dto : dtoList) {
            if (dto != null && !CollectionUtils.isEmpty(dto.getOrderIds())) {
                List<OrderExtendDataSimpleRpcDTO> orderExtendDataSimpleRpcDTOS = new ArrayList<>();
                for (Integer orderId : dto.getOrderIds()) {
                    Response<OrderExtendDataSimpleRpcDTO> extendDataSimpleByOrderId =
                        orderExtendDataServiceRpc.getExtendDataSimpleByOrderId(orderId);
                    if (extendDataSimpleByOrderId.isSuccess()) {
                        OrderExtendDataSimpleRpcDTO data = extendDataSimpleByOrderId.getData();
                        orderExtendDataSimpleRpcDTOS.add(data);
                    }
                }
                String b2bOrderNo = orderExtendDataSimpleRpcDTOS.stream().map(OrderExtendDataSimpleRpcDTO::getOrderNo)
                    .collect(Collectors.joining(","));
                String erpOrderNo = orderExtendDataSimpleRpcDTOS.stream()
                    .map(OrderExtendDataSimpleRpcDTO::getOrderErpNo).collect(Collectors.joining(","));
                dto.setB2bOrderNo(b2bOrderNo);
                dto.setErpOrderNo(erpOrderNo);
            }

        }
    }

    private void getBusinessId(List<ReconciliationExportDTO> dtoList) {
        for (ReconciliationExportDTO dto : dtoList) {
            String outTradeNo = dto.getOutTradeNo();
            String platform = outTradeNo.substring(0, 2);
            String customerFlag = outTradeNo.substring(2, 4);
            String action = outTradeNo.substring(4, 6);
            String businessId = null;
            if ("ce".equals(action)) {
                if ("c1".equals(customerFlag)) {
                    PayBillsCustomerDO byOutTradeNo = payBillsCustomerMapper.getByOutTradeNo(outTradeNo);
                    if (byOutTradeNo != null) {
                        businessId = byOutTradeNo.getBusinessId();
                    } else {
                        continue;
                    }
                } else if ("c0".equals(customerFlag)) {
                    PayBillsDistributorDO byOutTradeNo = payBillsDistributorMapper.getByOutTradeNo(outTradeNo);
                    if (byOutTradeNo != null) {
                        businessId = byOutTradeNo.getBusinessId();
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else if ("rf".equals(action)) {
                RefundBillsCustomerDO byOutRefundNo = refundBillsCustomerMapper.getByOutRefundNo(outTradeNo);
                if (byOutRefundNo != null) {
                    businessId = byOutRefundNo.getBusinessId();
                } else {
                    RefundBillsDistributorDO byOutRefundNo1 = refundBillsDistributorMapper.getByOutRefundNo(outTradeNo);
                    if (byOutRefundNo1 != null) {
                        businessId = byOutRefundNo1.getBusinessId();
                    } else {
                        continue;
                    }
                }
            }
            assert businessId != null;
            if (businessId.contains(",")) {
                log.info("多单支付情况");
                List<Integer> collect =
                    Arrays.stream(businessId.split(",")).map(Integer::valueOf).collect(Collectors.toList());
                dto.setOrderIds(collect);
            }
            dto.setOrderIds(Collections.singletonList(Integer.valueOf(businessId)));
        }
    }
}
