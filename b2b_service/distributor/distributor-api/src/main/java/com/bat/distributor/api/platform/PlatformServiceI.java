package com.bat.distributor.api.platform;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.platform.dto.data.PlatformDTO;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.platform.dto.PlatformCmd;
import com.bat.distributor.api.platform.dto.PlatformListQry;
import com.bat.distributor.api.platform.dto.PlatformOpenCmd;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 8:19
 */
public interface PlatformServiceI {

    /**
     * 根据搜索条件查找分销商平台列表(分页)
     */
    PageInfo<PlatformDTO> listPlatform(PlatformListQry qry);

    /**
     * 根据分销商平台id获取分销商平台详情
     */
    PlatformDTO getPlatform(BaseId qry);

    /**
     * 添加分销商平台
     */
    void createPlatform(PlatformCmd cmd);

    /**
     * 更新分销商平台
     */
    void updatePlatform(PlatformCmd cmd);

    /**
     * 更新分销商平台状态
     */
    void openPlatform(PlatformOpenCmd cmd);

    /**
     * 根据分销商平台id删除分销商平台
     */
    void deletePlatform(BaseId cmd);
}
