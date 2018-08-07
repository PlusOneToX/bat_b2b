package com.bat.system.api.oss;

import com.bat.system.api.oss.dto.data.AssumeRoleDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/11 21:30
 */
public interface OssService {
    /**
     * 获取sts授权
     * 
     * @param userId
     * @return
     */
    AssumeRoleDTO getSts(String userId);
}
