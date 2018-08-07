package com.bat.system.service.globalsetting;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.ShopSettingService;
import com.bat.system.api.globalsetting.dto.ShopSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.service.globalsetting.executor.ShopSettingCmdExc;
import com.bat.system.service.globalsetting.executor.ShopSettingQryExc;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 14:11
 */
@Service
@Slf4j
public class ShopSettingServiceImpl implements ShopSettingService {
    @Resource
    private ShopSettingQryExc shopSettingQryExc;
    @Resource
    private ShopSettingCmdExc shopSettingCmdExc;

    @Override
    public ShopSettingDTO getShopSetting() {
        return shopSettingQryExc.getShopSetting();
    }

    @Override
    public boolean updateShopSetting(ShopSettingUpdateCmd cmd) {
        return shopSettingCmdExc.updateShopSetting(cmd);
    }
}
