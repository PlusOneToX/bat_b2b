package com.bat.platform.service.tenant;

import java.util.List;

import javax.annotation.Resource;

import com.bat.platform.service.tenant.executor.TenantDBCmdExe;
import com.bat.platform.service.tenant.executor.TenantDBQryExe;
import org.springframework.stereotype.Service;

import com.bat.platform.api.base.BaseId;
import com.bat.platform.api.tenant.TenantDBServiceI;
import com.bat.platform.api.tenant.dto.TenantDBCmd;
import com.bat.platform.api.tenant.dto.TenantNoQry;
import com.bat.platform.api.tenant.dto.data.TenantDBDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
@Service
public class TenantDBServiceImpl implements TenantDBServiceI {

    @Resource
    TenantDBCmdExe cmdExe;
    @Resource
    TenantDBQryExe qryExe;

    @Override
    public List<TenantDBDTO> executeDB(TenantNoQry qry) {
        return qryExe.executeDB(qry);
    }

    @Override
    public void createUpdateDB(List<TenantDBCmd> cmds) {
        cmdExe.createUpdateDB(cmds);
    }

    @Override
    public void createDBTable(BaseId cmd) {
        cmdExe.createDBTable(cmd);
    }

    @Override
    public void deleteDB(BaseId cmd) {
        cmdExe.deleteDB(cmd);
    }

}
