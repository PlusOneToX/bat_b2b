package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantOssCmd;
import com.bat.platform.dao.tenant.PlatformTenantOssMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantOssDO;
import com.bat.platform.service.common.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 沙漠
 */
@Component
public class TenantOssCmdExe {

    @Resource
    private PlatformTenantOssMapper platformTenantOssMapper;

    public void add(TenantOssCmd cmd) {
        PlatformTenantOssDO platformTenantOssDO = platformTenantOssMapper.findByTenantIdAndOssType(cmd.getTenantId(),cmd.getOssType());
        if (platformTenantOssDO != null) {
            cmd.setId(platformTenantOssDO.getId());
            update(cmd);
            return;
        }
        platformTenantOssDO = new PlatformTenantOssDO();
        BeanUtils.copyProperties(cmd, platformTenantOssDO);
        Date date=new Date();
        platformTenantOssDO.setCreateTime(date);
        platformTenantOssDO.setUpdateTime(date);
        platformTenantOssMapper.insert(platformTenantOssDO);
    }

    public void update(TenantOssCmd cmd) {
        PlatformTenantOssDO platformTenantOssDO = new PlatformTenantOssDO();
        BeanUtils.copyProperties(cmd, platformTenantOssDO);
        platformTenantOssDO.setUpdateTime(new Date());
        if (platformTenantOssDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        platformTenantOssMapper.updateByPrimaryKeySelective(platformTenantOssDO);
    }
}
