package com.bat.system.service.organization.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.organization.dto.OrganizationCreateCmd;
import com.bat.system.api.organization.dto.OrganizationUpdateCmd;
import com.bat.system.api.organization.dto.data.OrganizationDTO;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class OrganizationConvertor {
    public static OrganizationDO toOrganizationDO(OrganizationCreateCmd cmd) {
        OrganizationDO organizationDO = new OrganizationDO();
        BeanUtils.copyProperties(cmd, organizationDO);
        Date date = new Date();
        organizationDO.setCreateTime(date);
        organizationDO.setUpdateTime(date);
        organizationDO.setStatus((short)1);
        return organizationDO;
    }

    public static OrganizationDO toOrganizationDO(OrganizationUpdateCmd cmd) {
        OrganizationDO organizationDO = new OrganizationDO();
        BeanUtils.copyProperties(cmd, organizationDO);
        Date date = new Date();
        organizationDO.setUpdateTime(date);
        return organizationDO;
    }

    public static OrganizationDTO toOrganizationDTO(OrganizationDO organizationDO) {
        OrganizationDTO organizationDTO = new OrganizationDTO();
        BeanUtils.copyProperties(organizationDO, organizationDTO);
        return organizationDTO;
    }

    public static List<OrganizationDTO> toOrganizationDTOList(List<OrganizationDO> organizationDOList) {
        return organizationDOList.stream().map(organizationDO -> {
            OrganizationDTO organizationDTO = new OrganizationDTO();
            BeanUtils.copyProperties(organizationDO, organizationDTO);
            return organizationDTO;
        }).collect(Collectors.toList());
    }
}
