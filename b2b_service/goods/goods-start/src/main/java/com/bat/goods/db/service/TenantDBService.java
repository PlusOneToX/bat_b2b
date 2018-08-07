package com.bat.goods.db.service;

import javax.annotation.Resource;

import com.bat.goods.db.service.executor.TenantDBCmd;
import com.bat.goods.manager.mq.dto.TenantDBDTO;
import org.springframework.stereotype.Service;

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
