package com.bat.system.dao.user;

import java.util.List;

import com.bat.system.dao.user.dataobject.MenuDO;
import org.apache.ibatis.annotations.Param;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MenuDO record);

    int insertSelective(MenuDO record);

    MenuDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MenuDO record);

    int updateByPrimaryKey(MenuDO record);

    /**
     * 前端获取菜单 （暂时未用）
     * 
     * @return
     */
    List<MenuDO> listMenuAll();

    /**
     * 后台管理菜单权限
     * 
     * @return
     */
    List<MenuDO> listManagerAll();

    MenuDO getByMenuEn(@Param("menuEn") String menuEn);

    List<MenuDO> listByPrimaryKeys(@Param("ids") List<Integer> ids);
}