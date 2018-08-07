package com.bat.system.service.user.executor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.user.dto.UserCreateCmd;
import com.bat.system.api.user.dto.UserStatusPasswordCmd;
import com.bat.system.api.user.dto.UserStatusUpdateCmd;
import com.bat.system.api.user.dto.UserUpdateCmd;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.dao.organization.DepartmentMapper;
import com.bat.system.dao.organization.OrganizationMapper;
import com.bat.system.dao.organization.dataobject.DepartmentDO;
import com.bat.system.dao.user.UserBrandMapper;
import com.bat.system.dao.user.UserMapper;
import com.bat.system.dao.user.UserRoleMapper;
import com.bat.system.dao.user.UserSaleMapper;
import com.bat.system.dao.user.dataobject.UserBrandDO;
import com.bat.system.dao.user.dataobject.UserDO;
import com.bat.system.dao.user.dataobject.UserRoleDO;
import com.bat.system.dao.user.dataobject.UserSaleDO;
import com.bat.system.mq.dto.SalesmanDTO;
import com.bat.system.service.message.MessageSendService;
import com.bat.system.service.user.convertor.UserConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.thirdparty.erp.dto.system.data.UserErpRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
@Slf4j
public class UserCmdExc {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserBrandMapper userBrandMapper;
    @Resource
    private UserSaleMapper userSaleMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private MessageSendService messageSendService;

    @Transactional(rollbackFor = SystemException.class)
    public boolean createUser(UserCreateCmd cmd) {
        // 保存用户信息
        UserDO userDO = UserConvertor.toUserDO(cmd);
        try {
            userMapper.insert(userDO);
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_user_name")) {
                throw SystemException.buildException(ErrorCode.B_USER_NAME_EXISTS);
            }
        }
        insertRelation(userDO.getId(), cmd.getRoleIds(), cmd.getBrandIds(), cmd.getSaleIds());
        // 保存分销商与订单数据权限
        return true;
    }

    public boolean deleteUserById(Integer id) {
        if (userMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_USER_ID_NOT_EXISTS);
        }
        return userMapper.deleteByPrimaryKey(id) == 1;
    }

    @Transactional(rollbackFor = SystemException.class)
    public boolean updateUser(UserUpdateCmd cmd) {
        UserDO userDO = userMapper.selectByPrimaryKey(cmd.getId());
        if (userDO == null) {
            throw SystemException.buildException(ErrorCode.B_USER_ID_NOT_EXISTS);
        }
        try {
            // 更新用户信息
            userMapper.updateByPrimaryKeySelective(UserConvertor.toUserDO(cmd));
            if (userDO.getDepartmentId() != null && !userDO.getDepartmentId().equals(cmd.getDepartmentId())) {
                // 如果部门发生变化 发送消息 更新可视
                log.info("{}:的部门发生变化，原部门id：{}，新部门id：{}。更新分销商可视", cmd.getId(), userDO.getDepartmentId(),
                    cmd.getDepartmentId());
                try {
                    SalesmanDTO salesmanDTO = new SalesmanDTO();
                    salesmanDTO.setSalesId(cmd.getId());
                    salesmanDTO.setNewDepartmentId(cmd.getDepartmentId());
                    salesmanDTO.setOldDepartmentId(userDO.getDepartmentId());
                    messageSendService.updateDistributorVisibleBySalesId(salesmanDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_user_name")) {
                throw SystemException.buildException(ErrorCode.B_USER_NAME_EXISTS);
            } else if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_erp_user_no")) {
                throw SystemException.buildException(ErrorCode.B_ERP_USER_NO_EXISTS);
            }
        }
        insertRelation(cmd.getId(), cmd.getRoleIds(), cmd.getBrandIds(), cmd.getSaleIds());
        return true;
    }

    private void insertRelation(Integer id, List<Integer> roleIds, List<Integer> brandIds, List<Integer> saleIds) {
        // 先删除旧的关联关系
        userRoleMapper.deleteByUserId(id);
        // 添加新的角色信息
        if (!CollectionUtils.isEmpty(roleIds)) {
            roleIds.forEach(integer -> {
                UserRoleDO userRoleDO = new UserRoleDO();
                userRoleDO.setUserId(id);
                userRoleDO.setRoleId(integer);
                userRoleMapper.insert(userRoleDO);
            });
        }

        // 先删除旧的关联关系
        userBrandMapper.deleteByUserId(id);
        // 保存品牌信息
        if (!CollectionUtils.isEmpty(brandIds)) {
            brandIds.forEach(integer -> {
                UserBrandDO userBrandDO = new UserBrandDO();
                userBrandDO.setUserId(id);
                userBrandDO.setBrandId(integer);
                userBrandMapper.insert(userBrandDO);
            });
        }

        // 先删除旧的关联关系
        userSaleMapper.deleteByUserId(id);
        // 保存品牌信息
        if (!CollectionUtils.isEmpty(brandIds)) {
            saleIds.forEach(integer -> {
                UserSaleDO userSaleDO = new UserSaleDO();
                userSaleDO.setUserId(id);
                userSaleDO.setSaleUserId(integer);
                userSaleMapper.insert(userSaleDO);
            });
        }
    }

    public boolean updateUserStatus(UserStatusUpdateCmd cmd) {
        UserDO userDO = userMapper.selectByPrimaryKey(cmd.getId());
        userDO.setStatus(cmd.getStatus());
        userMapper.updateByPrimaryKeySelective(userDO);
        return true;
    }

    public UserDTO syncUser(UserDTO userById, UserErpRpcDTO rpcDTO) {
        UserDO userDO = userMapper.selectByPrimaryKey(userById.getId());
        if (userDO != null) {
            DepartmentDO departmentDO = departmentMapper.getByErpDepartmentId(rpcDTO.getErpDepartmentNo());
            log.info("{}:比较部门：原部门id：{}，新部门id：{}", userById.getId(), userDO.getDepartmentId(), departmentDO.getId());
            if (!userDO.getDepartmentId().equals(departmentDO.getId())) {
                // 如果部门发生变化 发送消息 更新可视
                log.info("{}:的部门发生变化，原部门id：{}，新部门id：{}。更新分销商可视", userById.getId(), userDO.getDepartmentId(),
                    departmentDO.getId());
                try {
                    SalesmanDTO salesmanDTO = new SalesmanDTO();
                    salesmanDTO.setSalesId(userById.getId());
                    salesmanDTO.setNewDepartmentId(departmentDO.getId());
                    salesmanDTO.setOldDepartmentId(userDO.getDepartmentId());
                    messageSendService.updateDistributorVisibleBySalesId(salesmanDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            userDO.setDepartmentId(departmentDO.getId());
            userDO.setOrganizationId(departmentDO.getOrganizationId());
            userMapper.updateByPrimaryKeySelective(userDO);
            userById.setDepartmentId(departmentDO.getId());
            userById.setOrganizationId(departmentDO.getOrganizationId());
        }
        return userById;
    }

    public void updateUserPassword(UserStatusPasswordCmd cmd) {
        UserDO userDO = userMapper.selectByPrimaryKey(cmd.getId());
        if (userDO == null) {
            throw SystemException.buildException(ErrorCode.B_USER_ID_NOT_EXISTS);
        }
        String password = userDO.getPassword();
        if (!password.equals(cmd.getOldPassword())) {
            throw SystemException.buildException(ErrorCode.B_OLD_PASSWORD_ERROR);
        }
        userDO.setPassword(cmd.getNewPassword());
        userDO.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(userDO);
    }
}
