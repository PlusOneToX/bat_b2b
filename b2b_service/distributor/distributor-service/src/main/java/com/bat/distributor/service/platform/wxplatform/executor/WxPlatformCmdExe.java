package com.bat.distributor.service.platform.wxplatform.executor;

import static com.bat.distributor.service.common.Constant.OPERATION_TYPE_1;
import static com.bat.distributor.service.common.Constant.OPERATION_TYPE_2;
import static com.bat.distributor.service.platform.executor.ErrorCode.B_DISTRIBUTOR_PLATFORM_APP_ID_ERROR;
import static com.bat.distributor.service.platform.executor.ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.platform.dto.PlatformDistributorCmd;
import com.bat.distributor.api.platform.dto.WxPlatformCmd;
import com.bat.distributor.dao.platform.WxPlatformDistributorMapper;
import com.bat.distributor.dao.platform.WxPlatformMapper;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.distributor.service.platform.convertor.PlatformConvertor;

@Component
public class WxPlatformCmdExe {

    @Resource
    private WxPlatformMapper wxPlatformMapper;

    @Resource
    private WxPlatformDistributorMapper wxPlatformDistributorMapper;

    /**
     * 创建分销商微信平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createWxPlatform(WxPlatformCmd cmd) {
        WxPlatformDO platformDO = PlatformConvertor.toWxPlatformDO(cmd);
        try {
            wxPlatformMapper.insert(platformDO);
        } catch (DuplicateKeyException e) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_APP_ID_ERROR);
        }
        saveSysPlatformDistributor(platformDO.getId(), cmd, OPERATION_TYPE_1);
    }

    /**
     * 保存分销商微信平台分销关联数据
     * 
     */
    private void saveSysPlatformDistributor(Integer wxPlatformId, WxPlatformCmd cmd, Short operationType) {
        if (operationType.equals(OPERATION_TYPE_2)) {
            wxPlatformDistributorMapper.deleteByWxPlatformId(wxPlatformId);
        }
        List<PlatformDistributorCmd> distributors = cmd.getDistributors();
        if (!CollectionUtils.isEmpty(distributors)) {
            wxPlatformDistributorMapper
                .insertList(PlatformConvertor.toWxPlatformDistributorDOList(wxPlatformId, distributors));
        }
    }

    /**
     * 更新分销商微信平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWxPlatform(WxPlatformCmd cmd) {
        WxPlatformDO beforePlatformDO = wxPlatformMapper.selectByPrimaryKey(cmd.getId());
        if (beforePlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        WxPlatformDO afterPlatformDO = PlatformConvertor.toWxPlatformDO(cmd);
        afterPlatformDO.setCreateTime(beforePlatformDO.getCreateTime());
        wxPlatformMapper.updateByPrimaryKey(afterPlatformDO);
        saveSysPlatformDistributor(afterPlatformDO.getId(), cmd, OPERATION_TYPE_2);
    }

    /**
     * 根据Id删除分销商微信平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteWxPlatform(BaseId cmd) {
        wxPlatformDistributorMapper.deleteByWxPlatformId(cmd.getId());
        wxPlatformMapper.deleteByPrimaryKey(cmd.getId());
    }

}
