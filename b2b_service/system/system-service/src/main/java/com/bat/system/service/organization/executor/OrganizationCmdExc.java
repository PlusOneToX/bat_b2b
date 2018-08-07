package com.bat.system.service.organization.executor;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.organization.dto.OrganizationCreateCmd;
import com.bat.system.api.organization.dto.OrganizationUpdateCmd;
import com.bat.system.dao.organization.OrganizationMapper;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import com.bat.system.service.organization.convertor.OrganizationConvertor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class OrganizationCmdExc {

    @Resource
    private OrganizationMapper organizationMapper;

    public boolean createOrganization(OrganizationCreateCmd cmd) {
        OrganizationDO organizationDO = organizationMapper.getByErpOrganizationId(cmd.getErpOrganizationId());
        if (organizationDO != null) {
            throw SystemException.buildException(ErrorCode.B_ERP_ORGANIZATION_ID_EXISTS);
        }
        return organizationMapper.insert(OrganizationConvertor.toOrganizationDO(cmd)) == 1;
    }

    public boolean deleteOrganizationById(Integer id) {
        if (organizationMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_ORGANIZATION_ID_NOT_EXISTS);
        }
        return organizationMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateOrganization(OrganizationUpdateCmd cmd) {
        if (organizationMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_ORGANIZATION_ID_NOT_EXISTS);
        }
        try {
            organizationMapper.updateByPrimaryKeySelective(OrganizationConvertor.toOrganizationDO(cmd));
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_erp_organization_id")) {
                throw SystemException.buildException(ErrorCode.B_ERP_ORGANIZATION_ID_EXISTS);
            }
        }
        return true;
    }
}
