package com.bat.distributor.service.role.executor;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.role.dto.RoleCmd;
import com.bat.distributor.api.role.dto.RoleId;
import com.bat.distributor.api.role.dto.RoleOpenCmd;
import com.bat.distributor.dao.role.RoleMapper;
import com.bat.distributor.dao.role.dataobject.RoleDO;
import com.bat.distributor.service.common.Constant;
import com.bat.distributor.service.role.convertor.RoleConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleCmdExe {

    @Resource
    private RoleMapper mapper;

    /**
     * 创建分销商角色
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleCmd cmd) {
        RoleDO roleDO = RoleConvertor.toRoleDo(cmd);
        mapper.insert(roleDO);
    }

    /**
     * 更新分销商角色
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleCmd cmd) {
        RoleDO roleDO = RoleConvertor.toRoleDo(cmd);
        mapper.updateByPrimaryKey(roleDO);
    }

    /**
     * 更新分销商角色状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openRole(RoleOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(Constant.OPEN_NO)) {
            // 停用分销商类别前，确保分销商类别下的分销商都已转移或删除
            Integer count = mapper.getRoleContactsCount(cmd.getId());
            if (count > 0) {
                throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_ROLE_CONTACTNOTNUL);
            }
        }
        mapper.openRole(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 根据Id删除分销商角色
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(RoleId cmd) {
        RoleDO roleDO = mapper.selectByPrimaryKey(cmd.getId());
        if (roleDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_ROLE_NULL);
        }
        // 停用的分销商类别才允许删除
        if (!roleDO.getOpenFlag().equals(Constant.OPEN_NO)) {
            throw DistributorException.buildException(com.bat.distributor.service.group.executor.ErrorCode.B_DISTRIBUTOR_GROUP_DELETE_OPEN_ERROR);
        }
        mapper.deleteByPrimaryKey(cmd.getId());
    }

}
