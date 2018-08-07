package com.bat.system.service.organization;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.organization.OrganizationService;
import com.bat.system.api.organization.dto.OrganizationCreateCmd;
import com.bat.system.api.organization.dto.OrganizationIds;
import com.bat.system.api.organization.dto.OrganizationQry;
import com.bat.system.api.organization.dto.OrganizationUpdateCmd;
import com.bat.system.api.organization.dto.data.OrganizationDTO;
import com.bat.system.service.organization.executor.OrganizationCmdExc;
import com.bat.system.service.organization.executor.OrganizationQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 20:11
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationQryExc organizationQryExc;

    @Resource
    private OrganizationCmdExc organizationCmdExc;

    @Override
    public PageInfo<OrganizationDTO> listOrganization(OrganizationQry qry) {
        return organizationQryExc.listOrganization(qry);
    }

    @Override
    public OrganizationDTO getOrganizationById(Integer id) {
        return organizationQryExc.getOrganizationById(id);
    }

    @Override
    public boolean updateOrganization(OrganizationUpdateCmd cmd) {
        return organizationCmdExc.updateOrganization(cmd);
    }

    @Override
    public boolean deleteOrganizationById(Integer id) {
        return organizationCmdExc.deleteOrganizationById(id);
    }

    @Override
    public List<OrganizationDTO> getOrganizationByIds(OrganizationIds ids) {
        return organizationQryExc.getOrganizationByIds(ids);
    }

    @Override
    public boolean createOrganization(OrganizationCreateCmd cmd) {
        return organizationCmdExc.createOrganization(cmd);
    }

}
