package com.bat.distributor.service.platform.executor;

import static com.bat.distributor.service.common.Constant.OPEN_NO;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.platform.dto.PlatformCmd;
import com.bat.distributor.api.platform.dto.PlatformOpenCmd;
import com.bat.distributor.dao.platform.PlatformMapper;
import com.bat.distributor.dao.platform.dataobject.PlatformDO;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlatformCmdExe {

    @Resource
    private PlatformMapper mapper;

    /**
     * 创建分销商平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createPlatform(PlatformCmd cmd) {
        PlatformDO platformDO = PlatformConvertor.toPlatformDO(cmd);
        mapper.insert(platformDO);
    }

    /**
     * 更新分销商平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePlatform(PlatformCmd cmd) {
        PlatformDO beforePlatformDO = mapper.selectByPrimaryKey(cmd.getId());
        if (beforePlatformDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        PlatformDO afterPlatformDO = PlatformConvertor.toPlatformDO(cmd);
        afterPlatformDO.setCreateTime(beforePlatformDO.getCreateTime());
        mapper.updateByPrimaryKey(afterPlatformDO);
    }

    /**
     * 更新分销商平台状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openPlatform(PlatformOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(OPEN_NO)) {
            // TODO 停用分销商平台条件
        }
        mapper.openPlatform(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 根据Id删除分销商平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePlatform(BaseId cmd) {
        PlatformDO platformDO = mapper.selectByPrimaryKey(cmd.getId());
        if (platformDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        // TODO 删除分销商平台条件
        if (!platformDO.getOpenFlag().equals(OPEN_NO)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_PLATFORM_OPEN_ERROR);
        }
        mapper.deleteByPrimaryKey(cmd.getId());
    }

}
