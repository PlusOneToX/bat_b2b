package com.bat.system.service.user.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.user.dto.MenuCreateCmd;
import com.bat.system.api.user.dto.MenuUpdateCmd;
import com.bat.system.dao.user.MenuMapper;
import com.bat.system.dao.user.dataobject.MenuDO;
import com.bat.system.service.user.convertor.MenuConvertor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class MenuCmdExc {

    @Resource
    private MenuMapper menuMapper;

    public boolean createMenu(MenuCreateCmd cmd) {
        MenuDO menuDO = MenuConvertor.toMenuDO(cmd);
        menuMapper.insert(menuDO);
        return true;
    }

    public boolean deleteMenuById(Integer id) {
        if (menuMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_MENU_ID_NOT_EXISTS);
        }
        menuMapper.deleteByPrimaryKey(id);
        return true;
    }

    public boolean updateMenu(MenuUpdateCmd cmd) {
        if (menuMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_MENU_ID_NOT_EXISTS);
        }
        MenuDO menuDO = MenuConvertor.toMenuDO(cmd);
        menuMapper.updateByPrimaryKeySelective(menuDO);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncMenu(List<MenuCreateCmd> map) {
        for (MenuCreateCmd menuSyncDTO : map) {
            MenuDO menuDO = new MenuDO();
            BeanUtils.copyProperties(menuSyncDTO, menuDO);
            menuMapper.insert(menuDO);
        }
    }
}
