package com.bat.system.service.globalsetting.executor;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.dao.globalsetting.ShopSettingMapper;
import com.bat.system.dao.globalsetting.dataobject.ShopSettingDO;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class ShopSettingQryExc {
    @Resource
    private ShopSettingMapper shopSettingMapper;

    public ShopSettingDTO getShopSetting() {
        ShopSettingDTO dto = new ShopSettingDTO();
        ShopSettingDO customized_attend_event_flag = shopSettingMapper.selectByPrimaryKey("customized_attend_event_flag");
        if(customized_attend_event_flag != null){
            dto.setCustomizedAttendEventFlag(
                    Integer.valueOf(shopSettingMapper.selectByPrimaryKey("customized_attend_event_flag").getValue()));
        }
        ShopSettingDO direct_transportation_event_flag = shopSettingMapper.selectByPrimaryKey("direct_transportation_event_flag");
        if(direct_transportation_event_flag != null){
            dto.setDirectTransportationEventFlag(
                    Integer.valueOf(shopSettingMapper.selectByPrimaryKey("direct_transportation_event_flag").getValue()));
        }
        ShopSettingDO hint = shopSettingMapper.selectByPrimaryKey("hint");
        if(hint != null){
            dto.setHint(shopSettingMapper.selectByPrimaryKey("hint").getValue());
        }
        ShopSettingDO mto_attend_event_flag = shopSettingMapper.selectByPrimaryKey("mto_attend_event_flag");
        if(mto_attend_event_flag != null){
            dto.setMtoAttendEventFlag(
                    Integer.valueOf(shopSettingMapper.selectByPrimaryKey("mto_attend_event_flag").getValue()));
        }
        ShopSettingDO newproduct_time = shopSettingMapper.selectByPrimaryKey("newproduct_time");
        if (newproduct_time != null){
            dto.setNewproductTime(Integer.valueOf(shopSettingMapper.selectByPrimaryKey("newproduct_time").getValue()));
        }
        ShopSettingDO no_stiff_use_hint = shopSettingMapper.selectByPrimaryKey("no_stiff_use_hint");
        if(no_stiff_use_hint != null){
            dto.setNoStiffUseHint(Integer.valueOf(shopSettingMapper.selectByPrimaryKey("no_stiff_use_hint").getValue()));
        }
        ShopSettingDO on_way_attend_event_flag = shopSettingMapper.selectByPrimaryKey("on_way_attend_event_flag");
        if(on_way_attend_event_flag != null){
            dto.setOnWayAttendEventFlag(
                    Integer.valueOf(shopSettingMapper.selectByPrimaryKey("on_way_attend_event_flag").getValue()));
        }
        ShopSettingDO stiff_use_hint = shopSettingMapper.selectByPrimaryKey("stiff_use_hint");
        if(stiff_use_hint != null){
            dto.setStiffUseHint(Integer.valueOf(shopSettingMapper.selectByPrimaryKey("stiff_use_hint").getValue()));
        }
        ShopSettingDO stock_show_flag = shopSettingMapper.selectByPrimaryKey("stock_show_flag");
        if(stock_show_flag != null){
            dto.setStockShowFlag(Integer.valueOf(shopSettingMapper.selectByPrimaryKey("stock_show_flag").getValue()));
        }
        ShopSettingDO stock_show_number = shopSettingMapper.selectByPrimaryKey("stock_show_number");
        if(stock_show_number != null){
            dto.setStockShowNumber(Integer.valueOf(shopSettingMapper.selectByPrimaryKey("stock_show_number").getValue()));
        }
        return dto;
    }

    public Integer getShopSettingByKey(String key) {
        ShopSettingDO shopSettingDO = shopSettingMapper.selectByPrimaryKey(key);
        if(shopSettingDO != null){
            return Integer.valueOf(shopSettingMapper.selectByPrimaryKey(key).getValue());
        }else {
            return 0;
        }
    }
}
