package com.bat.thirdparty.order.executor.log;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.dao.log.OrderBusinessLogDOMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderBusinessLogCmdExe {

    @Autowired
    private OrderBusinessLogDOMapper orderBusinessLogDOMapper;

    public void create(OrderBusinessLogDO receiveLogDO) {
        receiveLogDO.setDeleteFlag(ThirdCommonConstant.COMMON_DEL_FLAG_NO);
        if(StringUtils.isNotBlank(receiveLogDO.getOtherOrderNo())){
            //将相同日志类型和相同的单号的设置为已删除
            this.deleteByOtherOrderNoAndLogTypeAndDistributorId(receiveLogDO.getOtherOrderNo(),receiveLogDO.getLogType(),
                    receiveLogDO.getDistributorId(),null);
        }
        orderBusinessLogDOMapper.insert(receiveLogDO);
    }

    /**
     *
     * @param orderBusinessLogDO
     * @param deleteOtherFlag 是否删除同单号的其他请求、true是 false 否
     */
    public void update(OrderBusinessLogDO orderBusinessLogDO,Boolean deleteOtherFlag) {
        if(StringUtils.isNotBlank(orderBusinessLogDO.getOtherOrderNo()) && deleteOtherFlag){
            //将相同日志类型和相同的单号的设置为已删除
            this.deleteByOtherOrderNoAndLogTypeAndDistributorId(orderBusinessLogDO.getOtherOrderNo(),orderBusinessLogDO.getLogType(),
                    orderBusinessLogDO.getDistributorId(),orderBusinessLogDO.getId());
        }
        orderBusinessLogDOMapper.updateByPrimaryKey(orderBusinessLogDO);
    }

    /**
     *
     * @param otherOrderNo 第三方单号
     * @param logType 日志类型
     * @param distributorId 分销商id
     * @param excludeId 排除不删除的id
     */
    public void deleteByOtherOrderNoAndLogTypeAndDistributorId(String otherOrderNo, Short logType,Integer distributorId,Integer excludeId) {
        orderBusinessLogDOMapper.deleteByOtherOrderNoAndLogTypeAndDistributorId(otherOrderNo,logType,distributorId,excludeId);
    }
}
