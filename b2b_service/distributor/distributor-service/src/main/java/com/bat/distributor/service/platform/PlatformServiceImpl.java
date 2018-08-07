package com.bat.distributor.service.platform;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.platform.PlatformServiceI;
import com.bat.distributor.api.platform.dto.PlatformCmd;
import com.bat.distributor.api.platform.dto.PlatformListQry;
import com.bat.distributor.api.platform.dto.PlatformOpenCmd;
import com.bat.distributor.api.platform.dto.data.PlatformDTO;
import com.bat.distributor.service.platform.executor.PlatformCmdExe;
import com.bat.distributor.service.platform.executor.PlatformQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 11:30
 */
@Service
public class PlatformServiceImpl implements PlatformServiceI {

    @Resource
    private PlatformCmdExe cmdExe;

    @Resource
    private PlatformQryExe qryExe;

    @Override
    public PageInfo<PlatformDTO> listPlatform(PlatformListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public PlatformDTO getPlatform(BaseId qry) {
        return qryExe.execute(qry);
    }

    @Override
    public void createPlatform(PlatformCmd cmd) {
        cmdExe.createPlatform(cmd);
    }

    @Override
    public void updatePlatform(PlatformCmd cmd) {
        cmdExe.updatePlatform(cmd);
    }

    @Override
    public void openPlatform(PlatformOpenCmd cmd) {
        cmdExe.openPlatform(cmd);
    }

    @Override
    public void deletePlatform(BaseId cmd) {
        cmdExe.deletePlatform(cmd);
    }
}
