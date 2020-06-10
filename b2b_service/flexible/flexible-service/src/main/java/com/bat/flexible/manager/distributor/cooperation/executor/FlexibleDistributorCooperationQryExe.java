package com.bat.flexible.manager.distributor.cooperation.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.distributor.FlexibleDistributorCooperationDOMapper;
import com.bat.flexible.dao.distributor.dataobject.FlexibleDistributorCooperationDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlexibleDistributorCooperationQryExe {

    @Autowired
    private FlexibleDistributorCooperationDOMapper flexibleDistributorCooperationDOMapper;


    public List<FlexibleDistributorCooperationDO> listByOpenFlagAndContent(Short openFlag, String content) {
        
        return flexibleDistributorCooperationDOMapper.listByOpenFlagAndContent(openFlag,content);
    }

    public Integer findMaxSequence() {
        return flexibleDistributorCooperationDOMapper.findMaxSequence();
    }

    public FlexibleDistributorCooperationDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        FlexibleDistributorCooperationDO flexibleDistributorCooperationDO = flexibleDistributorCooperationDOMapper.selectByPrimaryKey(id);
        if(flexibleDistributorCooperationDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if(FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(flexibleDistributorCooperationDO.getDelFlag())){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return flexibleDistributorCooperationDO;
    }
}
