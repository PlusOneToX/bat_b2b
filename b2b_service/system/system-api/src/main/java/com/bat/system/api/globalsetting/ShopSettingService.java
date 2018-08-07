package com.bat.system.api.globalsetting;

import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.api.globalsetting.dto.ShopSettingUpdateCmd;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 13:46
 */
public interface ShopSettingService {
    /**
     * 删除商店设置
     * 
     * @return
     */
    ShopSettingDTO getShopSetting();

    /**
     * 更新商店设置
     * 
     * @param cmd
     * @return
     */
    boolean updateShopSetting(ShopSettingUpdateCmd cmd);
}
