package com.bat.thirdparty.msgcenter.service.executor;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import com.bat.thirdparty.msgcenter.dao.MsgCenterLogMapper;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class UserDistributorMsgCenterCmdExe {

    @Autowired
    private MsgCenterLogMapper msgCenterLogMapper;


    public void read(Integer distributorId, Integer logId) {
        MsgCenterLogDO msgCenterLogDO = msgCenterLogMapper.selectByPrimaryKey(logId);
        if (msgCenterLogDO == null) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_ID_ERROR);
        }
        if (msgCenterLogDO.getToUserId() != distributorId.intValue()
                || msgCenterLogDO.getUserType() != MsgCenterConstant.USR_TYPE_B2B) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_FAIL);
        }
        msgCenterLogDO.setReadFlag(ThirdCommonConstant.COMMON_FLAG_YES);
        Date date=new Date();
        msgCenterLogDO.setReadTime(date);
        msgCenterLogDO.setUpdateTime(date);
        msgCenterLogMapper.updateByPrimaryKey(msgCenterLogDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void readAll(Integer distributorId) {
        msgCenterLogMapper.readAll(distributorId,MsgCenterConstant.USR_TYPE_B2B,new Date());
    }
}
