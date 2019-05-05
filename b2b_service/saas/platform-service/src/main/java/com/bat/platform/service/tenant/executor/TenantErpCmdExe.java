package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantErpCmd;
import com.bat.platform.dao.tenant.PlatformTenantErpMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantErpDO;
import com.bat.platform.service.common.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 沙漠
 */
@Component
public class TenantErpCmdExe {

    @Resource
    private PlatformTenantErpMapper platformTenantErpMapper;

    public void add(TenantErpCmd cmd) {
        PlatformTenantErpDO platformTenantErpDO = platformTenantErpMapper.selectByTenantId(cmd.getTenantId());
        if (platformTenantErpDO != null) {
            cmd.setId(platformTenantErpDO.getId());
            update(cmd);
            return;
        }
        platformTenantErpDO = new PlatformTenantErpDO();
        BeanUtils.copyProperties(cmd, platformTenantErpDO);
        Date date=new Date();
        platformTenantErpDO.setCreateTime(date);
        platformTenantErpDO.setUpdateTime(date);
        platformTenantErpMapper.insert(platformTenantErpDO);
    }

    public void update(TenantErpCmd cmd) {
        PlatformTenantErpDO platformTenantErpDO = new PlatformTenantErpDO();
        BeanUtils.copyProperties(cmd, platformTenantErpDO);
        platformTenantErpDO.setUpdateTime(new Date());
        if (platformTenantErpDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        platformTenantErpMapper.updateByPrimaryKeySelective(platformTenantErpDO);
    }
}
