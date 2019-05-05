package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantCommonCmd;
import com.bat.platform.dao.tenant.PlatformTenantCommonMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantCommonDO;
import com.bat.platform.service.common.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/4 16:01
 */
@Component
public class TenantCommonCmdExe {

    @Resource
    private PlatformTenantCommonMapper platformTenantCommonMapper;


    public void add(TenantCommonCmd cmd) {
        PlatformTenantCommonDO platformTenantCommonDO = platformTenantCommonMapper.selectByTenantId(cmd.getTenantId());
        if (platformTenantCommonDO != null) {
            cmd.setId(platformTenantCommonDO.getId());
            update(cmd);
            return;
        }
        platformTenantCommonDO = new PlatformTenantCommonDO();
        BeanUtils.copyProperties(cmd, platformTenantCommonDO);
        Date date=new Date();
        platformTenantCommonDO.setCreateTime(date);
        platformTenantCommonDO.setUpdateTime(date);
        platformTenantCommonMapper.insert(platformTenantCommonDO);
    }

    public void update(TenantCommonCmd cmd) {
        PlatformTenantCommonDO oldObj = platformTenantCommonMapper.selectByPrimaryKey(cmd.getId());
        PlatformTenantCommonDO platformTenantCommonDO = new PlatformTenantCommonDO();
        BeanUtils.copyProperties(cmd, platformTenantCommonDO);
        platformTenantCommonDO.setUpdateTime(new Date());
        if (platformTenantCommonDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        platformTenantCommonDO.setCreateTime(oldObj.getCreateTime());
        platformTenantCommonMapper.updateByPrimaryKey(platformTenantCommonDO);
    }

}
