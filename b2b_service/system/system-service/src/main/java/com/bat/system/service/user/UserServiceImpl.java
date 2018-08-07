package com.bat.system.service.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.organization.DepartmentService;
import com.bat.system.api.user.UserService;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.RockAccountInfoDTO;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.service.user.executor.UserCmdExc;
import com.bat.system.service.user.executor.UserQryExc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.ThirdPartySystemDingTalkServiceRpc;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.UserDetailListResp;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartySystemServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.system.UserErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.UserErpRpcDTO;
import com.bat.system.api.user.dto.*;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserQryExc userQryExc;

    @Resource
    private UserCmdExc userCmdExc;

    @Resource
    private DepartmentService departmentService;

    @DubboReference(check = false, timeout = 1000)
    private ThirdPartySystemServiceErpRpc thirdPartySystemServiceErpRpc;

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartySystemDingTalkServiceRpc thirdPartySystemDingTalkServiceRpc;

    @Resource
    private PermissionServiceImpl permissionService;

    @Override
    public boolean createUser(UserCreateCmd cmd) {
        boolean user = userCmdExc.createUser(cmd);
        permissionService.syncCache();
        return user;
    }

    /**
     * 查询用户的时候 如果有内码就从ERP 更新用户信息
     * 
     * @param id
     * @return
     */
    @SneakyThrows
    @Override
    public UserDTO getUserById(Integer id) {
        UserDTO userDTO = null;
        try {
            userDTO = syncUser(id);
            if (userDTO != null) {
                return userDTO;
            } else {
                return userQryExc.getUserById(id);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return userQryExc.getUserById(id);
        }
    }

    @Override
    public PageInfo<UserDTO> listUser(UserQry qry) {
        return userQryExc.listUser(qry);
    }

    @Override
    public boolean deleteUserById(Integer id) {
        boolean b = userCmdExc.deleteUserById(id);
        permissionService.syncCache();
        return b;
    }

    @Override
    public boolean updateUser(UserUpdateCmd cmd) {
        boolean b = userCmdExc.updateUser(cmd);
        permissionService.syncCache();
        return b;
    }

    @Override
    public UserDTO userLogin(UserLoginQry qry) {
        return userQryExc.userLogin(qry);
    }

    @Override
    public List<UserDTO> getUserByIds(UserIds ids) {
        return userQryExc.getUserByIds(ids.getIds());
    }

    @Override
    public PageInfo<UserDTO> listSalesMan(UserQry qry) {
        return userQryExc.listSalesMan(qry);
    }

    @Override
    public boolean updateUserStatus(UserStatusUpdateCmd cmd) {
        return userCmdExc.updateUserStatus(cmd);
    }

    @SneakyThrows
    @Override
    public UserDTO syncUser(Integer id) {
        UserDTO userById = userQryExc.getUserById(id);
        if (userById != null && StringUtils.isNotBlank(userById.getErpUserNo())) {
            log.info("用户有ERP内码，查询最新的组织");
            UserErpRpcQry qry = new UserErpRpcQry();
            qry.setErpUserNo(userById.getErpUserNo());
            Response<List<UserErpRpcDTO>> listResponse = thirdPartySystemServiceErpRpc.listUser(qry);
            if (listResponse.isSuccess()) {
                List<UserErpRpcDTO> userErpRpcDTOS = listResponse.getData();
                if (CollectionUtils.isNotEmpty(userErpRpcDTOS)) {
                    UserErpRpcDTO userErpRpcDTO = userErpRpcDTOS.get(0);
                    if (userErpRpcDTO != null) {
                        // 先同步部门 避免关联时没有数据(耗时)
                        // departmentService.syncDepartment();
                        return userCmdExc.syncUser(userById, userErpRpcDTO);
                    }
                }
            } else {
                throw SystemException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
            }
        }
        return null;
    }

    @Override
    public void updateUserPassword(UserStatusPasswordCmd cmd) {
        userCmdExc.updateUserPassword(cmd);
    }

    @Override
    public List<RockAccountInfoDTO> findRockAccountInfoList() {
        Response<List<UserDetailListResp.ResultDTO.ListDTO>> listResponse =
            thirdPartySystemDingTalkServiceRpc.listDepartment();
        if (listResponse.isSuccess()) {
            List<UserDetailListResp.ResultDTO.ListDTO> listDTOS = listResponse.getData();
            return listDTOS.stream().map(listDTO -> {
                RockAccountInfoDTO rockAccountInfoDTO = new RockAccountInfoDTO();
                rockAccountInfoDTO.setRockAccountId(listDTO.getUserid());
                rockAccountInfoDTO.setMobile(listDTO.getMobile());
                rockAccountInfoDTO.setName(listDTO.getName());
                rockAccountInfoDTO.setSelectTitle(listDTO.getName() + "|" + listDTO.getMobile());
                return rockAccountInfoDTO;
            }).collect(Collectors.toList());
        } else {
            throw SystemException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }
}