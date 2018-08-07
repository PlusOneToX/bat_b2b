package com.bat.financial.db.service;

import javax.annotation.Resource;

import com.bat.financial.db.service.executor.TenantDBCmd;
import org.springframework.stereotype.Service;

import com.bat.financial.mq.dto.TenantDBDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/05/22 11:27
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

}
