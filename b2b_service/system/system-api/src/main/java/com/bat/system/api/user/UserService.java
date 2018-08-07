package com.bat.system.api.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.RockAccountInfoDTO;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.api.user.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface UserService {
    /**
     * 添加后台用户
     *
     * @param cmd
     * @return
     */
    boolean createUser(UserCreateCmd cmd);

    /**
     * 根据ID获取后台用户
     *
     * @param id
     * @return
     */
    UserDTO getUserById(Integer id);

    /**
     * 获取所有后台用户（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<UserDTO> listUser(UserQry qry);

    /**
     * 根据id删除后台用户
     *
     * @param id
     * @return
     */
    boolean deleteUserById(Integer id);

    /**
     * 更新后台用户
     * 
     * @param cmd
     * @return
     */
    boolean updateUser(UserUpdateCmd cmd);

    /**
     * 用户登录
     *
     * @param qry
     * @return
     */
    UserDTO userLogin(UserLoginQry qry);

    /**
     * 根据ID集合获取多个后台用户
     * 
     * @param ids
     * @return
     */
    List<UserDTO> getUserByIds(UserIds ids);

    /**
     * 获取子业务员
     * 
     * @param qry
     * @return
     */
    PageInfo<UserDTO> listSalesMan(UserQry qry);

    /**
     * 更新用户状态
     * 
     * @param cmd
     * @return
     */
    boolean updateUserStatus(UserStatusUpdateCmd cmd);

    /**
     * 同步用户
     * 
     * @param id
     * @return
     */
    UserDTO syncUser(Integer id);

    /**
     * 更新用户密码
     * 
     * @param cmd
     */
    void updateUserPassword(UserStatusPasswordCmd cmd);

    /**
     * 获取钉钉用户列表
     * 
     * @return
     */
    List<RockAccountInfoDTO> findRockAccountInfoList();

}
