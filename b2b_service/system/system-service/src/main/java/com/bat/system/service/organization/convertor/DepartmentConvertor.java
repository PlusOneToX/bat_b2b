package com.bat.system.service.organization.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.organization.dto.DepartmentCreateCmd;
import com.bat.system.api.organization.dto.DepartmentUpdateCmd;
import com.bat.system.api.organization.dto.data.DepartmentDTO;
import com.bat.system.dao.organization.dataobject.DepartmentDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class DepartmentConvertor {

    public static DepartmentDO toDepartmentDO(DepartmentCreateCmd cmd) {
        DepartmentDO departmentDO = new DepartmentDO();
        BeanUtils.copyProperties(cmd, departmentDO);
        Date date = new Date();
        departmentDO.setCreateTime(date);
        departmentDO.setUpdateTime(date);
        departmentDO.setStatus((short)1);
        return departmentDO;
    }

    public static DepartmentDO toDepartmentDO(DepartmentUpdateCmd cmd) {
        DepartmentDO departmentDO = new DepartmentDO();
        BeanUtils.copyProperties(cmd, departmentDO);
        Date date = new Date();
        departmentDO.setUpdateTime(date);
        return departmentDO;
    }

    public static DepartmentDTO toDepartmentDTO(DepartmentDO departmentDO) {
        DepartmentDTO organizationDTO = new DepartmentDTO();
        BeanUtils.copyProperties(departmentDO, organizationDTO);
        return organizationDTO;
    }

    public static List<DepartmentDTO> toDepartmentDTOList(List<DepartmentDO> departmentDOList) {
        return departmentDOList.stream().map(departmentDO -> {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            BeanUtils.copyProperties(departmentDO, departmentDTO);
            return departmentDTO;
        }).collect(Collectors.toList());
    }

}
