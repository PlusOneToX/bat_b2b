package com.bat.system.service.common;

import java.util.List;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.common.CommonService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 通用的一些工具
 * @date: 2018/6/9 23:18
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @DubboReference(check = false, timeout = 30000)
    private SystemUserServiceRpc systemUserServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 获取当前用户下的用户列表
     *
     * @param userId
     * @return
     */
    public List<Integer> getOwnSaleIds(Integer userId) {
        List<Integer> ownSaleIds = null;
        Response<List<Integer>> response = systemUserServiceRpc.findOwnSaleIds(userId);
        if (response.isSuccess()) {
            ownSaleIds = response.getData();
        } else {
            throw SystemException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return ownSaleIds;
    }

    /**
     * 获取业务员对应的分销商id集合
     *
     * @param userId
     * @return
     */
    public List<Integer> getDistributorIds(Integer userId) {
        List<Integer> ownSaleIds = getOwnSaleIds(userId);
        List<Integer> distributorId = null;
        if (CollectionUtils.isNotEmpty(ownSaleIds)) {
            distributorId = distributorServiceRpc.getDistributorIdsBySalesIds(ownSaleIds);
        }
        return distributorId;
    }

    /**
     * 获取分销商信息
     * 
     * @param distributorId
     * @return
     */
    public DistributorRpcDTO getDistributorInfo(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> response =
            distributorServiceRpc.distributorById(distributorId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw SystemException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取组织信息
     * 
     * @param distributorId
     * @return
     */
    public OrganizationRpcDTO getOrganizationInfo(Integer distributorId) {
        DistributorRpcDTO data = getDistributorInfo(distributorId);
        return getOrganizationInfoBySaleId(data.getSalesId());
    }

    /**
     * 通过业务员id获取业务员所属组织id
     * 
     * @param sealId
     * @return
     */
    public OrganizationRpcDTO getOrganizationInfoBySaleId(Integer sealId) {
        Response<UserRpcDTO> response = systemUserServiceRpc.getUserById(sealId);
        if (response.isSuccess()) {
            return response.getData().getOrganizationDTO();
        } else {
            throw SystemException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取组织id
     * 
     * @param distributorId
     * @return
     */
    public Integer getOrganizationId(Integer distributorId) {
        return getOrganizationInfo(distributorId).getId();
    }

}
