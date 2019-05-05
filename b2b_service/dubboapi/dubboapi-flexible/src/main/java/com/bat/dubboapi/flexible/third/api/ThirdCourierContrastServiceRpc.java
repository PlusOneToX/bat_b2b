package com.bat.dubboapi.flexible.third.api;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.third.dto.ThirdCourierContrastRpcDTO;

public interface ThirdCourierContrastServiceRpc {

    /**
     * 根据分销商id和快递公司编码查询三方快递公司对照关系
     * @param distributorId
     * @param distributionKdnCode
     * @return
     */
    Response<ThirdCourierContrastRpcDTO> getByDistributorIdAndDistributionKdnCode(Integer distributorId, String distributionKdnCode);

}
