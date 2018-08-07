package com.bat.distributor.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.bat.distributor.tenant.TenantContext;

@Activate(group = Constants.PROVIDER)
public class DubboProviderContextFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String tenantNo = invocation.getAttachment("tenantNo");
        TenantContext.setTenantNo(tenantNo);
        Result result = invoker.invoke(invocation);
        TenantContext.removeTenantNo();
        return result;
    }
}
