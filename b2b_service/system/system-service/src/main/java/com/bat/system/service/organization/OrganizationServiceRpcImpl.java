package com.bat.system.service.organization;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.MessageUtils;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import com.bat.system.service.organization.executor.ErrorCode;
import com.bat.system.service.organization.executor.OrganizationQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.api.SystemOrganizationServiceRpc;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 20:11
 */
@DubboService
@Slf4j
public class OrganizationServiceRpcImpl implements SystemOrganizationServiceRpc {
    @Resource
    private OrganizationQryExc organizationQryExc;

    @Override
    public Response<List<OrganizationRpcDTO>> listOrganizationInfoByOrganizationIds(List<Integer> organizationIds) {
        List<OrganizationDO> organizationDOS = organizationQryExc.listByOrganizationIds(organizationIds);
        if (organizationDOS != null) {
            List<OrganizationRpcDTO> collect = organizationDOS.stream().map(organizationDO -> {
                OrganizationRpcDTO organizationRpcDTO = new OrganizationRpcDTO();
                BeanUtils.copyProperties(organizationDO, organizationRpcDTO);
                return organizationRpcDTO;
            }).collect(Collectors.toList());
            return Response.of(collect);
        }
        return Response.buildFailure(ErrorCode.B_ORGANIZATION_ID_NOT_EXISTS,
            MessageUtils.get(ErrorCode.B_ORGANIZATION_ID_NOT_EXISTS));
    }
}
