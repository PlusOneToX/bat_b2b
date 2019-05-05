package com.bat.dubboapi.system.organization.api;

import java.util.List;

import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.dubboapi.system.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 14:28
 */
public interface SystemOrganizationServiceRpc {
    /**
     * 根据组织id 查询组织信息
     * 
     * @return
     */
    Response<List<OrganizationRpcDTO>> listOrganizationInfoByOrganizationIds(List<Integer> organizationIds);
}
