package com.bat.goods.service.common;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;

@Component
public class CommonRpcQryExe {

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 根据分销商ids获取分销商名称列表
     * 
     * @param distributorIds
     * @return
     */
    public List<DistributorNameRpcDTO> getDistributorNameByDistributorIds(List<Integer> distributorIds) {
        Response<List<DistributorNameRpcDTO>> response =
            distributorServiceRpc.getDistributorNameByDistributorIds(distributorIds);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            return null;
        }
    }

    /**
     * 根据分销商分组ids获取分销商分组列表
     *
     * @param distributorGroupIds
     * @return
     */
    public List<DistributorGroupRpcDTO> getDistributorGroupByDistributorGroupIds(List<Integer> distributorGroupIds) {
        Response<List<DistributorGroupRpcDTO>> response =
            distributorServiceRpc.getDistributorGroupByDistributorGroupIds(distributorGroupIds);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            return null;
        }
    }

}
