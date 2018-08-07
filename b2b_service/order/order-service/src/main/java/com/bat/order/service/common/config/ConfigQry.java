package com.bat.order.service.common.config;

import static com.bat.order.service.common.Constant.ERP_TYPE_JD;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.tenant.TenantContext;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/05/26 11:59
 */
@Component
public class ConfigQry {
    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;
    @Resource
    private OrderConfig config;

    /**
     * 获取租户erp配置
     */
    public OrderConfig getTenantErpConfig() {
        OrderConfig config = new OrderConfig();
        Response<PlatformTenantErpRpcDTO> response =
            platformTenantServiceRpc.erpConfig(TenantContext.getTenantNo(), ERP_TYPE_JD);
        if (response.isSuccess()) {
            config.setWlfItemNo(response.getData().getWlfItemNo());
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return config;
    }
}
