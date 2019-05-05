package com.bat.platform.service.user;

import javax.annotation.Resource;

import com.bat.platform.api.user.dto.UserCmd;
import com.bat.platform.api.user.dto.UserLoginQry;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.platform.api.user.UserServiceI;
import com.bat.platform.api.user.dto.UserListQry;
import com.bat.platform.api.user.dto.data.UserDTO;
import com.bat.platform.service.user.executor.UserCmdExe;
import com.bat.platform.service.user.executor.UserQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/29 10:55
 */
@Service
public class UserServiceImpl implements UserServiceI {
    @Resource
    private UserQryExe qryExe;

    @Resource
    private UserCmdExe cmdExe;

    @Override
    public PageInfo<UserDTO> listUser(UserListQry qry) {
        return qryExe.listUser(qry);
    }

    @Override
    public UserDTO login(UserLoginQry qry) {
        return qryExe.login(qry);
    }

    @Override
    public void add(UserCmd cmd) {
        cmdExe.add(cmd);
    }

    @Override
    public void update(UserCmd cmd) {
        cmdExe.update(cmd);
    }

    @Override
    public void deleteById(Integer id) {
        cmdExe.deleteById(id);
    }

    @Override
    public UserDTO detail(Integer id) {
        return qryExe.detail(id);
    }
}
