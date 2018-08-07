package com.bat.system.service.organization.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.organization.dto.OrganizationIds;
import com.bat.system.api.organization.dto.OrganizationQry;
import com.bat.system.api.organization.dto.data.OrganizationDTO;
import com.bat.system.dao.organization.OrganizationMapper;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import com.bat.system.service.organization.convertor.OrganizationConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class OrganizationQryExc {
    @Resource
    private OrganizationMapper organizationMapper;

    public PageInfo<OrganizationDTO> listOrganization(OrganizationQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<OrganizationDO> organizationDOList = organizationMapper.listByName(qry.getName());
        PageInfo pageInfo = new PageInfo(organizationDOList);
        List<OrganizationDTO> toOrganizationDTOList = OrganizationConvertor.toOrganizationDTOList(pageInfo.getList());
        pageInfo.setList(toOrganizationDTOList);
        return pageInfo;
    }

    public OrganizationDTO getOrganizationById(Integer id) {
        OrganizationDO organizationDO = organizationMapper.selectByPrimaryKey(id);
        return organizationDO == null ? null : OrganizationConvertor.toOrganizationDTO(organizationDO);
    }

    public List<OrganizationDTO> getOrganizationByIds(OrganizationIds ids) {
        return ids.getIds().stream()
            .map(id -> OrganizationConvertor.toOrganizationDTO(organizationMapper.selectByPrimaryKey(id)))
            .collect(Collectors.toList());
    }

    public List<OrganizationDO> listByOrganizationIds(List<Integer> organizationIds) {
        return organizationMapper.listByOrganizationIds(organizationIds);
    }
}
