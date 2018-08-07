package com.bat.system.service.storesetting;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.BannerService;
import com.bat.system.api.storesetting.dto.BannerCreateCmd;
import com.bat.system.api.storesetting.dto.BannerQry;
import com.bat.system.api.storesetting.dto.BannerUpdateCmd;
import com.bat.system.api.storesetting.dto.data.BannerDTO;
import com.bat.system.service.storesetting.executor.BannerCmdExc;
import com.bat.system.service.storesetting.executor.BannerQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:13
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerQryExc bannerQryExc;

    @Resource
    private BannerCmdExc bannerCmdExc;

    @Override
    public PageInfo<BannerDTO> listBanner(BannerQry qry) {
        return bannerQryExc.listBanner(qry);
    }

    @Override
    public boolean createBanner(BannerCreateCmd cmd) {
        return bannerCmdExc.createBanner(cmd);
    }

    @Override
    public boolean updateBanner(BannerUpdateCmd cmd) {
        return bannerCmdExc.updateBanner(cmd);
    }

    @Override
    public boolean deleteBannerById(Integer id) {
        return bannerCmdExc.deleteBannerById(id);
    }

    @Override
    public boolean sortBannerUp(Integer id) {
        return bannerCmdExc.sortBannerUp(id);
    }

    @Override
    public boolean sortBannerDown(Integer id) {
        return bannerCmdExc.sortBannerDown(id);
    }

}
