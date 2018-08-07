package com.bat.system.service.check.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.system.api.check.dto.data.CheckConfigDTO;
import com.bat.system.dao.check.CheckConfigMapper;
import com.bat.system.dao.check.dataobject.CheckConfigDO;
import com.bat.system.dao.user.UserMapper;
import com.bat.system.dao.user.dataobject.UserDO;
import org.springframework.stereotype.Component;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class CheckQryExc {

    @Resource
    private CheckConfigMapper checkConfigMapper;

    @Resource
    private UserMapper userMapper;

    public List<CheckConfigDTO> checkDetail(Short ext) {
        // 获取所有的审批配置
        List<CheckConfigDTO> resultList = new ArrayList<CheckConfigDTO>();
        List<CheckConfigDO> checkConfigs = checkConfigMapper.listAll(ext);
        Map<Short, List<CheckConfigDO>> checkConfigMap = new HashMap<>(16);
        if (!checkConfigs.isEmpty()) {
            checkConfigs.forEach(checkConfigDO -> {
                // 根据审核人id 关联审核人名称
                UserDO userDO = userMapper.selectByPrimaryKey(checkConfigDO.getCheckUser());
                if (userDO == null) {
                    checkConfigDO.setCheckUserName("unknown");
                } else {
                    checkConfigDO.setCheckUserName(userDO.getRealName());
                }
                // map中 相同类型的审批单 放在map 同一个key 所对应的list中
                if (checkConfigMap.containsKey(checkConfigDO.getExt())) {
                    List<CheckConfigDO> list = checkConfigMap.get(checkConfigDO.getExt());
                    list.add(checkConfigDO);
                } else {
                    // 没有key 新增
                    List<CheckConfigDO> list = new ArrayList<>();
                    list.add(checkConfigDO);
                    checkConfigMap.put(checkConfigDO.getExt(), list);
                }
            });
        }

        // 如果集合
        for (Short key : checkConfigMap.keySet()) {
            CheckConfigDTO bean = new CheckConfigDTO();
            List<CheckConfigDO> list = checkConfigMap.get(key);
            short isOpen = 1;
            if (checkConfigs.isEmpty()) {
                isOpen = 0;
            } else {
                boolean b = list.stream().anyMatch(checkConfigDO -> checkConfigDO.getOpenFlag() == 0);
                if (b) {
                    isOpen = 0;
                }
            }
            bean.setExt(key);
            bean.setOpenFlag(isOpen);
            bean.setCheckConfigs(list);
            resultList.add(bean);
        }
        return resultList;
    }
}
