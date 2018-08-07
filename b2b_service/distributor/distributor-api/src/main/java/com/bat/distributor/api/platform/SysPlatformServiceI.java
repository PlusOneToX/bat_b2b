package com.bat.distributor.api.platform;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.platform.dto.SysPlatformListQry;
import com.bat.distributor.api.platform.dto.data.SysPlatformDTO;
import com.bat.distributor.api.platform.dto.data.SysPlatformListDTO;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.platform.dto.SysPlatformCmd;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 8:19
 */
public interface SysPlatformServiceI {
    /**
     * 根据搜索条件查找分销商系统平台列表(分页)
     */
    PageInfo<SysPlatformListDTO> listSysPlatform(SysPlatformListQry qry);

    /**
     * 根据分销商系统平台id获取分销商系统平台详情
     */
    SysPlatformDTO getSysPlatform(BaseId qry);

    /**
     * 添加分销商系统平台
     */
    void createSysPlatform(SysPlatformCmd cmd);

    /**
     * 更新分销商系统平台
     */
    void updateSysPlatform(SysPlatformCmd cmd);

    /**
     * 根据分销商系统平台id删除分销商系统平台
     */
    void deleteSysPlatform(BaseId cmd);
}
