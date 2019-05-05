package com.bat.thirdparty.order.executor.log;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.dao.log.OrderBusinessLogDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OrderBusinessLogQryExe {

    @Autowired
    private OrderBusinessLogDOMapper orderBusinessLogDOMapper;







    public List<OrderBusinessLogDO> listByCondition(Short logType, Date startTime, Date endTime, Short status, String content,String otherOrderNo,Short searchType,
                                                    List<String> platformList,Integer distributorId) {
        return orderBusinessLogDOMapper.listByCondtion(logType,startTime,endTime,status,content,otherOrderNo,searchType,platformList,distributorId);
    }

    public OrderBusinessLogDO getById(Integer id) {
        OrderBusinessLogDO orderBusinessLogDO = orderBusinessLogDOMapper.selectByPrimaryKey(id);
        if(orderBusinessLogDO ==null){
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_ID_ERROR);
        }
        if(ThirdCommonConstant.COMMON_DEL_FLAG_YES.equals(orderBusinessLogDO.getDeleteFlag())){
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_DATA_DEL_ALREADY);
        }
        return orderBusinessLogDO;
    }

    public List<OrderBusinessLogDO> listReceiveOrderByCondition(Date startTime, Date endTime, Short status, String content, String otherOrderNo, Short searchType, String platform,
                                                                Integer distributorId) {
        return orderBusinessLogDOMapper.listReceiveOrderByCondition(startTime,endTime,status,content,otherOrderNo,searchType,platform,distributorId);
    }


}
