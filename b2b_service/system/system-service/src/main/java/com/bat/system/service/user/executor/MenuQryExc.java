package com.bat.system.service.user.executor;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.user.dto.MenuManagerSyncDTO;
import com.bat.system.api.user.dto.MenuQry;
import com.bat.system.api.user.dto.MenuShowSyncDTO;
import com.bat.system.api.user.dto.data.MenuDTO;
import com.bat.system.dao.user.MenuMapper;
import com.bat.system.dao.user.dataobject.MenuDO;
import com.bat.system.service.user.convertor.MenuConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class MenuQryExc {
    @Resource
    private MenuMapper menuMapper;

    public PageInfo<MenuDTO> listMenu(MenuQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<MenuDO> menuDOList = menuMapper.listMenuAll();
        PageInfo pageInfo = new PageInfo(menuDOList);
        List<MenuDTO> toMenuDTOList = MenuConvertor.toMenuDTOList(pageInfo.getList());
        pageInfo.setList(toMenuDTOList);
        return pageInfo;
    }

    public MenuDTO getMenuById(Integer id) {
        MenuDO menuDO = menuMapper.selectByPrimaryKey(id);
        return menuDO == null ? null : MenuConvertor.toMenuDTO(menuDO);
    }

    public List<Map.Entry<String, List<MenuShowSyncDTO>>> listMenuTree() {
        List<MenuDO> menuDOS = menuMapper.listMenuAll();
        Map<String, List<MenuShowSyncDTO>> menuSyncDTOS = new TreeMap<>();
        Map<String, List<MenuDO>> collect = menuDOS.stream().sorted(Comparator.comparingInt(MenuDO::getSort))
            .collect(Collectors.groupingBy(MenuDO::getServiceEn));
        for (Map.Entry<String, List<MenuDO>> stringListEntry : collect.entrySet()) {
            List<MenuDO> value = stringListEntry.getValue();
            Map<String, List<MenuDO>> collect1 = value.stream().sorted(Comparator.comparingInt(MenuDO::getSort))
                .collect(Collectors.groupingBy(MenuDO::getModule));
            List<MenuShowSyncDTO> dtos = new ArrayList<>();
            for (Map.Entry<String, List<MenuDO>> listEntry : collect1.entrySet()) {
                MenuShowSyncDTO menuSyncDTO = new MenuShowSyncDTO();
                menuSyncDTO.setTitle(listEntry.getKey());
                List<MenuShowSyncDTO.ChildrenDTO> collect2 = listEntry.getValue().stream().map(menuDO -> {
                    MenuShowSyncDTO.ChildrenDTO childrenDTO = new MenuShowSyncDTO.ChildrenDTO();
                    childrenDTO.setName(menuDO.getMenu());
                    childrenDTO.setLink(menuDO.getMenuEn());
                    childrenDTO.setSort(menuDO.getSort());
                    return childrenDTO;
                }).sorted(Comparator.comparingInt(MenuShowSyncDTO.ChildrenDTO::getSort)).collect(Collectors.toList());
                menuSyncDTO.setChildren(collect2);
                menuSyncDTO.setSort(menuSyncDTO.getChildren().get(0).getSort());
                dtos.add(menuSyncDTO);
            }
            dtos.sort(Comparator.comparingInt(MenuShowSyncDTO::getSort));
            menuSyncDTOS.put(stringListEntry.getKey(), dtos);
        }
        List<Map.Entry<String, List<MenuShowSyncDTO>>> list = new ArrayList<>(menuSyncDTOS.entrySet());
        Collections.sort(list, Comparator.comparingInt(o -> o.getValue().get(0).getSort()));
        return list;
    }

    public List<MenuManagerSyncDTO> listManagerTree() {
        List<MenuDO> menuDOS = menuMapper.listManagerAll();
        List<MenuManagerSyncDTO> cmds = new ArrayList<>();
        Map<String, List<MenuDO>> collect = menuDOS.stream().collect(Collectors.groupingBy(MenuDO::getService));
        for (Map.Entry<String, List<MenuDO>> stringListEntry : collect.entrySet()) {
            MenuManagerSyncDTO menuSyncDTO = new MenuManagerSyncDTO();
            menuSyncDTO.setTitle(stringListEntry.getKey());

            MenuManagerSyncDTO.AuthDTO authDTO = new MenuManagerSyncDTO.AuthDTO();
            List<MenuManagerSyncDTO.AuthDTO.MenusDTO> menusDTOS = new ArrayList<>();
            Map<String, List<MenuDO>> collect1 =
                stringListEntry.getValue().stream().collect(Collectors.groupingBy(MenuDO::getModule));

            for (Map.Entry<String, List<MenuDO>> listEntry : collect1.entrySet()) {
                String module = listEntry.getKey();
                MenuManagerSyncDTO.AuthDTO.MenusDTO menusDTO = new MenuManagerSyncDTO.AuthDTO.MenusDTO();
                menusDTO.setName(module);
                List<MenuManagerSyncDTO.AuthDTO.MenusDTO.ChildrenDTO> dtos = new ArrayList<>();
                listEntry.getValue().forEach(menuDO -> {
                    MenuManagerSyncDTO.AuthDTO.MenusDTO.ChildrenDTO childrenDTO =
                        new MenuManagerSyncDTO.AuthDTO.MenusDTO.ChildrenDTO();
                    childrenDTO.setName(menuDO.getMenu());
                    childrenDTO.setModule(menuDO.getMenuEn());
                    menuSyncDTO.setSort(menuDO.getSort());
                    dtos.add(childrenDTO);
                });
                menusDTO.setChildren(dtos);
                menusDTOS.add(menusDTO);
            }
            authDTO.setMenus(menusDTOS);

            menuSyncDTO.setAuth(authDTO);
            cmds.add(menuSyncDTO);
        }
        return cmds;
    }
}
