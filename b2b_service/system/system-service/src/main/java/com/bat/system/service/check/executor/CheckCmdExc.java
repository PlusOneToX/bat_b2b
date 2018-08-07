package com.bat.system.service.check.executor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.check.dto.CheckConfigUpdateCmd;
import com.bat.system.dao.check.CheckConfigMapper;
import com.bat.system.dao.check.dataobject.CheckConfigDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class CheckCmdExc {

    @Resource
    private CheckConfigMapper checkConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean editCheckConfig(CheckConfigUpdateCmd cmd) {
        List<CheckConfigDO> checkConfigs = checkConfigMapper.getConfigsByExt(cmd.getExt());
        if (checkConfigs != null) {
            checkConfigs.forEach(checkConfigDO -> {
                checkConfigMapper.deleteByPrimaryKey(checkConfigDO.getId());
            });
        }
        String[] users = cmd.getCheckUsers().split(",");
        Date date = new Date();
        for (int i = 0; i < users.length; i++) {
            CheckConfigDO config = new CheckConfigDO();
            config.setExt(cmd.getExt());
            if (StringUtils.isNotBlank(users[i])) {
                config.setCheckUser(Integer.valueOf(users[i]));
            }
            config.setCheckOrder(i + 1);
            config.setCreateTime(date);
            config.setUpdateTime(date);
            config.setOpenFlag(cmd.getOpenFlag());
            checkConfigMapper.insert(config);
        }
        return true;
    }
}
