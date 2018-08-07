package com.bat.system.service.storesetting;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.MobileService;
import com.bat.system.api.storesetting.dto.MobileCreateCmd;
import com.bat.system.api.storesetting.dto.MobileQry;
import com.bat.system.api.storesetting.dto.MobileReleaseCmd;
import com.bat.system.api.storesetting.dto.MobileUpdateCmd;
import com.bat.system.api.storesetting.dto.data.MobileDTO;
import com.bat.system.service.storesetting.executor.MobileCmdExc;
import com.bat.system.service.storesetting.executor.MobileQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:13
 */
@Service
public class MobileServiceImpl implements MobileService {

    @Resource
    private MobileQryExc mobileQryExc;

    @Resource
    private MobileCmdExc mobileCmdExc;

    @Override
    public PageInfo<MobileDTO> listMobile(MobileQry qry) {
        return mobileQryExc.listMobile(qry);
    }

    @Override
    public boolean deleteMobileItemById(Integer id) {
        return mobileCmdExc.deleteMobileItemById(id);
    }

    @Override
    public MobileDTO getMobileById(Integer id) {
        return mobileQryExc.getMobileById(id);
    }

    @Override
    public boolean createMobile(MobileCreateCmd cmd) {
        return mobileCmdExc.createMobile(cmd);
    }

    @Override
    public boolean updateMobile(MobileUpdateCmd cmd) {
        return mobileCmdExc.updateMobile(cmd);
    }

    @Override
    public MobileDTO getMobileByModuleType(Short moduleType) {
        return mobileQryExc.getMobileByModuleType(moduleType);
    }

    @Override
    public boolean releaseMobile(MobileReleaseCmd cmd) {
        return mobileCmdExc.releaseMobile(cmd);
    }

    @Override
    public boolean deleteMobileById(Integer id) {
        return mobileCmdExc.deleteMobileById(id);
    }

}
