package com.bat.distributor.service.role.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.api.role.dto.RoleId;
import com.bat.distributor.api.role.dto.RoleListQry;
import com.bat.distributor.api.role.dto.data.RoleDTO;
import com.bat.distributor.dao.role.RoleMapper;
import com.bat.distributor.dao.role.dataobject.RoleDO;
import com.bat.distributor.service.role.convertor.RoleConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class RoleQryExe {

    @Resource
    private RoleMapper mapper;

    /**
     * 查询分销商角色列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<GroupDTO> executeList(RoleListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<RoleDO> roleDOList = mapper.listRole(qryMap);
        PageInfo pageInfo = new PageInfo(roleDOList);
        List<GroupDTO> tagDTOList = RoleConvertor.toRoleDTOList(pageInfo.getList());
        pageInfo.setList(tagDTOList);
        return pageInfo;
    }

    /**
     * 根据分销商角色ID查询分销商角色详情
     * 
     * @param qry
     * @return
     */
    public RoleDTO execute(RoleId qry) {
        RoleDO roleDO = mapper.selectByPrimaryKey(qry.getId());
        if (roleDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_ROLE_NULL);
        }
        RoleDTO dto = RoleConvertor.toRoleDTO(roleDO);
        return dto;
    }
}
