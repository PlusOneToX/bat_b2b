package com.bat.flexible.manager.third.executor;

import com.bat.flexible.dao.third.ThirdCourierContrastDOMapper;
import com.bat.flexible.dao.third.dataobject.ThirdCourierContrastDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThirdCourierContrastQryExe {

    @Autowired
    private ThirdCourierContrastDOMapper thirdCourierContrastDOMapper;



    public ThirdCourierContrastDO getByDistributorIdAndDistributionKdnCode(Integer distributorId, String expressCode) {
        return thirdCourierContrastDOMapper.getByDistributorIdAndDistributionKdnCode(distributorId,expressCode);
    }
}
