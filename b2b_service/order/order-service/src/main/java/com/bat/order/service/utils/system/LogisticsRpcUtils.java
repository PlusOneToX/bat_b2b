package com.bat.order.service.utils.system;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;

@Component
public class LogisticsRpcUtils {

    private static SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @DubboReference(check = false, timeout = 6000)
    public void setSystemLogisticsServiceRpc(SystemLogisticsServiceRpc systemLogisticsServiceRpc) {
        LogisticsRpcUtils.systemLogisticsServiceRpc = systemLogisticsServiceRpc;
    }

    /**
     * 根据配送方式id查询订单的配送金额对象
     * 
     * @param logisticsCalculationRpcQry
     * @return
     */
    public static LogisticsCalculationRpcDTO
        calculateByLogisticsId(LogisticsCalculationRpcQry logisticsCalculationRpcQry) {
        Response<LogisticsCalculationRpcDTO> logisticsCalculationRpcDTOResponse = null;
        return logisticsCalculationRpcDTOResponse.getData();
    }

}
