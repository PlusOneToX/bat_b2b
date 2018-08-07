package com.bat.distributor.service.platform.sysplatform;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.platform.SysPlatformServiceI;
import com.bat.distributor.api.platform.dto.SysPlatformCmd;
import com.bat.distributor.api.platform.dto.SysPlatformListQry;
import com.bat.distributor.api.platform.dto.data.SysPlatformDTO;
import com.bat.distributor.api.platform.dto.data.SysPlatformListDTO;
import com.bat.distributor.service.platform.sysplatform.executor.SysPlatformQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.service.platform.sysplatform.executor.SysPlatformCmdExe;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 12:04
 */
@Service
public class SysPlatformServiceImpl implements SysPlatformServiceI {

    @Resource
    private SysPlatformCmdExe cmdExe;

    @Resource
    private SysPlatformQryExe qryExe;

    @Override
    public PageInfo<SysPlatformListDTO> listSysPlatform(SysPlatformListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public SysPlatformDTO getSysPlatform(BaseId qry) {
        return qryExe.execute(qry);
    }

    @Override
    public void createSysPlatform(SysPlatformCmd cmd) {
        cmdExe.createSysPlatform(cmd);
    }

    @Override
    public void updateSysPlatform(SysPlatformCmd cmd) {
        cmdExe.updateSysPlatform(cmd);
    }

    @Override
    public void deleteSysPlatform(BaseId cmd) {
        cmdExe.deleteSysPlatform(cmd);
    }
}
