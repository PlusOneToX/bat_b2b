package com.bat.flexible.manager.third;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.third.executor.ThirdCourierContrastQryExe;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.third.api.ThirdCourierContrastServiceRpc;
import com.bat.dubboapi.flexible.third.dto.ThirdCourierContrastRpcDTO;
import com.bat.flexible.dao.third.dataobject.ThirdCourierContrastDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ThirdCourierContrastServiceRpcImpl implements ThirdCourierContrastServiceRpc {

    @Autowired
    private ThirdCourierContrastQryExe thirdCourierContrastQryExe;

    @Override
    public Response<ThirdCourierContrastRpcDTO> getByDistributorIdAndDistributionKdnCode(Integer distributorId, String distributionKdnCode) {
        ThirdCourierContrastDO thirdCourierContrastDO = thirdCourierContrastQryExe.getByDistributorIdAndDistributionKdnCode(distributorId, distributionKdnCode);
        return Response.of(BeanUtils.copy(thirdCourierContrastDO,ThirdCourierContrastRpcDTO.class));
    }
}
