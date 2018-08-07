package com.bat.system.service.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.bat.system.api.user.PermissionService;
import com.bat.system.api.user.dto.PermissionCreateCmd;
import com.bat.system.api.user.dto.PermissionQry;
import com.bat.system.api.user.dto.PermissionUpdateCmd;
import com.bat.system.api.user.dto.data.PermissionDTO;
import com.bat.system.api.user.dto.data.PermissionSyncDTO;
import com.bat.system.service.user.executor.PermissionCmdExc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.system.service.user.executor.PermissionQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionQryExc permissionQryExc;

    @Resource
    private PermissionCmdExc permissionCmdExc;

    @Override
    public boolean createPermission(PermissionCreateCmd cmd) {
        return permissionCmdExc.createPermission(cmd);
    }

    @Override
    public PermissionDTO getPermissionById(Integer id) {
        return permissionQryExc.getPermissionById(id);
    }

    @Override
    public PageInfo<PermissionDTO> listPermission(PermissionQry qry) {
        return permissionQryExc.listPermission(qry);
    }

    @Override
    public boolean deletePermissionById(Integer id) {
        return permissionCmdExc.deletePermissionById(id);
    }

    @Override
    public boolean updatePermission(PermissionUpdateCmd cmd) {
        return permissionCmdExc.updatePermission(cmd);
    }

    @Override
    public void syncPermission(String gateWaySwaggerUrl) {
        permissionCmdExc.syncPermission(gateWaySwaggerUrl);
    }

    @Override
    public List<PermissionSyncDTO> listPermissionTree() {
        return permissionQryExc.listPermissionTree();
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 手动开启 redis 事务进行权限列表的全量更新
     */
    @Override
    public void syncCache() {
        List<Map<String, String>> permissionUserMapping = permissionQryExc.getPermissionUserMapping();
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                Set keys = redisOperations.keys("url:*");
                if (!keys.isEmpty()) {
                    redisOperations.delete(keys);
                }
                permissionUserMapping.forEach(map -> {
                    String url = "url:" + map.get("url");
                    String[] userids = map.get("userids").split(",");
                    Long add = redisTemplate.opsForSet().add(url, userids);
                });
                return redisOperations.exec();
            }
        });
    }
}