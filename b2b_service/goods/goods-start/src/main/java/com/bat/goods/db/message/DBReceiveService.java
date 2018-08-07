package com.bat.goods.db.message;

import javax.annotation.Resource;

import com.bat.goods.db.service.TenantDBService;
import com.bat.goods.manager.mq.api.Sink;
import com.bat.goods.manager.mq.dto.TenantDBDTO;
import com.bat.goods.service.common.Constant;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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
    @StreamListener(value = Sink.GOODS_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(Constant.MODEL_TYPE1)) {
            dbService.tenantDBDelete(dbdto);
        }
    }

}
