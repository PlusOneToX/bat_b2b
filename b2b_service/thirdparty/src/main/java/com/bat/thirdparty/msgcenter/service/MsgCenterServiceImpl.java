package com.bat.thirdparty.msgcenter.service;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.service.executor.MsgCenterQryExe;
import com.bat.thirdparty.msgcenter.api.MsgCenterServiceI;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.service.executor.MsgCenterCmdExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgCenterServiceImpl implements MsgCenterServiceI {

    @Autowired
    private MsgCenterQryExe msgCenterQryExe;

    @Autowired
    private MsgCenterCmdExe msgCenterCmdExe;

    @Override
    public PageInfo<MsgCenterDTO> list(MsgCenterQry qry) {
        return msgCenterQryExe.list(qry);
    }

    @Override
    public MsgCenterDTO detail(Integer id) {
        return msgCenterQryExe.detail(id);
    }

    @Override
    public void delete(Integer id) {
        msgCenterCmdExe.delete(id);
    }

    @Override
    public void add(MsgCenterCmd cmd) {
        msgCenterCmdExe.add(cmd);
    }

    @Override
    public void update(MsgCenterCmd cmd) {
        msgCenterCmdExe.update(cmd);
    }

    @Override
    public PageInfo<MsgCenterLogDTO> logList(MsgCenterLogQry qry) {
        return msgCenterQryExe.logList(qry);
    }

    @Override
    public List<MsgCenterWechatTemplateDTO> wechatTemplateList() {
        return msgCenterQryExe.wechatTemplateList();
    }

    @Override
    public void updateWechatTemplate(List<MsgCenterWechatTemplateCmd> cmds) {
        msgCenterCmdExe.updateWechatTemplate(cmds);
    }

    @Override
    public void logSend(Integer logId) {
        msgCenterCmdExe.logSend(logId);
    }
}
