package com.bat.thirdparty.common.db.service;

import javax.annotation.Resource;

import com.bat.thirdparty.common.db.service.executor.TenantDBCmd;
import com.bat.thirdparty.message.dto.TenantDBDTO;
import org.springframework.stereotype.Service;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/22 11:27
 */
@Service
public class TenantDBService {

    @Resource
    private TenantDBCmd tenantDBCmd;

    /**
     * 删除租户服务信息
     *
     */
    public void tenantDBDelete(TenantDBDTO dbdto) {
        tenantDBCmd.tenantDBDelete(dbdto.getTenantNo(), dbdto.getDbName());
    }

    public void tenantMongoDBDelete(TenantDBDTO dbdto) {

    }

}
