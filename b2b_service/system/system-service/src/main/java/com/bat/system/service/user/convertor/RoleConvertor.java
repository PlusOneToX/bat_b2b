package com.bat.system.service.user.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.user.dto.RoleCreateCmd;
import com.bat.system.api.user.dto.RoleUpdateCmd;
import com.bat.system.api.user.dto.data.RoleDTO;
import com.bat.system.dao.user.dataobject.RoleDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class RoleConvertor {

    public static RoleDO toRoleDO(RoleCreateCmd cmd) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(cmd, roleDO);
        Date date = new Date();
        roleDO.setCreateTime(date);
        roleDO.setUpdateTime(date);
        return roleDO;
    }

    public static RoleDO toRoleDO(RoleUpdateCmd cmd) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(cmd, roleDO);
        Date date = new Date();
        roleDO.setUpdateTime(date);
        return roleDO;
    }

    public static RoleDTO toRoleDTO(RoleDO roleDO) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleDO, roleDTO);
        return roleDTO;
    }

    public static List<RoleDTO> toRoleDTOList(List<RoleDO> roleDOList) {
        return roleDOList.stream().map(roleDO -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(roleDO, roleDTO);
            return roleDTO;
        }).collect(Collectors.toList());
    }
}
