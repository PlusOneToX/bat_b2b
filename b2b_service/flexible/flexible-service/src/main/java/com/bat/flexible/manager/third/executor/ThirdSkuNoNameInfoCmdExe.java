package com.bat.flexible.manager.third.executor;

import com.bat.flexible.dao.third.ThirdSkuNoNameInfoDOMapper;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThirdSkuNoNameInfoCmdExe {

    @Autowired
    private ThirdSkuNoNameInfoDOMapper thirdSkuNoNameInfoDOMapper;


    public void deleteByDistributorId(Integer distributorId) {
        thirdSkuNoNameInfoDOMapper.deleteByDistributorId(distributorId);
    }

    public void create(ThirdSkuNoNameInfoDO info) {
        thirdSkuNoNameInfoDOMapper.insert(info);
    }
}
