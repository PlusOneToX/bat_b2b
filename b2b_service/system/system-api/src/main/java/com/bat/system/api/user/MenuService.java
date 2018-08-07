package com.bat.system.api.user;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.MenuDTO;
import com.bat.system.api.user.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface MenuService {
    /**
     * 添加角色
     *
     * @param cmd
     * @return
     */
    boolean createMenu(MenuCreateCmd cmd);

    /**
     * 根据ID获取角色
     *
     * @param id
     * @return
     */
    MenuDTO getMenuById(Integer id);

    /**
     * 获取所有角色（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<MenuDTO> listMenu(MenuQry qry);

    /**
     * 根据id删除角色
     *
     * @param id
     * @return
     */
    boolean deleteMenuById(Integer id);

    /**
     * 更新角色
     *
     * @param cmd
     * @return
     */
    boolean updateMenu(MenuUpdateCmd cmd);

    /**
     * 返回前端调用 为了适配格式 返回树状结构
     *
     * @return
     */
    List<Map.Entry<String, List<MenuShowSyncDTO>>> listMenuTree();

    /**
     * 根据前端代码json 同步菜单(仅限迁移代码时调用)
     *
     * @param map
     */
    void syncMenu(List<MenuCreateCmd> map);

    List<MenuManagerSyncDTO> listManagerTree();
}
