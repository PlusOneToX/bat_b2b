package com.bat.warehouse.db.message;

import static com.bat.warehouse.manager.common.constant.WarehouseCommonConstant.MODEL_TYPE3;

import javax.annotation.Resource;

import com.bat.warehouse.db.service.TenantDBService;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.bat.warehouse.mq.api.Sink;
import com.bat.warehouse.mq.dto.TenantDBDTO;

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
    @StreamListener(value = Sink.WAREHOUSE_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(MODEL_TYPE3)) {
            dbService.tenantDBDelete(dbdto);
        }
    }

}
