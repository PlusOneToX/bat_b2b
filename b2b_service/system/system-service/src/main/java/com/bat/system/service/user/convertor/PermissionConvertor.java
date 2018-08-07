package com.bat.system.service.user.convertor;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.user.dto.PermissionCreateCmd;
import com.bat.system.api.user.dto.PermissionUpdateCmd;
import com.bat.system.api.user.dto.data.PermissionDTO;
import com.bat.system.dao.user.dataobject.PermissionDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class PermissionConvertor {

    public static PermissionDO toPermissionDO(PermissionCreateCmd cmd) {
        PermissionDO permissionDO = new PermissionDO();
        BeanUtils.copyProperties(cmd, permissionDO);
        return permissionDO;
    }

    public static PermissionDO toPermissionDO(PermissionUpdateCmd cmd) {
        PermissionDO permissionDO = new PermissionDO();
        BeanUtils.copyProperties(cmd, permissionDO);
        return permissionDO;
    }

    public static PermissionDTO toPermissionDTO(PermissionDO permissionDO) {
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permissionDO, permissionDTO);
        return permissionDTO;
    }

    public static List<PermissionDTO> toPermissionDTOList(List<PermissionDO> permissionDOList) {
        return permissionDOList.stream().map(permissionDO -> {
            PermissionDTO permissionDTO = new PermissionDTO();
            BeanUtils.copyProperties(permissionDO, permissionDTO);
            return permissionDTO;
        }).collect(Collectors.toList());
    }
}
