package com.bat.platform.api.user;

import com.github.pagehelper.PageInfo;
import com.bat.platform.api.user.dto.UserCmd;
import com.bat.platform.api.user.dto.UserListQry;
import com.bat.platform.api.user.dto.UserLoginQry;
import com.bat.platform.api.user.dto.data.UserDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface UserServiceI {
    /**
     * 获取平台用户列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<UserDTO> listUser(UserListQry qry);

    /**
     * 用户登录
     * @param qry
     * @return
     */
    UserDTO login(UserLoginQry qry);

    void add(UserCmd cmd);

    void update(UserCmd cmd);

    void deleteById(Integer id);

    UserDTO detail(Integer id);
}
