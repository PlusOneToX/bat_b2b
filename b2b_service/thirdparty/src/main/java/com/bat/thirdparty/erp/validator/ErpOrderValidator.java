package com.bat.thirdparty.erp.validator;

import java.util.List;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.enumtype.OrderErpStatus;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdNameErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import com.bat.dubboapi.order.common.ResponseBaseBean;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderDetailRequest;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderRequest;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderStatusRequest;
import com.bat.dubboapi.order.order.dto.OrderCheckCmd;

public class ErpOrderValidator {

    /**
     * 校验ERP同步出库单参数
     */
    public static ResponseBaseBean validateErpSyncOutBoundParam(ErpDeliverOrderRequest erpDeliverOrderRequest) {
        if (erpDeliverOrderRequest.getDeliverOrderDetails() == null
            || erpDeliverOrderRequest.getDeliverOrderDetails().isEmpty()) {
            return ResponseBaseBean.responseBean(ThirdOrderErrorCode.ErpOrderDetailNullCode,
                MessageUtils.get(ThirdOrderErrorCode.T_ORDER_DETAIL_NULL));
        }
        List<ErpDeliverOrderDetailRequest> deliverOrderDetails = erpDeliverOrderRequest.getDeliverOrderDetails();
        for (int x = 0; x < deliverOrderDetails.size(); x++) {
            ErpDeliverOrderDetailRequest detailRequest = deliverOrderDetails.get(x);
            if (StringUtils.isBlank(detailRequest.getOrderNo())) {
                return ResponseBaseBean.responseBean(ThirdOrderErrorCode.ErpOrderNoNullCode,
                    MessageUtils.get(ThirdOrderErrorCode.COMMON_ORDER_NO_NULL));
            }
            if (StringUtils.isBlank(detailRequest.getItemNo())) {
                return ResponseBaseBean.responseBean(ThirdOrderErrorCode.ErpOrderDetailNoNullCode,
                    MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_ITEM)
                        + MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL));
            }
            if (detailRequest.getNum() == null) {
                return ResponseBaseBean.responseBean(ThirdOrderErrorCode.ErpDeliverOrderNumNoNullCode,
                    MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ERP_DELIVERY_NUM_NULL));
            }

        }
        return ResponseBaseBean.responseBean();
    }

    /**
     * 校验ERP同步出库单状态变更参数
     */
    public static ResponseBaseBean
        validateErpDeliverOrderStatusRequest(ErpDeliverOrderStatusRequest erpDeliverOrderStatusRequest) {
        if (StringUtils.isBlank(erpDeliverOrderStatusRequest.getDeliverOrderNo())) {
            return ResponseBaseBean.responseBean(ThirdOrderErrorCode.T_ERP_OUTBOUND_ORDER_NO_NULL_CODE,
                MessageUtils.get(ThirdOrderErrorCode.T_ERP_OUTBOUND_ORDER_NO_NULL_MSG));
        }
        if (erpDeliverOrderStatusRequest.getStatus() == null) {
            return ResponseBaseBean.responseBean(ThirdOrderErrorCode.T_ERP_OUTBOUND_ORDER_NO_NULL_CODE,
                MessageUtils.get(ThirdOrderErrorCode.T_ERP_OUTBOUND_ORDER_NO_NULL_MSG));
        }

        return ResponseBaseBean.responseBean();
    }

    public static void validateOrderCheck(OrderCheckCmd cmd) {
        if (!cmd.getOrderStatus().equals(OrderErpStatus.PENDING.getValue())
            && !cmd.getOrderStatus().equals(OrderErpStatus.CONFIRMED.getValue())
            && !cmd.getOrderStatus().equals(OrderErpStatus.PENDING.getValue())) {
            throw ThirdPartyException.buildException(ErrorCode.B_ERP_ORDER_CHECK_ERROR,
                MessageUtils.get(ErrorCode.B_ERP_ORDER_CHECK_ERROR));
        }
    }
}
