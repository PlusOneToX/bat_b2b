package com.bat.platform.api.tenant;

import java.util.List;

import com.bat.platform.api.base.BaseId;
import com.bat.platform.api.tenant.dto.TenantDBCmd;
import com.bat.platform.api.tenant.dto.TenantNoQry;
import com.bat.platform.api.tenant.dto.data.TenantDBDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface TenantDBServiceI {
    /**
     * 根据租户编码查询平台租户数据库列表信息
     * 
     * @param qry
     * @return
     */
    List<TenantDBDTO> executeDB(TenantNoQry qry);

    /**
     * 新增修改平台租户数据库信息
     * 
     * @param cmds
     */
    void createUpdateDB(List<TenantDBCmd> cmds);

    /**
     * 根据租户数据库id自动生成数据库表
     * 
     * @param cmd
     */
    void createDBTable(BaseId cmd);

    /**
     * 根据租户数据库id删除数据库
     * 
     * @param cmd
     */
    void deleteDB(BaseId cmd);

}
