package com.bat.system.service.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.system.api.user.MenuService;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.MenuDTO;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.dto.*;
import com.bat.system.service.user.executor.MenuCmdExc;
import com.bat.system.service.user.executor.MenuQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuQryExc menuQryExc;

    @Resource
    private MenuCmdExc menuCmdExc;

    @Override
    public boolean createMenu(MenuCreateCmd cmd) {
        return menuCmdExc.createMenu(cmd);
    }

    @Override
    public MenuDTO getMenuById(Integer id) {
        return menuQryExc.getMenuById(id);
    }

    @Override
    public PageInfo<MenuDTO> listMenu(MenuQry qry) {
        return menuQryExc.listMenu(qry);
    }

    @Override
    public boolean deleteMenuById(Integer id) {
        return menuCmdExc.deleteMenuById(id);
    }

    @Override
    public boolean updateMenu(MenuUpdateCmd cmd) {
        return menuCmdExc.updateMenu(cmd);
    }

    @Override
    public List<Map.Entry<String, List<MenuShowSyncDTO>>> listMenuTree() {
        return menuQryExc.listMenuTree();
    }

    @Override
    public List<MenuManagerSyncDTO> listManagerTree() {
        return menuQryExc.listManagerTree();
    }

    @Override
    public void syncMenu(List<MenuCreateCmd> map) {
        menuCmdExc.syncMenu(map);
    }

}