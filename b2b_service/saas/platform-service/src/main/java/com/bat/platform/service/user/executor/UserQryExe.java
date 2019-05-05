package com.bat.platform.service.user.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.platform.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.user.dto.UserListQry;
import com.bat.platform.api.user.dto.UserLoginQry;
import com.bat.platform.api.user.dto.data.UserDTO;
import com.bat.platform.dao.user.PlatformUserMapper;
import com.bat.platform.dao.user.dataobject.PlatformUserDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/29 11:27
 */
@Component
public class UserQryExe {

    @Resource
    private PlatformUserMapper platformUserMapper;

    /**
     * 查询平台用户列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserDTO> listUser(UserListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<PlatformUserDO> list =
            platformUserMapper.listCOByCondition(qry.getOpenFlag(), qry.getContentType(), qry.getContent());
        PageInfo pageInfo = new PageInfo(list);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (PlatformUserDO platformUserDO : list) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(platformUserDO, userDTO);
            userDTOList.add(userDTO);
        }
        pageInfo.setList(userDTOList);
        return pageInfo;
    }

    public UserDTO login(UserLoginQry qry) {
        PlatformUserDO platformUserDO = platformUserMapper.selectByUserName(qry.getUserName());
        if (platformUserDO == null || !platformUserDO.getPassword().equals(qry.getPassword())) {
            throw PlatformException.buildException(ErrorCode.USER_NOT_EXIST_OR_PASSWORD_ERROR);
        }
        if (platformUserDO.getOpenFlag() != Constant.OPEN_STATUS) {
            throw PlatformException.buildException(ErrorCode.USER_DISABLE_STATUS);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(platformUserDO, userDTO);
        return userDTO;
    }

    public UserDTO detail(Integer id) {
       PlatformUserDO platformUserDO= platformUserMapper.selectByPrimaryKey(id);
       if(platformUserDO==null){
           return null;
       }
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(platformUserDO,userDTO);
        return userDTO;
    }
}
