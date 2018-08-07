package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.dao.subaccount.DistributorSubAccountUserConfigDOMapper;
import com.bat.distributor.dao.subaccount.co.SubAccountUserConfigCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.DistributorCommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorSubAccountUserConfigQryExe {

    @Autowired
    private DistributorSubAccountUserConfigDOMapper distributorSubAccountUserConfigDOMapper;


    public DistributorSubAccountUserConfigDO getById(Integer id) {
        if(id ==null){
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_ID_NULL);
        }
        DistributorSubAccountUserConfigDO userConfigDO = distributorSubAccountUserConfigDOMapper.selectByPrimaryKey(id);
        if(userConfigDO ==null){
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        if(DistributorCommonConstant.COMMON_DELETE_FLAG_YES.equals(userConfigDO.getDeleteFlag())){
            //数据已被删除
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_DATA_DEL_ALREADY);
        }
        return userConfigDO;
    }

    public List<SubAccountUserConfigCO> listCOByDistributorId(String distributorId) {
        return distributorSubAccountUserConfigDOMapper.listCOByDistributorId(distributorId);
    }

    public List<SubAccountUserConfigCO> listCOByCondition(Short searchType, String content,List<Integer> idList) {
        return distributorSubAccountUserConfigDOMapper.listCOByCondition(searchType,content,idList);
    }

    public List<DistributorSubAccountUserConfigDO> listByCondition(Integer distributorId) {
        return distributorSubAccountUserConfigDOMapper.listByCondition(distributorId);
    }
}
