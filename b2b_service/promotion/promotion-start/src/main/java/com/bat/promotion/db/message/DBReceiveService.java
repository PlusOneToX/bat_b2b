package com.bat.promotion.db.message;

import javax.annotation.Resource;

import com.bat.promotion.db.service.TenantDBService;
import com.bat.promotion.service.common.Constant;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.bat.promotion.mq.api.Sink;
import com.bat.promotion.mq.dto.TenantDBDTO;

/**
 * @author bat(b2b_bat @ 163.com)
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
    @StreamListener(value = Sink.PROMOTION_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(Constant.MODEL_TYPE6)) {
            dbService.tenantDBDelete(dbdto);
        }
    }

}
