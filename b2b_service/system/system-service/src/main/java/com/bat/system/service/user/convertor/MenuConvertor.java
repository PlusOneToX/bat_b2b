package com.bat.system.service.user.convertor;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.user.dto.MenuCreateCmd;
import com.bat.system.api.user.dto.MenuUpdateCmd;
import com.bat.system.api.user.dto.data.MenuDTO;
import com.bat.system.dao.user.dataobject.MenuDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class MenuConvertor {

    public static MenuDO toMenuDO(MenuCreateCmd cmd) {
        if (cmd != null) {
            MenuDO menuDO = new MenuDO();
            BeanUtils.copyProperties(cmd, menuDO);
            return menuDO;
        }
        return null;
    }

    public static MenuDO toMenuDO(MenuUpdateCmd cmd) {
        if (cmd != null) {
            MenuDO menuDO = new MenuDO();
            BeanUtils.copyProperties(cmd, menuDO);
            return menuDO;
        }
        return null;
    }

    public static MenuDTO toMenuDTO(MenuDO menuDO) {
        if (menuDO != null) {
            MenuDTO menuDTO = new MenuDTO();
            BeanUtils.copyProperties(menuDO, menuDTO);
            return menuDTO;
        }
        return null;
    }

    public static List<MenuDTO> toMenuDTOList(List<MenuDO> menuDOList) {
        return menuDOList.stream().map(MenuConvertor::toMenuDTO).collect(Collectors.toList());
    }
}
