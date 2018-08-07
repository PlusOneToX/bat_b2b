package com.bat.system.db.message;

import com.bat.system.mq.api.Sink;
import com.bat.system.mq.dto.TenantDBDTO;
import com.bat.system.service.common.CommonConstant;
import com.bat.system.db.service.TenantDBService;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 13:54
 */
@Service
public class DBReceiveService {

    @Resource
    private TenantDBService dbService;

    /**
     * 删除租户服务信息
     *
     */
    @StreamListener(value = Sink.SYSTEM_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(CommonConstant.MODEL_TYPE4)) {
            dbService.tenantDBDelete(dbdto);
        }
    }

}
