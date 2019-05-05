package com.bat.dubboapi.system.user.api;

import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.user.dto.UserLoginRpcQry;

import java.util.List;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/19 19:47
 */
public interface SystemUserServiceRpc {
    /**
     * 根据用户id获取用户信息 包含组织 部门
     * 
     * @param id
     * @return
     */
    Response<UserRpcDTO> getUserById(Integer id);

    /**
     * 根据用户id获取用户信息 包含组织 部门
     *
     * @param id
     * @return
     */
    Response<List<UserRpcDTO>> getUserByIds(List<Integer> id);

    /**
     * 根据部门ids 获取部门下的用户
     * 
     * @param ids
     * @return
     */
    Response<List<UserRpcDTO>> getUserByDepartmentIds(List<Integer> ids);

    /**
     * 查找拥有的后台人员ids， 如果全局，返回null
     * 
     * @param userId
     * @return
     */
    Response<List<Integer>> findOwnSaleIds(Integer userId);

    /**
     * 登录接口
     * 
     * @param qry
     * @return
     */
    Response<UserRpcDTO> userLogin(UserLoginRpcQry qry);

    /**
     * 条件查询系统用户列表
     * @param status
     * @return
     */
    Response<List<UserRpcDTO>> listByCondition(Short status);
}
