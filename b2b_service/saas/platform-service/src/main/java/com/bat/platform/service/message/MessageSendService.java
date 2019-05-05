package com.bat.platform.service.message;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;
import com.bat.platform.mq.dto.TenantDBDTO;
import com.bat.platform.mq.service.SenderService;

@Component
public class MessageSendService {

    @Resource
    private SenderService service;

    /**
     * 发送租户信息删除消息
     */
    public void tenantDBDelete(String tenantNo, Short modelType, PlatformTenantDBDO dbdo) {
        TenantDBDTO dbdto = new TenantDBDTO();
        dbdto.setTenantNo(tenantNo);
        dbdto.setModelType(modelType);
        if(dbdo != null){
            BeanUtils.copyProperties(dbdo, dbdto);
        }
        service.sendObject(dbdto, "tenantDBDelete", "tenantDBDelete-" + tenantNo);
    }
}
