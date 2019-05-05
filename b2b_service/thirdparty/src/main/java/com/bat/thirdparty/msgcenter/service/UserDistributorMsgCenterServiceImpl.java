package com.bat.thirdparty.msgcenter.service;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.msgcenter.service.executor.UserDistributorMsgCenterQryExe;
import com.bat.thirdparty.msgcenter.api.UserDistributorMsgCenterServiceI;
import com.bat.thirdparty.msgcenter.api.dto.DistributorMsgTemplateQry;
import com.bat.thirdparty.msgcenter.api.dto.MsgCenterWechatTemplateDTO;
import com.bat.thirdparty.msgcenter.api.dto.UserDistributorMsgCenterLogDTO;
import com.bat.thirdparty.msgcenter.service.executor.UserDistributorMsgCenterCmdExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDistributorMsgCenterServiceImpl implements UserDistributorMsgCenterServiceI {

    @Autowired
    private UserDistributorMsgCenterQryExe userDistributorMsgCenterQryExe;

    @Autowired
    private UserDistributorMsgCenterCmdExe userDistributorMsgCenterCmdExe;

    @Override
    public PageInfo<UserDistributorMsgCenterLogDTO> list(Integer distributorId) {
        return userDistributorMsgCenterQryExe.list(distributorId);
    }

    @Override
    public Integer notReadNum(Integer distributorId) {
        return userDistributorMsgCenterQryExe.notReadNum(distributorId);
    }

    @Override
    public void read(Integer distributorId, Integer logId) {
        userDistributorMsgCenterCmdExe.read(distributorId,logId);
    }

    @Override
    public void readAll(Integer distributorId) {
        userDistributorMsgCenterCmdExe.readAll(distributorId);
    }

    @Override
    public List<MsgCenterWechatTemplateDTO> wechatTemplateList(DistributorMsgTemplateQry qry) {
        return userDistributorMsgCenterQryExe.wechatTemplateList(qry);
    }
}
