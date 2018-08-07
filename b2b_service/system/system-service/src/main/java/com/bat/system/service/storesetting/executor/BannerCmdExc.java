package com.bat.system.service.storesetting.executor;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.storesetting.dto.BannerCreateCmd;
import com.bat.system.api.storesetting.dto.BannerUpdateCmd;
import com.bat.system.dao.storesetting.BannerMapper;
import com.bat.system.dao.storesetting.dataobject.BannerDO;
import com.bat.system.service.storesetting.convertor.BannerConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class BannerCmdExc {

    @Resource
    private BannerMapper bannerMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean createBanner(BannerCreateCmd cmd) {
        if (bannerMapper.getBySort(cmd.getSort()) != null) {
            bannerMapper.updateSort(cmd.getSort());
        }
        BannerDO bannerDO = BannerConvertor.toBannerDO(cmd);
        bannerMapper.insert(bannerDO);
        return true;

    }

    public boolean updateBanner(BannerUpdateCmd cmd) {
        if (bannerMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_BANNER_ID_NOT_EXISTS);
        }
        BannerDO bannerDO = BannerConvertor.toBannerDO(cmd);
        bannerMapper.updateByPrimaryKeySelective(bannerDO);
        return true;
    }

    public boolean deleteBannerById(Integer id) {
        if (bannerMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_BANNER_ID_NOT_EXISTS);
        }
        bannerMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sortBannerUp(Integer id) {
        BannerDO bannerDO = bannerMapper.selectByPrimaryKey(id);
        if (bannerDO == null) {
            throw SystemException.buildException(ErrorCode.B_BANNER_ID_NOT_EXISTS);
        }
        BannerDO bannerDO1 = bannerMapper.getPreOne(bannerDO.getSort());
        if (bannerDO1 != null) {
            int tmp = bannerDO.getSort();
            bannerDO.setSort(bannerDO1.getSort());
            bannerMapper.updateByPrimaryKeySelective(bannerDO);
            bannerDO1.setSort(tmp);
            bannerMapper.updateByPrimaryKeySelective(bannerDO1);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sortBannerDown(Integer id) {
        BannerDO bannerDO = bannerMapper.selectByPrimaryKey(id);
        if (bannerDO == null) {
            throw SystemException.buildException(ErrorCode.B_BANNER_ID_NOT_EXISTS);
        }
        BannerDO bannerDO1 = bannerMapper.getNextOne(bannerDO.getSort());
        if (bannerDO1 != null) {
            int tmp = bannerDO.getSort();
            bannerDO.setSort(bannerDO1.getSort());
            bannerMapper.updateByPrimaryKeySelective(bannerDO);
            bannerDO1.setSort(tmp);
            bannerMapper.updateByPrimaryKeySelective(bannerDO1);
        }
        return false;
    }
}
