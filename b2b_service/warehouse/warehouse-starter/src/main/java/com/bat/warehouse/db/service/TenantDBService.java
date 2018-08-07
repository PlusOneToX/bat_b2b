package com.bat.warehouse.db.service;

import javax.annotation.Resource;

import com.bat.warehouse.db.service.executor.TenantDBCmd;
import org.springframework.stereotype.Service;

import com.bat.warehouse.mq.dto.TenantDBDTO;

/**
 * @author bat(b2b_bat @ 163.com)
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
