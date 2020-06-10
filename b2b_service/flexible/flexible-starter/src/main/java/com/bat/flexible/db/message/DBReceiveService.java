package com.bat.flexible.db.message;

import javax.annotation.Resource;

import com.bat.flexible.db.service.TenantDBService;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.mq.api.Sink;
import com.bat.flexible.mq.dto.TenantDBDTO;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/4 13:54
 */
@Service
public class DBReceiveService {

    @Resource
    private TenantDBService dbService;

    /**
     * 删除租户服务信息
     *
     */
    @StreamListener(value = Sink.FLEXIBLE_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(FlexibleCommonConstant.MODEL_TYPE5)) {
            dbService.tenantDBDelete(dbdto);
        }
    }

}
