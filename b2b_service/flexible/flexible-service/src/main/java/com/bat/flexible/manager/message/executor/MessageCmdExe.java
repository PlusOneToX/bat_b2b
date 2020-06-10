package com.bat.flexible.manager.message.executor;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.label.dto.OrderDiyLabelDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.OrderGoodsDiyLabelCmd;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.manager.common.utils.pdf.PDFLabelService;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class MessageCmdExe {

    @Autowired
    private PDFLabelService pdfLabelUtils;

    @DubboReference(check = false,timeout = 9000,retries = 0)
    private OrderServiceRpc orderServiceRpc;


    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    /**
     * 生成标签
     * @param orderLableCmd
     */
    public void createDiyLabel(OrderLableCmd orderLableCmd) {
        String manufactor = orderLableCmd.getDiySimpleDTOList().get(0).getManufactor();
        // 如果工厂没传 默认处理成功，等后续流程补偿
        if(StringUtils.isBlank(manufactor)){
            log.error("工厂没传 默认处理成功");
            return;
        }
        List<OrderDiyLabelDTO> orderDiyLabelDTOS = pdfLabelUtils.generateLabelFile(manufactor,orderLableCmd);
        if (orderDiyLabelDTOS ==null || orderDiyLabelDTOS.size()==0){
            log.error("orderDiyLabelDTOS ==null || orderDiyLabelDTOS.size()==0 标签处理失败");
            return;
        }
        Response response = orderServiceRpc.orderGoodsDiyLabel(BeanUtils.copyList(orderDiyLabelDTOS, OrderGoodsDiyLabelCmd.class));
        if(response ==null){
            throw FlexibleDubboApiException.buildException(FlexibleDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if(!response.isSuccess()){
            throw FlexibleDubboApiException.buildException(response.getErrCode(),response.getErrMessage());
        }
    }

    /**
     * C端订单取消订单、还原兑换码状态
     * @param orderId
     */
    @Transactional
    public void orderCancelExchangeCode(Integer orderId) {
        exchangeCodeServiceI.cancelOrder(orderId);
    }
}
