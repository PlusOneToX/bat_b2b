package com.bat.platform.service.tenant.executor;

import static com.bat.platform.service.common.Constant.SOURCE_TYPE1;
import static com.bat.platform.service.common.Constant.SOURCE_TYPE2;
import static com.bat.platform.service.tenant.executor.ErrorCode.B_PLATFORM_DB_MODEL_TYPE_ERROR;
import static com.bat.platform.service.tenant.executor.ErrorCode.B_PLATFORM_DB_TABLE_ERROR;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.platform.api.base.BaseId;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantDBCmd;
import com.bat.platform.dao.tenant.PlatformTenantDBMapper;
import com.bat.platform.dao.tenant.PlatformTenantServiceDBMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;
import com.bat.platform.service.common.config.PlatformDBConfig;
import com.bat.platform.service.message.MessageSendService;
import com.bat.platform.service.tenant.convertor.TenantConvertor;
import com.bat.platform.service.tenant.validator.TenantValidator;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/4 16:01
 */
@Component
public class TenantDBCmdExe {

    @Resource
    private PlatformTenantServiceDBMapper tenantServiceDBMapper;

    @Resource
    private PlatformTenantDBMapper tenantDBMapper;
    @Resource
    private PlatformDBConfig config;
    @Resource
    private TenantValidator tenantValidator;
    @Resource
    private MessageSendService sendService;

    /**
     * 新增修改平台租户数据库信息
     * 
     * @param cmds
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUpdateDB(List<TenantDBCmd> cmds) {
        List<PlatformTenantDBDO> updateDbdos = new ArrayList<>();
        List<PlatformTenantDBDO> addDbdos = new ArrayList<>();
        // 数据库更新
        List<TenantDBCmd> updateCmds = cmds.stream().filter(cmd -> cmd.getId() != null).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(updateCmds)) {
            updateDbdos.addAll(TenantConvertor.toPlatformTenantDBDOList(updateCmds));
        }
        // 手动生成数据源
        List<TenantDBCmd> manualCreateCmds = cmds.stream()
            .filter(
                cmd -> cmd.getId() == null && (cmd.getSourceType() == null || cmd.getSourceType().equals(SOURCE_TYPE2)))
            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(manualCreateCmds)) {
            addDbdos.addAll(TenantConvertor.toPlatformTenantDBDOList(manualCreateCmds));
        }
        // 自动生成数据源
        List<TenantDBCmd> autoCreateCmds = cmds.stream()
            .filter(
                cmd -> cmd.getId() == null && cmd.getSourceType() != null && cmd.getSourceType().equals(SOURCE_TYPE1))
            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(autoCreateCmds)) {
            List<PlatformTenantDBDO> autoCreateDOS =
                TenantConvertor.toPlatformTenantDBDOAutoList(autoCreateCmds, config);
            autoCreateDOS.forEach(autoCreateDO -> {
                tenantServiceDBMapper.createTenantDB(autoCreateDO.getDbName());
                autoCreateDO.setDbUrl(
                    autoCreateDO.getDbBaseUrl() + autoCreateDO.getDbName() + "?" + config.getDbconnectparam());
            });
            addDbdos.addAll(autoCreateDOS);
        }
        if (!CollectionUtils.isEmpty(updateDbdos)) {
            tenantValidator.checkTenantDB(updateDbdos);
            tenantDBMapper.updateList(updateDbdos);
        }
        if (!CollectionUtils.isEmpty(addDbdos)) {
            tenantValidator.checkTenantDB(addDbdos);
            try {
                tenantDBMapper.insertList(addDbdos);
            } catch (DuplicateKeyException e) {
                throw PlatformException.buildException(B_PLATFORM_DB_MODEL_TYPE_ERROR);
            }
        }
    }

    /**
     * 根据租户数据库id自动生成数据表
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void createDBTable(BaseId cmd) {
        PlatformTenantDBDO dbdo = tenantValidator.getPlatformTenantDBDOById(cmd.getId());
        String dbName = config.getDbnames().get(dbdo.getModelType() - 1);
        try {
            // 通过数据库名称，反射获取要创建数据库表的列表数据
            PropertyDescriptor pd = new PropertyDescriptor(dbName, config.getClass());
            Method readMethod = pd.getReadMethod();
            Object invoke = readMethod.invoke(config);
            if (invoke != null && invoke instanceof List) {
                List<String> dbTables = (List<String>)invoke;
                dbTables.forEach(dbTable -> {
                    dbTable = dbTable.replace("database", dbdo.getDbName());
                    tenantServiceDBMapper.createTenantDBTable(dbTable);
                });
            } else {
                throw PlatformException.buildException(B_PLATFORM_DB_TABLE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw PlatformException.buildException(B_PLATFORM_DB_TABLE_ERROR);
        }
    }

    /**
     * 根据租户数据库id删除数据库(实际数据库不会删除，如果删除实际数据库和表，需手动删除)
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDB(BaseId cmd) {
        PlatformTenantDBDO dbdo = tenantValidator.getPlatformTenantDBDOById(cmd.getId());
        tenantDBMapper.deleteByPrimaryKey(dbdo.getId());
        // 发送更新其他服务租户数据库缓存消息
        List<PlatformTenantDBDO> dbdos = tenantDBMapper.selectByDbName(dbdo.getDbName());
        // 当其他租户还在使用此数据库时，不能清除数据库缓存，只清楚租户信息
        if (!CollectionUtils.isEmpty(dbdos) && dbdos.size() == 1) {
            sendService.tenantDBDelete(dbdo.getTenantNo(), dbdo.getModelType(), dbdo);
        } else {
            sendService.tenantDBDelete(dbdo.getTenantNo(), dbdo.getModelType(), null);
        }
    }
}
