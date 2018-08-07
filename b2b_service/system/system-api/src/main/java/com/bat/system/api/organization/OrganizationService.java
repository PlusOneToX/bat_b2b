package com.bat.system.api.organization;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.organization.dto.OrganizationCreateCmd;
import com.bat.system.api.organization.dto.OrganizationIds;
import com.bat.system.api.organization.dto.OrganizationQry;
import com.bat.system.api.organization.dto.OrganizationUpdateCmd;
import com.bat.system.api.organization.dto.data.OrganizationDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 11:53
 */
public interface OrganizationService {
    /**
     * 添加组织
     * 
     * @param cmd
     * @return
     */
    boolean createOrganization(OrganizationCreateCmd cmd);

    /**
     * 分页查询组织
     * 
     * @param qry
     * @return
     */
    PageInfo<OrganizationDTO> listOrganization(OrganizationQry qry);

    /**
     * 根据ID获取组织
     * 
     * @param id
     * @return
     */
    OrganizationDTO getOrganizationById(Integer id);

    /**
     * 根据ID 更新组织
     * 
     * @param cmd
     * @return
     */
    boolean updateOrganization(OrganizationUpdateCmd cmd);

    /**
     * 根据ID删除组织
     * 
     * @param id
     * @return
     */
    boolean deleteOrganizationById(Integer id);

    /**
     * 根据ID集合获取多个组织
     * 
     * @param ids
     * @return
     */
    List<OrganizationDTO> getOrganizationByIds(OrganizationIds ids);
}
