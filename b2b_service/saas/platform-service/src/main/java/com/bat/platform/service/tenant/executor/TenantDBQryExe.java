package com.bat.platform.service.tenant.executor;

import com.bat.platform.service.tenant.convertor.TenantConvertor;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.platform.api.tenant.dto.TenantNoQry;
import com.bat.platform.api.tenant.dto.data.TenantDBDTO;
import com.bat.platform.dao.tenant.PlatformTenantDBMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;
import org.springframework.beans.BeanUtils;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/29 11:27
 */
@Component
public class TenantDBQryExe {

    @Resource
    private PlatformTenantDBMapper tenantDBMapper;

    /**
     * 查询平台租户数据库信息列表
     * 
     * @param qry
     * @return
     */
    public List<TenantDBDTO> executeDB(TenantNoQry qry) {
        List<PlatformTenantDBDO> platformTenantDBDOS = tenantDBMapper.selectByTenantNo(qry.getTenantNo());
        return TenantConvertor.toTenantDBDTOList(platformTenantDBDOS);
    }

    public PlatformTenantDBRpcDTO dbConfig(Integer tenantId, Short modelType) {
        PlatformTenantDBDO platformTenantDBDO = tenantDBMapper.selectByTenantIdAndModelType(tenantId, modelType);
        if (platformTenantDBDO == null) {
            return null;
        }
        PlatformTenantDBRpcDTO platformTenantDBRpcDTO = new PlatformTenantDBRpcDTO();
        BeanUtils.copyProperties(platformTenantDBDO, platformTenantDBRpcDTO);
        return platformTenantDBRpcDTO;
    }

    /**
     * 根据服务模块获取所有租户数据库信息
     * 
     * @param modelType
     * @return
     */
    public List<PlatformTenantDBDO> listTenantDBByModelType(Short modelType) {
        List<PlatformTenantDBDO> dbdos = tenantDBMapper.listTenantDBByModelType(modelType);
        return dbdos;
    }
}
