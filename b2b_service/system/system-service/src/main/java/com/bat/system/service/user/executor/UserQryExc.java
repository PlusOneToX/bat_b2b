package com.bat.system.service.user.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.user.dto.UserLoginQry;
import com.bat.system.api.user.dto.UserQry;
import com.bat.system.api.user.dto.data.RoleDTO;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.dao.organization.DepartmentMapper;
import com.bat.system.dao.organization.dataobject.DepartmentDO;
import com.bat.system.dao.user.*;
import com.bat.system.dao.user.dataobject.*;
import com.bat.system.service.user.constant.UserStatus;
import com.bat.system.service.user.convertor.RoleConvertor;
import com.bat.system.service.user.convertor.UserConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.system.dao.user.*;
import com.bat.system.dao.user.dataobject.*;

import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class UserQryExc {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserLoginMapper userLoginMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserBrandMapper userBrandMapper;

    @Resource
    private UserSaleMapper userSaleMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @SneakyThrows
    public PageInfo<UserDTO> listUser(UserQry qry) {
        if (qry.getSize() > 10) {
            throw SystemException.buildException(ErrorCode.B_PAGE_SIZE_TOO_LARGE);
        }
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<UserDO> userDOList = userMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(userDOList);
        List<UserDTO> toUserDTOList = UserConvertor.toUserDTOList(pageInfo.getList());
        List<UserDTO> collect = toUserDTOList.stream().map(userDTO -> {
            List<UserRoleDO> userRoleDOList = userRoleMapper.listByUserId(userDTO.getId());
            List<RoleDTO> roleDTOList = new ArrayList<>();
            userRoleDOList.forEach(userRoleDO -> {
                RoleDO roleDO = roleMapper.selectByPrimaryKey(userRoleDO.getRoleId());
                if (roleDO != null) {
                    roleDTOList.add(RoleConvertor.toRoleDTO(roleDO));
                }
            });
            userDTO.setRoleName(roleDTOList.stream().map(RoleDTO::getRoleName).collect(Collectors.joining(",")));
            DepartmentDO departmentDO = departmentMapper.selectByPrimaryKey(userDTO.getDepartmentId());
            if (departmentDO != null) {
                userDTO.setDepartmentName(departmentDO.getDepartmentName());
            }
            UserLoginDO userLoginDO = userLoginMapper.selectByPrimaryKey(userDTO.getId());
            if (userLoginDO != null) {
                userDTO.setLastOnlineTime(userLoginDO.getLastLoginTime());
                userDTO.setTimes(userLoginDO.getLoginTimes());
            }
            return userDTO;
        }).collect(Collectors.toList());
        pageInfo.setList(collect);
        return pageInfo;
    }

    public PageInfo<UserDTO> listSalesMan(UserQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<UserDO> userDOList = userMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(userDOList);
        List<UserDTO> userDTOS = UserConvertor.toUserDTOList(pageInfo.getList());
        pageInfo.setList(userDTOS);
        return pageInfo;
    }

    public UserDTO getUserById(Integer id) {
        UserDO userDO = userMapper.selectByPrimaryKey(id);
        if (userDO != null) {
            UserDTO userDTO = UserConvertor.toUserDTO(userDO);
            List<Integer> roles = userRoleMapper.listByUserId(userDTO.getId()).stream().map(UserRoleDO::getRoleId)
                .collect(Collectors.toList());
            userDTO.setRoleIds(roles);
            userDTO.setBrandIds(userBrandMapper.listByUserId(userDO.getId()).stream().map(UserBrandDO::getBrandId)
                .collect(Collectors.toList()));
            userDTO.setSaleIds(userSaleMapper.listByUserId(userDO.getId()).stream().map(UserSaleDO::getSaleUserId)
                .collect(Collectors.toList()));
            return userDTO;
        }
        return null;
    }

    public UserDTO userLogin(UserLoginQry qry) {
        if (userMapper.getByName(qry.getUserName()) == null) {
            throw SystemException.buildException(ErrorCode.B_USER_NOT_EXISTS);
        }
        UserDO userDO = userMapper.getByUserNameAndPassword(qry.getUserName(), qry.getPassword());
        if (userDO != null) {
            if (userDO.getStatus().equals(UserStatus.DISABLE)) {
                throw SystemException.buildException(ErrorCode.B_USER_STATUS_DISABLE);
            }
            // 记录用户登录
            UserLoginDO userLoginDO = userLoginMapper.selectByPrimaryKey(userDO.getId());
            if (userLoginDO == null) {
                userLoginDO = new UserLoginDO();
                userLoginDO.setId(userDO.getId());
                userLoginDO.setLastLoginTime(new Date());
                userLoginDO.setLoginTimes(1);
                userLoginMapper.insert(userLoginDO);
            } else {
                userLoginDO.setLastLoginTime(new Date());
                userLoginDO.setLoginTimes(userLoginDO.getLoginTimes() + 1);
                userLoginMapper.updateByPrimaryKey(userLoginDO);
            }
        } else {
            throw SystemException.buildException(ErrorCode.B_PASSWORD_ERROR);
        }
        UserDTO userDTO = UserConvertor.toUserDTO(userDO);
        List<Integer> collect = userRoleMapper.listByUserId(userDTO.getId()).stream().map(UserRoleDO::getRoleId)
            .collect(Collectors.toList());
        userDTO.setRoleIds(collect);
        return userDTO;
    }

    public List<UserDTO> getUserByIds(List<Integer> ids) {
        List<UserDTO> users = new ArrayList<>();
        ids.forEach(id ->{
            UserDTO user = getUserById(id);
            if(user != null){
                users.add(user);
            }
        });
        return users;
    }

    public List<UserDO> getUserByDepartmentIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : ids) {
            sb.append(",").append(id);
        }
        String substring = sb.substring(1);
        return userMapper.listUserByDepartmentIds(substring);
    }

    public List<UserDO> listByCondition(Short status) {
        return userMapper.listByCondition(status);
    }
}
