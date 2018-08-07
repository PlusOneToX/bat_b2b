package com.bat.distributor.service.platform.sysplatform.executor;

import static com.bat.distributor.service.common.Constant.OPERATION_TYPE_1;
import static com.bat.distributor.service.common.Constant.OPERATION_TYPE_2;
import static com.bat.distributor.service.platform.executor.ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.platform.dto.PlatformDistributorCmd;
import com.bat.distributor.api.platform.dto.SysPlatformApiCmd;
import com.bat.distributor.api.platform.dto.SysPlatformCmd;
import com.bat.distributor.dao.platform.SysPlatformApiMapper;
import com.bat.distributor.dao.platform.SysPlatformDistributorMapper;
import com.bat.distributor.dao.platform.SysPlatformMapper;
import com.bat.distributor.dao.platform.dataobject.SysPlatformDO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.distributor.service.platform.convertor.PlatformConvertor;

@Component
public class SysPlatformCmdExe {

    @Resource
    private SysPlatformMapper sysPlatformMapper;

    @Resource
    private SysPlatformApiMapper sysPlatformApiMapper;

    @Resource
    private SysPlatformDistributorMapper sysPlatformDistributorMapper;

    /**
     * 创建分销商系统平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSysPlatform(SysPlatformCmd cmd) {
        SysPlatformDO platformDO = PlatformConvertor.toSysPlatformDO(cmd);
        getSysAppStr(platformDO);
        sysPlatformMapper.insert(platformDO);
        saveSysPlatformApiAndDistributor(platformDO.getId(), cmd, OPERATION_TYPE_1);
    }

    /**
     * 系统生成app加密数据
     * 
     * @param platformDO
     */
    private void getSysAppStr(SysPlatformDO platformDO) {
        if (StringUtils.isBlank(platformDO.getAppId())) {
            String appId = RandomStringUtils.random(10, "0bat2018789");
            appId = "SYS" + appId;
            String appKey = RandomStringUtils.randomAlphanumeric(16);
            appKey = appKey.toUpperCase();
            String appSecret = RandomStringUtils.randomAlphanumeric(32);
            Integer count = sysPlatformMapper.selectByAppIdorAppKey(appId, appKey);
            if (count != null && count > 0) {
                getSysAppStr(platformDO);
            } else {
                platformDO.setAppId(appId);
                platformDO.setAppKey(appKey);
                platformDO.setAppSecret(appSecret);
            }
        }
    }

    /**
     * 保持分销商系统平台api和分销关联数据
     * 
     * @param sysPlatformId
     * @param cmd
     */
    private void saveSysPlatformApiAndDistributor(Integer sysPlatformId, SysPlatformCmd cmd, Short operationType) {
        if (operationType.equals(OPERATION_TYPE_2)) {
            deleteSysPlatformApiAndDistributor(sysPlatformId);
        }
        List<SysPlatformApiCmd> apis = cmd.getApis();
        if (!CollectionUtils.isEmpty(apis)) {
            sysPlatformApiMapper.insertList(PlatformConvertor.toSysPlatformApiDOList(sysPlatformId, apis));
        }
        List<PlatformDistributorCmd> distributors = cmd.getDistributors();
        if (!CollectionUtils.isEmpty(distributors)) {
            sysPlatformDistributorMapper
                .insertList(PlatformConvertor.toSysPlatformDistributorDOList(sysPlatformId, distributors));
        }
    }

    /**
     * 删除分销商平台相关数据（如api和分销关联数据）
     * 
     * @param sysPlatformId
     */
    private void deleteSysPlatformApiAndDistributor(Integer sysPlatformId) {
        sysPlatformApiMapper.deleteBySysPlatformId(sysPlatformId);
        sysPlatformDistributorMapper.deleteBySysPlatformId(sysPlatformId);
    }

    /**
     * 更新分销商系统平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSysPlatform(SysPlatformCmd cmd) {
        SysPlatformDO beforePlatformDO = sysPlatformMapper.selectByPrimaryKey(cmd.getId());
        if (beforePlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        SysPlatformDO afterPlatformDO = PlatformConvertor.toSysPlatformDO(cmd);
        afterPlatformDO.setCreateTime(beforePlatformDO.getCreateTime());
        // 如果更新的appkey为空的情况使用以前的appkey
        if (StringUtils.isBlank(afterPlatformDO.getAppId())) {
            afterPlatformDO.setAppId(beforePlatformDO.getAppId());
            afterPlatformDO.setAppKey(beforePlatformDO.getAppKey());
            afterPlatformDO.setAppSecret(beforePlatformDO.getAppSecret());
        }
        // 如果更新和以前的appkey都为空情况，重新生成appkey
        getSysAppStr(afterPlatformDO);
        sysPlatformMapper.updateByPrimaryKey(afterPlatformDO);
        saveSysPlatformApiAndDistributor(afterPlatformDO.getId(), cmd, OPERATION_TYPE_2);
    }

    /**
     * 根据Id删除分销商系统平台
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysPlatform(BaseId cmd) {
        deleteSysPlatformApiAndDistributor(cmd.getId());
        sysPlatformMapper.deleteByPrimaryKey(cmd.getId());
    }

}
