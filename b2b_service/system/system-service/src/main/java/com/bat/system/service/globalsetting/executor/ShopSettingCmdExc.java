package com.bat.system.service.globalsetting.executor;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.dto.ShopSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.dao.globalsetting.ShopSettingMapper;
import com.bat.system.dao.globalsetting.dataobject.ShopSettingDO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class ShopSettingCmdExc {

    @Resource
    private ShopSettingMapper shopSettingMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean updateShopSetting(ShopSettingUpdateCmd cmd) {
        ShopSettingDO customized_attend_event_flag = shopSettingMapper.selectByPrimaryKey("customized_attend_event_flag");
        if(customized_attend_event_flag != null){
            shopSettingMapper.updateByPrimaryKeySelective(
                    new ShopSettingDO("customized_attend_event_flag", cmd.getCustomizedAttendEventFlag()));
        }else {
            customized_attend_event_flag = new ShopSettingDO();
            customized_attend_event_flag.setKey("customized_attend_event_flag");
            customized_attend_event_flag.setValue(cmd.getCustomizedAttendEventFlag());
            shopSettingMapper.insert(customized_attend_event_flag);
        }
        ShopSettingDO direct_transportation_event_flag = shopSettingMapper.selectByPrimaryKey("direct_transportation_event_flag");
        if(direct_transportation_event_flag != null){
            shopSettingMapper.updateByPrimaryKeySelective(
                    new ShopSettingDO("direct_transportation_event_flag", cmd.getDirectTransportationEventFlag()));
        }else {
            direct_transportation_event_flag = new ShopSettingDO();
            direct_transportation_event_flag.setKey("direct_transportation_event_flag");
            direct_transportation_event_flag.setValue(cmd.getDirectTransportationEventFlag());
            shopSettingMapper.insert(direct_transportation_event_flag);
        }
        ShopSettingDO hint = shopSettingMapper.selectByPrimaryKey("hint");
        if(hint != null){
            shopSettingMapper.updateByPrimaryKeySelective(new ShopSettingDO("hint", cmd.getHint()));
        }else {
            hint = new ShopSettingDO();
            hint.setKey("hint");
            hint.setValue(cmd.getHint());
            shopSettingMapper.insert(hint);
        }
        ShopSettingDO mto_attend_event_flag = shopSettingMapper.selectByPrimaryKey("mto_attend_event_flag");
        if(mto_attend_event_flag != null){
            shopSettingMapper
                    .updateByPrimaryKeySelective(new ShopSettingDO("mto_attend_event_flag", cmd.getMtoAttendEventFlag()));
        }else {
            mto_attend_event_flag = new ShopSettingDO();
            mto_attend_event_flag.setKey("mto_attend_event_flag");
            mto_attend_event_flag.setValue(cmd.getMtoAttendEventFlag());
            shopSettingMapper.insert(mto_attend_event_flag);
        }
        ShopSettingDO newproduct_time = shopSettingMapper.selectByPrimaryKey("newproduct_time");
        if (newproduct_time != null){
            shopSettingMapper.updateByPrimaryKeySelective(new ShopSettingDO("newproduct_time", cmd.getNewproductTime()));
        }else {
            newproduct_time = new ShopSettingDO();
            newproduct_time.setKey("newproduct_time");
            newproduct_time.setValue(cmd.getNewproductTime());
            shopSettingMapper.insert(newproduct_time);
        }
        ShopSettingDO no_stiff_use_hint = shopSettingMapper.selectByPrimaryKey("no_stiff_use_hint");
        if(no_stiff_use_hint != null){
            shopSettingMapper.updateByPrimaryKeySelective(new ShopSettingDO("no_stiff_use_hint", cmd.getNoStiffUseHint()));
        }else {
            no_stiff_use_hint = new ShopSettingDO();
            no_stiff_use_hint.setKey("no_stiff_use_hint");
            no_stiff_use_hint.setValue(cmd.getNoStiffUseHint());
            shopSettingMapper.insert(no_stiff_use_hint);
        }
        ShopSettingDO on_way_attend_event_flag = shopSettingMapper.selectByPrimaryKey("on_way_attend_event_flag");
        if(on_way_attend_event_flag != null){
            shopSettingMapper
                    .updateByPrimaryKeySelective(new ShopSettingDO("on_way_attend_event_flag", cmd.getOnWayAttendEventFlag()));
        }else {
            on_way_attend_event_flag = new ShopSettingDO();
            on_way_attend_event_flag.setKey("on_way_attend_event_flag");
            on_way_attend_event_flag.setValue(cmd.getOnWayAttendEventFlag());
            shopSettingMapper.insert(on_way_attend_event_flag);
        }
        ShopSettingDO stiff_use_hint = shopSettingMapper.selectByPrimaryKey("stiff_use_hint");
        if(stiff_use_hint != null){
            shopSettingMapper.updateByPrimaryKeySelective(new ShopSettingDO("stiff_use_hint", cmd.getStiffUseHint()));
        }else {
            stiff_use_hint = new ShopSettingDO();
            stiff_use_hint.setKey("stiff_use_hint");
            stiff_use_hint.setValue(cmd.getStiffUseHint());
            shopSettingMapper.insert(stiff_use_hint);
        }
        ShopSettingDO stock_show_flag = shopSettingMapper.selectByPrimaryKey("stock_show_flag");
        if(stock_show_flag != null){
            shopSettingMapper.updateByPrimaryKeySelective(new ShopSettingDO("stock_show_flag", cmd.getStockShowFlag()));
        }else {
            stock_show_flag = new ShopSettingDO();
            stock_show_flag.setKey("stock_show_flag");
            stock_show_flag.setValue(cmd.getStockShowFlag());
            shopSettingMapper.insert(stock_show_flag);
        }
        ShopSettingDO stock_show_number = shopSettingMapper.selectByPrimaryKey("stock_show_number");
        if(stock_show_number != null){
            shopSettingMapper.updateByPrimaryKeySelective(new ShopSettingDO("stock_show_number", cmd.getStockShowNumber()));
        }else {
            stock_show_number = new ShopSettingDO();
            stock_show_number.setKey("stock_show_number");
            stock_show_number.setValue(cmd.getStockShowNumber());
            shopSettingMapper.insert(stock_show_number);
        }
        return true;
    }
}