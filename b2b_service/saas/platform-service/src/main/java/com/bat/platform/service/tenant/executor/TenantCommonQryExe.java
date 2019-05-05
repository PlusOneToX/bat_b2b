package com.bat.platform.service.tenant.executor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.platform.api.tenant.dto.data.TenantCommonDTO;
import com.bat.platform.dao.tenant.PlatformTenantCommonMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantCommonDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/4 16:01
 */
@Component
public class TenantCommonQryExe {

    @Resource
    private PlatformTenantCommonMapper platformTenantCommonMapper;

    public TenantCommonDTO selectByTenantId(Integer tenantId) {
        PlatformTenantCommonDO platformTenantCommonDO = platformTenantCommonMapper.selectByTenantId(tenantId);
        if (platformTenantCommonDO == null) {
            return null;
        }
        TenantCommonDTO tenantCommonDTO = new TenantCommonDTO();
        BeanUtils.copyProperties(platformTenantCommonDO, tenantCommonDTO);
        return tenantCommonDTO;
    }

    public PlatformTenantCommonDO selectByTenantNo(String tenantNo) {
        return platformTenantCommonMapper.selectByTenantNo(tenantNo);
    }

    /**
     * 根据分销小程序appid获取租户信息
     * 
     * @param wxProgramAppId
     * @return
     */
    public List<PlatformTenantCommonDO> selectByWxProgramAppId(String wxProgramAppId) {
        return platformTenantCommonMapper.selectByWxProgramAppId(wxProgramAppId);
    }

}
