package com.bat.platform.service.user.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.user.dto.UserCmd;
import com.bat.platform.dao.user.PlatformUserMapper;
import com.bat.platform.dao.user.dataobject.PlatformUserDO;
import com.bat.platform.service.common.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/4 16:01
 */
@Component
public class UserCmdExe {

    @Resource
    private PlatformUserMapper platformUserMapper;

    public void add(UserCmd cmd) {
        PlatformUserDO platformUserDO=new PlatformUserDO();
        BeanUtils.copyProperties(cmd,platformUserDO);
        Date date=new Date();
        platformUserDO.setCreateTime(date);
        platformUserDO.setUpdateTime(date);
        try {
            platformUserMapper.insert(platformUserDO);
        }catch (DuplicateKeyException e){
            throw PlatformException.buildException(ErrorCode.SAME_NAME_ERROR);
        }
    }

    public void update(UserCmd cmd) {
        PlatformUserDO platformUserDO = new PlatformUserDO();
        BeanUtils.copyProperties(cmd, platformUserDO);
        platformUserDO.setUpdateTime(new Date());
        if (platformUserDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        try {
            platformUserMapper.updateByPrimaryKeySelective(platformUserDO);
        } catch (DuplicateKeyException e) {
            throw PlatformException.buildException(ErrorCode.SAME_NAME_ERROR);
        }
    }

    public void deleteById(Integer id) {
        platformUserMapper.deleteByPrimaryKey(id);
    }
}
