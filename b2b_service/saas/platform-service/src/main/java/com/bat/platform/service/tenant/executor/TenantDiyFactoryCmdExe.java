package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantDiyFactoryCmd;
import com.bat.platform.dao.tenant.PlatformTenantDiyFactoryMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDiyFactoryDO;
import com.bat.platform.service.common.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 沙漠
 */
@Component
public class TenantDiyFactoryCmdExe {

    @Resource
    private PlatformTenantDiyFactoryMapper platformTenantDiyFactoryMapper;

    public void add(TenantDiyFactoryCmd cmd) {
        PlatformTenantDiyFactoryDO platformTenantDiyFactoryDO = new PlatformTenantDiyFactoryDO();
        BeanUtils.copyProperties(cmd, platformTenantDiyFactoryDO);
        Date date=new Date();
        platformTenantDiyFactoryDO.setCreateTime(date);
        platformTenantDiyFactoryDO.setUpdateTime(date);
        platformTenantDiyFactoryMapper.insert(platformTenantDiyFactoryDO);
    }

    public void deleteById(Integer id) {
        platformTenantDiyFactoryMapper.deleteByPrimaryKey(id);
    }

    public void update(TenantDiyFactoryCmd cmd) {
        PlatformTenantDiyFactoryDO platformTenantDiyFactoryDO = new PlatformTenantDiyFactoryDO();
        BeanUtils.copyProperties(cmd, platformTenantDiyFactoryDO);
        platformTenantDiyFactoryDO.setUpdateTime(new Date());
        if (platformTenantDiyFactoryDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        platformTenantDiyFactoryMapper.updateByPrimaryKeySelective(platformTenantDiyFactoryDO);
    }
}
