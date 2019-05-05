package com.bat.platform.service.tenant.validator;

import static com.bat.platform.service.common.Constant.MODEL_TYPE10;
import static com.bat.platform.service.common.Constant.MODEL_TYPE11;
import static com.bat.platform.service.tenant.executor.ErrorCode.*;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.dao.tenant.PlatformTenantDBMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/20 16:15
 */
@Component
public class TenantValidator {

    @Resource
    private PlatformTenantDBMapper tenantDBMapper;

    /**
     * 检查租户数据库信息合法性
     * 
     */
    public void checkTenantDB(List<PlatformTenantDBDO> dbdos) {
        dbdos.forEach(dbdo -> {
            if (dbdo.getModelType().equals(MODEL_TYPE10) || dbdo.getModelType().equals(MODEL_TYPE11)) {
                if (StringUtils.isBlank(dbdo.getHost()) || StringUtils.isBlank(dbdo.getPort())
                    || StringUtils.isBlank(dbdo.getNosqlDatabase())) {
                    throw PlatformException.buildException(B_PLATFORM_NOSQL_NULL);
                }
            } else {
                if (StringUtils.isBlank(dbdo.getDbBaseUrl()) || StringUtils.isBlank(dbdo.getDbName())
                    || StringUtils.isBlank(dbdo.getDbUrl()) || dbdo.getDbType() == null) {
                    throw PlatformException.buildException(B_PLATFORM_DB_NULL);
                }
            }
            if (dbdo.getTableFlag() == null) {
                throw PlatformException.buildException(B_PLATFORM_TABLE_FLAG_NULL);
            }
        });
    }

    public PlatformTenantDBDO getPlatformTenantDBDOById(Integer id) {
        PlatformTenantDBDO dbdo = tenantDBMapper.selectByPrimaryKey(id);
        if (dbdo == null) {
            throw PlatformException.buildException(B_PLATFORM_NULL);
        }
        return dbdo;
    }

}
