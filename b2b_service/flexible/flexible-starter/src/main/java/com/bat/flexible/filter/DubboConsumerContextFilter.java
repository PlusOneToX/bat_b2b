package com.bat.flexible.filter;

import com.alibaba.dubbo.common.Constants;
import com.bat.flexible.Tenant.TenantContext;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.HashMap;
import java.util.Map;

@Activate(group = Constants.CONSUMER)
public class DubboConsumerContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = new HashMap<>();
        attachments.put("tenantNo", TenantContext.getTenantNo());
        // 设置需要的内容
        RpcContext.getContext().setAttachments(attachments);
        return invoker.invoke(invocation);
    }
}
