package com.bat.thirdparty.common.db.message;

import static com.bat.thirdparty.common.ThirdCommonConstant.MODEL_TYPE10;
import static com.bat.thirdparty.common.ThirdCommonConstant.MODEL_TYPE9;

import javax.annotation.Resource;

import com.bat.thirdparty.common.db.service.TenantDBService;
import com.bat.thirdparty.message.api.Sink;
import com.bat.thirdparty.message.dto.TenantDBDTO;
import com.bat.thirdparty.mongodb.MongoDbContext;
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
    @Resource
    private MongoDbContext mongoDbContext;

    /**
     * 删除租户服务信息
     *
     */
    @StreamListener(value = Sink.THIRDPARTY_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(MODEL_TYPE9)) {
            dbService.tenantDBDelete(dbdto);
        } else if (dbdto.getModelType().equals(MODEL_TYPE10)) {
            // mongoDbContext.removeMongoTemplate(dbdto);
            mongoDbContext.removeMongoDbFactory(dbdto);
        }
    }

}
