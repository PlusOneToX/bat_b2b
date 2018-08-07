package com.bat.distributor.api.platform;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.platform.dto.data.WxPlatformDTO;
import com.bat.distributor.api.platform.dto.data.WxPlatformListDTO;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.platform.dto.WxPlatformCmd;
import com.bat.distributor.api.platform.dto.WxPlatformListQry;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 8:19
 */
public interface WxPlatformServiceI {
    /**
     * 根据搜索条件查找分销商微信平台列表(分页)
     */
    PageInfo<WxPlatformListDTO> listWxPlatform(WxPlatformListQry qry);

    /**
     * 根据分销商微信平台id获取分销商微信平台详情
     */
    WxPlatformDTO getWxPlatform(BaseId qry);

    /**
     * 添加分销商微信平台
     */
    void createWxPlatform(WxPlatformCmd cmd);

    /**
     * 更新分销商微信平台
     */
    void updateWxPlatform(WxPlatformCmd cmd);

    /**
     * 根据分销商微信平台id删除分销商微信平台
     */
    void deleteWxPlatform(BaseId cmd);
}
