package com.bat.system.service.user.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.user.dto.UserCreateCmd;
import com.bat.system.api.user.dto.UserUpdateCmd;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.dao.user.dataobject.UserDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class UserConvertor {

    public static UserDO toUserDO(UserCreateCmd cmd) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(cmd, userDO);
        Date date = new Date();
        userDO.setCreateTime(date);
        userDO.setUpdateTime(date);
        return userDO;
    }

    public static UserDO toUserDO(UserUpdateCmd cmd) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(cmd, userDO);
        Date date = new Date();
        userDO.setUpdateTime(date);
        return userDO;
    }

    public static UserDTO toUserDTO(UserDO userDO) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDO, userDTO);
        return userDTO;
    }

    public static List<UserDTO> toUserDTOList(List<UserDO> userDOList) {
        return userDOList.stream().map(userDO -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userDO, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }
}
