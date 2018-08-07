package com.bat.distributor.service.role.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.distributor.api.role.dto.RoleCmd;
import com.bat.distributor.api.role.dto.data.RoleDTO;
import com.bat.distributor.dao.role.dataobject.RoleDO;
import org.springframework.beans.BeanUtils;

public class RoleConvertor {
    /**
     * 分销商角色数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static RoleDO toRoleDo(RoleCmd cmd) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(cmd, roleDO);
        Date date = new Date(System.currentTimeMillis());
        roleDO.setCreateTime(date);
        roleDO.setUpdateTime(date);
        return roleDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param doList
     * @return
     */
    public static List<RoleDTO> toRoleDTOList(List<RoleDO> doList) {
        List<RoleDTO> dtoList = new ArrayList<>();
        doList.forEach(roleDO -> {
            RoleDTO dto = new RoleDTO();
            BeanUtils.copyProperties(roleDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    /**
     * do转dto
     * 
     * @param roleDO
     * @return
     */
    public static RoleDTO toRoleDTO(RoleDO roleDO) {
        RoleDTO dto = new RoleDTO();
        BeanUtils.copyProperties(roleDO, dto);
        return dto;
    }

}
