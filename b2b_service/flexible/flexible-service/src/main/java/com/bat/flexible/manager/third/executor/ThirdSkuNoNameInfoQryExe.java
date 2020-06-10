package com.bat.flexible.manager.third.executor;

import com.bat.flexible.dao.third.ThirdSkuNoNameInfoDOMapper;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThirdSkuNoNameInfoQryExe {

    @Autowired
    private ThirdSkuNoNameInfoDOMapper thirdSkuNoNameInfoDOMapper;


    public List<ThirdSkuNoNameInfoDO> listByDistributorId(Integer distributorId) {
        return thirdSkuNoNameInfoDOMapper.listByDistributorId(distributorId);
    }
}
