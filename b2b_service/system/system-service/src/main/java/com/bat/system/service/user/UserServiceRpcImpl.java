package com.bat.system.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.MessageUtils;
import com.bat.system.api.organization.dto.data.DepartmentDTO;
import com.bat.system.api.organization.dto.data.OrganizationDTO;
import com.bat.system.api.user.dto.UserLoginQry;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.dao.user.dataobject.UserDO;
import com.bat.system.service.organization.executor.DepartmentQryExc;
import com.bat.system.service.organization.executor.OrganizationQryExc;
import com.bat.system.service.user.executor.ErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.UserLoginRpcQry;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.system.service.user.constant.UserSaleScope;
import com.bat.system.service.user.executor.UserQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/19 19:48
 */
@DubboService
@Slf4j
public class UserServiceRpcImpl implements SystemUserServiceRpc {

    @Resource
    private UserQryExc userQryExc;

    @Resource
    private DepartmentQryExc departmentQryExc;

    @Resource
    private OrganizationQryExc organizationQryExc;

    @Override
    public Response<UserRpcDTO> getUserById(Integer id) {
        // log.info("getUserById:{}", id);
        UserDTO userById = userQryExc.getUserById(id);
        if (userById != null) {
            UserRpcDTO userRpcDTO = getUserRpcDTO(userById);
            return Response.of(userRpcDTO);
        }
        return Response.buildFailure(ErrorCode.B_USER_NOT_EXISTS, MessageUtils.get(ErrorCode.B_USER_NOT_EXISTS));
    }

    @Override
    public Response<List<UserRpcDTO>> getUserByIds(List<Integer> ids) {
        // log.info("getUserByIds:{}", ids);
        List<UserDTO> userByIds = userQryExc.getUserByIds(ids);
        if (CollectionUtils.isNotEmpty(userByIds)) {
            List<UserRpcDTO> collect = userByIds.stream().map(this::getUserRpcDTO).collect(Collectors.toList());
            return Response.of(collect);
        }
        return Response.buildFailure(ErrorCode.B_USER_NOT_EXISTS, MessageUtils.get(ErrorCode.B_USER_NOT_EXISTS));
    }

    @NotNull
    private UserRpcDTO getUserRpcDTO(UserDTO userDTO) {
        UserRpcDTO userRpcDTO = new UserRpcDTO();
        BeanUtils.copyProperties(userDTO, userRpcDTO);
        DepartmentRpcDTO departmentDTO = new DepartmentRpcDTO();
        OrganizationRpcDTO organizationRpcDTO = new OrganizationRpcDTO();
        DepartmentDTO departmentById = departmentQryExc.getDepartmentById(userDTO.getDepartmentId());
        OrganizationDTO organizationById = organizationQryExc.getOrganizationById(userDTO.getOrganizationId());
        if (departmentById != null) {
            BeanUtils.copyProperties(departmentById, departmentDTO);
        }
        if (organizationById != null) {
            BeanUtils.copyProperties(organizationById, organizationRpcDTO);
        }
        userRpcDTO.setDepartmentDTO(departmentDTO);
        userRpcDTO.setOrganizationDTO(organizationRpcDTO);
        return userRpcDTO;
    }

    @Override
    public Response<List<UserRpcDTO>> getUserByDepartmentIds(List<Integer> ids) {
        List<UserDO> userByDepartmentIds = userQryExc.getUserByDepartmentIds(ids);
        if (CollectionUtils.isNotEmpty(userByDepartmentIds)) {
            List<UserRpcDTO> collect = userByDepartmentIds.stream().map(userDO -> {
                UserRpcDTO dto = new UserRpcDTO();
                BeanUtils.copyProperties(userDO, dto);
                return dto;
            }).collect(Collectors.toList());
            return Response.of(collect);
        }
        return Response.buildFailure(ErrorCode.B_USER_NOT_EXISTS, MessageUtils.get(ErrorCode.B_USER_NOT_EXISTS));
    }

    @Override
    public Response<List<Integer>> findOwnSaleIds(Integer userId) {
        List<Integer> userIds = new ArrayList<>(16);
        UserDTO userById = userQryExc.getUserById(userId);
        if (userById == null) {
            return Response.buildFailure(ErrorCode.B_USER_NOT_EXISTS, MessageUtils.get(ErrorCode.B_USER_NOT_EXISTS));
        }
        if (userById.getSaleScope() == UserSaleScope.ALL) {
            return Response.of(userIds);
        } else if (userById.getSaleScope() == UserSaleScope.SINGLE) {
            userIds.add(userId);
            return Response.of(userIds);
        } else if (userById.getSaleScope() == UserSaleScope.MULTIPLE) {
            return Response.of(userById.getSaleIds());
        }
        return Response.buildFailure(ErrorCode.B_ERROR, MessageUtils.get(ErrorCode.B_ERROR));
    }

    @Override
    public Response<UserRpcDTO> userLogin(UserLoginRpcQry qry) {
        if (qry != null) {
            UserLoginQry qry1 = new UserLoginQry();
            BeanUtils.copyProperties(qry, qry1);
            UserDTO userDTO = userQryExc.userLogin(qry1);
            if (userDTO != null) {
                UserRpcDTO rpcDTO = new UserRpcDTO();
                BeanUtils.copyProperties(userDTO, rpcDTO);
                return Response.of(rpcDTO);
            } else {
                return Response.buildFailure(ErrorCode.B_USER_NAME_OR_PASSWORD_ERROR,
                    MessageUtils.get(ErrorCode.B_USER_NAME_OR_PASSWORD_ERROR));
            }
        } else {
            return Response.buildFailure(ErrorCode.B_USER_NAME_OR_PASSWORD_ERROR,
                MessageUtils.get(ErrorCode.B_USER_NAME_OR_PASSWORD_ERROR));
        }
    }

    @Override
    public Response<List<UserRpcDTO>> listByCondition(Short status) {
        List<UserDO> userDOList = userQryExc.listByCondition(status);
        return Response.of(com.bat.system.api.common.BeanUtils.copyList(userDOList, UserRpcDTO.class));
    }
}
