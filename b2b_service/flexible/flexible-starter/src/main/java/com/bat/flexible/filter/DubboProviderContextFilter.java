package com.bat.flexible.filter;

import com.alibaba.dubbo.common.Constants;
import com.bat.flexible.Tenant.TenantContext;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
