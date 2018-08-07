package com.bat.financial.db.message;

import com.bat.financial.db.service.TenantDBService;
import com.bat.financial.mq.api.Sink;
import com.bat.financial.mq.dto.TenantDBDTO;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.bat.financial.common.constant.FinancialConstant.MODEL_TYPE8;

/**
 * @author bat(b2b_bat399 @ 163.com)
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
    @StreamListener(value = Sink.FINANCIAL_PLATFORM_INPUT, condition = "headers['rocketmq_TAGS'] == 'tenantDBDelete'")
    public void tenantDBDelete(@Payload TenantDBDTO dbdto) {
        // 商品服务租户删除
        if (dbdto.getModelType().equals(MODEL_TYPE8)) {
            dbService.tenantDBDelete(dbdto);
        }
    }

}
