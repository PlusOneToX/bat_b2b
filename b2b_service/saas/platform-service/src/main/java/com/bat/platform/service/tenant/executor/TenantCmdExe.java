package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantCmd;
import com.bat.platform.dao.tenant.PlatformTenantMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDO;
import com.bat.platform.service.common.CommonErrorCode;
import com.bat.platform.service.user.executor.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/4 16:01
 */
@Component
public class TenantCmdExe {

    @Resource
    private PlatformTenantMapper platformTenantMapper;

    public void add(TenantCmd cmd) {
        PlatformTenantDO platformTenantDO = new PlatformTenantDO();
        BeanUtils.copyProperties(cmd, platformTenantDO);
        Date date = new Date();
        platformTenantDO.setCreateTime(date);
        platformTenantDO.setUpdateTime(date);
        try {
            platformTenantMapper.insert(platformTenantDO);
        } catch (DuplicateKeyException e) {
            throw PlatformException.buildException(ErrorCode.SAME_TENANT_NO_ERROR);
        }
    }

    public void update(TenantCmd cmd) {
        PlatformTenantDO platformTenantDO = new PlatformTenantDO();
        BeanUtils.copyProperties(cmd, platformTenantDO);
        platformTenantDO.setUpdateTime(new Date());
        if (platformTenantDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        try {
            platformTenantMapper.updateByPrimaryKeySelective(platformTenantDO);
        } catch (DuplicateKeyException e) {
            throw PlatformException.buildException(ErrorCode.SAME_TENANT_NO_ERROR);
        }

    }

    public void deleteById(Integer id) {
        platformTenantMapper.deleteByPrimaryKey(id);
    }
}
