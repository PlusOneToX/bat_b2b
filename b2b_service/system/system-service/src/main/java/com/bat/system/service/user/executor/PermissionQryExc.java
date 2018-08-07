package com.bat.system.service.user.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.user.dto.PermissionQry;
import com.bat.system.api.user.dto.data.PermissionDTO;
import com.bat.system.api.user.dto.data.PermissionSyncDTO;
import com.bat.system.dao.user.PermissionMapper;
import com.bat.system.dao.user.dataobject.PermissionDO;
import com.bat.system.service.user.convertor.PermissionConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class PermissionQryExc {
    @Resource
    private PermissionMapper permissionMapper;

    public PageInfo<PermissionDTO> listPermission(PermissionQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<PermissionDO> permissionDOList = permissionMapper.listAll();
        PageInfo pageInfo = new PageInfo(permissionDOList);
        List<PermissionDTO> toPermissionDTOList = PermissionConvertor.toPermissionDTOList(pageInfo.getList());
        pageInfo.setList(toPermissionDTOList);
        return pageInfo;
    }

    public PermissionDTO getPermissionById(Integer id) {
        PermissionDO permissionDO = permissionMapper.selectByPrimaryKey(id);
        return permissionDO == null ? null : PermissionConvertor.toPermissionDTO(permissionDO);
    }

    public List<PermissionSyncDTO> listPermissionTree() {
        List<PermissionDO> permissionDOS = permissionMapper.listAll();
        List<PermissionSyncDTO> cmds = new ArrayList<>();
        Map<String, List<PermissionDO>> collect =
            permissionDOS.stream().collect(Collectors.groupingBy(PermissionDO::getService));
        for (Map.Entry<String, List<PermissionDO>> stringListEntry : collect.entrySet()) {
            PermissionSyncDTO permissionSyncDTO = new PermissionSyncDTO();
            permissionSyncDTO.setTitle(stringListEntry.getKey());
            PermissionSyncDTO.AuthDTO authDTO = new PermissionSyncDTO.AuthDTO();
            List<PermissionSyncDTO.AuthDTO.ActionsDTO> actionsDTOS = new ArrayList<>();
            Map<String, List<PermissionDO>> collect1 =
                stringListEntry.getValue().stream().collect(Collectors.groupingBy(PermissionDO::getModule));
            for (Map.Entry<String, List<PermissionDO>> listEntry : collect1.entrySet()) {
                String module = listEntry.getKey();
                PermissionSyncDTO.AuthDTO.ActionsDTO actionsDTO = new PermissionSyncDTO.AuthDTO.ActionsDTO();
                actionsDTO.setName(module);
                List<PermissionSyncDTO.AuthDTO.ActionsDTO.ChildrenDTO> dtos = new ArrayList<>();
                listEntry.getValue().forEach(permissionDO -> {
                    PermissionSyncDTO.AuthDTO.ActionsDTO.ChildrenDTO childrenDTO =
                        new PermissionSyncDTO.AuthDTO.ActionsDTO.ChildrenDTO();
                    childrenDTO.setName(permissionDO.getPermissionName());
                    childrenDTO.setModule(permissionDO.getPermissionModule());
                    permissionSyncDTO.setSort(permissionDO.getSort());
                    dtos.add(childrenDTO);
                });
                actionsDTO.setChildren(dtos.stream().distinct().collect(Collectors.toList()));
                actionsDTOS.add(actionsDTO);
            }
            authDTO.setActions(actionsDTOS);
            permissionSyncDTO.setAuth(authDTO);
            cmds.add(permissionSyncDTO);
        }
        return cmds;
    }

    public List<Map<String, String>> getPermissionUserMapping() {
        return permissionMapper.listByPermissionUserMapping();
    }
}
