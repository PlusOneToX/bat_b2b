package com.bat.platform.service.tenant.convertor;

import static com.bat.platform.service.common.Constant.*;
import static com.bat.platform.service.tenant.executor.ErrorCode.B_PLATFORM_DB_NAME_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantDBCmd;
import com.bat.platform.api.tenant.dto.data.TenantDBDTO;
import com.bat.platform.api.tenant.dto.data.TenantUrlDTO;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantUrlDO;
import com.bat.platform.service.common.config.PlatformDBConfig;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/10/15 14:25
 */
public class TenantConvertor {

    public static List<TenantDBDTO> toTenantDBDTOList(List<PlatformTenantDBDO> dbdos) {
        List<TenantDBDTO> dtos = new ArrayList<>();
        dbdos.forEach(dbdo -> {
            TenantDBDTO dto = new TenantDBDTO();
            BeanUtils.copyProperties(dbdo, dto);
            dtos.add(dto);
        });
        return dtos;
    }

    public static List<PlatformTenantDBDO> toPlatformTenantDBDOList(List<TenantDBCmd> cmds) {
        List<PlatformTenantDBDO> dbdos = new ArrayList<>();
        cmds.forEach(cmd -> {
            Date date = new Date(System.currentTimeMillis());
            PlatformTenantDBDO dbdo = new PlatformTenantDBDO();
            BeanUtils.copyProperties(cmd, dbdo);
            if (dbdo.getId() == null) {
                dbdo.setTableFlag(TABLE_FLAG1);
            }
            dbdo.setTenantNo(dbdo.getTenantNo().toLowerCase());
            if (!dbdo.getModelType().equals(MODEL_TYPE10) && !dbdo.getModelType().equals(MODEL_TYPE11)) {
                dbdo.setDbName(dbdo.getDbName().toLowerCase());
            }
            dbdo.setCreateTime(date);
            dbdo.setUpdateTime(date);
            dbdos.add(dbdo);
        });
        return dbdos;
    }

    public static List<PlatformTenantDBDO> toPlatformTenantDBDOAutoList(List<TenantDBCmd> cmds,
        PlatformDBConfig config) {
        List<PlatformTenantDBDO> dbdos = new ArrayList<>();
        cmds.forEach(cmd -> {
            Date date = new Date(System.currentTimeMillis());
            PlatformTenantDBDO dbdo = new PlatformTenantDBDO();
            BeanUtils.copyProperties(cmd, dbdo);
            if (dbdo.getId() == null) {
                dbdo.setTableFlag(TABLE_FLAG2);
                // 服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb
                String dbName = config.getDbnames().get(cmd.getModelType() - 1);
                if (StringUtils.isBlank(dbName)) {
                    throw PlatformException.buildException(B_PLATFORM_DB_NAME_ERROR);
                }
                // 字符串转小写
                dbdo.setTenantNo(dbdo.getTenantNo().toLowerCase());
                dbdo.setDbName(dbName + "_" + dbdo.getTenantNo());
            }
            dbdo.setTenantNo(dbdo.getTenantNo().toLowerCase());
            dbdo.setDbName(dbdo.getDbName().toLowerCase());
            dbdo.setCreateTime(date);
            dbdo.setUpdateTime(date);
            dbdos.add(dbdo);
        });
        return dbdos;
    }

    public static List<PlatformTenantDBRpcDTO> toPlatformTenantDBRpcDTOList(List<PlatformTenantDBDO> dbdos) {
        List<PlatformTenantDBRpcDTO> rpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dbdos)) {
            dbdos.forEach(dbdo -> {
                PlatformTenantDBRpcDTO rpcDTO = new PlatformTenantDBRpcDTO();
                BeanUtils.copyProperties(dbdo, rpcDTO);
                rpcDTOS.add(rpcDTO);
            });
        }
        return rpcDTOS;
    }

    public static TenantUrlDTO getTenantUrlDTO(PlatformTenantUrlDO urlDO) {
        TenantUrlDTO urlDTO = new TenantUrlDTO();
        BeanUtils.copyProperties(urlDO, urlDTO);
        return urlDTO;
    }
}
