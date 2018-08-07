package com.bat.distributor.service.platform.wxplatform;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.platform.WxPlatformServiceI;
import com.bat.distributor.api.platform.dto.WxPlatformCmd;
import com.bat.distributor.api.platform.dto.WxPlatformListQry;
import com.bat.distributor.api.platform.dto.data.WxPlatformDTO;
import com.bat.distributor.api.platform.dto.data.WxPlatformListDTO;
import com.bat.distributor.service.platform.wxplatform.executor.WxPlatformCmdExe;
import com.bat.distributor.service.platform.wxplatform.executor.WxPlatformQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 12:05
 */
@Service
public class WxPlatformServiceImpl implements WxPlatformServiceI {

    @Resource
    private WxPlatformQryExe qryExe;

    @Resource
    private WxPlatformCmdExe cmdExe;

    @Override
    public PageInfo<WxPlatformListDTO> listWxPlatform(WxPlatformListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public WxPlatformDTO getWxPlatform(BaseId qry) {
        return qryExe.execute(qry);
    }

    @Override
    public void createWxPlatform(WxPlatformCmd cmd) {
        cmdExe.createWxPlatform(cmd);
    }

    @Override
    public void updateWxPlatform(WxPlatformCmd cmd) {
        cmdExe.updateWxPlatform(cmd);
    }

    @Override
    public void deleteWxPlatform(BaseId cmd) {
        cmdExe.deleteWxPlatform(cmd);
    }
}
